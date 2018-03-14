package com.handge.housingfund.common.service.finance.model.enums;

/**
 * Created by tanyi on 2017/10/10.
 */

/*财务报表类型*/
public enum FinanceReportType {

    住房公积金贷款发放和账户余额变动情况统计表("03","住房公积金贷款发放和账户余额变动情况统计表"),
    住房公积金存贷款报备表("04","住房公积金存贷款报备表"),
    住房公积金资产负债表_月("71","住房公积金资产负债表_月"),
    住房公积金资产负债表_季度("72","住房公积金资产负债表_季度"),
    住房公积金资产负债表_年度("73","住房公积金资产负债表_年度"),
    住房公积金增值收益表_月("81","住房公积金增值收益表_月"),
    住房公积金增值收益表_季度("82","住房公积金增值收益表_季度"),
    住房公积金增值收益表_年度("83","住房公积金增值收益表_年度"),
    住房公积金增值收益分配表_年("91","住房公积金增值收益分配表_年"),
    住房公积金全市暂收未分摊统计表("92", "住房公积金全市暂收未分摊统计表");

    private String type;

    private String name;

    FinanceReportType(String type , String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
