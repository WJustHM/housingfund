package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitPayHoldPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayHoldPost  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    SQHJKSNY;  //申请缓缴开始年月 

   private  String    HJYY;  //缓缴原因 

   private  String    JBRXM;  //经办人姓名 

   private  String JBRZJHM;  //经办人证件号码

   private  UnitPayHoldPostQCQR   QCQR;  //单位前两年经营情况

   private  String    YWWD;  //业务网点 

   private  String    DWZH;  //单位账号 

   private  String    CZLX;  //操作类型（0:保存；1:提交;） 

   private  String    CZY;  //操作员 

   private  String    SQHJJSNY;  //申请缓缴结束年月 

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


   public  String getSQHJKSNY(){ 

       return this.SQHJKSNY;   

   }


   public  void setSQHJKSNY(String  SQHJKSNY){ 

       this.SQHJKSNY = SQHJKSNY;   

   }


   public  String getHJYY(){ 

       return this.HJYY;   

   }


   public  void setHJYY(String  HJYY){ 

       this.HJYY = HJYY;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  UnitPayHoldPostQCQR getQCQR(){

       return this.QCQR;   

   }


   public  void setQCQR(UnitPayHoldPostQCQR  QCQR){

       this.QCQR = QCQR;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getSQHJJSNY(){ 

       return this.SQHJJSNY;   

   }


   public  void setSQHJJSNY(String  SQHJJSNY){ 

       this.SQHJJSNY = SQHJJSNY;   

   }


   public String toString(){ 

       return "UnitPayHoldPost{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "SQHJKSNY='" + this.SQHJKSNY + '\'' + "," +
              "HJYY='" + this.HJYY + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "QCQR='" + this.QCQR + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "SQHJJSNY='" + this.SQHJJSNY + '\'' + 

      "}";

  } 
}