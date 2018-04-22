package me.savchik.statistics.repository;

import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.entity.Transaction;
import me.savchik.statistics.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class InMemoryTransactionRepository implements TransactionRepository {

    private List<Transaction> transactions;

    private Statistics statistics;

    private final ReadWriteLock transactionsLock;

    private final ReadWriteLock statisticsLock;

    private final Timer timer;

    private final TransactionService service;

    @Autowired
    public InMemoryTransactionRepository(TransactionService service, @Value("${statistics.updatePeriod.ms}") Long updatePeriod) {
        this.service = service;
        this.timer = new Timer();
        this.transactionsLock = new ReentrantReadWriteLock();
        this.statisticsLock = new ReentrantReadWriteLock();
        this.transactions = new LinkedList<>();
        calculateStatistics();

        scheduleStatisticsCalculation(updatePeriod);
    }

    @Override
    public boolean addTransaction(Transaction transaction) {
        Lock transactionsWrite = transactionsLock.writeLock();

        transactionsWrite.lock();

        try {
            if (!service.isExpired(transaction)) {
                transactions.add(transaction);
                return true;
            }
            return false;
        } finally {
            transactionsWrite.unlock();
        }
    }

    @Override
    public Statistics getLastMinuteStatistics() {
        Lock statisticsRead = statisticsLock.readLock();

        statisticsRead.lock();
        try {
            return statistics;
        } finally {
            statisticsRead.unlock();
        }
    }

    private void scheduleStatisticsCalculation(Long updatePeriod) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                calculateStatistics();
            }
        }, 0, updatePeriod);
    }

    private void calculateStatistics() {
        Lock statisticsWrite = statisticsLock.writeLock();
        Lock transactionsWrite = transactionsLock.writeLock();

        statisticsWrite.lock();
        transactionsWrite.lock();

        try {
            cleanOld();
            statistics = transactions.size() > 0 ?
                    new Statistics(transactions.stream().mapToDouble(Transaction::getAmount).summaryStatistics()) :
                    new Statistics();
        } finally {
            statisticsWrite.unlock();
            transactionsWrite.unlock();
        }
    }

    private void cleanOld() {
        transactions.removeIf(service::isExpired);
    }

}
