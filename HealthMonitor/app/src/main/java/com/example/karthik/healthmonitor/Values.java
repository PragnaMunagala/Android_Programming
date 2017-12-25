package com.example.karthik.healthmonitor;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by karthik on 9/26/17.
 */
//Getters and Setters for database values
public class Values {
    private double _yValue;
    private double _xValue;
    private double _zValue;
    private long timestamp;
    public Values() {

    }

    public Values(double _xValue ,double _yValue, double _zValue,long timestamp) {
        this.timestamp = timestamp;
        this._xValue = _xValue;
        this._yValue = _yValue;
        this._zValue = _zValue;
    }

    public void setzValue(float _zValue) {
        this._zValue = _zValue;
    }

    public double getzValue() {
        return this._zValue;
    }


    public void setyValue(float yValue) {
        this._yValue = yValue;
    }

    public double getyValue() {
        return this._yValue;
    }

    public void setxValue(float _xValue) {
        this._xValue = _xValue;
    }

    public double getxValue() {
        return this._xValue;
    }

    public void setTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
