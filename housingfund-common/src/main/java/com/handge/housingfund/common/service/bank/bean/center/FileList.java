package com.handge.housingfund.common.service.bank.bean.center;

import java.io.Serializable;

/**
 * 文件列表
 */
public class FileList implements Serializable {
    private static final long serialVersionUID = 92433185843610311L;
    /**
     * 文件名(required)
     */
    private String NAME;
    /**
     * 文件内容(required)
     */
    private String  DATA;

    public FileList() {
    }

    public FileList(String NAME, String DATA) {
        this.NAME = NAME;
        this.DATA = DATA;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    @Override
    public String toString() {
        return "FileList{" +
                "NAME='" + NAME + '\'' +
                ", DATA='" + DATA + '\'' +
                '}';
    }
}
