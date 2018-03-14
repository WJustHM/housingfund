package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/12/8.
 */
@XmlRootElement(name = "账户变动通知文件")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccChangeNoticeSMWJ implements Serializable {

    private static final long serialVersionUID = 3406126272650994377L;
    private String SMWJ;

    public AccChangeNoticeSMWJ() {
    }


    public AccChangeNoticeSMWJ(String SMWJ) {
        this.SMWJ = SMWJ;
    }

    public String getSMWJ() {
        return SMWJ;
    }

    public void setSMWJ(String SMWJ) {
        this.SMWJ = SMWJ;
    }

    @Override
    public String toString() {
        return "AccChangeNoticeFile{" +
                "SMWJ='" + SMWJ + '\'' +
                '}';
    }
}
