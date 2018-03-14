package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by tanyi on 2017/10/25.
 */
@XmlRootElement(name = "对账")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompareBooks implements Serializable {

    private static final long serialVersionUID = 1075306714009473493L;
    BookDetails ZXDZD;//中心对账单

    BookDetails YHDZD;//银行对账单

    public CompareBooks() {
    }

    public CompareBooks(BookDetails ZXDZD, BookDetails YHDZD) {
        this.ZXDZD = ZXDZD;
        this.YHDZD = YHDZD;
    }

    public BookDetails getZXDZD() {
        return ZXDZD;
    }

    public void setZXDZD(BookDetails ZXDZD) {
        this.ZXDZD = ZXDZD;
    }

    public BookDetails getYHDZD() {
        return YHDZD;
    }

    public void setYHDZD(BookDetails YHDZD) {
        this.YHDZD = YHDZD;
    }
}
