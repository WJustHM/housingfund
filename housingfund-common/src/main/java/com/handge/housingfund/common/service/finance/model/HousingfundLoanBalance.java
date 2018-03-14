package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/13.
 */
@XmlRootElement(name = "住房公积金贷款发放和账户存款余额变动情况统计表")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousingfundLoanBalance implements Serializable {

    private static final long serialVersionUID = -3338045878530508438L;
    private String BZDW;//编制单位
    private String RiQi;//日期
    private String DRGDSPFFE;//当日个贷审批发放额
    private String ZFGJJZHCKYE;//住房公积金专户存款余额
    private String ZZSYZHCKYE;//增值收益专户存款余额
    private String YEHJ;//余额合计

    public HousingfundLoanBalance() {
    }

    public HousingfundLoanBalance(String BZDW, String riQi, String DRGDSPFFE, String ZFGJJZHCKYE, String ZZSYZHCKYE, String YEHJ) {
        this.BZDW = BZDW;
        this.RiQi = riQi;
        this.DRGDSPFFE = DRGDSPFFE;
        this.ZFGJJZHCKYE = ZFGJJZHCKYE;
        this.ZZSYZHCKYE = ZZSYZHCKYE;
        this.YEHJ = YEHJ;
    }

    public String getBZDW() {
        return BZDW;
    }

    public void setBZDW(String BZDW) {
        this.BZDW = BZDW;
    }

    public String getRiQi() {
        return RiQi;
    }

    public void setRiQi(String riQi) {
        RiQi = riQi;
    }

    public String getDRGDSPFFE() {
        return DRGDSPFFE;
    }

    public void setDRGDSPFFE(String DRGDSPFFE) {
        this.DRGDSPFFE = DRGDSPFFE;
    }

    public String getZFGJJZHCKYE() {
        return ZFGJJZHCKYE;
    }

    public void setZFGJJZHCKYE(String ZFGJJZHCKYE) {
        this.ZFGJJZHCKYE = ZFGJJZHCKYE;
    }

    public String getZZSYZHCKYE() {
        return ZZSYZHCKYE;
    }

    public void setZZSYZHCKYE(String ZZSYZHCKYE) {
        this.ZZSYZHCKYE = ZZSYZHCKYE;
    }

    public String getYEHJ() {
        return YEHJ;
    }

    public void setYEHJ(String YEHJ) {
        this.YEHJ = YEHJ;
    }

    @Override
    public String toString() {
        return "HousingfundLoanBalance{" +
                "BZDW='" + BZDW + '\'' +
                ", RiQi='" + RiQi + '\'' +
                ", DRGDSPFFE='" + DRGDSPFFE + '\'' +
                ", ZFGJJZHCKYE='" + ZFGJJZHCKYE + '\'' +
                ", ZZSYZHCKYE='" + ZZSYZHCKYE + '\'' +
                ", YEHJ='" + YEHJ + '\'' +
                '}';
    }
}
