package com.handge.housingfund.server.controllers.account.api.impl;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.INetworkManagementService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.server.controllers.account.api.INetworkManagementAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by gxy on 17-7-15.
 */
@Component
public class NetworkManagementAPI implements INetworkManagementAPI<Response> {

    @Autowired
    INetworkManagementService networkService;

    @Override
    public Response getNetworkList(int pageNo, int pageSize, String MingCheng) {
        try {
            if (pageSize == 0) {
                pageSize = 10;
            }
            int finalPageSize = pageSize;
            return Response.status(200).entity(networkService.getNetworkList(pageNo, finalPageSize, MingCheng)).build();
        } catch (ErrorException e) {
            Msg msg = new Msg();
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }
    }

    @Override
    public Response getNetworkDetail(String id) {
        if (!StringUtil.notEmpty(id)) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("网点ID不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            NetworkWithDepartment cAccountNetwork = networkService.getNetworkDetail(id);
            if (cAccountNetwork != null) {
                return Response.status(200).entity(cAccountNetwork).build();
            } else {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("网点不存在");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        } catch (ErrorException e) {
            Msg msg = new Msg();
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }

    }

    @Override
    public Response addNetwork(TokenContext tokenContext, Network network) {
        if (!StringUtil.notEmpty(network.getBLSJ()) || !StringUtil.notEmpty(network.getDiQu())
                || !StringUtil.notEmpty(network.getFZR()) || !StringUtil.notEmpty(network.getMingCheng())
                || !StringUtil.notEmpty(network.getLXDH())
                || !StringUtil.notEmpty(network.getXXDZ())) {
            Msg msg = new Msg();
            msg.setCode(ReturnCode.Error);
            msg.setMsg("参数有误");
            return Response.status(200).entity(msg).build();
        }
        try {
            String id = networkService.addNetwork(tokenContext, network);
            Success success = new Success();
            success.setId(id);
            success.setState("添加成功");
            return Response.status(200).entity(success).build();
        } catch (ErrorException e) {
            Msg msg = new Msg();
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }

    }

    @Override
    public Response updateNetwork(TokenContext tokenContext, String id, Network network) {
        Msg msg = new Msg();
        try {
            if (!StringUtil.notEmpty(network.getBLSJ()) || !StringUtil.notEmpty(network.getDiQu())
                    || !StringUtil.notEmpty(network.getFZR()) || !StringUtil.notEmpty(network.getMingCheng())
                    || !StringUtil.notEmpty(network.getXXDZ())) {
                msg.setCode(ReturnCode.Error);
                msg.setMsg("参数有误");
                return Response.status(200).entity(msg).build();
            }
            if (StringUtil.notEmpty(id)) {
                if (!networkService.putNetwork(tokenContext, id, network).equals("")) {
                    Success success = new Success();
                    success.setId(id);
                    success.setState("添加成功");
                    return Response.status(200).entity(success).build();
                } else {
                    msg.setCode(ReturnCode.Error);
                    msg.setMsg("修改失败");
                }
            } else {
                msg.setCode(ReturnCode.Error);
                msg.setMsg("网点ID不能为空");
            }
            return Response.status(200).entity(msg).build();
        } catch (ErrorException e) {
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }
    }

    @Override
    public Response deleteNetwork(List<String> list) {
        Msg msg = new Msg();
        boolean res = true;
        for (int i = 0; i < list.size(); i++) {
            if (!StringUtil.notEmpty(list.get(i))) {
                res = false;
                break;
            }
        }
        if (res) {
            try {
                List<Delres> delres = networkService.delNetWork(list);
                SuccessWithList success = new SuccessWithList();
                success.setIds(delres);
                success.setState("删除成功");
                return Response.status(200).entity(success).build();
            } catch (ErrorException e) {
                msg.setCode(ReturnCode.Error);
                msg.setMsg(e.getMsg());
                return Response.status(200).entity(msg).build();
            }
        } else {
            msg.setCode(ReturnCode.Error);
            msg.setMsg("id不能为空");
        }
        return Response.status(200).entity(msg).build();
    }

    @Override
    public Response getDepartmentList(String id) {
        // if (!StringUtil.notEmpty(id)) {
        // return Response.status(200).entity(new RpcMsg() {{
        // this.setMsg("网点ID不能为空");
        // this.setCode(ReturnCode.Error);
        // }}).build();
        // }
        Msg msg = new Msg();
        try {

            List<DepartmentWithNetwork> cAccountDepartment = networkService.getDepartmentList(id);
            if (cAccountDepartment != null) {
                return Response.status(200).entity(cAccountDepartment).build();
            } else {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("部门列表获取失败");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        } catch (ErrorException e) {
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }

    }
    //
    // @Override
    // public Response getDepartmentDetial(String id) {
    // return Response.status(200).entity(new RpcMsg() {{
    //
    // }}).build();
    // }

    @Override
    public Response addDepartment(Department department) {
        Msg msg = new Msg();
        if (!StringUtil.notEmpty(department.getFZR()) || !StringUtil.notEmpty(department.getMingCheng())
                || !StringUtil.notEmpty(department.getWangDian_id()) || !StringUtil.notEmpty(department.getLXDH())) {
            msg.setCode(ReturnCode.Error);
            msg.setMsg("参数有误");
            return Response.status(200).entity(msg).build();
        }
        try {
            String id = networkService.putDepartment(department);
            if (StringUtil.notEmpty(id)) {
                Success success = new Success();
                success.setId(id);
                success.setState("添加成功");
                return Response.status(200).entity(success).build();
            } else {
                msg.setCode(ReturnCode.Error);
                msg.setMsg("添加失败");
            }
            return Response.status(200).entity(msg).build();
        } catch (ErrorException e) {
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }

    }

    // @Override
    // public Response updateDepartment(String id, Department department) {
    // return Response.status(200).entity(new RpcMsg() {{
    //
    // }}).build();
    // }

    @Override
    public Response deleteDepartment(List<String> list) {
        Msg msg = new Msg();
        boolean res = true;
        for (int i = 0; i < list.size(); i++) {
            if (!StringUtil.notEmpty(list.get(i))) {
                res = false;
                break;
            }
        }
        if (res) {
            try {
                List<Delres> delres = networkService.delDepartment(list);
                SuccessWithList success = new SuccessWithList();
                success.setIds(delres);
                success.setState("删除成功");
                return Response.status(200).entity(success).build();
            } catch (ErrorException e) {
                msg.setCode(ReturnCode.Error);
                msg.setMsg(e.getMsg());
                return Response.status(200).entity(msg).build();
            }
        } else {
            msg.setCode(ReturnCode.Error);
            msg.setMsg("id不能为空");
        }
        return Response.status(200).entity(msg).build();
    }

    @Override
    public Response getDepartmentMember(String id) {
        Msg msg = new Msg();
        try {
            if (StringUtil.notEmpty(id)) {
                return Response.status(200).entity(networkService.getDepartmentMember(id)).build();
            } else {
                msg.setCode(ReturnCode.Error);
                msg.setMsg("网点ID不能为空");
                return Response.status(200).entity(msg).build();
            }
        } catch (ErrorException e) {
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }
    }

    @Override
    public Response getArea(String id) {
        return Response.status(200).entity(new Msg() {
            {

            }
        }).build();
    }
}
