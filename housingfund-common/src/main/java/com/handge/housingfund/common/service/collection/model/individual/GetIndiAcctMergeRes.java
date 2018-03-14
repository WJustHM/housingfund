package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetIndiAcctMergeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctMergeRes  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  String    ZJHM;  //证件号码 

   private  GetIndiAcctMergeResBLZHXX    BLZHXX;  //保留账号信息 

   private  String    ZJLX;  //证件类型 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    XingMing;  //姓名 

   private  String    CZY;  //操作员 

   private  ArrayList<GetIndiAcctMergeResFCZHXX>    FCZHXX;  //封存账号信息 

   private  String    BLZL;  //办理资料 

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


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  GetIndiAcctMergeResBLZHXX getBLZHXX(){ 

       return this.BLZHXX;   

   }


   public  void setBLZHXX(GetIndiAcctMergeResBLZHXX  BLZHXX){ 

       this.BLZHXX = BLZHXX;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

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


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  ArrayList<GetIndiAcctMergeResFCZHXX> getFCZHXX(){ 

       return this.FCZHXX;   

   }


   public  void setFCZHXX(ArrayList<GetIndiAcctMergeResFCZHXX>  FCZHXX){ 

       this.FCZHXX = FCZHXX;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public String toString(){ 

       return "GetIndiAcctMergeRes{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "BLZHXX='" + this.BLZHXX + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "FCZHXX='" + this.FCZHXX + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + 

      "}";

  } 
}