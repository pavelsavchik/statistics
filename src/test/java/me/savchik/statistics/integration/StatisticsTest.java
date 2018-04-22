package me.savchik.statistics.integration;

import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.entity.Statistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatisticsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void statisticsCalculation_transactionsWereAdded_valuesAreCorrect() throws Exception {
        for(int i = 0; i < 1000; i++) {
            TransactionCreateRequest request = new TransactionCreateRequest((double)i, System.currentTimeMillis());
            ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
            assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        }

        Thread.sleep(500);

        ResponseEntity result = restTemplate.getForEntity("/statistics", Statistics.class);
        Statistics statistics = (Statistics)result.getBody();
        assertThat(statistics.getSum()).isEqualByComparingTo(499500.0);
        assertThat(statistics.getMax()).isEqualByComparingTo(999.0);
        assertThat(statistics.getMin()).isEqualByComparingTo(0.0);
        assertThat(statistics.getCount()).isEqualByComparingTo(1000L);
        assertThat(statistics.getAvg()).isEqualByComparingTo(499.5);
    }

    @Test
    public void statisticsCalculation_transactionsWereAddedConcurrently_valuesAreCorrect() throws Exception {
        Runnable task = () -> {
            for(int i = 0; i < 1000; i++) {
                TransactionCreateRequest request = new TransactionCreateRequest((double)i, System.currentTimeMillis());
                ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
                assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
            }
        };

        allOf(runAsync(task), runAsync(task), runAsync(task), runAsync(task), runAsync(task)).join();

        Thread.sleep(500);

        ResponseEntity result = restTemplate.getForEntity("/statistics", Statistics.class);
        Statistics statistics = (Statistics)result.getBody();
        assertThat(statistics.getSum()).isEqualByComparingTo(2497500.0);
        assertThat(statistics.getMax()).isEqualByComparingTo(999.0);
        assertThat(statistics.getMin()).isEqualByComparingTo(0.0);
        assertThat(statistics.getCount()).isEqualByComparingTo(5000L);
        assertThat(statistics.getAvg()).isEqualByComparingTo(499.5);
    }

    @Test
    public void statisticsCalculation_valuesWithPeriod_valuesAreRounded() throws Exception {
        for(int i = 0; i < 10; i++) {
            TransactionCreateRequest request = new TransactionCreateRequest(1.5555555, System.currentTimeMillis());
            ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
            assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        }

        Thread.sleep(500);

        ResponseEntity result = restTemplate.getForEntity("/statistics", Statistics.class);
        Statistics statistics = (Statistics)result.getBody();
        assertThat(statistics.getSum()).isEqualByComparingTo(15.556);
        assertThat(statistics.getMax()).isEqualByComparingTo(1.556);
        assertThat(statistics.getMin()).isEqualByComparingTo(1.556);
        assertThat(statistics.getCount()).isEqualByComparingTo(10L);
        assertThat(statistics.getAvg()).isEqualByComparingTo(1.556);
    }

    @Test
    public void statisticsCalculation_someTransactionsAreExpired_expiredAreIgnored() throws Exception {
        for(int i = 0; i < 1000; i++) {
            Long timestamp = i % 2 == 0 ? System.currentTimeMillis() : System.currentTimeMillis() - 60000;
            HttpStatus expectedStatus = i % 2 == 0 ? HttpStatus.CREATED : HttpStatus.NO_CONTENT;
            TransactionCreateRequest request = new TransactionCreateRequest((double)i, timestamp);
            ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
            assertThat(result.getStatusCode()).isEqualByComparingTo(expectedStatus);
        }

        Thread.sleep(500);

        ResponseEntity result = restTemplate.getForEntity("/statistics", Statistics.class);
        Statistics statistics = (Statistics)result.getBody();
        assertThat(statistics.getSum()).isEqualByComparingTo(249500.0);
        assertThat(statistics.getMax()).isEqualByComparingTo(998.0);
        assertThat(statistics.getMin()).isEqualByComparingTo(0.0);
        assertThat(statistics.getCount()).isEqualByComparingTo(500L);
        assertThat(statistics.getAvg()).isEqualByComparingTo(499.0);
    }

    @Test
    public void statisticsCalculation_waitUntilTransactionsAreExpired_emptyStatistics() throws Exception {
        for(int i = 0; i < 1000; i++) {
            TransactionCreateRequest request = new TransactionCreateRequest((double)i, System.currentTimeMillis() - 58000);
            ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
            assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        }

        Thread.sleep(3000);

        ResponseEntity result = restTemplate.getForEntity("/statistics", Statistics.class);
        Statistics statistics = (Statistics)result.getBody();
        assertThat(statistics.getSum()).isEqualByComparingTo(0.0);
        assertThat(statistics.getMax()).isEqualByComparingTo(0.0);
        assertThat(statistics.getMin()).isEqualByComparingTo(0.0);
        assertThat(statistics.getCount()).isEqualByComparingTo(0L);
        assertThat(statistics.getAvg()).isEqualByComparingTo(0.0);

    }

}
