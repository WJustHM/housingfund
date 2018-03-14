package com.handge.housingfund.server.controllers.others;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.common.service.others.model.StateMachineConfig;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * 审核流程管理
 * Created by xuefei_wang on 17-8-8.
 */
@Controller
@Path("/statemachine")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,"application/json; charset=utf-8"})
public class StateMachineController{

    @Autowired
    @Qualifier("stateMachine_service")
    private IStateMachineService stateMachineService;



//    @GET
//    public Response listTypes(final @Context HttpRequest request) {
//
//        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
//        StringBuffer buffer = new StringBuffer("{types:[");
//        try {
//            List<String> types = (List<String>) stateMachineService.listTypes();
//            types.forEach(s ->  {
//                buffer.append(s);
//                buffer.append(",");});
//            int last = buffer.lastIndexOf(",");
//            buffer.deleteCharAt(last);
//            buffer.append("]}");
//        }catch (Exception e) {
//            e.printStackTrace();
//            return Response.ok(new Error("获取业务类型出现异常：", e.getMessage())).status(201).build();
//        }
//        return Response.ok(buffer.toString()).build();
//    }

    /**
     * 查询审核流程
     * @param request
     * @param type 业务类型(归集，提取，贷款，财务)
     * @return List<StateMachineConfig>
     */
    @Path("")
    @GET
    public Response list(final @Context HttpRequest request,
                         final @QueryParam("type")  String type,
                         final @QueryParam("subtype")  String subtype,
                         final @QueryParam("ywwd")  String ywwd){
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            List<StateMachineConfig>  stateMachineConfigContainerKS = stateMachineService.list(tokenContext ,type,ywwd,subtype);
//            return Response.ok(stateMachineConfigContainerKS).build();
            return ResUtils.wrapEntityIfNeeded(Response.ok(stateMachineConfigContainerKS).build());
        }catch (ErrorException e) {
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }
    }

    /**
     * 列出所有业务类型
     * @param request
     * @return List<String>
     */
    @GET
    @Path("/types")
    public Response listTypes(final @Context HttpRequest request){
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            List<String> types  = stateMachineService.listTypes(tokenContext);
//            return Response.ok(types).build();
            return ResUtils.wrapEntityIfNeeded(Response.ok(types).build());
        }catch (Exception e) {
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("服务器内部错误").build());
        }
    }

    /**
     * 列出某业务类型的所有业务子类型
     * @param request
     * @param type 业务类型(归集，提取，贷款，财务)
     * @return List<String>
     */
    @GET
    @Path("/types/{type}")
    public Response listSubTypes(final @Context HttpRequest request,
                                 final @PathParam("type") String type){
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            List<String> types  = stateMachineService.listSubTypes(tokenContext,type);
//            return Response.ok(types).build();
            return ResUtils.wrapEntityIfNeeded(Response.ok(types).build());
        }catch (Exception e) {
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("服务器内部错误").build());
        }
    }
    /**
     * 获取审核流程详情
     * @param request
     * @param type 业务类型
     * @param subtype 业务子类型
     * @return
     */
    @GET
    @Path("/details")
    public Response update(final @Context HttpRequest request,
                           final @QueryParam("type") String type,
                           final @QueryParam("subtype") String subtype) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            List<StateMachineConfig> details =  stateMachineService.listDetails(tokenContext,type,subtype);
            return ResUtils.wrapEntityIfNeeded(Response.ok(details).status(Response.Status.OK).build());
        }catch (Exception e){
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("服务器内部错误").build());
        }

    }

    /**
     * 修改审核流程
     * @param request
     * @param stateMachineConfig 自定义状态机流程
     * @return
     */
    @Path("")
    @PUT
    public Response update(final @Context HttpRequest request,
                           final @QueryParam("ywwd") String ywwd,
                           final RequestWrapper<StateMachineConfig> stateMachineConfig) {
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonReturn is_ok =  stateMachineService.update(tokenContext,stateMachineConfig.getReq(),ywwd);
            return ResUtils.wrapEntityIfNeeded(Response.ok(is_ok).status(Response.Status.OK).build());
        }catch (ErrorException e){
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(200).entity(e.getError()).build());
        }

    }

    /**
     * test
     * @return
     */
    @Path("/test")
    @PUT
    public Response update() {
//        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        try {
            CommonReturn is_ok = stateMachineService.updateStatemachineConfig();
            return ResUtils.wrapEntityIfNeeded(Response.ok(is_ok).status(Response.Status.OK).build());
        }catch (Exception e){
            e.printStackTrace();
            return ResUtils.wrapEntityIfNeeded(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("服务器内部错误").build());
        }

    }


