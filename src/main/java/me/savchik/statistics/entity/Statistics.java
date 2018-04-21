package me.savchik.statistics.entity;

import java.util.DoubleSummaryStatistics;

public class Statistics {

    private final double sum;

    private final double avg;

    private final double max;

    private final double min;

    private final long count;

    public Statistics() {
        sum = 0;
        avg = 0;
        max = 0;
        min = 0;
        count = 0;
    }

    public Statistics(DoubleSummaryStatistics statistics) {
        this.sum = statistics.getSum();
        this.avg = statistics.getAverage();
        this.max = statistics.getMax();
        this.min = statistics.getMin();
        this.count = statistics.getCount();
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public long getCount() {
        return count;
    }

}
