package com.handge.housingfund.account.service.policy;

import com.handge.housingfund.common.service.account.IPolicyService;
import com.handge.housingfund.common.service.account.model.PolicyCommonRes;
import com.handge.housingfund.common.service.loan.model.CommonResponses;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IStCommonPolicyDAO;
import com.handge.housingfund.database.entities.StCommonPolicy;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.handge.housingfund.common.service.util.DateUtil.date2Str1;

/**
 * Created by 向超 on 2017/9/19.
 */
@Component
public class PolicyServiceImpl implements IPolicyService {
    @Autowired
    private IStCommonPolicyDAO stCommonPolicyDAO;
    private static final String format = "yyyy-MM-dd";

    @Override
    public CommonResponses updatePolicyInfo(String id, String xgz, String sxrq) {
        BigDecimal xgzBD = new BigDecimal(xgz);
        StCommonPolicy stCommonPolicy = stCommonPolicyDAO.get(id);
        if (Arrays.asList(
                "DWJCBLSX",
                "DWJCBLXX",
                "GRJCBLSX",
                "GRJCBLXX",
                "GRZFDKZGDKBL",
                "XMDKZGDKBL").contains(id)) {
            xgzBD = xgzBD.divide(new BigDecimal(100));
            if (xgzBD.compareTo(new BigDecimal("0")) < 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保比例为正数");
            }
        }
        if (Arrays.asList("YJCEXX", "YJCESX").contains(id)) {
            if (xgzBD.compareTo(new BigDecimal("0")) < 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保缴存额为正数");
            }
        }
        if (id.substring(0, 2).contains("LL") || id.equals("GRZFGJJCKLL")) {
            if (xgzBD.compareTo(new BigDecimal("0")) < 0 || xgzBD.compareTo(new BigDecimal("100")) > 0) {
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保利率在0%-100%之间!");
            }
        }
        if (id.equals("GRZFDKZGED") && xgzBD.compareTo(new BigDecimal("0")) <= 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保个人住房贷款最高额度大于零");
        }
        if (Arrays.asList("GRZFDKZCNX", "XMDKZCNX").equals(id) && xgzBD.compareTo(new BigDecimal("0")) <= 0) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保贷款最长年限大于零");
        }
        if ("HKNLXS".equals(id)) {
            if (xgzBD.compareTo(new BigDecimal("0")) < 0 || xgzBD.compareTo(new BigDecimal("100")) > 0)
                throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "请确保还款能力系数在0%-100%之间");
        }
        stCommonPolicy.getcCommonPolicyExtension().setXgz(xgzBD);
        Date sxrq1 = null;
        try {
            sxrq1 = new SimpleDateFormat(format).parse(sxrq);
            stCommonPolicy.getcCommonPolicyExtension().setSxrq(sxrq1);
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "生效日期格式不正确,请确保正确格式为yyyy-MM-dd");
        }
        if (sxrq1.compareTo(new Date()) <= 0)
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "生效日期必须大于当前时间");
        stCommonPolicyDAO.update(stCommonPolicy);
        CommonResponses commonResponses = new CommonResponses();
        commonResponses.setId(id);
        commonResponses.setState("success");
        return commonResponses;
    }


    //初始化数据
    /*
    private ArrayList<StCommonPolicy> initPolicyInfo(){
        System.out.println("初始化政策信息");

        ArrayList<StCommonPolicy> stCommonPolicies =new ArrayList<StCommonPolicy>();
        //非利率政策信息设置
        StCommonPolicy stCommonPolicy1 = new StCommonPolicy();
        stCommonPolicy1.setDwjcblsx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension1 =new CCommonPolicyExtension();
        cCommonPolicyExtension1.setMingcheng("dwjcblsx");//单位缴存比例上限
        cCommonPolicyExtension1.setType(PolicyEnum.缴存比例设置.getCode());//缴存比例设置
        stCommonPolicy1.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy1);


        StCommonPolicy stCommonPolicy2 = new StCommonPolicy();
        stCommonPolicy2.setDwjcblxx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension2 =new CCommonPolicyExtension();
        cCommonPolicyExtension2.setMingcheng("dwjcblxx");//单位缴存比例下限
        cCommonPolicyExtension2.setType(PolicyEnum.缴存比例设置.getCode());
        stCommonPolicy2.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy2);

        StCommonPolicy stCommonPolicy3 = new StCommonPolicy();
        stCommonPolicy3.setGrjcblsx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension3 =new CCommonPolicyExtension();
        cCommonPolicyExtension3.setMingcheng("grjcblsx");//个人缴存比例上限
        cCommonPolicyExtension3.setType(PolicyEnum.缴存比例设置.getCode());
        stCommonPolicy3.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy3);

        StCommonPolicy stCommonPolicy4 = new StCommonPolicy();
        stCommonPolicy4.setDwjcblxx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension4 =new CCommonPolicyExtension();
        cCommonPolicyExtension4.setMingcheng("grjcblsxx");//个人缴存比例下限
        cCommonPolicyExtension4.setType(PolicyEnum.缴存比例设置.getCode());
        stCommonPolicy4.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy4);

        StCommonPolicy stCommonPolicy5 = new StCommonPolicy();
        stCommonPolicy5.setYjcesx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension5 =new CCommonPolicyExtension();
        cCommonPolicyExtension5.setMingcheng("yjcesx");//月缴存额上限
        cCommonPolicyExtension5.setType(PolicyEnum.缴存额度设置.getCode());
        stCommonPolicy5.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy5);

        StCommonPolicy stCommonPolicy6 = new StCommonPolicy();
        stCommonPolicy6.setYjcexx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension6 =new CCommonPolicyExtension();
        cCommonPolicyExtension6.setMingcheng("yjcexx");//月缴存额下限
        cCommonPolicyExtension6.setType(PolicyEnum.缴存额度设置.getCode());
        stCommonPolicy6.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy6);

        StCommonPolicy stCommonPolicy7 = new StCommonPolicy();
        stCommonPolicy7.setGrzfdkzcnx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension7 =new CCommonPolicyExtension();
        cCommonPolicyExtension7.setMingcheng("grzfdkzcnx");//个人住房贷款最长年限
        cCommonPolicyExtension7.setType(PolicyEnum.贷款年限设置.getCode());
        stCommonPolicy7.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy7);

        StCommonPolicy stCommonPolicy8 = new StCommonPolicy();
        stCommonPolicy8.setXmdkzcnx(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension8 =new CCommonPolicyExtension();
        cCommonPolicyExtension8.setMingcheng("xmdkzcnx");//项目贷款最长年限
        cCommonPolicyExtension8.setType(PolicyEnum.贷款年限设置.getCode());
        stCommonPolicy8.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy8);

        StCommonPolicy stCommonPolicy9 = new StCommonPolicy();
        stCommonPolicy9.setGrzfdkzgdkbl(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension9 =new CCommonPolicyExtension();
        cCommonPolicyExtension9.setMingcheng("grzfdkzgdkbl");//个人住房贷款最高贷款比例
        cCommonPolicyExtension9.setType(PolicyEnum.贷款比例设置.getCode());
        stCommonPolicy9.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy9);

        StCommonPolicy stCommonPolicy10 = new StCommonPolicy();
        stCommonPolicy10.setXmdkzgdkbl(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension10 =new CCommonPolicyExtension();
        cCommonPolicyExtension10.setMingcheng("xmdkzgdkbl");//项目贷款最高贷款比例
        cCommonPolicyExtension10.setType(PolicyEnum.贷款比例设置.getCode());
        stCommonPolicy10.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy10);

        StCommonPolicy stCommonPolicy11 = new StCommonPolicy();
        stCommonPolicy11.setGrzfdkzged(new BigDecimal(12));
        CCommonPolicyExtension cCommonPolicyExtension11 =new CCommonPolicyExtension();
        cCommonPolicyExtension11.setMingcheng("grzfdkzged");//个人住房贷款最高额度
        cCommonPolicyExtension11.setType(PolicyEnum.贷款额度设置.getCode());
        stCommonPolicy11.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy11);

        //利率设置

        StCommonPolicy stCommonPolicy12 = new StCommonPolicy();
        stCommonPolicy12.setLllx(RateEnum.五年以下公积金贷款利率.getCode());
        stCommonPolicy12.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension12 =new CCommonPolicyExtension();
        cCommonPolicyExtension12.setMingcheng(RateEnum.五年以下公积金贷款利率.getCode());//五年以下公积金贷款利率
        cCommonPolicyExtension12.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy12.setcCommonPolicyExtension(cCommonPolicyExtension12);
        stCommonPolicy12.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy12);

        StCommonPolicy stCommonPolicy13 = new StCommonPolicy();
        stCommonPolicy13.setLllx(RateEnum.五年以上公积金贷款利率.getCode());
        stCommonPolicy13.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension13 =new CCommonPolicyExtension();
        cCommonPolicyExtension13.setMingcheng(RateEnum.五年以上公积金贷款利率.getCode());//五年以上公积金贷款利率
        cCommonPolicyExtension13.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy13.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy13);

        StCommonPolicy stCommonPolicy14 = new StCommonPolicy();
        stCommonPolicy14.setLllx(RateEnum.公积金贷款利率.getCode());
        stCommonPolicy14.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension14 =new CCommonPolicyExtension();
        cCommonPolicyExtension14.setMingcheng(RateEnum.公积金贷款利率.getCode());//公积金贷款利率
        cCommonPolicyExtension14.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy14.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy14);

        StCommonPolicy stCommonPolicy15 = new StCommonPolicy();
        stCommonPolicy15.setLllx(RateEnum.活期存款利率.getCode());
        stCommonPolicy15.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension15 =new CCommonPolicyExtension();
        cCommonPolicyExtension15.setMingcheng(RateEnum.活期存款利率.getCode());//活期存款利率
        cCommonPolicyExtension15.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy15.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy15);

        StCommonPolicy stCommonPolicy16 = new StCommonPolicy();
        stCommonPolicy16.setLllx(RateEnum.三个月定期存款利率.getCode());
        stCommonPolicy16.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension16 =new CCommonPolicyExtension();
        cCommonPolicyExtension16.setMingcheng(RateEnum.三个月定期存款利率.getCode());//三个月定期存款利率
        cCommonPolicyExtension16.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy16.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy16);

        StCommonPolicy stCommonPolicy17 = new StCommonPolicy();
        stCommonPolicy17.setLllx(RateEnum.六个月定期存款利率.getCode());
        stCommonPolicy17.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension17 =new CCommonPolicyExtension();
        cCommonPolicyExtension17.setMingcheng(RateEnum.六个月定期存款利率.getCode());//六个月定期存款利率
        cCommonPolicyExtension17.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy17.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy17);

        StCommonPolicy stCommonPolicy18 = new StCommonPolicy();
        stCommonPolicy18.setLllx(RateEnum.一年定期存款利率.getCode());
        stCommonPolicy18.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension18 =new CCommonPolicyExtension();
        cCommonPolicyExtension18.setMingcheng(RateEnum.一年定期存款利率.getCode());//一年定期存款利率
        cCommonPolicyExtension18.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy18.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy18);

        StCommonPolicy stCommonPolicy19 = new StCommonPolicy();
        stCommonPolicy19.setLllx(RateEnum.两年定期存款利率.getCode());
        stCommonPolicy19.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension19 =new CCommonPolicyExtension();
        cCommonPolicyExtension19.setMingcheng(RateEnum.两年定期存款利率.getCode());//两年定期存款利率
        cCommonPolicyExtension19.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy19.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy19);

        StCommonPolicy stCommonPolicy20 = new StCommonPolicy();
        stCommonPolicy20.setLllx(RateEnum.三年定期存款利率.getCode());
        stCommonPolicy20.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension20 =new CCommonPolicyExtension();
        cCommonPolicyExtension20.setMingcheng(RateEnum.三年定期存款利率.getCode());//三年定期存款利率
        cCommonPolicyExtension20.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy20.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy20);

        StCommonPolicy stCommonPolicy21 = new StCommonPolicy();
        stCommonPolicy21.setLllx(RateEnum.五年定期存款利率.getCode());
        stCommonPolicy21.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension21 =new CCommonPolicyExtension();
        cCommonPolicyExtension21.setMingcheng(RateEnum.五年定期存款利率.getCode());//五年定期存款利率
        cCommonPolicyExtension21.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy21.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy21);

        StCommonPolicy stCommonPolicy22 = new StCommonPolicy();
        stCommonPolicy22.setLllx(RateEnum.一天通知存款利率.getCode());
        stCommonPolicy22.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension22 =new CCommonPolicyExtension();
        cCommonPolicyExtension22.setMingcheng(RateEnum.一天通知存款利率.getCode());//一天通知存款利率
        cCommonPolicyExtension22.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy22.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy22);

        StCommonPolicy stCommonPolicy23 = new StCommonPolicy();
        stCommonPolicy23.setLllx(RateEnum.七天通知存款利率.getCode());
        stCommonPolicy23.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension23 =new CCommonPolicyExtension();
        cCommonPolicyExtension23.setMingcheng(RateEnum.七天通知存款利率.getCode());//七天通知存款利率
        cCommonPolicyExtension23.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy23.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy23);

        StCommonPolicy stCommonPolicy24 = new StCommonPolicy();
        stCommonPolicy24.setLllx(RateEnum.协定存款利率.getCode());
        stCommonPolicy24.setZxll(new BigDecimal(10));
        CCommonPolicyExtension cCommonPolicyExtension24 =new CCommonPolicyExtension();
        cCommonPolicyExtension24.setMingcheng(RateEnum.协定存款利率.getCode());//协定存款利率
        cCommonPolicyExtension24.setType(PolicyEnum.利率设置.getCode());
        stCommonPolicy24.setcCommonPolicyExtension(cCommonPolicyExtension1);
        stCommonPolicies.add(stCommonPolicy24);

       ArrayList<StCommonPolicy> stCommonPolicies1 = DAOBuilder.instance(stCommonPolicyDAO).entities(stCommonPolicies).saveThenFetchObjects(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        return stCommonPolicies1;
    }
    */

    @Override
    public ArrayList<PolicyCommonRes> getPolicy() {
        List<StCommonPolicy> stCommonPolicies = DAOBuilder.instance(stCommonPolicyDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCommonPolicies.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "请确保数据库有数据");
        }
        ArrayList<PolicyCommonRes> policyCommonRess = new ArrayList<PolicyCommonRes>();
        for (StCommonPolicy stCommonPolicy : stCommonPolicies) {
            PolicyCommonRes policyCommonRes = new PolicyCommonRes();
            if (stCommonPolicy.getId().equals("GRZFGJJCKLL")) {    //个人住房公积金存款利率
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getcCommonPolicyExtension().getDngrzfgjjckll().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("HKNLXS")) {    //还款能力系数
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getcCommonPolicyExtension().getHknlxs().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("DWJCBLSX")) {//单位缴存比例上限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getDwjcblsx() == null ? null : stCommonPolicy.getDwjcblsx().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("DWJCBLXX")) {//单位缴存比例下限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getDwjcblxx() == null ? null : stCommonPolicy.getDwjcblxx().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("GRJCBLSX")) {//个人缴存比例上限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getGrjcblsx() == null ? null : stCommonPolicy.getGrjcblsx().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("GRJCBLXX")) {//个人缴存比例下限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getGrjcblxx() == null ? null : stCommonPolicy.getGrjcblxx().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("GRZFDKZCNX")) {//个人住房贷款最长年限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getGrzfdkzcnx().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("GRZFDKZGDKBL")) {//个人住房贷款最高贷款比例
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getGrzfdkzgdkbl() == null ? null : stCommonPolicy.getGrzfdkzgdkbl().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("GRZFDKZGED")) {//个人住房贷款最高额度
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getGrzfdkzged().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("XMDKZCNX")) {//项目贷款最长年限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getXmdkzcnx().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("XMDKZGDKBL")) {//项目贷款最高贷款比例
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getXmdkzgdkbl() == null ? null : stCommonPolicy.getXmdkzgdkbl().multiply(new BigDecimal(100)) + "");//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().multiply(new BigDecimal(100)) + "");//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("YJCESX")) {//月缴存额上限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getYjcesx().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }
            if (stCommonPolicy.getId().equals("YJCEXX")) {//月缴存额下限
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getYjcexx().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }

            if (stCommonPolicy.getId().substring(0, 2).equals("LL")) {//存款利率
                policyCommonRes.setSjxmc(stCommonPolicy.getId());
                policyCommonRes.setSJXMC(stCommonPolicy.getcCommonPolicyExtension().getMingcheng());
                policyCommonRes.setDqz(stCommonPolicy.getZxll().toPlainString());//当前值
                if (stCommonPolicy.getcCommonPolicyExtension().getXgz() != null)
                    policyCommonRes.setXgz(stCommonPolicy.getcCommonPolicyExtension().getXgz().toPlainString());//修改值
                policyCommonRes.setSxrq(date2Str1(stCommonPolicy.getcCommonPolicyExtension().getSxrq(), format));//生效日期
                policyCommonRess.add(policyCommonRes);
            }

        }
        return policyCommonRess;
    }

    @Override
    public BigDecimal getPolicyRate(String LLLX) {
        StCommonPolicy stCommonPolicies = DAOBuilder.instance(stCommonPolicyDAO).searchFilter(new HashMap<String, Object>() {{
            if (StringUtil.notEmpty(LLLX)) {
                this.put("lllx", LLLX);
            }
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCommonPolicies == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应类型的利率");
        }
        return stCommonPolicies.getZxll();
    }

    @Override
    public HashMap<String, BigDecimal> getPolicyRateById(String id) {
        StCommonPolicy stCommonPolicies = DAOBuilder.instance(stCommonPolicyDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (stCommonPolicies == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有对应类型的利率");
        }

        HashMap<String, BigDecimal> rateMap = new HashMap<>();
        rateMap.put("dngj", stCommonPolicies.getcCommonPolicyExtension().getDngrzfgjjckll());
        rateMap.put("snjz", stCommonPolicies.getcCommonPolicyExtension().getSngrzfgjjckll());

        return rateMap;
    }
}
