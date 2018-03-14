package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitAcctBasicResDWDJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitAcctBasicResDWDJXX  implements Serializable{

   private  String    GRJCBL;  //个人缴存比例 

   private  String    FXZHKHYH;  //发薪账号开户银行 

   private  String    DWFXR;  //单位发薪日 

   private  String    BeiZhu;  //备注 

   private  String    DWJCBL;  //单位缴存比例 

   private  String    DWSCHJNY;  //单位首次汇缴年月 

   private  String    FXZH;  //发薪账号 

   private  String    DWSLRQ;  //单位设立日期 

   public  String getGRJCBL(){ 

       return this.GRJCBL;   

   }


   public  void setGRJCBL(String  GRJCBL){ 

       this.GRJCBL = GRJCBL;   

   }


   public  String getFXZHKHYH(){ 

       return this.FXZHKHYH;   

   }


   public  void setFXZHKHYH(String  FXZHKHYH){ 

       this.FXZHKHYH = FXZHKHYH;   

   }


   public  String getDWFXR(){ 

       return this.DWFXR;   

   }


   public  void setDWFXR(String  DWFXR){ 

       this.DWFXR = DWFXR;   

   }


   public  String getBeiZhu(){ 

       return this.BeiZhu;   

   }


   public  void setBeiZhu(String  BeiZhu){ 

       this.BeiZhu = BeiZhu;   

   }


   public  String getDWJCBL(){ 

       return this.DWJCBL;   

   }


   public  void setDWJCBL(String  DWJCBL){ 

       this.DWJCBL = DWJCBL;   

   }


   public  String getDWSCHJNY(){ 

       return this.DWSCHJNY;   

   }


   public  void setDWSCHJNY(String  DWSCHJNY){ 

       this.DWSCHJNY = DWSCHJNY;   

   }


   public  String getFXZH(){ 

       return this.FXZH;   

   }


   public  void setFXZH(String  FXZH){ 

       this.FXZH = FXZH;   

   }


   public  String getDWSLRQ(){ 

       return this.DWSLRQ;   

   }


   public  void setDWSLRQ(String  DWSLRQ){ 

       this.DWSLRQ = DWSLRQ;   

   }


   public String toString(){ 

       return "HeadUnitAcctBasicResDWDJXX{" +  
 
              "GRJCBL='" + this.GRJCBL + '\'' + "," +
              "FXZHKHYH='" + this.FXZHKHYH + '\'' + "," +
              "DWFXR='" + this.DWFXR + '\'' + "," +
              "BeiZhu='" + this.BeiZhu + '\'' + "," +
              "DWJCBL='" + this.DWJCBL + '\'' + "," +
              "DWSCHJNY='" + this.DWSCHJNY + '\'' + "," +
              "FXZH='" + this.FXZH + '\'' + "," +
              "DWSLRQ='" + this.DWSLRQ + '\'' + 

      "}";

  } 
}