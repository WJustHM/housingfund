package com.handge.housingfund.common.service.loan.enums;

/**
 * 创建人Dalu Guo
 * 创建时间 2017/8/16.
 * 描述
 */
public enum LoanBussinessStatus {

    //贷款模块（最终应保持和归集一致）

    所有("所有","00"),

    /*表示中间普通流程*/
    新建("新建","01"),
    待入账("待入账", "02"),
    待签合同("待签合同", "03"),
    待放款("待放款", "04"),
    已发放("已发放", "05"),
    待确认("待确认", "07"),

    /*表示业务失败（暂时），可能需要手动处理*/
    审核不通过("审核不通过", "80"),
    入账失败("入账失败", "81"),
    扣款已发送("扣款已发送","82"),
    逾期("逾期","83"),

    /*表示待sb.审核，编码都是98*/
    待审核("待审核", "98"),
    待某人审核("待%审核","98"),

    /*表示业务办结，编码都是99*/
    办结("办结", "99"),
    已入账分摊("已入账分摊", "99"),
    已入账("已入账", "99"),
    已到账("已到账","99"),
    已放款("已放款", "99"),
    已作废("已作废", "99"),

    初始状态("初始状态", "-1")
    ;

    private String name;

    private String code;

    LoanBussinessStatus(String name, String code) {
        this.name = name;
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

    public void setCode(String code) {
        this.code = code;
    }
}