//    @GET
//    @Path("/{type}/{subtype}")
//    public Response listStateMachineConfs(final @PathParam("type") String type, final @PathParam("subtype") String subType) {
//        ArrayList<StateMachineConfModel> confModels = new ArrayList<>();
//        try{
//            confModels = (ArrayList<StateMachineConfModel>) stateMachineService.listStateMachineConfs(type,subType);
//        } catch (Exception e){
//            e.printStackTrace();
//            return Response.ok(new Error("获取对应业务类型的流程出现异常：", e.getMessage())).status(201).build();
//        }
//        return Response.ok(confModels).build();
//    }
//
//
//    @PUT
//    @Path("/{type}/{subtype}/{id}")
//    public Response updateStateMachineConf(final @PathParam("id") String id,
//                                           final @PathParam("type") String type ,
//                                           final @PathParam("subtype") String subtype,
//                                           StateMachineConfModel stateMachineConfModel) {
//        System.out.println("信息： type: " + stateMachineConfModel);
//        stateMachineConfModel.setType(type);
//        stateMachineConfModel.setSubType(subtype);
//        return Response.ok(stateMachineService.updateStateMachineConf(id, stateMachineConfModel)).build();
//    }
//
//
//    @DELETE
//    @Path("/{type}/{subtype}/{id}")
//    public Response deleteStateMachineConf(final @PathParam("id") String id) {
//        return Response.ok(stateMachineService.deleteStateMachineConf(id)).build();
//    }
//
//
//    @Path("/addConf/{type}/{subtype}")
//    @PUT
//    public Response addStateMachineConf(final @PathParam("type") String type ,
//                                        final @PathParam("subtype") String subtype,
//                                        StateMachineConfModel stateMachineConfModel) {
//        stateMachineConfModel.setSubType(subtype);
//        stateMachineConfModel.setType(type);
//        return Response.ok(stateMachineService.addStateMachineConf(stateMachineConfModel)).build();
//    }
//
//    @Path("/addConfs/{type}/{subtype}")
//    @PUT
//    public Response addStateMachineConfs(final @PathParam("type") String type ,
//                                         final @PathParam("subtype") String subtype,
//                                         ArrayList<StateMachineConfModel> confModels) {
//        return Response.ok(stateMachineService.addStateMachineConfs(confModels)).build();
//    }
//
//    @Path("/updateConfs/{type}/{subtype}")
//    @POST
//    public Response updateStateMachineConfs(final @PathParam("type") String type,
//                                            final @PathParam("subtype") String subtype,
//                                            ArrayList<StateMachineConfModel> confModels) {
//        System.out.println("获取到列表信息为： " + confModels);
//        return Response.ok(stateMachineService.updateStateMachineConfs(type, subtype, confModels)).build();
//    }
//
//    @GET
//    @Path("/listWorkstation/{type}/{subtype}")
//    public Response listWorkstation(final @PathParam("type") String type,
//                                    final @PathParam("subtype") String subtype) {
//        StringBuffer buffer = new StringBuffer("{workstation:[");
//        List<String> workstations = (List<String>) stateMachineService.listWorkstation(type, subtype);
//        workstations.stream().forEach(workstation -> {
//            if(!workstation.equals("null") || workstation != null){
//                buffer.append(workstation);
//                buffer.append(",");
//            }
//        });
//        int last = buffer.lastIndexOf(",");
//        buffer.deleteCharAt(last);
//        buffer.append("]}");
//        return Response.ok(buffer.toString()).build();
//    }
//
//    @GET
//    @Path("/listWorkstationBusinessType/")
//    public Response listWorkstationBusinessType(final @QueryParam("workstation") String workstation) {
//        StringBuffer buffer = new StringBuffer("{workstationBusinessType:[");
//        List<String> types = (List<String>) stateMachineService.listWorkstationBusinessType(workstation);
//        types.stream().forEach(type -> {
//            if(buffer.toString().matches(".*" +type+"*.")){
//                return;
//            }else{
//                buffer.append(type);
//                buffer.append(",");
//            }
//        });
//        int last = buffer.lastIndexOf(",");
//        if(last > 0) buffer.deleteCharAt(last);
//        buffer.append("]}");
//        return Response.ok(buffer.toString()).build();
//    }
//
//    @GET
//    @Path("/listWorkstationBusinessSubType/")
//    public Response listWorkstationBusinessSubType(final @QueryParam("type") String type,
//                                                   final @QueryParam("workstation") String workstation) {
//        StringBuffer buffer = new StringBuffer("{workstationBusinessSubType:[");
//        List<String> subTypes = (List<String>) stateMachineService.listWorkstationBusinessSubType(workstation,type);
//        subTypes.stream().forEach(subtype -> {
//            if(buffer.toString().matches(".*" +subtype+"*.")) return;
//            else{
//                buffer.append(subtype);
//                buffer.append(",");
//            }
//        });
//        int last = buffer.lastIndexOf(",");
//        buffer.deleteCharAt(last);
//        buffer.append("]}");
//        return Response.ok(buffer.toString()).build();
//    }
//
//    @GET
//    @Path("/listWorkstationBusinessSMConfs/")
//    public Response listWorkstationBusinessStateMachineConfs(final @QueryParam("workstation") String workstation,
//                                                             final @QueryParam("type") String type,
//                                                             final @QueryParam("subtype") String subtype) {
//        return Response.ok(stateMachineService.listWorkstationBusinessStateMachineConfs(workstation, type, subtype)).status(200).build();
//    }
//
//    @POST
//    @Path("/addWorkstationBusinessSMConf/")
//    public Response addWorkstationBusinessStateMachineConf(final @QueryParam("workstation") String workstation,
//                                                           StateMachineConfModel stateMachineConfModel) {
//        return Response.ok(stateMachineService.addWorkstationBusinessStateMachineConf(workstation,stateMachineConfModel)).status(200).build();
//    }
//
//    @POST
//    @Path("/updateWorkstationonf")
//    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
//    public Response updateWorkstationBusinessStateMachineConf(final @QueryParam("workstation") String workstation,
//                                                              StateMachineConfModel stateMachineConfModel) {
//        return Response.ok(stateMachineService.updateWorkstationBusinessStateMachineConf(workstation,stateMachineConfModel)).status(200).build();
//    }
//
//    @POST
//    @Path("/updateWorkstationConfs")
//    public Response updateWorkstationBusinessStateMachineConfs(final @QueryParam("workstation") String workstation,
//                                                               ArrayList<StateMachineConfModel> stateConfigs) {
//        return Response.ok(stateMachineService.updateWorkstationBusinessStateMachineConfs(workstation, stateConfigs)).status(200).build();
//    }
//    @GET
//    @Path("/addWorkStation/{workstation}/{type}")
//    public Response addWorkStation(String workstation) {
//        return null;
//    }
//
//    @GET
//    @Path("/listAuditConfsBySubType")
//    public Response listAuditConfsBySubType(final @QueryParam("subType") String subType,
//                                            final @QueryParam("role") String role,
//                                            final @QueryParam("workstation") String workstation){
//        List<StateMachineConfModel> auditConfs = (List<StateMachineConfModel>) stateMachineService.listAuditConfsBySubType(subType, role, workstation);
//        return Response.ok(auditConfs).status(200).build();
//    }
//
//    @GET
//    @Path("/listAuditConfs")
//    public Response listAuditConfs(final @QueryParam("type") String type,
//                                   final @QueryParam("subType") String subType,
//                                   final @QueryParam("role") String role,
//                                   final @QueryParam("workstation") String workstation){
//        List<StateMachineConfModel> auditConfs = (List<StateMachineConfModel>) stateMachineService.listAuditConfs(type, subType, role, workstation);
//        return Response.ok(auditConfs).status(200).build();
//    }
//
//    private class Error{
//        private String msg;
//        private String description;
//
//        public Error(String msg, String description) {
//            this.msg = msg;
//            this.description = description;
//        }
//
//        public Error() {
//        }
//
//        public String getMsg() {
//            return msg;
//        }
//
//        public void setMsg(String msg) {
//            this.msg = msg;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//    }
}
