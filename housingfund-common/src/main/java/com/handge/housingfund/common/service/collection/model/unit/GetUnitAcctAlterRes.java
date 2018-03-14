package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GetUnitAcctAlterRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctAlterRes  implements Serializable{

   private  GetUnitAcctAlterResDWKHBLZL    DWKHBLZL;  // 

   private  GetUnitAcctAlterResDWDJXX    DWDJXX;  // 

   private  GetUnitAcctAlterResDWLXFS    DWLXFS;  // 

   private  GetUnitAcctAlterResDWGJXX    DWGJXX;  // 

   private  GetUnitAcctAlterResJBRXX    JBRXX;  // 

   private  GetUnitAcctAlterResWTYHXX    WTYHXX;  // 

    private ArrayList<String>DELTA;

    public ArrayList<String> getDELTA() {
        return DELTA;
    }

    public void setDELTA(ArrayList<String> DELTA) {
        this.DELTA = DELTA;
    }

    public  GetUnitAcctAlterResDWKHBLZL getDWKHBLZL(){

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(GetUnitAcctAlterResDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public  GetUnitAcctAlterResDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(GetUnitAcctAlterResDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  GetUnitAcctAlterResDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(GetUnitAcctAlterResDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  GetUnitAcctAlterResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(GetUnitAcctAlterResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  GetUnitAcctAlterResJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(GetUnitAcctAlterResJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  GetUnitAcctAlterResWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(GetUnitAcctAlterResWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public String toString(){ 

       return "GetUnitAcctAlterRes{" +  
 
              "DWKHBLZL='" + this.DWKHBLZL + '\'' + "," +
              "DWDJXX='" + this.DWDJXX + '\'' + "," +
              "DWLXFS='" + this.DWLXFS + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "JBRXX='" + this.JBRXX + '\'' + "," +
              "WTYHXX='" + this.WTYHXX + '\'' + 

      "}";

  } 
}