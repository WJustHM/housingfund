package com.handge.housingfund.database.enums;

import com.handge.housingfund.common.service.util.ErrorException;

public enum StateMachineBusinessType {
	归集("归集","Collection"),
	提取("提取","WithDrawl"),
	财务("财务","Finance"),
	贷款("贷款","Loan");


	private String description;
	private String type;

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	StateMachineBusinessType(String description, String type) {
		this.description = description;
		this.type = type;
	}

	public static String getTypeByDesc(String desc){
		StateMachineBusinessType[] values = StateMachineBusinessType.values();
		for(StateMachineBusinessType businessType : values){
			if(businessType.getDescription().equals(desc)){
				return businessType.getType();
			}
		}
		throw new ErrorException("不存在该业务类型 "+desc);
	}
	public static String getDescByType(String type){
		StateMachineBusinessType[] values = StateMachineBusinessType.values();
		for(StateMachineBusinessType businessType : values){
			if(businessType.getType().equals(type)){
				return businessType.getDescription();
			}
		}
		throw new ErrorException("不存在该业务类型 "+type);
	}
}
