package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.bank.bean.center.*;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.bean.sys.AccChangeNotice;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.finance.IFinanceTrader;
import com.handge.housingfund.common.service.finance.IFixedRecordService;
import com.handge.housingfund.common.service.finance.IVoucherManagerService;
import com.handge.housingfund.common.service.finance.model.ActivedBalance;
import com.handge.housingfund.common.service.finance.model.FixedRecord;
import com.handge.housingfund.common.service.finance.model.VoucherRes;
import com.handge.housingfund.common.service.finance.model.enums.VoucherBusinessType;
import com.handge.housingfund.common.service.util.*;
import com.handge.housingfund.database.dao.ICFinanceActived2FixedDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedBalanceDAO;
import com.handge.housingfund.database.dao.ICFinanceFixedDrawDAO;
import com.handge.housingfund.database.dao.IStFinanceTimeDepositDAO;
import com.handge.housingfund.database.entities.CFinanceActived2Fixed;
import com.handge.housingfund.database.entities.CFinanceFixedBalance;
import com.handge.housingfund.database.entities.CFinanceFixedDraw;
import com.handge.housingfund.database.entities.StFinanceTimeDeposit;
import com.handge.housingfund.database.utils.DAOBuilder;
import com.handge.housingfund.finance.utils.DepositPeriodTransfer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/9/15.
 */
@SuppressWarnings("Duplicates")
@Component
public class FinanceTrader implements IFinanceTrader {
    private static Logger logger = LogManager.getLogger(FinanceTrader.class);

    @Autowired
    private ICFinanceActived2FixedDAO icFinanceActived2FixedDAO;
    @Autowired
    private ICFinanceFixedDrawDAO icFinanceFixedDrawDAO;
    @Autowired
    private IStFinanceTimeDepositDAO iStFinanceTimeDepositDAO;
    @Autowired
    private ICFinanceFixedBalanceDAO icFinanceFixedBalanceDAO;
    @Autowired
    private ISettlementSpecialBankAccountManageService iSettlementSpecialBankAccountManageService;
    @Autowired
    private IBank iBank;
    @Autowired
    private IVoucherManagerService voucherManagerService;
    @Autowired
    private IFixedRecordService iFixedRecordService;

    // TODO: 2017/10/8 日常财务的办结操作（审核通过时调用），汪雪飞实现 
    @Override
    public void doFinanceCommon(String YWLSH) {

    }

    @Override
    public String actived2FixedNotice(AccChangeNotice accChangeNotice) {
        String ywlsh = accChangeNotice.getAccChangeNoticeFile().getNo();
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到业务流水号为" + ywlsh + "的活期转定期业务");
        }

        String dqckbh = cFinanceActived2Fixed.getBook_no() + cFinanceActived2Fixed.getBook_list_no() + System.currentTimeMillis();

        String czy = cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy();

