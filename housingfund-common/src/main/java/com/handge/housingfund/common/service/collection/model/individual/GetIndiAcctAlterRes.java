package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetIndiAcctAlterRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctAlterRes  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  GetIndiAcctAlterResGRXX    GRXX;  //个人信息 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  GetIndiAcctAlterResDWXX    DWXX;  //单位信息 

   private  GetIndiAcctAlterResGRZHXX    GRZHXX;  //个人账户信息 

   private  String    CZY;  //操作员 

   private ArrayList<String> DELTA;//
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


   public  GetIndiAcctAlterResGRXX getGRXX(){ 

       return this.GRXX;   

   }


   public  void setGRXX(GetIndiAcctAlterResGRXX  GRXX){ 

       this.GRXX = GRXX;   

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


   public  GetIndiAcctAlterResDWXX getDWXX(){ 

       return this.DWXX;   

   }


   public  void setDWXX(GetIndiAcctAlterResDWXX  DWXX){ 

       this.DWXX = DWXX;   

   }


   public  GetIndiAcctAlterResGRZHXX getGRZHXX(){ 

       return this.GRZHXX;   

   }


   public  void setGRZHXX(GetIndiAcctAlterResGRZHXX  GRZHXX){ 

       this.GRZHXX = GRZHXX;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


    public ArrayList<String> getDelta() {
        return DELTA;
    }

    public void setDelta(ArrayList<String> delta) {
        this.DELTA = delta;
    }

    public  void setCZY(String  CZY){

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "GetIndiAcctAlterRes{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "GRXX='" + this.GRXX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWXX='" + this.DWXX + '\'' + "," +
              "GRZHXX='" + this.GRZHXX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}