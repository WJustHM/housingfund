package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitPayHoldPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayHoldPut  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    JBRXM;  //经办人姓名 

   private  UnitPayHoldPutQCQR    QCQR;  //单位前两年经营情况

   private  String    YWWD;  //业务网点 

   private  String    SQYHJSJKS;  //申请延缓缴时间开始 

   private  String    CZLX;  //操作类型（0:保存；1:提交;） 

   private  String    HJYY;  //缓缴原因 

   private  String    SQYHJSJJS;  //申请延缓缴时间结束 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    BLZL;  //办理资料 

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


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


    public UnitPayHoldPutQCQR getQCQR() {
        return QCQR;
    }

    public void setQCQR(UnitPayHoldPutQCQR QCQR) {
        this.QCQR = QCQR;
    }

    public  String getYWWD(){

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getSQYHJSJKS(){ 

       return this.SQYHJSJKS;   

   }


   public  void setSQYHJSJKS(String  SQYHJSJKS){ 

       this.SQYHJSJKS = SQYHJSJKS;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getHJYY(){ 

       return this.HJYY;   

   }


   public  void setHJYY(String  HJYY){ 

       this.HJYY = HJYY;   

   }


   public  String getSQYHJSJJS(){ 

       return this.SQYHJSJJS;   

   }


   public  void setSQYHJSJJS(String  SQYHJSJJS){ 

       this.SQYHJSJJS = SQYHJSJJS;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getDWYHJNY(){ 

       return this.DWYHJNY;   

   }


   public  void setDWYHJNY(String  DWYHJNY){ 

       this.DWYHJNY = DWYHJNY;   

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


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


    @Override
    public String toString() {
        return "UnitPayHoldPut{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", QCQR=" + QCQR +
                ", YWWD='" + YWWD + '\'' +
                ", SQYHJSJKS='" + SQYHJSJKS + '\'' +
                ", CZLX='" + CZLX + '\'' +
                ", HJYY='" + HJYY + '\'' +
                ", SQYHJSJJS='" + SQYHJSJJS + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", DWYHJNY='" + DWYHJNY + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}