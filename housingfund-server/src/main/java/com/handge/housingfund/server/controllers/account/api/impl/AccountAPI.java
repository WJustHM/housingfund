package com.handge.housingfund.server.controllers.account.api.impl;

import com.google.gson.Gson;
import com.handge.housingfund.common.Constant;
import com.handge.housingfund.common.configure.Configure;
import com.handge.housingfund.common.service.account.IAccountService;
import com.handge.housingfund.common.service.account.IPermissionService;
import com.handge.housingfund.common.service.account.IUserService;
import com.handge.housingfund.common.service.account.UserTypeConst;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.server.controllers.account.api.IAccountAPI;
import org.apache.commons.configuration2.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tanyi on 2017/7/10.
 */
@Component
public class AccountAPI implements IAccountAPI<Response> {
    @Autowired
    IAccountService accountService;

    @Autowired
    IUserService userService;

    @Autowired
    IPermissionService permissionService;

    @Autowired
    IActionService actionService;

    Configuration serverConf = Configure.getInstance().getConfiguration(Constant.SERVER_CONF);

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    int expires = serverConf.getInt("token_expirestime", 1800);

    @Override
    public Response login(Login login) {
        Msg msg = new Msg();
        try {
            if (StringUtil.notEmpty(login.getUsername())) {
                if (StringUtil.notEmpty(login.getPwd())) {
                    try {
                        RpcAuth auth = accountService.verifPwd(login.getUsername(), login.getPwd());
                        if (auth != null) {
                            String token = accountService.getToken(auth.getUser_id(), auth.getType());
                            LoginData loginData = new LoginData();
                            loginData.setToken(token);
                            loginData.setId(auth.getUser_id());
                            cacheUserInfo(auth, token);
                            return Response.status(200).entity(loginData).build();
                        }
                        msg.setCode(ReturnCode.Error);
                        msg.setMsg("账号或密码错误");
                    } catch (Exception e) {
                        msg.setCode(ReturnCode.Error);
                        msg.setMsg(e.getMessage());
                    }
                } else {
                    msg.setCode(ReturnCode.Error);
                    msg.setMsg("密码不能为空");
                }
            } else {
                msg.setCode(ReturnCode.Error);
                msg.setMsg("用户名不能为空");
            }
            return Response.status(200).entity(msg).build();
        } catch (
                ErrorException e)

        {
            msg.setCode(ReturnCode.Error);
            msg.setMsg(e.getMsg());
            return Response.status(200).entity(msg).build();
        }

    }

    @Override
    public Response loginByCa(LoginCA loginCA) {
        try {
            if (!StringUtil.notEmpty(loginCA.getCert()) || !StringUtil.notEmpty(loginCA.getInfo())
                    || !StringUtil.notEmpty(loginCA.getSigninfo()) || !StringUtil.notEmpty(loginCA.getUsername())) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("参数不能为空");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
            RpcAuth auth = accountService.verifyCa(loginCA);
            if (auth == null) {
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("CA认证失败");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
            String token = accountService.getToken(auth.getUser_id(), auth.getType());

            LoginData loginData = new LoginData();
            loginData.setToken(token);
            loginData.setId(auth.getUser_id());
            cacheUserInfo(auth, token);
            return Response.status(200).entity(loginData).build();
        } catch (Exception e) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg(e.getMessage());
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }

    }

