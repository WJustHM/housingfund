package com.handge.housingfund.common.service.collection.model.unit;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitAcctSetPostJBRXX")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitAcctSetPostJBRXX  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   @Annotation.Phone(name = "经办人固定电话号码")
   private  String    JBRGDDHHM;  //经办人固定电话号码 

   private  String    JBRXM;  //经办人姓名 

   private  String    JBRZJHM;  //经办人证件号码 

   @Annotation.Mobile(name = "经办人手机号码")
   private  String    JBRSJHM;  //经办人手机号码 

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRGDDHHM(){ 

       return this.JBRGDDHHM;   

   }


   public  void setJBRGDDHHM(String  JBRGDDHHM){ 

       this.JBRGDDHHM = JBRGDDHHM;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getJBRSJHM(){ 

       return this.JBRSJHM;   

   }


   public  void setJBRSJHM(String  JBRSJHM){ 

       this.JBRSJHM = JBRSJHM;   

   }


   public String toString(){ 

       return "UnitAcctSetPostJBRXX{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRGDDHHM='" + this.JBRGDDHHM + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "JBRSJHM='" + this.JBRSJHM + '\'' + 

      "}";

  } 
}