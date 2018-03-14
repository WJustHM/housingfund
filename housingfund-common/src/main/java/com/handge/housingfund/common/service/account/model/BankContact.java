package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "签约银行信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankContact implements Serializable {

    private static final long serialVersionUID = 5143359780034531821L;
    private String YHMC;//银行名称
    private String YHDM;//银行代码
    private String node;//银行节点号
    private String XDCKJE;//协定存款金额
    private String WDDZ;//网点地址
    private String LXR;//联系人
    private String LXDH;//联系电话
    private String chgno;//联行号
    private String PLXMBH;//批量项目编号
    private boolean KHSFSS;//跨行是否实时

    public BankContact() {
    }

    public BankContact(String YHMC, String YHDM, String node, String XDCKJE, String WDDZ, String LXR, String LXDH, String chgno, String PLXMBH, boolean KHSFSS) {
        this.YHMC = YHMC;
        this.YHDM = YHDM;
        this.node = node;
        this.XDCKJE = XDCKJE;
        this.WDDZ = WDDZ;
        this.LXR = LXR;
        this.LXDH = LXDH;
        this.chgno = chgno;
        this.PLXMBH = PLXMBH;
        this.KHSFSS = KHSFSS;
    }

    public String getChgno() {
        return chgno;
    }

    public void setChgno(String chgno) {
        this.chgno = chgno;
    }

    public String getYHMC() {
        return YHMC;
    }

    public void setYHMC(String YHMC) {
        this.YHMC = YHMC;
    }

    public String getYHDM() {
        return YHDM;
    }

    public void setYHDM(String YHDM) {
        this.YHDM = YHDM;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getXDCKJE() {
        return XDCKJE;
    }

    public void setXDCKJE(String XDCKJE) {
        this.XDCKJE = XDCKJE;
    }

    public String getWDDZ() {
        return WDDZ;
    }

    public void setWDDZ(String WDDZ) {
        this.WDDZ = WDDZ;
    }

    public String getLXR() {
        return LXR;
    }

    public void setLXR(String LXR) {
        this.LXR = LXR;
    }

    public String getLXDH() {
        return LXDH;
    }

    public void setLXDH(String LXDH) {
        this.LXDH = LXDH;
    }

    public String getPLXMBH() {
        return PLXMBH;
    }

    public void setPLXMBH(String PLXMBH) {
        this.PLXMBH = PLXMBH;
    }

    public boolean isKHSFSS() {
        return KHSFSS;
    }

    public void setKHSFSS(boolean KHSFSS) {
        this.KHSFSS = KHSFSS;
    }
}
