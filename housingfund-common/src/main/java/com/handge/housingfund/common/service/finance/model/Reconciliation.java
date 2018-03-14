package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gxy on 17-10-25.
 */
@XmlRootElement(name = "余额调节详情")
@XmlAccessorType(XmlAccessType.FIELD)
public class Reconciliation extends ReconciliationBase {

    private static final long serialVersionUID = 2545741319263911845L;
    private String id;

    private String ZBR;//制表人

    private String ZBSJ;//制表时间

    public Reconciliation() {
    }

    public Reconciliation(String TJRQ, String ZHHM, String node, String ZXCKYE, String ZXJIA, String ZXJ, String ZXYE, String YHDZYE, String YHJIA, String YHJ, String YHYE, String KHYHM, String id, String ZBR, String ZBSJ) {
        super(TJRQ, ZHHM, node, ZXCKYE, ZXJIA, ZXJ, ZXYE, YHDZYE, YHJIA, YHJ, YHYE, KHYHM);
        this.id = id;
        this.ZBR = ZBR;
        this.ZBSJ = ZBSJ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZBR() {
        return ZBR;
    }

    public void setZBR(String ZBR) {
        this.ZBR = ZBR;
    }

    public String getZBSJ() {
        return ZBSJ;
    }

    public void setZBSJ(String ZBSJ) {
        this.ZBSJ = ZBSJ;
    }

    @Override
    public String toString() {
        return "Reconciliation{" +
                "id='" + id + '\'' +
                ", ZBR='" + ZBR + '\'' +
                ", ZBSJ='" + ZBSJ + '\'' +
                '}';
    }
}
