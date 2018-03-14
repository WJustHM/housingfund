package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/9/22.
 */
@XmlRootElement(name = "签约银行信息列表")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankContactList extends BankContact implements Serializable {

    private static final long serialVersionUID = 2940345743760025588L;
    private String id;
    private Boolean canRewrite;
    private Boolean canDel;

    public BankContactList() {
    }

    public BankContactList(String YHMC, String YHDM, String node, String XDCKJE, String WDDZ, String LXR,
                           String LXDH, String id, Boolean canRewrite, Boolean canDel, String chgno, String PLXMBH, boolean KHSFSS) {
        super(YHMC, YHDM, node, XDCKJE, WDDZ, LXR, LXDH, chgno, PLXMBH, KHSFSS);
        this.id = id;
        this.canRewrite = canRewrite;
        this.canDel = canDel;
    }

    public Boolean getCanRewrite() {
        return canRewrite;
    }

    public void setCanRewrite(Boolean canRewrite) {
        this.canRewrite = canRewrite;
    }

    public Boolean getCanDel() {
        return canDel;
    }

    public void setCanDel(Boolean canDel) {
        this.canDel = canDel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
