package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "PersonRadixPutJCJSTZXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRadixPutJCJSTZXX  implements Serializable{

    private static final long serialVersionUID = -3982267645976330595L;
    private  String    TZHGRJCJS;  //调整后个人缴存基数

   private  String    TZQGRJCJS;  //调整前个人缴存基数

   private  String    GRZH;  //个人账号

    private String GRYJCE;

    private String ZJHM;

    public String getZJHM() {
        return ZJHM;
    }

    public void setZJHM(String ZJHM) {
        this.ZJHM = ZJHM;
    }

    public String getGRYJCE() {
        return GRYJCE;
    }

    public void setGRYJCE(String GRYJCE) {
        this.GRYJCE = GRYJCE;
    }


    public String getTZHGRJCJS() {
        return TZHGRJCJS;
    }

    public void setTZHGRJCJS(String TZHGRJCJS) {
        this.TZHGRJCJS = TZHGRJCJS;
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


   public String toString(){ 

       return "PersonRadixPutJCJSTZXX{" +  
 
              "TZHGRJCJS='" + this.TZHGRJCJS + '\'' + "," +
              "TZQGRJCJS='" + this.TZQGRJCJS + '\'' + "," +
              "GRZH='" + this.GRZH + '\'' + 

      "}";

  } 
}