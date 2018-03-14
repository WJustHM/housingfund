package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplyLoanImportPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplyLoanImportPost  implements Serializable {

    private String Content;  //导入信息的内容（一般为二进制）

    public String getContent() {

        return this.Content;

    }


    public void setContent(String Content) {

        this.Content = Content;

    }


    public String toString() {

        return "ApplyLoanImportPost{" +

                "Content='" + this.Content + '\'' +

                "}";

    }
}