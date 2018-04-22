package me.savchik.statistics.service;

import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.entity.Transaction;
import me.savchik.statistics.mapper.TransactionMapper;
import me.savchik.statistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private Long expirationPeriod;

    private TransactionRepository transactionRepository;

    private final TransactionMapper mapper;

    @Autowired
    public TransactionService(@Value("${transaction.expirationPeriod.ms}") Long expirationPeriod,
                              TransactionRepository transactionRepository,
                              TransactionMapper mapper) {
        this.expirationPeriod = expirationPeriod;
        this.transactionRepository = transactionRepository;
        this.mapper = mapper;
    }

    public boolean addTransaction(TransactionCreateRequest request) {
        Transaction transaction = mapper.requestToTransaction(request);
        return transactionRepository.addTransaction(transaction);
    }

}