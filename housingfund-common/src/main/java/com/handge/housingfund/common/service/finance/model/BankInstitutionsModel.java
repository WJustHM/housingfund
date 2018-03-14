package com.handge.housingfund.common.service.finance.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by xuefei_wang on 17-8-16.
 */

@XmlRootElement(name = "银行机构")
@XmlAccessorType(XmlAccessType.FIELD)
public class BankInstitutionsModel implements Serializable {
    @XmlElement(name = "银行代码")
    private String code;

    @XmlElement(name = "银行名称")
    private String name;

    @XmlElement(name = "银行网点")
    private String workstation;

    @XmlElement(name = "网点地址")
    private String location;

    @XmlElement(name = "联系人")
    private String contact;

    @XmlElement(name = "联系人电话")
    private String contactPhone;

    @XmlElement(name = "备注")
    private String note;

    public BankInstitutionsModel() {
        this(null, null, null, null, null, null, null);
    }

    public BankInstitutionsModel(String code, String name, String workstation, String location, String contact, String contactPhone, String note) {
        this.code = code;
        this.name = name;
        this.workstation = workstation;
        this.location = location;
        this.contact = contact;
        this.contactPhone = contactPhone;
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "BankInstitutionsModel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", workstation='" + workstation + '\'' +
                ", location='" + location + '\'' +
                ", contact='" + contact + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
