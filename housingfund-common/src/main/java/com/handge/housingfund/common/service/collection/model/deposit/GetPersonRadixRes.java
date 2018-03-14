package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetPersonRadixRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPersonRadixRes  implements Serializable{

    private static final long serialVersionUID = 5460977872697859496L;
    private  ArrayList<GetPersonRadixResJCJSTZXX>    JCJSTZXX;  //缴存基数调整信息

    private  String    BLZL;  //办理资料

    private String SXNY;

    private  String    CZY;  //操作员

    private  GetPersonRadixResDWGJXX    DWGJXX;  //单位关键信息

    private  String    YWWD;  //业务网点

    private String  GRYJCEHJ; //个人月缴存合计

    private String  DWYJCEHJ; //单位月缴存合计

    private String  YJCEHJ; //月缴存合计

    private String  DWJCBL; //单位缴存比例

    private String  GRJCBL; //个人缴存比例

    private String DWYHJNY;


    public  ArrayList<GetPersonRadixResJCJSTZXX> getJCJSTZXX(){

       return this.JCJSTZXX;   

   }

    public String getSXNY() {
        return SXNY;
    }

    public void setSXNY(String SXNY) {
        this.SXNY = SXNY;
    }

    public  void setJCJSTZXX(ArrayList<GetPersonRadixResJCJSTZXX>  JCJSTZXX){

       this.JCJSTZXX = JCJSTZXX;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  GetPersonRadixResDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(GetPersonRadixResDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }

    public String getGRYJCEHJ() {
        return GRYJCEHJ;
    }

    public void setGRYJCEHJ(String GRYJCEHJ) {
        this.GRYJCEHJ = GRYJCEHJ;
    }

    public String getDWYJCEHJ() {
        return DWYJCEHJ;
    }

    public void setDWYJCEHJ(String DWYJCEHJ) {
        this.DWYJCEHJ = DWYJCEHJ;
    }

    public String getYJCEHJ() {
        return YJCEHJ;
    }

    public void setYJCEHJ(String YJCEHJ) {
        this.YJCEHJ = YJCEHJ;
    }

    public String getDWJCBL() {
        return DWJCBL;
    }

    public void setDWJCBL(String DWJCBL) {
        this.DWJCBL = DWJCBL;
    }

    public String getGRJCBL() {
        return GRJCBL;
    }

    public void setGRJCBL(String GRJCBL) {
        this.GRJCBL = GRJCBL;
    }

    public String getDWYHJNY() {
        return DWYHJNY;
    }

    public void setDWYHJNY(String DWYHJNY) {
        this.DWYHJNY = DWYHJNY;
    }

    public String toString(){

       return "GetPersonRadixRes{" +  
 
              "JCJSTZXX='" + this.JCJSTZXX + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' +
               "GRYJCEHJ='" + this.GRYJCEHJ + '\'' +
               "DWYJCEHJ='" + this.DWYJCEHJ + '\'' +
               "YJCEHJ='" + this.YJCEHJ + '\'' +
               "DWJCBL='" + this.DWJCBL + '\'' +
               "GRJCBL='" + this.GRJCBL + '\'' +
               "}";
  } 
}