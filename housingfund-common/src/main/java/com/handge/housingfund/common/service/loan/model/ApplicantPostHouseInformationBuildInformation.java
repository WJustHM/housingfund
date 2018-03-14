package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostHouseInformationBuildInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostHouseInformationBuildInformation  implements Serializable {

   @Annotation.BankCard(name = "个人收款银行账号")
   private  String    GRSKYHZH;  //个人收款银行账号 

   private  String    FWZL;  //房屋坐落 

   private  String    JSGCSGXKZH;  //建设工程施工许可证号 

   private  String    FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） 

   private  String    FWJG;  //房屋结构（0：框架 1：砖混 2：其他） 

   private  String    KHYHMC;  //开户银行名称 

   private  String    JHJGRQ;  //计划竣工日期 

   private  String    TDSYZH;  //土地使用证号 

   @Annotation.Money(name = "个人使用资金")
   private  String    GRSYZJ;  //个人使用资金 

   private  String    JZCS;  //建造层数 

   private  String    YHKHM;  //银行开户名 

   private  String    JZYDGHXKZH;  //建造用地规划许可证号 

   private  String    PZJGWH;  //批准机关文号 

   private  String    JHJZFY;  //计划建造费用 

   private  String    BLZL;   //办理资料  //

   private  String    JZGCGHXKZH;  //建造工程规划许可证号 

   private  String    JCHJZMJ;  //建成后建造面积 

   private  String    JHKGRQ;  //计划开工日期 

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


   public  String getJSGCSGXKZH(){ 

       return this.JSGCSGXKZH;   

   }


   public  void setJSGCSGXKZH(String  JSGCSGXKZH){ 

       this.JSGCSGXKZH = JSGCSGXKZH;   

   }


   public  String getFWXZ(){ 

       return this.FWXZ;   

   }


   public  void setFWXZ(String  FWXZ){ 

       this.FWXZ = FWXZ;   

   }


   public  String getFWJG(){ 

       return this.FWJG;   

   }


   public  void setFWJG(String  FWJG){ 

       this.FWJG = FWJG;   

   }


   public  String getKHYHMC(){ 

       return this.KHYHMC;   

   }


   public  void setKHYHMC(String  KHYHMC){ 

       this.KHYHMC = KHYHMC;   

   }


   public  String getJHJGRQ(){ 

       return this.JHJGRQ;   

   }


   public  void setJHJGRQ(String  JHJGRQ){ 

       this.JHJGRQ = JHJGRQ;   

   }


   public  String getTDSYZH(){ 

       return this.TDSYZH;   

   }


   public  void setTDSYZH(String  TDSYZH){ 

       this.TDSYZH = TDSYZH;   

   }


   public  String getGRSYZJ(){ 

       return this.GRSYZJ;   

   }


   public  void setGRSYZJ(String  GRSYZJ){ 

       this.GRSYZJ = GRSYZJ;   

   }


   public  String getJZCS(){ 

       return this.JZCS;   

   }


   public  void setJZCS(String  JZCS){ 

       this.JZCS = JZCS;   

   }


   public  String getYHKHM(){ 

       return this.YHKHM;   

   }


   public  void setYHKHM(String  YHKHM){ 

       this.YHKHM = YHKHM;   

   }


   public  String getJZYDGHXKZH(){ 

       return this.JZYDGHXKZH;   

   }


   public  void setJZYDGHXKZH(String  JZYDGHXKZH){ 

       this.JZYDGHXKZH = JZYDGHXKZH;   

   }


   public  String getPZJGWH(){ 

       return this.PZJGWH;   

   }


   public  void setPZJGWH(String  PZJGWH){ 

       this.PZJGWH = PZJGWH;   

   }


   public  String getJHJZFY(){ 

       return this.JHJZFY;   

   }


   public  void setJHJZFY(String  JHJZFY){ 

       this.JHJZFY = JHJZFY;   

   }


   public String  getSCZL(){

       return this.BLZL;

   }


   public  void setSCZL( String SCZL){

       this.BLZL = SCZL;

   }


   public  String getJZGCGHXKZH(){ 

       return this.JZGCGHXKZH;   

   }


   public  void setJZGCGHXKZH(String  JZGCGHXKZH){ 

       this.JZGCGHXKZH = JZGCGHXKZH;   

   }


   public  String getJCHJZMJ(){ 

       return this.JCHJZMJ;   

   }


   public  void setJCHJZMJ(String  JCHJZMJ){ 

       this.JCHJZMJ = JCHJZMJ;   

   }


   public  String getJHKGRQ(){ 

       return this.JHKGRQ;   

   }


   public  void setJHKGRQ(String  JHKGRQ){ 

       this.JHKGRQ = JHKGRQ;   

   }


   public String toString(){ 

       return "ApplicantPostHouseInformationBuildInformation{" +  
 
              "GRSKYHZH='" + this.GRSKYHZH + '\'' + "," +
              "FWZL='" + this.FWZL + '\'' + "," +
              "JSGCSGXKZH='" + this.JSGCSGXKZH + '\'' + "," +
              "FWXZ='" + this.FWXZ + '\'' + "," +
              "FWJG='" + this.FWJG + '\'' + "," +
              "KHYHMC='" + this.KHYHMC + '\'' + "," +
              "JHJGRQ='" + this.JHJGRQ + '\'' + "," +
              "TDSYZH='" + this.TDSYZH + '\'' + "," +
              "GRSYZJ='" + this.GRSYZJ + '\'' + "," +
              "JZCS='" + this.JZCS + '\'' + "," +
              "YHKHM='" + this.YHKHM + '\'' + "," +
              "JZYDGHXKZH='" + this.JZYDGHXKZH + '\'' + "," +
              "PZJGWH='" + this.PZJGWH + '\'' + "," +
              "JHJZFY='" + this.JHJZFY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "JZGCGHXKZH='" + this.JZGCGHXKZH + '\'' + "," +
              "JCHJZMJ='" + this.JCHJZMJ + '\'' + "," +
              "JHKGRQ='" + this.JHKGRQ + '\'' + 

      "}";

  } 
}