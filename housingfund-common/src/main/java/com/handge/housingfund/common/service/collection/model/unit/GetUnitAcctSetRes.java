package com.handge.housingfund.common.service.collection.model.unit;

import com.handge.housingfund.common.service.review.model.ReviewInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetUnitAcctSetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctSetRes  implements Serializable{

   private  GetUnitAcctSetResDWKHBLZL    DWKHBLZL;  // 

   private  GetUnitAcctSetResDWDJXX    DWDJXX;  // 

   private  GetUnitAcctSetResDWLXFS    DWLXFS;  // 

   private  GetUnitAcctSetResDWGJXX    DWGJXX;  // 

   private  GetUnitAcctSetResJBRXX    JBRXX;  // 

   private  GetUnitAcctSetResWTYHXX    WTYHXX;  // 
    private ArrayList<ReviewInfo> ReviewInfos;

    public ArrayList<ReviewInfo> getReviewInfos() {
        return ReviewInfos;
    }

    public void setReviewInfos(ArrayList<ReviewInfo> reviewInfos) {
        this.ReviewInfos = reviewInfos;
    }

   public  GetUnitAcctSetResDWKHBLZL getDWKHBLZL(){ 

       return this.DWKHBLZL;   

   }


   public  void setDWKHBLZL(GetUnitAcctSetResDWKHBLZL  DWKHBLZL){ 

       this.DWKHBLZL = DWKHBLZL;   

   }


   public  GetUnitAcctSetResDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(GetUnitAcctSetResDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  GetUnitAcctSetResDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(GetUnitAcctSetResDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  GetUnitAcctSetResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(GetUnitAcctSetResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  GetUnitAcctSetResJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(GetUnitAcctSetResJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  GetUnitAcctSetResWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }


   public  void setWTYHXX(GetUnitAcctSetResWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }


   public String toString(){ 

       return "GetUnitAcctSetRes{" +  
 
              "DWKHBLZL='" + this.DWKHBLZL + '\'' + "," +
              "DWDJXX='" + this.DWDJXX + '\'' + "," +
              "DWLXFS='" + this.DWLXFS + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "JBRXX='" + this.JBRXX + '\'' + "," +
              "WTYHXX='" + this.WTYHXX + '\'' + 

      "}";

  } 
}