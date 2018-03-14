package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.ISubjectService;
import com.handge.housingfund.common.service.finance.model.SubjectBaseModel;
import com.handge.housingfund.common.service.finance.model.SubjectModel;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.IBaseDAO;
import com.handge.housingfund.database.dao.IStFinanceSubjectsDAO;
import com.handge.housingfund.database.dao.IStSettlementSpecialBankAccountDAO;
import com.handge.housingfund.database.entities.CFinanceSubjectsExtension;
import com.handge.housingfund.database.entities.StFinanceSubjects;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.FinanceComputeHelper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
@SuppressWarnings("Duplicates")
@Component
public class SubjectService implements ISubjectService {
    @Autowired
    IStFinanceSubjectsDAO iStFinanceSubjectsDAO;
    @Autowired
    IStSettlementSpecialBankAccountDAO specialBankAccountDAO;

    @Override
    public PageRes<SubjectModel> searchSubjects(String kmbh, String kmmc, String kmsx, int pageNo, int pageSize) {

        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(kmbh)) filter.put("kmbh", kmbh);
        if (StringUtil.notEmpty(kmmc)) filter.put("kmmc", kmmc);
        if (StringUtil.notEmpty(kmsx)) filter.put("kmsx", kmsx);

        PageRes pageRes = new PageRes();

