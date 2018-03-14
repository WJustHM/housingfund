package com.handge.housingfund.common.service.collection.model.individual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "IndiAcctActionPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndiAcctActionPost  implements Serializable{

   private  String    BLZL;  //办理资料 

   private  String    BeiZhu;  //备注 

   private  String    YWWD;  //业务网点 

   private  String    SXNY;  //生效年月（格式yyyy-MM-dd） 

   private  String    CZYY;  //操作原因 

   private  String    CZY;  //操作员 

   private  String    CZLX;  //操作类型（0:保存；1:提交;） 

   private  String    CZMC;  //操作名称(05:封存;04:启封；14:冻结;15:解冻)

   private  String    JBRXM; //经办人姓名

   private  String    JBRZJHM; //经办人证件号码


    public String getJBRXM() {
        return JBRXM;
    }

    public void setJBRXM(String JBRXM) {
        this.JBRXM = JBRXM;
    }

    public String getJBRZJHM() {
        return JBRZJHM;
    }

    public void setJBRZJHM(String JBRZJHM) {
        this.JBRZJHM = JBRZJHM;
    }

    public String getJBRZJLX() {
        return JBRZJLX;
    }

    public void setJBRZJLX(String JBRZJLX) {
        this.JBRZJLX = JBRZJLX;
    }

    private  String    JBRZJLX; //经办人证件类型

   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


   public  String getBeiZhu(){ 

       return this.BeiZhu;   

   }


   public  void setBeiZhu(String  BeiZhu){ 

       this.BeiZhu = BeiZhu;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getSXNY(){ 

       return this.SXNY;   

   }


   public  void setSXNY(String  SXNY){ 

       this.SXNY = SXNY;   

   }


   public  String getCZYY(){ 

       return this.CZYY;   

   }


   public  void setCZYY(String  CZYY){ 

       this.CZYY = CZYY;   

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getCZMC(){ 

       return this.CZMC;   

   }


   public  void setCZMC(String  CZMC){ 

       this.CZMC = CZMC;   

   }


   public String toString(){ 

       return "IndiAcctActionPost{" +
              "BLZL='" + this.BLZL + '\'' + "," +
              "BeiZhu='" + this.BeiZhu + '\'' + "," +
              "YWWD='" + this.YWWD + '\'' + "," +
              "SXNY='" + this.SXNY + '\'' + "," +
              "CZYY='" + this.CZYY + '\'' + "," +
              "CZY='" + this.CZY + '\'' + "," +
              "CZLX='" + this.CZLX + '\'' + "," +
              "CZMC='" + this.CZMC + '\'' +

      "}";

  } 
}