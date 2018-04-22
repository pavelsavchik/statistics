package me.savchik.statistics.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        this.sum = round(statistics.getSum());
        this.avg = round(statistics.getAverage());
        this.max = round(statistics.getMax());
        this.min = round(statistics.getMin());
        this.count = statistics.getCount();
    }

    public Statistics(Double sum, Double avg, Double max, Double min, Long count) {
        this.sum = round(sum);
        this.avg = round(avg);
        this.max = round(max);
        this.min = round(min);
        this.count = count;
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

    private Double round(Double value) {
        BigDecimal rounded = new BigDecimal(value);
        rounded = rounded.setScale(3, RoundingMode.HALF_UP);
        return rounded.doubleValue();

    }

}