        accChangeNotice.getAccChangeNoticeFile().setSummary("定期存款存入");
        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", czy,
                czy, "", "管理员",
                VoucherBusinessType.定期存款存入.getCode(), VoucherBusinessType.定期存款存入.getCode(), accChangeNotice, null);

        if (StringUtil.notEmpty(voucherRes.getJZPZH())) {
            //活期转定期入账
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("定期存款成功");
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setBjsj(new Date());
            cFinanceActived2Fixed.setDqckbh(dqckbh);
            cFinanceActived2Fixed.setBook_no(accChangeNotice.getAccChangeNoticeFile().getBookNo());
            cFinanceActived2Fixed.setBook_list_no(Integer.parseInt(accChangeNotice.getAccChangeNoticeFile().getBookListNo()));
            DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            //定期存款明细
            StFinanceTimeDeposit stFinanceTimeDeposit = new StFinanceTimeDeposit();
            stFinanceTimeDeposit.setDqckbh(dqckbh);
            stFinanceTimeDeposit.setZhhm(cFinanceActived2Fixed.getAcct_no());
            stFinanceTimeDeposit.setZhmc(cFinanceActived2Fixed.getAcct_name());
            stFinanceTimeDeposit.setKhyhmc(cFinanceActived2Fixed.getKhyhmc());
            stFinanceTimeDeposit.setLiLv(cFinanceActived2Fixed.getInterest_rate());
            stFinanceTimeDeposit.setBjje(cFinanceActived2Fixed.getAmt());
            BigDecimal ckqx = DepositPeriodTransfer.flagToDays(cFinanceActived2Fixed.getDeposit_period());
            stFinanceTimeDeposit.setCkqx(ckqx);

//            DAOBuilder.instance(iStFinanceTimeDepositDAO).entity(stFinanceTimeDeposit).save(new DAOBuilder.ErrorHandler() {
//                @Override
//                public void error(Exception e) {
//                    throw new ErrorException(e);
//                }
//            });

            //定期余额
            String acctNo = cFinanceActived2Fixed.getAcct_no();
            String khyhmc = cFinanceActived2Fixed.getKhyhmc();
//            getFixedBalance(czy, acctNo, dqckbh, khyhmc, true);

            //测试用-----start
            FixedRecord fixedRecord = new FixedRecord();
            fixedRecord.setDqckbh(dqckbh);
            fixedRecord.setAcct_name(cFinanceActived2Fixed.getAcct_name());
            fixedRecord.setAcct_no(cFinanceActived2Fixed.getAcct_no());
            fixedRecord.setBook_no(cFinanceActived2Fixed.getBook_no());
            fixedRecord.setBook_list_no(cFinanceActived2Fixed.getBook_list_no());
            fixedRecord.setDeposit_period(DepositPeriodTransfer.flagToDays(cFinanceActived2Fixed.getDeposit_period()).toPlainString());
            fixedRecord.setBeg_amt(cFinanceActived2Fixed.getAmt().toPlainString());
            fixedRecord.setDraw_amt(cFinanceActived2Fixed.getAmt().toPlainString());
            fixedRecord.setDeposit_begin_date(DateUtil.date2Str(new Date(), "yyyyMMdd"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            switch (cFinanceActived2Fixed.getDeposit_period()) {
                case "0":
                    calendar.add(Calendar.MONTH, 3);
                    break;
                case "1":
                    calendar.add(Calendar.MONTH, 6);
                    break;
                case "2":
                    calendar.add(Calendar.MONTH, 12);
                    break;
                case "3":
                    calendar.add(Calendar.MONTH, 24);
                    break;
                case "4":
                    calendar.add(Calendar.MONTH, 36);
                    break;
                case "5":
                    calendar.add(Calendar.MONTH, 60);
                    break;
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String endDate = format.format(calendar.getTime());
            fixedRecord.setDeposit_end_date(endDate);
            fixedRecord.setKhyhmc(khyhmc);

            iFixedRecordService.addFixedRecord(fixedRecord);

            cFinanceActived2Fixed.setDeposit_begin_date(new Date());
            try {
                cFinanceActived2Fixed.setDeposit_end_date(DateUtil.str2Date("yyyyMMdd", endDate));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }
            DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    logger.error(LogUtil.getTrace(e));
                }
            });

            //更新定期存款明细存入日期和到期日期
            stFinanceTimeDeposit.setCrrq(new Date());
            try {
                stFinanceTimeDeposit.setDqrq(DateUtil.str2Date("yyyyMMdd", endDate));
            } catch (ParseException e) {
                System.err.println(e.getMessage());
            }

            DAOBuilder.instance(iStFinanceTimeDepositDAO).entity(stFinanceTimeDeposit).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            //测试用-----end
        } else {
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("定期存款失败");
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setBjsj(new Date());
            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setSbyy(voucherRes.getMSG());
            cFinanceActived2Fixed.setDqckbh(dqckbh);
            DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }

        return "Success";
    }

    @Override
    public String fixedDrawNotice(AccChangeNotice accChangeNotice) {
        String ywlsh = accChangeNotice.getAccChangeNoticeFile().getNo();
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("ywlsh", ywlsh);
        }}).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有业务流水号为" + ywlsh + "的定期支取业务");
        }

        String czy = cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy();

        accChangeNotice.getAccChangeNoticeFile().setSummary("定期存款支取");
        VoucherRes voucherRes = voucherManagerService.addVoucher("毕节市住房公积金管理中心", czy,
                czy, "", "管理员",
                VoucherBusinessType.定期存款支取.getCode(), VoucherBusinessType.定期存款支取.getCode(), accChangeNotice, null);

        if (StringUtil.notEmpty(voucherRes.getJZPZH())) {
            //定期支取入账
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("定期支取成功");
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setBjsj(new Date());
            DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            //定期存款明细
            String dqckbh = cFinanceFixedDraw.getDqckbh();
            StFinanceTimeDeposit stFinanceTimeDeposit = DAOBuilder.instance(iStFinanceTimeDepositDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("dqckbh", dqckbh);
            }}).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            if (stFinanceTimeDeposit == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有定期存款编号为" + dqckbh + "的相关信息");
            }

            stFinanceTimeDeposit.setZqqk(cFinanceFixedDraw.getZqqk());
            stFinanceTimeDeposit.setQkrq(new Date());
            stFinanceTimeDeposit.setLxsr(accChangeNotice.getAccChangeNoticeFile().getAmt().subtract(cFinanceFixedDraw.getDraw_amt()));//利息收入

            DAOBuilder.instance(iStFinanceTimeDepositDAO).entity(stFinanceTimeDeposit).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

            //定期余额
            String acctNo = cFinanceFixedDraw.getAcct_no();
            String khyhmc = cFinanceFixedDraw.getKhyhmc();
