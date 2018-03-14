package com.handge.housingfund.account.service.account;

import com.handge.housingfund.account.util.PasswordUtil;
import com.handge.housingfund.common.service.account.AccountRpcService;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.RpcAuth;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAuthDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.entities.CAuth;
import com.handge.housingfund.database.entities.CRole;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.SearchOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tanyi on 2017/7/13.
 */
@Component
public class AccountRpcServiceImpl implements AccountRpcService {

    @Autowired
    ICAuthDAO iAuthDAO;
    @Autowired
    ICRoleDAO roleDAO;

    /**
     * 用户注册
     * <p>
     * 用户信息写入c_auth表，添加auth_role表记录
     *
     * @param rpcAuth 认证信息，所有信息应经过验证
     * @return
     */
    @Override
    public Msg registerAuth(RpcAuth rpcAuth) {
        Msg rpcMsg = new Msg();
        try {
            String pwd;
            if (!StringUtil.notEmpty(rpcAuth.getPassword())) {
                pwd = null;
            } else {
                pwd = PasswordUtil.encryptPwd(rpcAuth.getPassword());
//                pwd = PasswordUtil.encryptPwd(rpcAuth.getPassword() + rpcAuth.getUser_id()); //增强密码强度，相同密码，生成密文不同
            }
            //写入数据库
            CAuth auth1 = new CAuth();
            if (rpcAuth.getType() == 2) {
                List<CRole> roles = new ArrayList<>();
                CRole role = roleDAO.get(rpcAuth.getRole_id());
                if (role != null) {
                    roles.add(role);
                    auth1.setState(rpcAuth.getState() == 1);
                    auth1.setPassword(pwd);
                    auth1.setType(rpcAuth.getType());
                    auth1.setUser_id(rpcAuth.getUser_id());
                    auth1.setUsername(rpcAuth.getUsername());
                    auth1.setEmail(rpcAuth.getEmail());
                    auth1.setRoles(roles);
                    iAuthDAO.save(auth1);
                    rpcMsg.setCode(ReturnCode.Success);
                    rpcMsg.setMsg("用户注册成功");
                } else {
                    rpcMsg.setCode(ReturnCode.Error);
                    rpcMsg.setMsg("角色不存在");
                }
            } else {
                auth1.setState(rpcAuth.getState() == 1);
                auth1.setPassword(pwd);
                auth1.setType(rpcAuth.getType());
                auth1.setUser_id(rpcAuth.getUser_id());
                auth1.setUsername(rpcAuth.getUsername());
                auth1.setEmail(rpcAuth.getEmail());
                auth1.setRoles(new ArrayList<>());
                iAuthDAO.save(auth1);
                rpcMsg.setCode(ReturnCode.Success);
                rpcMsg.setMsg("用户注册成功");
            }


        } catch (Exception e) {
            e.printStackTrace();
            rpcMsg.setCode(ReturnCode.Error);
            rpcMsg.setMsg(e.getMessage());
        }
        return rpcMsg;
    }

    /**
     * 修改用户
     *
     * @param user_id
     * @param rpcAuth
     * @return
     */
    @Override
    public Msg updateAuth(String user_id, RpcAuth rpcAuth) {
        Msg rpcMsg = new Msg();
        try {
            //写入数据库
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("user_id", user_id);
            List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
            CAuth auth1 = auths.size() > 0 ? auths.get(0) : null;
            if (auth1 != null) {
                if (rpcAuth.getType() == 2 && StringUtil.notEmpty(rpcAuth.getRole_id())) {
                    List<CRole> roles = new ArrayList<>();
                    CRole role = roleDAO.get(rpcAuth.getRole_id());
                    if (role != null) {
                        roles.add(role);
                        auth1.setState(rpcAuth.getState() == 1);
                        auth1.setUsername(rpcAuth.getUsername());
                        auth1.setEmail(rpcAuth.getEmail());
                        auth1.setRoles(roles);
                        iAuthDAO.update(auth1);
                        rpcMsg.setCode(ReturnCode.Success);
                        rpcMsg.setMsg("用户修改成功");
                    } else {
                        rpcMsg.setCode(ReturnCode.Error);
                        rpcMsg.setMsg("角色不存在");
                    }
                } else {
                    auth1.setState(rpcAuth.getState() == 1);
                    auth1.setUsername(rpcAuth.getUsername());
                    auth1.setEmail(rpcAuth.getEmail());
                    auth1.setRoles(new ArrayList<>());
                    iAuthDAO.update(auth1);
                    rpcMsg.setCode(ReturnCode.Success);
                    rpcMsg.setMsg("用户修改成功");
                }
            } else {
                rpcMsg.setCode(ReturnCode.Error);
                rpcMsg.setMsg("认证记录不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            rpcMsg.setCode(ReturnCode.Error);
            rpcMsg.setMsg(e.getMessage());
        }
        return rpcMsg;
    }
}
