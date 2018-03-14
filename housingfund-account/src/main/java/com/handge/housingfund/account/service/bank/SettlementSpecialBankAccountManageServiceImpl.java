package com.handge.housingfund.account.service.bank;

import com.handge.housingfund.common.service.account.ISettlementSpecialBankAccountManageService;
import com.handge.housingfund.common.service.account.model.CenterAccountInfo;
import com.handge.housingfund.common.service.account.model.PageRes;
import com.handge.housingfund.common.service.account.model.SettlementSpecialBankAccount;
import com.handge.housingfund.common.service.bank.bean.center.ActivedAccBalanceQueryIn;
import com.handge.housingfund.common.service.bank.bean.center.ActivedAccBalanceQueryOut;
import com.handge.housingfund.common.service.bank.bean.head.CenterHeadIn;
import com.handge.housingfund.common.service.bank.ibank.IBank;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICBankBankInfoDAO;
import com.handge.housingfund.database.dao.ICBankContractDAO;
import com.handge.housingfund.database.dao.IStFinanceSubjectsDAO;
import com.handge.housingfund.database.dao.IStSettlementSpecialBankAccountDAO;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */
@Component
public class SettlementSpecialBankAccountManageServiceImpl implements ISettlementSpecialBankAccountManageService {
    @Autowired
    private IStSettlementSpecialBankAccountDAO specialBankAccountDAO;
    @Autowired
    private ICBankBankInfoDAO icBankBankInfoDAO;
    @Autowired
    private ICBankContractDAO icBankContractDAO;
    @Autowired
    private IStFinanceSubjectsDAO iStFinanceSubjectsDAO;
    @Autowired
    private IBank iBank;

