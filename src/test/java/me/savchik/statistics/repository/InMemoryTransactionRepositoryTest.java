package me.savchik.statistics.repository;

import me.savchik.statistics.entity.Transaction;
import org.junit.Test;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository = new InMemoryTransactionRepository();

    private void addTransaction() {
        addTransaction(10.);
    }

    private void addTransaction(Double amount) {
        addTransaction(amount, System.currentTimeMillis());
    }

    private void addTransaction(Double amount, Long timestamp) {
        repository.addTransaction(new Transaction(amount, timestamp));
    }

    @Test
    public void getLastMinuteStatistics_normalCase_valuesAreRight() throws InterruptedException {
        addTransaction(1.5);
        addTransaction(2.4);
        addTransaction(3.6);

        Thread.sleep(1000);
        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(7.5);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(2.5);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(3);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(3.6);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(1.5);
    }

    @Test
    public void getLastMinuteStatistics_noTransactions_valuesAreZero() throws InterruptedException {
        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(0);
    }

    @Test
    public void getLastMinuteStatistics_concurrent_valuesAreRight() throws InterruptedException {
        Runnable task = () -> {
            for(int i = 0; i < 10000; i++) {
                addTransaction((double)i);
            }
        };
        allOf(runAsync(task), runAsync(task), runAsync(task), runAsync(task), runAsync(task)).join();

        Thread.sleep(1000);

        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(249975000);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(4999.5);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(50000);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(9999);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(0);
    }

    @Test
    public void getLastMinuteStatistics_expiredTimestamps_valuesAreZero() throws InterruptedException {
        Long expiredTimestamp = System.currentTimeMillis() - 61000;
        addTransaction(1.5, expiredTimestamp);
        addTransaction(2.4, expiredTimestamp);
        addTransaction(3.6, expiredTimestamp);

        Thread.sleep(1000);
        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(0);
    }

    @Test
    public void getLastMinuteStatistics_timestampExpiration_valuesAreRemoved() throws InterruptedException {
        Long actualTimestamp = System.currentTimeMillis() - 58000;
        addTransaction(1.5, actualTimestamp);
        addTransaction(2.4, actualTimestamp);
        addTransaction(3.6, actualTimestamp);

        Thread.sleep(1000);
        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(7.5);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(2.5);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(3);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(3.6);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(1.5);

        Thread.sleep(2000);
        assertThat(repository.getLastMinuteStatistics().getSum()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getAvg()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getCount()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMax()).isEqualTo(0);
        assertThat(repository.getLastMinuteStatistics().getMin()).isEqualTo(0);
    }

}
