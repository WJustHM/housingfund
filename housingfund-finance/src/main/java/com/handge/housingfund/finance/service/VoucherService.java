package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IVoucherService;
import com.handge.housingfund.common.service.finance.model.SubjectModel;
import com.handge.housingfund.common.service.finance.model.VoucherModel;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.ICFinanceBusinessVoucherSetsDAO;
import com.handge.housingfund.database.dao.ICFinanceDailyBusinessSetsDAO;
import com.handge.housingfund.database.dao.IStFinanceSubjectsDAO;
import com.handge.housingfund.database.entities.CFinanceBusinessVoucherSets;
import com.handge.housingfund.database.entities.CFinanceDailyBusinessSets;
import com.handge.housingfund.database.entities.StFinanceSubjects;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.SubjectHelper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */
@Component
public class VoucherService implements IVoucherService {

    @Autowired
    ICFinanceBusinessVoucherSetsDAO icFinanceBusinessVoucherSetsDAO;
    @Autowired
    IStFinanceSubjectsDAO iStFinanceSubjectsDAO;
    @Autowired
    ICFinanceDailyBusinessSetsDAO icFinanceDailyBusinessSetsDAO;
    @Autowired
    IUpdateSFSYService iUpdateSFSYService;

    @Override
    public PageRes<VoucherModel> getVoucherList(String mbbh, String ywmc, String km, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(mbbh)) filter.put("mbbh", mbbh);
        if (StringUtil.notEmpty(ywmc)) filter.put("ywmc", ywmc);

        PageRes pageRes = new PageRes();

