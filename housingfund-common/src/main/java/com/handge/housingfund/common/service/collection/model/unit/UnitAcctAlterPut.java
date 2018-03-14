package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctAlterPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctAlterPut  implements Serializable{

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  UnitAcctAlterPutDWDJXX    DWDJXX;  // 

   private  UnitAcctAlterPutWTYHXX    WTYHXX;  // 

   private  String    YWWD;  //业务网点 

   private  UnitAcctAlterPutDWLXFS    DWLXFS;  // 

   private  UnitAcctAlterPutDWGJXX    DWGJXX;  // 

   private  UnitAcctAlterPutJBRXX    JBRXX;  // 

   private  UnitAcctAlterPutDWKHBLZL    DWKHBLZL;  // 

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


   public  UnitAcctAlterPutDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(UnitAcctAlterPutDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  UnitAcctAlterPutWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(UnitAcctAlterPutWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  UnitAcctAlterPutDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(UnitAcctAlterPutDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  UnitAcctAlterPutDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctAlterPutDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  UnitAcctAlterPutJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(UnitAcctAlterPutJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  UnitAcctAlterPutDWKHBLZL getDWKHBLZL(){ 

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(UnitAcctAlterPutDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public String toString(){ 

       return "UnitAcctAlterPut{" +  
 
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