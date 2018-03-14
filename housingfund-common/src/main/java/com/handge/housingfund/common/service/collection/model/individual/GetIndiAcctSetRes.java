package com.handge.housingfund.common.service.collection.model.individual;

import com.handge.housingfund.common.service.review.model.ReviewInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetIndiAcctSetRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetIndiAcctSetRes  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRXM;  //经办人姓名 

   private  GetIndiAcctSetResGRXX    GRXX;  //个人信息 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  GetIndiAcctSetResDWXX    DWXX;  //单位信息 

   private  GetIndiAcctSetResGRZHXX    GRZHXX;  //个人账户信息

   private  String    CZY;  //操作员 

    private ArrayList<ReviewInfo> ReviewInfos;

    public ArrayList<ReviewInfo> getReviewInfos() {
        return ReviewInfos;
    }

    public void setReviewInfos(ArrayList<ReviewInfo> reviewInfos) {
        this.ReviewInfos = reviewInfos;
    }

    public  String getBLZL(){

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  GetIndiAcctSetResGRXX getGRXX(){ 

       return this.GRXX;   

   }


   public  void setGRXX(GetIndiAcctSetResGRXX  GRXX){ 

       this.GRXX = GRXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  GetIndiAcctSetResDWXX getDWXX(){ 

       return this.DWXX;   

   }


   public  void setDWXX(GetIndiAcctSetResDWXX  DWXX){ 

       this.DWXX = DWXX;   

   }


   public  GetIndiAcctSetResGRZHXX getGRZHXX(){

       return this.GRZHXX;

   }


   public  void setGRZHXX(GetIndiAcctSetResGRZHXX  GRZHXX){

       this.GRZHXX = GRZHXX;

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "GetIndiAcctSetRes{" +  
 
              "BLZL='" + this.BLZL + '\'' + "," +
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "GRXX='" + this.GRXX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWXX='" + this.DWXX + '\'' + "," +
              "GRZHXX='" + this.GRZHXX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}