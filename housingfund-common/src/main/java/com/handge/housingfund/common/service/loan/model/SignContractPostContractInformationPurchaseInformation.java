package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostContractInformationPurchaseInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostContractInformationPurchaseInformation  implements Serializable {

   private  String    FWJZMJ;  //房屋建筑面积 

   private  String    SFRKHHM;  //售房人开户户名 

   private  String    FWZL;  //房屋坐落 

   private  String    FWTNMJ;  //房屋套内面积 

   private  String    FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） 

   @Annotation.Money(name = "房屋总价")
   private  String    FWZJ;  //房屋总价 

   private  String    JKRZJHM;  //借款人证件号码 

   private  String    SFRKHYHMC;  //售房人开户银行名称 

   private  String    DKQS;  //贷款期数 

   @Annotation.Money(name = "购房首付款")
   private  String    GFSFK;  //购房首付款 

   private  String    DKLX;  //贷款类型 

   private  String    JKRXM;  //借款人姓名 

   private  String    HTDKJE;  //合同贷款金额 

   private  String    SFRZHHM;  //售房人账户号码 

   @Annotation.Mobile(name = "手机号码")
   private  String    SJHM;  //手机号码 

   private  String    JKRGJJZH;  //借款人公积金账号 

   private  String    JKRZJLX;  //借款人证件类型 

   private  String    GFHTBH;  //购房合同编号 

   private  String    BLZL;   //办理资料  //

   private  String    SFRMC;  //售房人名称 

   private  String    DKDBLX;  //贷款担保类型 

   private  String    DKHKFS;  //贷款还款方式 

   public  String getFWJZMJ(){ 

       return this.FWJZMJ;   

   }


   public  void setFWJZMJ(String  FWJZMJ){ 

       this.FWJZMJ = FWJZMJ;   

   }


   public  String getSFRKHHM(){ 

       return this.SFRKHHM;   

   }


   public  void setSFRKHHM(String  SFRKHHM){ 

       this.SFRKHHM = SFRKHHM;   

   }


   public  String getFWZL(){ 

       return this.FWZL;   

   }


   public  void setFWZL(String  FWZL){ 

       this.FWZL = FWZL;   

   }


   public  String getFWTNMJ(){ 

       return this.FWTNMJ;   

   }


   public  void setFWTNMJ(String  FWTNMJ){ 

       this.FWTNMJ = FWTNMJ;   

   }


   public  String getFWXZ(){ 

       return this.FWXZ;   

   }


   public  void setFWXZ(String  FWXZ){ 

       this.FWXZ = FWXZ;   

   }


   public  String getFWZJ(){ 

       return this.FWZJ;   

   }


   public  void setFWZJ(String  FWZJ){ 

       this.FWZJ = FWZJ;   

   }


   public  String getJKRZJHM(){ 

       return this.JKRZJHM;   

   }


   public  void setJKRZJHM(String  JKRZJHM){ 

       this.JKRZJHM = JKRZJHM;   

   }


   public  String getSFRKHYHMC(){ 

       return this.SFRKHYHMC;   

   }


   public  void setSFRKHYHMC(String  SFRKHYHMC){ 

       this.SFRKHYHMC = SFRKHYHMC;   

   }


   public  String getDKQS(){ 

       return this.DKQS;   

   }


   public  void setDKQS(String  DKQS){ 

       this.DKQS = DKQS;   

   }


   public  String getGFSFK(){ 

       return this.GFSFK;   

   }


   public  void setGFSFK(String  GFSFK){ 

       this.GFSFK = GFSFK;   

   }


   public  String getDKLX(){ 

       return this.DKLX;   

   }


   public  void setDKLX(String  DKLX){ 

       this.DKLX = DKLX;   

   }


   public  String getJKRXM(){ 

       return this.JKRXM;   

   }


   public  void setJKRXM(String  JKRXM){ 

       this.JKRXM = JKRXM;   

   }


   public  String getHTDKJE(){ 

       return this.HTDKJE;   

   }


   public  void setHTDKJE(String  HTDKJE){ 

       this.HTDKJE = HTDKJE;   

   }


   public  String getSFRZHHM(){ 

       return this.SFRZHHM;   

   }


   public  void setSFRZHHM(String  SFRZHHM){ 

       this.SFRZHHM = SFRZHHM;   

   }


   public  String getSJHM(){ 

       return this.SJHM;   

   }


   public  void setSJHM(String  SJHM){ 

       this.SJHM = SJHM;   

   }


   public  String getJKRGJJZH(){ 

       return this.JKRGJJZH;   

   }


   public  void setJKRGJJZH(String  JKRGJJZH){ 

       this.JKRGJJZH = JKRGJJZH;   

   }


   public  String getJKRZJLX(){ 

       return this.JKRZJLX;   

   }


   public  void setJKRZJLX(String  JKRZJLX){ 

       this.JKRZJLX = JKRZJLX;   

   }


   public  String getGFHTBH(){ 

       return this.GFHTBH;   

   }


   public  void setGFHTBH(String  GFHTBH){ 

       this.GFHTBH = GFHTBH;   

   }


   public String  getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  String getSFRMC(){ 

       return this.SFRMC;   

   }


   public  void setSFRMC(String  SFRMC){ 

       this.SFRMC = SFRMC;   

   }


   public  String getDKDBLX(){ 

       return this.DKDBLX;   

   }


   public  void setDKDBLX(String  DKDBLX){ 

       this.DKDBLX = DKDBLX;   

   }


   public  String getDKHKFS(){ 

       return this.DKHKFS;   

   }


   public  void setDKHKFS(String  DKHKFS){ 

       this.DKHKFS = DKHKFS;   

   }


   public String toString(){ 

       return "SignContractPostContractInformationPurchaseInformation{" +  
 
              "FWJZMJ='" + this.FWJZMJ + '\'' + "," +
              "SFRKHHM='" + this.SFRKHHM + '\'' + "," +
              "FWZL='" + this.FWZL + '\'' + "," +
              "FWTNMJ='" + this.FWTNMJ + '\'' + "," +
              "FWXZ='" + this.FWXZ + '\'' + "," +
              "FWZJ='" + this.FWZJ + '\'' + "," +
              "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
              "SFRKHYHMC='" + this.SFRKHYHMC + '\'' + "," +
              "DKQS='" + this.DKQS + '\'' + "," +
              "GFSFK='" + this.GFSFK + '\'' + "," +
              "DKLX='" + this.DKLX + '\'' + "," +
              "JKRXM='" + this.JKRXM + '\'' + "," +
              "HTDKJE='" + this.HTDKJE + '\'' + "," +
              "SFRZHHM='" + this.SFRZHHM + '\'' + "," +
              "SJHM='" + this.SJHM + '\'' + "," +
              "JKRGJJZH='" + this.JKRGJJZH + '\'' + "," +
              "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
              "GFHTBH='" + this.GFHTBH + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "SFRMC='" + this.SFRMC + '\'' + "," +
              "DKDBLX='" + this.DKDBLX + '\'' + "," +
              "DKHKFS='" + this.DKHKFS + '\'' + 

      "}";

  } 
}