package com.handge.housingfund.common.service.collection.model.individual;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctSetPostGRZHXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctSetPostGRZHXX  implements Serializable{

   private  String    GJJSCHJNY;  //公积金首次汇缴年月 

   @Annotation.BankCard(name = "个人存款账户号码")
   private  String    GRCKZHHM;  //个人存款账户号码 

   @Annotation.Money(name = "个人缴存基数")
   private  String    GRJCJS;  //个人缴存基数

   private String   GRCKZHKHYHDM; //个人存款账户开户银行代码

   private String GRCKZHKHYHMC; //个人存款账户开银行名称

    public String getGRCKZHKHYHDM() {
        return GRCKZHKHYHDM;
    }

    public void setGRCKZHKHYHDM(String GRCKZHKHYHDM) {
        this.GRCKZHKHYHDM = GRCKZHKHYHDM;
    }

    public String getGRCKZHKHYHMC() {
        return GRCKZHKHYHMC;
    }

    public void setGRCKZHKHYHMC(String GRCKZHKHYHMC) {
        this.GRCKZHKHYHMC = GRCKZHKHYHMC;
    }

    public  String getGJJSCHJNY(){

       return this.GJJSCHJNY;   

   }


   public  void setGJJSCHJNY(String  GJJSCHJNY){ 

       this.GJJSCHJNY = GJJSCHJNY;   

   }


   public  String getGRCKZHHM(){ 

       return this.GRCKZHHM;   

   }


   public  void setGRCKZHHM(String  GRCKZHHM){ 

       this.GRCKZHHM = GRCKZHHM;   

   }


   public  String getGRJCJS(){

       return this.GRJCJS;   

   }


   public  void setGRJCJS(String  GRJCJS){

       this.GRJCJS = GRJCJS;   

   }


   public String toString(){ 

       return "IndiAcctSetPostGRZHXX{" +
 
              "GJJSCHJNY='" + this.GJJSCHJNY + '\'' + "," +
              "GRCKZHHM='" + this.GRCKZHHM + '\'' + "," +
              "GRJCJS='" + this.GRJCJS + '\'' + 

      "}";

  } 
}