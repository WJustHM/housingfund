package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetPersonRadixBeforeResJCJSTZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPersonRadixBeforeResJCJSTZXX  implements Serializable{

    private static final long serialVersionUID = 1020833269235011688L;
    private  String    JZNY;  //缴至年月

    private  String    TZQGRJCJS;  //调整前个人缴存基数

    private  String    GRZH;  //个人账号

    private  String    XingMing;  //姓名

    private String ZJHM;

    private String YJCE;

    private String GRYJCE;

    private String DWYJCE;



    public GetPersonRadixBeforeResJCJSTZXX(Object[] obj) {
        this.setGRZH(obj[0].toString());
        this.setXingMing(obj[1].toString());
        this.setTZQGRJCJS(obj[2].toString());
        this.setYJCE(obj[3].toString());
        this.setJZNY((String)obj[4]);
        this.setZJHM((String)obj[5]);
        this.setGRYJCE(obj[6].toString());
        this.setDWYJCE(obj[7].toString());
    }

    public GetPersonRadixBeforeResJCJSTZXX(){}

    public String getYJCE() {
        return YJCE;
    }

    public void setYJCE(String YJCE) {
        this.YJCE = YJCE;
    }

   public  String getJZNY(){ 

       return this.JZNY;   

   }


    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public  void setJZNY(String  JZNY){

       this.JZNY = JZNY;   

   }


    public String getTZQGRJCJS() {
        return TZQGRJCJS;
    }

    public void setTZQGRJCJS(String TZQGRJCJS) {
        this.TZQGRJCJS = TZQGRJCJS;
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


    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }

    public String getDWYJCE() {
        return DWYJCE;
    }

    public void setDWYJCE(String DWYJCE) {
        this.DWYJCE = DWYJCE;
    }

    public String toString(){

       return "GetPersonRadixBeforeResJCJSTZXX{" +  
 
              "JZNY='" + this.JZNY + '\'' + "," +
              "TZQGRJCJS='" + this.TZQGRJCJS + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + "," +
              "XingMing='" + this.XingMing + '\'' +
               "YJCE='" + this.YJCE + '\'' +
               "GRYJCE='" + this.GRYJCE + '\'' +
               "DWYJCE='" + this.DWYJCE + '\'' +
               "}";

  } 
}