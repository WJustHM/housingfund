package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctSetPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctSetPost  implements Serializable{

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  UnitAcctSetPostDWDJXX    DWDJXX;  // 

   private  UnitAcctSetPostWTYHXX    WTYHXX;  // 

   private  String    YWWD;  //业务网点 

   private  UnitAcctSetPostDWLXFS    DWLXFS;  // 

   private  UnitAcctSetPostDWGJXX    DWGJXX;  // 

   private  UnitAcctSetPostJBRXX    JBRXX;  // 

   private  UnitAcctSetPostDWKHBLZL    DWKHBLZL;  //

   private  String FJCBLZL; //否检查办理资料

   public  String getCZY(){ 

       return this.CZY;   

   }

    public  void setCZY(String  CZY){

       this.CZY = CZY;

   }

    public String getFJCBLZL() {
        return FJCBLZL;
    }

    public void setFJCBLZL(String FJCBLZL) {
        this.FJCBLZL = FJCBLZL;
    }

   public  String getCZLX(){

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  UnitAcctSetPostDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(UnitAcctSetPostDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  UnitAcctSetPostWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(UnitAcctSetPostWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  UnitAcctSetPostDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(UnitAcctSetPostDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  UnitAcctSetPostDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(UnitAcctSetPostDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  UnitAcctSetPostJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(UnitAcctSetPostJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  UnitAcctSetPostDWKHBLZL getDWKHBLZL(){ 

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(UnitAcctSetPostDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public String toString(){ 

       return "UnitAcctSetPost{" +  
 
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