package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseCapitalInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseCapitalInformation  implements Serializable {

    private  String    HKFS;  //还款方式（0：等额本息 1：等额本金 2：一次还款付息 3：自由还款方式 4：其他）

    private  String    HTDKJE;  //合同贷款金额

    private  String    LLFSBL;  //利率浮动比例

    private  String    HTDKJEDX;  //合同贷款金额大写

    private  String    DKLX;  //贷款类型（0：公积金贷款 1：组合贷款 2：贴息贷款 3：其他）

    private  String    JKHTLL;  //借款合同利率

    private  String    DKQS;  //贷款期数

    private  String    DKDBLX;  //贷款担保类型  （0：抵押 1：质押 2：保证 3：其他）

    private  String    WTKHYJCE;  //委托扣划月缴存额

    //private  String    ZXLL;  //执行利率

    private  String    FWTS;  //房屋套数（0：一套 1：二套 2：三套 3：四套  5：五套及以上）
    
    public  String getHKFS(){

        return this.HKFS;

    }


    public  void setHKFS(String  HKFS){

        this.HKFS = HKFS;

    }


    public  String getHTDKJE(){

        return this.HTDKJE;

    }


    public  void setHTDKJE(String  HTDKJE){

        this.HTDKJE = HTDKJE;

    }


    public  String getLLFSBL(){

        return this.LLFSBL;

    }


    public  void setLLFSBL(String  LLFSBL){

        this.LLFSBL = LLFSBL;

    }


    public  String getHTDKJEDX(){

        return this.HTDKJEDX;

    }


    public  void setHTDKJEDX(String  HTDKJEDX){

        this.HTDKJEDX = HTDKJEDX;

    }


    public  String getDKLX(){

        return this.DKLX;

    }


    public  void setDKLX(String  DKLX){

        this.DKLX = DKLX;

    }


    public  String getJKHTLL(){

        return this.JKHTLL;

    }


    public  void setJKHTLL(String  JKHTLL){

        this.JKHTLL = JKHTLL;

    }


    public  String getDKQS(){

        return this.DKQS;

    }


    public  void setDKQS(String  DKQS){

        this.DKQS = DKQS;

    }


    public  String getDKDBLX(){

        return this.DKDBLX;

    }


    public  void setDKDBLX(String  DKDBLX){

        this.DKDBLX = DKDBLX;

    }


    public  String getWTKHYJCE(){

        return this.WTKHYJCE;

    }


    public  void setWTKHYJCE(String  WTKHYJCE){

        this.WTKHYJCE = WTKHYJCE;

    }


//    public  String getZXLL(){
//
//        return this.ZXLL;
//
//    }
//
//
//    public  void setZXLL(String  ZXLL){
//
//        this.ZXLL = ZXLL;
//
//    }


    public  String getFWTS(){

        return this.FWTS;

    }


    public  void setFWTS(String  FWTS){

        this.FWTS = FWTS;

    }


    public String toString(){

        return "ApplicantPostCapitalInformation{" +

                "HKFS='" + this.HKFS + '\'' + "," +
                "HTDKJE='" + this.HTDKJE + '\'' + "," +
                "LLFSBL='" + this.LLFSBL + '\'' + "," +
                "HTDKJEDX='" + this.HTDKJEDX + '\'' + "," +
                "DKLX='" + this.DKLX + '\'' + "," +
                "JKHTLL='" + this.JKHTLL + '\'' + "," +
                "DKQS='" + this.DKQS + '\'' + "," +
                "DKDBLX='" + this.DKDBLX + '\'' + "," +
                "WTKHYJCE='" + this.WTKHYJCE + '\'' + "," +
                //"ZXLL='" + this.ZXLL + '\'' + "," +
                "FWTS='" + this.FWTS + '\'' +

                "}";

    }
}