//            getFixedBalance(czy, acctNo, dqckbh, khyhmc, true);

            //测试用-----start
            iFixedRecordService.updateFixedRecord(dqckbh, "1");
            //测试用-----end
        } else {
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("定期支取失败");
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setBjsj(new Date());
            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setSbyy(voucherRes.getMSG());
            DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
        }

        return "Success";
    }

    @Override
    public void actived2Fixed(String id) {
        CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO).UID(id).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });

        if (cFinanceActived2Fixed == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息,业务不存在");
        }

        CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cFinanceActived2Fixed.getAcct_no());

        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setSendSeqNo(cFinanceActived2Fixed.getYwlsh());
        centerHeadIn.setReceiveNode(centerAccountInfo.getNode());
        centerHeadIn.setOperNo(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getCzy());
        centerHeadIn.setCustNo(centerAccountInfo.getKHBH());

        Actived2FixedIn actived2FixedIn = new Actived2FixedIn();
        actived2FixedIn.setCenterHeadIn(centerHeadIn);
        actived2FixedIn.setActivedAcctName(cFinanceActived2Fixed.getAcct_name());
        actived2FixedIn.setActivedAcctNo(cFinanceActived2Fixed.getAcct_no());
        actived2FixedIn.setDepositPeriod(cFinanceActived2Fixed.getDeposit_period());
        actived2FixedIn.setAmt(cFinanceActived2Fixed.getAmt());
        actived2FixedIn.setExtendDepositType(cFinanceActived2Fixed.getExtend_deposit_type());
        actived2FixedIn.setPartExtendDepositAcctNo(cFinanceActived2Fixed.getPart_extend_deposit_acct_no());
        actived2FixedIn.setCurrNo("156");
        actived2FixedIn.setCurrIden("1");
        actived2FixedIn.setRemark(cFinanceActived2Fixed.getcFinanceManageFinanceExtension().getBeizhu());
