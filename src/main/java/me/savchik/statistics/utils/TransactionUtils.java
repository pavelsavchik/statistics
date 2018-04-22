package me.savchik.statistics.utils;

import me.savchik.statistics.entity.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.lang.System.currentTimeMillis;

@Service
public class TransactionUtils {

    private Long expirationPeriod;

    public TransactionUtils(@Value("${transaction.expirationPeriod.ms}") Long expirationPeriod) {
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