package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/15.
 * 描述
 */
public class FSLXE implements Serializable {
    String FSLXE;

    public String getFSLXE() {
        return FSLXE;
    }

    public void setFSLXE(String FSLXE) {
        this.FSLXE = FSLXE;
    }

    @Override
    public String toString() {
        return "FSLXE{" +
                "FSLXE=" + FSLXE +
                '}';
    }
}
