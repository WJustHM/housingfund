package com.handge.housingfund.server.controllers.account.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Employee;
import com.handge.housingfund.common.service.account.model.Role;

import java.util.List;

/**
 * Created by tanyi on 2017/7/18.
 */
public interface IPermissionAPI<T> {

    /**
     * 获取角色列表
     *
     * @param rolename 角色名称，非必选
     * @return
     */
    T getRoleList(String rolename, int pageNo, int pageSize);

    /**
     * 获取角色详情
     *
     * @param id
     * @return
     */
    T getRoleDetail(String id);

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return
     */
    T addRole(Role role);

    /**
     * 修改角色
     *
     * @param id
     * @param role
     * @return
     */
    T putRole(String id, Role role);

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    T delRole(String id);

    /**
     * 未分配角色的员工列表
     *
     * @return
     */
    T getRoleEmployeeList(String name, String id);

    /**
     * 修改角色拥有的员工
     * @param id
     * @param list
     * @return
     */
    T putRoleEmployee(String id, List<String> list);

    /**
     * 获取编辑权限列表
     *
     * @return
     */
    T getClassifyList(String id);

    /**
     * 给角色添加权限
     *
     * @param id   角色ID
     * @param list 权限ID数组
     * @return
     */
    T addPermission(String id, List<String> list);

    /**
     * 获取审批流程列表
     *
     * @return
     */
    T getApproveList();

    /**
     * 添加
     *
     * @return
     */
    T addApprove();

    /**
     * 获取员工列表
     *
     * @param pageNo
     * @param pageSize
     * @param WangDian
     * @param MingCheng
     * @param ZhangHao
     * @param BuMen
     * @return
     */
    T getEmployeeList(int pageNo, int pageSize, String WangDian, String MingCheng,
                      String ZhangHao, String BuMen);

    /**
     * 获取员工详情
     *
     * @param id 员工ID
     * @return
     */
    T getEmployeeDetail(String id);

    /**
     * 添加员工
     *
     * @param employee 员工信息
     * @return
     */
    T addEmployee(Employee employee);

    /**
     * 修改员工
     *
     * @param id       员工ID
     * @param employee 员工信息
     * @return
     */
    T putEmployee(String id, Employee employee);

    /**
     * 删除员工
     *
     * @param list 员工ID列表
     * @return
     */
    T delEmployee(List<String> list);
    /**
     * 获取平台员工信息
     *
     * @param tokenContext 员工信息
     * @return
     */
    T getEmployeeInfo(TokenContext tokenContext);
}
