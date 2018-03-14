package com.handge.housingfund.common.service.loan.model;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "ApplicantPostHouseInformationPurchaseFirstInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicantPostHouseInformationPurchaseFirstInformation  implements Serializable {

   private  String    FWJZMJ;  //房屋建筑面积 

   private  String    FWZL;  //房屋坐落 

   private  String    FWTNMJ;  //房屋套内面积 

   private  String    FWXS;  //房屋形式（0：在建房  1：现房） 

   private  String    FWXZ;  //房屋性质（0：商品房 1：障碍性住房 2：自建房 3：两限房 4：集资房 5：危改房 6：经济适用房 7：房改房 8：其他） 


    private  String    SFRYHKHM;  //售房人银行开户名

   private  String    FWJG;  //房屋结构（0：框架 1：砖混 2：其他） 

   private  String    SFRKHYHMC;  //售房人开户银行名称 

   private  String    FWZJ;  //房屋总价 

   private  String    FWJGRQ;  //房屋竣工日期 

   private  String    HTJE;  //合同金额 

   @Annotation.BankCard(name = "售房人账户号码")
   private  String    SFRZHHM;  //售房人账户号码 

   private  String    LPMC;  //楼盘名称 

   @Annotation.Money(name = "单价")
   private  String    DanJia;  //单价 

   private  String    LXFS;  //联系方式 

   private  String    BLZL;   //办理资料  //

   private  String    SFRMC;  //售房人名称 

   private  String    GFHTBH;  //购房合同编号 

   @Annotation.Money(name = "首付款")
   private  String    SFK;  //首付款 

   private  String    SPFYSXKBH;  //商品房预售许可编号 

    public String getSFRYHKHM() {
        return SFRYHKHM;
    }

    public void setSFRYHKHM(String SFRYHKHM) {
        this.SFRYHKHM = SFRYHKHM;
    }
   public  String getFWJZMJ(){ 

       return this.FWJZMJ;   

   }


   public  void setFWJZMJ(String  FWJZMJ){ 

       this.FWJZMJ = FWJZMJ;   

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


   public  String getFWXS(){ 

       return this.FWXS;   

   }


   public  void setFWXS(String  FWXS){ 

       this.FWXS = FWXS;   

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


   public  String getSFRKHYHMC(){ 

       return this.SFRKHYHMC;   

   }


   public  void setSFRKHYHMC(String  SFRKHYHMC){ 

       this.SFRKHYHMC = SFRKHYHMC;   

   }


   public  String getFWZJ(){ 

       return this.FWZJ;   

   }


   public  void setFWZJ(String  FWZJ){ 

       this.FWZJ = FWZJ;   

   }


   public  String getFWJGRQ(){ 

       return this.FWJGRQ;   

   }


   public  void setFWJGRQ(String  FWJGRQ){ 

       this.FWJGRQ = FWJGRQ;   

   }


   public  String getHTJE(){ 

       return this.HTJE;   

   }


   public  void setHTJE(String  HTJE){ 

       this.HTJE = HTJE;   

   }


   public  String getSFRZHHM(){ 

       return this.SFRZHHM;   

   }


   public  void setSFRZHHM(String  SFRZHHM){ 

       this.SFRZHHM = SFRZHHM;   

   }


   public  String getLPMC(){ 

       return this.LPMC;   

   }


   public  void setLPMC(String  LPMC){ 

       this.LPMC = LPMC;   

   }


   public  String getDanJia(){ 

       return this.DanJia;   

   }


   public  void setDanJia(String  DanJia){ 

       this.DanJia = DanJia;   

   }


   public  String getLXFS(){ 

       return this.LXFS;   

   }


   public  void setLXFS(String  LXFS){ 

       this.LXFS = LXFS;   

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


   public  String getGFHTBH(){ 

       return this.GFHTBH;   

   }


   public  void setGFHTBH(String  GFHTBH){ 

       this.GFHTBH = GFHTBH;   

   }


   public  String getSFK(){ 

       return this.SFK;   

   }


   public  void setSFK(String  SFK){ 

       this.SFK = SFK;   

   }


   public  String getSPFYSXKBH(){ 

       return this.SPFYSXKBH;   

   }


   public  void setSPFYSXKBH(String  SPFYSXKBH){ 

       this.SPFYSXKBH = SPFYSXKBH;   

   }


   public String toString(){ 

       return "ApplicantPostHouseInformationPurchaseFirstInformation{" +  
 
              "FWJZMJ='" + this.FWJZMJ + '\'' + "," +
              "FWZL='" + this.FWZL + '\'' + "," +
              "FWTNMJ='" + this.FWTNMJ + '\'' + "," +
              "FWXS='" + this.FWXS + '\'' + "," +
              "FWXZ='" + this.FWXZ + '\'' + "," +
              "SFRYHKHM='" + this.SFRYHKHM + '\'' + "," +
              "FWJG='" + this.FWJG + '\'' + "," +
              "SFRKHYHMC='" + this.SFRKHYHMC + '\'' + "," +
              "FWZJ='" + this.FWZJ + '\'' + "," +
              "FWJGRQ='" + this.FWJGRQ + '\'' + "," +
              "HTJE='" + this.HTJE + '\'' + "," +
              "SFRZHHM='" + this.SFRZHHM + '\'' + "," +
              "LPMC='" + this.LPMC + '\'' + "," +
              "DanJia='" + this.DanJia + '\'' + "," +
              "LXFS='" + this.LXFS + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "SFRMC='" + this.SFRMC + '\'' + "," +
              "GFHTBH='" + this.GFHTBH + '\'' + "," +
              "SFK='" + this.SFK + '\'' + "," +
              "SPFYSXKBH='" + this.SPFYSXKBH + '\'' + 

      "}";

  } 
}