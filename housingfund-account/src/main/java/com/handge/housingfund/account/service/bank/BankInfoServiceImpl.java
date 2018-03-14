package com.handge.housingfund.account.service.bank;

import com.handge.housingfund.common.service.account.IBankInfoService;
import com.handge.housingfund.common.service.account.model.*;
import com.handge.housingfund.common.service.loan.model.DelList;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICBankBankInfoDAO;
import com.handge.housingfund.database.dao.ICBankContractDAO;
import com.handge.housingfund.database.entities.CBankBankInfo;
import com.handge.housingfund.database.entities.CBankContract;
import com.handge.housingfund.database.entities.StSettlementSpecialBankAccount;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/6.
 */
@Component
public class BankInfoServiceImpl implements IBankInfoService {
    @Autowired
    private ICBankBankInfoDAO icBankBankInfoDAO;

    @Autowired
    private ICBankContractDAO icBankContractDAO;

    @Override
    public PageRes<BankInfoModel> getBankInfoList(String yhmc, String code, int pageNo, int pageSize) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("area_code", "7090");
        if (StringUtil.notEmpty(yhmc)) filter.put("bank_name", yhmc);
        if (StringUtil.notEmpty(code)) filter.put("code", code);

        PageRes pageRes = new PageRes();

        List<CBankBankInfo> cBankBankInfoList = DAOBuilder.instance(icBankBankInfoDAO).searchFilter(filter).searchOption(SearchOption.FUZZY)
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                    }
                });

        PageRes<BankInfoModel> bankInfoModelPageRes = new PageRes<>();
        bankInfoModelPageRes.setCurrentPage(pageRes.getCurrentPage());
        bankInfoModelPageRes.setNextPageNo(pageRes.getNextPageNo());
        bankInfoModelPageRes.setPageCount(pageRes.getPageCount());
        bankInfoModelPageRes.setPageSize(pageRes.getPageSize());
        bankInfoModelPageRes.setTotalCount(pageRes.getTotalCount());

        ArrayList<BankInfoModel> bankInfoModelArrayList = new ArrayList<>();

        for (CBankBankInfo cBankBankInfo : cBankBankInfoList) {
            BankInfoModel bankInfoModel = new BankInfoModel();
            bankInfoModel.setBank_name(cBankBankInfo.getBank_name());
            bankInfoModel.setChgno(cBankBankInfo.getChgno());
            bankInfoModel.setCode(cBankBankInfo.getCode());
            bankInfoModel.setId(cBankBankInfo.getId());
            bankInfoModel.setNode(cBankBankInfo.getNode());

            bankInfoModelArrayList.add(bankInfoModel);
        }

        bankInfoModelPageRes.setResults(bankInfoModelArrayList);
        return bankInfoModelPageRes;
    }

    public BankInfoModel getBankInfo(String yhmc) {
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(yhmc)) filter.put("bank_name", yhmc);

        CBankBankInfo cBankBankInfo = DAOBuilder.instance(icBankBankInfoDAO).searchFilter(filter).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cBankBankInfo == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "开户银行名称有误,请更正");
        }

        BankInfoModel bankInfoModel = new BankInfoModel();
        bankInfoModel.setBank_name(cBankBankInfo.getBank_name());
        bankInfoModel.setChgno(cBankBankInfo.getChgno());
        bankInfoModel.setCode(cBankBankInfo.getCode());
        bankInfoModel.setId(cBankBankInfo.getId());
        bankInfoModel.setNode(cBankBankInfo.getNode());

        return bankInfoModel;
    }

    @Override
    public PageRes<BankContactList> getBankContactList(String YHMC, int pageNo, int pageSize) {
        PageRes<BankContactList> res = new PageRes<>();
        HashMap<String, Object> filter = new HashMap<>();
        if (StringUtil.notEmpty(YHMC)) {
            filter.put("yhmc", YHMC);
        }
        PageRes pageRes = new PageRes();
        List<CBankContract> cBankContracts = DAOBuilder.instance(icBankContractDAO).searchFilter(filter).searchOption(SearchOption.FUZZY)
                .pageOption(pageRes, pageSize, pageNo).getList(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(e);
                    }
                });
        res.setCurrentPage(pageRes.getCurrentPage());
        res.setNextPageNo(pageRes.getNextPageNo());
        res.setPageCount(pageRes.getPageCount());
        res.setPageSize(pageRes.getPageSize());
        res.setTotalCount(pageRes.getTotalCount());

        ArrayList<BankContactList> bankContactLists = new ArrayList<>();
        for (CBankContract cBankContract : cBankContracts) {
            BankContactList bankContactList = new BankContactList();
            bankContactList.setId(cBankContract.getId());
            bankContactList.setLXDH(cBankContract.getLxdh());
            bankContactList.setLXR(cBankContract.getLxr());
            bankContactList.setNode(cBankContract.getNode());
            bankContactList.setWDDZ(cBankContract.getWddz());
            bankContactList.setXDCKJE(String.valueOf(cBankContract.getXdckje()));
            bankContactList.setYHDM(cBankContract.getYhdm());
            bankContactList.setYHMC(cBankContract.getYhmc());
            bankContactList.setChgno(cBankContract.getChgno());
            bankContactList.setPLXMBH(cBankContract.getPlxmbh());
            bankContactList.setKHSFSS(cBankContract.getKhsfss());
            List<StSettlementSpecialBankAccount> list = cBankContract.getStSettlementSpecialBankAccounts();
            if (list == null || list.size() <= 0) {
                bankContactList.setCanDel(true);
                bankContactList.setCanRewrite(true);
            } else {
                bankContactList.setCanDel(false);
                bankContactList.setCanRewrite(false);
            }
            bankContactLists.add(bankContactList);
        }
        res.setResults(bankContactLists);
        return res;
    }

    @Override
    public Success addBankContact(BankContactReq bankContactReq) {
        try {
            CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).searchFilter(new HashMap<String, Object>() {{
                this.put("yhmc", bankContactReq.getYHMC());
            }}).searchOption(SearchOption.REFINED).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cBankContract != null) {
                throw new ErrorException(ReturnEnumeration.Data_Already_Eeist, "该银行已经签约");
            } else {
                cBankContract = new CBankContract();
            }
            cBankContract.setBeizhu(bankContactReq.getBeizhu());
            cBankContract.setKhbh(bankContactReq.getKHBH());
            cBankContract.setLxdh(bankContactReq.getLXDH());
            cBankContract.setLxr(bankContactReq.getLXR());
            cBankContract.setNode(bankContactReq.getNode());
            cBankContract.setStSettlementSpecialBankAccounts(new ArrayList<>());
            cBankContract.setWddz(bankContactReq.getWDDZ());
            cBankContract.setXdckje(new BigDecimal(bankContactReq.getXDCKJE()));
            cBankContract.setYhdm(bankContactReq.getYHDM());
            cBankContract.setYhmc(bankContactReq.getYHMC());
            cBankContract.setChgno(bankContactReq.getChgno());
            cBankContract.setPlxmbh(bankContactReq.getPLXMBH());
            cBankContract.setKhsfss(bankContactReq.isKHSFSS());
            String id = DAOBuilder.instance(icBankContractDAO).entity(cBankContract).saveThenFetchId(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            Success res = new Success();
            res.setId(id);
            res.setState("Success");
            return res;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public BankContactRes getBankContactDetail(String id) {
        CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cBankContract != null) {
            BankContactRes res = new BankContactRes();
            res.setBeizhu(cBankContract.getBeizhu());
            res.setId(cBankContract.getId());
            res.setKHBH(cBankContract.getKhbh());
            res.setLXDH(cBankContract.getLxdh());
            res.setLXR(cBankContract.getLxr());
            res.setNode(cBankContract.getNode());
            res.setWDDZ(cBankContract.getWddz());
            res.setXDCKJE(String.valueOf(cBankContract.getXdckje()));
            res.setYHDM(cBankContract.getYhdm());
            res.setYHMC(cBankContract.getYhmc());
            res.setChgno(cBankContract.getChgno());
            res.setPLXMBH(cBankContract.getPlxmbh());
            res.setKHSFSS(cBankContract.getKhsfss());
            return res;
        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "签约银行不存在");
        }
    }

    @Override
    public Success putBankContact(String id, BankContactReq bankContactReq) {
        CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
        if (cBankContract != null) {
            List<StSettlementSpecialBankAccount> list = cBankContract.getStSettlementSpecialBankAccounts();
            if (list == null || list.size() <= 0) {
                cBankContract.setNode(bankContactReq.getNode());
                cBankContract.setStSettlementSpecialBankAccounts(new ArrayList<>());
                cBankContract.setYhdm(bankContactReq.getYHDM());
                cBankContract.setYhmc(bankContactReq.getYHMC());
                cBankContract.setChgno(bankContactReq.getChgno());
            }
            cBankContract.setKhbh(bankContactReq.getKHBH());
            cBankContract.setPlxmbh(bankContactReq.getPLXMBH());
            cBankContract.setBeizhu(bankContactReq.getBeizhu());
            cBankContract.setLxdh(bankContactReq.getLXDH());
            cBankContract.setLxr(bankContactReq.getLXR());
            cBankContract.setWddz(bankContactReq.getWDDZ());
            cBankContract.setXdckje(new BigDecimal(bankContactReq.getXDCKJE()));
            cBankContract.setKhsfss(bankContactReq.isKHSFSS());
            DAOBuilder.instance(icBankContractDAO).entity(cBankContract).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });

        } else {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "签约银行不存在");
        }
        Success res = new Success();
        res.setId(id);
        res.setState("Success");
        return res;
    }

    @Override
    public List<Delres> delBankContact(DelList delList) {
        List<Delres> delres = new ArrayList<>();
        for (String id : delList.getDellist()) {
            Delres res = new Delres();
            res.setId(id);
            res.setRes(true);
            CBankContract cBankContract = DAOBuilder.instance(icBankContractDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(e);
                }
            });
            if (cBankContract != null) {
                List<StSettlementSpecialBankAccount> list = cBankContract.getStSettlementSpecialBankAccounts();
                if (list == null || list.size() <= 0) {
                    DAOBuilder.instance(icBankContractDAO).entity(cBankContract).delete(new DAOBuilder.ErrorHandler() {
                        @Override
                        public void error(Exception e) {
                            res.setRes(false);
                        }
                    });
                } else {
                    res.setRes(false);
                }
            } else {
                res.setRes(false);
            }
            delres.add(res);
        }
        return delres;
    }
}
