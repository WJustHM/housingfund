package com.handge.housingfund.database.dao.impl;

import com.handge.housingfund.common.service.loan.enums.LoanAccountType;
import com.handge.housingfund.common.service.loan.enums.LoanRiskStatus;
import com.handge.housingfund.common.service.loan.model.BackBasicInfomation;
import com.handge.housingfund.common.service.loan.model.SMSBackBasicInfomation;
import com.handge.housingfund.common.service.util.DateUtil;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;
import com.handge.housingfund.database.dao.ICAccountEmployeeDAO;
import com.handge.housingfund.database.dao.ICLoanHousingPersonInformationBasicDAO;
import com.handge.housingfund.database.entities.CLoanHousingPersonInformationBasic;
import com.handge.housingfund.database.entities.StHousingPersonalAccount;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("JpaQlInspection")
@Repository
public class CLoanHousingPersonInformationBasicDAO extends BaseDAO<CLoanHousingPersonInformationBasic> implements ICLoanHousingPersonInformationBasicDAO {

    @Autowired
    ICAccountEmployeeDAO iAccountEmployeeDAO;

    public BigDecimal getPersonLoanBalanceLess5Y(String CXNY) {
        Date cxny = null;
        try {
            cxny = DateUtil.str2Date("yyyy-MM", CXNY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cxny);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            System.out.println(format.format(calendar.getTime()));
            cxny = format.parse(format.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sql = "SELECT SUM(t2.dkye) FROM CLoanHousingPersonInformationBasic t1 LEFT JOIN StHousingPersonalAccount t2 ON t1.personalAccount = t2.id WHERE t2.dkqs <= 60 AND t2.dkffrq <= :cxny";
        return (BigDecimal) getSession().createQuery(sql).setParameter("cxny", cxny).getSingleResult();
    }

    public BigDecimal getPersonLoanBalanceMore5Y(String CXNY) {
        Date cxny = null;
        try {
            cxny = DateUtil.str2Date("yyyy-MM", CXNY);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cxny);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            System.out.println(format.format(calendar.getTime()));
            cxny = format.parse(format.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String sql = "SELECT SUM(t2.dkye) FROM CLoanHousingPersonInformationBasic t1 LEFT JOIN StHousingPersonalAccount t2 ON t1.personalAccount = t2.id WHERE t2.dkqs > 60 AND t2.dkffrq <= :cxny";
        return (BigDecimal) getSession().createQuery(sql).setParameter("cxny", cxny).getSingleResult();
    }

    @Override
    public CLoanHousingPersonInformationBasic getByDKZH(String DKZH) {
        List<CLoanHousingPersonInformationBasic> list = getSession().createCriteria(CLoanHousingPersonInformationBasic.class)
                .add(Restrictions.eq("dkzh", DKZH))
                .add(Restrictions.eq("deleted", false))
                .list();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<CLoanHousingPersonInformationBasic> getLoan() {
        String sql = "select result,account from CLoanHousingPersonInformationBasic result " +
                "left join StHousingPersonalAccount account on result.personalAccount = account.id " +
                "where result.dkzhzt in ('2','3')";
        return getSession().createQuery(sql, CLoanHousingPersonInformationBasic.class).list();

    }

    @Override
    public void batchSubmit(List<StHousingPersonalAccount> personalAccount) {
        int i = 0;
        for (StHousingPersonalAccount account : personalAccount) {
            getSession().update(account);
//            if(i==1000) {
//                try {
//                    getSession().flush();
//                    getSession().clear();
//                    i=0;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//            }
//            i++;
        }
        getSession().flush();
        getSession().clear();
    }

    @Override
    public void batchUpdate(List<CLoanHousingPersonInformationBasic> basic) {
        int i = 0;
        for (CLoanHousingPersonInformationBasic account : basic) {
            getSession().update(account);
            if (i == 1000) {
                try {
                    getSession().flush();
                    getSession().clear();
                    i = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            i++;
        }
        getSession().flush();
        getSession().clear();
    }
//    {

//        '23791057400001707','23793057900000455','23793057700000456','23818057900001281','23818057000001233','23818057700001282','23854057900000302','23808057900000579','23818057800001272','23818057800001229','23818057600001230','23818057200001232','23818057000001271','23818057200003009','23818057400003013','23818057400003032','23818057900002068','23818057700002069','23818057500002070','23818057300002071','23818057100002072','23818057900002073','23818057900002997','23818057000003010','23818057800003011','23818057600003012','23818057200003014','23818057900003015','23818057700003016','23818057500003017','23818057300003018','23818057900003020','23793057000000394','23793057700000395','52001069403600000000267478','52001069623600000000513668','52001069403600000001055097','52001069643600000000183287','52001069633600000000877997','52001069403600000001055261','52001069633600000000878003','52001069633600000000428172','52001069403600000000182529','52001069623600000000399366','52001069623600000000513650','52001069403600000001098796','52001069403600000001055085','52001069403600000001098270','2406071098001955364','2406071098002064524','2406071098002541717','2406071098002745823','2406071098002745947','2406071098002746450','2406071098002746175','2406071098002746601','2406071098002746973','2406071098000780958','2406071098002747104','52001069623600000000452963','52001069623600000000513667','52001069623600000000513666','52001069403600000000182536','52001069633600000000877993','52001069403600000000182531','52001069623600000000399368','52001069623600000000399369','132010303949','52001069403600000000182538','52001069633600000000428173','133010303986','52001069403600000000182548','52001069633600000000284245','52001069633600000000428169','132010303972','132010304024','132010304002','133010304027','132010304013','133010303997','52001069633600000000877995','52001069633600000000877992','52001069623600000000326152','52001069403600000001032880','52001069403600000001055249','52001069403600000000182542','52001069633600000000877988','52001069623600000000513652','52001069623600000000399371','52001069633600000000665756','52001069623600000000475101','52001069403600000000180812','52001069403600000000180642','52001069403600000001032887','52001069623600000000497480','52001069623600000000513660','52001069633600000000665755','52001069623600000000513656','52001069623600000000227842','52001069403600000000182530','52001069633600000000665761','52001069623600000000412649','52001069633600000000665766','52001069623600000000544093','52001069403600000000182533','52001069403600000001055250','52001069403600000000182535','52001069403600000001032886','52001069633600000000665758','52001069403600000001055325','52001069623600000000513647','52001069603600000000276834','52001069633600000000877973','52001069403600000001055247','52001069403600000000182547','52001069623600000000399364','52001069403600000000182528','52001069403600000000182537','52001069603600000000242450','52001069623600000000375900','52001069403600000001032883','52001069403600000000182534','52001069403600000000182814','52001069633600000000877996','52001069623600000000399367','52001069623600000000544092','52001069403600000000265686','52001069633600000000877994','52001069403600000000182815','52001069403600000001032876','52001069403600000001055322','52001069633600000000665757','52001069633600000000428171','52001069403600000001055263','52001069623600000000513664','52001069623600000000367550','52001069403600000000529093','52001069403600000000182545','52001069633600000000428170','52001069623600000000367482','52001069633600000000665759','52001069403600000000182541','52001069633600000000428174','52001069403600000000182527','52001069623600000000513644','52001069623600000000399370','52001069633600000000665762','52001069403600000000475089','52001069633600000000877989','52001069633600000000665760','52001069403600000000182543','52001069633600000000665765','52001069403600000000182742','52001069623600000000452962','23793001230009499','52001069403600000000182532','52001069403600000000182546','23791057100000912','23791057900000913','52001069403600000000182539','23873057900001828','52001069643600000000183288','23791057200004387','23791057000004388','23791057800004389','23791057600004390','52001069403600000000707721','52001069623600000000513663','52001069403600000001055323','52001069403600000000182540','52001069403600000001032888','52001069623600000000399365','52001069403600000001055255','52001069623600000000412648','52001069633600000000878002','52001069623600000000513658','52001069403600000000182811','52001069623600000000475100','52001069633600000000284246','52001069623600000000452961','52001069623600000000475103','52001069623600000000367549','52001069403600000000182726','52001069623600000000475102','52001069623600000000574226','52001069403600000000475088','52001069623600000000513668','52001069403600000001055097','52001069633600000000877997','52001069403600000001055261','52001069633600000000878003','52001069633600000000428172','52001069623600000000399366','52001069623600000000513650','52001069403600000001098796','52001069403600000001055085','52001069403600000001098270','2406071098001955364','2406071098002064524','2406071098002541717','2406071098002745823','2406071098002745947','2406071098002746450','2406071098002746175','2406071098002746601','2406071098002746973','2406071098000780958','2406071098002747104','52001069623600000000452963','52001069623600000000513667','52001069623600000000513666','52001069633600000000877993','52001069623600000000399368','52001069623600000000399369','132010303949','52001069633600000000428173','133010303986','52001069633600000000284245','52001069633600000000428169','132010303972','132010304024','132010304002','133010304027','132010304013','133010303997','52001069633600000000877995','52001069633600000000877992','52001069623600000000326152','52001069403600000001032880','52001069403600000001055249','52001069633600000000877988','52001069623600000000513652','52001069623600000000399371','52001069633600000000665756','52001069623600000000475101','52001069403600000001032887','52001069623600000000497480','52001069623600000000513660','52001069633600000000665755','52001069623600000000513656','52001069623600000000227842','52001069633600000000665761','52001069623600000000412649','52001069633600000000665766','52001069623600000000544093','52001069403600000001055250','52001069403600000001032886','52001069633600000000665758','52001069403600000001055325','52001069623600000000513647','52001069633600000000877973','52001069403600000001055247','52001069623600000000399364','52001069623600000000375900','52001069403600000001032883','52001069633600000000877996','52001069623600000000399367','52001069623600000000544092','52001069633600000000877994','52001069403600000001032876','52001069403600000001055322','52001069633600000000665757','52001069633600000000428171','52001069403600000001055263','52001069623600000000513664','52001069623600000000367550','52001069633600000000428170','52001069623600000000367482','52001069633600000000665759','52001069633600000000428174','52001069623600000000513644','52001069623600000000399370','52001069633600000000665762','52001069403600000000475089','52001069633600000000877989','52001069633600000000665760','52001069633600000000665765','52001069623600000000452962','23793001230009499','23791057100000912','23791057900000913','23791057200004387','23791057000004388','23791057800004389','23791057600004390','52001069403600000000707721','52001069623600000000513663','52001069403600000001055323','52001069403600000001032888','52001069623600000000399365','52001069403600000001055255','52001069623600000000412648','52001069633600000000878002','52001069623600000000513658','52001069623600000000475100','52001069633600000000284246','52001069623600000000452961','52001069623600000000475103','52001069623600000000367549','52001069623600000000475102','52001069623600000000574226','52001069403600000000475088'

//    }


    public List<Object[]> getLoan2() {
        String sql2 = "select result.id,result.dkll,result.llfdbl,extension.dkgbjhqs,extension.dkgbjhye,extension.dqqc,personalLoan.dkhkfs " +
                "from StHousingPersonalAccount result " +
                "join  result.cLoanHousingPersonalAccountExtension extension " +
                "join  result.stHousingPersonalLoan personalLoan , " +
                "CLoanHousingPersonInformationBasic basic join basic.personalAccount result2 " +
                "where basic.dkzhzt in ('2','3','5') and basic.dkzh in ('23791057400001707','23793057900000455','23793057700000456','23818057900001281','23818057000001233','23818057700001282','23854057900000302','23808057900000579','23818057800001272','23818057800001229','23818057600001230','23818057200001232','23818057000001271','23818057200003009','23818057400003013','23818057400003032','23818057900002068','23818057700002069','23818057500002070','23818057300002071','23818057100002072','23818057900002073','23818057900002997','23818057000003010','23818057800003011','23818057600003012','23818057200003014','23818057900003015','23818057700003016','23818057500003017','23818057300003018','23818057900003020','23793057000000394','23793057700000395','52001069403600000000267478','52001069623600000000513668','52001069403600000001055097','52001069643600000000183287','52001069633600000000877997','52001069403600000001055261','52001069633600000000878003','52001069633600000000428172','52001069403600000000182529','52001069623600000000399366','52001069623600000000513650','52001069403600000001098796','52001069403600000001055085','52001069403600000001098270','2406071098001955364','2406071098002064524','2406071098002541717','2406071098002745823','2406071098002745947','2406071098002746450','2406071098002746175','2406071098002746601','2406071098002746973','2406071098000780958','2406071098002747104','52001069623600000000452963','52001069623600000000513667','52001069623600000000513666','52001069403600000000182536','52001069633600000000877993','52001069403600000000182531','52001069623600000000399368','52001069623600000000399369','132010303949','52001069403600000000182538','52001069633600000000428173','133010303986','52001069403600000000182548','52001069633600000000284245','52001069633600000000428169','132010303972','132010304024','132010304002','133010304027','132010304013','133010303997','52001069633600000000877995','52001069633600000000877992','52001069623600000000326152','52001069403600000001032880','52001069403600000001055249','52001069403600000000182542','52001069633600000000877988','52001069623600000000513652','52001069623600000000399371','52001069633600000000665756','52001069623600000000475101','52001069403600000000180812','52001069403600000000180642','52001069403600000001032887','52001069623600000000497480','52001069623600000000513660','52001069633600000000665755','52001069623600000000513656','52001069623600000000227842','52001069403600000000182530','52001069633600000000665761','52001069623600000000412649','52001069633600000000665766','52001069623600000000544093','52001069403600000000182533','52001069403600000001055250','52001069403600000000182535','52001069403600000001032886','52001069633600000000665758','52001069403600000001055325','52001069623600000000513647','52001069603600000000276834','52001069633600000000877973','52001069403600000001055247','52001069403600000000182547','52001069623600000000399364','52001069403600000000182528','52001069403600000000182537','52001069603600000000242450','52001069623600000000375900','52001069403600000001032883','52001069403600000000182534','52001069403600000000182814','52001069633600000000877996','52001069623600000000399367','52001069623600000000544092','52001069403600000000265686','52001069633600000000877994','52001069403600000000182815','52001069403600000001032876','52001069403600000001055322','52001069633600000000665757','52001069633600000000428171','52001069403600000001055263','52001069623600000000513664','52001069623600000000367550','52001069403600000000529093','52001069403600000000182545','52001069633600000000428170','52001069623600000000367482','52001069633600000000665759','52001069403600000000182541','52001069633600000000428174','52001069403600000000182527','52001069623600000000513644','52001069623600000000399370','52001069633600000000665762','52001069403600000000475089','52001069633600000000877989','52001069633600000000665760','52001069403600000000182543','52001069633600000000665765','52001069403600000000182742','52001069623600000000452962','23793001230009499','52001069403600000000182532','52001069403600000000182546','23791057100000912','23791057900000913','52001069403600000000182539','23873057900001828','52001069643600000000183288','23791057200004387','23791057000004388','23791057800004389','23791057600004390','52001069403600000000707721','52001069623600000000513663','52001069403600000001055323','52001069403600000000182540','52001069403600000001032888','52001069623600000000399365','52001069403600000001055255','52001069623600000000412648','52001069633600000000878002','52001069623600000000513658','52001069403600000000182811','52001069623600000000475100','52001069633600000000284246','52001069623600000000452961','52001069623600000000475103','52001069623600000000367549','52001069403600000000182726','52001069623600000000475102','52001069623600000000574226','52001069403600000000475088','52001069623600000000513668','52001069403600000001055097','52001069633600000000877997','52001069403600000001055261','52001069633600000000878003','52001069633600000000428172','52001069623600000000399366','52001069623600000000513650','52001069403600000001098796','52001069403600000001055085','52001069403600000001098270','2406071098001955364','2406071098002064524','2406071098002541717','2406071098002745823','2406071098002745947','2406071098002746450','2406071098002746175','2406071098002746601','2406071098002746973','2406071098000780958','2406071098002747104','52001069623600000000452963','52001069623600000000513667','52001069623600000000513666','52001069633600000000877993','52001069623600000000399368','52001069623600000000399369','132010303949','52001069633600000000428173','133010303986','52001069633600000000284245','52001069633600000000428169','132010303972','132010304024','132010304002','133010304027','132010304013','133010303997','52001069633600000000877995','52001069633600000000877992','52001069623600000000326152','52001069403600000001032880','52001069403600000001055249','52001069633600000000877988','52001069623600000000513652','52001069623600000000399371','52001069633600000000665756','52001069623600000000475101','52001069403600000001032887','52001069623600000000497480','52001069623600000000513660','52001069633600000000665755','52001069623600000000513656','52001069623600000000227842','52001069633600000000665761','52001069623600000000412649','52001069633600000000665766','52001069623600000000544093','52001069403600000001055250','52001069403600000001032886','52001069633600000000665758','52001069403600000001055325','52001069623600000000513647','52001069633600000000877973','52001069403600000001055247','52001069623600000000399364','52001069623600000000375900','52001069403600000001032883','52001069633600000000877996','52001069623600000000399367','52001069623600000000544092','52001069633600000000877994','52001069403600000001032876','52001069403600000001055322','52001069633600000000665757','52001069633600000000428171','52001069403600000001055263','52001069623600000000513664','52001069623600000000367550','52001069633600000000428170','52001069623600000000367482','52001069633600000000665759','52001069633600000000428174','52001069623600000000513644','52001069623600000000399370','52001069633600000000665762','52001069403600000000475089','52001069633600000000877989','52001069633600000000665760','52001069633600000000665765','52001069623600000000452962','23793001230009499','23791057100000912','23791057900000913','23791057200004387','23791057000004388','23791057800004389','23791057600004390','52001069403600000000707721','52001069623600000000513663','52001069403600000001055323','52001069403600000001032888','52001069623600000000399365','52001069403600000001055255','52001069623600000000412648','52001069633600000000878002','52001069623600000000513658','52001069623600000000475100','52001069633600000000284246','52001069623600000000452961','52001069623600000000475103','52001069623600000000367549','52001069623600000000475102','52001069623600000000574226','52001069403600000000475088')" +
                "and result.id = result2.id ";
        List<Object[]> list = getSession().createQuery(sql2, Object[].class).list();
        return list;
    }

//    public List<Object[]> getLoan2() {
//        String sql2 = "select result.id,result.dkll,result.llfdbl,extension.dkgbjhqs,extension.dkgbjhye,extension.dqqc,personalLoan.dkhkfs " +
//                "from StHousingPersonalAccount result " +
//                "join  result.cLoanHousingPersonalAccountExtension extension " +
//                "join  result.stHousingPersonalLoan personalLoan , " +
//                "CLoanHousingPersonInformationBasic basic join basic.personalAccount result2 " +
//                "where basic.dkzhzt in ('5') " +
//                "and result.id = result2.id ";
//        List<Object[]> list = getSession().createQuery(sql2, Object[].class).list();
//        return list;
//    }

    public List<CLoanHousingPersonInformationBasic> getLoanDKZHZT(String yhdm) {
//        String sql2 = "select result,basic,extension " +
//                "from StHousingPersonalAccount result " +
//                "join  result.cLoanHousingPersonalAccountExtension extension, " +
//                "CLoanHousingPersonInformationBasic basic join basic.personalAccount result2 " +
//                "where basic.dkzhzt in ('2','3') " +
//                "and result.id = result2.id and basic.loanContract.zhkhyhdm = :yhdm ";
//        List<Object[]> list = getSession().createQuery(sql2, Object[].class).setParameter("yhdm",yhdm).list();
//        return list;

//        String sql2 = "select personalAccount,basic,extension " +
//                "from CLoanHousingPersonInformationBasic basic " +
//                "join basic.personalAccount personalAccount " +
//                "join personalAccount.extension extension " +
//                "join basic.loanContract loanContract " +
//                "where basic.dkzhzt in ('2','3') " +
//                "and loanContract.zhkhyhdm = :yhdm ";
//        List<Object[]> list = getSession().createQuery(sql2, Object[].class)
//                .setParameter("yhdm", yhdm)
//                .list();
//        return list;


        String sql2 = "select basic from CLoanHousingPersonInformationBasic basic " +
                "inner join  basic.personalAccount personalAccount ON basic.personalAccount=personalAccount.id " +
                "inner join CLoanHousingPersonalAccountExtension extension ON extension.id=personalAccount.cLoanHousingPersonalAccountExtension " +
                "inner  join StHousingPersonalLoan loan ON  loan.id=basic.loanContract " +
                "where basic.dkzhzt in ('2','3') " +
                "and loan.zhkhyhdm = :yhdm ";
        List<CLoanHousingPersonInformationBasic> list = getSession().createQuery(sql2, CLoanHousingPersonInformationBasic.class)
                .setParameter("yhdm", yhdm)
                .list();
        return list;

    }

    public void updateLoan(String id, BigDecimal dqyhje, BigDecimal dqyhbj, BigDecimal dqyhlx, BigDecimal dqjhhkje, BigDecimal dqjhghlx, BigDecimal dqjhghbj) {
        String sql = "update st_housing_personal_account set " +
                "DQYHJE = :dqyhje " +
                ", DQYHBJ = :dqyhbj " +
                ", DQYHLX = :dqyhlx " +
                ", DQJHHKJE = :dqjhhkje " +
                ", DQJHGHLX = :dqjhghlx " +
                ", DQJHGHBJ = :dqjhghbj " +
                ", updated_at = now() " +
                "where id = :id";
        getSession().createSQLQuery(sql)
                .setParameter("dqyhje", dqyhje)
                .setParameter("dqyhbj", dqyhbj)
                .setParameter("dqyhlx", dqyhlx)
                .setParameter("dqjhhkje", dqjhhkje)
                .setParameter("dqjhghlx", dqjhghlx)
                .setParameter("dqjhghbj", dqjhghbj)
                .setParameter("id", id)
                .executeUpdate();
    }

    public void searchnew() {
        String sql = "SELECT shpl.ZHKHYHDM from st_housing_personal_loan shpl GROUP BY shpl.ZHKHYHDM\n" +
                "SELECT\n" +
                "\tshpl.id\n" +
                "FROM\n" +
                "\tc_loan_housing_person_information_basic clhpib\n" +
                " INNER JOIN c_loan_funds_information_basic clfib ON clfib.id = clhpib.fundsBasic\n" +
                " AND clfib.WTKHYJCE = 0\n" +
                "INNER JOIN st_housing_personal_account shpa ON shpa.id = clhpib.personalAccount\n" +
                "\n" +
                " INNER JOIN st_housing_personal_loan shpl ON shpl.id = shpa.contract\n" +
                " AND shpl.ZHKHYHDM = '10'\n" +
                " INNER JOIN c_loan_housing_personal_account_extension alhpae ON alhpae.id=shpa.extenstion AND Date(NOW())=date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH)\n" +
                " WHERE\n" +
                " \tclhpib.DKZHZT = 2";
        NativeQuery sqlQuery = getSession().createSQLQuery(sql);

    }

    @Override
    public void flush() {
        getSession().flush();
        getSession().clear();
    }

    @Override
    public ArrayList<BackBasicInfomation> repamentNor(String yhdm, Date kkyf, Date overTime) {
        try {
            String zhkhyhdms = "";
            String starttime = "";
            String endtime = "";
            HashMap<String, Object> param = new HashMap<>();
            if (StringUtil.notEmpty(yhdm)) {
                zhkhyhdms = " and shpl.ZHKHYHDM = :ZHKHYHDM";
                param.put("ZHKHYHDM", yhdm);
            }
            if (kkyf != null) {
                starttime = " AND :starttime <=date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH)";
                param.put("starttime", kkyf);
            }
            if (overTime != null) {
                endtime = "  AND :endtime >=date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH) ";
                param.put("endtime", overTime);
            }


            String sqls = " SELECT" +
                    " shpa.DKYE,shpa.HSBJZE,shpa.DKFFE,shpa.DKQS,shpa.DQYHBJ,shpa.DQYHLX,shpa.DKLL,shpa.DKFFRQ," +
                    " shpa.DQYHJE,shpa.LJYQQS,shpa.YQBJZE,shpa.YQLXZE,shpa.HSLXZE,clhpib.DKZHZT," +
                    " clhpib.YHQS,shpa.DKZH,le.HKZHHM,alhpae.DKXFFRQ,alhpae.DQQC,alhpae.DKGBJHQS,alhpae.DKGBJHYE,alhpae.DKYEZCBJ," +
                    " shpl.DKHKFS,shpl.LLFDBL,shpl.ZHKHYHDM,shpl.ZHKHYHMC,shpl.HKZH,clhpib.JKRGJJZH,clfib.WTKHYJCE,shc.GTJKRGJJZH," +
                    " clhpib.id AS clhpibid,shpa.id AS shpaid,shpl.id AS shplid,alhpae.id AS alhpaeid,clhpib.YWWD,clhpib.HYZK" +
                    " FROM" +
                    " c_loan_housing_person_information_basic clhpib" +
                    " LEFT JOIN c_loan_funds_information_basic clfib ON clfib.id = clhpib.fundsBasic" +
                    " INNER JOIN st_housing_personal_account shpa ON shpa.id = clhpib.personalAccount" +
                    " LEFT JOIN st_housing_coborrower shc ON shc.id=clhpib.coborrower " +
                    " INNER JOIN st_housing_personal_loan shpl ON shpl.id = shpa.contract INNER JOIN c_loan_housing_personal_loan_extension le ON  shpl.extenstion=le.id " +
                    zhkhyhdms +
//                        " INNER JOIN c_loan_housing_personal_account_extension alhpae ON alhpae.id=shpa.extenstion AND '2017-12-01'<=date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH) AND date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH) <= '2017-12-15'" +
                    " INNER JOIN c_loan_housing_personal_account_extension alhpae ON alhpae.id=shpa.extenstion" +
                    starttime +
                    endtime +
                    " WHERE" +
                    " clhpib.DKZHZT in ('2','5') AND clhpib.deleted='0'";
            List<Object[]> list = iAccountEmployeeDAO.list(sqls, param);
            ArrayList<BackBasicInfomation> backBasicInfomations = fromObjectList(list);


            return backBasicInfomations;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }

    @Override
    public void getInfo() {
        Session session = this.getSession();
        session.createNativeQuery("CREATE TEMPORARY TABLE xx(dkzh varchar(30) PRIMARY KEY,yhqs NUMERIC(4,0))").executeUpdate();
        session.createNativeQuery("INSERT xx(dkzh,yhqs) SELECT basic.DKZH as a,account.DKQS-pe.DKGBJHQS+pe.DQQC-1 as b from c_loan_housing_person_information_basic basic" +
                " INNER JOIN st_housing_personal_account account ON account.id=basic.personalAccount" +
                " INNER JOIN c_loan_housing_personal_account_extension pe ON account.extenstion=pe.id" +
                " WHERE basic.DKZHZT  in ('2','5')").executeUpdate();
        session.createNativeQuery("update c_loan_housing_person_information_basic a,xx b SET a.YHQS=b.yhqs where a.DKZH=b.dkzh").executeUpdate();
        session.createNativeQuery("DROP TEMPORARY TABLE xx").executeUpdate();

        session.createNativeQuery("CREATE TEMPORARY TABLE xx(dkzh varchar(30) PRIMARY KEY,yhqs NUMERIC(4,0))").executeUpdate();
        session.createNativeQuery("INSERT xx(dkzh,yhqs) SELECT over.DKZH,count(*) FROM st_housing_overdue_registration over  INNER JOIN c_housing_overdue_registration_extension  pe ON  over.extenstion=pe.id WHERE pe.YWZT <> '已入账' " +
                "GROUP BY over.DKZH").executeUpdate();
        session.createNativeQuery("update c_loan_housing_person_information_basic a,xx b SET a.YHQS=a.YHQS-b.yhqs where a.DKZH=b.dkzh").executeUpdate();
        session.createNativeQuery("DROP TEMPORARY TABLE xx").executeUpdate();

        session.createNativeQuery("UPDATE c_loan_housing_person_information_basic basic,st_housing_personal_account acc, c_loan_housing_personal_account_extension pe  set" +
                " basic.DKZHZT='3',basic.YHQS=acc.DKQS,acc.HSBJZE=acc.DKFFE,acc.dkye=0,pe.DKGBJHQS=0,pe.DKGBJHYE=0,pe.dqqc=1,pe.DKYEZCBJ=0" +
                " where   basic.personalAccount=acc.id AND acc.extenstion=pe.id and  acc.DKYE < 1").executeUpdate();

        List<String> dkzh = session.createNativeQuery("select DISTINCT(basic.DKZH) from c_loan_housing_person_information_basic basic WHERE  basic.DKZHZT='5'").list();
        for (String acc : dkzh) {
            List<BigInteger> count = session.createNativeQuery("select COUNT(*) from st_housing_overdue_registration over, c_housing_overdue_registration_extension pe WHERE over.extenstion=pe.id AND over.DKZH =:dkzh AND pe.YWZT <> '已入账'").setParameter("dkzh", acc).list();
            if (count.get(0).compareTo(BigInteger.ZERO) <= 0) {
                session.createNativeQuery("update c_loan_housing_person_information_basic set DKZHZT='2' WHERE DKZH =:dkzh").setParameter("dkzh", acc).executeUpdate();
            }
        }

        List<String> zcbj = session.createNativeQuery("select DISTINCT(acc.DKZH) from st_housing_personal_account acc,c_loan_housing_personal_account_extension pe where  acc.extenstion=pe.id  AND acc.DKYE <> pe.DKYEZCBJ").list();
        for (String acc : zcbj) {
            List<BigInteger> count = session.createNativeQuery("select COUNT(*) from st_housing_overdue_registration over, c_housing_overdue_registration_extension pe WHERE over.extenstion=pe.id AND over.DKZH =:dkzh AND pe.YWZT <> '已入账'").setParameter("dkzh", acc).list();
            if (count.get(0).compareTo(BigInteger.ZERO) <= 0) {
                session.createNativeQuery("update  st_housing_personal_account acc,c_loan_housing_personal_account_extension pe set acc.DKYE=pe.DKYEZCBJ where  acc.extenstion=pe.id  AND acc.DKYE <> pe.DKYEZCBJ AND acc.DKZH =:dkzh").setParameter("dkzh", acc).executeUpdate();
            }
        }

//        List<String> dkzhs= session.createNativeQuery("select DISTINCT(basic.DKZH) from c_loan_housing_person_information_basic basic,st_housing_personal_account acc,c_loan_housing_personal_account_extension pe where" +
//                "  basic.personalAccount=acc.id AND acc.extenstion=pe.id and pe.DKYEZCBJ=0 AND acc.DKYE <> 0").list();
//        for(String  acc:dkzhs){
//            List<BigInteger>  count=session.createNativeQuery("select COUNT(*) from st_housing_overdue_registration over, c_housing_overdue_registration_extension pe WHERE over.extenstion=pe.id AND over.DKZH =:dkzh AND pe.YWZT <> '已入账'").setParameter("dkzh",acc).list();
//            if(count.get(0).compareTo(BigInteger.ZERO)<=0){
//                session.createNativeQuery(" UPDATE c_loan_housing_person_information_basic basic,st_housing_personal_account acc,c_loan_housing_personal_account_extension pe" +
//                        " set basic.DKZHZT='3',acc.DKYE=0,acc.DQJHGHBJ=0,acc.DQJHGHLX=0,acc.DQJHHKJE=0,acc.DQYHBJ=0,acc.DQYHLX=0,acc.DQYHJE=0,pe.DKGBJHQS=0,pe.DKGBJHYE=0,pe.dqqc=1 where basic.personalAccount=acc.id AND acc.extenstion=pe.id AND pe.DKYEZCBJ=0 AND basic.DKZH =:dkzh").setParameter("dkzh",acc).executeUpdate();
//            }
//        }
    }

    @Override
    public void updateYhqs(BigInteger yhqs, String dkzh) {
        String sql = "UPDATE c_loan_housing_person_information_basic basic  " +
                " SET " +
                " basic.YHQS =:yhqs" +
                " AND basic.updated_at= NOW() where  basic.DKZH =:dkzh";
        getSession().createSQLQuery(sql)
                .setParameter("yhqs", yhqs)
                .setParameter("dkzh", dkzh)
                .executeUpdate();
    }

    @Override
    public void updateAccountStatus(String dkzh) {
        String sql = "UPDATE c_loan_housing_person_information_basic basic INNER JOIN st_housing_personal_account accont" +
                " ON basic.personalAccount=accont.id" +
                " SET basic.DKZHZT='3',accont.HSBJZE=accont.DKFFE WHERE" +
                " basic.DKZH =:dkzh ";
        getSession().createNativeQuery(sql).setParameter("dkzh", dkzh).executeUpdate();

    }

    @Override
    public boolean getByDkzh(String dkzh, String zjhm) {
        String sql = "SELECT basic.JKRZJHM FROM c_loan_housing_person_information_basic basic WHERE" +
                " basic.DKZH =:dkzh";
        List<Object[]> itemList = getSession().createNativeQuery(sql).setParameter("dkzh", "%" + dkzh + "%").getResultList();

        if (itemList.size() > 0) {
            Object[] obj = itemList.get(0);
            return ((String) obj[0]).equals(zjhm);
        }

        return false;
    }

    private BackBasicInfomation fromObject(Object[] rs) {
        BackBasicInfomation backBasicInfomation = new BackBasicInfomation();
        backBasicInfomation.setDKYE((BigDecimal) rs[0]);
        backBasicInfomation.setHSBJZE((BigDecimal) rs[1]);
        backBasicInfomation.setDKFFE((BigDecimal) rs[2]);
        backBasicInfomation.setDKQS((BigDecimal) rs[3]);
        backBasicInfomation.setDQYHBJ((BigDecimal) rs[4]);
        backBasicInfomation.setDQYHLX((BigDecimal) rs[5]);
        backBasicInfomation.setDKLL((BigDecimal) rs[6]);
        backBasicInfomation.setDKFFRQ((java.sql.Timestamp) rs[7]);
        backBasicInfomation.setDQYHJE((BigDecimal) rs[8]);
        backBasicInfomation.setLJYQQS((BigDecimal) rs[9]);
        backBasicInfomation.setYQBJZE((BigDecimal) rs[10]);
        backBasicInfomation.setYQLXZE((BigDecimal) rs[11]);
        backBasicInfomation.setHSLXZE((BigDecimal) rs[12]);
        backBasicInfomation.setDKZHZT((String) rs[13]);
        backBasicInfomation.setYHQS((BigDecimal) rs[14]);
        backBasicInfomation.setDKZH((String) rs[15]);
        backBasicInfomation.setJKRXM((String) rs[16]);
        backBasicInfomation.setDKXFFRQ((java.sql.Timestamp) rs[17]);
        backBasicInfomation.setDQQC((BigDecimal) rs[18]);
        backBasicInfomation.setDKGBJHQS((BigDecimal) rs[19]);
        backBasicInfomation.setDKGBJHYE((BigDecimal) rs[20]);
        backBasicInfomation.setDKYEZCBJ((BigDecimal) rs[21]);
        backBasicInfomation.setDKHKFS((String) rs[22]);
        backBasicInfomation.setLLFDBL((BigDecimal) rs[23]);
        backBasicInfomation.setZHKHYHDM((String) rs[24]);
        backBasicInfomation.setZHKHYHMC((String) rs[25]);
        backBasicInfomation.setHKZH((String) rs[26]);
        backBasicInfomation.setJKRGJJZH((String) rs[27]);
        backBasicInfomation.setWTKHYJCE((boolean) rs[28]);
        backBasicInfomation.setGTJKRGJJZH((String) rs[29]);
        backBasicInfomation.setCLHPIBID((String) rs[30]);
        backBasicInfomation.setSHPAID((String) rs[31]);
        backBasicInfomation.setSHPLID((String) rs[32]);
        backBasicInfomation.setALHPAEID((String) rs[33]);
        backBasicInfomation.setYWWD((String) rs[34]);
        backBasicInfomation.setHYZK((String) rs[35]);
        return backBasicInfomation;
    }

    private ArrayList<BackBasicInfomation> fromObjectList(List<Object[]> rsList) {
        ArrayList<BackBasicInfomation> backBasicInfomations = new ArrayList<>();
        for (Object[] rs : rsList) {
            backBasicInfomations.add(fromObject(rs));
        }
        return backBasicInfomations;
    }

    @Override
    public List<CLoanHousingPersonInformationBasic> getSFDKData(String jkrzjhm) {
        String sql = "select result from CLoanHousingPersonInformationBasic result" +
                " where result.jkrzjhm = :jkrzjhm " +
                " and result.deleted = false " +
                " and result.loanContract is not null " +
                " and result.personalAccount is not null " +
                " and result.dkzhzt not in ('3','8')";
        List<CLoanHousingPersonInformationBasic> list = getSession().createQuery(sql, CLoanHousingPersonInformationBasic.class)
                .setParameter("jkrzjhm", jkrzjhm)
                .list();
        return list;
    }

    @Override
    public BigInteger accountOverCount(Date etime) {
        String jzsj = DateUtil.date2Str(etime, "yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT count(*)  FROM c_loan_housing_person_information_basic basic" +
                " inner join st_housing_personal_loan loan on basic.loanContract=loan.id" +
                " inner join st_housing_personal_account acc on basic.personalAccount=acc.id where basic.deleted='0' and basic.DKZHZT in ('2','4','5') and acc.DKFFRQ <= '" + jzsj + "'";

        String sql2 = "SELECT count(*) FROM c_loan_housing_person_information_basic basic " +
                "INNER JOIN st_housing_personal_loan loan ON basic.loanContract = loan.id " +
                "INNER JOIN st_housing_personal_account acc ON basic.personalAccount = acc.id " +
                "WHERE ( basic.DKZHZT IN ('2', '4', '5') OR ( basic.DKZHZT = '3' AND acc.DKJQRQ > '" + jzsj + "' )) AND acc.DKFFRQ <= '" + jzsj + "'";
        BigInteger count = (BigInteger) getSession().createNativeQuery(sql2).getSingleResult();
        return count;
    }

    @Override
    public ArrayList<SMSBackBasicInfomation> SMSrepamentNor() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 3);
            HashMap<String, Object> param = new HashMap<>();
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
            String starttime = " AND :starttime =date_add(DATE(alhpae.DKXFFRQ), interval alhpae.DQQC MONTH)";
            param.put("starttime", sim.parse(sim.format(cal.getTime())));

            String sqls = " SELECT" +
                    " shpa.DKYE,shpa.HSBJZE,shpa.DKFFE,shpa.DKQS,shpa.DQYHBJ,shpa.DQYHLX,shpa.DKLL,shpa.DKFFRQ," +
                    " shpa.DQYHJE,shpa.LJYQQS,shpa.YQBJZE,shpa.YQLXZE,shpa.HSLXZE,clhpib.DKZHZT," +
                    " clhpib.YHQS,shpa.DKZH,le.HKZHHM,alhpae.DKXFFRQ,alhpae.DQQC,alhpae.DKGBJHQS,alhpae.DKGBJHYE,alhpae.DKYEZCBJ," +
                    " shpl.DKHKFS,shpl.LLFDBL,shpl.ZHKHYHDM,shpl.ZHKHYHMC,shpl.HKZH,clhpib.JKRGJJZH,clfib.WTKHYJCE,shc.GTJKRGJJZH," +
                    " clhpib.id AS clhpibid,shpa.id AS shpaid,shpl.id AS shplid,alhpae.id AS alhpaeid,clhpib.YWWD,clhpib.HYZK,clhpib.sjhm" +
                    " FROM" +
                    " c_loan_housing_person_information_basic clhpib" +
                    " LEFT JOIN c_loan_funds_information_basic clfib ON clfib.id = clhpib.fundsBasic" +
                    " INNER JOIN st_housing_personal_account shpa ON shpa.id = clhpib.personalAccount" +
                    " LEFT JOIN st_housing_coborrower shc ON shc.id=clhpib.coborrower " +
                    " INNER JOIN st_housing_personal_loan shpl ON shpl.id = shpa.contract INNER JOIN c_loan_housing_personal_loan_extension le ON  shpl.extenstion=le.id " +
                    " INNER JOIN c_loan_housing_personal_account_extension alhpae ON alhpae.id=shpa.extenstion" +
                    starttime +
                    " WHERE" +
                    " clhpib.DKZHZT in ('2','5') AND clhpib.deleted='0'";
            List<Object[]> list = iAccountEmployeeDAO.list(sqls, param);
            ArrayList<SMSBackBasicInfomation> backBasicInfomations = SMSfromObjectList(list);
            return backBasicInfomations;
        } catch (Exception e) {
            throw new ErrorException(e);
        }
    }


    private SMSBackBasicInfomation SMSfromObject(Object[] rs) {
        SMSBackBasicInfomation backBasicInfomation = new SMSBackBasicInfomation();
        backBasicInfomation.setDKYE((BigDecimal) rs[0]);
        backBasicInfomation.setHSBJZE((BigDecimal) rs[1]);
        backBasicInfomation.setDKFFE((BigDecimal) rs[2]);
        backBasicInfomation.setDKQS((BigDecimal) rs[3]);
        backBasicInfomation.setDQYHBJ((BigDecimal) rs[4]);
        backBasicInfomation.setDQYHLX((BigDecimal) rs[5]);
        backBasicInfomation.setDKLL((BigDecimal) rs[6]);
        backBasicInfomation.setDKFFRQ((java.sql.Timestamp) rs[7]);
        backBasicInfomation.setDQYHJE((BigDecimal) rs[8]);
        backBasicInfomation.setLJYQQS((BigDecimal) rs[9]);
        backBasicInfomation.setYQBJZE((BigDecimal) rs[10]);
        backBasicInfomation.setYQLXZE((BigDecimal) rs[11]);
        backBasicInfomation.setHSLXZE((BigDecimal) rs[12]);
        backBasicInfomation.setDKZHZT((String) rs[13]);
        backBasicInfomation.setYHQS((BigDecimal) rs[14]);
        backBasicInfomation.setDKZH((String) rs[15]);
        backBasicInfomation.setJKRXM((String) rs[16]);
        backBasicInfomation.setDKXFFRQ((java.sql.Timestamp) rs[17]);
        backBasicInfomation.setDQQC((BigDecimal) rs[18]);
        backBasicInfomation.setDKGBJHQS((BigDecimal) rs[19]);
        backBasicInfomation.setDKGBJHYE((BigDecimal) rs[20]);
        backBasicInfomation.setDKYEZCBJ((BigDecimal) rs[21]);
        backBasicInfomation.setDKHKFS((String) rs[22]);
        backBasicInfomation.setLLFDBL((BigDecimal) rs[23]);
        backBasicInfomation.setZHKHYHDM((String) rs[24]);
        backBasicInfomation.setZHKHYHMC((String) rs[25]);
        backBasicInfomation.setHKZH((String) rs[26]);
        backBasicInfomation.setJKRGJJZH((String) rs[27]);
        backBasicInfomation.setWTKHYJCE((boolean) rs[28]);
        backBasicInfomation.setGTJKRGJJZH((String) rs[29]);
        backBasicInfomation.setCLHPIBID((String) rs[30]);
        backBasicInfomation.setSHPAID((String) rs[31]);
        backBasicInfomation.setSHPLID((String) rs[32]);
        backBasicInfomation.setALHPAEID((String) rs[33]);
        backBasicInfomation.setYWWD((String) rs[34]);
        backBasicInfomation.setHYZK((String) rs[35]);
        backBasicInfomation.setSJHM((String)rs[36]);
        return backBasicInfomation;
    }

    private ArrayList<SMSBackBasicInfomation> SMSfromObjectList(List<Object[]> rsList) {
        ArrayList<SMSBackBasicInfomation> backBasicInfomations = new ArrayList<>();
        for (Object[] rs : rsList) {
            backBasicInfomations.add(SMSfromObject(rs));
        }
        return backBasicInfomations;
    }

    @Override
    public Object[] getLoanAccountMoneyCount(String DKZH, String JKRXM, String status, String DKFXDJ, String YWWD, String SWTYH, String ZJHM) {

        String sql =
                "SELECT " +
                        "SUM(acct.DKFFE)," +
                        "SUM(acct.DKYE) " +
                        "FROM " +
                        "c_loan_housing_person_information_basic basic " +
                        "INNER JOIN st_housing_personal_account acct ON basic.personalAccount = acct.id " +
                        "LEFT JOIN st_housing_personal_loan contract ON basic.loanContract = contract.id " +
                        "WHERE " +
                        "basic.deleted = 0 ";
        if (StringUtil.notEmpty(status)) {
            sql += "AND basic.dkzhzt = '" + status + "' ";
        }
        if (StringUtil.notEmpty(DKFXDJ) && !DKFXDJ.equals(LoanRiskStatus.所有.getCode())) {
            sql += "AND acct.DKFXDJ = '" + DKFXDJ + "' ";
        }
        if (StringUtil.notEmpty(YWWD)) {
            sql += "AND basic.YWWD = '" + YWWD + "' ";
        }
        if (StringUtil.notEmpty(SWTYH)) {
            sql += "AND contract.SWTYHDM = '" + SWTYH + "' ";
        }
        if (StringUtil.notEmpty(JKRXM)) {
            sql += "AND basic.JKRXM LIKE '%" + JKRXM +"%' ";
        }
        if (StringUtil.notEmpty(ZJHM)) {
            sql += "AND basic.JKRZJHM LIKE '%" + ZJHM + "%' ";
        }
        if (StringUtil.notEmpty(DKZH)) {
            sql += "AND basic.DKZH LIKE '%" + DKZH + "%' ";
        }

        Object[] result = (Object[]) getSession().createNativeQuery(sql).getSingleResult();

        return result;
    }

}
