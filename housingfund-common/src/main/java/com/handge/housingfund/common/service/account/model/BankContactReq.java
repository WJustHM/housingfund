package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "新增签约银行")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankContactReq extends BankContact implements Serializable {

    private static final long serialVersionUID = -2080753959883307462L;
    private String KHBH;//客户编号
    private String beizhu;//备注

    public BankContactReq() {
    }

    public BankContactReq(String YHMC, String YHDM, String node, String XDCKJE, String WDDZ,
                          String LXR, String LXDH, String KHBH, String beizhu, String chgno, String PLXMBH, boolean KHSFSS) {
        super(YHMC, YHDM, node, XDCKJE, WDDZ, LXR, LXDH, chgno, PLXMBH, KHSFSS);
        this.KHBH = KHBH;
        this.beizhu = beizhu;
    }

    public String getKHBH() {
        return KHBH;
    }

    public void setKHBH(String KHBH) {
        this.KHBH = KHBH;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }
}
