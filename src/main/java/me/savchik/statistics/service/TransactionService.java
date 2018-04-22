package me.savchik.statistics.service;

import me.savchik.statistics.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class TransactionService {

    private Long expirationPeriod;

    public TransactionService(@Value("${transaction.expirationPeriod.ms}") Long expirationPeriod) {
        this.expirationPeriod = expirationPeriod;
    }

    public boolean isExpired(Transaction transaction) {
        if(transaction == null || transaction.getTime() == null) {
            return false;
        }
        Long currentTimeMillis = currentTimeMillis();
        return !(currentTimeMillis >= transaction.getTime() && currentTimeMillis - transaction.getTime() < expirationPeriod);
    }

}