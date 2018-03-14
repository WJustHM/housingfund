package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.finance.IBusinessClassifySetService;
import com.handge.housingfund.common.service.finance.model.BusinessClassifySet;
import com.handge.housingfund.common.service.finance.model.BusinessSet;
import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICFinanceBusinessVoucherSetsDAO;
import com.handge.housingfund.database.dao.ICFinanceDailyBusinessClassfiySetsDAO;
import com.handge.housingfund.database.dao.ICFinanceDailyBusinessSetsDAO;
import com.handge.housingfund.database.dao.ICFundBusinessTypeDAO;
import com.handge.housingfund.database.entities.CFinanceDailyBusinessClassfiySets;
import com.handge.housingfund.database.entities.CFinanceDailyBusinessSets;
import com.handge.housingfund.database.entities.CFundBusinessType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */
@Component
public class BusinessClassifySetService implements IBusinessClassifySetService {

    @Autowired
    private ICFinanceDailyBusinessClassfiySetsDAO icFinanceDailyBusinessClassfiySetsDAO;
    @Autowired
    private ICFinanceDailyBusinessSetsDAO icFinanceDailyBusinessSetsDAO;
    @Autowired
    private ICFundBusinessTypeDAO icFundBusinessTypeDAO;
    @Autowired
    private IStateMachineService iStateMachineService;

    //宋姐专用
    @Autowired
    ICFinanceBusinessVoucherSetsDAO icFinanceBusinessVoucherSetsDAO;

