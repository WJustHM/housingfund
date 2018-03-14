package com.handge.housingfund.database.enums;

public enum BusinessType {
	Collection,WithDrawl,Finance,Loan
//	归集("归集","Collection"),
//	提取("提取","WithDrawl"),
//	财务("财务","Finance"),
//	贷款("贷款","Loan");


//	private String description;
//	private String type;
//
//	public String getDescription() {
//		return description;
//	}
//
//	public String getType() {
//		return type;
//	}
//
//	BusinessType(String description, String type) {
//		this.description = description;
//		this.type = type;
//	}

//	public static String getTypeByDesc(String desc){
//		BusinessType[] values = BusinessType.values();
//		for(BusinessType businessType : values){
//			if(businessType.getDescription().equals(desc)){
//				return businessType.getType();
//			}
//		}
//		throw new ErrorException("当前枚举编码："+desc+",不存在对应的枚举类型!（description值错误）");
//	}
//	public static String getDescByType(String type){
//		BusinessType[] values = BusinessType.values();
//		for(BusinessType businessType : values){
//			if(businessType.getType().equals(type)){
//				return businessType.getDescription();
//			}
//		}
//		throw new ErrorException("当前枚举编码："+type+",不存在对应的枚举类型!（type错误）");
//	}
}
