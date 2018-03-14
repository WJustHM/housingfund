package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Liujuhao on 2017/8/10.
 */

@XmlRootElement(name = "GetLoanContractRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetLoanContractRes  implements Serializable {

    private GetLoanContractResPerson contractResPerson;

    private GetLoanContractResCoborrower contractResCoborrower;

    public GetLoanContractResPerson getContractResPerson() {
        return contractResPerson;
    }

    public void setContractResPerson(GetLoanContractResPerson contractResPerson) {
        this.contractResPerson = contractResPerson;
    }

    public GetLoanContractResCoborrower getContractResCoborrower() {
        return contractResCoborrower;
    }

    public void setContractResCoborrower(GetLoanContractResCoborrower contractResCoborrower) {
        this.contractResCoborrower = contractResCoborrower;
    }

    @Override
    public String toString() {
        return "GetLoanContractRes{" +
                "contractResPerson=" + contractResPerson +
                ", contractResCoborrower=" + contractResCoborrower +
                '}';
    }
}
