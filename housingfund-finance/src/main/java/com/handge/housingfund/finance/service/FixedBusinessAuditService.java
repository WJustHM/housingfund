package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IFixedBusinessAuditService;
import com.handge.housingfund.common.service.finance.model.FixedBusinessAudit;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICFinanceActived2FixedDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedDrawDAO;
import com.handge.housingfund.database.entities.CFinanceActived2Fixed;
import com.handge.housingfund.database.entities.CFinanceFixedDraw;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
@Component
public class FixedBusinessAuditService implements IFixedBusinessAuditService{

    @Autowired
    ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;    //1

    @Autowired
    ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;    //2

    @Autowired
    IFinanceTrader iFinanceTrader;

    @Override
    public PageRes<FixedBusinessAudit> getAuditRecords(String khyhmc, String acctNo, String ywlx, String audit, String begin, String end, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(khyhmc)) filter.put("khyhmc", khyhmc);
        if (StringUtil.notEmpty(acctNo)) filter.put("acct_no", acctNo);
        if ("0".equals(audit) || "待审核".equals(audit))
            filter.put("cFinanceManageFinanceExtension.step", "待审核");
        else {
            ArrayList<String> steps = new ArrayList<>();
            steps.add("待活转定");
            steps.add("审核不通过");
            filter.put("cFinanceManageFinanceExtension.step", steps);
        }

        PageRes pageRes = new PageRes();
        PageRes<FixedBusinessAudit> fixedBusinessAuditPageRes = new PageRes<>();
        ArrayList<FixedBusinessAudit> fixedBusinessAudits = new ArrayList<>();

