package com.handge.housingfund.database.enums;

import com.handge.housingfund.common.service.collection.enumeration.CollectionBusinessType;
import com.handge.housingfund.common.service.finance.model.enums.FinanceBusinessType;
import com.handge.housingfund.common.service.loan.enums.LoanBusinessType;
import com.handge.housingfund.common.service.util.ErrorException;

/**
 * Created by 周君 on 2017/8/18.
 */
public enum BusinessSubType {

    /*  目前有效的子模块前缀：{DW GR JC TQ} {FK LP DK HK HT}
     *  依据是审核页面的布局
     */
    归集_补缴记录("缴存补缴", "缴存补缴", CollectionBusinessType.补缴.getCode()),
    归集_汇缴记录("缴存汇缴", "缴存汇缴", CollectionBusinessType.汇缴.getCode()),
    归集_错缴更正("缴存错缴", "缴存错缴", CollectionBusinessType.错缴更正.getCode()),
    归集_缴存清册记录("缴存清册", "缴存清册", CollectionBusinessType.单位清册.getCode()),
    归集_单位缓缴申请("缴存单位缓缴", "缴存单位缓缴", CollectionBusinessType.缓缴处理.getCode()),
    归集_个人基数调整("缴存基数调整", "缴存基数调整", CollectionBusinessType.基数调整.getCode()),

    归集_单位开户("单位开户", "单位开户", CollectionBusinessType.开户.getCode()),
    归集_单位账户变更("单位变更", "单位变更", CollectionBusinessType.变更.getCode()),
    归集_单位账户销户("单位销户", "单位销户", CollectionBusinessType.销户.getCode()),
    归集_单位缴存比例调整("单位缴存比例调整", "单位缴存比例调整", CollectionBusinessType.比例调整.getCode()),
    归集_单位账户封存("单位封存", "单位封存", CollectionBusinessType.封存.getCode()),
    归集_单位账户启封("单位启封", "单位启封", CollectionBusinessType.启封.getCode()),

    归集_个人账户设立("个人开户", "个人开户", CollectionBusinessType.开户.getCode()),
    归集_合并个人账户("个人合并", "个人合并", CollectionBusinessType.合并.getCode()),
    归集_个人账户信息变更("个人变更", "个人变更", CollectionBusinessType.变更.getCode()),
    归集_个人账户内部转移("个人内部转移", "个人内部转移", CollectionBusinessType.内部转移.getCode()),
    归集_个人账户封存("个人封存", "个人封存", CollectionBusinessType.封存.getCode()),
    归集_个人账户启封("个人启封", "个人启封", CollectionBusinessType.启封.getCode()),
    归集_冻结个人账户("个人冻结", "个人冻结", CollectionBusinessType.冻结.getCode()),
    归集_解冻个人账户("个人解冻", "个人解冻", CollectionBusinessType.解冻.getCode()),
    归集_转出个人接续("转出个人接续", "转出个人接续", CollectionBusinessType.外部转出.getCode()),
    归集_转入个人接续("转入个人接续", "转入个人接续", CollectionBusinessType.外部转入.getCode()),

    //todo 考虑拆分
    归集_提取("提取个人", "提取个人", null),

    贷款_房开申请受理("房开申请", "房开申请", LoanBusinessType.新建房开.getCode()),
    贷款_房开变更受理("房开变更", "房开变更", LoanBusinessType.房开变更.getCode()),
    贷款_楼盘申请受理("楼盘申请", "楼盘申请", LoanBusinessType.新建楼盘.getCode()),
    贷款_楼盘变更受理("楼盘变更", "楼盘变更", LoanBusinessType.楼盘变更.getCode()),
    贷款_个人贷款申请("贷款个人申请", "贷款个人申请", LoanBusinessType.贷款发放.getCode()),
    //todo 考虑拆分
    贷款_个人还款申请("还款个人申请", "还款个人申请", null),
    贷款_合同变更申请("合同变更", "合同变更", LoanBusinessType.合同变更.getCode()),

    财务_定期活期转定期("定期活期转定期", "定期活期转定期", null),
    财务_定期支取("定期支取","定期支取", null),

    财务_日常处理("日常处理","日常处理" ,FinanceBusinessType.日常财务处理.getCode()),
    //=========================================
    财务_增值收益利息收入("日常增值收益利息收入","日常增值收益利息收入"),
    财务_国家债券利息收入("日常国家债券利息收入","日常国家债券利息收入"),
    财务_暂收分摊("日常暂收分摊","日常暂收分摊"),
    财务_同行转账("日常同行转账","日常同行转账"),
    财务_跨行转账("日常跨行转账","日常跨行转账"),
    财务_计提个人贷款手续费("日常计提个人贷款手续费","日常计提个人贷款手续费"),
    财务_计提项目贷款手续费("日常计提项目贷款手续费","日常计提项目贷款手续费"),
    财务_支付住房公积金归集手续费("日常支付住房公积金归集手续费","日常支付住房公积金归集手续费"),
    财务_支付个人贷款手续费("日常支付个人贷款手续费","日常支付个人贷款手续费"),
    财务_支付项目贷款手续费("日常支付项目贷款手续费","日常支付项目贷款手续费"),
    财务_支付其他应付款("日常支付其他应付款","日常支付其他应付款"),
    财务_支付补息贷款利息("日常支付补息贷款利息","日常支付补息贷款利息"),
    财务_增值收益分配("日常增值收益分配","日常增值收益分配"),
    财务_年度结转增值收益("日常年度结转增值收益","日常年度结转增值收益"),
    财务_未分摊转移("日常未分摊转移","日常未分摊转移"),
    财务_支付专项应付款("日常支付专项应付款","日常支付专项应付款"),
    财务_扣除结余("日常扣除结余","日常扣除结余"),
    财务_普通财务("日常普通财务","日常普通财务"),
    财务_普通需审财务("日常普通需审财务","日常普通需审财务"),

    财务_住房公积金利息收入("日常住房公积金利息收入","日常住房公积金利息收入");




    private String description;
    private String subType;
    private String typeCode;

    public String getDescription() {
        return description;
    }

    public String getSubType() {
        return subType;
    }

    public String getTypeCode() {
        return typeCode;
    }

    BusinessSubType(String description, String subType, String typeCode) {
        this.description = description;
        this.subType = subType;
        this.typeCode = typeCode;
    }

    BusinessSubType(String description, String subType) {
        this.description = description;
        this.subType = subType;
    }
    public static String getSubTypeByDesc(String desc){
        BusinessSubType[] values = BusinessSubType.values();
        for(BusinessSubType businessSubType : values){
            if(businessSubType.getDescription().equals(desc)){
                return businessSubType.getSubType();
            }
        }
        throw new ErrorException("当前枚举编码："+desc+",不存在对应的枚举类型!（description值错误）");
    }
    public static String getDescBySubType(String subType){
        BusinessSubType[] values = BusinessSubType.values();
        for(BusinessSubType businessSubType : values){
            if(businessSubType.getSubType().equals(subType)){
                return businessSubType.getDescription();
            }
        }
        throw new ErrorException("当前枚举编码："+subType+",不存在对应的枚举类型!（subType错误）");
    }

    public static String getTypeCodeBySubType(String subType) {
        BusinessSubType[] values = BusinessSubType.values();
        for (BusinessSubType businessSubType :values) {
            if (businessSubType.getSubType().equals(subType)) {
                return businessSubType.getTypeCode();
            }
        }
        throw new ErrorException("当前枚举编码："+subType+",不存在对应的枚举类型!（subType错误）");
    }

}

