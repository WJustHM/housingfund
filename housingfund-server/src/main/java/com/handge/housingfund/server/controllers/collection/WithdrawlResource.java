
package com.handge.housingfund.server.controllers.collection;

import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.BatchWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.ReWithdrawlsInfo;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.WithdrawlsDetailInfo;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.collection.api.WithdrawlApi;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei_wang on 17-6-26.
 */
@Controller
@Path("/withdrawls")
public class WithdrawlResource {

    @Autowired
    private WithdrawlApi<Response> service;

    /**
     * 　获取提取业务详细信息
     * @param taskId 　业务流水号
     * @return Response
     */
    @GET
    @Path("/tasks/{taskId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //complete
    public Response getTask(@Context HttpRequest httpRequest, final @PathParam("taskId") String taskId) {
        System.out.println("getTask:  " + taskId);
        return ResUtils.wrapEntityIfNeeded(service.getWithdrawlDetails((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),taskId));
    }

    /**
     * 修改提取业务信息．
     *
     * @param taskId 　业务流水号
     * @param info   　BaseInfo,
     * @param op      提交1 保存0 ,
     * @return Response
     */
    @Path("/{taskId}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //complete
    public Response updateTask(@Context HttpRequest httpRequest, @QueryParam("operation") String op,@PathParam("taskId") String taskId, RequestWrapper<ArrayList<ReWithdrawlsInfo>> info) {
        System.out.println("update:  " + taskId);
        return ResUtils.wrapEntityIfNeeded(service.updateWithdrawlsTask((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),op,taskId,info.getReq()));
    }

    /**
     * 保存或提交提取业务
     * @param info 　WithdrawlsDetailInfo
     * @param operation 　提交1 保存2
     * @return　Response
     */
    @Path("")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //complete
    public Response saveOrSubmit(@Context HttpRequest httpRequest, @QueryParam("operation")String operation, RequestWrapper<ArrayList<WithdrawlsDetailInfo>> info) {
            System.out.println("SaveOrSubmit:  " + info);
            return ResUtils.wrapEntityIfNeeded(service.saveOrSubmitWithdrawlsTask((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),operation,info.getReq()));
    }

    /**
     * 撤销一个提取业务．　这个是对于已经进入待审核状态的业务操作．　业务有待审核转入到新建状态
     *
     * @param taskId 　业务流水号
     * @return Response
     */
    @Path("/revoke/{taskId}")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //待完善
    public Response revokeTask(@Context HttpRequest httpRequest, @PathParam("taskId") String taskId) {
        System.out.println("revokeTask:  " + taskId);
        return ResUtils.wrapEntityIfNeeded(service.revokeWithdrawlsTask((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),taskId));
    }

    /**
     * 搜索提取记录
     *
     * @param XingMing  　个人姓名
     * @param dwmc   　单位名称
     * @param grzh   　个人帐号
     * @param ywzt   　业务状态 //00所有 01新建 02待审核 03待入账 04已入账 05审核不通过
     * @param begain 　起始时间
     * @param end    　结束时间
     * @return　Response
     */
    @Path("")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response searchWithdrawls(@Context HttpRequest httpRequest, final @QueryParam("XingMing") String XingMing,
                                final @QueryParam("dwmc") String dwmc,
                                final @QueryParam("grzh") String grzh,
                                final @QueryParam("shzt")  String ywzt,
                                final @QueryParam("pageNo")  String pageNo,
                                final @QueryParam("pageSize")  String pageSize,
                                final @QueryParam("ZJHM")  String zjhm,
                                final @QueryParam("ZongE")  String ZongE,
                                final @QueryParam("TQYY")  String tqyy,
                                final @QueryParam("begain")  String begain,
                                     final @QueryParam("ywwd")    String ywwd,
                                     final @QueryParam("yhmc")  String yhmc,
                                final @QueryParam("end") String end) {
        System.out.println("searchTasks: XingMing "  +XingMing +"    dwmc:"+dwmc +"   grzh:"+grzh  +"  ywzt:"+ywzt + "  begain:"+begain + "  end:"+end+"zjhm: "+ zjhm +"ZongE" + ZongE +"tqyy:"+tqyy);
        return ResUtils.wrapEntityIfNeeded(this.service.searchWithdrawlsTasks((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),XingMing,dwmc,grzh,ywzt,ywwd,yhmc,begain,end,pageNo,pageSize,zjhm,ZongE,tqyy));

    }

    /**
     * 搜索提取记录new
     *
     * @param XingMing  　个人姓名
     * @param dwmc   　单位名称
     * @param grzh   　个人帐号
     * @param ywzt   　业务状态 //00所有 01新建 02待审核 03待入账 04已入账 05审核不通过
     * @param begain 　起始时间
     * @param end    　结束时间
     * @return　Response
     */
    @Path("/new")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response searchWithdrawlsNew(@Context HttpRequest httpRequest, final @QueryParam("XingMing") String XingMing,
                                     final @QueryParam("dwmc") String dwmc,
                                     final @QueryParam("grzh") String grzh,
                                     final @QueryParam("shzt")  String ywzt,
                                     final @QueryParam("action")  String action,
                                        final @QueryParam("ZJHM")  String zjhm,
                                        final @QueryParam("ZongE")  String ZongE,
                                     final @QueryParam("marker")  String marker,
                                     final @QueryParam("pageSize")  String pageSize,
                                     final @QueryParam("begain")  String begain,
                                        final @QueryParam("ywwd")  String ywwd,
                                        final @QueryParam("yhmc")  String yhmc,
                                     final @QueryParam("end") String end) {
        System.out.println("searchTasks: XingMing "  +XingMing +"    dwmc:"+dwmc +"   grzh:"+grzh  +"  ywzt:"+ywzt + "  begain:"+begain + "  end:"+end+"zjhm: "+ zjhm +"ZongE" + ZongE);
        return ResUtils.wrapEntityIfNeeded(this.service.searchWithdrawlsTasksNew((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),XingMing,dwmc,grzh,ywzt,ywwd,yhmc,begain,end,action,marker,pageSize,zjhm,ZongE));

    }

    /**
     * @param info 批量操作提取信息
     * @return
     */
    @Path("/batch")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //complete
    public Response batchHandler(@Context HttpRequest httpRequest, RequestWrapper<BatchWithdrawlsInfo> info) {
        List<String> tasks = info.getReq().getIds();
        for (String s : tasks) {
            System.out.println(s + "   " + info.getReq().getAction());
        }
       return ResUtils.wrapEntityIfNeeded(service.batchOpWithdrawls((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),info.getReq()));
    }

    /**
     * 打印个人提取记录
     * @param zjhm 个人账号
     * @return
     */
    @Path("/printRecords")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response printWithdrawlsRecords(@Context HttpRequest httpRequest,
                                           final @QueryParam("zjhm") String zjhm,
                                           final @QueryParam("begain") String begain,
                                           final @QueryParam("end") String end,
                                           final @QueryParam("GRZH") String grzh,
                                           final @QueryParam("XingMing") String XingMing,
                                           final @QueryParam("pageNo") String pageNo,
                                           final @QueryParam("pageSize") String pageSize){
        System.out.println("printWithdrawlsRecords" + "zjhm" + zjhm + "grzh" + grzh );
        return ResUtils.wrapEntityIfNeeded(this.service.printWithdrawlsRecords((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),zjhm,begain,end,pageNo,pageSize,grzh, XingMing));
    }
    /**
     * 搜索个人提取记录
     * @param zjhm
     * @param begain
     * @param end
     * @return
     */
    @Path("/records")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response searchWithdrawlRecords(@Context HttpRequest httpRequest,
                                           final @QueryParam("zjhm") String zjhm,
                                           final @QueryParam("GRZH") String grzh,
                                           final @QueryParam("XingMing") String XingMing,
                                        final @QueryParam("pageNo") String pageNo,
                                        final @QueryParam("pageSize") String pageSize,
                                        final @QueryParam("begain") String begain,
                                        final @QueryParam("end")  String end){
        System.out.println("get IndiRecords: " + "zjhm" + zjhm + "begain" +begain +"end" +end);
        return ResUtils.wrapEntityIfNeeded(this.service.searchWithdrawlsRecords((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),zjhm,begain,end,pageNo,pageSize, grzh, XingMing));
    }
    @Path("/getNextDate")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getNextDate(@Context HttpRequest httpRequest,final @QueryParam("FSE") String  fse,final @QueryParam("YHKE") String yhke){
        System.out.println("getNextDate");
        return ResUtils.wrapEntityIfNeeded(this.service.getNextDate((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),fse,yhke));
    }

    /**
     * 获取发生利息额
     * @param grzh
     * @return
     */
    @Path("/getFSLXE")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getFslxe(@Context HttpRequest httpRequest,final @QueryParam("GRZH") String grzh){
        System.out.println("getFslxe");
        return ResUtils.wrapEntityIfNeeded(this.service.getFslxe((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),grzh));
    }

    /**
     * 获取只读部分
     * @param zjhm
     * @param type 0 第一次 1 第二次
     * @return
     */
    @Path("/readOnly/{zjhm}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReadOnly(@Context HttpRequest httpRequest,final @PathParam("zjhm") String zjhm,
                                final @QueryParam("type") String type){
        System.out.println("getReadOnly");
        return ResUtils.wrapEntityIfNeeded(this.service.getWithdrawlsReadOnly((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),zjhm,type));
    }


    /**
     * 处理失败的提取业务
     * @param httpRequest
     * @param ywlsh
     * @param operation 0入账成功 1作废
     * @return
     */
    @PUT
    @Path("/failed")
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response getFse(@Context HttpRequest httpRequest,@QueryParam("ywlsh") String ywlsh,
                           @QueryParam("operation") String operation
                           ){
        return ResUtils.wrapEntityIfNeeded(this.service.doFailedWithdrawl((TokenContext)httpRequest.getAttribute(Constant.Server.TOKEN_KEY),ywlsh,operation));
    }


}
