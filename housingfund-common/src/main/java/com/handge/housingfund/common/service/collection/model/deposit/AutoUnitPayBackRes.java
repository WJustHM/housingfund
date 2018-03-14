package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "AutoUnitPayBackRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoUnitPayBackRes  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    DWZHYE;  //单位账户余额

   private  ArrayList<AutoUnitPayBackResBJXX>    BJXX;  //补缴信息 

   private  String    ZSYE;  //暂收余额

   private  String    JZNY;  //缴至年月 

   private  String    JBRZJHM;  //经办人证件号码

   private  String    DWMC;  //单位名称 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

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


   public  String getDWZHYE(){

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){

       this.DWZHYE = DWZHYE;   

   }


   public  ArrayList<AutoUnitPayBackResBJXX> getBJXX(){ 

       return this.BJXX;   

   }


   public  void setBJXX(ArrayList<AutoUnitPayBackResBJXX>  BJXX){ 

       this.BJXX = BJXX;   

   }


   public  String getZSYE(){

       return this.ZSYE;   

   }


   public  void setZSYE(String  ZSYE){

       this.ZSYE = ZSYE;   

   }


   public  String getJZNY(){ 

       return this.JZNY;   

   }


   public  void setJZNY(String  JZNY){ 

       this.JZNY = JZNY;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

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

       return "AutoUnitPayBackRes{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "DWZHYE='" + this.DWZHYE + '\'' + "," +
              "BJXX='" + this.BJXX + '\'' + "," +
              "ZSYE='" + this.ZSYE + '\'' + "," +
              "JZNY='" + this.JZNY + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWYHJNY='" + this.DWYHJNY + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}