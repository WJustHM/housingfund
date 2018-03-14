package com.handge.housingfund.common.service.others.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by tanyi on 2017/8/19.
 */
public class UFile implements Serializable {

    private String modle;
    private String business;
    private String name;
    private String operation;
    private boolean required;
    private String[] data;

    public UFile(String modle, String business, String name, String operation, boolean required, String[] data) {
        this.modle = modle;
        this.business = business;
        this.name = name;
        this.operation = operation;
        this.required = required;
        this.data = data;
    }

    public UFile() {
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getModle() {
        return modle;
    }

    public void setModle(String modle) {
        this.modle = modle;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UFile{" +
                "modle='" + modle + '\'' +
                ", business='" + business + '\'' +
                ", name='" + name + '\'' +
                ", operation='" + operation + '\'' +
                ", required=" + required +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
