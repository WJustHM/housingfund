package com.handge.housingfund.account.service.policy;

import com.handge.housingfund.common.service.account.IPolicyTaskService;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.IStCommonPolicyDAO;
import com.handge.housingfund.database.entities.StCommonPolicy;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 向超 on 2017/9/21.
 */
@Component
public class PolicyTask implements IPolicyTaskService {
    @Autowired
    private IStCommonPolicyDAO stCommonPolicyDAO;

    @Override
    public void updateDQZ() {
        System.out.println("定时更新政策信息");
        List<StCommonPolicy> stCommonPolicies = stCommonPolicyDAO.list(null, null, null, null, null, null, null);
        ArrayList<StCommonPolicy> stCommonPolicies1 = new ArrayList<StCommonPolicy>();
        for (StCommonPolicy stCommonPolicy : stCommonPolicies) {
            if (stCommonPolicy.getcCommonPolicyExtension().getSxrq() != null && stCommonPolicy.getcCommonPolicyExtension().getSxrq().compareTo(new Date()) <= 0) {
                if (stCommonPolicy.getId().equals("GRZFGJJCKLL")) {// 个人住房公积金存款利率
                    stCommonPolicy.getcCommonPolicyExtension().setSngrzfgjjckll(stCommonPolicy.getcCommonPolicyExtension().getDngrzfgjjckll());
                    stCommonPolicy.getcCommonPolicyExtension().setDngrzfgjjckll(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("HKNLXS")) {//还款能力系数
                    stCommonPolicy.getcCommonPolicyExtension().setHknlxs(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("DWJCBLSX")) {//单位缴存比例上限
                    stCommonPolicy.setDwjcblsx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("DWJCBLXX")) {//单位缴存比例下限
                    stCommonPolicy.setDwjcblxx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("GRJCBLSX")) {//个人缴存比例上限
                    stCommonPolicy.setGrjcblsx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("GRJCBLXX")) {//个人缴存比例下限
                    stCommonPolicy.setGrjcblxx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("GRZFDKZCNX")) {//个人住房贷款最长年限
                    stCommonPolicy.setGrzfdkzcnx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("GRZFDKZGDKBL")) {//个人住房贷款最高贷款比例
                    stCommonPolicy.setGrzfdkzgdkbl(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("GRZFDKZGED")) {//个人住房贷款最高额度
                    stCommonPolicy.setGrzfdkzged(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("XMDKZCNX")) {//项目贷款最长年限
                    stCommonPolicy.setXmdkzcnx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("XMDKZGDKBL")) {//项目贷款最高贷款比例
                    stCommonPolicy.setXmdkzgdkbl(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("YJCESX")) {//月缴存额上限
                    stCommonPolicy.setYjcesx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
                if (stCommonPolicy.getId().equals("YJCEXX")) {//月缴存额下限
                    stCommonPolicy.setYjcexx(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }

                if (stCommonPolicy.getId().substring(0, 2).equals("LL")) {//存款利率
                    stCommonPolicy.setZxll(stCommonPolicy.getcCommonPolicyExtension().getXgz());
                    stCommonPolicy.getcCommonPolicyExtension().setXgz(null);
                    stCommonPolicy.getcCommonPolicyExtension().setSxrq(null);
                    stCommonPolicies1.add(stCommonPolicy);
                }
            }
        }
        DAOBuilder.instance(stCommonPolicyDAO).entities(stCommonPolicies1).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }
}
