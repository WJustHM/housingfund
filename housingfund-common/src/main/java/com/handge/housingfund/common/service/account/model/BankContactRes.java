package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "签约银行详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankContactRes extends BankContact implements Serializable {
    private static final long serialVersionUID = 7499517036580452980L;
    private String id;
    private String KHBH;//客户编号
    private String beizhu;//备注

    public BankContactRes() {
    }

    public BankContactRes(String YHMC, String YHDM, String node, String XDCKJE, String WDDZ,
                          String LXR, String LXDH, String id, String KHBH, String beizhu, String chgno, String PLXMBH, boolean KHSFSS) {
        super(YHMC, YHDM, node, XDCKJE, WDDZ, LXR, LXDH, chgno, PLXMBH, KHSFSS);
        this.id = id;
        this.KHBH = KHBH;
        this.beizhu = beizhu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