        if ("09".equals(ywlx) || "所有".equals(ywlx)) {
            List<CFinanceActived2Fixed> cFinanceActived2Fixeds = DAOBuilder.instance(icFinanceActived2FixedDAO).searchFilter(filter).pageOption(pageRes, pageSize, pageNo)
                    .orderOption("created_at", Order.DESC)
                    .extension(getExtension(audit, begin, end))
                    .getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                        }
                    });

            for (CFinanceActived2Fixed cFinanceActived2Fixed : cFinanceActived2Fixeds) {
                FixedBusinessAudit fixedBusinessAudit = new FixedBusinessAudit();
                fixedBusinessAudit.setAcct_no(cFinanceActived2Fixed.getAcct_no());
                fixedBusinessAudit.setCzy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy());
                fixedBusinessAudit.setDdsj(DateUtil.date2Str(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getDdsj(),"yyyy-MM-dd"));
                fixedBusinessAudit.setDeposti_amt(cFinanceActived2Fixed.getAmt() != null ? cFinanceActived2Fixed.getAmt().toPlainString() : null);
                fixedBusinessAudit.setId(cFinanceActived2Fixed.getId());
                fixedBusinessAudit.setKhyhmc(cFinanceActived2Fixed.getKhyhmc());
                fixedBusinessAudit.setStep(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep());
                fixedBusinessAudit.setShsj(DateUtil.date2Str(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getShsj(),"yyyy-MM-dd"));
                fixedBusinessAudit.setYwlsh(cFinanceActived2Fixed.getYwlsh());
                fixedBusinessAudit.setYwlx(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getYwlx());

                fixedBusinessAudits.add(fixedBusinessAudit);
            }
            fixedBusinessAuditPageRes.setResults(fixedBusinessAudits);
        } else if ("10".equals(ywlx) || "所有".equals(ywlx)) {
            List<CFinanceFixedDraw> cFinanceFixedDraws = DAOBuilder.instance(icFinanceFixedDrawDAO).searchFilter(filter).pageOption(pageRes, pageSize, pageNo)
                    .orderOption("created_at", Order.DESC)
                    .extension(getExtension(audit, begin, end))
                    .getList(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                        }
                    });
            for (CFinanceFixedDraw cFinanceFixedDraw : cFinanceFixedDraws) {
                FixedBusinessAudit fixedBusinessAudit = new FixedBusinessAudit();
                fixedBusinessAudit.setAcct_no(cFinanceFixedDraw.getAcct_no());
                fixedBusinessAudit.setCzy(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy());
                fixedBusinessAudit.setDdsj(DateUtil.date2Str(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getDdsj(),"yyyy-MM-dd"));
                fixedBusinessAudit.setDeposti_amt(cFinanceFixedDraw.getDeposti_amt() != null ? cFinanceFixedDraw.getDeposti_amt().toPlainString() : null);
                fixedBusinessAudit.setId(cFinanceFixedDraw.getId());
                fixedBusinessAudit.setKhyhmc(cFinanceFixedDraw.getKhyhmc());
                fixedBusinessAudit.setStep(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep());
                fixedBusinessAudit.setShsj(DateUtil.date2Str(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getShsj(),"yyyy-MM-dd"));
                fixedBusinessAudit.setYwlsh(cFinanceFixedDraw.getYwlsh());
                fixedBusinessAudit.setYwlx(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getYwlx());

                fixedBusinessAudits.add(fixedBusinessAudit);
            }
            fixedBusinessAuditPageRes.setResults(fixedBusinessAudits);
        } else if ("11".equals(ywlx) || "所有".equals(ywlx)) {

        } else if ("12".equals(ywlx) || "所有".equals(ywlx)) {

        }

        fixedBusinessAuditPageRes.setCurrentPage(pageRes.getCurrentPage());
        fixedBusinessAuditPageRes.setNextPageNo(pageRes.getNextPageNo());
        fixedBusinessAuditPageRes.setPageCount(pageRes.getPageCount());
        fixedBusinessAuditPageRes.setPageSize(pageRes.getPageSize());
        fixedBusinessAuditPageRes.setTotalCount(pageRes.getTotalCount());

        return fixedBusinessAuditPageRes;
    }

    @Override
    public void auditActived2Fixed(String id, ReviewInfo reviewInfo) {
        //TODO 审核, 走状态机
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息,业务不存在");
        }

        if ("01".equals(reviewInfo.getSHJG())) {
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("待活转定");
        } else {
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("审核不通过");
        }
        cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setShsj(new Date());

        DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败");
            }
        });

        if ("01".equals(reviewInfo.getSHJG())) {
            //TODO 调结算平台
            iFinanceTrader.actived2Fixed(id);
        }
    }

    @Override
    public void auditFixedDraw(String id, ReviewInfo reviewInfo) {
        //TODO 审核, 走状态机
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });
        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息,业务不存在");
        }

        if ("01".equals(reviewInfo.getSHJG())) {
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("通过");
        } else {
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("不通过");
        }
        cFinanceFixedDraw.getcFinanceManageFinanceExtension().setShsj(new Date());

        DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败");
            }
        });
        if ("01".equals(reviewInfo.getSHJG())) {
            //TODO 调结算平台
            iFinanceTrader.fixedDraw(id);
        }
    }

    private IBaseDAO.CriteriaExtension getExtension(String audit, String begin, String end) {
       return new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                Date beginDate;
                Date endDate;
                if ("0".equals(audit) || "待审核".equals(audit)) {
                    if (StringUtil.notEmpty(begin)) {
                        try {
                            beginDate = DateUtil.str2Date("yyyyMMdd", begin);
                        } catch (ParseException e) {
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                        }
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.shsj", beginDate));
                    }
                    if (StringUtil.notEmpty(end)) {
                        try {
                            endDate = DateUtil.str2Date("yyyyMMdd", end);
                        } catch (ParseException e) {
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                        }
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.shsj", endDate));
                    }
                } else {
                    if (StringUtil.notEmpty(begin)) {
                        try {
                            beginDate = DateUtil.str2Date("yyyyMMdd", begin);
                        } catch (ParseException e) {
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                        }
                        criteria.add(Restrictions.ge("cFinanceManageFinanceExtension.ddsj", beginDate));
                    }
                    if (StringUtil.notEmpty(end)) {
                        try {
                            endDate = DateUtil.str2Date("yyyyMMdd", end);
                        } catch (ParseException e) {
                            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "日期格式有误");
                        }
                        criteria.add(Restrictions.le("cFinanceManageFinanceExtension.ddsj", endDate));
                    }
                }
            }
        };
    }
}
