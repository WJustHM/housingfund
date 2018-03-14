package com.handge.housingfund.common.service.util;

import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessStatus;
import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.enumeration.BusinessDetailsModule;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBussinessStatus;
import com.handge.housingfund.database.enums.BusinessSubType;

/**
 * Created by Liujuhao on 2017/9/13.
 */
public class TranslateUtils {


    /**
     * 转换状态机流程返回的“办结”为实际流程名称，归集重构
     *
     * @param next
     * @param collection
     * @return
     */
    public static String step2Status(String next, CollectionBusinessType collection) {

        if (!"办结".equals(next))
            return next;

        if (collection == null) {
            throw new ErrorException("业务类型参数异常，请确保有且只有一个枚举参数");
        }

        if (collection.equals(CollectionBusinessType.部分提取) || collection.equals(CollectionBusinessType.销户提取) || collection.equals(CollectionBusinessType.年终结息)) {
            return CollectionBusinessStatus.待入账.getName();
        } else if (collection.equals(CollectionBusinessType.汇缴) || collection.equals(CollectionBusinessType.补缴) || collection.equals(CollectionBusinessType.错缴更正)) {
            return CollectionBusinessStatus.待入账.getName();
        } else if (collection.equals(CollectionBusinessType.外部转入)) {
            return CollectionBusinessStatus.联系函审核通过.getName();
        } else if (collection.equals(CollectionBusinessType.外部转出)) {
            return CollectionBusinessStatus.账户信息审核通过.getName();
        } else {
            return CollectionBusinessStatus.办结.getName();
        }

    }

    /**
     * 转换状态机流程返回的“办结”为实际流程名称，贷款重构
     *
     * @param next
     * @param loan
     * @return
     */
    public static String step2Status(String next, LoanBusinessType loan) {

        if (!"办结".equals(next))
            return next;

        if ((loan == null)) {
            throw new ErrorException("业务类型参数异常，请确保有且只有一个枚举参数");
        }

        if (loan.equals(LoanBusinessType.贷款发放)) {

            return LoanBussinessStatus.待签合同.getName();

        } else if (loan.equals(LoanBusinessType.提前还款) || loan.equals(LoanBusinessType.逾期还款) || loan.equals(LoanBusinessType.结清)) {

            return LoanBussinessStatus.待入账.getName();

        } else {

            return LoanBussinessStatus.办结.getName();
        }

    }

    /**
     * 根据业务模块和业务类型获取在状态机中的业务子类型
     *
     * @param YWMK 业务模块（归集个人01，归集单位02,，个人贷款03，项目贷款04，财务日常05，财务活转定06，财务支取07）
     * @param YWLX
     * @return
     */
    public static String getSubTypeByBussinessType(String YWMK, String YWLX) {

        String subtype = null;

        if (BusinessDetailsModule.Collection_person.getCode().equals(YWMK)) {
            if (CollectionBusinessType.开户.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人账户设立.getSubType();
            } else if (CollectionBusinessType.变更.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人账户信息变更.getSubType();
            } else if (CollectionBusinessType.冻结.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_冻结个人账户.getSubType();
            } else if (CollectionBusinessType.解冻.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_解冻个人账户.getSubType();
            } else if (CollectionBusinessType.封存.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人账户封存.getSubType();
            } else if (CollectionBusinessType.启封.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人账户启封.getSubType();
            } else if (CollectionBusinessType.部分提取.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_提取.getSubType();
            } else if (CollectionBusinessType.销户提取.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_提取.getSubType();
            } else if (CollectionBusinessType.外部转入.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_转入个人接续.getSubType();
            } else if (CollectionBusinessType.外部转出.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_转出个人接续.getSubType();
            } else if (CollectionBusinessType.内部转移.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人账户内部转移.getSubType();
            }else if (CollectionBusinessType.合并.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_合并个人账户.getSubType();
            }else {
                return null;
            }

        } else if (BusinessDetailsModule.Collection_unit.getCode().equals(YWMK)) {
            if (CollectionBusinessType.开户.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位开户.getSubType();
            } else if (CollectionBusinessType.变更.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位账户变更.getSubType();
            } else if (CollectionBusinessType.封存.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位账户封存.getSubType();
            } else if (CollectionBusinessType.启封.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位账户启封.getSubType();
            } else if (CollectionBusinessType.缓缴处理.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位缓缴申请.getSubType();
            } else if (CollectionBusinessType.汇缴.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_缴存清册记录.getSubType();
            } else if (CollectionBusinessType.错缴更正.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_错缴更正.getSubType();
            } else if (CollectionBusinessType.补缴.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_补缴记录.getSubType();
            } else if (CollectionBusinessType.比例调整.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位缴存比例调整.getSubType();
            } else if (CollectionBusinessType.基数调整.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_个人基数调整.getSubType();
            } else if (CollectionBusinessType.销户.getCode().equals(YWLX)) {
                subtype = BusinessSubType.归集_单位账户销户.getSubType();
            } else {
                return null;
            }

        } else if (BusinessDetailsModule.Loan_person.getCode().equals(YWMK)) {
            if (LoanBusinessType.房开变更.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_房开变更受理.getSubType();
            } else if (LoanBusinessType.新建楼盘.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_楼盘申请受理.getSubType();
            } else if (LoanBusinessType.楼盘变更.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_楼盘变更受理.getSubType();
            } else if (LoanBusinessType.贷款发放.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_个人贷款申请.getSubType();
            } else if (LoanBusinessType.逾期还款.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_个人还款申请.getSubType();
            } else if (LoanBusinessType.提前还款.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_个人还款申请.getSubType();
            } else if (LoanBusinessType.结清.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_个人还款申请.getSubType();
            } else if (LoanBusinessType.合同变更.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_合同变更申请.getSubType();
            } else if (LoanBusinessType.新建房开.getCode().equals(YWLX)) {
                subtype = BusinessSubType.贷款_房开申请受理.getSubType();
            }
        } else {
            return null;
        }

        return subtype;
    }
}
