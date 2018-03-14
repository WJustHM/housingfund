package com.handge.housingfund.server.filters;

import com.google.gson.Gson;
import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Created by xuefei_wang on 17-9-5.
 */
@Service
@PreMatching
@Priority(2000)
public class TokenContextProcessFilter implements ContainerRequestFilter {

    @SuppressWarnings("unchecked")
    @Override
    public void filter(ContainerRequestContext requestContext) {
        boolean skip = false;
        if (requestContext.getProperty("skip") != null) {
            skip = (boolean) requestContext.getProperty("skip");
        }
        if (skip == true) {
            return;
        }
        TokenContext tokenContext = new TokenContext();
        Map<String,String> data = (Map<String,String>)requestContext.getProperty("data");
        Object userId = data.get("userId");
        Object userType = data.get("type");
        if (null == userId || userType == null) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("非法用户").build());
            return;
        }
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(data.get("userinfo"),UserInfo.class);
        if (userInfo == null) {
            requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("未知用户").build());
            return;
        }
        tokenContext.setRoleList(gson.fromJson(data.get("roleList"),List.class));
        tokenContext.setUserType(data.get("type"));
        tokenContext.setUserId(data.get("userId"));
        tokenContext.setUserInfo(userInfo);
        tokenContext.setChannel("核心业务系统");
        requestContext.setProperty(Constant.Server.TOKEN_KEY, tokenContext);
    }

}
