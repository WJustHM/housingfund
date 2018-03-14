package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetIndiAcctTransferResList")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctTransferResList  implements Serializable{

   private  String    ZRDWMC;  //转入单位名称 

   private  String    ZRDWZH;  //转入单位账号 

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   private  String    BeiZhu;  //备注 

   public  String getZRDWMC(){ 

       return this.ZRDWMC;   

   }


   public  void setZRDWMC(String  ZRDWMC){ 

       this.ZRDWMC = ZRDWMC;   

   }


   public  String getZRDWZH(){ 

       return this.ZRDWZH;   

   }


   public  void setZRDWZH(String  ZRDWZH){ 

       this.ZRDWZH = ZRDWZH;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getBeiZhu(){ 

       return this.BeiZhu;   

   }


   public  void setBeiZhu(String  BeiZhu){ 

       this.BeiZhu = BeiZhu;   

   }


   public String toString(){ 

       return "GetIndiAcctTransferResList{" +  
 
              "ZRDWMC='" + this.ZRDWMC + '\'' + "," +
              "ZRDWZH='" + this.ZRDWZH + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "BeiZhu='" + this.BeiZhu + '\'' + 

      "}";

  } 
}