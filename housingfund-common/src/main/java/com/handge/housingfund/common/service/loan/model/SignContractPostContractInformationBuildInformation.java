package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "SignContractPostContractInformationBuildInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignContractPostContractInformationBuildInformation  implements Serializable {

   private  String    FWJZMJ;  //房屋建筑面积 

   @Annotation.BankCard(name = "个人收款银行账号")
   private  String    GRSKYHZH;  //个人收款银行账号 

   private  String    FWZL;  //房屋坐落 

   private  String    FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） 

   private  String    JKRZJHM;  //借款人证件号码 

   private  String    KHYHMC;  //开户银行名称 

   private  String    KHHM;  //开户户名 

   private  String    DKLX;  //贷款类型 

   private  String    DKQS;  //贷款期数 

   private  String    JKRXM;  //借款人姓名 

   @Annotation.Money(name = "个人使用资金")
   private  String    GRSYZJ;  //个人使用资金 

   @Annotation.Money(name = "合同贷款金额")
   private  String    HTDKJE;  //合同贷款金额 

   private  String    JKRGJJZH;  //借款人公积金账号 

   private  String    JKRZJLX;  //借款人证件类型 

   private  String    BLZL;   //办理资料  //

   private  String    DKDBLX;  //贷款担保类型 

   private  String    DKHKFS;  //贷款还款方式 

   @Annotation.Money(name = "计划建造总价")
   private  String    JHJZZJ;  //计划建造总价 

   public  String getFWJZMJ(){ 

       return this.FWJZMJ;   

   }


   public  void setFWJZMJ(String  FWJZMJ){ 

       this.FWJZMJ = FWJZMJ;   

   }


   public  String getGRSKYHZH(){ 

       return this.GRSKYHZH;   

   }


   public  void setGRSKYHZH(String  GRSKYHZH){ 

       this.GRSKYHZH = GRSKYHZH;   

   }


   public  String getFWZL(){ 

       return this.FWZL;   

   }


   public  void setFWZL(String  FWZL){ 

       this.FWZL = FWZL;   

   }


   public  String getFWXZ(){ 

       return this.FWXZ;   

   }


   public  void setFWXZ(String  FWXZ){ 

       this.FWXZ = FWXZ;   

   }


   public  String getJKRZJHM(){ 

       return this.JKRZJHM;   

   }


   public  void setJKRZJHM(String  JKRZJHM){ 

       this.JKRZJHM = JKRZJHM;   

   }


   public  String getKHYHMC(){ 

       return this.KHYHMC;   

   }


   public  void setKHYHMC(String  KHYHMC){ 

       this.KHYHMC = KHYHMC;   

   }


   public  String getKHHM(){ 

       return this.KHHM;   

   }


   public  void setKHHM(String  KHHM){ 

       this.KHHM = KHHM;   

   }


   public  String getDKLX(){ 

       return this.DKLX;   

   }


   public  void setDKLX(String  DKLX){ 

       this.DKLX = DKLX;   

   }


   public  String getDKQS(){ 

       return this.DKQS;   

   }


   public  void setDKQS(String  DKQS){ 

       this.DKQS = DKQS;   

   }


   public  String getJKRXM(){ 

       return this.JKRXM;   

   }


   public  void setJKRXM(String  JKRXM){ 

       this.JKRXM = JKRXM;   

   }


   public  String getGRSYZJ(){ 

       return this.GRSYZJ;   

   }


   public  void setGRSYZJ(String  GRSYZJ){ 

       this.GRSYZJ = GRSYZJ;   

   }


   public  String getHTDKJE(){ 

       return this.HTDKJE;   

   }


   public  void setHTDKJE(String  HTDKJE){ 

       this.HTDKJE = HTDKJE;   

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


   public String  getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

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


   public  String getJHJZZJ(){ 

       return this.JHJZZJ;   

   }


   public  void setJHJZZJ(String  JHJZZJ){ 

       this.JHJZZJ = JHJZZJ;   

   }


   public String toString(){ 

       return "SignContractPostContractInformationBuildInformation{" +  
 
              "FWJZMJ='" + this.FWJZMJ + '\'' + "," +
              "GRSKYHZH='" + this.GRSKYHZH + '\'' + "," +
              "FWZL='" + this.FWZL + '\'' + "," +
              "FWXZ='" + this.FWXZ + '\'' + "," +
              "JKRZJHM='" + this.JKRZJHM + '\'' + "," +
              "KHYHMC='" + this.KHYHMC + '\'' + "," +
              "KHHM='" + this.KHHM + '\'' + "," +
              "DKLX='" + this.DKLX + '\'' + "," +
              "DKQS='" + this.DKQS + '\'' + "," +
              "JKRXM='" + this.JKRXM + '\'' + "," +
              "GRSYZJ='" + this.GRSYZJ + '\'' + "," +
              "HTDKJE='" + this.HTDKJE + '\'' + "," +
              "JKRGJJZH='" + this.JKRGJJZH + '\'' + "," +
              "JKRZJLX='" + this.JKRZJLX + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "DKDBLX='" + this.DKDBLX + '\'' + "," +
              "DKHKFS='" + this.DKHKFS + '\'' + "," +
              "JHJZZJ='" + this.JHJZZJ + '\'' + 

      "}";

  } 
}