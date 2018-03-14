package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "UnitPayBackPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayBackPost  implements Serializable{

   private String YWLSH;    //业务流水号

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    BJFS;  //补缴方式 

   private  String    DWZHYE;  //单位账户余额

   private  ArrayList<UnitPayBackPostBJXX>    BJXX;  //补缴信息 

   private  String    ZSYE;  //暂收余额

   private  String    JZNY;  //缴至年月 

   private  String    JBRZJHM;  //经办人证件号码

   private  String    HBJNY;  //汇补缴年月 

   private  String    DWYHJNY;  //单位应汇缴年月 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    BLZL;  //办理资料 


    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
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


   public  String getBJFS(){ 

       return this.BJFS;   

   }


   public  void setBJFS(String  BJFS){ 

       this.BJFS = BJFS;   

   }


   public  String getDWZHYE(){

       return this.DWZHYE;   

   }


   public  void setDWZHYE(String  DWZHYE){

       this.DWZHYE = DWZHYE;   

   }


   public  ArrayList<UnitPayBackPostBJXX> getBJXX(){ 

       return this.BJXX;   

   }


   public  void setBJXX(ArrayList<UnitPayBackPostBJXX>  BJXX){ 

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


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

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
        return "UnitPayBackPost{" +
                "YWLSH='" + YWLSH + '\'' +
                ", JBRZJLX='" + JBRZJLX + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", BJFS='" + BJFS + '\'' +
                ", DWZHYE=" + DWZHYE +
                ", BJXX=" + BJXX +
                ", ZSYE=" + ZSYE +
                ", JZNY='" + JZNY + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", HBJNY='" + HBJNY + '\'' +
                ", DWYHJNY='" + DWYHJNY + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}