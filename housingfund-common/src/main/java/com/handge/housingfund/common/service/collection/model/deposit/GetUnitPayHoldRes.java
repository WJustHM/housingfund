package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitPayHoldRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitPayHoldRes  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    SQHJKSNY;  //申请缓缴开始年月 

   private  String    JBRXM;  //经办人姓名 

   private  GetUnitPayHoldResQCQR   QCQR;  //单位前两年经营情况

   private  String    YWWD;  //业务网点 

   private  String    JZNY;  //缴至年月 

   private  String    HJYY;  //缓缴原因 

   private  String JBRZJHM;  //经办人证件号码

   private  String    DWMC;  //单位名称 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    CZY;  //操作员 

   private  String    DWZH;  //单位账号 

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


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getSQHJKSNY(){ 

       return this.SQHJKSNY;   

   }


   public  void setSQHJKSNY(String  SQHJKSNY){ 

       this.SQHJKSNY = SQHJKSNY;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  GetUnitPayHoldResQCQR getQCQR(){

       return this.QCQR;   

   }


   public  void setQCQR(GetUnitPayHoldResQCQR  QCQR){

       this.QCQR = QCQR;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getHJYY(){ 

       return this.HJYY;   

   }


   public  void setHJYY(String  HJYY){ 

       this.HJYY = HJYY;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWYHJNY(){ 

       return this.DWYHJNY;   

   }


   public  void setDWYHJNY(String  DWYHJNY){ 

       this.DWYHJNY = DWYHJNY;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getSQHJJSNY(){ 

       return this.SQHJJSNY;   

   }


   public  void setSQHJJSNY(String  SQHJJSNY){ 

       this.SQHJJSNY = SQHJJSNY;   

   }


   public String toString(){ 

       return "GetUnitPayHoldRes{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "SQHJKSNY='" + this.SQHJKSNY + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "QCQR='" + this.QCQR + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "HJYY='" + this.HJYY + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "SQHJJSNY='" + this.SQHJJSNY + '\'' + 

      "}";

  } 
}