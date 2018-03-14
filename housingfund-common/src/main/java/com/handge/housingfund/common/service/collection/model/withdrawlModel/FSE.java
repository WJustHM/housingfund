package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import java.io.Serializable;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/29.
 * 描述
 */
public class FSE implements Serializable{
    String FSE;

    public String getFSE() {
        return FSE;
    }

    public void setFSE(String FSE) {
        this.FSE = FSE;
    }

    @Override
    public String toString() {
        return "FSE{" +
                "FSE=" + FSE +
                '}';
    }
}
