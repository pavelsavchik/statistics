package me.savchik.statistics.mapper;

import me.savchik.statistics.dto.TransactionCreateRequest;
import me.savchik.statistics.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction requestToTransaction(TransactionCreateRequest request) {
        return new Transaction(request.getAmount(), request.getTimestamp());
    }

}