package me.savchik.statistics.service;

import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.mapper.TransactionMapper;
import me.savchik.statistics.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransactionMapper transactionMapper;

    public boolean create(@Valid TransactionCreateRequest createRequest) {
        return repository.addTransaction(transactionMapper.requestToTransaction(createRequest));
    }

}