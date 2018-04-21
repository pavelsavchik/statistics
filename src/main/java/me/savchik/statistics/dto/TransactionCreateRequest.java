package me.savchik.statistics.dto;

import javax.validation.constraints.NotNull;

public class TransactionCreateRequest {

    @NotNull
    private double amount;

    @NotNull
    private long timestamp;

    public TransactionCreateRequest(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
