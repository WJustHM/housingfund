package com.handge.housingfund.common.service.collection.model.withdrawlModel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-6-27.
 */

@XmlRootElement(name = "BatchWithdrawlsInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchWithdrawlsInfo implements Serializable {
    /**
     * @param ids 业务流水号集合
     * @param action 操作  0提交 1删除
     */
    private ArrayList<String> ids ;
    private String action;

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
