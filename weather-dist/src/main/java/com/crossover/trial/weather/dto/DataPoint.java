package com.crossover.trial.weather.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    private double mean;
    private int first, second, third, count;

    // CR: mean should be double
    // CR: bad (unlogical) arguments order: (first, second, third, mean, count) is much better
    public DataPoint(int first, int second, int mean, int third, int count) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.mean = mean;
        this.count = count;
    }

    /** the mean of the observations */
    public double getMean() {
        return mean;
    }

    // CR: Class is meant to be immutable, setters violates this
    /** 1st quartile -- useful as a lower bound */
    public int getFirst() {
        return first;
    }

    // CR: Class is meant to be immutable, setters violates this
    /** 2nd quartile -- median value */
    public int getSecond() {
        return second;
    }

    // CR: Class is meant to be immutable, setters violates this
    /** 3rd quartile value -- less noisy upper value */
    public int getThird() {
        return third;
    }

    /** the total number of measurements */
    public int getCount() {
        return count;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataPoint dataPoint = (DataPoint) o;

        if (Double.compare(dataPoint.mean, mean) != 0) return false;
        if (first != dataPoint.first) return false;
        if (second != dataPoint.second) return false;
        if (third != dataPoint.third) return false;
        return count == dataPoint.count;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(mean);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + first;
        result = 31 * result + second;
        result = 31 * result + third;
        result = 31 * result + count;
        return result;
    }
}
