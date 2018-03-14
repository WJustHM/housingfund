package com.handge.housingfund.database.enums;

public enum DictionaryType {
	BuildingProjectLevel("建设项目分类级别编码表"),
	BuildingProjectType("建设项目类型编码表"), 
	ChartOfAccounts("建设项目类型编码表"), 
	CollectionAndExtractionDetailType("归集和提取业务明细类型编码表"),
	CreditOrDebt("借贷方向编码表"),
	EntryState("入账状态编码表"),
	ExtrationType("提取方式编码表"),
	FundBusinessType("资金业务类型编码表"),
	GovernmentLoanType("国债种类编码表"),
	LoanDetailType("贷款业务明细类型代码表"),
	LoanGuaranteeType("贷款担保类型编码表"),
	LoanPaymentMode("贷款还款方式编码表"),
	LoanRiskRating("贷款风险等级编码表"),
	LoanType("贷款类型编码表"),
	NatureOfAccount("专户性质编码表"),
	PersonalAccountState("个人账户状态编码表"),
	PersonalCertificate("个人证件类型编码表"),
	PersonalExtractionAndCollectionReason("个人提取销户原因表编码表"),
	ProjectLoanDetailType("项目贷款明细类型编码表"),
	ProjectLoanPaymentMode("项目贷款发放方式编码表"),
	ProjectLoanPledgeType("项目贷款抵押物类型编码表"),
	ProjectLoanRepaymentMode("项目贷款还本方式编码表"),
	ProjectLoanUsage("项目贷款资金支付用途编码表"),
	RateOfInterest("利率类型编码表"),
	ReverseMark("冲账标识代码表"),
	SaleChannel("购买渠道编码表"),
	SourcesOfFunds("资金流入来源编码表"),
	StateOfHouse("房屋性质编码表"),
	SubjectAttribute("科目属性编码表"),
	SubjectBalanceDirection("科目余额方向编码表"),
	UnitAccountState("缴存单位销户原因编码表"),
	UnitCancellationReason("缴存单位销户原因编码表"),
	Withdrawal("支取情况编码表");
	
	private String description;

	DictionaryType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
