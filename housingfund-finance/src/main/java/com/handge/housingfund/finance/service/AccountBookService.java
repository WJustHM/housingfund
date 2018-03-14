package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.finance.IAccountBookService;
import com.handge.housingfund.common.service.finance.model.AccountBookModel;
import com.handge.housingfund.common.service.finance.model.AccountPeriod;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.ICFinanceAccountPeriodDAO;
import com.handge.housingfund.database.dao.ICFinanceAccountSetsDAO;
import com.handge.housingfund.database.entities.CFinanceAccountPeriod;
import com.handge.housingfund.database.entities.CFinanceAccountSets;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */
@Component
public class AccountBookService implements IAccountBookService {

    @Autowired
    ICFinanceAccountSetsDAO icFinanceAccountSetsDAO;
    @Autowired
    ICFinanceAccountPeriodDAO icFinanceAccountPeriodDAO;

    public AccountBookModel getAccountBookList() {
        List<CFinanceAccountSets> cFinanceAccountSets = DAOBuilder.instance(icFinanceAccountSetsDAO).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceAccountSets == null||cFinanceAccountSets.size()<=0) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账套不存在");
        }

        AccountBookModel accountBookModel = new AccountBookModel();
        accountBookModel.setJZR(cFinanceAccountSets.get(0).getJzr());
        accountBookModel.setJZRQ(DateUtil.date2Str(cFinanceAccountSets.get(0).getJzrq(), "yyyy-MM-dd"));
        accountBookModel.setKJZG(cFinanceAccountSets.get(0).getKjzg());
        accountBookModel.setQYQJ(cFinanceAccountSets.get(0).getQyqj());
        accountBookModel.setZTBZ(cFinanceAccountSets.get(0).getZtbz());
        accountBookModel.setZTMC(cFinanceAccountSets.get(0).getZtmc());
        accountBookModel.setZTQYRQ(DateUtil.date2Str(cFinanceAccountSets.get(0).getZtqyrq(), "yyyy-MM-dd"));

        return accountBookModel;
    }

    @Override
    public void updateAccountBook(AccountBookModel accountBookModel) {
        CFinanceAccountSets cFinanceAccountSets = DAOBuilder.instance(icFinanceAccountSetsDAO).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });
        if (cFinanceAccountSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "要更新的账套不存在");
        }
        cFinanceAccountSets.setKjzg(accountBookModel.getKJZG());
        cFinanceAccountSets.setZtbz(accountBookModel.getZTBZ());

        DAOBuilder.instance(icFinanceAccountSetsDAO).entity(cFinanceAccountSets).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新数据库失败");
            }
        });
    }

    @Override
    public ArrayList<AccountPeriod> getAccountPeriodList(String KJND) {
        HashMap<String, Object> filter = new HashMap<>();
        filter.put("kjnd", KJND);

        List<CFinanceAccountPeriod> cFinanceAccountPeriodList = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        ArrayList<AccountPeriod> accountPeriods = new ArrayList<>();

        for (CFinanceAccountPeriod cFinanceAccountPeriod : cFinanceAccountPeriodList) {
            AccountPeriod accountPeriod = new AccountPeriod();
            accountPeriod.setId(cFinanceAccountPeriod.getId());
            accountPeriod.setJIEZRQ(DateUtil.date2Str(cFinanceAccountPeriod.getJiezrq(), "yyyy-MM-dd"));
            accountPeriod.setJZR(cFinanceAccountPeriod.getJzr());
            accountPeriod.setJZRQ(DateUtil.date2Str(cFinanceAccountPeriod.getJzrq(), "yyyy-MM-dd"));
            accountPeriod.setKJND(cFinanceAccountPeriod.getKjnd());
            accountPeriod.setKJQJ(cFinanceAccountPeriod.getKjqj());
            accountPeriod.setQSRQ(DateUtil.date2Str(cFinanceAccountPeriod.getQsrq(), "yyyy-MM-dd"));
            accountPeriod.setSFJS(cFinanceAccountPeriod.isSfjs());

            accountPeriods.add(accountPeriod);
        }
        return accountPeriods;
    }

    @Override
    public ArrayList<AccountPeriod> addAccountPeriod() {
        CFinanceAccountSets cFinanceAccountSets = DAOBuilder.instance(icFinanceAccountSetsDAO).getObject(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
            }
        });

        if (cFinanceAccountSets == null) {
            throw new ErrorException(ReturnEnumeration.Data_MISS, "账套不存在");
        }

        try {
            int year = DateUtil.getYear(DateUtil.date2Str(new Date()));
            String KJND = String.valueOf(year);
            HashMap<String, Object> filter = new HashMap<>();
            filter.put("kjnd", KJND);

            List<CFinanceAccountPeriod> cFinanceAccountPeriodList = DAOBuilder.instance(icFinanceAccountPeriodDAO).searchFilter(filter).getList(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                }
            });

            if (cFinanceAccountPeriodList.size() > 0)
                throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "此会计年度已经存在");

            ArrayList<AccountPeriod> accountPeriods = new ArrayList<>();


            String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            HashMap<String, String[]> dayCount = new HashMap<>();
            dayCount.put("01", new String[]{"01", "31"});
            dayCount.put("02", new String[]{"01", "28", "29"});
            dayCount.put("03", new String[]{"01", "31"});
            dayCount.put("04", new String[]{"01", "30"});
            dayCount.put("05", new String[]{"01", "31"});
            dayCount.put("06", new String[]{"01", "30"});
            dayCount.put("07", new String[]{"01", "31"});
            dayCount.put("08", new String[]{"01", "31"});
            dayCount.put("09", new String[]{"01", "30"});
            dayCount.put("10", new String[]{"01", "31"});
            dayCount.put("11", new String[]{"01", "30"});
            dayCount.put("12", new String[]{"01", "31"});

            for (String m : month) {
                CFinanceAccountPeriod cFinanceAccountPeriod = new CFinanceAccountPeriod();
                cFinanceAccountPeriod.setKjnd(KJND);
                cFinanceAccountPeriod.setKjqj(KJND + m);
                cFinanceAccountPeriod.setQsrq(DateUtil.str2Date("yyyy-MM-dd", KJND + "-" + m + "-" + dayCount.get(m)[0]));
                cFinanceAccountPeriod.setJiezrq(DateUtil.str2Date("yyyy-MM-dd", KJND + "-" + m + "-" + (("02".equals(m) && (year % 4 == 0 && year % 100 != 0) || year % 400 == 0) ? dayCount.get(m)[2] : dayCount.get(m)[1])));
                cFinanceAccountPeriod.setcFinanceAccountSets(cFinanceAccountSets);

                String id = DAOBuilder.instance(icFinanceAccountPeriodDAO).entity(cFinanceAccountPeriod).saveThenFetchId(new DAOBuilder.ErrorHandler() {
                    @Override
                    public void error(Exception e) {
                        throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "插入数据库失败");
                    }
                });

                AccountPeriod accountPeriod = new AccountPeriod();
                accountPeriod.setJIEZRQ(DateUtil.date2Str(cFinanceAccountPeriod.getJiezrq(), "yyyy-MM-dd"));
                accountPeriod.setKJND(cFinanceAccountPeriod.getKjnd());
                accountPeriod.setKJQJ(cFinanceAccountPeriod.getKjqj());
                accountPeriod.setQSRQ(DateUtil.date2Str(cFinanceAccountPeriod.getQsrq(), "yyyy-MM-dd"));
                accountPeriod.setId(id);

                accountPeriods.add(accountPeriod);
            }
            return accountPeriods;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }

    }

    @Override
    public void putAccountPeriodSettle(String id, String jzr, String jzrq) {
        try {
            CFinanceAccountPeriod cFinanceAccountPeriod = DAOBuilder.instance(icFinanceAccountPeriodDAO).UID(id).getObject(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "查询数据库失败");
                }
            });

            if (cFinanceAccountPeriod == null) {
                throw new ErrorException(ReturnEnumeration.Data_MISS, "没有id为" + id + "的相关信息");
            }

            cFinanceAccountPeriod.setJzr(jzr);


            cFinanceAccountPeriod.setJzrq(DateUtil.str2Date("yyyy-MM-dd", jzrq));

            cFinanceAccountPeriod.setSfjs(true);

            DAOBuilder.instance(icFinanceAccountPeriodDAO).entity(cFinanceAccountPeriod).save(new DAOBuilder.ErrorHandler() {
                @Override
                public void error(Exception e) {
                    throw new ErrorException(ReturnEnumeration.Program_UNKNOW_ERROR, "更新会计期间失败");
                }
            });
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            throw new ErrorException(e);
        }
    }
}
