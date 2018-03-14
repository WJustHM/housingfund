package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctSetPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctSetPut  implements Serializable{

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  UnitAcctSetPutDWDJXX    DWDJXX;  // 

   private  UnitAcctSetPutWTYHXX    WTYHXX;  // 

   private  String    YWWD;  //业务网点 

   private  UnitAcctSetPutDWLXFS    DWLXFS;  // 

   private  UnitAcctSetPutDWGJXX    DWGJXX;  // 

   private  UnitAcctSetPutJBRXX    JBRXX;  // 

   private  UnitAcctSetPutDWKHBLZL    DWKHBLZL;  // 

   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  UnitAcctSetPutDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(UnitAcctSetPutDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  UnitAcctSetPutWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(UnitAcctSetPutWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  UnitAcctSetPutDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(UnitAcctSetPutDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  UnitAcctSetPutDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctSetPutDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  UnitAcctSetPutJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(UnitAcctSetPutJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  UnitAcctSetPutDWKHBLZL getDWKHBLZL(){ 

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(UnitAcctSetPutDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public String toString(){ 

       return "UnitAcctSetPut{" +  
 
              "CZY='" + this.CZY + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + "," +
              "DWDJXX='" + this.DWDJXX + '\'' + "," +
              "WTYHXX='" + this.WTYHXX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "DWLXFS='" + this.DWLXFS + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "JBRXX='" + this.JBRXX + '\'' + "," +
              "DWKHBLZL='" + this.DWKHBLZL + '\'' + 

      "}";

  } 
}