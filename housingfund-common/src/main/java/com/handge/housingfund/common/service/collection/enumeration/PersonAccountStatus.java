package com.handge.housingfund.common.service.collection.enumeration;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;

/**
 * Created by Liujuhao on 2017/8/16.
 */
public enum PersonAccountStatus {

    所有("所有","00"),
    正常("正常","01"),
    封存("封存","02"),
    合并销户("合并销户","03"),
    外部转出销户("外部转出销户","04"),
    提取销户("提取销户","05"),
    冻结("冻结","06"),
    其他("其他","99")
    ;

    private String name;

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    PersonAccountStatus(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static String getNameByCode(String code){
        if(StringUtil.isEmpty(code)){
            return PersonAccountStatus.所有.getName();
        }
        PersonAccountStatus[] values = PersonAccountStatus.values();
        for(PersonAccountStatus personAccountStatus : values){
            if(personAccountStatus.getCode().equals(code)){
                return personAccountStatus.getName();
            }
        }
        throw new ErrorException("当前枚举编码："+code+",不存在对应的枚举类型!（业务状态码值错误）");
    }
}