    @Override
    public PageRes<SettlementSpecialBankAccount> getSpecialAccountList(String yhmc, String yhzhhm, String status, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhmc)) filter.put("yhmc", yhmc);
        if (StringUtil.notEmpty(yhzhhm)) filter.put("yhzhhm", yhzhhm);
        if (StringUtil.notEmpty(status) && !"所有".equals(status))
            filter.put("cSettlementSpecialBankAccountExtension.status", status);

        PageRes pageRes = new PageRes();

        List<StSettlementSpecialBankAccount> specialBankAccountList = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).
                pageOption(pageRes, pageSize, pageNo).
                getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageRes<SettlementSpecialBankAccount> bankAccountPageRes = new PageRes<>();
        bankAccountPageRes.setCurrentPage(pageRes.getCurrentPage());
        bankAccountPageRes.setNextPageNo(pageRes.getNextPageNo());
        bankAccountPageRes.setPageCount(pageRes.getPageCount());
        bankAccountPageRes.setPageSize(pageRes.getPageSize());
        bankAccountPageRes.setTotalCount(pageRes.getTotalCount());
        List<StFinanceSubjects> stFinanceSubjects = DAOBuilder.instance(iStFinanceSubjectsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        ArrayList<SettlementSpecialBankAccount> bankAccounts = new ArrayList();
        for (StSettlementSpecialBankAccount specialBankAccount : specialBankAccountList) {
            SettlementSpecialBankAccount settlementSpecialBankAccount = new SettlementSpecialBankAccount();
            settlementSpecialBankAccount.setKHRQ(DateUtil.date2Str(specialBankAccount.getKhrq(), "yyyy-MM-dd"));
            settlementSpecialBankAccount.setXHRQ(DateUtil.date2Str(specialBankAccount.getXhrq(), "yyyy-MM-dd"));
            settlementSpecialBankAccount.setId(specialBankAccount.getId());
            settlementSpecialBankAccount.setKMBH(specialBankAccount.getKmbh());
            settlementSpecialBankAccount.setSFYSY(specialBankAccount.getcSettlementSpecialBankAccountExtension().getSfysy());
            settlementSpecialBankAccount.setStatus(specialBankAccount.getcSettlementSpecialBankAccountExtension().getStatus());
            settlementSpecialBankAccount.setYHDM(specialBankAccount.getYhdm());
            settlementSpecialBankAccount.setYHMC(specialBankAccount.getYhmc());
            settlementSpecialBankAccount.setYHZHHM(specialBankAccount.getYhzhhm());
            settlementSpecialBankAccount.setYHZHMC(specialBankAccount.getYhzhmc());
            settlementSpecialBankAccount.setZHXZ(specialBankAccount.getZhxz());
            settlementSpecialBankAccount.setYHID(specialBankAccount.getcBankContract().getId());
            settlementSpecialBankAccount.setNode(specialBankAccount.getcBankContract().getNode());
            settlementSpecialBankAccount.setKHBH(specialBankAccount.getcBankContract().getKhbh());
            StFinanceSubjects subjects = searchSubject(stFinanceSubjects, specialBankAccount.getKmbh());
            settlementSpecialBankAccount.setKMMC(subjects == null ? "" : subjects.getKmmc());
            bankAccounts.add(settlementSpecialBankAccount);
        }

        bankAccountPageRes.setResults(bankAccounts);
        return bankAccountPageRes;
    }

    private StFinanceSubjects searchSubject(List<StFinanceSubjects> stFinanceSubjects, String KMBH) {
        if (!StringUtil.notEmpty(KMBH)) {
            return null;
        }
        for (StFinanceSubjects subjects : stFinanceSubjects) {
            if (KMBH.equals(subjects.getKmbh())) {
                return subjects;
            }
        }
        return null;
    }

    @Override
    public SettlementSpecialBankAccount getSpecialAccountById(String id) {
        StSettlementSpecialBankAccount specialBankAccount = DAOBuilder.instance(specialBankAccountDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (specialBankAccount == null || specialBankAccount.isDeleted()) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }
        StFinanceSubjects stFinanceSubject = DAOBuilder.instance(iStFinanceSubjectsDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("kmbh", specialBankAccount.getKmbh());
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        SettlementSpecialBankAccount settlementSpecialBankAccount = new SettlementSpecialBankAccount();
        settlementSpecialBankAccount.setKHRQ(DateUtil.date2Str(specialBankAccount.getKhrq(), "yyyy-MM-dd"));
        settlementSpecialBankAccount.setXHRQ(DateUtil.date2Str(specialBankAccount.getXhrq(), "yyyy-MM-dd"));
        settlementSpecialBankAccount.setId(specialBankAccount.getId());
        settlementSpecialBankAccount.setKMBH(specialBankAccount.getKmbh());
        settlementSpecialBankAccount.setKMMC(stFinanceSubject == null ? "" : stFinanceSubject.getKmmc());
        settlementSpecialBankAccount.setSFYSY(specialBankAccount.getcSettlementSpecialBankAccountExtension().getSfysy());
        settlementSpecialBankAccount.setStatus(specialBankAccount.getcSettlementSpecialBankAccountExtension().getStatus());
        settlementSpecialBankAccount.setYHDM(specialBankAccount.getYhdm());
        settlementSpecialBankAccount.setYHMC(specialBankAccount.getYhmc());
        settlementSpecialBankAccount.setYHZHHM(specialBankAccount.getYhzhhm());
        settlementSpecialBankAccount.setYHZHMC(specialBankAccount.getYhzhmc());
        settlementSpecialBankAccount.setZHXZ(specialBankAccount.getZhxz());
        settlementSpecialBankAccount.setYHID(specialBankAccount.getcBankContract().getId());
        settlementSpecialBankAccount.setNode(specialBankAccount.getcBankContract().getNode());
        settlementSpecialBankAccount.setKHBH(specialBankAccount.getcBankContract().getKhbh());

        return settlementSpecialBankAccount;
    }


    @Override
    public SettlementSpecialBankAccount addSpecialAccount(SettlementSpecialBankAccount settlementSpecialBankAccount) {

        StSettlementSpecialBankAccount stSettlementSpecialBankAccount = DAOBuilder.instance(specialBankAccountDAO).searchFilter(new HashMap<String, Object>() {{
            this.put("yhzhhm", settlementSpecialBankAccount.getYHZHHM());
        }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (stSettlementSpecialBankAccount != null) {
            throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "该专户号码已存在");
        } else {
            stSettlementSpecialBankAccount = new StSettlementSpecialBankAccount();
        }
        try {
            stSettlementSpecialBankAccount.setKhrq(DateUtil.str2Date("yyyy-MM-dd", settlementSpecialBankAccount.getKHRQ()));
            if (stSettlementSpecialBankAccount.getKhrq().getTime() > new Date().getTime()) {
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "开户日期不能超过当前日期");
            }
        } catch (ParseException e) {
            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "开户日期格式有误");
        }
        stSettlementSpecialBankAccount.setKmbh(settlementSpecialBankAccount.getKMBH());
        stSettlementSpecialBankAccount.setYhdm(settlementSpecialBankAccount.getYHDM());
        stSettlementSpecialBankAccount.setYhmc(settlementSpecialBankAccount.getYHMC());
        stSettlementSpecialBankAccount.setYhzhhm(settlementSpecialBankAccount.getYHZHHM());
        stSettlementSpecialBankAccount.setYhzhmc(settlementSpecialBankAccount.getYHZHMC());
        stSettlementSpecialBankAccount.setZhxz(settlementSpecialBankAccount.getZHXZ());

        CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).UID(settlementSpecialBankAccount.getYHID()).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankContract == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到此签约银行");
        }
        stSettlementSpecialBankAccount.setcBankContract(cBankContract);

        CSettlementSpecialBankAccountExtension specialBankAccountExtension = new CSettlementSpecialBankAccountExtension();
        specialBankAccountExtension.setStatus("0");
        specialBankAccountExtension.setSfysy(false);

        settlementSpecialBankAccount.setSFYSY(false);

        //查询活期余额,结算平台
        CenterHeadIn centerHeadIn = new CenterHeadIn();
        centerHeadIn.setOperNo("admin");
        centerHeadIn.setCustNo(cBankContract.getKhbh());
        centerHeadIn.setReceiveNode(cBankContract.getNode());

        ActivedAccBalanceQueryIn activedAccBalanceQueryIn = new ActivedAccBalanceQueryIn();
        activedAccBalanceQueryIn.setCenterHeadIn(centerHeadIn);
        activedAccBalanceQueryIn.setAcctNo(settlementSpecialBankAccount.getYHZHHM());
        activedAccBalanceQueryIn.setAcctType("1");
        activedAccBalanceQueryIn.setCurrIden("156");
        activedAccBalanceQueryIn.setCurrNo("1");

        try {
            ActivedAccBalanceQueryOut activedAccBalanceQueryOut = iBank.sendMsg(activedAccBalanceQueryIn);
            if ("0".equals(activedAccBalanceQueryOut.getCenterHeadOut().getTxStatus())) {
                settlementSpecialBankAccount.setSFYSY(true);
                specialBankAccountExtension.setSfysy(true);
            }
        } catch (Exception ignored) {
        }

        stSettlementSpecialBankAccount.setcSettlementSpecialBankAccountExtension(specialBankAccountExtension);

        String id = DAOBuilder.instance(specialBankAccountDAO).entity(stSettlementSpecialBankAccount).saveThenFetchId(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "插入数据库失败");
            }
        });

        settlementSpecialBankAccount.setId(id);
        settlementSpecialBankAccount.setStatus("0");

        return settlementSpecialBankAccount;
    }

    @Override
    public boolean updateSpecialAccount(String id, SettlementSpecialBankAccount settlementSpecialBankAccount) {
        StSettlementSpecialBankAccount stSettlementSpecialBankAccount = DAOBuilder.instance(specialBankAccountDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (stSettlementSpecialBankAccount == null || stSettlementSpecialBankAccount.isDeleted()) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (stSettlementSpecialBankAccount.getcSettlementSpecialBankAccountExtension().getSfysy()) return true;

        stSettlementSpecialBankAccount.setKmbh(settlementSpecialBankAccount.getKMBH());
        stSettlementSpecialBankAccount.setYhdm(settlementSpecialBankAccount.getYHDM());
        stSettlementSpecialBankAccount.setYhmc(settlementSpecialBankAccount.getYHMC());
        stSettlementSpecialBankAccount.setYhzhhm(settlementSpecialBankAccount.getYHZHHM());
        stSettlementSpecialBankAccount.setYhzhmc(settlementSpecialBankAccount.getYHZHMC());
        stSettlementSpecialBankAccount.setZhxz(settlementSpecialBankAccount.getZHXZ());

        CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).UID(settlementSpecialBankAccount.getYHID()).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankContract == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "未找到此签约银行");
        }
        stSettlementSpecialBankAccount.setcBankContract(cBankContract);

        DAOBuilder.instance(specialBankAccountDAO).entity(stSettlementSpecialBankAccount).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败");
            }
        });

        return false;
    }

    @Override
    public boolean delSpecialAccount(String id) {
        StSettlementSpecialBankAccount stSettlementSpecialBankAccount = DAOBuilder.instance(specialBankAccountDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (stSettlementSpecialBankAccount == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }

        if (stSettlementSpecialBankAccount.getcSettlementSpecialBankAccountExtension().getSfysy()) return true;

        DAOBuilder.instance(specialBankAccountDAO).entity(stSettlementSpecialBankAccount).delete(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "删除专户失败");
            }
        });

        return false;
    }

    @Override
    public boolean delSpecialAccounts(ArrayList<String> ids) {
        for (String id : ids) {
            if (delSpecialAccount(id)) return true;
        }
        return false;
    }

    @Override
    public void cancelSpecialAccount(String id) {
        StSettlementSpecialBankAccount stSettlementSpecialBankAccount = DAOBuilder.instance(specialBankAccountDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (stSettlementSpecialBankAccount == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
        }
        if (stSettlementSpecialBankAccount.getXhrq() != null) {
            throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "已销户的专户不能再次销户");
        }

        stSettlementSpecialBankAccount.setXhrq(new Date());
        stSettlementSpecialBankAccount.getcSettlementSpecialBankAccountExtension().setStatus("1");
        stSettlementSpecialBankAccount.getcSettlementSpecialBankAccountExtension().setSfysy(true);

        DAOBuilder.instance(specialBankAccountDAO).entity(stSettlementSpecialBankAccount).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "销户失败");
            }
        });
    }

    @Override
    public ArrayList<CenterAccountInfo> getSpecialAccount(String yhmc) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhmc)) filter.put("bank_name", yhmc);

        CBankBankInfo cBankBankInfo = DAOBuilder.instance(icBankBankInfoDAO).searchFilter(filter).searchOption(SearchOption.FUZZY).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankBankInfo == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有银行名称为" + yhmc + "的相关信息");
        }

        if (!StringUtil.notEmpty(cBankBankInfo.getNode())) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不支持" + yhmc + "办理相关业务");
        }

        filter.clear();
        filter.put("cBankContract.node", cBankBankInfo.getNode());

        List<StSettlementSpecialBankAccount> specialBankAccounts = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).searchOption(SearchOption.REFINED).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });

        if (specialBankAccounts.size() == 0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "不支持" + yhmc + "办理相关业务");
        }

        ArrayList<CenterAccountInfo> centerAccountInfos = new ArrayList<>();
        for (StSettlementSpecialBankAccount specialBankAccount : specialBankAccounts) {
            CenterAccountInfo centerAccountInfo = new CenterAccountInfo();
            centerAccountInfo.setBank_name(specialBankAccount.getYhmc());
            centerAccountInfo.setChgno(cBankBankInfo.getChgno());
            centerAccountInfo.setCode(specialBankAccount.getYhdm());
            centerAccountInfo.setNode(specialBankAccount.getcBankContract().getNode());
            centerAccountInfo.setYHZHHM(specialBankAccount.getYhzhhm());
            centerAccountInfo.setYHZHMC(specialBankAccount.getYhzhmc());
            centerAccountInfo.setZHXZ(specialBankAccount.getZhxz());
            centerAccountInfo.setKHBH(specialBankAccount.getcBankContract().getKhbh());
            centerAccountInfo.setKMBH(specialBankAccount.getKmbh());
            centerAccountInfo.setPLXMBH(specialBankAccount.getcBankContract().getPlxmbh());
            centerAccountInfo.setKHSFSS(specialBankAccount.getcBankContract().getKhsfss());

            centerAccountInfos.add(centerAccountInfo);
        }
        return centerAccountInfos;
    }

    @Override
    public CenterAccountInfo getSpecialAccountByZHHM(String zhhm) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zhhm)) filter.put("yhzhhm", zhhm);

        StSettlementSpecialBankAccount specialBankAccount = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        if (specialBankAccount == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "没有专户为" + zhhm + "的相关信息");
        }

        CenterAccountInfo centerAccountInfo = new CenterAccountInfo();
        centerAccountInfo.setBank_name(specialBankAccount.getYhmc());
        centerAccountInfo.setCode(specialBankAccount.getYhdm());
        centerAccountInfo.setNode(specialBankAccount.getcBankContract().getNode());
        centerAccountInfo.setYHZHHM(specialBankAccount.getYhzhhm());
        centerAccountInfo.setYHZHMC(specialBankAccount.getYhzhmc());
        centerAccountInfo.setZHXZ(specialBankAccount.getZhxz());
        centerAccountInfo.setKHBH(specialBankAccount.getcBankContract().getKhbh());
        centerAccountInfo.setPLXMBH(specialBankAccount.getcBankContract().getPlxmbh());
        centerAccountInfo.setKMBH(specialBankAccount.getKmbh());
        centerAccountInfo.setChgno(specialBankAccount.getcBankContract().getChgno());
        centerAccountInfo.setKHSFSS(specialBankAccount.getcBankContract().getKhsfss());

        return centerAccountInfo;
    }

    public boolean isSpecialAccountByZHHM(String zhhm) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(zhhm)) filter.put("yhzhhm", zhhm);

        StSettlementSpecialBankAccount specialBankAccount = DAOBuilder.instance(specialBankAccountDAO).searchFilter(filter).getObject(
                new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        return specialBankAccount != null;
    }
}
