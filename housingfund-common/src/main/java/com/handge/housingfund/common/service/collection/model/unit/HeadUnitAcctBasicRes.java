package com.handge.housingfund.common.service.collection.model.unit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "HeadUnitAcctBasicRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeadUnitAcctBasicRes  implements Serializable{

   private  HeadUnitAcctBasicResDWKHBLZL    DWKHBLZL;  // 

   private  HeadUnitAcctBasicResDWDJXX    DWDJXX;  // 

   private  HeadUnitAcctBasicResDWLXFS    DWLXFS;  // 

   private  HeadUnitAcctBasicResDWGJXX    DWGJXX;  // 

   private  HeadUnitAcctBasicResJBRXX    JBRXX;  // 

   private  HeadUnitAcctBasicResWTYHXX    WTYHXX;  //

   private  String SHR;//审核员

    public String getSHR() {
        return SHR;
    }

    public void setSHR(String SHR) {
        this.SHR = SHR;
    }

    public  HeadUnitAcctBasicResDWKHBLZL getDWKHBLZL(){

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(HeadUnitAcctBasicResDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public  HeadUnitAcctBasicResDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(HeadUnitAcctBasicResDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  HeadUnitAcctBasicResDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(HeadUnitAcctBasicResDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  HeadUnitAcctBasicResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(HeadUnitAcctBasicResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  HeadUnitAcctBasicResJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(HeadUnitAcctBasicResJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  HeadUnitAcctBasicResWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(HeadUnitAcctBasicResWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public String toString(){ 

       return "HeadUnitAcctBasicRes{" +  
 
              "DWKHBLZL='" + this.DWKHBLZL + '\'' + "," +
              "DWDJXX='" + this.DWDJXX + '\'' + "," +
              "DWLXFS='" + this.DWLXFS + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "JBRXX='" + this.JBRXX + '\'' + "," +
              "WTYHXX='" + this.WTYHXX + '\'' +
              "SHR='" + this.SHR + '\'' +

           "}";

  } 
}