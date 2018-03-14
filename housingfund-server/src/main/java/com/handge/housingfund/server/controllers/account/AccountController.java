package com.handge.housingfund.server.controllers.account;

import com.handge.housingfund.common.service.account.model.LoginCABody;
import com.handge.housingfund.common.service.account.model.LoginReq;
import com.handge.housingfund.common.service.account.model.ResetPwdBody;
import com.handge.housingfund.common.service.account.model.ResetPwdEmailBody;
import com.handge.housingfund.server.controllers.ResUtils;
import com.handge.housingfund.server.controllers.account.api.IAccountAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tanyi on 2017/7/10.
 */
@Path("/account")
@Controller
public class AccountController {

    @Autowired
    IAccountAPI<Response> accountAPI;

    @Path("/login")
    @POST
    @Produces("application/json; charset=utf-8")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginReq loginBody) {
        System.out.println("账户密码登录");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.login(loginBody.getReq()));
    }

    @Path("/loginByCa")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response loginByCa(LoginCABody loginCABody) {
        System.out.println("CA登录");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.loginByCa(loginCABody.getReq()));
    }

    @Path("/curtime")
    @GET
    @Produces("application/json; charset=utf-8")
    public Response curtime() {
        System.out.println("获取系统当前时间");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.curtime());
    }


    @Path("/checkToken")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response checkToken(@QueryParam("token") String token) {
        System.out.println("检查重置密码token");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.checkToken(token));
    }

    @Path("/resetPwdSendEmail")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response resetPwdSendEmail(ResetPwdEmailBody resetPwdEmailBody) {
        System.out.println("重置密码发送邮件");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.resetPwdSendEmail(resetPwdEmailBody.getReq().getEmail()));
    }

    @Path("/resetPwd")
    @POST
    @Produces("application/json; charset=utf-8")
    public Response resetPwd(ResetPwdBody resetPwdBody) {
        System.out.println("重置密码");
        return ResUtils.wrapEntityIfNeeded(this.accountAPI.resetPwd(resetPwdBody.getReq()));
    }

}
