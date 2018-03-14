package com.handge.housingfund.database.enums;

import com.handge.housingfund.common.service.util.ErrorException;

/**
 * Created by sjw on 2017/11/15.
 */
public enum Postalcode {

    威宁县管理部("威宁县管理部", "553100"),
    纳雍县管理部("纳雍县管理部", "553300"),
    织金县管理部("织金县管理部", "562100"),
    金沙县管理部("金沙县管理部", "561800"),
    黔西县管理部("黔西县管理部", "551500"),
    大方县管理部("大方县管理部", "551600"),
    七星关区管理部("七星关区管理部", "551700"),
    市直管理部("市直管理部", "551700"),
    百管委管理部("百管委管理部", "551614"),
    赫章县管理部("赫章县管理部", "553200");

    private String name;

    private String code;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    Postalcode(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public static String getCode(String name){
        Postalcode[] values = Postalcode.values();
        for(Postalcode postalcode : values){
            if(postalcode.getName().equals(name)){
                return postalcode.getCode();
            }
        }
        throw new ErrorException("当前的业务网点："+name+",不存在对应的枚举类型!（description值错误）");
    }

}
