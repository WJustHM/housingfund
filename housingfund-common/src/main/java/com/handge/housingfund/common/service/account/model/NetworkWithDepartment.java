package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tanyi on 2017/7/25.
 */
@XmlRootElement(name = "NetworkWithDepartment")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkWithDepartment extends Network implements Serializable {

    private List<DepartmentWithNetwork> cAccountDepartments;

    public List<DepartmentWithNetwork> getcAccountDepartments() {
        return cAccountDepartments;
    }

    public void setcAccountDepartments(List<DepartmentWithNetwork> cAccountDepartments) {
        this.cAccountDepartments = cAccountDepartments;
    }
}
