package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "ListUnitPayCallHistoryResRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUnitPayCallHistoryResRes  implements Serializable{

   private  String    CJR;  //催缴人 

   private  String    CJFS;  //催缴方式 

   private  String    CJSJ;  //催缴时间

   private String DWMC;     //单位名称

   private String DWZH;     //单位账号

   public  String getCJR(){ 

       return this.CJR;   

   }


   public  void setCJR(String  CJR){ 

       this.CJR = CJR;   

   }


   public  String getCJFS(){ 

       return this.CJFS;   

   }


   public  void setCJFS(String  CJFS){ 

       this.CJFS = CJFS;   

   }


   public  String getCJSJ(){ 

       return this.CJSJ;   

   }


   public  void setCJSJ(String  CJSJ){ 

       this.CJSJ = CJSJ;   

   }

    public String getDWMC() {
        return DWMC;
    }

    public void setDWMC(String DWMC) {
        this.DWMC = DWMC;
    }

    public String getDWZH() {
        return DWZH;
    }

    public void setDWZH(String DWZH) {
        this.DWZH = DWZH;
    }

    @Override
    public String toString() {
        return "ListUnitPayCallHistoryResRes{" +
                "CJR='" + CJR + '\'' +
                ", CJFS='" + CJFS + '\'' +
                ", CJSJ='" + CJSJ + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                '}';
    }
}