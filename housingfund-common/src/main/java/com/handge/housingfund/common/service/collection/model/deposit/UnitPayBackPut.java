package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "UnitPayBackPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayBackPut  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    BJFS;  //补缴方式 

   private  String    JBRZJHM;  //经办人证件号码 

   private  ArrayList<UnitPayBackPutBJXX>    BJXX;  //补缴信息 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    HBJNY;  //汇补缴年月 

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


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  ArrayList<UnitPayBackPutBJXX> getBJXX(){ 

       return this.BJXX;   

   }


   public  void setBJXX(ArrayList<UnitPayBackPutBJXX>  BJXX){ 

       this.BJXX = BJXX;   

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


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public String toString(){ 

       return "UnitPayBackPut{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "BJFS='" + this.BJFS + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "BJXX='" + this.BJXX + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "HBJNY='" + this.HBJNY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}