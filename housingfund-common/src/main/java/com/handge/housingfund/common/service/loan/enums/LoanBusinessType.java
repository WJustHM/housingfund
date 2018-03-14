package com.handge.housingfund.common.service.loan.enums;

import com.handge.housingfund.common.service.util.StringUtil;

@SuppressWarnings("NonAsciiCharacters")
public enum LoanBusinessType {

    所有("所有", "00"),
    贷款发放("贷款发放", "01"),
    正常还款("正常还款", "02"),
    提前还款("提前还款", "03"),
    逾期还款("逾期还款", "04"),
    公积金提取还款("公积金提取还款", "05"),
    结清("结清", "06"),
    贷款展期("贷款展期", "07"),
    贷款缩期("贷款缩期", "08"),
    核销("核销", "09"),
    其他("其他", "99"),

    //自定义在此添加...

    /*贷款申请("贷款申请", "70"),*/ //划分到：贷款发放
    /*签订合同("签订合同", "71"),*/ //去掉（看作“贷款发放”的一部分）
    新建房开("新建房开", "72"), //不写入st表
    新建楼盘("新建楼盘", "73"), //不写入st表
    合同变更("合同变更", "74"), //不写入st表
    房开变更("房开变更", "76"), //不写入st表
    楼盘变更("楼盘变更", "77"); //不写入st表

    //

    private String code;

    private String name;

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }


    LoanBusinessType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static String getNameByCode(String code) {
        if (StringUtil.isEmpty(code)) {
            return LoanBusinessType.所有.getName();
        }
        LoanBusinessType[] values = LoanBusinessType.values();
        for (LoanBusinessType businessStatus : values) {
            if (businessStatus.getCode().equals(code)) {
                return businessStatus.getName();
            }
        }
        return code;
    }
}
