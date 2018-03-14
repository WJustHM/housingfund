package com.handge.housingfund.common.service.collection.model.deposit;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetPersonRadixBeforeRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetPersonRadixBeforeRes  implements Serializable{

    private static final long serialVersionUID = 8023269176323868867L;
    private  String    JBRZJLX;  //经办人证件类型

   private  String    JBRXM;  //经办人姓名 

   private  String    FSRS;  //发生人数 

   private  String    YWWD;  //业务网点 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  ArrayList<GetPersonRadixBeforeResJCJSTZXX>    JCJSTZXX;  //缴存基数调整信息

    private  String  DWJCBL; //单位缴存比列

    private  String  GRJCBL; //个人缴存比列

    private  String  DWYJCEHJ;

    private  String  GRYJCEHJ;

    private  String  YJCEHJ;

    private String SXNY;

    private String DWYHJNY;

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(String  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  ArrayList<GetPersonRadixBeforeResJCJSTZXX> getJCJSTZXX(){ 

       return this.JCJSTZXX;   

   }


   public  void setJCJSTZXX(ArrayList<GetPersonRadixBeforeResJCJSTZXX>  JCJSTZXX){ 

       this.JCJSTZXX = JCJSTZXX;   

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

    public String getDWYJCEHJ() {
        return DWYJCEHJ;
    }

    public void setDWYJCEHJ(String DWYJCEHJ) {
        this.DWYJCEHJ = DWYJCEHJ;
    }

    public String getGRYJCEHJ() {
        return GRYJCEHJ;
    }

    public void setGRYJCEHJ(String GRYJCEHJ) {
        this.GRYJCEHJ = GRYJCEHJ;
    }

    public String getYJCEHJ() {
        return YJCEHJ;
    }

    public void setYJCEHJ(String YJCEHJ) {
        this.YJCEHJ = YJCEHJ;
    }

    public String getSXNY() {
        return SXNY;
    }

    public void setSXNY(String SXNY) {
        this.SXNY = SXNY;
    }

    public String getDWYHJNY() {
        return DWYHJNY;
    }

    public void setDWYHJNY(String DWYHJNY) {
        this.DWYHJNY = DWYHJNY;
    }

    public String toString(){

       return "GetPersonRadixBeforeRes{" +  
 
              "JBRZJLX='" + this.JBRZJLX + '\'' + "," +
              "JBRXM='" + this.JBRXM + '\'' + "," +
              "FSRS='" + this.FSRS + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "JBRZJHM='" + this.JBRZJHM + '\'' + "," +
              "DWMC='" + this.DWMC + '\'' + "," +
              "DWZH='" + this.DWZH + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "JCJSTZXX='" + this.JCJSTZXX + '\'' +
               "DWJCBL='" + this.DWJCBL + '\'' +
               "GRJCBL='" + this.GRJCBL + '\'' +
               "GRYJCEHJ='" + this.GRYJCEHJ + '\'' +
               "DWYJCEHJ='" + this.DWYJCEHJ + '\'' +
               "YJCEHJ='" + this.YJCEHJ + '\'' +

               "}";

  } 
}