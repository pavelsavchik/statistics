package me.savchik.statistics.entity;

import java.util.DoubleSummaryStatistics;

public class Statistics {

    private final Double sum;

    private final Double avg;

    private final Double max;

    private final Double min;

    private final Long count;

    public Statistics() {
        sum = 0D;
        avg = 0D;
        max = 0D;
        min = 0D;
        count = 0L;
    }

    public Statistics(DoubleSummaryStatistics statistics) {
        this.sum = statistics.getSum();
        this.avg = statistics.getAverage();
        this.max = statistics.getMax();
        this.min = statistics.getMin();
        this.count = statistics.getCount();
    }

    public Double getSum() {
        return sum;
    }

    public Double getAvg() {
        return avg;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }

    public Long getCount() {
        return count;
    }

}
