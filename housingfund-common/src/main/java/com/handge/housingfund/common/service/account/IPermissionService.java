package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.*;

import java.util.List;

/**
 * Created by gxy on 17-7-19.
 */
public interface IPermissionService {
    RoleWithAuthList getRoleList(String roleName, int pageNo, int pageSize);

    RoleWithAuth getRoleDetail(String id);

    boolean addRole(Role roleRep);

    boolean putRole(String id, Role roleRep);

    boolean delRole(String id);

    List<EmployeeWithDepartment> getRoleEmployeeList(String name, String id);

    boolean putRoleEmployee(String id, List<String> list);

    List<Classify> getClassifyList(String id);

    boolean addPermission(String id, List<String> list);

    public List<String> getUserAccessAPS(String userId, int type,String action ,String path);

    /**
     * 验证账号是否有权限
     * @param userId
     * @param MU
     * @return
     */
    boolean getPermissionByUserId(String userId,int type, String MU) throws Exception;

    /**
     * 获取员工列表
     *
     * @param WangDian
     * @param MingCheng
     * @param ZhangHao
     * @param BuMen
     * @return
     */
    PageRes<AccountEmployeeDetail> getEmployeeList(int YeMa, int TiaoShu, String WangDian, String MingCheng,
                                                   String ZhangHao, String BuMen);

    /**
     * 获取员工详情
     *
     * @param id 员工ID
     * @return
     */
    AccountEmployeeDetail getEmployeeDetail(String id);

    /**
     * 添加员工
     *
     * @param employee
     * @return
     */
    AccountEmployeeDetail addEmployee(Employee employee);

    /**
     * 修改员工信息
     *
     * @param id       员工ID
     * @param employee 员工
     * @return
     */
    String putEmployee(String id, Employee employee);

    /**
     * 删除员工
     *
     * @param list id数组
     * @return
     */
    Msg delEmployee(List<String> list);

	List<String> getRoleIDByUserId(String userId);

}