        List<StFinanceSubjects> subjectsPageResults = DAOBuilder.instance(iStFinanceSubjectsDAO).searchFilter(filter).searchOption(SearchOption.FUZZY)
                .pageOption(pageRes, pageSize, pageNo)
                .orderOption("kmbh", Order.ASC)
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });

        PageRes<SubjectModel> subjectPageRes = new PageRes<>();
        subjectPageRes.setCurrentPage(pageRes.getCurrentPage());
        subjectPageRes.setNextPageNo(pageRes.getNextPageNo());
        subjectPageRes.setPageCount(pageRes.getPageCount());
        subjectPageRes.setPageSize(pageRes.getPageSize());
        subjectPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<SubjectModel> subjectList = new ArrayList<>();
        for (StFinanceSubjects stFinanceSubjects : subjectsPageResults) {
            SubjectModel subjectModel = new SubjectModel();
            subjectModel.setId(stFinanceSubjects.getId());
            subjectModel.setBEIZHU(stFinanceSubjects.getcFinanceSubjectsExtension().getBeizhu());
            subjectModel.setCZKZ(stFinanceSubjects.getcFinanceSubjectsExtension().getCzkz());
            subjectModel.setKMBH(stFinanceSubjects.getKmbh());
            subjectModel.setKMJB(stFinanceSubjects.getKmjb().intValue());
            subjectModel.setKMJM(stFinanceSubjects.getcFinanceSubjectsExtension().getKmjm());
            subjectModel.setKMMC(stFinanceSubjects.getKmmc());
            subjectModel.setKMSX(stFinanceSubjects.getKmsx());
            subjectModel.setKMXZ(stFinanceSubjects.getcFinanceSubjectsExtension().getKmxz());
            subjectModel.setKMYEFX(stFinanceSubjects.getKmyefx());
            subjectModel.setSJKM(stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm());
            subjectModel.setTYPE(stFinanceSubjects.getcFinanceSubjectsExtension().getType());
            subjectModel.setSFYSY(stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy());
            subjectModel.setSFMR(stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr());

            subjectList.add(subjectModel);
        }

        if (subjectList.size() != 0) {
            subjectPageRes.setResults(subjectList);
        }
        return subjectPageRes;
    }

    @Override
    public List<SubjectBaseModel> getSubjectList() {
        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        List<StFinanceSubjects> subject1 = FinanceComputeHelper.filterSubjects(stFinanceSubjects, 1);
        List<SubjectBaseModel> res = new ArrayList<>();
        for (StFinanceSubjects s1 : subject1) {
            SubjectBaseModel m1 = new SubjectBaseModel();
            m1.setId(s1.getId());
            m1.setKMBH(s1.getKmbh());
            m1.setKMMC(s1.getKmmc());
            m1.setSJKM(s1.getcFinanceSubjectsExtension().getSjkm());
            List<StFinanceSubjects> subject2 = FinanceComputeHelper.filterSubjects(stFinanceSubjects, s1.getKmbh(), 2);
            ArrayList<SubjectBaseModel> res1 = new ArrayList<>();
            for (StFinanceSubjects s2 : subject2) {
                SubjectBaseModel m2 = new SubjectBaseModel();
                m2.setId(s2.getId());
                m2.setKMBH(s2.getKmbh());
                m2.setKMMC(s2.getKmmc());
                m2.setSJKM(s2.getcFinanceSubjectsExtension().getSjkm());
                List<StFinanceSubjects> subject3 = FinanceComputeHelper.filterSubjects(stFinanceSubjects, s2.getKmbh(), 3);
                ArrayList<SubjectBaseModel> res2 = new ArrayList<>();
                for (StFinanceSubjects s3 : subject3) {
                    SubjectBaseModel m3 = new SubjectBaseModel();
                    m3.setId(s3.getId());
                    m3.setKMBH(s3.getKmbh());
                    m3.setKMMC(s3.getKmmc());
                    m3.setSJKM(s3.getcFinanceSubjectsExtension().getSjkm());
                    m3.setSubjectBaseModels(null);
                    res2.add(m3);
                }
                if (res2.size() != 0) m2.setSubjectBaseModels(res2);

                res1.add(m2);
            }
            if (res1.size() != 0) m1.setSubjectBaseModels(res1);
            res.add(m1);
        }
        return res;
    }

    @Override
    public SubjectModel createSubject(SubjectModel subjectModel) {
        if (isExist(subjectModel.getKMBH())) {
            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "科目编码");
        }
        if (isExist(subjectModel.getKMMC())) {
            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "科目名称");
        }

        HashMap<String, Object> filter = new HashMap<>();
        filter.put("kmbh", subjectModel.getSJKM());
        StFinanceSubjects SJKM = DAOBuilder.instance(iStFinanceSubjectsDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (SJKM == null) throw new ErrorException(ReturnEnumeration.Data_MISS, "上级科目");
        if (!SJKM.getKmsx().equals(subjectModel.getKMSX())
                || !SJKM.getKmyefx().equals(subjectModel.getKMYEFX())
                || !SJKM.getcFinanceSubjectsExtension().getKmxz().equals(subjectModel.getKMXZ())
                || !SJKM.getcFinanceSubjectsExtension().getType().equals(subjectModel.getTYPE()))
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "科目属性、科目性质或科目余额方向与上级科目不符");

        StFinanceSubjects stFinanceSubjects = new StFinanceSubjects();
        stFinanceSubjects.setKmbh(subjectModel.getKMBH());
        String kmbh = subjectModel.getKMBH();
        int length = kmbh.length();
        int kmjb = (length - 3) / 2 + 1;
        stFinanceSubjects.setKmjb(new BigDecimal(kmjb));
        stFinanceSubjects.setKmmc(subjectModel.getKMMC());
        stFinanceSubjects.setKmsx(subjectModel.getKMSX());
        stFinanceSubjects.setKmyefx(subjectModel.getKMYEFX());

        CFinanceSubjectsExtension cFinanceSubjectsExtension = new CFinanceSubjectsExtension();
        cFinanceSubjectsExtension.setBeizhu(subjectModel.getBEIZHU());
        cFinanceSubjectsExtension.setCzkz(subjectModel.getCZKZ());
        cFinanceSubjectsExtension.setKmjm(subjectModel.getKMJM());
        cFinanceSubjectsExtension.setKmxz(subjectModel.getKMXZ());
        cFinanceSubjectsExtension.setSjkm(subjectModel.getSJKM());
        cFinanceSubjectsExtension.setType(subjectModel.getTYPE());
        cFinanceSubjectsExtension.setSfysy(false);
        cFinanceSubjectsExtension.setSfmr(false);

        stFinanceSubjects.setcFinanceSubjectsExtension(cFinanceSubjectsExtension);

        String id = DAOBuilder.instance(iStFinanceSubjectsDAO).entity(stFinanceSubjects).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "插入数据库失败，请联系管理员");
            }
        });

        subjectModel.setId(id);
        subjectModel.setSFYSY(stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy());
        subjectModel.setSFMR(stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr());

        SJKM.getcFinanceSubjectsExtension().setSfysy(true);

        DAOBuilder.instance(iStFinanceSubjectsDAO).entity(SJKM).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return subjectModel;
    }

    @Override
    public SubjectModel getSubject(String id) {
        StFinanceSubjects stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (stFinanceSubjects == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该科目(" + id + ")的相关信息");
        }
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setId(stFinanceSubjects.getId());
        subjectModel.setBEIZHU(stFinanceSubjects.getcFinanceSubjectsExtension().getBeizhu());
        subjectModel.setCZKZ(stFinanceSubjects.getcFinanceSubjectsExtension().getCzkz());
        subjectModel.setKMBH(stFinanceSubjects.getKmbh());
        subjectModel.setKMJB(stFinanceSubjects.getKmjb().intValue());
        subjectModel.setKMJM(stFinanceSubjects.getcFinanceSubjectsExtension().getKmjm());
        subjectModel.setKMMC(stFinanceSubjects.getKmmc());
        subjectModel.setKMSX(stFinanceSubjects.getKmsx());
        subjectModel.setKMXZ(stFinanceSubjects.getcFinanceSubjectsExtension().getKmxz());
        subjectModel.setKMYEFX(stFinanceSubjects.getKmyefx());
        String sjkm = stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm();
        StringBuffer sjkM = new StringBuffer();
        while (true) {
            SubjectModel parentSubject = getSubjectByKMBH(sjkm);
            if (parentSubject == null) break;
            if (sjkM.length() == 0) sjkM.insert(0, parentSubject.getKMMC());
            else sjkM.insert(0, parentSubject.getKMMC() + "-");

            if (!StringUtil.notEmpty(parentSubject.getSJKM())) break;
            sjkm = parentSubject.getSJKM();
        }
        subjectModel.setSJKM(sjkm + "-" + sjkM.toString());
        subjectModel.setTYPE(stFinanceSubjects.getcFinanceSubjectsExtension().getType());
        subjectModel.setSFYSY(stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy());
        subjectModel.setSFMR(stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr());

        return subjectModel;
    }

    @Override
    public boolean updateSubject(String id, SubjectModel subjectModel) {
        StFinanceSubjects stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (stFinanceSubjects == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该科目(" + id + ")的相关信息");
        }
        if (stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy()) return true;

        stFinanceSubjects.setKmbh(subjectModel.getKMBH());
        String kmbh = subjectModel.getKMBH();
        int length = kmbh.length();
        int kmjb = (length - 3) / 2 + 1;
        stFinanceSubjects.setKmjb(new BigDecimal(kmjb));
        stFinanceSubjects.setKmyefx(subjectModel.getKMYEFX());
        stFinanceSubjects.setKmsx(subjectModel.getKMSX());
        stFinanceSubjects.setKmmc(subjectModel.getKMMC());
        stFinanceSubjects.getcFinanceSubjectsExtension().setType(subjectModel.getTYPE());
        stFinanceSubjects.getcFinanceSubjectsExtension().setSjkm(subjectModel.getSJKM());
        stFinanceSubjects.getcFinanceSubjectsExtension().setKmxz(subjectModel.getKMXZ());
        stFinanceSubjects.getcFinanceSubjectsExtension().setKmjm(subjectModel.getKMJM());
        stFinanceSubjects.getcFinanceSubjectsExtension().setCzkz(subjectModel.getCZKZ());
        stFinanceSubjects.getcFinanceSubjectsExtension().setBeizhu(subjectModel.getBEIZHU());

        DAOBuilder.instance(iStFinanceSubjectsDAO).entity(stFinanceSubjects).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败，请联系管理员");
            }
        });
        return false;
    }

    @Override
    public boolean deleteSubject(String id) {
        HashMap<String, Object> filter = new HashMap<>();

        StFinanceSubjects stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (stFinanceSubjects == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该科目(" + id + ")的相关信息");
        }
        if (stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy() || stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr())
            return true;

        filter.put("cFinanceSubjectsExtension.sjkm", stFinanceSubjects.getKmbh());
        List<StFinanceSubjects> stFinanceSubjectsList = DAOBuilder.instance(iStFinanceSubjectsDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (stFinanceSubjectsList.size() > 0) return true;

        DAOBuilder.instance(iStFinanceSubjectsDAO).entity(stFinanceSubjects).deleteForever(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除科目失败，请联系管理员");
            }
        });
        return false;
    }

    @Override
    public boolean delSubjects(ArrayList<String> delList) {
        for (String id : delList) {
            if (deleteSubject(id)) return true;
        }
        return false;
    }

    @Override
    public ArrayList<SubjectModel> getSubjects(String param) {

        List<StFinanceSubjects> stFinanceSubjectsList = DAOBuilder.instance(iStFinanceSubjectsDAO).searchOption(SearchOption.FUZZY).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                if (StringUtil.notEmpty(param) && !"all".equals(param))
                    criteria.add(Restrictions.or(Restrictions.like("kmbh", "%" + param + "%"), Restrictions.like("kmmc", "%" + param + "%")));
            }
        }).orderOption("kmbh", Order.ASC).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        ArrayList<SubjectModel> subjectModels = new ArrayList<>();

        for (StFinanceSubjects stFinanceSubjects : stFinanceSubjectsList) {
            SubjectModel subjectModel = new SubjectModel();
            subjectModel.setKMBH(stFinanceSubjects.getKmbh());
            subjectModel.setKMJB(stFinanceSubjects.getKmjb().intValue());
            StringBuffer kmmC = new StringBuffer(stFinanceSubjects.getKmmc());
            if (!StringUtil.notEmpty(stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm())) {
                subjectModel.setKMMC(kmmC.toString());
            } else {
                String sjkm = stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm();
                while (true) {
                    SubjectModel parentSubject = getSubjectByKMBH(sjkm);
                    if (parentSubject == null) break;
                    kmmC.insert(0, parentSubject.getKMMC() + "-");

                    if (!StringUtil.notEmpty(parentSubject.getSJKM())) break;
                    sjkm = parentSubject.getSJKM();
                }
                subjectModel.setKMMC(kmmC.toString());
            }
            subjectModel.setId(stFinanceSubjects.getId());
            subjectModel.setBEIZHU(stFinanceSubjects.getcFinanceSubjectsExtension().getBeizhu());
            subjectModel.setCZKZ(stFinanceSubjects.getcFinanceSubjectsExtension().getCzkz());
            subjectModel.setKMJM(stFinanceSubjects.getcFinanceSubjectsExtension().getKmjm());
            subjectModel.setKMSX(stFinanceSubjects.getKmsx());
            subjectModel.setKMXZ(stFinanceSubjects.getcFinanceSubjectsExtension().getKmxz());
            subjectModel.setKMYEFX(stFinanceSubjects.getKmyefx());
            subjectModel.setSJKM(stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm());
            subjectModel.setTYPE(stFinanceSubjects.getcFinanceSubjectsExtension().getType());
            subjectModel.setSFYSY(stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy());
            subjectModel.setSFMR(stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr());

            subjectModels.add(subjectModel);
        }
        return subjectModels;
    }

    private SubjectModel getSubjectByKMBH(String kmbh) {
        StFinanceSubjects stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("kmbh", kmbh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (stFinanceSubjects == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "会计科目");
        }
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setId(stFinanceSubjects.getId());
        subjectModel.setBEIZHU(stFinanceSubjects.getcFinanceSubjectsExtension().getBeizhu());
        subjectModel.setCZKZ(stFinanceSubjects.getcFinanceSubjectsExtension().getCzkz());
        subjectModel.setKMBH(stFinanceSubjects.getKmbh());
        subjectModel.setKMJB(stFinanceSubjects.getKmjb().intValue());
        subjectModel.setKMJM(stFinanceSubjects.getcFinanceSubjectsExtension().getKmjm());
        subjectModel.setKMMC(stFinanceSubjects.getKmmc());
        subjectModel.setKMSX(stFinanceSubjects.getKmsx());
        subjectModel.setKMXZ(stFinanceSubjects.getcFinanceSubjectsExtension().getKmxz());
        subjectModel.setKMYEFX(stFinanceSubjects.getKmyefx());
        subjectModel.setSJKM(stFinanceSubjects.getcFinanceSubjectsExtension().getSjkm());
        subjectModel.setTYPE(stFinanceSubjects.getcFinanceSubjectsExtension().getType());
        subjectModel.setSFYSY(stFinanceSubjects.getcFinanceSubjectsExtension().getSfysy());
        subjectModel.setSFMR(stFinanceSubjects.getcFinanceSubjectsExtension().getSfmr());

        return subjectModel;
    }

    @Override
    public boolean isExist(String param) {
        List<StFinanceSubjects> stFinanceSubjectsList = DAOBuilder.instance(iStFinanceSubjectsDAO)
                .extension(new IBaseDAO.CriteriaExtension() {
                    @Override
                    public void extend(Criteria criteria) {
                        if (StringUtil.notEmpty(param))
                            criteria.add(Restrictions.or(Restrictions.eq("kmbh", param), Restrictions.eq("kmmc", param)));
                    }
                })
                .getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });
        return stFinanceSubjectsList.size() > 0;
    }

    @Override
    public SubjectModel getSubjectByBid(String bidno) {
        StSettlementSpecialBankAccount acb = DAOBuilder.instance(specialBankAccountDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("yhzhhm", bidno);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (acb == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "银行专户");
        }
        String kmbh = acb.getKmbh();
        return getSubjectByKMBH(kmbh);
    }
}