    @Override
    public ArrayList<BusinessClassifySet> getBusinessClassifys() {


        List<CFinanceDailyBusinessClassfiySets> classifySets = DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO)
                .searchFilter(new HashMap<String, Object>() {{
                    this.put("sfrc", true);
                }}).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });

        ArrayList<BusinessClassifySet> businessClassifySets = new ArrayList<>();

        for (CFinanceDailyBusinessClassfiySets classify : classifySets) {
            BusinessClassifySet classifySet = new BusinessClassifySet();
            classifySet.setId(classify.getId());
            classifySet.setCJR(classify.getCjr());
            classifySet.setCJSJ(DateUtil.date2Str(classify.getCjsj(), "yyyy-MM-dd HH:mm"));
            classifySet.setSFRC(classify.getSfrc());
            classifySet.setYWMC(classify.getYwmc());

            List<CFinanceDailyBusinessSets> cFinanceDailyBusinessSets = classify.getcFinanceDailyBusinessSetsList();
            ArrayList<BusinessSet> businessSets = new ArrayList<>();
            for (CFinanceDailyBusinessSets dailyBusinessSets : cFinanceDailyBusinessSets) {
                if (dailyBusinessSets.getSfzd()) {
                    continue;
                }
                BusinessSet business = new BusinessSet();
                business.setId(dailyBusinessSets.getId());
                business.setCJR(dailyBusinessSets.getCjr());
                business.setCJSJ(DateUtil.date2Str(dailyBusinessSets.getCjsj(), "yyyy-MM-dd HH:mm"));
                business.setYWMC(dailyBusinessSets.getYwmc());
                business.setSFMR(dailyBusinessSets.getSfmr());
                business.setSFYSY(dailyBusinessSets.getSfsy());

                CFundBusinessType cFundBusinessType = DAOBuilder.instance(icFundBusinessTypeDAO).searchFilter(new HashMap<String, Object>() {{
                    this.put("ywmcid", dailyBusinessSets.getId());
                }}).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

                if (cFundBusinessType != null) {
                    business.setZJYWLXBM(cFundBusinessType.getZjywlxbm());
                    business.setZJYWLX(cFundBusinessType.getZjywlx());
                }

                businessSets.add(business);
            }
            classifySet.setRCYW(businessSets);

            businessClassifySets.add(classifySet);
        }

        return businessClassifySets;
    }

    @Override
    public boolean updateBusinessClassify(ArrayList<BusinessClassifySet> businessClassifySets) {

        for (BusinessClassifySet classify : businessClassifySets) {
            if (classify.getId() != null) {
                CFinanceDailyBusinessClassfiySets dailyBusinessClassfiySet = DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO).UID(classify.getId()).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });

                if (dailyBusinessClassfiySet == null) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该分类(" + classify.getId() + ")的相关信息");
                }
                if (dailyBusinessClassfiySet.getSfmr() || dailyBusinessClassfiySet.getcFinanceDailyBusinessSetsList().size() > 0)
                    return true;

                if ("delete".equals(classify.getAction())) {
                    DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO).entity(dailyBusinessClassfiySet).deleteForever(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除业务分类失败，请联系管理员");
                        }
                    });
                } else {
                    dailyBusinessClassfiySet.setYwmc(classify.getYWMC());
                    dailyBusinessClassfiySet.setCjr(classify.getCJR());

                    DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO).entity(dailyBusinessClassfiySet).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "修改业务分类失败，请联系管理员");
                        }
                    });
                }
            } else {
                CFinanceDailyBusinessClassfiySets newdailyBusinessClassfiySet = new CFinanceDailyBusinessClassfiySets();
                newdailyBusinessClassfiySet.setYwmc(classify.getYWMC());
                newdailyBusinessClassfiySet.setCjr(classify.getCJR());
                newdailyBusinessClassfiySet.setCjsj(new Date());
                newdailyBusinessClassfiySet.setSfmr(false);
                newdailyBusinessClassfiySet.setSfrc(true);

                DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO).entity(newdailyBusinessClassfiySet).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "新增业务分类失败，请联系管理员");
                    }
                });
            }
        }

        return false;
    }

    @Override
    public boolean updateBusiness(BusinessClassifySet businessClassifySet) {

        String id = businessClassifySet.getId();
        CFinanceDailyBusinessClassfiySets dailyBusinessClassfiySet = DAOBuilder.instance(icFinanceDailyBusinessClassfiySetsDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (dailyBusinessClassfiySet == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该分类(" + id + ")的相关信息");
        }

        ArrayList<BusinessSet> businessSetList = businessClassifySet.getRCYW();
        for (BusinessSet businessSet : businessSetList) {
            String businessId = businessSet.getId();
            if (businessId != null) {
                CFinanceDailyBusinessSets cFinanceDailyBusinessSet = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).UID(businessId).getObject(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });

                if (cFinanceDailyBusinessSet == null) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该分类(" + businessId + ")的相关信息");
                }

                if (cFinanceDailyBusinessSet.getSfmr() || cFinanceDailyBusinessSet.getSfsy()) return true;

                if ("delete".equals(businessSet.getAction())) {
                    dailyBusinessClassfiySet.getcFinanceDailyBusinessSetsList().remove(cFinanceDailyBusinessSet);
                    cFinanceDailyBusinessSet.setcFinanceDailyBusinessClassfiySets(null);

                    DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).entity(cFinanceDailyBusinessSet).deleteForever(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除业务失败，请联系管理员");
                        }
                    });

                    //删除业务流程
                    iStateMachineService.deleteConfig(BusinessType.Finance, "日常" + cFinanceDailyBusinessSet.getYwmc());
                } else {
//                    //宋姐专用,正式不需要-start
//                    CFinanceBusinessVoucherSets voucherSets = DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).searchFilter(new HashMap<String, Object>(){{
//                        this.put("ywmc", cFinanceDailyBusinessSet.getYwmc());
//                    }}).getObject(new DAOBuilder.ErrorHandler() {
//                        @Override
//                        public void error(Exception e) {
//                            throw new ErrorException(e);
//                        }
//                    });
//
//                    voucherSets.setYwmc(businessSet.getYWMC());
//                    DAOBuilder.instance(icFinanceBusinessVoucherSetsDAO).entity(voucherSets).save(new DAOBuilder.ErrorHandler() {
//                        @Override
//                        public void error(Exception e) {
//                            throw new ErrorException(e);
//                        }
//                    });
//                    //宋姐专用,正式不需要-end

                    cFinanceDailyBusinessSet.setYwmc(businessSet.getYWMC());
                    cFinanceDailyBusinessSet.setCjr(businessSet.getCJR());
                    cFinanceDailyBusinessSet.setSfjspt(false);

                    DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).entity(cFinanceDailyBusinessSet).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "修改业务失败，请联系管理员");
                        }
                    });
                }
            } else {
                CFinanceDailyBusinessSets newcFinanceDailyBusinessSet = new CFinanceDailyBusinessSets();
                newcFinanceDailyBusinessSet.setYwmc(businessSet.getYWMC());
                newcFinanceDailyBusinessSet.setCjsj(new Date());
                newcFinanceDailyBusinessSet.setCjr(businessSet.getCJR());
                newcFinanceDailyBusinessSet.setSfsy(false);
                newcFinanceDailyBusinessSet.setSfmr(false);
                newcFinanceDailyBusinessSet.setSfzd(false);
                newcFinanceDailyBusinessSet.setSfjspt(false);
                newcFinanceDailyBusinessSet.setcFinanceDailyBusinessClassfiySets(dailyBusinessClassfiySet);

                businessId = DAOBuilder.instance(icFinanceDailyBusinessSetsDAO).entity(newcFinanceDailyBusinessSet).saveThenFetchId(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "新增业务失败，请联系管理员");
                    }
                });

                //新建业务流程
                iStateMachineService.addNewConfig(businessSet.getYWMC());
            }

            addFundBusinessType(businessSet.getZJYWLXBM(), businessSet.getZJYWLX(), businessId);
        }

        return false;
    }

    private void addFundBusinessType(String zjywlxbm, String zjywlx, String ywmcid) {
        CFundBusinessType cFundBusinessType = DAOBuilder.instance(icFundBusinessTypeDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywmcid", ywmcid);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFundBusinessType == null) {
            cFundBusinessType = new CFundBusinessType();

            cFundBusinessType.setZjywlxbm(zjywlxbm);
            cFundBusinessType.setZjywlx(zjywlx);
            cFundBusinessType.setYwmcid(ywmcid);

            DAOBuilder.instance(icFundBusinessTypeDAO).entity(cFundBusinessType).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
        if (!zjywlxbm.equals(cFundBusinessType.getZjywlxbm())) {
            cFundBusinessType.setZjywlxbm(zjywlxbm);
            cFundBusinessType.setZjywlx(zjywlx);

            DAOBuilder.instance(icFundBusinessTypeDAO).entity(cFundBusinessType).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
    }

    @Override
    public HashMap<String, String> getFundBusinessType(String ywmcid) {
        HashMap<String, String> fundBusinessType = new HashMap<>();

        CFundBusinessType cFundBusinessType = DAOBuilder.instance(icFundBusinessTypeDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywmcid", ywmcid);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        fundBusinessType.put("zjywlxbm", cFundBusinessType != null ? cFundBusinessType.getZjywlxbm() : "99");
        fundBusinessType.put("zjywlx", cFundBusinessType != null ? cFundBusinessType.getZjywlx() : "其他");

        return fundBusinessType;
    }
}
