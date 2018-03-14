package com.handge.housingfund.common.service.loan.model;

import java.util.Date;

/**
 * Created by Funnyboy on 2017/8/21.
 */
public class CurrentPeriodRange {
    private Date  beforeTime;
    private Date  afterTime;
    private int currentPeriod;

    public CurrentPeriodRange() {
    }

    public Date getBeforeTime() {
        return beforeTime;
    }

    public void setBeforeTime(Date beforeTime) {
        this.beforeTime = beforeTime;
    }

    public Date getAfterTime() {
        return afterTime;
    }

    public void setAfterTime(Date afterTime) {
        this.afterTime = afterTime;
    }

    public int getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(int currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    @Override
    public String toString() {
        return "CurrentPeriodRange{" +
                "beforeTime=" + beforeTime +
                ", afterTime=" + afterTime +
                ", currentPeriod=" + currentPeriod +
                '}';
    }
}
