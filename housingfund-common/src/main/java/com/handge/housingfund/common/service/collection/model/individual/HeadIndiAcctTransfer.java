package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "HeadIndiAcctTransfer")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadIndiAcctTransfer  implements Serializable{

   private  String    YWLSH;  //业务流水号 

   private  String    ZYSXNY;  //转移生效年月 

   private  String    JBRXM;  //经办人姓名 

   private  ArrayList<HeadIndiAcctTransferList>    list;  // 

   private  String    ZCDWZH;  //转出单位账号 

   private  String    CZY;  //操作员 

   private  String    ZCDWMC;  //转出单位名称 

   private  String    RiQi;  //日期 

   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getZYSXNY(){ 

       return this.ZYSXNY;   

   }


   public  void setZYSXNY(String  ZYSXNY){ 

       this.ZYSXNY = ZYSXNY;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  ArrayList<HeadIndiAcctTransferList> getlist(){ 

       return this.list;   

   }


   public  void setlist(ArrayList<HeadIndiAcctTransferList>  list){ 

       this.list = list;   

   }


   public  String getZCDWZH(){ 

       return this.ZCDWZH;   

   }


   public  void setZCDWZH(String  ZCDWZH){ 

       this.ZCDWZH = ZCDWZH;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getZCDWMC(){ 

       return this.ZCDWMC;   

   }


   public  void setZCDWMC(String  ZCDWMC){ 

       this.ZCDWMC = ZCDWMC;   

   }


   public  String getRiQi(){ 

       return this.RiQi;   

   }


   public  void setRiQi(String  RiQi){ 

       this.RiQi = RiQi;   

   }


   public String toString(){ 

       return "HeadIndiAcctTransfer{" +  
 
              "YWLSH='" + this.YWLSH + '\'' + "," +
              "ZYSXNY='" + this.ZYSXNY + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "list='" + this.list + '\'' + "," +
              "ZCDWZH='" + this.ZCDWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "ZCDWMC='" + this.ZCDWMC + '\'' + "," +
              "RiQi='" + this.RiQi + '\'' + 

      "}";

  } 
}