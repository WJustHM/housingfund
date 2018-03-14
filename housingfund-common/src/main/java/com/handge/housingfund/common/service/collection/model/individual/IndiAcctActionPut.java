package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctActionPut")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctActionPut  implements Serializable{

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  String    CZY;  //操作员

   private  String    JBRXM; //经办人姓名

   private  String    JBRZJHM; //经办人证件号码

   private  String    JBRZJLX; //经办人证件类型

   private  String    BLZL;  //办理资料 

   private  String    YWWD;  //业务网点 

   private  String    CZYY;  //操作原因

   private  String    SXNY;  //生效年月

   private  String    BeiZhu;  //备注

   private  String    CZMC;  //操作名称(05:封存;04:启封；14:冻结;15:解冻)

    public IndiAcctActionPut() {
    }



   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getCZYY(){ 

       return this.CZYY;   

   }


   public  void setCZYY(String  CZYY){ 

       this.CZYY = CZYY;   

   }


   public  String getSXNY(){ 

       return this.SXNY;   

   }


   public  void setSXNY(String  SXNY){ 

       this.SXNY = SXNY;   

   }


   public  String getCZMC(){ 

       return this.CZMC;   

   }


   public  void setCZMC(String  CZMC){ 

       this.CZMC = CZMC;   

   }


   public String toString(){ 

       return "IndiAcctActionPut{" +
              "CZLX='" + this.CZLX + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "BLZL='" + this.BLZL + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "CZYY='" + this.CZYY + '\'' + "," +
              "SXNY='" + this.SXNY + '\'' + "," +
              "BeiZhu='" + this.BeiZhu + '\'' +
              "JBRXM='" + this.JBRXM + '\'' +
              "JBRZJLX='" + this.JBRZJLX + '\'' +
              "JBRZJHM='" + this.JBRZJHM + '\'' +
              "CZMC='" + this.CZMC + '\'' +


      "}";

  }

    public String getBeiZhu() {
        return BeiZhu;
    }

    public void setBeiZhu(String beiZhu) {
        BeiZhu = beiZhu;
    }

    public String getJBRXM() {
        return JBRXM;
    }

    public void setJBRXM(String JBRXM) {
        this.JBRXM = JBRXM;
    }

    public String getJBRZJLX() {
        return JBRZJLX;
    }

    public void setJBRZJLX(String JBRZJLX) {
        this.JBRZJLX = JBRZJLX;
    }

    public String getJBRZJHM() {
        return JBRZJHM;
    }

    public void setJBRZJHM(String JBRZJHM) {
        this.JBRZJHM = JBRZJHM;
    }
}