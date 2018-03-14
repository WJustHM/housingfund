package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.collection.model.BusCommonRetrun;
import com.handge.housingfund.common.service.collection.model.individual.DeleteOperationRes;
import com.handge.housingfund.common.service.collection.model.individual.RevokeOperationRes;
import com.handge.housingfund.common.service.collection.service.common.CommonOps;
import com.handge.housingfund.common.service.collection.service.unitdeposit.UnitRemittance;
import com.handge.housingfund.common.service.enumeration.BusinessDetailsModule;
import com.handge.housingfund.common.service.loan.model.GetCommonHistory;
import com.handge.housingfund.common.service.review.model.DetailsReviewInfo;
import com.handge.housingfund.common.service.review.model.MultiReviewConfig;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CAuditHistory;
import com.handge.housingfund.database.entities.Common;
import com.handge.housingfund.database.entities.StCollectionPersonalBusinessDetails;
import com.handge.housingfund.database.entities.StCollectionUnitBusinessDetails;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Liujuhao on 2017/7/4.
 */
@SuppressWarnings({"Duplicates", "Convert2Lambda"})
@Component
public class CommonOpsImpl implements CommonOps {

    @Autowired
    private ICAuditHistoryDAO auditHistoryDAO;

    @Autowired
    protected ISaveAuditHistory iSaveAuditHistory;

    @Autowired
    private ICAccountNetworkDAO icAccountNetworkDAO;

    @Autowired
    private UnitRemittance unitRemittance;

   /* @Autowired
    private UnitPayback payBack;*/

    /*归集个人*/
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO iCollectionPersonalBusinessDetailsDAO;
    @Autowired
    private IStCollectionUnitBusinessDetailsDAO iStCollectionUnitBusinessDetailsDAO;
    @Autowired
    private ICCollectionIndividualAccountBasicViceDAO iCollectionIndividualAccountBasicViceDAO;
    @Autowired
    private ICCollectionIndividualAccountActionViceDAO iCollectionIndividualAccountActionViceDAO;
    @Autowired
    private ICCollectionIndividualAccountMergeViceDAO iCollectionIndividualAccountMergeViceDAO;
    @Autowired
    private ICCollectionIndividualAccountTransferViceDAO iCollectionIndividualAccountTransferViceDAO;
    @Autowired
    private ICCollectionWithdrawViceDAO iCollectionWithdrawViceDAO;
    @Autowired
    private ICCollectionAllochthounousTransferViceDAO icCollectionAllochthounousTransferViceDAO;
    /*归集单位*/
    @Autowired
    private ICCollectionUnitInformationActionViceDAO icCollectionUnitInformationActionViceDAO;
    @Autowired
    private ICCollectionUnitInformationBasicViceDAO icCollectionUnitInformationBasicViceDAO;
    /*    private ICCollectionUnitDepositInventoryViceDAO icCollectionUnitDepositInventoryViceDAO;  单位清册不能被删除、撤回*/
    @Autowired
    private ICCollectionUnitPaybackViceDAO icCollectionUnitPaybackViceDAO;
    @Autowired
    private ICCollectionUnitPayCallViceDAO icCollectionUnitPayCallViceDAO;
    @Autowired
    private ICCollectionUnitPayholdViceDAO icCollectionUnitPayholdViceDAO;
    @Autowired
    private ICCollectionUnitPayWrongViceDAO icCollectionUnitPayWrongViceDAO;
    @Autowired
    private ICCollectionUnitRemittanceViceDAO icCollectionUnitRemittanceViceDAO;
    @Autowired
    private ICCollectionPersonRadixViceDAO icCollectionPersonRadixViceDAO;
    @Autowired
    private ICCollectionUnitDepositRatioViceDAO icCollectionUnitDepositRatioViceDAO;