//        actived2FixedIn.setFixedAcctName("公积金中心定期账户67"); //测试用,生产下一本通账户不需要填此项
//        actived2FixedIn.setFixedAcctNo("310899999600008158610");//测试用,生产下一本通账户不需要填此项

        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(new SettlementHandler.Success() {
                        @Override
                        public void handle() {
                            System.out.println("---------------------Success");
                        }
                    })
                    .setFail(new SettlementHandler.Fail() {
                        @Override
                        public void handle(String sbyy) {
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("定期存款失败");
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setSbyy(sbyy);

                            DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                            System.out.println("--------------------Fail");
                        }
                    })
                    .setManualProcess(new SettlementHandler.ManualProcess() {
                        @Override
                        public void handle() {
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("定期存款失败");
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setSbyy("状态未知,需人工线下查询");
                            cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setRgcl("1");

                            DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            System.out.println("--------------------Manual Process");
                        }
                    });

            Actived2FixedOut actived2FixedOut = iBank.sendMsg(actived2FixedIn);

            logger.info("-----------------------------------------------------------");
            logger.info(actived2FixedOut);
            logger.info("-----------------------------------------------------------");

            settlementHandler.setCenterHeadOut(actived2FixedOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(new SettlementHandler.SendException() {
                @Override
                public void handle(String sbyy) {
                    cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setStep("定期存款失败");
                    cFinanceActived2Fixed.getcFinanceManageFinanceExtension().setSbyy(sbyy);

                    DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

                    System.out.println("--------------------发送失败");
                }
            }).handleException(e);
        }
    }

    @Override
    public void fixedDraw(String id) {
        CFinanceFixedDraw cFinanceFixedDraw = DAOBuilder.instance(icFinanceFixedDrawDAO).UID(id).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        if (cFinanceFixedDraw == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息,业务不存在");
        }

        CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(cFinanceFixedDraw.getAcct_no());

        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setSendSeqNo(cFinanceFixedDraw.getYwlsh());
        centerHeadIn.setReceiveNode(centerAccountInfo.getNode());
        centerHeadIn.setOperNo(cFinanceFixedDraw.getcFinanceManageFinanceExtension().getCzy());
        centerHeadIn.setCustNo(centerAccountInfo.getKHBH());

        FixedDrawIn fixedDrawIn = new FixedDrawIn();
        fixedDrawIn.setCenterHeadIn(centerHeadIn);
        fixedDrawIn.setBookListNo(cFinanceFixedDraw.getBook_list_no());
        fixedDrawIn.setBookNo(cFinanceFixedDraw.getBook_no());
        fixedDrawIn.setCurrNo("156");
        fixedDrawIn.setCurrIden("1");
        fixedDrawIn.setDepositBeginDate(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_begin_date(), "yyyy-MM-dd"));
        fixedDrawIn.setDepositEndDate(DateUtil.date2Str(cFinanceFixedDraw.getDeposit_end_date(), "yyyy-MM-dd"));
        fixedDrawIn.setDepositPeriod(cFinanceFixedDraw.getDeposit_period());
        fixedDrawIn.setDrawAmt(cFinanceFixedDraw.getDraw_amt());
        fixedDrawIn.setFixedAcctName(cFinanceFixedDraw.getAcct_name());
        fixedDrawIn.setFixedAcctNo(cFinanceFixedDraw.getAcct_no());
