package com.handge.housingfund.server.controllers.loan;

import com.handge.housingfund.common.service.loan.model.GuaranteeCompanyPost;
import com.handge.housingfund.common.service.loan.model.GuaranteePut;
import com.handge.housingfund.common.service.util.RequestWrapper;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.loan.api.IGuaranteeCompanyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 待定
 */
@Path("/loan/guaranteeCompany")
@Controller
public class GuaranteeCompanyResource {

    @Autowired
    private IGuaranteeCompanyApi<Response> service;

    /**
     * 担保公司列表（审核查询,申请受理都用）
     *
     * @param GSMC   公司名称
     * @param Status 担保状态
     **/
    @Path("")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getGuarateeCompanyList(final @QueryParam("GSMC") String GSMC, final @QueryParam("Status") Integer Status) {

        System.out.println("担保公司列表（审核查询,申请受理都用）");

        return ResUtils.wrapEntityIfNeeded(this.service.getGuarateeCompanyList(GSMC, Status));
    }


    /**
     * 新增担保公司信息
     *
     * @param status 状态（0：保存 1：提交）
     **/
    @Path("")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postGuarateeCompany(final @QueryParam("status") String status, final RequestWrapper<GuaranteeCompanyPost> guaranteecompanypost) {

        System.out.println("新增担保公司信息");

        return ResUtils.wrapEntityIfNeeded(this.service.postGuarateeCompany(status, guaranteecompanypost == null ? null : guaranteecompanypost.getReq()));
    }


    /**
     * 单独修改状态（提交与撤回）
     *
     * @param guarantee_id 担保公司id
     * @param status       状态（0：提交 1：撤回）
     **/
    @Path("/{guarantee_id}/state/{status}")
    @PUT
    @Produces("application/json; charset=utf-8")
    public Response putGuarateeCompanySubmit(final @PathParam("guarantee_id") String guarantee_id, final @PathParam("status") String status) {

        System.out.println("单独修改状态（提交与撤回）");

        return ResUtils.wrapEntityIfNeeded(this.service.putGuarateeCompanySubmit(guarantee_id, status));
    }


    /**
     * 删除担保公司信息（标识符删除）
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @DELETE
    @Produces("application/json; charset=utf-8")
    public Response deleteGuarateeCompany(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("删除担保公司信息（标识符删除）");

        return ResUtils.wrapEntityIfNeeded(this.service.deleteGuarateeCompany(YWLSH));
    }


    /**
     * 修改担保公司信息
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @PUT
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putGuarateeCompany(final @PathParam("YWLSH") String YWLSH, final RequestWrapper<GuaranteePut> guaranteeput) {

        System.out.println("修改担保公司信息");

        return ResUtils.wrapEntityIfNeeded(this.service.putGuarateeCompany(YWLSH, guaranteeput == null ? null : guaranteeput.getReq()));
    }


    /**
     * 担保公司详情(打印回执单)
     *
     * @param YWLSH 业务流水号
     **/
    @Path("/{YWLSH}")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response getGuarateeCompany(final @PathParam("YWLSH") String YWLSH) {

        System.out.println("担保公司详情(打印回执单)");

        return ResUtils.wrapEntityIfNeeded(this.service.getGuarateeCompany(YWLSH));
    }


}