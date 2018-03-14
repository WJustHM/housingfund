package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostHouseInformationOverhaulInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostHouseInformationOverhaulInformation  implements Serializable {

   @Annotation.Money(name = "大修工程预算")
   private  String    DXGCYS;  //大修工程预算 

   @Annotation.BankCard(name = "个人收款银行账号")
   private  String    GRSKYHZH;  //个人收款银行账号 

   private  String    FWZJBGJGMCJBH;  //房屋质检报告机关名称及编号 

   private  String    FXHCS;  //土地使用证号 

   private  String    FWZL;  //房屋坐落 

   private  String    YJZMJ;  //原建筑面积 

   private  String    KHYHMC;  //开户银行名称 

   private  String    BLZL;   //办理资料  //

   private  String    YBDCZH;  //原不动产证号 

   private  String    JHJGRQ;  //计划竣工日期 

   private  String    JHKGRQ;  //计划开工日期 

   private  String    YHKHM;  //银行开户名 

   public  String getDXGCYS(){ 

       return this.DXGCYS;   

   }


   public  void setDXGCYS(String  DXGCYS){ 

       this.DXGCYS = DXGCYS;   

   }


   public  String getGRSKYHZH(){ 

       return this.GRSKYHZH;   

   }


   public  void setGRSKYHZH(String  GRSKYHZH){ 

       this.GRSKYHZH = GRSKYHZH;   

   }


   public  String getFWZJBGJGMCJBH(){ 

       return this.FWZJBGJGMCJBH;   

   }


   public  void setFWZJBGJGMCJBH(String  FWZJBGJGMCJBH){ 

       this.FWZJBGJGMCJBH = FWZJBGJGMCJBH;   

   }


   public  String getFXHCS(){ 

       return this.FXHCS;   

   }


   public  void setFXHCS(String  FXHCS){ 

       this.FXHCS = FXHCS;   

   }


   public  String getFWZL(){ 

       return this.FWZL;   

   }


   public  void setFWZL(String  FWZL){ 

       this.FWZL = FWZL;   

   }


   public  String getYJZMJ(){ 

       return this.YJZMJ;   

   }


   public  void setYJZMJ(String  YJZMJ){ 

       this.YJZMJ = YJZMJ;   

   }


   public  String getKHYHMC(){ 

       return this.KHYHMC;   

   }


   public  void setKHYHMC(String  KHYHMC){ 

       this.KHYHMC = KHYHMC;   

   }


   public  String getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  String getYBDCZH(){ 

       return this.YBDCZH;   

   }


   public  void setYBDCZH(String  YBDCZH){ 

       this.YBDCZH = YBDCZH;   

   }


   public  String getJHJGRQ(){ 

       return this.JHJGRQ;   

   }


   public  void setJHJGRQ(String  JHJGRQ){ 

       this.JHJGRQ = JHJGRQ;   

   }


   public  String getJHKGRQ(){ 

       return this.JHKGRQ;   

   }


   public  void setJHKGRQ(String  JHKGRQ){ 

       this.JHKGRQ = JHKGRQ;   

   }


   public  String getYHKHM(){ 

       return this.YHKHM;   

   }


   public  void setYHKHM(String  YHKHM){ 

       this.YHKHM = YHKHM;   

   }


   public String toString(){ 

       return "ApplicantPostHouseInformationOverhaulInformation{" +  
 
              "DXGCYS='" + this.DXGCYS + '\'' + "," +
              "GRSKYHZH='" + this.GRSKYHZH + '\'' + "," +
              "FWZJBGJGMCJBH='" + this.FWZJBGJGMCJBH + '\'' + "," +
              "FXHCS='" + this.FXHCS + '\'' + "," +
              "FWZL='" + this.FWZL + '\'' + "," +
              "YJZMJ='" + this.YJZMJ + '\'' + "," +
              "KHYHMC='" + this.KHYHMC + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "YBDCZH='" + this.YBDCZH + '\'' + "," +
              "JHJGRQ='" + this.JHJGRQ + '\'' + "," +
              "JHKGRQ='" + this.JHKGRQ + '\'' + "," +
              "YHKHM='" + this.YHKHM + '\'' + 

      "}";

  } 
}