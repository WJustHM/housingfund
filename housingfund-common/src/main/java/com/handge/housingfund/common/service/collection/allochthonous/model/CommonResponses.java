package com.handge.housingfund.common.service.collection.allochthonous.model;

import java.io.*;
import javax.xml.bind.annotation.*;


@SuppressWarnings({"WeakerAccess", "unused", "SpellCheckingInspection", "serial"})
@XmlRootElement(name = "CommonResponses")
@XmlAccessorType(XmlAccessType.FIELD)
public class CommonResponses implements Serializable {

    private String Status;//操作码

    private String Msg;//信息描述

    private String YWLSH;

    @SuppressWarnings("unused")
    private void defaultConstructor() {

        this.setStatus(null/*操作码*/);
        this.setMsg(null/*信息描述*/);
        this.setYWLSH(null);
    }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    /**
     * 操作码
     **/
    public String getStatus() {
        return Status;
    }

    public void setStatus(String Code) {
        this.Status = Code;
    }

    /**
     * 信息描述
     **/
    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }


    @Override
    public String toString() {

        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonResponses {\n");
        sb.append("Status:").append(Status).append("\n");
        sb.append("Msg:").append(Msg).append("\n");
        sb.append("YWLSH:").append(YWLSH).append("\n");
        sb.append("}\n");
        return sb.toString();

    }

    public void checkValidation() {

    }
}
