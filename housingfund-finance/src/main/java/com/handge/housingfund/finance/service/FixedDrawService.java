package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.ISaveAuditHistory;
import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IFixedDrawService;
import com.handge.housingfund.common.service.finance.IFixedRecordService;
import com.handge.housingfund.common.service.finance.model.FixedDraw;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessStatus;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICFinanceFixedBalanceDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedDrawDAO;
import com.handge.housingfund.database.entities.CFinanceFixedBalance;
import com.handge.housingfund.database.entities.CFinanceFixedDraw;
import com.handge.housingfund.database.entities.CFinanceManageFinanceExtension;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.StateMachineUtils;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
@SuppressWarnings("Duplicates")
@Component
public class FixedDrawService implements IFixedDrawService{
    private static Logger logger = LogManager.getLogger(FixedDrawService.class);

    @Autowired
    private ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;
    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    private ICFinanceFixedBalanceDAO icFinanceFixedBalanceDAO;
    @Autowired
    @Resource(name = "stateMachineServiceV2")
    private IStateMachineService iStateMachineService;
    @Autowired
    private ISaveAuditHistory iSaveAuditHistory;
    @Autowired
    private IFinanceTrader iFinanceTrader;
    @Autowired
    private IFixedRecordService iFixedRecordService;


    @Override
    public PageRes<FixedDraw> getFixedDraws(TokenContext tokenContext, String khyhmc, String acctNo, String bookNo, int bookListNo, String step, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(khyhmc)) filter.put("khyhmc", khyhmc);
        if (StringUtil.notEmpty(acctNo)) filter.put("acct_no", acctNo);
        if (StringUtil.notEmpty(bookNo)) filter.put("book_no", bookNo);
        if (bookListNo != -1) filter.put("book_list_no", bookListNo);
        if (StringUtil.notEmpty(step) && !"所有".equals(step)) filter.put("cFinanceManageFinanceExtension.step", step);

        PageRes pageRes = new PageRes();

        List<CFinanceFixedDraw> cFinanceActived2Fixeds = DAOBuilder.instance(icFinanceFixedDrawDAO)
                .searchFilter(filter)
                .pageOption(pageRes, pageSize, pageNo)
                .searchOption(SearchOption.FUZZY)
                .orderOption("created_at", Order.DESC).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageRes<FixedDraw> fixedDrawPageRes = new PageRes<>();
        fixedDrawPageRes.setCurrentPage(pageRes.getCurrentPage());
        fixedDrawPageRes.setNextPageNo(pageRes.getNextPageNo());
        fixedDrawPageRes.setPageCount(pageRes.getPageCount());
        fixedDrawPageRes.setPageSize(pageRes.getPageSize());
        fixedDrawPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<FixedDraw> fixedDraws = new ArrayList<>();

