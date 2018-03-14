package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "HousingCompanyReviewInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingCompanyReviewInfo  implements Serializable {

    private String Action;  //审核结果(0通过1不通过)

    private String Reason;  //审核不通过（原因）

    private String SHYJ;    // 审核通过（审核意见）

    public String getSHYJ() {
        return SHYJ;
    }

    public void setSHYJ(String SHYJ) {
        this.SHYJ = SHYJ;
    }

    public String getAction() {

        return this.Action;

    }


    public void setAction(String Action) {

        this.Action = Action;

    }


    public String getReason() {

        return this.Reason;

    }


    public void setReason(String Reason) {

        this.Reason = Reason;

    }


    @Override
    public String toString() {
        return "HousingCompanyReviewInfo{" +
                "Action='" + Action + '\'' +
                ", Reason='" + Reason + '\'' +
                ", SHYJ='" + SHYJ + '\'' +
                '}';
    }
}