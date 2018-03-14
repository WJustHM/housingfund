package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "GetUnitPayWrongResGZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitPayWrongResGZXX  implements Serializable{

   private  String    DWCJJE;  //单位错缴金额

   private  String    FSE;  //发生额

   private  String    GRZH;  //个人账号 

   private  String    XingMing;  //姓名 

   private  String    ZJHM;  //证件号码 

   private  String GRCJJE;  //个人错缴金额

   private String CJYY;     //错缴原因

   public  String getDWCJJE(){

       return this.DWCJJE;   

   }


   public  void setDWCJJE(String  DWCJJE){

       this.DWCJJE = DWCJJE;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

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


   public  String getZJHM(){ 

       return this.ZJHM;   

   }


   public  void setZJHM(String  ZJHM){ 

       this.ZJHM = ZJHM;   

   }


   public  String getGRCJJE(){

       return this.GRCJJE;

   }


   public  void setGRCJJE(String GRCJJE){

       this.GRCJJE = GRCJJE;

   }

    public String getCJYY() {
        return CJYY;
    }

    public void setCJYY(String CJYY) {
        this.CJYY = CJYY;
    }

    @Override
    public String toString() {
        return "GetUnitPayWrongResGZXX{" +
                "DWCJJE=" + DWCJJE +
                ", FSE=" + FSE +
                ", GRZH='" + GRZH + '\'' +
                ", XingMing='" + XingMing + '\'' +
                ", ZJHM='" + ZJHM + '\'' +
                ", GRCJJE=" + GRCJJE +
                ", CJYY='" + CJYY + '\'' +
                '}';
    }
}