package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitDepositRatioPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitDepositRatioPut  implements Serializable{

    private static final long serialVersionUID = 6940091946910302213L;
    private  String    BLZL;  //办理资料

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    TZHDWJCBL;  //调整后单位缴存比例

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    SXNY;  //生效年月 

   private  String    TZHGRJCBL;  //调整后个人缴存比例

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交）

    public  String getBLZL(){

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }





   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getSXNY(){ 

       return this.SXNY;   

   }


   public  void setSXNY(String  SXNY){ 

       this.SXNY = SXNY;   

   }


    public String getTZHDWJCBL() {
        return TZHDWJCBL;
    }

    public void setTZHDWJCBL(String TZHDWJCBL) {
        this.TZHDWJCBL = TZHDWJCBL;
    }

    public String getTZHGRJCBL() {
        return TZHGRJCBL;
    }

    public void setTZHGRJCBL(String TZHGRJCBL) {
        this.TZHGRJCBL = TZHGRJCBL;
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


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public String toString(){ 

       return "UnitDepositRatioPut{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "TZHDWJCBL='" + this.TZHDWJCBL + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "SXNY='" + this.SXNY + '\'' + "," +
              "TZHGRJCBL='" + this.TZHGRJCBL + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + 

      "}";

  } 
}