        for (CFinanceFixedDraw cFinanceFixedDraw : cFinanceActived2Fixeds) {
            FixedDraw fixedDraw = new FixedDraw();
            fixedDraw.setAcct_name(cFinanceFixedDraw.getAcct_name());
            fixedDraw.setAcct_no(cFinanceFixedDraw.getAcct_no());
            fixedDraw.setBeizhu(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getBeizhu());
            fixedDraw.setBook_list_no(cFinanceFixedDraw.getBook_list_no());
            fixedDraw.setBook_no(cFinanceFixedDraw.getBook_no());
            fixedDraw.setCzy(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy());
            fixedDraw.setDeposit_begin_date(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_begin_date(), "yyyy-MM-dd"));
            fixedDraw.setDeposit_end_date(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_end_date(), "yyyy-MM-dd"));
            fixedDraw.setDeposit_period(cFinanceFixedDraw.getDeposit_period());
            fixedDraw.setDeposti_amt(cFinanceFixedDraw.getDeposti_amt() != null ? cFinanceFixedDraw.getDeposti_amt().toPlainString() : null);
            fixedDraw.setDraw_amt(cFinanceFixedDraw.getDraw_amt() != null ? cFinanceFixedDraw.getDraw_amt().toPlainString() : null);
            fixedDraw.setId(cFinanceFixedDraw.getId());
            fixedDraw.setInterest_rate(cFinanceFixedDraw.getInterest_rate() != null ? cFinanceFixedDraw.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
            fixedDraw.setKhyhmc(cFinanceFixedDraw.getKhyhmc());
            fixedDraw.setStep(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep());
            fixedDraw.setYwlsh(cFinanceFixedDraw.getYwlsh());
            fixedDraw.setZqqk(cFinanceFixedDraw.getZqqk());
            fixedDraw.setDqckbh(cFinanceFixedDraw.getDqckbh());
            fixedDraw.setSbyy(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getSbyy());
            fixedDraw.setRgcl(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getRgcl());

            fixedDraws.add(fixedDraw);
        }

        fixedDrawPageRes.setResults(fixedDraws);
        return fixedDrawPageRes;
    }

    @Override
    public FixedDraw getFixedDraw(TokenContext tokenContext,String id) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).searchFilter(new HashMap<String, Object>(){{
                this.put("ywlsh", id);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                }
            });
            if (cFinanceFixedDraw == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有" + id + "的相关信息");
            }
        }

        FixedDraw fixedDraw = new FixedDraw();
        fixedDraw.setAcct_name(cFinanceFixedDraw.getAcct_name());
        fixedDraw.setAcct_no(cFinanceFixedDraw.getAcct_no());
        fixedDraw.setBeizhu(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getBeizhu());
        fixedDraw.setBook_list_no(cFinanceFixedDraw.getBook_list_no());
        fixedDraw.setBook_no(cFinanceFixedDraw.getBook_no());
        fixedDraw.setCzy(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy());
        fixedDraw.setDeposit_begin_date(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_begin_date(), "yyyy-MM-dd"));
        fixedDraw.setDeposit_end_date(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_end_date(), "yyyy-MM-dd"));
        fixedDraw.setDeposit_period(cFinanceFixedDraw.getDeposit_period());
        fixedDraw.setDeposti_amt(cFinanceFixedDraw.getDeposti_amt() != null ? cFinanceFixedDraw.getDeposti_amt().toPlainString() : null);
        fixedDraw.setDraw_amt(cFinanceFixedDraw.getDraw_amt() != null ? cFinanceFixedDraw.getDraw_amt().toPlainString() : null);
        fixedDraw.setId(cFinanceFixedDraw.getId());
        fixedDraw.setInterest_rate(cFinanceFixedDraw.getInterest_rate() != null ? cFinanceFixedDraw.getInterest_rate().setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() : null);
        fixedDraw.setKhyhmc(cFinanceFixedDraw.getKhyhmc());
        fixedDraw.setStep(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep());
        fixedDraw.setYwlsh(cFinanceFixedDraw.getYwlsh());
        fixedDraw.setZqqk(cFinanceFixedDraw.getZqqk());
        fixedDraw.setDqckbh(cFinanceFixedDraw.getDqckbh());
        fixedDraw.setSbyy(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getSbyy());
        fixedDraw.setRgcl(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getRgcl());

        return fixedDraw;
    }

    @Override
    public FixedDraw addFixedDraw(TokenContext tokenContext, FixedDraw fixedDraw, String type) {
        CFinanceFixedDraw cFinanceFixedDraw = new CFinanceFixedDraw();

        cFinanceFixedDraw.setAcct_name(iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(fixedDraw.getAcct_no()).getYHZHMC());
        cFinanceFixedDraw.setAcct_no(fixedDraw.getAcct_no());
        cFinanceFixedDraw.setBook_list_no(fixedDraw.getBook_list_no());
        cFinanceFixedDraw.setBook_no(fixedDraw.getBook_no());
        try {
            cFinanceFixedDraw.setDeposit_begin_date(DateUtil.str2Date("yyyy-MM-dd",fixedDraw.getDeposit_begin_date()));
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "存入日期格式有误");
        }
        try {
            cFinanceFixedDraw.setDeposit_end_date(DateUtil.str2Date("yyyy-MM-dd",fixedDraw.getDeposit_end_date()));
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Data_NOT_MATCH, "到期日期格式有误");
        }
        cFinanceFixedDraw.setDeposit_period(fixedDraw.getDeposit_period());
        cFinanceFixedDraw.setDraw_amt(new BigDecimal(fixedDraw.getDraw_amt()));

        if (StringUtil.notEmpty(fixedDraw.getDeposti_amt()))
            cFinanceFixedDraw.setDeposti_amt(new BigDecimal(fixedDraw.getDeposti_amt()));

        if (StringUtil.notEmpty(fixedDraw.getInterest_rate()))
            cFinanceFixedDraw.setInterest_rate(new BigDecimal(fixedDraw.getInterest_rate()));

        cFinanceFixedDraw.setKhyhmc(fixedDraw.getKhyhmc());
        cFinanceFixedDraw.setZqqk(fixedDraw.getZqqk());
        cFinanceFixedDraw.setDqckbh(fixedDraw.getDqckbh());

        //TODO 走状态机
        CFinanceManageFinanceExtension cFinanceManageFinanceExtension = new CFinanceManageFinanceExtension();
        cFinanceManageFinanceExtension.setStep(FinanceBusinessStatus.初始状态.getName());
        cFinanceManageFinanceExtension.setBeizhu(fixedDraw.getBeizhu());
        cFinanceManageFinanceExtension.setCzy(fixedDraw.getCzy());
        cFinanceManageFinanceExtension.setCzmc("定期支取");
        cFinanceManageFinanceExtension.setYwlx("10");
        cFinanceFixedDraw.setcFinanceManageFinanceExtension(cFinanceManageFinanceExtension);

        CFinanceFixedDraw cfinanceFixedDraw = DAOBuilder.instance(this.icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).saveThenFetchObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        execute(tokenContext, cfinanceFixedDraw, type, "0");

        CFinanceFixedBalance cFinanceFixedBalance = DAOBuilder.instance(icFinanceFixedBalanceDAO).searchFilter(new HashMap<String, Object>(){{
            this.put("dqckbh", fixedDraw.getDqckbh());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cFinanceFixedBalance.setAcct_status("99");
        DAOBuilder.instance(icFinanceFixedBalanceDAO).entity(cFinanceFixedBalance).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        iSaveAuditHistory.saveNormalBusiness(cfinanceFixedDraw.getYwlsh(), tokenContext, BusinessSubType.财务_定期支取.getDescription(), "新建");

        fixedDraw.setId(cfinanceFixedDraw.getId());

        if ("1".equals(type)) {
            //TODO 调结算平台
            iFinanceTrader.fixedDraw(cfinanceFixedDraw.getId());
        }

        return fixedDraw;
    }

    @Override
    public boolean modifyFixedDraw(TokenContext tokenContext,String id, FixedDraw fixedDraw, String type) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        if (!"新建".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()) && !"未通过".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep())) return true;

