package me.savchik.statistics.controller;

import me.savchik.statistics.entity.Statistics;
import me.savchik.statistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private TransactionRepository repository;

    @Autowired
    public StatisticsController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Statistics getStatistics() {
        return repository.getLastMinuteStatistics();
    }

}
