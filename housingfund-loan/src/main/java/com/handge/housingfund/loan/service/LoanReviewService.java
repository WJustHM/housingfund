package com.handge.housingfund.loan.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.loan.ILoanReviewService;
import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanGuaranteeType;
import com.handge.housingfund.common.service.loan.enums.LoanRiskStatus;
import com.handge.housingfund.common.service.loan.model.*;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
@SuppressWarnings({"Duplicates", "SpringAutowiredFieldsWarningInspection", "SpringJavaAutowiredMembersInspection", "Convert2Lambda", "serial", "Convert2Diamond", "SpringJavaAutowiringInspection"})
public class LoanReviewService implements ILoanReviewService {

    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;

    @Autowired
    private ICLoanHousingPersonInformationBasicDAO loanHousingPersonInformationBasicDAO;

    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    private IStHousingBusinessDetailsDAO housingBusinessDetailsDAO;

    @Autowired
    private ICAccountNetworkDAO cAccountNetworkDAO;

    private static String format = "yyyy-MM-dd";

    private static String formatNY = "yyyy-MM";

    /*
      *  completed
      *
      * !逻辑未完成
      *
      * !存在问题
      *
      * */
    @Override
    public void postLoanReviewReason(TokenContext tokenContext, String YWLSH) {

//        body.getAction(/*审核结果(0通过1不通过)*/);
//        body.getReason(/*审核不通过（原因）*/);
//        body.getSHYJ(/* 审核通过（审核意见）*/);

        //region //必要字段申明

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (loanHousingBusinessProcess == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //附表
        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null ? new CLoanHousingPersonInformationVice() : loanHousingBusinessProcess.getLoanHousingPersonInformationVice();

        CLoanHousePurchasingVice loanHousePurchasingVice = loanHousingBusinessProcess.getLoanHousePurchasingVice() == null ? new CLoanHousePurchasingVice() : loanHousingBusinessProcess.getLoanHousePurchasingVice();

        CLoanHouseBuildVice loanHouseBuildVice = loanHousingBusinessProcess.getLoanHouseBuildVice() == null ? new CLoanHouseBuildVice() : loanHousingBusinessProcess.getLoanHouseBuildVice();

        CLoanFundsVice loanFundsVice = loanHousingBusinessProcess.getLoanFundsVice() == null ? new CLoanFundsVice() : loanHousingBusinessProcess.getLoanFundsVice();

        CLoanHouseOverhaulVice loanHouseOverhaulVice = loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null ? new CLoanHouseOverhaulVice() : loanHousingBusinessProcess.getLoanHouseOverhaulVice();

        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice() == null ? new CLoanHousingCoborrowerVice() : loanHousingBusinessProcess.getLoanHousingCoborrowerVice();

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null ? new CLoanHousingGuaranteeContractVice() : loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice();

        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = loanHousingGuaranteeContractVice.getGuaranteeMortgageVices();

        List<CLoanGuaranteeVice> list_loanGuaranteeVice = loanHousingGuaranteeContractVice.getGuaranteeVices();

        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = loanHousingGuaranteeContractVice.getGuaranteePledgeVices();


        //主表
        CLoanHousingPersonInformationBasic loanHousingPersonInformationBasic = new CLoanHousingPersonInformationBasic();

        CLoanHousePurchasingBasic loanHousePurchasingBasic = new CLoanHousePurchasingBasic();
        loanHousePurchasingBasic.setcLoanHousingPersonInformationBasic(loanHousingPersonInformationBasic);
        loanHousingPersonInformationBasic.setPurchasing(loanHousePurchasingBasic);

        CLoanHouseBuildBasic loanHouseBuildBasic = new CLoanHouseBuildBasic();
        loanHouseBuildBasic.setcLoanHousingPersonInformationBasic(loanHousingPersonInformationBasic);
        loanHousingPersonInformationBasic.setBuild(loanHouseBuildBasic);

        CLoanFundsInformationBasic loanFundsInformationBasic = new CLoanFundsInformationBasic();
        loanFundsInformationBasic.setcLoanHousingPersonInformationBasic(loanHousingPersonInformationBasic);
        loanHousingPersonInformationBasic.setFunds(loanFundsInformationBasic);

        CLoanHouseOverhaulBasic loanHouseOverhaulBasic = new CLoanHouseOverhaulBasic();
        loanHouseOverhaulBasic.setcLoanHousingPersonInformationBasic(loanHousingPersonInformationBasic);
        loanHousingPersonInformationBasic.setOverhaul(loanHouseOverhaulBasic);

        StHousingCoborrower housingCoborrower = new StHousingCoborrower();
        loanHousingPersonInformationBasic.setCoborrower(housingCoborrower);

        CLoanHousingCoborrowerExtension loanHousingCoborrowerExtension = new CLoanHousingCoborrowerExtension();
        housingCoborrower.setcExtension(loanHousingCoborrowerExtension);

        StHousingPersonalAccount personalAccount = new StHousingPersonalAccount();
        loanHousingPersonInformationBasic.setPersonalAccount(personalAccount);


        StHousingGuaranteeContract housingGuaranteeContract = new StHousingGuaranteeContract();
        loanHousingPersonInformationBasic.setGuaranteeContract(housingGuaranteeContract);

        CHousingGuaranteeContractExtension housingGuaranteeContractExtension = new  CHousingGuaranteeContractExtension();
        housingGuaranteeContract.setExtension(housingGuaranteeContractExtension);

        List<CLoanGuaranteeMortgageExtension> list_loanGuaranteeMortgageExtension = new ArrayList<CLoanGuaranteeMortgageExtension>();
        housingGuaranteeContract.setcLoanGuaranteeMortgageExtensions(list_loanGuaranteeMortgageExtension);

        List<CLoanGuaranteeExtension> list_loanGuaranteeExtension = new ArrayList<CLoanGuaranteeExtension>();
        housingGuaranteeContract.setcLoanGuaranteeExtensions(list_loanGuaranteeExtension);

        List<CLoanGuaranteePledgeExtension> list_loanGuaranteePledgeExtension = new ArrayList<CLoanGuaranteePledgeExtension>();
        housingGuaranteeContract.setcLoanGuaranteePledgeExtensions(list_loanGuaranteePledgeExtension);

        loanHousingPersonInformationBasic.setDkzhzt(LoanAccountType.待签合同.getCode());


        //endregion

        //region //数据填充
        //common
        loanHousingBusinessProcess.setCznr(LoanBusinessType.贷款发放.getCode());
        loanHousingBusinessProcess.setBlsj(new Date());

        loanHousingPersonInformationBasic.setYwlsh(loanHousingBusinessProcess.getYwlsh());
        loanHousingPersonInformationBasic.setDkzhzt(LoanAccountType.待签合同.getCode());
        loanHousingPersonInformationBasic.setYhqs(BigDecimal.ZERO);
        loanHousingPersonInformationBasic.setYwwd(loanHousingBusinessProcess.getYwwd().getId());
        loanHousingPersonInformationBasic.setCzy(loanHousingBusinessProcess.getCzy());
        loanHousingPersonInformationBasic.setSpbwj(loanHousingPersonInformationVice.getSpbwj());


        //loanHousingPersonInformationBasic.setDkzhzt();
        //loanHousingBusinessProcess.setStep("3");


        //region//申请人信息
        loanHousingPersonInformationBasic.setHyzk(loanHousingPersonInformationVice.getHyzk(/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/));
        loanHousingPersonInformationBasic.setNianLing(loanHousingPersonInformationVice.getNianLing(/*年龄*/));
        loanHousingPersonInformationBasic.setJtzz(loanHousingPersonInformationVice.getJtzz(/*家庭住址*/));
        loanHousingPersonInformationBasic.setCsny(loanHousingPersonInformationVice.getCsny(/*出生年月*/));
        loanHousingPersonInformationBasic.setJkrzjhm(loanHousingPersonInformationVice.getJkrzjhm(/*借款人证件号码*/));
        loanHousingPersonInformationBasic.setYgxz(loanHousingPersonInformationVice.getYgxz(/*用工性质（0：正式职工 1：合同制 2：聘用制）*/));
        loanHousingPersonInformationBasic.setYsr(loanHousingPersonInformationVice.getYsr(/*月收入*/));
        loanHousingPersonInformationBasic.setJkrxm(loanHousingPersonInformationVice.getJkrxm(/*借款人姓名*/));
        loanHousingPersonInformationBasic.setHkszd(loanHousingPersonInformationVice.getHkszd(/*户口所在地*/));
        loanHousingPersonInformationBasic.setZhiCheng(loanHousingPersonInformationVice.getZhiCheng(/*职称*/));
        loanHousingPersonInformationBasic.setJkrzjlx(loanHousingPersonInformationVice.getJkrzjlx(/*借款人证件类型*/));
        loanHousingPersonInformationBasic.setSjhm(loanHousingPersonInformationVice.getSjhm(/*手机号码*/));
        loanHousingPersonInformationBasic.setJkzk(loanHousingPersonInformationVice.getJkzk(/*健康状态（0：良好 1：一般 2：差）*/));
        loanHousingPersonInformationBasic.setGddhhm(loanHousingPersonInformationVice.getGddhhm(/*固定电话号码*/));
        loanHousingPersonInformationBasic.setZyjjly(loanHousingPersonInformationVice.getZyjjly(/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/));
        loanHousingPersonInformationBasic.setXingBie(loanHousingPersonInformationVice.getXingBie(/*性别*/));
        loanHousingPersonInformationBasic.setJtysr(loanHousingPersonInformationVice.getJtysr(/*家庭月收入*/));
        loanHousingPersonInformationBasic.setXueLi(loanHousingPersonInformationVice.getXueLi(/*学历*/));
        loanHousingPersonInformationBasic.setZhiWu(loanHousingPersonInformationVice.getZhiWu(/*职务*/));


        loanHousingPersonInformationBasic.setBlzl(loanHousingPersonInformationVice.getBlzl(/* 办理资料*/));


        loanHousingPersonInformationBasic.setGrzhzt(loanHousingPersonInformationVice.getGrzhzt(/*个人账户状态*/));
        loanHousingPersonInformationBasic.setJzny(loanHousingPersonInformationVice.getJzny(/*缴至年月*/));
        loanHousingPersonInformationBasic.setYjce(loanHousingPersonInformationVice.getYjce(/*月缴存额*/));
        loanHousingPersonInformationBasic.setGrzhye(loanHousingPersonInformationVice.getGrzhye(/*个人账户余额*/));
        loanHousingPersonInformationBasic.setGrjcjs(loanHousingPersonInformationVice.getGrjcjs(/*个人缴存基数*/));
        loanHousingPersonInformationBasic.setLxzcjcys(loanHousingPersonInformationVice.getLxzcjcys(/*连续正常缴存月数*/));


        loanHousingPersonInformationBasic.setDwdh(loanHousingPersonInformationVice.getDwdh(/*单位电话*/));
        loanHousingPersonInformationBasic.setDwmc(loanHousingPersonInformationVice.getDwmc(/*单位名称*/));
        loanHousingPersonInformationBasic.setDwzh(loanHousingPersonInformationVice.getDwzh(/*单位账号*/));
        loanHousingPersonInformationBasic.setDwxz(loanHousingPersonInformationVice.getDwxz(/*单位性质*/));
        loanHousingPersonInformationBasic.setDwdz(loanHousingPersonInformationVice.getDwdz(/*单位地址*/));


        loanHousingPersonInformationBasic.setJkrgjjzh(loanHousingPersonInformationVice.getJkrgjjzh(/*借款人公积金账号*/));
        loanHousingPersonInformationBasic.setJcd(loanHousingPersonInformationVice.getJcd(/*缴存地*/));

        //endregion


        //region //共同借款人信息

        housingCoborrower.setGtjkrgjjzh(loanHousingCoborrowerVice.getGtjkrgjjzh(/*共同借款人公积金账号 */));
        housingCoborrower.setCdgx(loanHousingCoborrowerVice.getCdgx(/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他） */));
        housingCoborrower.setSjhm(loanHousingCoborrowerVice.getSjhm(/*手机号码 */));
        loanHousingCoborrowerExtension.setJcd(loanHousingCoborrowerVice.getJcd(/*缴存地 */));
        housingCoborrower.setGddhhm(loanHousingCoborrowerVice.getGddhhm(/*固定电话号码 */));
        housingCoborrower.setGtjkrxm(loanHousingCoborrowerVice.getGtjkrxm(/*共同借款人姓名 */));
        housingCoborrower.setGtjkrzjlx(loanHousingCoborrowerVice.getGtjkrzjlx(/*共同借款人证件类型 */));
        housingCoborrower.setYsr(loanHousingCoborrowerVice.getYsr(/*月收入 */));
        housingCoborrower.setGtjkrzjhm(loanHousingCoborrowerVice.getGtjkrzjhm(/*共同借款人证件号码 */));
        loanHousingCoborrowerExtension.setHkszd(loanHousingCoborrowerVice.getHkszd(/*户口所在地*/));

        loanHousingCoborrowerExtension.setBlzl(loanHousingCoborrowerVice.getBlzl(/*提交资料*/));


        loanHousingCoborrowerExtension.setGrzhzt(loanHousingCoborrowerVice.getGrzhzt(/*个人账户状态 */));
        loanHousingCoborrowerExtension.setJzny(loanHousingCoborrowerVice.getJzny(/*缴至年月 */));
        loanHousingCoborrowerExtension.setYjce(loanHousingCoborrowerVice.getYjce(/*月缴存额 */));
        loanHousingCoborrowerExtension.setGrzhye(loanHousingCoborrowerVice.getGrzhye(/*个人账户余额 */));
        loanHousingCoborrowerExtension.setGrjcjs(loanHousingCoborrowerVice.getGrjcjs(/*个人缴存基数 */));
        loanHousingCoborrowerExtension.setLxzcjcys(loanHousingCoborrowerVice.getLxzcjcys(/*连续正常缴存月数 */));


        loanHousingCoborrowerExtension.setDwdh(loanHousingCoborrowerVice.getDwdh(/*单位电话 */));
        loanHousingCoborrowerExtension.setDwmc(loanHousingCoborrowerVice.getDwmc(/*单位名称 */));
        loanHousingCoborrowerExtension.setDwzh(loanHousingCoborrowerVice.getDwzh(/*单位账号 */));
        loanHousingCoborrowerExtension.setDwxz(loanHousingCoborrowerVice.getDwxz(/*单位性质 */));
        loanHousingCoborrowerExtension.setDwdz(loanHousingCoborrowerVice.getDwdz(/*单位地址 */));

        if (!"20".equals(loanHousingPersonInformationBasic.getHyzk(/*婚姻状况 */))) {

            loanHousingBusinessProcess.setLoanHousingCoborrowerVice(null);
            loanHousingPersonInformationBasic.setCoborrower(null);

        }
        //endregion


        //region //房屋信息

        loanHousingPersonInformationBasic.setDkyt(loanHousingBusinessProcess.getDkyt());


        if (loanHousingBusinessProcess.getDkyt(/*贷款用途*/).equals("0")) {

            //region //购买

            if (loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfwesf(/*是否为二手房*/)) {

                //region //二手房

                loanHousePurchasingBasic.setFwjzmj(loanHousePurchasingVice.getFwjzmj(/*房屋建筑面积*/));
                loanHousePurchasingBasic.setGrskyhzh(loanHousePurchasingVice.getGrskyhzh(/*个人收款银行账号*/));
                loanHousePurchasingBasic.setFwtnmj(loanHousePurchasingVice.getFwtnmj(/*房屋套内面积*/));
                loanHousePurchasingBasic.setFwzj(loanHousePurchasingVice.getFwzj(/*房屋总价*/));
                loanHousePurchasingBasic.setFwxz(loanHousePurchasingVice.getFwxz(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingBasic.setKhyhmc(loanHousePurchasingVice.getKhyhmc(/*开户银行名称*/));
                loanHousePurchasingBasic.setFwjgrq(loanHousePurchasingVice.getFwjgrq(/*房屋竣工日期*/));
                loanHousePurchasingBasic.setFwjg(loanHousePurchasingVice.getFwjg(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingBasic.setHtje(loanHousePurchasingVice.getHtje(/*合同金额*/));
                loanHousePurchasingBasic.setGfhtbh(loanHousePurchasingVice.getGfhtbh(/*购房合同编号*/));
                loanHousePurchasingBasic.setBlzl(loanHousePurchasingVice.getBlzl(/*资料*/));
                loanHousePurchasingBasic.setYfk(loanHousePurchasingVice.getYfk(/*已付款（全部付完）*/));
                loanHousePurchasingBasic.setDanJia(loanHousePurchasingVice.getDanJia(/**/));
                loanHousePurchasingBasic.setLxfs(loanHousePurchasingVice.getLxfs(/*联系方式*/));
                loanHousePurchasingBasic.setSfrmc(loanHousePurchasingVice.getSfrmc(/*售房人名称*/));
                loanHousePurchasingBasic.setFwzl(loanHousePurchasingVice.getFwzl(/*房屋坐落*/));
                loanHousePurchasingBasic.setFwxs(loanHousePurchasingVice.getFwxs(/**/));
                loanHousePurchasingBasic.setSfrkhyhmc(loanHousePurchasingVice.getSfrkhyhmc()/*开户行银行名称*/);
                loanHousePurchasingBasic.setSfrzhhm(loanHousePurchasingVice.getSfrzhhm(/*售房人账户号码*/));
                loanHousePurchasingBasic.setSfryhkhm(loanHousePurchasingVice.getSfryhkhm(/*售房人银行开户名*/));
                loanHousePurchasingBasic.setYhkhm(loanHousePurchasingVice.getYhkhm(/*银行开户名*/));
                //endregion
            }


            loanHousePurchasingBasic.setSfwesf(loanHousePurchasingVice.getSfwesf(/*是否为二手房*/));

            if (!loanHousingBusinessProcess.getLoanHousePurchasingVice().getSfwesf(/*是否为二手房*/)) {

                //region //非二手房
                loanHousePurchasingBasic.setFwjzmj(loanHousePurchasingVice.getFwjzmj(/*房屋建筑面积*/));
                loanHousePurchasingBasic.setFwtnmj(loanHousePurchasingVice.getFwtnmj(/*房屋套内面积*/));
                loanHousePurchasingBasic.setFwxs(loanHousePurchasingVice.getFwxs(/*房屋形式（0：在建房  1：现房）*/));
                loanHousePurchasingBasic.setFwxz(loanHousePurchasingVice.getFwxz(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
                loanHousePurchasingBasic.setFwzj(loanHousePurchasingVice.getFwzj(/*房屋总价*/));
                loanHousePurchasingBasic.setFwjgrq(loanHousePurchasingVice.getFwjgrq(/*房屋竣工日期*/));
                loanHousePurchasingBasic.setSfrkhyhmc(loanHousePurchasingVice.getSfrkhyhmc(/*售房人开户银行名称*/));
                loanHousePurchasingBasic.setFwjg(loanHousePurchasingVice.getFwjg(/*房屋结构（0：框架 1：砖混 2：其他）*/));
                loanHousePurchasingBasic.setHtje(loanHousePurchasingVice.getHtje(/*合同金额*/));
                loanHousePurchasingBasic.setSfrzhhm(loanHousePurchasingVice.getSfrzhhm(/*售房人账户号码*/));
                loanHousePurchasingBasic.setLpmc(loanHousePurchasingVice.getLpmc(/*楼盘名称*/));
                loanHousePurchasingBasic.setGfhtbh(loanHousePurchasingVice.getGfhtbh(/*购房合同编号*/));
                loanHousePurchasingBasic.setBlzl(loanHousePurchasingVice.getBlzl(/*办理资料*/));
                loanHousePurchasingBasic.setSfk(loanHousePurchasingVice.getSfk(/*首付款*/));
                loanHousePurchasingBasic.setLxfs(loanHousePurchasingVice.getLxfs(/*联系方式*/));
                loanHousePurchasingBasic.setSfrmc(loanHousePurchasingVice.getSfrmc(/*售房人名称*/));
                loanHousePurchasingBasic.setFwzl(loanHousePurchasingVice.getFwzl(/*房屋坐落*/));
                loanHousePurchasingBasic.setDanJia(loanHousePurchasingVice.getDanJia(/*单价*/));
                loanHousePurchasingBasic.setSfryhkhm(loanHousePurchasingVice.getSfryhkhm(/*售房人银行开户名*/));
                loanHousePurchasingBasic.setGrskyhzh(loanHousePurchasingVice.getGrskyhzh()/*个人收款银行账号*/);
                loanHousePurchasingBasic.setKhyhmc(loanHousePurchasingVice.getKhyhmc(/*开户银行名称*/));
                loanHousePurchasingBasic.setYhkhm(loanHousePurchasingVice.getYhkhm(/*银行开户名*/));
                loanHousePurchasingBasic.setSpfysxkbh(loanHousePurchasingVice.getSpfysxkbh()/*商品房预售许可证编号*/);
                //endregion
            }


            //endregion

            loanHousingPersonInformationBasic.setBuild(null);
            loanHousingPersonInformationBasic.setOverhaul(null);

            loanHouseBuildBasic.setcLoanHousingPersonInformationBasic(null);
            loanHouseOverhaulBasic.setcLoanHousingPersonInformationBasic(null);


        }

        if (loanHousingBusinessProcess.getDkyt(/*贷款用途*/).equals("1")) {

            //region//自建翻修

            loanHouseBuildBasic.setGrskyhzh(loanHouseBuildVice.getGrskyhzh(/*个人收款银行账号*/));
            loanHouseBuildBasic.setJhjgrq(loanHouseBuildVice.getJhjgrq(/*计划开工日期*/));
            loanHouseBuildBasic.setFwxz(loanHouseBuildVice.getFwxz(/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/));
            loanHouseBuildBasic.setFwjg(loanHouseBuildVice.getFwjg(/*房屋结构（0：框架 1：砖混 2：其他）*/));
            loanHouseBuildBasic.setJhjgrq(loanHouseBuildVice.getJhjgrq(/*计划开工日期*/));
            loanHouseBuildBasic.setFwzl(loanHouseBuildVice.getFwzl(/*房屋坐落地址*/));
            loanHouseBuildBasic.setTdsyzh(loanHouseBuildVice.getTdsyzh(/*土地使用证号*/));
            loanHouseBuildBasic.setGrsyzj(loanHouseBuildVice.getGrsyzj(/*个人使用资金*/));
            loanHouseBuildBasic.setJzcs(StringUtil.safeBigDecimal(loanHouseBuildVice.getJzcs(/*建造层数*/)));
            loanHouseBuildBasic.setYhkhm(loanHouseBuildVice.getYhkhm(/*银行开户名*/));
            loanHouseBuildBasic.setJzydghxkzh(loanHouseBuildVice.getJzydghxkzh(/*建造用地规划许可证号*/));
            loanHouseBuildBasic.setPzjgwh(loanHouseBuildVice.getPzjgwh(/*批准机关文号*/));
            loanHouseBuildBasic.setJhjzfy(loanHouseBuildVice.getJhjzfy(/*计划建造费用*/));
            loanHouseBuildBasic.setYhkhm(loanHouseBuildVice.getYhkhm(/*开户行银行名称*/));
            loanHouseBuildBasic.setBlzl(loanHouseBuildVice.getBlzl(/* 办理资料*/));
            loanHouseBuildBasic.setJzgcghxkzh(loanHouseBuildVice.getJzgcghxkzh(/*建造工程规划许可证号*/));
            loanHouseBuildBasic.setJchjzmj(loanHouseBuildVice.getJchjzmj(/*建成后建造面积*/));
            loanHouseBuildBasic.setJhkgrq(loanHouseBuildVice.getJhkgrq(/*计划开工日期*/));
            loanHouseBuildBasic.setKhhyhmc(loanHouseBuildVice.getKhhyhmc()/*开户行银行名称*/);
            loanHouseBuildBasic.setJsgcsgxkzh(loanHouseBuildBasic.getJsgcsgxkzh()/*建设工程施工许可证号*/);
            //endregion


            loanHousingPersonInformationBasic.setOverhaul(null);
            loanHousingPersonInformationBasic.setPurchasing(null);

            loanHouseOverhaulBasic.setcLoanHousingPersonInformationBasic(null);
            loanHousePurchasingBasic.setcLoanHousingPersonInformationBasic(null);

        }

        if (loanHousingBusinessProcess.getDkyt(/*贷款用途*/).equals("2")) {

            // region//大修

            loanHouseOverhaulBasic.setGrskyhzh(loanHouseOverhaulVice.getGrskyhzh(/*个人收款银行账号*/));
            loanHouseOverhaulBasic.setBlzl(loanHouseOverhaulVice.getBlzl(/*资料*/));
            loanHouseOverhaulBasic.setFwzl(loanHouseOverhaulVice.getFwzl(/*房屋坐落地址*/));
            loanHouseOverhaulBasic.setJhkgrq(loanHouseOverhaulVice.getJhkgrq(/*计划开工日期*/));
            loanHouseOverhaulBasic.setYhkhm(loanHouseOverhaulVice.getYhkhm(/*银行开户名*/));
            loanHouseOverhaulBasic.setYbdczh(loanHouseOverhaulVice.getYbdczh(/*原不动产证号*/));
            loanHouseOverhaulBasic.setDxgcys(loanHouseOverhaulVice.getDxgcys(/**/));
            loanHouseOverhaulBasic.setYjzmj(loanHouseOverhaulVice.getYjzmj(/**/));
            loanHouseOverhaulBasic.setTdsyzh(loanHouseOverhaulVice.getTdsyzh(/**/));
            loanHouseOverhaulBasic.setJhjgrq(loanHouseOverhaulVice.getJhjgrq(/**/));
            loanHouseOverhaulBasic.setFwzjbgjgmcjbh(loanHouseOverhaulVice.getFwzjbgjgmcjbh(/**/));
            loanHouseOverhaulBasic.setKhyhmc(loanHouseOverhaulVice.getKhyhmc(/**/));
            //endregion

            loanHousingPersonInformationBasic.setBuild(null);
            loanHousingPersonInformationBasic.setPurchasing(null);

            loanHouseBuildBasic.setcLoanHousingPersonInformationBasic(null);
            loanHousePurchasingBasic.setcLoanHousingPersonInformationBasic(null);


        }

        //endregion


        //region//担保信息
        housingGuaranteeContractExtension.setBlzl(loanHousingGuaranteeContractVice.getBlzl(/* 办理资料*/));


        if (loanHousingBusinessProcess.getDkdblx(/*贷款担保类型*/).equals(LoanGuaranteeType.抵押.getCode())) {

            //region //抵押

            for (CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice : list_loanGuaranteeMortgageVice) {

                CLoanGuaranteeMortgageExtension loanGuaranteeMortgageExtension = new CLoanGuaranteeMortgageExtension();

                loanGuaranteeMortgageExtension.setDywsyqrsfzhm(loanGuaranteeMortgageVice.getDywsyqrsfzhm(/*抵押物所有权人身份证号码*/));
                loanGuaranteeMortgageExtension.setDyfwxs(loanGuaranteeMortgageVice.getDyfwxs(/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/));
                loanGuaranteeMortgageExtension.setDywfwzl(loanGuaranteeMortgageVice.getDywfwzl(/*抵押物房屋坐落*/));
                loanGuaranteeMortgageExtension.setDywmc(loanGuaranteeMortgageVice.getDywmc(/*抵押物名称*/));
                loanGuaranteeMortgageExtension.setFwjg(loanGuaranteeMortgageVice.getFwjg(/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/));
                loanGuaranteeMortgageExtension.setDywgyqrlxdh(loanGuaranteeMortgageVice.getDywgyqrlxdh(/*抵押物共有权人联系电话*/));
                loanGuaranteeMortgageExtension.setDywpgjz(loanGuaranteeMortgageVice.getDywpgjz(/*抵押物评估价值*/));
                loanGuaranteeMortgageExtension.setDywgyqrsfzhm(loanGuaranteeMortgageVice.getDywgyqrsfzhm(/*抵押物共有权人身份证号码*/));
                loanGuaranteeMortgageExtension.setDywsyqrxm(loanGuaranteeMortgageVice.getDywsyqrxm(/*抵押物所有权人姓名*/));
                loanGuaranteeMortgageExtension.setDywsyqrlxdh(loanGuaranteeMortgageVice.getDywsyqrlxdh(/*抵押物所有权人联系电话*/));
                loanGuaranteeMortgageExtension.setFwmj(loanGuaranteeMortgageVice.getFwmj(/*房屋面积*/));
                loanGuaranteeMortgageExtension.setQszsbh(loanGuaranteeMortgageVice.getQszsbh(/*权属证书编号*/));
                loanGuaranteeMortgageExtension.setDywgyqrxm(loanGuaranteeMortgageVice.getDywgyqrxm(/*抵押物共有权人姓名*/));

                list_loanGuaranteeMortgageExtension.add(loanGuaranteeMortgageExtension);
            }


            //endregion

            housingGuaranteeContract.setcLoanGuaranteeExtensions(null);
            housingGuaranteeContract.setcLoanGuaranteePledgeExtensions(null);


        }

        if (loanHousingBusinessProcess.getDkdblx(/*贷款担保类型*/).equals(LoanGuaranteeType.质押.getCode())) {

            //region //质押

            for (CLoanGuaranteePledgeVice loanGuaranteePledgeVice : list_loanGuaranteePledgeVice) {

                CLoanGuaranteePledgeExtension loanGuaranteePledgeExtension = new CLoanGuaranteePledgeExtension();

                loanGuaranteePledgeExtension.setZywsyqrsfzhm(loanGuaranteePledgeVice.getZywsyqrsfzhm(/*质押物所有权人身份证号码*/));
                loanGuaranteePledgeExtension.setZywsyqrxm(loanGuaranteePledgeVice.getZywsyqrxm(/*质押物所有权人姓名*/));
                loanGuaranteePledgeExtension.setZywjz(loanGuaranteePledgeVice.getZywjz(/*质押物价值*/));
                loanGuaranteePledgeExtension.setZywsyqrlxdh(loanGuaranteePledgeVice.getZywsyqrlxdh(/*质押物所有权人联系电话*/));
                loanGuaranteePledgeExtension.setZywmc(loanGuaranteePledgeVice.getZywmc(/*质押物名称*/));

                list_loanGuaranteePledgeExtension.add(loanGuaranteePledgeExtension);
            }


            //endregion

            housingGuaranteeContract.setcLoanGuaranteeMortgageExtensions(null);
            housingGuaranteeContract.setcLoanGuaranteeExtensions(null);

        }

        if (loanHousingBusinessProcess.getDkdblx(/*贷款担保类型*/).equals(LoanGuaranteeType.保证.getCode())) {

            //region//保证

            for (CLoanGuaranteeVice loanGuaranteeVice : list_loanGuaranteeVice) {

                CLoanGuaranteeExtension loanGuaranteeExtension = new CLoanGuaranteeExtension();

                loanGuaranteeExtension.setYzbm(loanGuaranteeVice.getYzbm(/*邮政编码*/));
                loanGuaranteeExtension.setFrdbxjzdz(loanGuaranteeVice.getFrdbxjzdz(/*保证人现居住地址*/));
                loanGuaranteeExtension.setFrdbsfzhm(loanGuaranteeVice.getFrdbsfzhm(/*保证人身份证号码*/));
                loanGuaranteeExtension.setFrdbxm(loanGuaranteeVice.getFrdbxm(/*保证人姓名*/));
                loanGuaranteeExtension.setFrdblxdh(loanGuaranteeVice.getFrdblxdh(/*保证人联系电话*/));
                loanGuaranteeExtension.setTxdz(loanGuaranteeVice.getTxdz(/*通讯地址*/));
                loanGuaranteeExtension.setBzflx(loanGuaranteeVice.getBzflx(/*保证方类型（0：个人 1：机构）*/));

                list_loanGuaranteeExtension.add(loanGuaranteeExtension);
            }


            //endregion

            housingGuaranteeContract.setcLoanGuaranteeMortgageExtensions(null);
            housingGuaranteeContract.setcLoanGuaranteePledgeExtensions(null);

        }

        //endregion

        //region//资金信息
        loanFundsInformationBasic.setHkfs(loanFundsVice.getHkfs(/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/));
        loanFundsInformationBasic.setHtdkje(loanFundsVice.getHtdkje(/*合同贷款金额*/));
        loanFundsInformationBasic.setLlfsbl(loanFundsVice.getLlfsbl(/*利率浮动比例*/));
        loanFundsInformationBasic.setHtdkjedx(loanFundsVice.getHtdkjedx(/*合同贷款金额大写*/));
        loanFundsInformationBasic.setDklx(loanFundsVice.getDklx(/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/));
        loanFundsInformationBasic.setJkhtll(loanFundsVice.getJkhtll(/*借款合同利率*/));
        loanFundsInformationBasic.setDkqs(loanFundsVice.getDkqs(/*贷款期数*/));
        loanFundsInformationBasic.setDkdblx(loanFundsVice.getDkdblx(/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/));
        loanFundsInformationBasic.setWtkhyjce(loanFundsVice.getWtkhyjce(/*委托划扣月缴存额*/));
        loanFundsInformationBasic.setZxll(loanFundsVice.getZxll(/*执行利率*/));
        loanFundsInformationBasic.setFwts(loanFundsVice.getFwts(/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/));

        //endregion

        //personalAccount.setDkzh(null/*贷款账号*/);
        //personalAccount.setJkhtbh(null/*借款合同编号*/);
        personalAccount.setDkfxdj(LoanRiskStatus.正常.getCode()/*贷款风险等级 A.14*/);
        personalAccount.setDkffe(loanFundsInformationBasic.getHtdkje()/*贷款发放额*/);
        personalAccount.setDkffrq(null/*贷款发放日期 YYYYMMDD*/);
        personalAccount.setDkye(loanFundsInformationBasic.getHtdkje()/*贷款余额*/);
        personalAccount.setDkll(loanFundsInformationBasic.getJkhtll()/*贷款利率*/);
        personalAccount.setLlfdbl(loanFundsInformationBasic.getLlfsbl()/*利率浮动比例*/);
        personalAccount.setDkqs(loanFundsVice.getDkqs()/*贷款期数*/);
        personalAccount.setDqyhje(CommLoanAlgorithm.currentBX(loanFundsInformationBasic.getHtdkje(), loanFundsVice.getDkqs()==null?0:loanFundsVice.getDkqs().intValue(), loanFundsInformationBasic.getHkfs(), loanFundsInformationBasic.getJkhtll(), 1/*当期应还金额*/));
        personalAccount.setDqyhlx(CommLoanAlgorithm.overdueThisPeriodLX(loanFundsInformationBasic.getHtdkje(), 1, loanFundsInformationBasic.getHkfs(), loanFundsInformationBasic.getJkhtll(), loanFundsVice.getDkqs() == null ? 0 :loanFundsVice.getDkqs().intValue())/*当前应还利息*/);
        personalAccount.setDqyhbj(personalAccount.getDqyhje().subtract(personalAccount.getDqyhlx())/*当前应还本金*/);
        personalAccount.setDqjhhkje(personalAccount.getDqyhje()/*当期计划还款金额*/);
        personalAccount.setDqjhghbj(personalAccount.getDqyhbj()/*当期计划归还本金*/);
        personalAccount.setDqjhghlx(personalAccount.getDqyhlx()/*当期计划归还利息*/);
        personalAccount.setDqyhfx(new BigDecimal("0")/*当期应还罚息*/);
        //personalAccount.setDkjqrq(null/*贷款结算日期 YYYYMMDD*/);
        personalAccount.setHsbjze(new BigDecimal("0")/*回收本金总额*/);
        personalAccount.setHslxze(new BigDecimal("0")/*回收利息总额*/);
        personalAccount.setFxze(new BigDecimal("0")/*罚息总额*/);
        personalAccount.setTqghbjze(new BigDecimal("0")/*提前归还本金总额*/);
        personalAccount.setYqbjze(new BigDecimal("0")/*逾期本金总额*/);
        personalAccount.setYqlxze(new BigDecimal("0")/*逾期利息总额*/);
        personalAccount.setLjyqqs(new BigDecimal("0")/*累计逾期期数*/);

        //endregion

        CLoanHousingPersonInformationBasic savedBasic = DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(loanHousingPersonInformationBasic).saveThenFetchObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        savedBasic.setDkzh(savedBasic.getPersonalAccount().getDkzh());

        loanHousingBusinessProcess.setDkzh(savedBasic.getPersonalAccount().getDkzh());

        DAOBuilder.instance(this.loanHousingPersonInformationBasicDAO).entity(savedBasic).save(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        DAOBuilder.instance(this.loanHousingBusinessProcessDAO).entity(loanHousingBusinessProcess).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) { throw new ErrorException(e);}
        });
    }

    /*
    *  completed
    *
    * !逻辑已完成
    * 
    *  已测试 lian
    * */
    @Override
    public GetApplicantResponse getLoanReviewDetails(TokenContext tokenContext,String YWLSH) {

        //region //查询必要字段

        CLoanHousingBusinessProcess loanHousingBusinessProcess = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

        }}).getObject(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) { throw new ErrorException(e); }

        });

        //endregion

        //region //数据完整性验证

        if (loanHousingBusinessProcess == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingPersonInformationVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousePurchasingVice() == null && loanHousingBusinessProcess.getLoanHouseBuildVice() == null && loanHousingBusinessProcess.getLoanHouseOverhaulVice() == null) {

            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        if (loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }


        if (loanHousingBusinessProcess.getLoanFundsVice() == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "业务信息");
        }

        //endregion

        //region //必要数据声明

        CLoanHousingPersonInformationVice loanHousingPersonInformationVice = loanHousingBusinessProcess.getLoanHousingPersonInformationVice();

        CLoanHousePurchasingVice loanHousePurchasingVice = loanHousingBusinessProcess.getLoanHousePurchasingVice();

        CLoanHouseBuildVice loanHouseBuildVice = loanHousingBusinessProcess.getLoanHouseBuildVice();

        CLoanFundsVice loanFundsVice = loanHousingBusinessProcess.getLoanFundsVice();

        CLoanHousingCoborrowerVice loanHousingCoborrowerVice = loanHousingBusinessProcess.getLoanHousingCoborrowerVice();

        CLoanHouseOverhaulVice loanHouseOverhaulVice = loanHousingBusinessProcess.getLoanHouseOverhaulVice();

        CLoanHousingGuaranteeContractVice loanHousingGuaranteeContractVice = loanHousingBusinessProcess.getLoanHousingGuaranteeContractVice();

        List<CLoanGuaranteeMortgageVice> list_loanGuaranteeMortgageVice = loanHousingGuaranteeContractVice.getGuaranteeMortgageVices() == null ? new ArrayList<CLoanGuaranteeMortgageVice>() : loanHousingGuaranteeContractVice.getGuaranteeMortgageVices();


        List<CLoanGuaranteeVice> list_loanGuaranteeVice = loanHousingGuaranteeContractVice.getGuaranteeVices() == null ? new ArrayList<CLoanGuaranteeVice>() : loanHousingGuaranteeContractVice.getGuaranteeVices();


        List<CLoanGuaranteePledgeVice> list_loanGuaranteePledgeVice = loanHousingGuaranteeContractVice.getGuaranteePledgeVices() == null ? new ArrayList<CLoanGuaranteePledgeVice>() : loanHousingGuaranteeContractVice.getGuaranteePledgeVices();

        //endregion

        return new GetApplicantResponse() {{

            this.setCommonBorrowerInformation(loanHousingCoborrowerVice == null ? new GetApplicantResponseCommonBorrowerInformation() : new GetApplicantResponseCommonBorrowerInformation() {{

                this.setHKSZD(loanHousingCoborrowerVice.getHkszd()/*户口所在地*/);
                this.setSCZL(loanHousingCoborrowerVice.getBlzl()/*提交资料*/);
                this.setGTJKRGJJZH(loanHousingCoborrowerVice.getGtjkrgjjzh()/*共同借款人公积金账号*/);
                this.setCHGX(loanHousingCoborrowerVice.getCdgx()/*参货关系 （0：本人或户主 1：配偶 2：子 3：女 4：孙子、孙女或外孙子、外孙女 5：父母 6：祖父母或外祖父母 7：兄、弟、姐、妹 8：其他）*/);
                this.setSJHM(loanHousingCoborrowerVice.getSjhm()/*手机号码*/);
                this.setJCD(loanHousingCoborrowerVice.getJcd()/*缴存地*/);
                this.setGDDHHM(loanHousingCoborrowerVice.getGddhhm()/*固定电话号码*/);
                this.setGTJKRXM(loanHousingCoborrowerVice.getGtjkrxm()/*共同借款人姓名*/);
                this.setGTJKRZJLX(loanHousingCoborrowerVice.getGtjkrzjlx()/*共同借款人证件类型*/);
                this.setYSR(loanHousingCoborrowerVice.getYsr() + ""/*月收入*/);
                this.setGTJKRZJHM(loanHousingCoborrowerVice.getGtjkrzjhm()/*共同借款人证件号码*/);
                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingCoborrowerVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingCoborrowerVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingCoborrowerVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingCoborrowerVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingCoborrowerVice.getDwdz()/*单位地址*/);
                }});
                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingCoborrowerVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingCoborrowerVice.getJzny(), formatNY)/*缴至年月*/);
                    this.setYJCE(loanHousingCoborrowerVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingCoborrowerVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingCoborrowerVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS(loanHousingCoborrowerVice.getLxzcjcys() == null ? null : (loanHousingCoborrowerVice.getLxzcjcys().intValue() + "")/*连续正常缴存月数*/);
                }});
            }});

            this.setSCZL(loanHousingBusinessProcess.getBlzl()/*提交资料*/);

            this.setSQSJ(DateUtil.date2Str(loanHousingBusinessProcess.getBlsj(), format)/*申请时间*/);

            this.setHouseInformation(new GetApplicantResponseHouseInformation() {{

                this.setPurchaseSecondInformation((loanHousePurchasingVice == null || !loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : (loanHousePurchasingVice == null ? new GetApplicantResponseHouseInformationPurchaseSecondInformation() : new GetApplicantResponseHouseInformationPurchaseSecondInformation() {{

                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setGRSKYHZH(loanHousePurchasingVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setKHYHMC(loanHousePurchasingVice.getKhyhmc()/*开户银行名称*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setYHKHM(loanHousePurchasingVice.getYhkhm()/*银行开户名*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + "");
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSFK(loanHousePurchasingVice.getYfk() + ""/*首付款*/);
                    this.setSCZL(loanHousePurchasingVice.getBlzl()/*上传资料*/);
                }}));

                this.setDKYT(loanHousingBusinessProcess.getDkyt() + ""/*贷款用途*/);

                this.setSFWESF(loanHousePurchasingVice == null ? null : loanHousePurchasingVice.getSfwesf() ? "1" : "0"/*是否为二手房*/);

                this.setPurchaseFirstInformation((loanHousePurchasingVice == null || loanHousePurchasingVice.getSfwesf()) ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : (loanHousePurchasingVice == null ? new GetApplicantResponseHouseInformationPurchaseFirstInformation() : new GetApplicantResponseHouseInformationPurchaseFirstInformation() {{
                    this.setFWJZMJ(loanHousePurchasingVice.getFwjzmj() + ""/*房屋建筑面积*/);
                    this.setFWZL(loanHousePurchasingVice.getFwzl()/*房屋坐落*/);
                    this.setFWTNMJ(loanHousePurchasingVice.getFwtnmj() + ""/*房屋套内面积*/);
                    this.setFWXS(loanHousePurchasingVice.getFwxs() + ""/*房屋形式（0：在建房  1：现房）*/);
                    this.setFWXZ(loanHousePurchasingVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWZJ(loanHousePurchasingVice.getFwzj() + ""/*房屋总价*/);
                    this.setFWJG(loanHousePurchasingVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setFWJGRQ(DateUtil.date2Str(loanHousePurchasingVice.getFwjgrq(), format)/*房屋竣工日期*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人开户银行名称*/);
                    this.setHTJE(loanHousePurchasingVice.getHtje() + ""/*合同金额*/);
                    this.setSFRZHHM(loanHousePurchasingVice.getSfrzhhm()/*售房人账户号码*/);
                    this.setLPMC(loanHousePurchasingVice.getLpmc()/*楼盘名称*/);
                    this.setGFHTBH(loanHousePurchasingVice.getGfhtbh()/*购房合同编号*/);
                    this.setDanJia(loanHousePurchasingVice.getDanJia() + ""/*单价*/);
                    this.setLXFS(loanHousePurchasingVice.getLxfs()/*联系方式*/);
                    this.setSFRMC(loanHousePurchasingVice.getSfrmc()/*售房人名称*/);
                    this.setSPFYSXKBH(loanHousePurchasingVice.getSpfysxkbh()/*商品房预售许可编号*/);
                    this.setSFK(loanHousePurchasingVice.getSfk() + ""/*首付款*/);
                    this.setSFRKHYHMC(loanHousePurchasingVice.getSfrkhyhmc()/*售房人银行开户名*/);
                    this.setSFRYHKHM(loanHousePurchasingVice.getSfryhkhm());
                    this.setSCZL(loanHousePurchasingVice.getBlzl()/*上传资料*/);
                }}));

                this.setOverhaulInformation(loanHouseOverhaulVice == null ? new GetApplicantResponseHouseInformationOverhaulInformation() : new GetApplicantResponseHouseInformationOverhaulInformation() {{
                    this.setDXGCYS(loanHouseOverhaulVice.getDxgcys() + ""/*大修工程预算*/);
                    this.setGRSKYHZH(loanHouseOverhaulVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWZJBGJGMCJBH(loanHouseOverhaulVice.getFwzjbgjgmcjbh(/*房屋质检报告机关名称及编号*/));
                    this.setFXHCS(loanHouseOverhaulVice.getTdsyzh()/*土地使用证号*/);
                    this.setFWZL(loanHouseOverhaulVice.getFwzl()/*房屋坐落*/);
                    this.setYJZMJ(loanHouseOverhaulVice.getYjzmj() + ""/*原建筑面积*/);
                    this.setKHYHMC(loanHouseOverhaulVice.getKhyhmc()/*开户银行名称*/);
                    this.setSCZL(loanHouseOverhaulVice.getBlzl()/*提交资料*/);
                    this.setYBDCZH(loanHouseOverhaulVice.getYbdczh()/*原不动产证号*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseOverhaulVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setYHKHM(loanHouseOverhaulVice.getYhkhm()/*银行开户名*/);
                    this.setSCZL(loanHouseOverhaulVice.getBlzl()/*上传资料*/);
                }});

                this.setBuildInformation(loanHouseBuildVice == null ? new GetApplicantResponseHouseInformationBuildInformation() : new GetApplicantResponseHouseInformationBuildInformation() {{

                    this.setGRSKYHZH(loanHouseBuildVice.getGrskyhzh()/*个人收款银行账号*/);
                    this.setFWXZ(loanHouseBuildVice.getFwxz() + ""/*房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他）*/);
                    this.setFWJG(loanHouseBuildVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：其他）*/);
                    this.setJHJGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhjgrq(), format)/*计划竣工日期*/);
                    this.setFWZLDZ(loanHouseBuildVice.getFwzl()/*房屋坐落地址*/);
                    this.setTDSYZH(loanHouseBuildVice.getTdsyzh()/*土地使用证号*/);
                    this.setGRSYZJ(loanHouseBuildVice.getGrsyzj() + ""/*个人使用资金*/);
                    this.setJZCS(loanHouseBuildVice.getJzcs()/*建造层数*/);
                    this.setSCZL(loanHouseBuildVice.getBlzl()/*提交资料*/);
                    this.setYHKHM(loanHouseBuildVice.getYhkhm()/*银行开户名*/);
                    this.setJZYDGHXKZH(loanHouseBuildVice.getJzydghxkzh()/*建造用地规划许可证号*/);
                    this.setPZJGWH(loanHouseBuildVice.getPzjgwh()/*批准机关文号*/);
                    this.setJHJZFY(loanHouseBuildVice.getJhjzfy() + ""/*计划建造费用*/);
                    this.setKHHYHMC(loanHouseBuildVice.getKhhyhmc()/*开户行银行名称*/);
                    this.setJZGCGHXKZH(loanHouseBuildVice.getJzgcghxkzh()/*建造工程规划许可证号*/);
                    this.setJCHJZMJ(loanHouseBuildVice.getJchjzmj() + ""/*建成后建造面积*/);
                    this.setJHKGRQ(DateUtil.date2Str(loanHouseBuildVice.getJhkgrq(), format)/*计划开工日期*/);
                    this.setJSGCSGXKZH(loanHouseBuildVice.getJsgcsgxkzh()/*建设工程施工许可证号*/);
                    this.setSCZL(loanHouseBuildVice.getBlzl()/*上传资料*/);
                }});
            }});

            this.setYWLSH(YWLSH/*业务流水号*/);

            this.setreviewInformation(new ArrayList<GetApplicantResponseReviewInformation>() {{//审核信息


                for (ReviewInfo reviewInfo : (List<ReviewInfo>) iSaveAuditHistory.getAuditHistoryList(YWLSH)) {

                    this.add(new GetApplicantResponseReviewInformation() {{
                        String ywwdmc = cAccountNetworkDAO.get(reviewInfo.getYWWD()).getMingCheng();
                        this.setYWWD(ywwdmc);
                        this.setShiJian(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, reviewInfo.getSHSJ(), format));
                        this.setQuDao(reviewInfo.getCZQD());
                        this.setZhiWu(reviewInfo.getZhiWu());
                        this.setYiJian(reviewInfo.getYYYJ());
                        this.setCZY(reviewInfo.getCZY());

                    }});
                }
            }});

            this.setCollateralInformation(new GetApplicantResponseCollateralInformation() {{

                this.setMortgageInformation(new ArrayList<GetApplicantResponseCollateralInformationMortgageInformation>() {{

                    for (CLoanGuaranteeMortgageVice loanGuaranteeMortgageVice : list_loanGuaranteeMortgageVice) {

                        this.add(new GetApplicantResponseCollateralInformationMortgageInformation() {{
                            this.setDYWSYQRSFZHM(loanGuaranteeMortgageVice.getDywsyqrsfzhm()/*抵押物所有权人身份证号码*/);
                            this.setDYFWXS(loanGuaranteeMortgageVice.getDyfwxs() + ""/*抵押房屋形式（0：住宅 （期房） 1：住宅（现房） 2：商铺 3：其他）*/);
                            this.setDYWFWZL(loanGuaranteeMortgageVice.getDywfwzl()/*抵押物房屋坐落*/);
                            this.setDYWMC(loanGuaranteeMortgageVice.getDywmc()/*抵押物名称*/);
                            this.setFWJG(loanGuaranteeMortgageVice.getFwjg() + ""/*房屋结构（0：框架 1：砖混 2：土木 3：其他）*/);
                            this.setDYWGYQRLXDH(loanGuaranteeMortgageVice.getDywgyqrlxdh()/*抵押物共有权人联系电话*/);
                            this.setDYWPGJZ(loanGuaranteeMortgageVice.getDywpgjz() + ""/*抵押物评估价值*/);
                            this.setDYWGYQRSFZHM(loanGuaranteeMortgageVice.getDywgyqrsfzhm()/*抵押物共有权人身份证号码*/);
                            this.setDYWSYQRXM(loanGuaranteeMortgageVice.getDywsyqrxm()/*抵押物所有权人姓名*/);
                            this.setDYWSYQRLXDH(loanGuaranteeMortgageVice.getDywsyqrlxdh()/*抵押物所有权人联系电话*/);
                            this.setFWMJ(loanGuaranteeMortgageVice.getFwmj() + ""/*房屋面积1*/);
                            this.setQSZSBH(loanGuaranteeMortgageVice.getQszsbh()/*权属证书编号*/);
                            this.setDYWGYQRXM(loanGuaranteeMortgageVice.getDywgyqrxm()/*抵押物共有权人姓名*/);
                        }});

                    }
                }});

                this.setPledgeInformation(new ArrayList<GetApplicantResponseCollateralInformationPledgeInformation>() {{

                    for (CLoanGuaranteePledgeVice loanGuaranteePledgeVice : list_loanGuaranteePledgeVice) {

                        this.add(new GetApplicantResponseCollateralInformationPledgeInformation() {{
                            this.setZYWSYQRSFZHM(loanGuaranteePledgeVice.getZywsyqrsfzhm()/*质押物所有权人身份证号码*/);
                            this.setZYWSYQRXM(loanGuaranteePledgeVice.getZywsyqrxm()/*质押物所有权人姓名*/);
                            this.setZYWJZ(loanGuaranteePledgeVice.getZywjz() + ""/*质押物价值*/);
                            this.setZYWSYQRLXDH(loanGuaranteePledgeVice.getZywsyqrlxdh()/*质押物所有权人联系电话*/);
                            this.setZYWMC(loanGuaranteePledgeVice.getZywmc()/*质押物名称*/);
                        }});

                    }
                }});

                this.setDKDBLX(loanHousingBusinessProcess.getDkdblx()/*贷款担保类型*/);

                this.setGuaranteeInformation(new ArrayList<GetApplicantResponseCollateralInformationGuaranteeInformation>() {{

                    for (CLoanGuaranteeVice loanGuaranteeVice : list_loanGuaranteeVice) {

                        this.add(new GetApplicantResponseCollateralInformationGuaranteeInformation() {{
                            this.setYZBM(loanGuaranteeVice.getYzbm()/*邮政编码*/);
                            this.setBZRXJZDZ(loanGuaranteeVice.getFrdbxjzdz()/*保证人现居住地址*/);
                            this.setBZRSFZHM(loanGuaranteeVice.getFrdbsfzhm()/*保证人身份证号码*/);
                            this.setBZRXM(loanGuaranteeVice.getFrdbxm()/*保证人姓名*/);
                            this.setBZRLXDH(loanGuaranteeVice.getFrdblxdh()/*保证人联系电话*/);
                            this.setTXDZ(loanGuaranteeVice.getTxdz()/*通讯地址*/);
                            this.setBZFLX(loanGuaranteeVice.getBzflx()/*保证方类型（0：个人 1：机构）*/);
                        }});

                    }
                }});

                this.setSCZL(loanHousingGuaranteeContractVice.getBlzl()/*提交资料*/);
            }});

            this.setCapitalInformation(new GetApplicantResponseCapitalInformation() {{
                this.setHTDKJEDX(loanFundsVice.getHtdkjedx()/*合同贷款金额大写*/);
                this.setDKLX(loanFundsVice.getDklx() + ""/*贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）*/);
                this.setDKQS(loanFundsVice.getDkqs() + ""/*贷款期数*/);
                this.setHKFS(loanFundsVice.getHkfs() + ""/*还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）*/);
                this.setHTDKJE(loanFundsVice.getHtdkje() + ""/*合同贷款金额*/);
                this.setLLFSBL(loanFundsVice.getLlfsbl() + ""/*利率浮动比例*/);
                this.setJKHTLL(loanFundsVice.getJkhtll()==null?"0":(loanFundsVice.getJkhtll().doubleValue() + "")/*借款合同利率*/);
                //this.setZXLL(loanFundsVice.getZxll() + ""/*执行利率*/);
                this.setWTKHYJCE(loanFundsVice.getWtkhyjce() ? "1" : "0"/*委托扣划月缴存额*/);
                this.setDKDBLX(loanFundsVice.getDkdblx() + ""/*贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）*/);
                this.setFWTS(loanFundsVice.getFwts() + ""/*房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）*/);
            }});

            this.setApplicantInformation(new GetApplicantResponseApplicantInformation() {{
                this.setBorrowerInformation(new GetApplicantResponseApplicantInformationBorrowerInformation() {{
                    this.setHYZK(loanHousingPersonInformationVice.getHyzk() + ""/*婚姻状态（0：已婚 1：未婚 2：丧偶 3：离婚 4：未说明的婚姻状况）*/);
                    this.setNianLing(loanHousingPersonInformationVice.getNianLing()/*年龄*/);
                    this.setJTZZ(loanHousingPersonInformationVice.getJtzz()/*家庭住址*/);
                    this.setCSNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingPersonInformationVice.getCsny(), formatNY)/*出生年月*/);
                    this.setJKRZJHM(loanHousingPersonInformationVice.getJkrzjhm()/*借款人证件号码*/);
                    this.setYGXZ(loanHousingPersonInformationVice.getYgxz() + ""/*用工性质（0：正式职工 1：合同制 2：聘用制）*/);
                    this.setYSR(loanHousingPersonInformationVice.getYsr() + ""/*月收入*/);
                    this.setJKRXM(loanHousingPersonInformationVice.getJkrxm()/*借款人姓名*/);
                    this.setHKSZD(loanHousingPersonInformationVice.getHkszd()/*户口所在地*/);
                    this.setZhiCheng(loanHousingPersonInformationVice.getZhiCheng()/*职称*/);
                    this.setJKRZJLX(loanHousingPersonInformationVice.getJkrzjlx() + ""/*借款人证件类型*/);
                    this.setSJHM(loanHousingPersonInformationVice.getSjhm()/*手机号码*/);
                    this.setJKZK(loanHousingPersonInformationVice.getJkzk() + ""/*健康状态（0：良好 1：一般 2：差）*/);
                    this.setGDDHHM(loanHousingPersonInformationVice.getGddhhm()/*固定电话号码*/);
                    this.setZYJJLY(loanHousingPersonInformationVice.getZyjjly() + ""/*主要经济来源（0：工资收入 1：个体经营收入 2：其他非工资收入）*/);
                    this.setXingBie(loanHousingPersonInformationVice.getXingBie() + ""/*性别*/);
                    this.setJTYSR(loanHousingPersonInformationVice.getJtysr() + ""/*家庭月收入*/);
                    this.setXueLi(loanHousingPersonInformationVice.getXueLi()/*学历*/);
                    this.setZhiWu(loanHousingPersonInformationVice.getZhiWu()/*职务*/);
                }});
                this.setJKRGJJZH(loanHousingPersonInformationVice.getJkrgjjzh()/*借款人公积金账号*/);
                this.setUnitInformation(new GetApplicantResponseApplicantInformationUnitInformation() {{
                    this.setDWDH(loanHousingPersonInformationVice.getDwdh()/*单位电话*/);
                    this.setDWMC(loanHousingPersonInformationVice.getDwmc()/*单位名称*/);
                    this.setDWZH(loanHousingPersonInformationVice.getDwzh()/*单位账号*/);
                    this.setDWXZ(loanHousingPersonInformationVice.getDwxz() + ""/*单位性质*/);
                    this.setDWDZ(loanHousingPersonInformationVice.getDwdz()/*单位地址*/);
                }});
                this.setJCD(loanHousingPersonInformationVice.getJcd()/*缴存地*/);
                this.setSCZL(loanHousingPersonInformationVice.getBlzl()/* 办理资料*/);
                this.setAccountInformation(new GetApplicantResponseApplicantInformationAccountInformation() {{
                    this.setGRZHZT(loanHousingPersonInformationVice.getGrzhzt() + ""/*个人账户状态*/);
                    this.setJZNY(DateUtil.DBdate2Str(DateUtil.dbformatYear_Month, loanHousingPersonInformationVice.getJzny(), formatNY)/*缴至年月*/);
                    this.setYJCE(loanHousingPersonInformationVice.getYjce() + ""/*月缴存额*/);
                    this.setGRZHYE(loanHousingPersonInformationVice.getGrzhye() + ""/*个人账户余额*/);
                    this.setGRJCJS(loanHousingPersonInformationVice.getGrjcjs() + ""/*个人缴存基数*/);
                    this.setLXZCJCYS((loanHousingPersonInformationVice.getLxzcjcys() == null ? 0 : loanHousingPersonInformationVice.getLxzcjcys().intValue()) + ""/*连续正常缴存月数*/);
                }});
            }});

            this.setYWWD(loanHousingBusinessProcess.getYwwd().getMingCheng());

            this.setCZY(loanHousingBusinessProcess.getCzy());
        }};
    }


    /*
      *  completed
      *
      * !逻辑已完成
      *
      *  已测试 lian
      *
      * !存在问题
      *
      * ~swagger未分页
      *
      * */
    @Override
    public PageRes<LoanReviewListResponse> getLoanReview(TokenContext tokenContext,String SFCL, String JKRXM, String status, String startTime, String endTime, String pageSize, String page) {

        PageRes pageRes = new PageRes();

        int page_number = 0;

        int pagesize_number = 0;

        try {

            if (page != null) {
                page_number = Integer.parseInt(page);
            }

            if (pageSize != null) {
                pagesize_number = Integer.parseInt(pageSize);
            }

        } catch (Exception e) {

            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "页码");
        }


        List<CLoanHousingBusinessProcess> list_process = DAOBuilder.instance(this.loanHousingBusinessProcessDAO).searchFilter(new HashMap<String, Object>() {{

            if (StringUtil.notEmpty(JKRXM)) {
                this.put("loanHousingPersonInformationVice.jkrxm", JKRXM);
            }

            if (StringUtil.notEmpty(SFCL) && SFCL.equals("0")) {
                this.put("step", "待审核");
            }

            this.put("cznr", LoanBusinessType.贷款发放.getCode());

            if (StringUtil.notEmpty(status) && !status.equals("00")) this.put("step", new HashMap<String, String>() {{

                this.put("0", "审核不通过");
                this.put("1", "办结");
                this.put("2", "待入账");

            }}.get(status));

        }}).extension(new IBaseDAO.CriteriaExtension() {

            @Override
            public void extend(Criteria criteria) {

                if (DateUtil.safeStr2Date(format, startTime) != null) {
                    criteria.add(Restrictions.ge("blsj", DateUtil.safeStr2DBDate(format, startTime,DateUtil.dbformatYear_Month)));
                }

                if (DateUtil.safeStr2Date(format, endTime) != null) {
                    criteria.add(Restrictions.lt("blsj", DateUtil.safeStr2DBDate(format, endTime,  DateUtil.dbformatYear_Month)));
                }

                if (StringUtil.notEmpty(SFCL) && SFCL.equals("1")) {

                    criteria.add(Restrictions.and(Restrictions.ne("step", "待审核"), Restrictions.ne("step", "新建")));
                }
                criteria.createAlias("ywwd","ywwd");

                criteria.add(Restrictions.eq("ywwd.id",tokenContext.getUserInfo().getYWWD()));
            }
        }).pageOption(pageRes, pagesize_number, page_number).searchOption(SearchOption.FUZZY).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        });


        return new PageRes<LoanReviewListResponse>() {{

            this.setResults(new ArrayList<LoanReviewListResponse>() {{

                for (CLoanHousingBusinessProcess process : list_process)
                    this.add(new LoanReviewListResponse() {{
                        this.setJKRXM(process.getLoanHousingPersonInformationVice().getJkrxm()/*借款人姓名*/);
                        this.setStatus(process.getStep()/*状态*/);
                        this.setYWLSH(process.getYwlsh()/*业务流水号*/);
                        this.setHTDKJE(process.getLoanFundsVice().getHtdkje() + ""/*合同贷款金额*/);
                        this.setSLSJ(StringUtil.notEmpty(process.getStep()) && (!process.getStep().equals("0") && !(process.getStep().equals("1"))) ? null : DateUtil.date2Str(process.getBlsj(), format)/*受理时间*/);
                        this.setDDSJ(StringUtil.notEmpty(process.getStep()) && (process.getStep().equals("0") || (process.getStep().equals("1"))) ? null : DateUtil.date2Str(process.getBlsj(), format)/*到达时间*/);
                        this.setJKRZJHM(process.getLoanHousingPersonInformationVice().getJkrzjhm()/*借款人证件号码*/);
                        this.setYWWD(process.getYwwd().getMingCheng()/*业务网点*/);
                        this.setCZY(process.getCzy()/*操作员*/);
                        //this.setXuHao(null/*序号*/);
                    }});
            }});

            this.setCurrentPage(pageRes.getCurrentPage());

            this.setNextPageNo(pageRes.getNextPageNo());

            this.setPageCount(pageRes.getPageCount());

            this.setTotalCount(pageRes.getTotalCount());

            this.setPageSize(pageRes.getPageSize());
        }};
    }
}
