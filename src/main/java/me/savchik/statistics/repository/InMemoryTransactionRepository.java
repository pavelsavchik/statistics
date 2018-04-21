package me.savchik.statistics.repository;


import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

@Component
public class InMemoryTransactionRepository implements TransactionRepository {

    private List<Transaction> transactions;


    public InMemoryTransactionRepository() {
        this.transactions = new LinkedList<>();
    }

    @Override
    public boolean addTransaction(Transaction transaction) {
        if(!transaction.isOlderThanMinute()) {
            transactions.add(transaction);
            return true;
        }
        return false;
    }

    @Override
    public Statistics getLastMinuteStatistics() {
        return new Statistics();
    }

}