//        cFinanceFixedDraw.setDraw_amt(new BigDecimal(fixedDraw.getDraw_amt()));
        cFinanceFixedDraw.setZqqk(fixedDraw.getZqqk());

        CFinanceManageFinanceExtension cFinanceManageFinanceExtension = cFinanceFixedDraw.getcFinanceManageFinanceExtension();
        cFinanceManageFinanceExtension.setBeizhu(fixedDraw.getBeizhu());
        cFinanceManageFinanceExtension.setCzy(fixedDraw.getCzy());

        execute(tokenContext, cFinanceFixedDraw, type, "1");

        iSaveAuditHistory.saveNormalBusiness(cFinanceFixedDraw.getYwlsh(), tokenContext, BusinessSubType.财务_定期支取.getDescription(), "修改");

        if ("1".equals(type)) {
            //TODO 调结算平台
            iFinanceTrader.fixedDraw(id);
        }

        return false;
    }

    @Override
    public boolean delFixedDraw(TokenContext tokenContext,String id) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        if (!"新建".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()) && !"审核不通过".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()))
            return true;

        DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除活期转到定期失败");
            }
        });

        CFinanceFixedBalance cFinanceFixedBalance = DAOBuilder.instance(icFinanceFixedBalanceDAO).searchFilter(new HashMap<String, Object>(){{
            this.put("dqckbh", cFinanceFixedDraw.getDqckbh());
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        cFinanceFixedBalance.setAcct_status("0");
        DAOBuilder.instance(icFinanceFixedBalanceDAO).entity(cFinanceFixedBalance).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return false;
    }

    public boolean submitFixedDraw(TokenContext tokenContext,String id) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        if (!"新建".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()) && !"审核不通过".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()))
            return true;

        execute(tokenContext, cFinanceFixedDraw, "1", "1");

        iSaveAuditHistory.saveNormalBusiness(cFinanceFixedDraw.getYwlsh(), tokenContext, BusinessSubType.财务_定期支取.getDescription(), "修改");

        //TODO 调结算平台
        iFinanceTrader.fixedDraw(id);

        return false;
    }

    public boolean revokeFixedDraw(TokenContext tokenContext, String id) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        if (!"待审核".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()))
            return true;

        cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("新建");
        DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        return false;
    }

    public boolean updateF2AStep(TokenContext tokenContext, String id, String step) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为"+ id +"的相关信息");
        }

        if (!"定期支取失败".equals(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getStep()))
            return true;

        cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep(step);
        DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (!"定期支取成功".equals(step)) {
            iFixedRecordService.updateFixedRecord(cFinanceFixedDraw.getDqckbh(), "0");
        }

        return false;
    }

    private void execute(TokenContext tokenContext, CFinanceFixedDraw cfinanceFixedDraw, String type, String action) {
        StateMachineUtils.updateState(iStateMachineService, "0".equals(action) ? (type.equals("0") ? Events.通过.getEvent() : Events.提交.getEvent()) : (type.equals("0") ? Events.保存.getEvent() : Events.通过.getEvent()),
                new TaskEntity() {{
                    this.setOperator(tokenContext.getUserInfo().getCZY());
                    this.setStatus(cfinanceFixedDraw.getcFinanceManageFinanceExtension().getStep());
                    this.setTaskId(cfinanceFixedDraw.getYwlsh());
                    if (tokenContext.getRoleList().size() != 0) {
                        this.setRole(tokenContext.getRoleList().get(0)/*角色*/);
                    }
                    this.setSubtype(BusinessSubType.财务_定期支取.getSubType());
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
                        if (FinanceBusinessStatus.待定转活.getName().equals(next))
                            cfinanceFixedDraw.getcFinanceManageFinanceExtension().setDdsj(new Date());
                        cfinanceFixedDraw.getcFinanceManageFinanceExtension().setStep(next);
                        icFinanceFixedDrawDAO.update(cfinanceFixedDraw);
                    }
                });
    }
}
