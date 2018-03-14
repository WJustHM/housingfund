package com.handge.housingfund.common.service.collection.enumeration;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;

/**
 * Created by Liujuhao on 2017/8/16.
 */
public enum UnitAccountStatus {

    所有("所有","00"),
    正常("正常","01"),
    开户("开户","02"),
    缓缴("缓缴","03"),
    销户("销户","04"),
    封存("封存","05"),
    其他("其他","99");

    private String code;

    private String name;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    UnitAccountStatus(String name, String code) {
        this.code = code;

        this.name = name;
    }

    public static String getNameByCode(String code){
        if(StringUtil.isEmpty(code)){
            return UnitAccountStatus.所有.getName();
        }
        UnitAccountStatus[] values = UnitAccountStatus.values();
        for(UnitAccountStatus unitAccountStatus : values){
            if(unitAccountStatus.getCode().equals(code)){
                return unitAccountStatus.getName();
            }
        }
        throw new ErrorException("当前枚举编码："+code+",不存在对应的枚举类型!（业务状态码值错误）");
    }
}
