package com.handge.housingfund.database.dao;

import com.handge.housingfund.common.service.loan.model.BackBasicInfomation;
import com.handge.housingfund.common.service.loan.model.SMSBackBasicInfomation;
import com.handge.housingfund.database.entities.CLoanHousingPersonInformationBasic;
import com.handge.housingfund.database.entities.StHousingPersonalAccount;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ICLoanHousingPersonInformationBasicDAO extends IBaseDAO<CLoanHousingPersonInformationBasic> {
    BigDecimal getPersonLoanBalanceLess5Y(String CXNY);

    BigDecimal getPersonLoanBalanceMore5Y(String CXNY);

    CLoanHousingPersonInformationBasic getByDKZH(String DKZH);
    List<CLoanHousingPersonInformationBasic> getLoan();

    void batchSubmit(List<StHousingPersonalAccount> personalAccount);

    void batchUpdate(List<CLoanHousingPersonInformationBasic> basic);

    List<Object[]> getLoan2();

    public List<CLoanHousingPersonInformationBasic> getLoanDKZHZT(String yhdm);

    public void updateLoan(String id,BigDecimal dqyhje,BigDecimal dqyhbj,BigDecimal dqyhlx,BigDecimal dqjhhkje,BigDecimal dqjhghlx,BigDecimal dqjhghbj);

    void flush();

    ArrayList<BackBasicInfomation> repamentNor(String yhdm, Date kkyf, Date overTime);

    void  getInfo();

    void updateYhqs(BigInteger yhqs,String dkzh);

    void updateAccountStatus(String dkzh);

    boolean getByDkzh(String dkzh,String zjhm);

    List<CLoanHousingPersonInformationBasic> getSFDKData(String jkrzjhm);

    BigInteger accountOverCount(Date etime);

    ArrayList<SMSBackBasicInfomation> SMSrepamentNor();

    Object[] getLoanAccountMoneyCount(String DKZH, String JKRXM, String status, String DKFXDJ, String YWWD, String SWTYH, String ZJHM);
}
