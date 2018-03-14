package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "UnitPayWrongPutGZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayWrongPutGZXX  implements Serializable{

   private  String    DWCJJE;  //单位错缴金额

   private  String    FSE;  //发生额

   private  String    GRZH;  //个人账号 

   private  String    YuanYin;  //原因 

   private  String GRCJJE;  //个人错缴金额

   public  String getDWCJJE(){

       return this.DWCJJE;   

   }


   public  void setDWCJJE(String  DWCJJE){

       this.DWCJJE = DWCJJE;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getYuanYin(){ 

       return this.YuanYin;   

   }


   public  void setYuanYin(String  YuanYin){ 

       this.YuanYin = YuanYin;   

   }


   public  String getGRCJJE(){

       return this.GRCJJE;

   }


   public  void setGRCJJE(String GRCJJE){

       this.GRCJJE = GRCJJE;

   }


   public String toString(){ 

       return "UnitPayWrongPutGZXX{" +  
 
              "DWCJJE='" + this.DWCJJE + '\'' + "," +
              "FSE='" + this.FSE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "YuanYin='" + this.YuanYin + '\'' + "," +
              "GRCJJE='" + this.GRCJJE + '\'' +

      "}";

  } 
}