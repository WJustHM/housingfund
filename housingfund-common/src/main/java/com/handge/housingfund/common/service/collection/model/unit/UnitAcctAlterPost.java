package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctAlterPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctAlterPost  implements Serializable{

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  UnitAcctAlterPostDWDJXX    DWDJXX;  // 

   private  UnitAcctAlterPostWTYHXX    WTYHXX;  // 

   private  String    YWWD;  //业务网点 

   private  UnitAcctAlterPostDWLXFS    DWLXFS;  // 

   private  UnitAcctAlterPostDWGJXX    DWGJXX;  // 

   private  UnitAcctAlterPostJBRXX    JBRXX;  // 

   private  UnitAcctAlterPostDWKHBLZL    DWKHBLZL;  // 

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


   public  UnitAcctAlterPostDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(UnitAcctAlterPostDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  UnitAcctAlterPostWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(UnitAcctAlterPostWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  UnitAcctAlterPostDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(UnitAcctAlterPostDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  UnitAcctAlterPostDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctAlterPostDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  UnitAcctAlterPostJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(UnitAcctAlterPostJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  UnitAcctAlterPostDWKHBLZL getDWKHBLZL(){ 

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(UnitAcctAlterPostDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public String toString(){ 

       return "UnitAcctAlterPost{" +  
 
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