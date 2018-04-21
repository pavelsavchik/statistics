package me.savchik.statistics.entity;

import static java.lang.System.currentTimeMillis;

public class Transaction {

    private double amount;

    private long time;



    public Transaction(double amount, long time) {
        this.amount = amount;
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isOlderThanMinute() {
        long currentTimeMillis = currentTimeMillis();
        return !(currentTimeMillis >= time && currentTimeMillis - time < 60000);
    }
}
