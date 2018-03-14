package com.handge.housingfund.server.controllers.account.api;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.Department;
import com.handge.housingfund.common.service.account.model.Network;

import java.util.List;

/**
 * Created by gxy on 17-7-15.
 */
public interface INetworkManagementAPI<T> {
    /**
     * 获取网点列表
     *
     * @param pageNo
     * @param pageSize
     * @param MingCheng
     * @return
     */
    T getNetworkList(int pageNo, int pageSize, String MingCheng);

    /**
     * 获取网点详情
     *
     * @param id 网点ID
     * @return
     */
    T getNetworkDetail(String id);

    /**
     * 添加网点
     *
     * @return
     */
    T addNetwork(TokenContext tokenContext, Network network);

    /**
     * 修改网点信息
     *
     * @param id 网点id
     * @return
     */
    T updateNetwork(TokenContext tokenContext, String id, Network network);

    /**
     * 删除网点
     *
     * @param list 网点id集合
     * @return
     */
    T deleteNetwork(List<String> list);

    /**
     * 获取部门列表
     *
     * @param id 网点ID 若为空则查询所有
     * @return response
     */
    T getDepartmentList(String id);

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return
     */
//    T getDepartmentDetial(String id);

    /**
     * 添加部门
     *
     * @return
     */
    T addDepartment(Department department);

    /**
     * 修改部门信息
     *
     * @param id         部门ID
     * @param department 部门信息
     * @return
     */
//    T updateDepartment(String id, Department department);

    /**
     * 删除部门
     *
     * @param list 部门信息
     * @return
     */
    T deleteDepartment(List<String> list);

    /**
     * 获取未分配部门的成员列表
     * 所有没有部门的成员
     *
     * @param id 网点ID
     * @return
     */
    T getDepartmentMember(String id);

    /**
     * 获取地区信息
     *
     * @param id 上级ID
     * @return
     */
    T getArea(String id);

}
