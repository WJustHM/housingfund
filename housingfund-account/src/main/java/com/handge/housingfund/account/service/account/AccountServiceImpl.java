package com.handge.housingfund.account.service.account;

import com.handge.housingfund.account.util.EmailUtil;
import com.handge.housingfund.account.util.PasswordUtil;
import com.handge.housingfund.common.service.account.IAccountService;
import com.handge.housingfund.common.service.account.model.LoginCA;
import com.handge.housingfund.common.service.account.model.Msg;
import com.handge.housingfund.common.service.account.model.ResetPwd;
import com.handge.housingfund.common.service.account.model.RpcAuth;
import com.handge.housingfund.common.service.ca.CaService;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.JWTTokenCentral;
import com.handge.housingfund.common.service.util.ReturnCode;
import com.handge.housingfund.database.dao.ICAuthDAO;
import com.handge.housingfund.database.entities.CAuth;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.SearchOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by tanyi on 2017/7/11.
 */
@Component
public class AccountServiceImpl implements IAccountService {

    @Autowired
    CaService ca;

    @Autowired
    ICAuthDAO iAuthDAO;


    //changed by wangxuefei

//    static CompositeConfiguration config = PropertiesUtil.getConfiguration("token.properties");
//    static int expires = config.getInt("expirestime", 15);

    /**
     * @param userid 用户ID
     * @param type
     * @return
     * @throws Exception
     * @implNote String token = JWTTokenCentral.generateToken(userid,type); by xuefei_wang
     */
    @Override
    public String getToken(String userid, int type) {
//        String token = JWTUtil.getToken(userid, type);
//        RedisUtil.setex("Valid_" + token, expires * 60, "Valid");

        String token = null;
        try {
            token = JWTTokenCentral.generateToken(userid, type);
        } catch (UnsupportedEncodingException e) {
            throw new ErrorException(e);
        }
        return token;
    }

    @Override
    public RpcAuth verifyCa(LoginCA loginCA) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("username", loginCA.getUsername());
        List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        if (auth == null) {
            return null;
        }
        //ca加密数据有效期验证
        if (loginCA.getInfo().split(",").length < 2 || Long.valueOf(loginCA.getInfo().split(",")[1]) + 60000 < new Date().getTime()) {
            return null;
        }
        if (!ca.VerifySignP1(loginCA.getCert(), loginCA.getInfo(), loginCA.getSigninfo())) {
            return null;
        } else {
            RpcAuth rpcAuth = new RpcAuth();
            rpcAuth.setEmail(auth.getEmail());
            rpcAuth.setPassword(auth.getPassword());
            rpcAuth.setUsername(auth.getUsername());
            rpcAuth.setUser_id(auth.getUser_id());
            rpcAuth.setType(auth.getType());
            return rpcAuth;
        }
    }

    /**
     * 验证用户名密码
     *
     * @param username 用户名
     * @param pwd      密码
     * @return
     */
    @Override
    public RpcAuth verifPwd(String username, String pwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("username", username);
        List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        RpcAuth rpcAuth = new RpcAuth();
        if (auth != null) {
            rpcAuth.setEmail(auth.getEmail());
            rpcAuth.setPassword(auth.getPassword());
            rpcAuth.setUsername(auth.getUsername());
            rpcAuth.setUser_id(auth.getUser_id());
            rpcAuth.setType(auth.getType());
            //增强密码强度，相同密码，生成密文不同
//            if (!PasswordUtil.validatePwd(pwd + auth.getUser_id(), auth.getPassword())) {
//                rpcAuth = null;
//            }
            if (!PasswordUtil.validatePwd(pwd, auth.getPassword())) {
                rpcAuth = null;
            }
        } else {
            rpcAuth = null;
        }
        return rpcAuth;
    }

    /**
     * @param email 邮件地址
     * @return
     * @implNote String token = JWTTokenCentral.generateToken(auth_id,101);  by xuefei_wang
     * 发送重置邮件
     * 查询用户id
     */
    @Override
    public Msg resetPwdSendEmail(String email) throws UnsupportedEncodingException {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("email", email);
        filter.put("type", 1);
        List<CAuth> auths = iAuthDAO.list(filter, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        CAuth auth = auths.size() > 0 ? auths.get(0) : null;
        Msg res = new Msg();
        if (auth != null) {
            if (auth.getType() == 1) {
                String auth_id = auth.getId();
                String username = auth.getUsername();

                String token = JWTTokenCentral.generateToken(auth_id, 101);
                String url = "https://www.baidu.com" + "?token=" + token;
                String content = EmailUtil.createEmailContent(username, url, DateUtil.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
                try {
                    EmailUtil.sendEmail(email, username, "密码重置邮件", content);
                    res.setCode(ReturnCode.Success);
                    res.setMsg("重置密码邮件发送成功");
                } catch (UnsupportedEncodingException | MessagingException e) {
                    res.setCode(ReturnCode.Error);
                    res.setMsg(e.getMessage());
                }
            } else {
                res.setCode(ReturnCode.Error);
                res.setMsg("仅个人账户支持重置密码");
            }

        } else {
            res.setCode(ReturnCode.Error);
            res.setMsg("仅个人账户支持重置密码");
        }
        return res;
    }

    /**
     * 重置用户密码
     * <p>
     * 根据auth_id查询用户认证表记录，重置密码
     *
     * @param auth_id  认证ID
     * @param resetPwd 新密码信息
     * @return
     */
    @Override
    public Msg resetPwd(String auth_id, ResetPwd resetPwd) {
        Msg msg = new Msg();
        try {
            System.out.println(auth_id);
            CAuth auth = iAuthDAO.get(auth_id);
//            String hash = PasswordUtil.encryptPwd(resetPwd.getPwd() + auth.getUser_id());
            String hash = PasswordUtil.encryptPwd(resetPwd.getPwd());
            if (auth != null) {
                System.out.println(auth);
                auth.setPassword(hash);
                iAuthDAO.update(auth);
                try {

                    // changed by wangxuefei
//                    RedisUtil.setex("Invalid_" + resetPwd.getToken(), 11 * 60, "Invalid");
                    msg.setCode(ReturnCode.Success);
                    msg.setMsg("密码重置成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setMsg(e.getMessage());
                    msg.setCode(ReturnCode.Error);
                }
            } else {
                msg.setMsg("账户不存在");
                msg.setCode(ReturnCode.Error);
            }

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            msg.setMsg(e.getMessage());
            msg.setCode(ReturnCode.Error);
        }
        return msg;
    }

}
