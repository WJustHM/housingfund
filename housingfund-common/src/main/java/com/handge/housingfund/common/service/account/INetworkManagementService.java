package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.*;

import java.util.List;

/**
 * Created by tanyi on 2017/7/19.
 */
public interface INetworkManagementService {


    /**
     * 获取网点列表
     *
     * @param pageNo
     * @param pageSize
     * @param MingCheng
     * @return
     */
    PageRes<Network> getNetworkList(int pageNo, int pageSize, String MingCheng);

    /**
     * 获取网点详情
     *
     * @param id
     * @return
     */
    NetworkWithDepartment getNetworkDetail(String id);

    /**
     * 添加网点
     *
     * @param network 网点信息
     * @return
     */
    String addNetwork(TokenContext tokenContext, Network network);

    /**
     * 修改网点
     *
     * @param id      网点ID
     * @param network 网点信息
     * @return
     */
    String putNetwork(TokenContext tokenContext, String id, Network network);

    /**
     * 删除网点
     *
     * @param ids
     * @return
     */
    List<Delres> delNetWork(List<String> ids);

    /**
     * 获取部门列表
     *
     * @param id 网点ID
     * @return
     */
    List<DepartmentWithNetwork> getDepartmentList(String id);

    /**
     * 新增、修改部门
     *
     * @param department
     * @return
     */
    String putDepartment(Department department);

    /**
     * 删除部门
     *
     * @param ids
     * @return
     */
    List<Delres> delDepartment(List<String> ids);

    /**
     * 获取未分配部门的员工列表
     *
     * @return
     */
    List<EmployeeWithDepartment> getDepartmentMember(String id);

}
