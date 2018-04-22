package me.savchik.statistics.dto;

import javax.validation.constraints.NotNull;

public class TransactionCreateRequest {

    @NotNull
    private Double amount;

    @NotNull
    private Long timestamp;

    public TransactionCreateRequest() { }

    public TransactionCreateRequest(Double amount, Long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
