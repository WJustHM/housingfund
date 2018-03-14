package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.IPermissionService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.Error;
import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.RedisCache;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.account.api.IPermissionAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tanyi on 2017/7/18.
 */
@Component
public class PermissionAPI implements IPermissionAPI<Response> {
    @Autowired
    IPermissionService permissionService;
    @Autowired
    IActionService actionService;

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    @Override
    public Response getRoleList(String rolename, int pageNo, int pageSize) {
        if (pageNo == 0)
            pageNo = 1;
        if (pageSize == 0)
            pageSize = 10;

        try {
            RoleWithAuthList roleList = permissionService.getRoleList(rolename, pageNo, pageSize);
            return Response.status(200).entity(roleList).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response getRoleDetail(String id) {
        RoleWithAuth roleResBody = permissionService.getRoleDetail(id);
        try {
            return Response.status(200).entity(roleResBody != null ? roleResBody : new Msg() {
                {
                    this.setMsg("未查到");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response addRole(Role role) {
        if (role == null || !StringUtil.notEmpty(role.getRole_name())) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("添加角色失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            permissionService.addRole(role);
            Success success = new Success();
            success.setId("");
            success.setState("添加成功");
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response putRole(String id, Role role) {
        if (!StringUtil.notEmpty(id) || role == null || !StringUtil.notEmpty(role.getRole_name())) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("修改角色失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            permissionService.putRole(id, role);
            Success success = new Success();
            success.setId(id);
            success.setState("修改角色成功");
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response delRole(String id) {
        if (!StringUtil.notEmpty(id))
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("删除角色失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        try {
            permissionService.delRole(id);
            Success success = new Success();
            success.setId(id);
            success.setState("删除角色成功");
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }
    }

    @Override
    public Response getRoleEmployeeList(String name, String id) {
        if (!StringUtil.notEmpty(id)) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("id不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            List<EmployeeWithDepartment> authWithDepartmentList = permissionService.getRoleEmployeeList(name, id);

            return Response.status(200).entity(authWithDepartmentList != null ? authWithDepartmentList : new Msg() {
                {
                    this.setMsg("查询失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response putRoleEmployee(String id, List<String> list) {
        if (!StringUtil.notEmpty(id) || list == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("修改角色成员失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        try {
            permissionService.putRoleEmployee(id, list);
            Success success = new Success();
            success.setId(id);
            success.setState("修改角色成员成功");
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response getClassifyList(String id) {
        try {
            List<Classify> classifyList = permissionService.getClassifyList(id);
            return Response.status(200).entity(classifyList != null ? classifyList : new Msg() {
                {
                    this.setMsg("查询失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response addPermission(String id, List<String> list) {
        if (!StringUtil.notEmpty(id) || list == null)
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("修改角色权限失败");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        try {
            permissionService.addPermission(id, list);
            Success success = new Success();
            success.setId(id);
            success.setState("修改角色权限成功");
            try {
                HashSet<String> tmpActions = actionService.getActionCodesByRole(id);
                Iterator<String> iterator = tmpActions.iterator();
                JedisCluster redis = redisCache.getJedisCluster();
                while (iterator.hasNext()) {
                    redis.sadd("roleid_" + id, iterator.next());
                }
                redis.close();
            } catch (Exception e) {
                return Response.status(200).entity(success).build();
            }
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }
    }

    @Override
    public Response getApproveList() {
        return null;
    }

    @Override
    public Response addApprove() {
        return null;
    }

    /**
     * @param WangDian
     * @param MingCheng
     * @param ZhangHao
     * @param BuMen
     * @return
     */
    @Override
    public Response getEmployeeList(int pageNo, int pageSize, String WangDian, String MingCheng, String ZhangHao,
                                    String BuMen) {
        if (pageSize == 0) {
            pageSize = 10;
        }
        int finalPageSize = pageSize;
        try {
            return Response.status(200).entity(permissionService.getEmployeeList(pageNo, finalPageSize, WangDian, MingCheng, ZhangHao,
                    BuMen)).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response getEmployeeDetail(String id) {
        if (!StringUtil.notEmpty(id)) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setCode(ReturnCode.Success);
                    this.setMsg("ID不能为空");
                }
            }).build();
        }
        try {
            AccountEmployeeDetail accountEmployeeDetail = permissionService.getEmployeeDetail(id);
            return Response.status(200).entity(accountEmployeeDetail).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }
    }

    @Override
    public Response addEmployee(Employee employee) {
        Msg rpcMsg = new Msg();
        if (!StringUtil.notEmpty(employee.getXingMing()) || !StringUtil.notEmpty(employee.getXingBie())
                || !StringUtil.notEmpty(employee.getState()) || !StringUtil.notEmpty(employee.getWangDian())
                || !StringUtil.notEmpty(employee.getLXDH())) {
            rpcMsg.setMsg("请检查是否有未填项");
            rpcMsg.setCode(ReturnCode.Error);
        } else {
            try {
                AccountEmployeeDetail accountEmployee = permissionService.addEmployee(employee);
                Success success = new Success();
                success.setId(accountEmployee.getId());
                success.setState("添加成功");
                return Response.status(200).entity(success).build();

            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {
                    {
                        this.setCode(e.getCode());
                        this.setMsg(e.getMsg());
                    }
                }).build();
            }
        }
        return Response.status(200).entity(rpcMsg).build();
    }

    @Override
    public Response putEmployee(String id, Employee employee) {
        Msg rpcMsg = new Msg();
        if (!StringUtil.notEmpty(employee.getXingMing()) || !StringUtil.notEmpty(employee.getXingBie())
                || !StringUtil.notEmpty(employee.getState()) || !StringUtil.notEmpty(employee.getWangDian())
                || !StringUtil.notEmpty(employee.getLXDH())) {
            rpcMsg.setMsg("请检查是否有未填项");
            rpcMsg.setCode(ReturnCode.Error);
        } else {
            try {
                String employee_id = permissionService.putEmployee(id, employee);
                Success success = new Success();
                success.setId(employee_id);
                success.setState("修改成功");
                return Response.status(200).entity(success).build();
            } catch (ErrorException e) {
                return Response.status(200).entity(new Error() {
                    {
                        this.setCode(e.getCode());
                        this.setMsg(e.getMsg());
                    }
                }).build();
            }
        }
        return Response.status(200).entity(rpcMsg).build();
    }

    @Override
    public Response delEmployee(List<String> list) {
        try {
            Msg msg = permissionService.delEmployee(list);
            if (msg.getCode() == ReturnCode.Success) {
                Success success = new Success();
                success.setId("");
                success.setState(msg.getMsg());
                return Response.status(200).entity(success).build();
            } else {
                return Response.status(200).entity(msg).build();
            }
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }

    }

    @Override
    public Response getEmployeeInfo(TokenContext tokenContext) {
        System.out.println("获取个人详情");
        if (tokenContext.getRoleList().size() <= 0) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setCode(ReturnCode.Error);
                    this.setMsg("账号不属于任何角色");
                }
            }).build();
        }
        try {
            return Response.status(200).entity(new LoginUserInfo() {
                {
                    this.setUser_id(tokenContext.getUserId());
                    this.setRole_id(tokenContext.getRoleList().get(0));
                    this.setXingMing(tokenContext.getUserInfo().getCZY());
                    this.setYWWDMC(tokenContext.getUserInfo().getYWWDMC());
                    this.setYWWD(tokenContext.getUserInfo().getYWWD());
                }
            }).build();
        } catch (ErrorException e) {
            return Response.status(200).entity(new Error() {
                {
                    this.setCode(e.getCode());
                    this.setMsg(e.getMsg());
                }
            }).build();
        }
    }

}
