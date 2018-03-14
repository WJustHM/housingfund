package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListUnitPayHoldResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayHoldResRes  implements Serializable{
   private String id;

   public String getId() {
        return id;
    }

   public void setId(String id) {
        this.id = id;
    }
   private  String    JZNY;  //缴至年月 

   private  String    YWLSH;  //业务流水号 

   private  String    SQHJNY;  //申请缓缴年月 

   private  String SLSJ;  //受理时间

   private  String    YWWD;  //业务网点 

   private  String    YWZT;  //状态 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getSQHJNY(){ 

       return this.SQHJNY;   

   }


   public  void setSQHJNY(String  SQHJNY){ 

       this.SQHJNY = SQHJNY;   

   }


   public  String getSLSJ(){

       return this.SLSJ;

   }


   public  void setSLSJ(String SLSJ){

       this.SLSJ = SLSJ;

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getYWZT(){ 

       return this.YWZT;   

   }


   public  void setYWZT(String  YWZT){ 

       this.YWZT = YWZT;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "ListUnitPayHoldResRes{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "SQHJNY='" + this.SQHJNY + '\'' + "," +
              "SLSJ='" + this.SLSJ + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "YWZT='" + this.YWZT + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}