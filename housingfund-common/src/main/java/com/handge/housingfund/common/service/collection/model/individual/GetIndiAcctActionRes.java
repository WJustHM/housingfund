package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctActionRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctActionRes  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  GetIndiAcctActionResDWXX    DWXX;  //单位信息 

   private  GetIndiAcctActionResGRZHXX    GRZHXX;  //个人账户信息 

   private  String    CZY;  //操作员 

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


   public  GetIndiAcctActionResDWXX getDWXX(){ 

       return this.DWXX;   

   }


   public  void setDWXX(GetIndiAcctActionResDWXX  DWXX){ 

       this.DWXX = DWXX;   

   }


   public  GetIndiAcctActionResGRZHXX getGRZHXX(){ 

       return this.GRZHXX;   

   }


   public  void setGRZHXX(GetIndiAcctActionResGRZHXX  GRZHXX){ 

       this.GRZHXX = GRZHXX;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "GetIndiAcctActionRes{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWXX='" + this.DWXX + '\'' + "," +
              "GRZHXX='" + this.GRZHXX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}