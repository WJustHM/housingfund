package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "HeadIndiAcctMergeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadIndiAcctMergeRes  implements Serializable{

   private  String    YWLSH;  //业务流水号 

   private  String    JBRXM;  //经办人姓名 

   private  String    ZJHM;  //证件号码 

   private  HeadIndiAcctMergeResBLZHXX    BLZHXX;  //保留账号信息 

   private  String    ZJLX;  //证件类型 

   private  String    XingMing;  //姓名 

   private  String    CZY;  //操作员 

   private  ArrayList<HeadIndiAcctMergeResFCZHXX>    FCZHXX;  //封存账号信息 

   private  String    RiQi;  //日期 

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


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  HeadIndiAcctMergeResBLZHXX getBLZHXX(){ 

       return this.BLZHXX;   

   }


   public  void setBLZHXX(HeadIndiAcctMergeResBLZHXX  BLZHXX){ 

       this.BLZHXX = BLZHXX;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

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


   public  ArrayList<HeadIndiAcctMergeResFCZHXX> getFCZHXX(){ 

       return this.FCZHXX;   

   }


   public  void setFCZHXX(ArrayList<HeadIndiAcctMergeResFCZHXX>  FCZHXX){ 

       this.FCZHXX = FCZHXX;   

   }


   public  String getRiQi(){ 

       return this.RiQi;   

   }


   public  void setRiQi(String  RiQi){ 

       this.RiQi = RiQi;   

   }


   public String toString(){ 

       return "HeadIndiAcctMergeRes{" +  
 
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "BLZHXX='" + this.BLZHXX + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "FCZHXX='" + this.FCZHXX + '\'' + "," +
              "RiQi='" + this.RiQi + '\'' + 

      "}";

  } 
}