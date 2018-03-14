package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.review.model.ReviewInfo;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAuditHistoryDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.entities.CAuditHistory;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/15.
 * 描述
 */
public class SaveAuditHistoryImpl implements ISaveAuditHistory {

    @Autowired
    ICAuditHistoryDAO auditHistoryDAO;

    @Autowired
    ICRoleDAO roleDAO;

    @Override
    public void saveNormalBusiness(String YWLSH, TokenContext tokenContext, String YWLX, String CaoZuo) {

        CAuditHistory auditHistory = new CAuditHistory();
        auditHistory.setYwlsh(YWLSH);
        auditHistory.setBeiZhu(null);
        auditHistory.setShsj(null);
        auditHistory.setDdsj(new Date());
        auditHistory.setYwwd(tokenContext.getUserInfo().getYWWD());
        auditHistory.setYyyj(null);
        auditHistory.setCzy(tokenContext.getUserInfo().getCZY());
        auditHistory.setCzqd(tokenContext.getChannel());
        auditHistory.setQzsj(null);
        String ZhiWuMC = roleDAO.get(tokenContext.getRoleList().get(0)) == null ? null : roleDAO.get(tokenContext.getRoleList().get(0)).getRole_name();
        auditHistory.setZhiwu(ZhiWuMC);
        auditHistory.setCaoZuo(CaoZuo);
        if ("不通过撤回".equals(CaoZuo)) {/*为生成审批表加分界*/
            auditHistory.setShjg("02");
            auditHistory.setShsj(new Date());
        }
        auditHistory.setYwlx(YWLX);
        auditHistory.setCzybh(tokenContext.getUserId());
        this.auditHistoryDAO.save(auditHistory);
    }

    @Override
    public void saveNormalBusiness(String YWLSH, String YWLX, String CaoZuo) {

        CAuditHistory auditHistory = new CAuditHistory();
        auditHistory.setYwlsh(YWLSH);
        auditHistory.setBeiZhu(null);
        auditHistory.setShsj(null);
        auditHistory.setDdsj(new Date());
        auditHistory.setYwwd("1");
        if(YWLX.equals(LoanBusinessType.贷款发放.getName()) && CaoZuo.equals("新建")) {
            auditHistory.setYyyj("同意受理");
        } else {
            auditHistory.setYyyj(null);
        }
        auditHistory.setCzy("系统");
        auditHistory.setCzqd("系统自动处理");
        auditHistory.setQzsj(null);
//        String ZhiWuMC = roleDAO.get(tokenContext.getRoleList().get(0)) == null ? null : roleDAO.get(tokenContext.getRoleList().get(0)).getRole_name();
        auditHistory.setZhiwu(null);
        auditHistory.setShjg(null);
        auditHistory.setCaoZuo(CaoZuo);
        auditHistory.setYwlx(YWLX);
        auditHistory.setCzybh(null);
        this.auditHistoryDAO.save(auditHistory);
    }

    @Override
    public void saveAuditHistory(String YWLSH, ReviewInfo reviewInfo) {

        String shjg = reviewInfo.getSHJG();
        CAuditHistory auditHistory = new CAuditHistory();
        auditHistory.setYwlsh(YWLSH);
        auditHistory.setBeiZhu(reviewInfo.getBeiZhu());
        auditHistory.setShsj(reviewInfo.getSHSJ());
        auditHistory.setDdsj(reviewInfo.getDDSJ());
        auditHistory.setYwwd(reviewInfo.getYWWD());
        auditHistory.setYyyj(reviewInfo.getYYYJ());
        auditHistory.setCzy(reviewInfo.getCZY());
        auditHistory.setCzqd(reviewInfo.getCZQD());
        auditHistory.setQzsj(reviewInfo.getQZSJ());
        String ZhiWuMC = roleDAO.get(reviewInfo.getZhiWu()) == null ? null : roleDAO.get(reviewInfo.getZhiWu()).getRole_name();
        auditHistory.setZhiwu(ZhiWuMC);
        if ("01".equals(shjg))
            auditHistory.setCaoZuo(ZhiWuMC + "审核通过");
        else if ("02".equals(shjg))
            auditHistory.setCaoZuo(ZhiWuMC + "审核不通过");
        else if ("03".equals(shjg))
            auditHistory.setCaoZuo(ZhiWuMC + "发起特审");
        else
            auditHistory.setCaoZuo(reviewInfo.getCaoZuo());
        auditHistory.setShjg(shjg);
        auditHistory.setYwlx(reviewInfo.getYWLX());
        auditHistory.setCzybh(reviewInfo.getSHYBH());
/*        auditHistory.setCzybh(reviewInfo.getSHYBH());
        Calendar t1 = Calendar.getInstance();
        t1.add(Calendar.SECOND, - 5); //当前秒数-5*/
        auditHistory.setCreated_at(reviewInfo.getSHSJ());
        this.auditHistoryDAO.save(auditHistory);
    }

/*    @Override
    public List<ReviewInfo> getAuditHistoryList(String YWLSH) {
        List<CAuditHistory> auditHistoryList = DAOBuilder.instance(this.auditHistoryDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                });
        List<ReviewInfo> reviewInfoList = new ArrayList<>();

        for (CAuditHistory cAuditHistory : auditHistoryList) {
            ReviewInfo reviewInfo = new ReviewInfo();

            if (cAuditHistory.getShjg() != null || Arrays.asList("新建", "新增").contains(cAuditHistory.getCaoZuo())) {

                reviewInfo.setCaoZuo(cAuditHistory.getCaoZuo());
                reviewInfo.setSHJG(cAuditHistory.getShjg());
                reviewInfo.setYYYJ(cAuditHistory.getYyyj());
                reviewInfo.setCZY(cAuditHistory.getCzy());
                reviewInfo.setZhiWu(cAuditHistory.getZhiwu());
                reviewInfo.setYWWD(cAuditHistory.getYwwd());
                reviewInfo.setCZQD(cAuditHistory.getCzqd());
                reviewInfo.setBeiZhu(cAuditHistory.getBeiZhu());
                reviewInfo.setCZQD(cAuditHistory.getCzqd());
                reviewInfo.setSHSJ(cAuditHistory.getShsj());

                reviewInfo.setYWLSH(cAuditHistory.getYwlsh());
                reviewInfo.setQZSJ(cAuditHistory.getQzsj());
                reviewInfoList.add(reviewInfo);
            }
        }

        return reviewInfoList;
    }*/

