package com.handge.housingfund.common.service.account.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gxy on 17-7-20.
 */
@XmlRootElement(name = "Module")
@XmlAccessorType(XmlAccessType.FIELD)
public class Module implements Serializable {
    private String id;
    private String moduleName;
    List<ModulePermission> modulePermissions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<ModulePermission> getModulePermissions() {
        return modulePermissions;
    }

    public void setModulePermissions(List<ModulePermission> modulePermissions) {
        this.modulePermissions = modulePermissions;
    }
}
