package me.savchik.statistics.service;

import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

    private TransactionRepository repository;

    @Autowired
    public StatisticsService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Statistics getStatistics() {
        return repository.getStatistics();
    }

}