    @Override
    public List<ReviewInfo> getAuditHistoryList(String YWLSH) {

        List<CAuditHistory> auditHistoryList = DAOBuilder.instance(this.auditHistoryDAO)
                .searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", YWLSH);
                    }
                }).orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                });

        List<ReviewInfo> reviewInfoList = new ArrayList<>();

        /*直接获取最后一条（新建）写入列表第一条*/
        ReviewInfo reviewInfoAdd = new ReviewInfo();
        for(CAuditHistory cAuditHistory : auditHistoryList) {
            if ("新建".equals(cAuditHistory.getCaoZuo()) || "修改".equals(cAuditHistory.getCaoZuo())) {
                reviewInfoAdd.setCaoZuo("新建");
                reviewInfoAdd.setSHJG(cAuditHistory.getShjg());
                reviewInfoAdd.setYYYJ(cAuditHistory.getYyyj());
                reviewInfoAdd.setCZY(cAuditHistory.getCzy());
                reviewInfoAdd.setZhiWu(cAuditHistory.getZhiwu());
                reviewInfoAdd.setYWWD(cAuditHistory.getYwwd());
                reviewInfoAdd.setCZQD(cAuditHistory.getCzqd());
                reviewInfoAdd.setBeiZhu(cAuditHistory.getBeiZhu());
                reviewInfoAdd.setCZQD(cAuditHistory.getCzqd());
                reviewInfoAdd.setSHSJ(cAuditHistory.getCreated_at());
                reviewInfoAdd.setYWLSH(cAuditHistory.getYwlsh());
                reviewInfoAdd.setQZSJ(cAuditHistory.getQzsj());
                reviewInfoList.add(0, reviewInfoAdd);
                break;
            }
        }

        if (reviewInfoList.size() == 0) {
            throw new ErrorException("业务受理(新建)记录缺失");
        }

        for (CAuditHistory cAuditHistory : auditHistoryList) {

            String shjg = cAuditHistory.getShjg();

            if (StringUtil.isEmpty(shjg))
                continue;

            if ("02".equals(shjg))
                break;

            if ("01".equals(shjg)) {
                ReviewInfo reviewInfo = new ReviewInfo();
                reviewInfo.setCaoZuo(cAuditHistory.getCaoZuo());
                reviewInfo.setSHJG(cAuditHistory.getShjg());
                reviewInfo.setYYYJ(cAuditHistory.getYyyj());
                reviewInfo.setCZY(cAuditHistory.getCzy());
                reviewInfo.setZhiWu(cAuditHistory.getZhiwu());
                reviewInfo.setYWWD(cAuditHistory.getYwwd());
                reviewInfo.setCZQD(cAuditHistory.getCzqd());
                reviewInfo.setBeiZhu(cAuditHistory.getBeiZhu());
                reviewInfo.setCZQD(cAuditHistory.getCzqd());
                reviewInfo.setSHSJ(cAuditHistory.getShsj());
                reviewInfo.setYWLSH(cAuditHistory.getYwlsh());
                reviewInfo.setQZSJ(cAuditHistory.getQzsj());
                reviewInfoList.add(1, reviewInfo);
            }
        }

        return reviewInfoList;
    }
}