        List<CFinanceBusinessVoucherSets> voucherSetsList = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).
                pageOption(pageRes, pageSize, pageNo).orderOption("created_at", Order.ASC).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(km))
                    criteria.add(Restrictions.or(Restrictions.like("jfkm", "%" + km + "%"), Restrictions.like("dfkm", "%" + km + "%")));
            }
        }).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        PageRes<VoucherModel> voucherModelPageRes = new PageRes<>();
        voucherModelPageRes.setCurrentPage(pageRes.getCurrentPage());
        voucherModelPageRes.setNextPageNo(pageRes.getNextPageNo());
        voucherModelPageRes.setPageCount(pageRes.getPageCount());
        voucherModelPageRes.setPageSize(pageRes.getPageSize());
        voucherModelPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<VoucherModel> voucherModels = new ArrayList<>();

        for (CFinanceBusinessVoucherSets voucherSets : voucherSetsList) {
            VoucherModel voucherModel = new VoucherModel();
            voucherModel.setId(voucherSets.getId());
            voucherModel.setBEIZHU(voucherSets.getBeizhu());
            voucherModel.setCJR(voucherSets.getCjr());
            voucherModel.setCJSJ(DateUtil.date2Str(voucherSets.getCjsj(), "yyyy-MM-dd HH:mm"));
            voucherModel.setDFKM(voucherSets.getDfkm());
            voucherModel.setJFKM(voucherSets.getJfkm());
            voucherModel.setMBBH(voucherSets.getMbbh());
            voucherModel.setYWMC(voucherSets.getYwmc());
            voucherModel.setYWMCID(voucherSets.getYwmcid());
            voucherModel.setSFYSY(voucherSets.getSfysy());

            voucherModels.add(voucherModel);
        }

        voucherModelPageRes.setResults(voucherModels);
        return voucherModelPageRes;
    }

    @Override
    public VoucherModel getVoucher(String id) {

        CFinanceBusinessVoucherSets voucherSets = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (voucherSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务(" + id + ")的相关信息");
        }
        VoucherModel voucherModel = new VoucherModel();
        voucherModel.setId(voucherSets.getId());
        voucherModel.setBEIZHU(voucherSets.getBeizhu());
        voucherModel.setCJR(voucherSets.getCjr());
        voucherModel.setCJSJ(DateUtil.date2Str(voucherSets.getCjsj(), "yyyy-MM-dd HH:mm"));
        voucherModel.setDFKM(voucherSets.getDfkm());
        voucherModel.setJFKM(voucherSets.getJfkm());
        voucherModel.setMBBH(voucherSets.getMbbh());
        voucherModel.setYWMC(voucherSets.getYwmc());
        voucherModel.setYWMCID(voucherSets.getYwmcid());
        voucherModel.setSFYSY(voucherSets.getSfysy());

        return voucherModel;
    }

    @Override
    public VoucherModel getVoucherByYWID(String ywid) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("ywmcid", ywid);

        CFinanceBusinessVoucherSets voucherSets = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });
        VoucherModel voucherModel = new VoucherModel();
        if (voucherSets != null) {
            voucherModel.setId(voucherSets.getId());
            voucherModel.setBEIZHU(voucherSets.getBeizhu());
            voucherModel.setCJR(voucherSets.getCjr());
            voucherModel.setCJSJ(DateUtil.date2Str(voucherSets.getCjsj(), "yyyy-MM-dd HH:mm"));
            voucherModel.setDFKM(voucherSets.getDfkm());
            voucherModel.setJFKM(voucherSets.getJfkm());
            voucherModel.setMBBH(voucherSets.getMbbh());
            voucherModel.setYWMC(voucherSets.getYwmc());
            voucherModel.setYWMCID(voucherSets.getYwmcid());
            voucherModel.setSFYSY(voucherSets.getSfysy());
        } else {
            voucherModel.setId("");
            voucherModel.setBEIZHU("");
            voucherModel.setCJR("");
            voucherModel.setCJSJ("");
            voucherModel.setDFKM("[]");
            voucherModel.setJFKM("[]");
            voucherModel.setMBBH("");
            voucherModel.setYWMC("");
            voucherModel.setYWMCID("");
            voucherModel.setSFYSY(false);
        }
        return voucherModel;

    }

    @Override
    public VoucherModel addVoucher(VoucherModel voucherModel) {
        CFinanceBusinessVoucherSets voucherSets = new CFinanceBusinessVoucherSets();
        voucherSets.setBeizhu(voucherModel.getBEIZHU());
        voucherSets.setCjr(voucherModel.getCJR());
        voucherSets.setCjsj(new Date());
        voucherSets.setDfkm(voucherModel.getDFKM());
        voucherSets.setJfkm(voucherModel.getJFKM());
        voucherSets.setMbbh(voucherModel.getMBBH());
        voucherSets.setYwmc(voucherModel.getYWMC());
        voucherSets.setYwmcid(voucherModel.getYWMCID());
        voucherSets.setSfysy(false);

        List<SubjectModel> jfkmList = SubjectHelper.getSubjectByJson(voucherModel.getJFKM());
        List<SubjectModel> dfkmList = SubjectHelper.getSubjectByJson(voucherModel.getDFKM());

        if (jfkmList == null) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "借方科目格式错误");
        if (dfkmList == null) throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "贷方科目格式错误");

        for (SubjectModel jfkm : jfkmList)
            iUpdateSFSYService.updateSFSY(new StFinanceSubjects(), iStFinanceSubjectsDAO, jfkm.getId());

        for (SubjectModel dfkm : dfkmList) {
            iUpdateSFSYService.updateSFSY(new StFinanceSubjects(), iStFinanceSubjectsDAO, dfkm.getId());
        }
        iUpdateSFSYService.updateSFSY(new CFinanceDailyBusinessSets(), icFinanceDailyBusinessSetsDAO, voucherModel.getYWMCID());

        CFinanceBusinessVoucherSets newBusinessVoucherSet = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).entity(voucherSets).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "写入数据库失败，请联系管理员");
            }
        });

        voucherModel.setId(newBusinessVoucherSet.getId());
        voucherModel.setMBBH(newBusinessVoucherSet.getMbbh());
        voucherModel.setCJSJ(DateUtil.date2Str(newBusinessVoucherSet.getCjsj(), "yyyy-MM-dd HH:mm"));
        voucherModel.setSFYSY(newBusinessVoucherSet.getSfysy());

        return voucherModel;
    }

    @Override
    public boolean updateVoucher(String id, VoucherModel voucherModel) {
        CFinanceBusinessVoucherSets voucherSets = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (voucherSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务(" + id + ")的相关信息");
        }

        if (voucherSets.getSfysy()) {
            if (!voucherModel.getDFKM().equals(voucherSets.getDfkm()) ||
                    !voucherSets.getJfkm().equals(voucherModel.getJFKM()) ||
                    !voucherSets.getMbbh().equals(voucherModel.getMBBH()) ||
                    !voucherSets.getYwmc().equals(voucherModel.getYWMC())) {
                return true;
            } else {
                voucherSets.setBeizhu(voucherModel.getBEIZHU());
                DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).entity(voucherSets).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败，请联系管理员");
                    }
                });
                return false;
            }
        }

        voucherSets.setBeizhu(voucherModel.getBEIZHU());
        voucherSets.setCjr(voucherModel.getCJR());
        voucherSets.setDfkm(voucherModel.getDFKM());
        voucherSets.setJfkm(voucherModel.getJFKM());
        voucherSets.setYwmc(voucherModel.getYWMC());
        voucherSets.setYwmcid(voucherModel.getYWMCID());

        DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).entity(voucherSets).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败，请联系管理员");
            }
        });

        return false;
    }

    @Override
    public boolean deleteVoucher(String id) {
        CFinanceBusinessVoucherSets voucherSets = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (voucherSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务(" + id + ")的相关信息");
        }
        if (voucherSets.getSfysy()) return true;
        CFinanceDailyBusinessSets sets = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).UID(voucherSets.getYwmcid()).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        try {
            icFinanceBusinessVoucherSetsDAO.deleteForever(voucherSets);
            if (sets != null) {
                sets.setSfsy(false);
                sets.setSfmr(false);
                icFinanceDailyBusinessSetsDAO.update(sets);
            }
        } catch (Exception e) {
            throw new ErrorException(e);
        }
        return false;
    }

    @Override
    public boolean delVouchers(ArrayList<String> delList) {
        for (String id : delList) {
            if (deleteVoucher(id)) return true;
        }
        return false;
    }
}
