package me.savchik.statistics.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.savchik.statistics.dto.TransactionCreateRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void POST_normalCase_s() throws Exception {
        TransactionCreateRequest request = new TransactionCreateRequest(10D, System.currentTimeMillis());
        ResponseEntity result = restTemplate.postForEntity("/transactions", request, Object.class);
        assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

}
