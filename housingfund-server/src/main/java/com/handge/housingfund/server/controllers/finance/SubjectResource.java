package com.handge.housingfund.server.controllers.finance;


import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.finance.model.SubjectModel;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.finance.api.ISubjectAPI;
import org.jboss.resteasy.spi.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by xuefei_wang on 17-8-16.
 */

@Path("/finance/subject")
@Controller
@Produces("application/json; charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON)
public class SubjectResource {

    @Autowired
    ISubjectAPI iSubjectAPI;

    /**
     * 会计科目列表
     *
     * @param kmbh     科目编号
     * @param kmmc     科目名称
     * @param kmsx     科目属性
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return
     */
    @Path("")
    @GET
    public Response searchSubjects(final @QueryParam("KMBH") String kmbh,
                                   final @QueryParam("KMMC") String kmmc,
                                   final @QueryParam("KMSX") String kmsx,
                                   final @QueryParam("pageNo") int pageNo,
                                   final @QueryParam("pageSize") int pageSize) {

        System.out.println("获取会计科目列表");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.searchSubjects(kmbh, kmmc, kmsx, pageNo, pageSize));
    }

    @Path("/list")
    @GET
    public Response getSubjectList() {

        System.out.println("获取会计科目列表（三级）");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.getSubjectList());
    }


    /**
     * 新增会计科目
     *
     * @param subjectModel 新增会计科目信息
     * @return
     */
    @Path("")
    @POST
    public Response createSubject(final RequestWrapper<SubjectModel> subjectModel) {
        System.out.println("新增会计科目信息");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.createSubject(subjectModel == null ? null : subjectModel.getReq()));
    }

    /**
     * 更新科目信息
     *
     * @param id           科目id
     * @param subjectModel 会计科目信息
     * @return
     */
    @PUT
    @Path("/{id}")
    public Response updateSubject(final @PathParam("id") String id, final RequestWrapper<SubjectModel> subjectModel) {
        System.out.println("更新会计科目信息");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.updateSubject(id, subjectModel == null ? null : subjectModel.getReq()));
    }

    /**
     * 删除科目信息
     *
     * @param id 科目id
     * @return
     */
    @DELETE
    @Path("/{id}")
    public Response delSubject(final @PathParam("id") String id) {
        System.out.println("删除科目信息");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.deleteSubject(id));
    }

    /**
     * 批量删除科目信息
     *
     * @param delList
     * @return
     */
    @Path("")
    @DELETE
    public Response delSubjects(final RequestWrapper<ArrayList<String>> delList) {
        System.out.println("批量删除科目信息");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.delSubjects(delList == null ? null : delList.getReq()));
    }

    /**
     * 获取科目详细信息
     *
     * @param id 科目id
     * @return
     */
    @GET
    @Path("/{id}")
    public Response getSubject(final @PathParam("id") String id) {
        System.out.println("获取科目详细信息");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.getSubject(id));
    }

    @GET
    @Path("/fuzzy/{param}")
    public Response getSubjects(final @PathParam("param") String param) {
        System.out.println("根据科目编号或名称模糊查询");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.getSubjects(param));
    }

    @GET
    @Path("/isExist/{param}")
    public Response isExist(final @PathParam("param") String param) {
        System.out.println("查询该科目编码是否已存在");
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.isExist(param));
    }


    @GET
    @Path("/bankacct/{no}")
    public Response getSubjectByBid(final @Context HttpRequest request,final @PathParam("no") String bidno ){
        TokenContext tokenContext = (TokenContext) request.getAttribute(Constant.Server.TOKEN_KEY);
        return ResUtils.wrapEntityIfNeeded(iSubjectAPI.getSubjectByBid(tokenContext,bidno));
    }
}

