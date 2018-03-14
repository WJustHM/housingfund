package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetApplicantResponseReviewInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetApplicantResponseReviewInformation  implements Serializable {

   private  String    YWWD;  //业务网点 

   private  String    ShiJian;  //时间 

   private  String    QuDao;  //渠道 

   private  String    ZhiWu;  //职务 

   private  String    YiJian;  //意见 

   private  String    CZY;  //操作员 

   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getShiJian(){ 

       return this.ShiJian;   

   }


   public  void setShiJian(String  ShiJian){ 

       this.ShiJian = ShiJian;   

   }


   public  String getQuDao(){ 

       return this.QuDao;   

   }


   public  void setQuDao(String  QuDao){ 

       this.QuDao = QuDao;   

   }


   public  String getZhiWu(){ 

       return this.ZhiWu;   

   }


   public  void setZhiWu(String  ZhiWu){ 

       this.ZhiWu = ZhiWu;   

   }


   public  String getYiJian(){ 

       return this.YiJian;   

   }


   public  void setYiJian(String  YiJian){ 

       this.YiJian = YiJian;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public String toString(){ 

       return "GetApplicantResponseReviewInformation{" +  
 
              "YWWD='" + this.YWWD + '\'' + "," +
              "ShiJian='" + this.ShiJian + '\'' + "," +
              "QuDao='" + this.QuDao + '\'' + "," +
              "ZhiWu='" + this.ZhiWu + '\'' + "," +
              "YiJian='" + this.YiJian + '\'' + "," +
              "CZY='" + this.CZY + '\'' + 

      "}";

  } 
}