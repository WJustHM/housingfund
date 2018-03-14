package com.handge.housingfund.common.service.finance.model;

import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-9-28.
 */
public class TimedTaskInfo implements Serializable {
    private String  voucherId ;

    public TimedTaskInfo(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }
}
