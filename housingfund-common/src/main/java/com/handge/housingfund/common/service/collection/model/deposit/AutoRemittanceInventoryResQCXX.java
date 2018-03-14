package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;


@XmlRootElement(name = "AutoRemittanceInventoryResQCXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoRemittanceInventoryResQCXX  implements Serializable{

   private  String    GRJCJS;  //个人缴存基数

   private  String    DWYJCE;  //单位月缴存额

   private  String    HeJi;  //合计

   private  String    ZJHM;  //证件号码 

   private  String    GRYJCE;  //个人月缴存额

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名

    public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public  String getDWYJCE(){

       return this.DWYJCE;   

   }


   public  void setDWYJCE(String  DWYJCE){

       this.DWYJCE = DWYJCE;   

   }


   public  String getHeJi(){

       return this.HeJi;   

   }


   public  void setHeJi(String  HeJi){

       this.HeJi = HeJi;   

   }


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGRYJCE(){

       return this.GRYJCE;   

   }


   public  void setGRYJCE(String  GRYJCE){

       this.GRYJCE = GRYJCE;   

   }


   public  String getGRZH(){ 

       return this.GRZH;   

   }


   public  void setGRZH(String  GRZH){ 

       this.GRZH = GRZH;   

   }


   public  String getXingMing(){ 

       return this.XingMing;   

   }


   public  void setXingMing(String  XingMing){ 

       this.XingMing = XingMing;   

   }


    public AutoRemittanceInventoryResQCXX() {
    }

    public AutoRemittanceInventoryResQCXX(Object[] obj) {
        this.setGRZH(obj[0].toString());
        this.setXingMing(obj[1].toString());
        this.setZJHM(obj[2].toString());
        BigDecimal grjcjs = new BigDecimal(obj[3].toString());
        BigDecimal dwyjce = new BigDecimal(obj[4].toString());
        BigDecimal gryjce = new BigDecimal(obj[5].toString());
        BigDecimal heji = dwyjce.add(gryjce);
        this.setGRJCJS(grjcjs.toString());
        this.setDWYJCE(dwyjce.toString());
        this.setGRYJCE(gryjce.toString());
        this.setHeJi(heji.toString());
    }

    public String toString(){

       return "AutoRemittanceInventoryResQCXX{" +  
 
              "GRJCJS='" + this.GRJCJS + '\'' + "," +
              "DWYJCE='" + this.DWYJCE + '\'' + "," +
              "HeJi='" + this.HeJi + '\'' + "," +
              "ZJHM='" + this.ZJHM + '\'' + "," +
              "GRYJCE='" + this.GRYJCE + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' + 

      "}";

  } 
}