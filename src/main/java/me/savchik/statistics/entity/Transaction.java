package me.savchik.statistics.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import static java.lang.System.currentTimeMillis;

public class Transaction {

    private BigDecimal amount;

    private Long time;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isOlderThanMinute() {
        long currentTimeMillis = currentTimeMillis();
        return currentTimeMillis >= time && currentTimeMillis - time < 60000;
    }
}
