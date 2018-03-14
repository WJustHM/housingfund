package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IActived2FixedService;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.model.Actived2Fixed;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICFinanceActived2FixedDAO;
import com.handge.housingfund.database.entities.CFinanceActived2Fixed;
import com.handge.housingfund.database.entities.CFinanceManageFinanceExtension;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */
@SuppressWarnings("Duplicates")
@Component
public class Actived2FixedService implements IActived2FixedService {
    @Autowired
    private ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    IFinanceTrader iFinanceTrader;



    @Override
    public PageRes<Actived2Fixed> getActivedToFixeds(TokenContext tokenContext, String khyhmc, String acct_no, String deposit_period, String step, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(khyhmc)) filter.put("khyhmc", khyhmc);
        if (StringUtil.notEmpty(acct_no)) filter.put("acct_no", acct_no);
        if (StringUtil.notEmpty(deposit_period)) filter.put("deposit_period", deposit_period);
        if (StringUtil.notEmpty(step) && !"所有".equals(step)) filter.put("cFinanceManageFinanceExtension.step", step);

        PageRes pageRes = new PageRes();

        List<CFinanceActived2Fixed> cFinanceActived2Fixeds = DAOBuilder.instance(icFinanceActived2FixedDAO)
                .searchFilter(filter)
                .pageOption(pageRes, pageSize, pageNo)
                .searchOption(SearchOption.FUZZY)
                .orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                    }
                });

        PageRes<Actived2Fixed> actived2FixedPageRes = new PageRes<>();
        actived2FixedPageRes.setCurrentPage(pageRes.getCurrentPage());
        actived2FixedPageRes.setNextPageNo(pageRes.getNextPageNo());
        actived2FixedPageRes.setPageCount(pageRes.getPageCount());
        actived2FixedPageRes.setPageSize(pageRes.getPageSize());
        actived2FixedPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<Actived2Fixed> actived2Fixeds = new ArrayList<>();

        for (CFinanceActived2Fixed cFinanceActived2Fixed : cFinanceActived2Fixeds) {
            Actived2Fixed actived2Fixed = new Actived2Fixed();
            actived2Fixed.setAcct_name(cFinanceActived2Fixed.getAcct_name());
            actived2Fixed.setAcct_no(cFinanceActived2Fixed.getAcct_no());
            actived2Fixed.setAmt(cFinanceActived2Fixed.getAmt() != null ? cFinanceActived2Fixed.getAmt().toPlainString() : null);
            actived2Fixed.setBeizhu(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getBeizhu());
            actived2Fixed.setCzy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy());
            actived2Fixed.setDeposit_begin_date(DateUtil.date2Str(cFinanceActived2Fixed.getDeposit_begin_date(), "yyyy-MM-dd"));
            actived2Fixed.setDeposit_end_date(DateUtil.date2Str(cFinanceActived2Fixed.getDeposit_end_date(), "yyyy-MM-dd"));
            actived2Fixed.setDeposit_period(cFinanceActived2Fixed.getDeposit_period());
            actived2Fixed.setId(cFinanceActived2Fixed.getId());
            actived2Fixed.setInterest_rate(cFinanceActived2Fixed.getInterest_rate() != null ? cFinanceActived2Fixed.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
            actived2Fixed.setKhyhmc(cFinanceActived2Fixed.getKhyhmc());
            actived2Fixed.setStep(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep());
            actived2Fixed.setYwlsh(cFinanceActived2Fixed.getYwlsh());
            actived2Fixed.setSlrq(DateUtil.date2Str(cFinanceActived2Fixed.getCreated_at(),"yyyy-MM-dd"));
            actived2Fixed.setSbyy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getSbyy());
            actived2Fixed.setRgcl(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getRgcl());

            actived2Fixeds.add(actived2Fixed);
        }

        actived2FixedPageRes.setResults(actived2Fixeds);
        return actived2FixedPageRes;
    }

    @Override
    public Actived2Fixed getActivedToFixed(TokenContext tokenContext, String id) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
            }
        });

        if (cFinanceActived2Fixed == null) {
            cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("ywlsh", id);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败，请联系管理员");
                }
            });
            if (cFinanceActived2Fixed == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有该业务(" + id + ")的相关信息");
            }
        }

        Actived2Fixed actived2Fixed = new Actived2Fixed();
        actived2Fixed.setAcct_name(cFinanceActived2Fixed.getAcct_name());
        actived2Fixed.setAcct_no(cFinanceActived2Fixed.getAcct_no());
        actived2Fixed.setAmt(cFinanceActived2Fixed.getAmt() != null ? cFinanceActived2Fixed.getAmt().toPlainString() : null);
        actived2Fixed.setBeizhu(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getBeizhu());
        actived2Fixed.setCzy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy());
        actived2Fixed.setDeposit_begin_date(DateUtil.date2Str(cFinanceActived2Fixed.getDeposit_begin_date(), "yyyy-MM-dd"));
        actived2Fixed.setDeposit_end_date(DateUtil.date2Str(cFinanceActived2Fixed.getDeposit_end_date(), "yyyy-MM-dd"));
        actived2Fixed.setDeposit_period(cFinanceActived2Fixed.getDeposit_period());
        actived2Fixed.setId(cFinanceActived2Fixed.getId());
        actived2Fixed.setInterest_rate(cFinanceActived2Fixed.getInterest_rate() != null ? cFinanceActived2Fixed.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
        actived2Fixed.setKhyhmc(cFinanceActived2Fixed.getKhyhmc());
        actived2Fixed.setStep(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep());
        actived2Fixed.setYwlsh(cFinanceActived2Fixed.getYwlsh());
        actived2Fixed.setSlrq(DateUtil.date2Str(cFinanceActived2Fixed.getCreated_at(),"yyyy-MM-dd"));
        actived2Fixed.setSbyy(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getSbyy());
        actived2Fixed.setRgcl(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getRgcl());

        return actived2Fixed;
    }

    @Override
    public Actived2Fixed addActivedToFixed(TokenContext tokenContext, Actived2Fixed actived2Fixed, String type) {
        CFinanceActived2Fixed cFinanceActived2Fixed = new CFinanceActived2Fixed();

        cFinanceActived2Fixed.setAcct_name(actived2Fixed.getAcct_name());
        cFinanceActived2Fixed.setAcct_no(actived2Fixed.getAcct_no());
        cFinanceActived2Fixed.setAmt(new BigDecimal(actived2Fixed.getAmt()).setScale(2, BigDecimal.ROUND_HALF_UP));
        cFinanceActived2Fixed.setDeposit_period(actived2Fixed.getDeposit_period());

        if (StringUtil.notEmpty(actived2Fixed.getInterest_rate()))
            cFinanceActived2Fixed.setInterest_rate(new BigDecimal(actived2Fixed.getInterest_rate()));

        cFinanceActived2Fixed.setKhyhmc(actived2Fixed.getKhyhmc());
        cFinanceActived2Fixed.setExtend_deposit_type("2");
        cFinanceActived2Fixed.setPart_extend_deposit_acct_no(actived2Fixed.getAcct_no());

        // 走状态机
        CFinanceManageFinanceExtension cFinanceManageFinanceExtension = new CFinanceManageFinanceExtension();
        cFinanceManageFinanceExtension.setStep(FinanceBusinessStatus.初始状态.getName());
        cFinanceManageFinanceExtension.setBeizhu(actived2Fixed.getBeizhu());
        cFinanceManageFinanceExtension.setCzy(actived2Fixed.getCzy());
        cFinanceManageFinanceExtension.setCzmc("活期转定期");
        cFinanceManageFinanceExtension.setYwlx("09");
        cFinanceActived2Fixed.setcFinanceManageFinanceExtension(cFinanceManageFinanceExtension);

        CFinanceActived2Fixed cnanceActived2Fixed = DAOBuilder.instance(this.icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        execute(tokenContext, cnanceActived2Fixed, type, "0");

        iSaveAuditHistory.saveNormalBusiness(cnanceActived2Fixed.getYwlsh(), tokenContext, BusinessSubType.财务_定期活期转定期.getDescription(), "新建");

        actived2Fixed.setId(cnanceActived2Fixed.getId());

        if ("1".equals(type)) {
            //TODO 调结算平台
            iFinanceTrader.actived2Fixed(cnanceActived2Fixed.getId());
        }

        return actived2Fixed;
    }

    @Override
    public boolean modifyActivedToFixed(TokenContext tokenContext, String id, Actived2Fixed actived2Fixed, String type) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (!"新建".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()) && !"未通过".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()))
            return true;

        cFinanceActived2Fixed.setAmt(new BigDecimal(actived2Fixed.getAmt()).setScale(2, BigDecimal.ROUND_HALF_UP));
        cFinanceActived2Fixed.setDeposit_period(actived2Fixed.getDeposit_period());
        cFinanceActived2Fixed.setInterest_rate(new BigDecimal(actived2Fixed.getInterest_rate()));

        CFinanceManageFinanceExtension cFinanceManageFinanceExtension = cFinanceActived2Fixed.getcFinanceManageFinanceExtension();
        cFinanceManageFinanceExtension.setBeizhu(actived2Fixed.getBeizhu());
        cFinanceManageFinanceExtension.setCzy(actived2Fixed.getCzy());

        execute(tokenContext, cFinanceActived2Fixed, type, "1");

        iSaveAuditHistory.saveNormalBusiness(cFinanceActived2Fixed.getYwlsh(), tokenContext, BusinessSubType.财务_定期活期转定期.getDescription(), "修改");

        if ("1".equals(type)) {
            //TODO 调结算平台
            iFinanceTrader.actived2Fixed(id);
        }

        return false;
    }

    @Override
    public boolean delActivedToFixed(TokenContext tokenContext, String id) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (!"新建".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()) && !"未通过".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()))
            return true;

        DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除活期转到定期失败");
            }
        });

        return false;
    }

    public boolean submitActivedToFixed(TokenContext tokenContext, String id) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (!"新建".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()) && !"未通过".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()))
            return true;

        execute(tokenContext, cFinanceActived2Fixed, "1", "1");

        iSaveAuditHistory.saveNormalBusiness(cFinanceActived2Fixed.getYwlsh(), tokenContext, BusinessSubType.财务_定期活期转定期.getDescription(), "修改");

        //TODO 调结算平台
        iFinanceTrader.actived2Fixed(id);

        return false;
    }

    public boolean revokeActivedToFixed(TokenContext tokenContext, String id) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (!"待审核".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()))
            return true;

        cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("新建");
        DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return false;
    }

    public boolean updateA2FStep(TokenContext tokenContext, String id, String step) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (!"定期存款失败".equals(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep()))
            return true;

        cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep(step);
        DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return false;
    }

    private void execute(TokenContext tokenContext, CFinanceActived2Fixed cFinanceActived2Fixed, String type, String action) {
        StateMachineUtils.updateState(iStateMachineService, "0".equals(action) ? (type.equals("0") ? Events.通过.getEvent() : Events.提交.getEvent()) : (type.equals("0") ? Events.保存.getEvent() : Events.通过.getEvent()),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getStep());
                    this.setTaskId(cFinanceActived2Fixed.getYwlsh());
                    if (tokenContext.getRoleList().size() != 0) {
                        this.setRole(tokenContext.getRoleList().get(0)/*角色*/);
                    }
                    this.setSubtype(BusinessSubType.财务_定期活期转定期.getSubType());
                    this.setType(BusinessType.Finance);
                    this.setWorkstation(tokenContext.getUserInfo().getYWWD());
                }}, new StateMachineUtils.StateChangeHandler() {
                    @Override
                    public void onStateChange(boolean succeed, String next, Exception e) {
                        if (e != null) {
                            throw new ErrorException(e);
                        }
                        if (!succeed || next == null) {
                            return;
                        }
                        if (FinanceBusinessStatus.待活转定.getName().equals(next))
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setDdsj(new Date());
                        cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep(next);
                        icFinanceActived2FixedDAO.update(cFinanceActived2Fixed);
                    }
                });
    }
}
