package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sjw on 2017/10/25.
 */
@XmlRootElement(name = "ImportExcelRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportExcelRes implements Serializable {

    private static final long serialVersionUID = 5361085019868527623L;

    private String success_num; //成功条数

    private String fail_num; //失败条数


    private ArrayList ImportExcelErrorListRes; //错误提示列表


    public ArrayList getImportExcelErrorListRes() {
        return ImportExcelErrorListRes;
    }

    public void setImportExcelErrorListRes(ArrayList importExcelErrorListRes) {
        ImportExcelErrorListRes = importExcelErrorListRes;
    }


    public String getSuccess_num() {
        return success_num;
    }

    public void setSuccess_num(String success_num) {
        this.success_num = success_num;
    }

    public String getFail_num() {
        return fail_num;
    }

    public void setFail_num(String fail_num) {
        this.fail_num = fail_num;
    }





    public String toString(){
        return "ImportExcelRes{" +
            "success_num='" + this.success_num + '\'' + "," +
            "fail_num='" + this.fail_num + '\'' + "," +
            "fail_list='" + this.ImportExcelErrorListRes + '\'' + "," +
            "}";

    }
}
