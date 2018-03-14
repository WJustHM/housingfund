package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoUnitDepositRatioRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitDepositRatioRes  implements Serializable{

    private static final long serialVersionUID = 559113840645163069L;
    private  String    TZQGRJCBL;  //调整前个人缴存比例

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JZNY;  //缴至年月 

   private  String    JBRXM;  //经办人姓名 

   private  String    DWMC;  //单位名称 

   private  String    TZQDWJCBL;  //调整前单位缴存比例

   private  String    DWZH;  //单位账号

    private String    JBRZJHM;//经办人证件号码

    public String getJBRZJHM() {
        return JBRZJHM;
    }

    public void setJBRZJHM(String JBRZJHM) {
        this.JBRZJHM = JBRZJHM;
    }

    public  String getTZQGRJCBL(){

       return this.TZQGRJCBL;   

   }


   public  void setTZQGRJCBL(String  TZQGRJCBL){

       this.TZQGRJCBL = TZQGRJCBL;   

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getTZQDWJCBL(){

       return this.TZQDWJCBL;   

   }


   public  void setTZQDWJCBL(String  TZQDWJCBL){

       this.TZQDWJCBL = TZQDWJCBL;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public String toString(){ 

       return "AutoUnitDepositRatioRes{" +  
 
              "TZQGRJCBL='" + this.TZQGRJCBL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "TZQDWJCBL='" + this.TZQDWJCBL + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + 

      "}";

  } 
}