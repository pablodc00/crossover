package com.crossover.trial.weather.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
public class AirportData {

    // CR: Should be private
    /** the three letter IATA code */
    private String iata;

    // CR: Should be private
    /** latitude value in degrees */
    private double latitude;

    // CR: Should be private
    /** longitude value in degrees */
    private double longitude;

    public AirportData() { }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirportData that = (AirportData) o;

        return iata != null ? iata.equals(that.iata) : that.iata == null;

    }

    @Override
    public int hashCode() {
        return iata != null ? iata.hashCode() : 0;
    }
}
