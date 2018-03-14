package com.handge.housingfund.statemachine.entity;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.database.enums.BusinessType;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by xuefei on 17-7-9.
 */
public class TaskEntity<T> implements Serializable{
    private static final long serialVersionUID = 834685499259845244L;

    public static final String key = "TASK";

    private T object;
    /**
     * 当前业务状态
     */
    private  String  currentStatus;

    /**
     * 业务流水号
     */
    private  String  taskId;

    /**
     * 操作角色
     * use roleSet insted
     */
    @Deprecated
    private  String  role;

    /**
     * 角色集合
     *
     */
    private HashSet<String> roleSet = new HashSet<>();


    /**
     * 操作备注
     */
    private String note;

    /**
     * 业务子类型
     */
    private String subtype;

    /**
     * 业务类型
     */
    private BusinessType type;

    /**
     * 操作员
     */
    private String operator;

    private String workstation;

    private TokenContext tokenContext;

    @SuppressWarnings("没有实现")
    public TaskEntity(String name, String ywlsh, String ty, String czy) {
    }


    public boolean verifyTask(){
        if(taskId == null || taskId.equals("")) throw new NullPointerException("taskId 不可为空");
        if(roleSet.size() == 0) throw new NullPointerException("role 不可为空");
        if(operator == null || operator.equals("")) throw new NullPointerException("operator 不可为空");
        if(subtype == null || subtype.equals("")) throw new NullPointerException("subType 不可为空");
        if(type == null || type.equals("")) throw new NullPointerException("type 不可为空");
        if(workstation == null || workstation.equals("")) throw new NullPointerException("workstation 不可为空");
        if(currentStatus == null || currentStatus.equals("")) throw new NullPointerException("currentStatus 不可为空");
        return true;
    }

    public TaskEntity() {
    }

    /**
     *
     * @param taskId
     * @param currentStatus
     * @param role
     * @param operator
     * @param note
     * @param subtype
     * @param type
     * @param workstation
     */
    @Deprecated
    public TaskEntity( String taskId,
                       String currentStatus,
                       String role,
                       String operator,
                       String note,
                       String subtype,
                       BusinessType type,
                       String workstation) {
        this.currentStatus = currentStatus;
        this.taskId = taskId;
        this.role = role;
        this.addRole(role);
        this.operator = operator;
        this.subtype = subtype;
        this.type = type;
        this.workstation = workstation;
        this.note = note;
    }
    public TaskEntity( String taskId,
                       String currentStatus,
                       HashSet<String> roleSet,
                       String operator,
                       String note,
                       String subtype,
                       BusinessType type,
                       String workstation) {
        this.currentStatus = currentStatus;
        this.taskId = taskId;
        this.roleSet = roleSet;
        this.operator = operator;
        this.subtype = subtype;
        this.type = type;
        this.workstation = workstation;
        this.note = note;
    }

    public TaskEntity(String taskId,
                      String currentStatus,
                      String operator,
                      String note,
                      String subtype,
                      BusinessType type,
                      String workstation,
                      HashSet roleSet){
        this.currentStatus = currentStatus;
        this.taskId = taskId;
        this.roleSet = roleSet;
        this.operator = operator;
        this.subtype = subtype;
        this.type = type;
        this.workstation = workstation;
        this.note = note;
    }

    public void setTokenContext(TokenContext tokenContext){
        this.tokenContext = tokenContext;
    }
    public TokenContext getTokenContext(){
        return this.tokenContext;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public HashSet<String> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(HashSet<String> roleSet) {
        this.roleSet = roleSet;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return currentStatus;
    }

    public void setStatus(String status) {
        this.currentStatus = status;
    }

    @Deprecated
    public String getRole() {
        return role;
    }

    @Deprecated
    public void setRole(String role) {
        this.role = role;
        this.roleSet.add(role);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public BusinessType getType() {
        return type;
    }

    public void setType(BusinessType type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public void  addRole(String role){
        this.roleSet.add(role);
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "currentStatus='" + currentStatus + '\'' +
                ", taskId='" + taskId + '\'' +
                ", role='" + role + '\'' +
                ", roleSet=" + roleSet +
                ", note='" + note + '\'' +
                ", subtype='" + subtype + '\'' +
                ", type=" + type +
                ", operator='" + operator + '\'' +
                ", workstation='" + workstation + '\'' +
                '}';
    }
}
