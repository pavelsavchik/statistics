package me.savchik.statistics.entity;

import static java.lang.System.currentTimeMillis;

public class Transaction {

    private Double amount;

    private Long time;



    public Transaction(Double amount, Long time) {
        this.amount = amount;
        this.time = time;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isOlderThanMinute() {
        Long currentTimeMillis = currentTimeMillis();
        return !(currentTimeMillis >= time && currentTimeMillis - time < 60000);
    }
}