    public void deleteViceTable(IBaseDAO dao, String YWLSH, String YWMK) {

        Common entity = DAOBuilder.instance(dao).searchFilter(new HashMap<String, Object>() {
            {
                if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) this.put("grywmx.ywlsh", YWLSH);
                if (YWMK.equals(BusinessDetailsModule.Collection_unit.getCode())) this.put("dwywmx.ywlsh", YWLSH);
            }
        }).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        DAOBuilder.instance(dao).entity(entity).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }

    /**
     * 删除业务
     *
     * @param YWLSHs 业务流水号集合
     * @param YWMK   业务模块（个人01，单位02）
     * @return
     */
    public DeleteOperationRes deleteOperation(TokenContext tokenContext, List<String> YWLSHs, String YWMK) {

        List<String> pchList = new ArrayList();

        for (String YWLSH : YWLSHs) {

            if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

                StCollectionPersonalBusinessDetails det = iCollectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH);

                if (det == null) {
                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
                }

                if (det.getExtension().getCzmc().equals(CollectionBusinessType.部分提取.getCode()) || det.getExtension().getCzmc().equals(CollectionBusinessType.销户提取.getCode())) {

                    List<String> TQ_YWLSHs = iCollectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(YWLSH);
                    String currentPch = det.getExtension().getPch();
                    if (pchList.contains(currentPch)) {
                        continue;
                    }
                    try {
                        deleteWholeOperation(tokenContext, TQ_YWLSHs, YWMK);

                    } catch (ErrorException e) {
                        throw e;
                    }
                    pchList.add(currentPch);
                } else {
                    try {
                        deleteSingleOperation(tokenContext, YWLSH, YWMK);
                    } catch (ErrorException e) {
                        e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                        throw e;
                    }
                }
            } else {

                try {
                    deleteSingleOperation(tokenContext, YWLSH, YWMK);
                } catch (ErrorException e) {
                    e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                    throw e;
                }
            }
        }

        return new DeleteOperationRes() {

            {
                this.setStatus("success");
            }
        };
    }

    public void deleteWholeOperation(TokenContext tokenContext, List<String> YWLSHs, String YWMK) {

        for (String YWLSH : YWLSHs) {

            if (YWLSH == null) {

                throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
            }

            if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

                StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils
                        .getFirst(this.iCollectionPersonalBusinessDetailsDAO.list(new HashMap<String, Object>() {
                            {
                                this.put("ywlsh", YWLSH);
                            }
                        }, null, null, null, null, null, null));

                if (collectionPersonalBusinessDetails == null) {

                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务(" + YWLSH + ")不存在");
                }

                // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)

                String step = collectionPersonalBusinessDetails.getExtension().getStep();

                if (step == null || (!step.equals(CollectionBusinessStatus.新建.getName()) && !step.equals(CollectionBusinessStatus.审核不通过.getName()))) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务(" + YWLSH + ")不能被删除");
                }

                String ywwd = collectionPersonalBusinessDetails.getExtension().getYwwd().getId();
                String czy = collectionPersonalBusinessDetails.getExtension().getCzy();
                if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {

                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能删除");
                }

                String czmc = collectionPersonalBusinessDetails.getExtension().getCzmc();

//            根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更;72提取）
                if (czmc.equals(CollectionBusinessType.部分提取.getCode()) || czmc.equals(CollectionBusinessType.销户提取.getCode())) {

                    deleteViceTable(iCollectionWithdrawViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
                }
                DAOBuilder.instance(iCollectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).delete(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
            }
        }
    }

    public void deleteSingleOperation(TokenContext tokenContext, String YWLSH, String YWMK) {

        if (YWLSH == null) {

            throw new ErrorException(ReturnEnumeration.Parameter_MISS, "业务流水号");
        }

        if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils
                    .getFirst(this.iCollectionPersonalBusinessDetailsDAO.list(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", YWLSH);
                        }
                    }, null, null, null, null, null, null));

            if (collectionPersonalBusinessDetails == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务(" + YWLSH + ")不存在");
            }

            // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)

            String step = collectionPersonalBusinessDetails.getExtension().getStep();

            if (step == null || (!step.equals(CollectionBusinessStatus.新建.getName()) && !step.equals(CollectionBusinessStatus.审核不通过.getName()))) {

                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "该业务(" + YWLSH + ")不能被删除");
            }

            String ywwd = collectionPersonalBusinessDetails.getExtension().getYwwd().getId();
            String czy = collectionPersonalBusinessDetails.getExtension().getCzy();
            if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {

                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能删除");
            }

            String czmc = collectionPersonalBusinessDetails.getExtension().getCzmc();

