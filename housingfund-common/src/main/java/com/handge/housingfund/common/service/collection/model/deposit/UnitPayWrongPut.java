package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "UnitPayWrongPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayWrongPut  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  ArrayList<UnitPayWrongPutGZXX>    GZXX;  //更正信息 

   private  String    JCGZNY;  //缴存更正年月 

   private  String    SKYHMC;  //收款银行名称 

   private  String JBRZJHM;  //经办人证件号码

   private  String    SKYHHM;  //收款银行户名 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    SKYHZH;  //收款银行账号 

    private String SKYHDM;

   private  String    BLZL;  //办理资料 

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }

    public String getSKYHDM() {
        return SKYHDM;
    }

    public void setSKYHDM(String SKYHDM) {
        this.SKYHDM = SKYHDM;
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


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  ArrayList<UnitPayWrongPutGZXX> getGZXX(){ 

       return this.GZXX;   

   }


   public  void setGZXX(ArrayList<UnitPayWrongPutGZXX>  GZXX){ 

       this.GZXX = GZXX;   

   }


   public  String getJCGZNY(){ 

       return this.JCGZNY;   

   }


   public  void setJCGZNY(String  JCGZNY){ 

       this.JCGZNY = JCGZNY;   

   }


   public  String getSKYHMC(){ 

       return this.SKYHMC;   

   }


   public  void setSKYHMC(String  SKYHMC){ 

       this.SKYHMC = SKYHMC;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  String getSKYHHM(){ 

       return this.SKYHHM;   

   }


   public  void setSKYHHM(String  SKYHHM){ 

       this.SKYHHM = SKYHHM;   

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


   public  String getSKYHZH(){ 

       return this.SKYHZH;   

   }


   public  void setSKYHZH(String  SKYHZH){ 

       this.SKYHZH = SKYHZH;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }

    @Override
    public String toString() {
        return "UnitPayWrongPut{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZLX='" + CZLX + '\'' +
                ", GZXX=" + GZXX +
                ", JCGZNY='" + JCGZNY + '\'' +
                ", SKYHMC='" + SKYHMC + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", SKYHHM='" + SKYHHM + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", SKYHZH='" + SKYHZH + '\'' +
                ", SKYHDM='" + SKYHDM + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}