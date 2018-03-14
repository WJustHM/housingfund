package com.handge.housingfund.statemachine.entity;

import org.springframework.statemachine.transition.TransitionKind;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuefei_wang on 17-7-15.
 */
public class TransEntity implements Serializable{

    /**
     * State Machine Dictionary :
     * ------------------------------------------------
     * | stepId  |  source |  target | event | status   | -----> table schema
     * ------------------------------------------------
     * | zero  |   zero  |   zero  |  zero |  zero    | -----> zero step
     * ------------------------------------------------
     * | end   |   end   |   end   |  end  |  end     | -----> end step
     * ------------------------------------------------
     * | *     |   *     |    *    |  *    |   *      | -----> custom step
     * ------------------------------------------------
     */
    private static final long serialVersionUID = 834685499259845441L;

    /**
     * 转移内型状态
     */
    private TransitionKind transitionKind;

    /**
     * 　源状态
     */
    private String source;

    /**
     * 目标状态
     */

    private String target;

    /**
     * 事件
     */
    private String event;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 业务网点
     */
    private String workstation;

    public TransEntity() {
    }

    public TransEntity(TransitionKind transitionKind,
                       String source, String target,
                       String event, List<String> roles,
                       String workstation) {
        this.transitionKind = transitionKind;
        this.source = source;
        this.target = target;
        this.event = event;
        this.roles = roles;
        this.workstation = workstation;
    }

    public TransitionKind getTransitionKind() {
        return transitionKind;
    }

    public void setTransitionKind(TransitionKind transitionKind) {
        this.transitionKind = transitionKind;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<String> getRole() {
        return roles;
    }

    public void setRole(List<String> roles) {
        this.roles = roles;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    @Override
    public String toString() {
        return "TransEntity{" +
                "transitionKind=" + transitionKind +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", event='" + event + '\'' +
                ", role='" + roles + '\'' +
                ", workstation='" + workstation + '\'' +
                '}';
    }
}
