package com.handge.housingfund.common.service.collection.model.individual;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctAlterPostGRXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctAlterPostGRXX  implements Serializable{

   private  String    HYZK;  //婚姻状况 

   private  String    JTZZ;  //家庭住址 

   private  String    CSNY;  //出生年月 

   private  String    ZJLX;  //证件类型 

   private  String    ZhiYe;  //职业 

    @Annotation.Postalcode(name = "邮政编码")
   private  String    YZBM;  //邮政编码 

    @Annotation.Custom(name = "职称")
   private  String    ZhiCheng;  //职称 

    @Annotation.Mobile(name = "手机号码")
   private  String    SJHM;  //手机号码 

   private  String    ZJHM;  //证件号码 

   private  String    GDDHHM;  //固定电话号码 

   private  String    XMQP;  //姓名全拼 

   private  String    XingBie;  //性别 

   private  String    JTYSR;  //家庭月收入

   private  String    XueLi;  //学历 

   private  String    XingMing;  //姓名 

   private  String    ZhiWu;  //职务 

    @Annotation.Email(name = "邮箱")
   private  String    YouXiang;  //邮箱 

   public  String getHYZK(){ 

       return this.HYZK;   

   }


   public  void setHYZK(String  HYZK){ 

       this.HYZK = HYZK;   

   }


   public  String getJTZZ(){ 

       return this.JTZZ;   

   }


   public  void setJTZZ(String  JTZZ){ 

       this.JTZZ = JTZZ;   

   }


   public  String getCSNY(){ 

       return this.CSNY;   

   }


   public  void setCSNY(String  CSNY){ 

       this.CSNY = CSNY;   

   }


   public  String getZJLX(){ 

       return this.ZJLX;   

   }


   public  void setZJLX(String  ZJLX){ 

       this.ZJLX = ZJLX;   

   }


   public  String getZhiYe(){ 

       return this.ZhiYe;   

   }


   public  void setZhiYe(String  ZhiYe){ 

       this.ZhiYe = ZhiYe;   

   }


   public  String getYZBM(){ 

       return this.YZBM;   

   }


   public  void setYZBM(String  YZBM){ 

       this.YZBM = YZBM;   

   }


   public  String getZhiCheng(){ 

       return this.ZhiCheng;   

   }


   public  void setZhiCheng(String  ZhiCheng){ 

       this.ZhiCheng = ZhiCheng;   

   }


   public  String getSJHM(){ 

       return this.SJHM;   

   }


   public  void setSJHM(String  SJHM){ 

       this.SJHM = SJHM;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGDDHHM(){ 

       return this.GDDHHM;   

   }


   public  void setGDDHHM(String  GDDHHM){ 

       this.GDDHHM = GDDHHM;   

   }


   public  String getXMQP(){ 

       return this.XMQP;   

   }


   public  void setXMQP(String  XMQP){ 

       this.XMQP = XMQP;   

   }


   public  String getXingBie(){ 

       return this.XingBie;   

   }


   public  void setXingBie(String  XingBie){ 

       this.XingBie = XingBie;   

   }


   public  String getJTYSR(){

       return this.JTYSR;   

   }


   public  void setJTYSR(String  JTYSR){

       this.JTYSR = JTYSR;   

   }


   public  String getXueLi(){ 

       return this.XueLi;   

   }


   public  void setXueLi(String  XueLi){ 

       this.XueLi = XueLi;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


   public  String getZhiWu(){ 

       return this.ZhiWu;   

   }


   public  void setZhiWu(String  ZhiWu){ 

       this.ZhiWu = ZhiWu;   

   }


   public  String getYouXiang(){ 

       return this.YouXiang;   

   }


   public  void setYouXiang(String  YouXiang){ 

       this.YouXiang = YouXiang;   

   }


   public String toString(){ 

       return "IndiAcctAlterPostGRXX{" +  
 
              "HYZK='" + this.HYZK + '\'' + "," +
              "JTZZ='" + this.JTZZ + '\'' + "," +
              "CSNY='" + this.CSNY + '\'' + "," +
              "ZJLX='" + this.ZJLX + '\'' + "," +
              "ZhiYe='" + this.ZhiYe + '\'' + "," +
              "YZBM='" + this.YZBM + '\'' + "," +
              "ZhiCheng='" + this.ZhiCheng + '\'' + "," +
              "SJHM='" + this.SJHM + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "GDDHHM='" + this.GDDHHM + '\'' + "," +
              "XMQP='" + this.XMQP + '\'' + "," +
              "XingBie='" + this.XingBie + '\'' + "," +
              "JTYSR='" + this.JTYSR + '\'' + "," +
              "XueLi='" + this.XueLi + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + "," +
              "ZhiWu='" + this.ZhiWu + '\'' + "," +
              "YouXiang='" + this.YouXiang + '\'' + 

      "}";

  } 
}