//            根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更;72提取）
            if (czmc.equals(CollectionBusinessType.开户.getCode()) || czmc.equals(CollectionBusinessType.变更.getCode())) {

                deleteViceTable(iCollectionIndividualAccountBasicViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
            } else if (czmc.equals(CollectionBusinessType.封存.getCode()) || czmc.equals(CollectionBusinessType.启封.getCode()) || czmc.equals(CollectionBusinessType.冻结.getCode()) || czmc.equals(CollectionBusinessType.解冻.getCode())) {

                deleteViceTable(iCollectionIndividualAccountActionViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());

            } else if (czmc.equals(CollectionBusinessType.合并.getCode())) {

                deleteViceTable(iCollectionIndividualAccountMergeViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
            } else if (czmc.equals(CollectionBusinessType.内部转移.getCode())) {

                deleteViceTable(iCollectionIndividualAccountTransferViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
            } else if (czmc.equals(CollectionBusinessType.部分提取.getCode()) || czmc.equals(CollectionBusinessType.销户提取.getCode())) {

                deleteViceTable(iCollectionWithdrawViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
            } else if (czmc.equals(CollectionBusinessType.外部转入.getCode()) || czmc.equals(CollectionBusinessType.外部转出.getCode())) {

                deleteViceTable(icCollectionAllochthounousTransferViceDAO, YWLSH, BusinessDetailsModule.Collection_person.getCode());
            }
            DAOBuilder.instance(iCollectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        } else if (YWMK.equals(BusinessDetailsModule.Collection_unit.getCode())) {

            StCollectionUnitBusinessDetails collectionUnitBusinessDetails = CollectionUtils
                    .getFirst(this.iStCollectionUnitBusinessDetailsDAO.list(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", YWLSH);
                        }
                    }, null, null, null, null, null, null));

            if (collectionUnitBusinessDetails == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
            String step = collectionUnitBusinessDetails.getExtension().getStep();

            if (step == null || (!step.equals(CollectionBusinessStatus.新建.getName()) && !step.equals(CollectionBusinessStatus.审核不通过.getName()))) {

                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务号为" + YWLSH);
            }

            String ywwd = collectionUnitBusinessDetails.getExtension().getYwwd().getId();
            String czy = collectionUnitBusinessDetails.getExtension().getCzy();

            if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是由您受理的，不能删除");
            }

            String czmc = collectionUnitBusinessDetails.getExtension().getCzmc();

            //根据业务名称查询（71:合并;03:开户;72:销户;05:封存;04:启封;10:基数调整;75:比例调整;01:汇缴;02:补缴;13:缓缴处理;73:错缴更正;74:催缴)

            if (czmc.equals(CollectionBusinessType.开户.getCode()) || czmc.equals(CollectionBusinessType.变更.getCode())) {

                deleteViceTable(icCollectionUnitInformationBasicViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.封存.getCode()) || czmc.equals(CollectionBusinessType.启封.getCode()) || czmc.equals(CollectionBusinessType.销户.getCode())) {

                deleteViceTable(icCollectionUnitInformationActionViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.比例调整.getCode())) {

                deleteViceTable(icCollectionUnitDepositRatioViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.基数调整.getCode())) {

                deleteViceTable(icCollectionPersonRadixViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.补缴.getCode())) {

                deleteViceTable(icCollectionUnitPaybackViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.汇缴.getCode())) {

                deleteViceTable(icCollectionUnitRemittanceViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

                // TODO: 2017/11/9 杨凡扩展


            } else if (czmc.equals(CollectionBusinessType.催缴.getCode())) {

                deleteViceTable(icCollectionUnitPayCallViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.错缴更正.getCode())) {

                deleteViceTable(icCollectionUnitPayWrongViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());

            } else if (czmc.equals(CollectionBusinessType.缓缴处理.getCode())) {

                deleteViceTable(icCollectionUnitPayholdViceDAO, YWLSH, BusinessDetailsModule.Collection_unit.getCode());
            }

            DAOBuilder.instance(iStCollectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).delete(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }
    }

    @Deprecated /*目前撤回功能无需更改副表业务状态*/
    public void revokeViceTable(IBaseDAO dao, String YWLSH, String YWMK, String CZMC) {
    }

    /**
     * 撤回业务
     *
     * @param YWLSH 业务流水号
     * @param YWMK  业务模块（归集个人01，归集单位02,，个人贷款03，项目贷款04，财务日常05，财务活转定06，财务支取07）
     * @return
     */
    public RevokeOperationRes revokeOperation(TokenContext tokenContext, String YWLSH, String YWMK) {

        if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

            StCollectionPersonalBusinessDetails det = iCollectionPersonalBusinessDetailsDAO.getByYwlsh(YWLSH);

            if (det == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            if (det.getExtension().getCzmc().equals(CollectionBusinessType.部分提取.getCode()) || det.getExtension().getCzmc().equals(CollectionBusinessType.销户提取.getCode())) {

                List<String> TQ_YWLSHs = iCollectionPersonalBusinessDetailsDAO.getYwlshListByPchByYwlsh(YWLSH);
                try {
                    revokeWholeOperation(tokenContext, TQ_YWLSHs, YWMK);

                } catch (ErrorException e) {
                    throw e;
                }
            } else {
                try {
                    revokeSingleOperation(tokenContext, YWLSH, YWMK);

                } catch (ErrorException e) {
                    e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                    throw e;
                }
            }
        } else {
            try {
                revokeSingleOperation(tokenContext, YWLSH, YWMK);

            } catch (ErrorException e) {

                e.setMsg(e.getMsg() + " 业务号为" + YWLSH);
                throw e;
            }
        }

        return new RevokeOperationRes() {
            {
                this.setStatus("success");

            }
        };
    }

    public void revokeWholeOperation(TokenContext tokenContext, List<String> YWLSHs, String YWMK) {

        for (String YWLSH : YWLSHs) {

            if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

                StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils
                        .getFirst(this.iCollectionPersonalBusinessDetailsDAO.list(new HashMap<String, Object>() {
                            {
                                this.put("ywlsh", YWLSH);
                            }
                        }, null, null, null, null, null, null));

                if (collectionPersonalBusinessDetails == null) {

                    throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
                }

                // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)

                String step = collectionPersonalBusinessDetails.getExtension().getStep();

                if (step == null || !StringUtil.isIntoReview(step, null) /** 还应考虑是否处于审核中**/) {

                    throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务号为" + YWLSH);

                }

                MultiReviewConfig config = NormalJsonUtils.toObj4Review(collectionPersonalBusinessDetails.getExtension().getShybh());

                String CaoZuo = "不通过撤回";

                step = checkByReivew(config, tokenContext);

                if (CollectionBusinessStatus.新建.getName().equals(step)) {
                    String ywwd = collectionPersonalBusinessDetails.getExtension().getYwwd().getId();
                    String czy = collectionPersonalBusinessDetails.getExtension().getCzy();
                    if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {
                        throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是您受理的，不能撤回");
                    }
                    CaoZuo = "撤回";
                }

            /*根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更,72:提取）*/
                String ywlx = collectionPersonalBusinessDetails.getExtension().getCzmc();

                collectionPersonalBusinessDetails.getExtension().setStep(step);
                collectionPersonalBusinessDetails.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));

                iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.getNameByCode(ywlx), CaoZuo);

                DAOBuilder.instance(iCollectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {

                        throw new ErrorException(e);
                    }
                });
            }
        }
    }

    public void revokeSingleOperation(TokenContext tokenContext, String YWLSH, String YWMK) {
        if (YWMK.equals(BusinessDetailsModule.Collection_person.getCode())) {

            StCollectionPersonalBusinessDetails collectionPersonalBusinessDetails = CollectionUtils
                    .getFirst(this.iCollectionPersonalBusinessDetailsDAO.list(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", YWLSH);
                        }
                    }, null, null, null, null, null, null));

            if (collectionPersonalBusinessDetails == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)

            String step = collectionPersonalBusinessDetails.getExtension().getStep();

            if (step == null || !StringUtil.isIntoReview(step, null) /** 还应考虑是否处于审核中**/) {

                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务号为" + YWLSH);

            }

            MultiReviewConfig config = NormalJsonUtils.toObj4Review(collectionPersonalBusinessDetails.getExtension().getShybh());

            String CaoZuo = "不通过撤回";

            step = checkByReivew(config, tokenContext);

            if (CollectionBusinessStatus.新建.getName().equals(step)) {
                String ywwd = collectionPersonalBusinessDetails.getExtension().getYwwd().getId();
                String czy = collectionPersonalBusinessDetails.getExtension().getCzy();
                if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是您受理的，不能撤回");
                }
                CaoZuo = "撤回";
            }

            /*根据业务名称查询（71:合并;03:开户;06:内部转移;05:封存;04:启封;14:冻结;15:解冻;70:变更,72:提取）*/
            String ywlx = collectionPersonalBusinessDetails.getExtension().getCzmc();

            collectionPersonalBusinessDetails.getExtension().setStep(step);
            collectionPersonalBusinessDetails.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));

            iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.getNameByCode(ywlx), CaoZuo);

            DAOBuilder.instance(iCollectionPersonalBusinessDetailsDAO).entity(collectionPersonalBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });
        } else if (YWMK.equals(BusinessDetailsModule.Collection_unit.getCode())) {

            StCollectionUnitBusinessDetails collectionUnitBusinessDetails = CollectionUtils
                    .getFirst(this.iStCollectionUnitBusinessDetailsDAO.list(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", YWLSH);
                        }
                    }, null, null, null, null, null, null));

            if (collectionUnitBusinessDetails == null) {

                throw new ErrorException(ReturnEnumeration.Data_MISS, "业务不存在");
            }

            // 业务状态(00:所有；01：新建；02:待审核；03:审核不通过；04:办结)
            String step = collectionUnitBusinessDetails.getExtension().getStep();

            if (step == null || !StringUtil.isIntoReview(step, null) /** 还应考虑是否处于审核中**/) {

                throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务号为" + YWLSH);

            }

            MultiReviewConfig config = NormalJsonUtils.toObj4Review(collectionUnitBusinessDetails.getExtension().getShybh());

            String CaoZuo = "不通过撤回";

            step = checkByReivew(config, tokenContext);

            if (CollectionBusinessStatus.新建.getName().equals(step)) {
                String ywwd = collectionUnitBusinessDetails.getExtension().getYwwd().getId();
                String czy = collectionUnitBusinessDetails.getExtension().getCzy();
                if (!tokenContext.getUserInfo().getYWWD().equals(ywwd) || !tokenContext.getUserInfo().getCZY().equals(czy)) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务(" + YWLSH + ")不是您受理的，不能撤回");
                }
                CaoZuo = "撤回";
            }

            /*根据业务名称查询（71:合并;03:开户;72:销户;05:封存;04:启封;10:基数调整;75:比例调整;01:汇缴;02:补缴;13:缓缴处理;73:错缴更正;74:催缴)*/
            String ywlx = collectionUnitBusinessDetails.getExtension().getCzmc();

            collectionUnitBusinessDetails.getExtension().setStep(step);
            collectionUnitBusinessDetails.getExtension().setShybh(NormalJsonUtils.toJson4Review(config));
            iSaveAuditHistory.saveNormalBusiness(YWLSH, tokenContext, CollectionBusinessType.getNameByCode(ywlx), CaoZuo);
            DAOBuilder.instance(iStCollectionUnitBusinessDetailsDAO).entity(collectionUnitBusinessDetails).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });
        }
    }

    //基于审核配置的验证，并返回正确的step状态
    public String checkByReivew(MultiReviewConfig config, TokenContext tokenContext) {
/*        if (StringUtil.notEmpty(config.getDQSHY())) {

            throw new ErrorException(ReturnEnumeration.Business_Status_NOT_MATCH, "业务正在被审核员:" + config.getDQSHY() + "审核中");
        }*/
        String step;
        if (config == null) {
            step = CollectionBusinessStatus.新建.getName();
        } else {
            //审核级别（0：普审，1：特审）
            if ("S".equals(config.getSHJB())) {
                throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务已进入特审阶段，不能撤回");
            }
            if (StringUtil.notEmpty(config.getSCSHY())) {
                if (!tokenContext.getUserId().equals(config.getSCSHY())) {
                    throw new ErrorException(ReturnEnumeration.Permission_Denied, "该业务的上一级审核非您操作，不能撤回");
                }
                step = CollectionBusinessStatus.审核不通过.getName();
            } else {
                step = CollectionBusinessStatus.新建.getName();
            }
            config.setLSSHYBH(null);
            config.setSHJB(null);
            config.setSCSHY(null);
            config.setDQSHY(null);
            config.setDQXM(null);
        }
        return step;
    }

    /**
     * 查询单位账号、个人账号的业务历史（审核）记录
     *
     * @param ZhangHao 个人账号或单位账号
     * @param YWMK     业务模块，01个人，02单位
     * @return
     */
    @Override
    public PageRes<GetCommonHistory> recordHistory(String ZhangHao, String YWMK, String pageSize, String pageNo) {

        ArrayList<GetCommonHistory> results = new ArrayList();

        PageRes pageRes = new PageRes();

        /*个人历史记录*/
        if (BusinessDetailsModule.Collection_person.getCode().equals(YWMK)) {

            List<StCollectionPersonalBusinessDetails> entities = DAOBuilder.instance(iCollectionPersonalBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("grzh", ZhangHao);
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            ArrayList<String> ywlshs = new ArrayList();

            for (StCollectionPersonalBusinessDetails entity : entities) {
                if (StringUtil.notEmpty(entity.getYwlsh()))
                    ywlshs.add(entity.getYwlsh());
            }

            if (!ywlshs.isEmpty()) {
                List<CAuditHistory> histories = DAOBuilder.instance(auditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", ywlshs);
                    }
                }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(pageNo)).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {

                        throw new ErrorException(e);
                    }
                });

                for (CAuditHistory history : histories) {
                    GetCommonHistory getCommonHistory = new GetCommonHistory();
                    getCommonHistory.setYWLX(history.getYwlx());
                    getCommonHistory.setSLSJ(DateUtil.date2Str(history.getShsj() == null ? history.getDdsj() : history.getShsj(), "yyyy-MM-dd HH:mm"));
                    getCommonHistory.setCZNR(history.getCaoZuo());
                    getCommonHistory.setCZY(history.getCzy());
                    try {
                        String ywwdmc = icAccountNetworkDAO.get(history.getYwwd()).getMingCheng();
                        getCommonHistory.setYWWD(ywwdmc);
                    } catch (Exception e) {
                        getCommonHistory.setYWWD("缺失");
                    }
                    getCommonHistory.setCZQD(history.getCzqd());
                    getCommonHistory.setYWLSH(history.getYwlsh());
                    results.add(getCommonHistory);
                }
            }
        }

        /*单位历史记录*/
        else if (BusinessDetailsModule.Collection_unit.getCode().equals(YWMK)) {

            List<StCollectionUnitBusinessDetails> entities = DAOBuilder.instance(iStCollectionUnitBusinessDetailsDAO).searchFilter(new HashMap<String, Object>() {
                {
                    this.put("dwzh", ZhangHao);
                }
            }).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {

                    throw new ErrorException(e);
                }
            });

            ArrayList<String> ywlshs = new ArrayList();

            for (StCollectionUnitBusinessDetails entity : entities) {
                if (StringUtil.notEmpty(entity.getYwlsh()))
                    ywlshs.add(entity.getYwlsh());
            }

            if (!ywlshs.isEmpty()) {
                List<CAuditHistory> histories = DAOBuilder.instance(auditHistoryDAO).searchFilter(new HashMap<String, Object>() {
                    {
                        this.put("ywlsh", ywlshs);
                    }
                }).pageOption(pageRes, Integer.parseInt(pageSize), Integer.parseInt(pageNo)).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {

                        throw new ErrorException(e);
                    }
                });

                for (CAuditHistory history : histories) {
                    GetCommonHistory getCommonHistory = new GetCommonHistory();
                    getCommonHistory.setYWLX(history.getYwlx());
                    getCommonHistory.setSLSJ(DateUtil.date2Str(history.getShsj() == null ? history.getDdsj() : history.getShsj(), "yyyy-MM-dd HH:mm"));
                    getCommonHistory.setCZNR(history.getCaoZuo());
                    getCommonHistory.setCZY(history.getCzy());
                    String ywwdmc = icAccountNetworkDAO.get(history.getYwwd()).getMingCheng();
                    getCommonHistory.setYWWD(ywwdmc);
                    getCommonHistory.setCZQD(history.getCzqd());
                    getCommonHistory.setYWLSH(history.getYwlsh());
                    results.add(getCommonHistory);
                }
            }
        }

        pageRes.setResults(results);
        pageRes.setCurrentPage(pageRes.getCurrentPage());
        pageRes.setNextPageNo(pageRes.getNextPageNo());
        pageRes.setPageCount(pageRes.getPageCount());
        pageRes.setPageSize(pageRes.getPageSize());
        pageRes.setTotalCount(pageRes.getTotalCount());

        return pageRes;
    }

    @Override
    public ArrayList<DetailsReviewInfo> getReviewInfos(String YWLSH) {

        return new ArrayList<>(CollectionUtils.flatmap(DAOBuilder.instance(this.auditHistoryDAO).searchFilter(new HashMap<String, Object>() {{

            this.put("ywlsh", YWLSH);

        }}).orderOption("shsj", Order.DESC).extension(new IBaseDAO.CriteriaExtension() {
            @Override
            public void extend(Criteria criteria) {
                criteria.add(Restrictions.isNotNull("shjg"));
                criteria.add(Restrictions.ne("CaoZuo", "撤回"));
            }
        }).getList(new DAOBuilder.ErrorHandler() {

            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }

        }), new CollectionUtils.Transformer<CAuditHistory, DetailsReviewInfo>() {
            @Override
            public DetailsReviewInfo tansform(CAuditHistory cAuditHistory) {

                return new DetailsReviewInfo() {{

                    this.setSHSJ(DateUtil.date2Str(cAuditHistory.getShsj(), "yyyy-MM-dd HH:mm"));
                    this.setSHJG(cAuditHistory.getShjg());
                    this.setYYYJ(cAuditHistory.getYyyj());
                    this.setCZY(cAuditHistory.getCzy());
                    String ywwdmc = icAccountNetworkDAO.get(cAuditHistory.getYwwd()).getMingCheng();
                    this.setYWWD(ywwdmc);
                    this.setZhiWu(cAuditHistory.getZhiwu());
                    this.setCZQD(cAuditHistory.getCzqd());
                    this.setBeiZhu(cAuditHistory.getBeiZhu());
                }};
            }
        }));
    }

    @Override
    public BusCommonRetrun accountRecord(String ywlsh, String ywlx, String code) {

        if ("01".equals(ywlx)) {
            return unitRemittance.accountRecord(ywlsh, code);
        } else if ("02".equals(ywlx)) {
            /*return payBack.accountRecord(ywlsh,code);*/
        }

        BusCommonRetrun busCommonRetrun = new BusCommonRetrun("01", ywlsh);
        busCommonRetrun.setMessage("失败");
        return busCommonRetrun;

    }
}