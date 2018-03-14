package com.handge.housingfund.database.enums;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;

/**
 * Created by Administrator on 2017/11/24.
 */
public enum ListAction {
    Previous("Previous", "1"),
    Next("Next", "2"),
    Last("Last", "3"),
    First("First", "4");

    private String name;
    private String code;

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

    ListAction(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static ListAction pageType(String  action) {
        ListAction listaction = null;
        switch (action) {
            case "4":
                listaction = ListAction.First;
                break;
            case "2":
                listaction = ListAction.Next;
                break;
            case "3":
                listaction = ListAction.Last;
                break;
            case "1":
                listaction = ListAction.Previous;
                break;
            default:
                throw new ErrorException(ReturnEnumeration.Parameter_NOT_MATCH, "非法的列表操作");

        }
        return listaction;
    }
}
