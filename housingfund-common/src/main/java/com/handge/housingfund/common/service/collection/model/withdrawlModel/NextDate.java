package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/9.
 * 描述
 */
public class NextDate implements Serializable {
    String nextDate;//下次提取日期

    public String getNextDate() {
        return nextDate;
    }

    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    @Override
    public String toString() {
        return "NextDate{" +
                "nextDate='" + nextDate + '\'' +
                '}';
    }
}