//        fixedDrawIn.setActivedAcctName("公积金中心结算账户67");//测试用,生产下一本通账户不需要填此项
//        fixedDrawIn.setActivedAcctNo("310899991010008580716");//测试用,生产下一本通账户不需要填此项

        SettlementHandler settlementHandler = SettlementHandler.instance(iBank);
        try {
            settlementHandler.setCenterHeadIn(centerHeadIn)
                    .setSuccess(new SettlementHandler.Success() {
                        @Override
                        public void handle() {
                            System.out.println("Success");
                        }
                    })
                    .setFail(new SettlementHandler.Fail() {
                        @Override
                        public void handle(String sbyy) {
                            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("定期支取失败");
                            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setSbyy(sbyy);

                            DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                            iFixedRecordService.updateFixedRecord(cFinanceFixedDraw.getDqckbh(), "0");

                            System.out.println("Fail");
                        }
                    })
                    .setManualProcess(new SettlementHandler.ManualProcess() {
                        @Override
                        public void handle() {
                            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("定期支取失败");
                            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setSbyy("状态未知,需人工线下查询");
                            cFinanceFixedDraw.getcFinanceManageFinanceExtension().setRgcl("1");

                            DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });
                            System.out.println("Manual Process");
                        }
                    });

            FixedDrawOut fixedDrawOut = iBank.sendMsg(fixedDrawIn);
            logger.info("-----------------------------------------------------------");
            logger.info(fixedDrawOut);
            logger.info("-----------------------------------------------------------");

            settlementHandler.setCenterHeadOut(fixedDrawOut.getCenterHeadOut()).handle();
        } catch (Exception e) {
            settlementHandler.setSendException(new SettlementHandler.SendException() {
                @Override
                public void handle(String sbyy) {
                    cFinanceFixedDraw.getcFinanceManageFinanceExtension().setStep("定期支取失败");
                    cFinanceFixedDraw.getcFinanceManageFinanceExtension().setSbyy(sbyy);

                    DAOBuilder.instance(icFinanceFixedDrawDAO).entity(cFinanceFixedDraw).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

                    iFixedRecordService.updateFixedRecord(cFinanceFixedDraw.getDqckbh(), "0");

                    System.out.println("发送失败");
                }
            }).handleException(e);
        }
    }

    /**
     * 通过结算平台获取定期余额
     *
     * @param czy    操作员
     * @param acctNo 定期账号
     * @param dqckbh 定期存款编号
     * @param khyhmc 客户银行名称
     */
    public FixedAccBalanceQueryOut getFixedBalance(String czy, String acctNo, String dqckbh, String khyhmc, boolean isSave) {

        CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(acctNo);
        logger.info(centerAccountInfo);

        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setReceiveNode(centerAccountInfo.getNode());
        centerHeadIn.setOperNo(czy);
        centerHeadIn.setCustNo(centerAccountInfo.getKHBH());

        FixedAccBalanceQueryIn fixedAccBalanceQueryIn = new FixedAccBalanceQueryIn();
        fixedAccBalanceQueryIn.setCenterHeadIn(centerHeadIn);
        fixedAccBalanceQueryIn.setAcctNo(acctNo);
        fixedAccBalanceQueryIn.setFixedType("0");

        FixedAccBalanceQueryOut fixedAccBalanceQueryOut = null;
        try {
            fixedAccBalanceQueryOut = iBank.sendMsg(fixedAccBalanceQueryIn);
            logger.info("-----------------------------------------------------------");
            logger.info(fixedAccBalanceQueryOut);
            logger.info("-----------------------------------------------------------");
            if (isSave && "0".equals(fixedAccBalanceQueryOut.getCenterHeadOut().getTxStatus())) {

                for (BDC122Summary bdc122Summary : fixedAccBalanceQueryOut.getSUMMARY()) {
                    CFinanceFixedBalance cFinanceFixedBalance = DAOBuilder.instance(icFinanceFixedBalanceDAO)
                            .searchFilter(new HashMap<String, Object>() {{
                                this.put("acct_no", acctNo);
                                this.put("book_no", bdc122Summary.getBookNo());
                                this.put("book_list_no", bdc122Summary.getBookListNo());
                            }}).getObject(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                    CFinanceActived2Fixed cFinanceActived2Fixed = DAOBuilder.instance(icFinanceActived2FixedDAO)
                            .searchFilter(new HashMap<String, Object>() {{
                                this.put("acct_no", acctNo);
                                this.put("book_no", bdc122Summary.getBookNo());
                                this.put("book_list_no", bdc122Summary.getBookListNo());
                            }}).getObject(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                    if (cFinanceActived2Fixed != null && cFinanceActived2Fixed.getDeposit_end_date() != null && cFinanceActived2Fixed.getDeposit_end_date().getTime() < new Date().getTime()) {
                        cFinanceActived2Fixed.setDeposit_begin_date(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getBegDate()));
                        cFinanceActived2Fixed.setDeposit_end_date(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getDepositEndDate()));
                        DAOBuilder.instance(icFinanceActived2FixedDAO).entity(cFinanceActived2Fixed).save(new DAOBuilder.ErrorHandler() {
                            @Override
                            public void error(Exception e) {
                                logger.error(LogUtil.getTrace(e));
                            }
                        });
                    }

                    //新增定期
                    if (cFinanceFixedBalance == null) {
                        cFinanceFixedBalance = new CFinanceFixedBalance();
                        cFinanceFixedBalance.setDqckbh(dqckbh);
                        cFinanceFixedBalance.setAcct_no(acctNo);
                        cFinanceFixedBalance.setBook_list_no(bdc122Summary.getBookListNo());
                        cFinanceFixedBalance.setBook_no(bdc122Summary.getBookNo());
                        cFinanceFixedBalance.setBeg_amt(bdc122Summary.getBegAmt());
                        cFinanceFixedBalance.setDeposit_begin_date(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getBegDate()));
                        cFinanceFixedBalance.setDeposit_end_date(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getDepositEndDate()));
                        cFinanceFixedBalance.setDeposit_period(bdc122Summary.getDepositPeriod());
                        cFinanceFixedBalance.setKhyhmc(khyhmc);
                    }

                    //更新定期
                    cFinanceFixedBalance.setAcct_status(bdc122Summary.getAcctStatus());
                    cFinanceFixedBalance.setDraw_amt(bdc122Summary.getDrawAmt());
                    cFinanceFixedBalance.setFreeze_type(bdc122Summary.getFreezeType());
                    cFinanceFixedBalance.setInterest(bdc122Summary.getInterest());
                    cFinanceFixedBalance.setInterest_rate(bdc122Summary.getInterestRate());
                    cFinanceFixedBalance.setLoss_flag(bdc122Summary.getLossFlag());

                    DAOBuilder.instance(icFinanceFixedBalanceDAO).entity(cFinanceFixedBalance).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });

                    //更新定期存款明细存入日期和到期日期
                    HashMap<String, Object> filter = new HashMap<>();
                    filter.put("dqckbh", cFinanceFixedBalance.getDqckbh());
                    StFinanceTimeDeposit stFinanceTimeDeposit = DAOBuilder.instance(iStFinanceTimeDepositDAO)
                            .searchFilter(filter)
                            .getObject(new DAOBuilder.ErrorHandler() {
                                @Override
                                public void error(Exception e) {
                                    throw new ErrorException(e);
                                }
                            });

                    if (stFinanceTimeDeposit == null) {
                        throw new ErrorException(ReturnEnumeration.Data_MISS, "没有定期存款编号为" + dqckbh + "的相关信息");
                    }

                    stFinanceTimeDeposit.setCrrq(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getBegDate()));
                    stFinanceTimeDeposit.setDqrq(DateUtil.str2Date("yyyyMMdd", bdc122Summary.getDepositEndDate()));

                    DAOBuilder.instance(iStFinanceTimeDepositDAO).entity(stFinanceTimeDeposit).save(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            throw new ErrorException(e);
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return fixedAccBalanceQueryOut;
    }

    @Override
    public ActivedBalance getActivedBalance(String zhhm) {
        try {
            CenterAccountInfo centerAccountInfo = iSettlementSpecialBankAccountManageService.getSpecialAccountByZHHM(zhhm);
            CenterHeadIn centerHeadIn = new CenterHeadIn();
            centerHeadIn.setOperNo("admin");
            centerHeadIn.setCustNo(centerAccountInfo.getKHBH());
            centerHeadIn.setReceiveNode(centerAccountInfo.getNode());

            ActivedAccBalanceQueryIn activedAccBalanceQueryIn = new ActivedAccBalanceQueryIn();
            activedAccBalanceQueryIn.setCenterHeadIn(centerHeadIn);
            activedAccBalanceQueryIn.setAcctNo(centerAccountInfo.getYHZHHM());
            activedAccBalanceQueryIn.setAcctType("1");
            activedAccBalanceQueryIn.setCurrIden("156");
            activedAccBalanceQueryIn.setCurrNo("1");

            ActivedAccBalanceQueryOut activedAccBalanceQueryOut = iBank.sendMsg(activedAccBalanceQueryIn);
            logger.info("-----------------------------------------------------------");
            logger.info(activedAccBalanceQueryOut);
            logger.info("-----------------------------------------------------------");
            ActivedBalance activedBalance = new ActivedBalance();
            activedBalance.setAcctName(activedAccBalanceQueryOut.getAcctName());
            activedBalance.setAcctNo(activedAccBalanceQueryOut.getAcctNo());
            activedBalance.setAcctOverBal(activedAccBalanceQueryOut.getAcctOverBal() != null ? activedAccBalanceQueryOut.getAcctOverBal().toPlainString() : null);
            activedBalance.setAcctBal(activedAccBalanceQueryOut.getAcctBal() != null ? activedAccBalanceQueryOut.getAcctBal().toPlainString() : null);
            activedBalance.setAcctRestBal(activedAccBalanceQueryOut.getAcctRestBal() != null ? activedAccBalanceQueryOut.getAcctRestBal().toPlainString() : null);
            activedBalance.setAcctStatus(activedAccBalanceQueryOut.getAcctStatus());
            return activedBalance;
        } catch (Exception e) {
            logger.error(e.getMessage());
            ActivedBalance activedBalance = new ActivedBalance();
            activedBalance.setAcctBal("-");
            return activedBalance;
        }

        //测试用
//        ActivedBalance activedBalance = new ActivedBalance();
//        activedBalance.setAcctBal("10000000.00");
//        return activedBalance;
    }
}
