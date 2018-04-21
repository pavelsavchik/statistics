package me.savchik.statistics.repository;


import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.entity.Transaction;

public interface TransactionRepository {

    boolean addTransaction(Transaction transaction);

    Statistics getLastMinuteStatistics();

}