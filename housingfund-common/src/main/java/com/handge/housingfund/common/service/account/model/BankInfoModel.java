package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/6.
 */
@XmlRootElement(name = "银行信息")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankInfoModel implements Serializable {

    private static final long serialVersionUID = -4444878995053877406L;

    private String id;

    //银行全称
    private String bank_name;

    //联行号
    private String chgno;

    //银行节点号
    private String node;

    //银行代码，联行号前三位
    private String code;

    public BankInfoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getChgno() {
        return chgno;
    }

    public void setChgno(String chgno) {
        this.chgno = chgno;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BankInfoModel{" +
                "id='" + id + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", chgno='" + chgno + '\'' +
                ", node='" + node + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