    public void cacheUserInfo(RpcAuth auth, String token) throws Exception {
        JedisCluster redis = redisCache.getJedisCluster();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("userId", auth.getUser_id());
        data.put("type", String.valueOf(auth.getType()));
        //获取用户信息
        UserInfo userInfo = null;
        switch (auth.getType()) {
            case UserTypeConst.INDIVIDUAL: {
                userInfo = userService.getPersonInfo(auth.getUser_id().toString());
                break;
            }
            case UserTypeConst.EMPLOYEE: {
                userInfo = userService.getEmployeeInfo(auth.getUser_id().toString());
                break;
            }
            case UserTypeConst.UNIT: {
                userInfo = userService.getUnitInfo(auth.getUser_id().toString());
                break;
            }
            case UserTypeConst.HOUSDEVELOPER: {
                userInfo = userService.getHousdeveloperInfo(auth.getUser_id().toString());
                break;
            }
        }
        Gson gson = new Gson();
        String userInfoStr = gson.toJson(userInfo);
        data.put("userinfo", userInfoStr);
        //获取角色列表及权限
        List<String> roleList = permissionService.getRoleIDByUserId(auth.getUser_id().toString());
        roleList.add("0");
        HashSet<String> actionCodes = new HashSet<>();
        for (String roleID : roleList) {
            HashSet<String> tmpActions = actionService.getActionCodesByRole(roleID);
            actionCodes.addAll(tmpActions);
        }
        String actionCodesStr = gson.toJson(actionCodes);
        roleList.remove("0");
        String roleListStr = gson.toJson(roleList);
        data.put("roleList", roleListStr);
        data.put("actionCodes", actionCodesStr);
        //获取keywords
        HashSet<String> keywords = actionService.getActionKeywords();
        String keywordsStr = gson.toJson(keywords);
        data.put("keywords", keywordsStr);
        redis.hmset("token_" + token + ":data", data);
        redis.expire("token_" + token + ":data", expires);
        redis.close();
    }

    @Override
    public Response curtime() {
        Success success = new Success();
        success.setId(String.valueOf(new Date().getTime()));
        success.setState("");
        return Response.status(200).entity(success).build();
    }

    @Override
    public Response checkToken(String token) {
        Msg msg = new Msg();
        if (token == null || token.equals("")) {
            msg.setMsg("token不能为空");
            msg.setCode(ReturnCode.Error);
        } else {
            try {
                // 检测token是否被吊销
                JedisCluster redis = redisCache.getJedisCluster();
                if (redis.exists("Invalid_" + token)) {
                    redis.close();
                    msg.setMsg("链接失效，请重新申请重置密码");
                    msg.setCode(ReturnCode.Error);
                } else {
                    // 验证token有效性
                    TokenData data = JWTTokenCentral.decodeToken(token);
                    if (data.getCode() == 1 && data.getType() == 101) {
                        Success success = new Success();
                        success.setId("");
                        success.setState("Token有效");
                        return Response.status(200).entity(success).build();
                    } else {
                        msg.setMsg("Token失效，请重新申请重置密码");
                        msg.setCode(ReturnCode.Error);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg.setMsg(e.getMessage());
                msg.setCode(ReturnCode.Error);
            }
        }
        return Response.status(200).entity(msg).build();
    }

    @Override
    public Response resetPwdSendEmail(String email) {
        if (!StringUtil.notEmpty(email)) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("email地址不能为空");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            Msg msg = accountService.resetPwdSendEmail(email);
            if (msg.getCode() == ReturnCode.Success) {
                Success success = new Success();
                success.setId("");
                success.setState(msg.getMsg());
                return Response.status(200).entity(success).build();
            } else {
                return Response.status(200).entity(msg).build();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg(e.getMessage());
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
    }

    @Override
    public Response resetPwd(ResetPwd resetPwd) {
        if (!resetPwd.getPwd().equals(resetPwd.getRepwd())) {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("两次密码不一致");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        try {
            JedisCluster redis = redisCache.getJedisCluster();
            if (redis.exists("Invalid_" + resetPwd.getToken())) {
                redis.close();
                return Response.status(200).entity(new Msg() {
                    {
                        this.setMsg("链接失效，请重新申请重置密码");
                        this.setCode(ReturnCode.Error);
                    }
                }).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg(e.getMessage());
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
        TokenData data = JWTTokenCentral.decodeToken(resetPwd.getToken());
        if (data.getCode() == 1 && data.getType() == 101) {
            Msg msg = accountService.resetPwd(data.getUserid(), resetPwd);
            if (msg.getCode() == ReturnCode.Success) {
                Success success = new Success();
                success.setId("");
                success.setState(msg.getMsg());
                return Response.status(200).entity(success).build();
            } else {
                return Response.status(200).entity(msg).build();
            }
        } else {
            return Response.status(200).entity(new Msg() {
                {
                    this.setMsg("链接失效，请重新申请重置密码");
                    this.setCode(ReturnCode.Error);
                }
            }).build();
        }
    }

}
