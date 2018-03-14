package com.handge.housingfund.common.service.collection.enumeration;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.StringUtil;

import java.util.ArrayList;


public enum CollectionBusinessStatus {

    所有("所有","00"),

    /*表示中间普通流程*/
    新建("新建","01"),
    待入账("待入账", "02"),
    待签合同("待签合同", "03"),
    待放款("待放款", "04"),
    已发放("已发放", "05"),
    待确认("待确认", "06"),
//    联系函新建("联系函新建", "07"),
    联系函审核通过("联系函审核通过", "08"),   //转移接续专用
    联系函确认接收("联系函确认接收", "09"),   //转移接续专用
    账户信息审核通过("账户信息审核通过", "10"), //转移接续专用
    转出地已转账("转出地已转账", "11"), //转移接续专用
    转出审核不通过("转出审核不通过", "12"),   //转移接续专用
    协商中("协商中", "13"),   //转移接续专用
    转账中("转账中", "14"),   //转移接续专用


    /*表示业务失败（暂时），可能需要手动处理*/
    审核不通过("审核不通过", "80"),
    入账失败("入账失败", "81"),
    转入撤销业务办结("转入撤销业务办结", "82"), //转移接续专用
    转出失败业务办结("转出失败业务办结", "83"), //转移接续专用
    协商后业务办结("协商后业务办结", "84"),   //转移接续专用

    /*表示待sb.审核，编码都是98*/
    待审核("待审核", "98"),
    待某人审核("待%审核","98"),

    /*表示业务办结，编码都是99*/
    办结("办结", "99"),
    正常办结("正常办结", "99"), //转移接续专用
    已入账分摊("已入账分摊", "99"),
    已入账("已入账", "99"),
    已放款("已放款", "99"),
    已作废("已作废", "99"),

    初始状态("初始状态", "-1")
    ;

    private String name;

    private String code;

    CollectionBusinessStatus(String name, String code) {
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

    public static String getNameByCode(String code){
        if(StringUtil.isEmpty(code)){
            return CollectionBusinessStatus.所有.getName();
        }
        CollectionBusinessStatus[] values = CollectionBusinessStatus.values();
        for(CollectionBusinessStatus businessStatus : values){
            if(businessStatus.getCode().equals(code)){
                return businessStatus.getName();
            }
        }
        throw new ErrorException("当前枚举编码："+code+",不存在对应的枚举类型!（业务状态码值错误）");
    }
    public ArrayList<CollectionBusinessStatus>getSubTypes(){

        ArrayList<CollectionBusinessStatus> list = new ArrayList<>();

        CollectionBusinessStatus[] values = CollectionBusinessStatus.values();

        for(CollectionBusinessStatus businessStatus : values){
            if(businessStatus.getCode().equals(code)){

                list.add(businessStatus);

            }
        }
        return list;
    }

    public static String getCodeByName(String name){
        CollectionBusinessStatus[] values = CollectionBusinessStatus.values();
        for(CollectionBusinessStatus businessStatus : values){
            if(businessStatus.getName().equals(name)){
                return businessStatus.getName();
            }
        }
        throw new ErrorException("当前枚举名称："+name+"不存在对应的枚举类型!");
    }

}
