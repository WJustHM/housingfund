package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "PostUnitAcctAlterSubmitRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PostUnitAcctAlterSubmitRes  implements Serializable{

    private String Status;  //状态（成功：success；失败：fail）

    private String SBYWLSH; //导致失败的业务流水号

    public String getStatus() {

        return this.Status;

    }


    public void setStatus(String Status) {

        this.Status = Status;

    }

    public String getSBYWLSH() {
        return SBYWLSH;
    }

    public void setSBYWLSH(String SBYWLSH) {
        this.SBYWLSH = SBYWLSH;
    }

    @Override
    public String toString() {
        return "PostUnitAcctAlterSubmitRes{" +
                "Status='" + Status + '\'' +
                ", SBYWLSH='" + SBYWLSH + '\'' +
                '}';
    }
}