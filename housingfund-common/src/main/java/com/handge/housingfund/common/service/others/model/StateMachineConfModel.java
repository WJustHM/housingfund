package com.handge.housingfund.common.service.others.model;

import com.handge.housingfund.database.enums.TransitionKind;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-8.
 */

@XmlRootElement(name = "StateMachineConfModel")
@XmlAccessorType(XmlAccessType.FIELD)
@Deprecated
public class StateMachineConfModel implements Serializable {

    private static final long serialVersionUID = -1048707832023890364L;

    private String id;

    private String source;

    private String event;

//    @XmlElement(name = "目标状态")
    private String target;

//    @XmlElement(name = "转移类型")
    private String transitionKind ;

//    @XmlElement(name = "角色列表")
    private List<String> roles;

//    @XmlElement(name = "业务类型")
    private String type;

//    @XmlElement(name = "业务子类型")
    private String subType;

//    @XmlElement(name = "业务网点")
    private String workstation;


    public StateMachineConfModel(String id, String source, String event, String target, String transitionKind, List<String> roles, String type, String subType) {
        this.id = id;
        this.source = source;
        this.event = event;
        this.target = target;
        this.transitionKind = transitionKind;
        this.roles = roles;
        this.type = type;
        this.subType = subType;
    }

    public StateMachineConfModel() {
        this(null,null,null,null, TransitionKind.EXTERNAL.toString(),null,null,null);
    }


    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTransitionKind() {
        return transitionKind;
    }

    public void setTransitionKind(String transitionKind) {
        this.transitionKind = transitionKind;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    @Override
    public String toString() {
        return "StateMachineConfModel{" +
                "id='" + id + '\'' +
                ", source='" + source + '\'' +
                ", event='" + event + '\'' +
                ", target='" + target + '\'' +
                ", transitionKind='" + transitionKind + '\'' +
                ", roles=" + roles +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", workstation='" + workstation + '\'' +
                '}';
    }
}
