package com.handge.housingfund.common.service.collection.model.deposit;

import com.handge.housingfund.common.annotation.Annotation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitPayHoldPostQCQR")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayHoldPostQCQR  implements Serializable{
    @Annotation.Money(name = "去年盈利")
   private  String    QuNianYL;  //去年盈利
    @Annotation.Money(name = "前年亏损")
   private  String    QianNianKS;  //前年亏损
    @Annotation.Money(name = "前年盈利")
   private  String    QianNianYL;  //前年盈利
    @Annotation.Money(name = "去年人月平均工资")
   private  String    QuNianRYPJGZ;  //去年人月平均工资
    @Annotation.Money(name = "前年人月平均工资")
   private  String    QianNianRYPJGZ;  //前年人月平均工资
    @Annotation.Money(name = "去年亏损")
   private  String    QuNianKS;  //去年亏损

   public  String getQuNianYL(){

       return this.QuNianYL;   

   }


   public  void setQuNianYL(String  QuNianYL){

       this.QuNianYL = QuNianYL;   

   }


   public  String getQianNianKS(){

       return this.QianNianKS;   

   }


   public  void setQianNianKS(String  QianNianKS){

       this.QianNianKS = QianNianKS;   

   }


   public  String getQianNianYL(){

       return this.QianNianYL;   

   }


   public  void setQianNianYL(String  QianNianYL){

       this.QianNianYL = QianNianYL;   

   }


   public  String getQuNianRYPJGZ(){

       return this.QuNianRYPJGZ;   

   }


   public  void setQuNianRYPJGZ(String  QuNianRYPJGZ){

       this.QuNianRYPJGZ = QuNianRYPJGZ;   

   }


   public  String getQianNianRYPJGZ(){

       return this.QianNianRYPJGZ;   

   }


   public  void setQianNianRYPJGZ(String  QianNianRYPJGZ){

       this.QianNianRYPJGZ = QianNianRYPJGZ;   

   }


   public  String getQuNianKS(){

       return this.QuNianKS;   

   }


   public  void setQuNianKS(String  QuNianKS){

       this.QuNianKS = QuNianKS;   

   }


   public String toString(){ 

       return "UnitPayHoldPostQCQR{" +  
 
              "QuNianYL='" + this.QuNianYL + '\'' + "," +
              "QianNianKS='" + this.QianNianKS + '\'' + "," +
              "QianNianYL='" + this.QianNianYL + '\'' + "," +
              "QuNianRYPJGZ='" + this.QuNianRYPJGZ + '\'' + "," +
              "QianNianRYPJGZ='" + this.QianNianRYPJGZ + '\'' + "," +
              "QuNianKS='" + this.QuNianKS + '\'' + 

      "}";

  } 
}