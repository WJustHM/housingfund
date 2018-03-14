package com.handge.housingfund.common.service.others.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei_wang on 17-9-14.
 */
@XmlRootElement(name = "StateMachineConfig")
@XmlAccessorType(XmlAccessType.FIELD)
public class StateMachineConfig  implements Serializable{

    private String id;

    private String type ;//业务类型

    private String sub_type;//业务子类型

    private String effectiveTime;//生效日期

    private List<FlowInfo> flowInfos = new ArrayList<>();//流程图

    private String ywwd;//业务网点

    public String getYwwd() {
        return ywwd;
    }

    public void setYwwd(String ywwd) {
        this.ywwd = ywwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public List<FlowInfo> getFlowInfos() {
        return flowInfos;
    }

    public void setFlowInfos(List<FlowInfo> flowInfos) {
        this.flowInfos = flowInfos;
    }
    public void addFlow(FlowInfo info){
        this.flowInfos.add(info);
    }

    public static  class  FlowInfo implements Serializable{

        private int no;//编号

        private String state;//状态

        private List<String> roles = new ArrayList<>();//权限列表

        private int auditLevel  = 0 ; //审核级别

        private String netWorkstation;

        private boolean isSpeicial;//是否特审

        public boolean getIsSpeicial() {
            return isSpeicial;
        }

        public void setIsSpeicial(boolean isSpeicial) {
            this.isSpeicial = isSpeicial;
        }

        public int getAuditLevel() {
            return auditLevel;
        }

        public void setAuditLevel(int auditLevel) {
            this.auditLevel = auditLevel;
        }

        public int getNo() {
            return no;
        }

        public void setNo(int no) {
            this.no = no;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public void addRole(String role){
            this.roles.add(role);
        }

        public String getNetWorkstation() {
            return netWorkstation;
        }

        public void setNetWorkstation(String netWorkstation) {
            this.netWorkstation = netWorkstation;
        }

        @Override
        public String toString() {
            return "FlowInfo{" +
                    "no=" + no +
                    ", state='" + state + '\'' +
                    ", roles=" + roles +
                    ", auditLevel=" + auditLevel +
                    ", netWorkstation='" + netWorkstation + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "StateMachineConfig{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", sub_type='" + sub_type + '\'' +
                ", effectiveTime='" + effectiveTime + '\'' +
                ", flowInfos=" + flowInfos +
                '}';
    }
}
