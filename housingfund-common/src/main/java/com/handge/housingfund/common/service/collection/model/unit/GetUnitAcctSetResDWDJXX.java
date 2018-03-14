package com.handge.housingfund.common.service.collection.model.unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitAcctSetResDWDJXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctSetResDWDJXX  implements Serializable{

   private  String    GRJCBL;  //个人缴存比例 

   private  String    FXZHKHYH;  //发薪账号开户银行 

   private  String    DWFXR;  //单位发薪日 

   private  String    BeiZhu;  //备注 

   private  String    DWJCBL;  //单位缴存比例 

   private  String    FXZHHM;  //发薪账号户名 

   private  String    DWSCHJNY;  //单位首次汇缴年月 

   private  String    FXZH;  //发薪账号 

   private  String    DWSLRQ;  //单位设立日期

   private String DWKHRQ;   //单位开户日期

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


   public  String getFXZHHM(){ 

       return this.FXZHHM;   

   }


   public  void setFXZHHM(String  FXZHHM){ 

       this.FXZHHM = FXZHHM;   

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

    public String getDWKHRQ() {
        return DWKHRQ;
    }

    public void setDWKHRQ(String DWKHRQ) {
        this.DWKHRQ = DWKHRQ;
    }

    @Override
    public String toString() {
        return "GetUnitAcctSetResDWDJXX{" +
                "GRJCBL='" + GRJCBL + '\'' +
                ", FXZHKHYH='" + FXZHKHYH + '\'' +
                ", DWFXR='" + DWFXR + '\'' +
                ", BeiZhu='" + BeiZhu + '\'' +
                ", DWJCBL='" + DWJCBL + '\'' +
                ", FXZHHM='" + FXZHHM + '\'' +
                ", DWSCHJNY='" + DWSCHJNY + '\'' +
                ", FXZH='" + FXZH + '\'' +
                ", DWSLRQ='" + DWSLRQ + '\'' +
                ", DWKHRQ='" + DWKHRQ + '\'' +
                '}';
    }
}