package com.handge.housingfund.finance.service;

/**
 * Created by xuefei_wang on 17-9-9.
 */
public class ReportConstant {

    public enum Assets {
        住房公积金存款("101", "住房公积金存款"),
        增值收益存款("102", "增值收益存款"),
        应收利息("111", "应收利息"),
        其他应收款("119", "其他应收款"),
        委托贷款("121", "委托贷款"),
        逾期贷款("122", "逾期贷款"),
        国家债券("124", "国家债券");
        private String id;
        private String name;

        Assets(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum Liabilities {
        住房公积金("201", "住房公积金"),
        应付利息("211", "应付利息"),
        其他应付款("219", "其他应付款"),
        专项应付款("214", "专项应付款"),
        城市廉租住房建设补充资金("21402", "城市廉租住房建设补充资金"),;
        private String id;
        private String name;

        Liabilities(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum Equity {
        贷款风险准备("301", "贷款风险准备"),
        增值收益("311", "增值收益"),
        待分配增值收益("32103", "待分配增值收益"),
        增值收益分配_提取贷款风险准备金("3210102", "增值收益分配-提取贷款风险准备金"),
        增值收益分配_提取公积金中心管理费用("3210103", "增值收益分配-提取公积金中心管理费用"),
        增值收益分配_城市廉租住房补充资金("3210104", "增值收益分配-城市廉租住房补充资金");
        private String id;
        private String name;

        Equity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public enum BusinessIncome {
        业务收入("401", "业务收入"),
        住房公积金利息收入("40101", "住房公积金利息收入"),
        增值收益利息收入("40102", "增值收益利息收入"),
        委托贷款利息收入("40103", "委托贷款利息收入"),
        国家债券利息收入("40104", "国家债券利息收入"),
        其他收入("40105", "其他收入");

        private String id;
        private String name;

        BusinessIncome(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum BusinessExpense {
        业务支出("411", "业务支出"),
        住房公积金利息支出("41101", "住房公积金利息支出"),
        住房公积金归集手续费支出("41102", "住房公积金归集手续费支出"),
        委托贷款手续费支出("41103", "委托贷款手续费支出"),
        增值收益("311", "增值收益");
        private String id;
        private String name;

        BusinessExpense(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}