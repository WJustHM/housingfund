package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "GetRemittanceInventoryDetailRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRemittanceInventoryDetailRes  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    YWLSH;  //业务流水号 

   private  String    FSE;  //发生额 

   private  String    JBRXM;  //经办人姓名 

   private  String    QCNY;  //清册年月 

   private  String    YWWD;  //业务网点 

   private  GetRemittanceInventoryDetailResYJEZHJ    YJEZHJ;  //月缴存额合计 

   private  String    DWFCRS;  //单位封存人数 

   private  String    FSRS;  //发生人数 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    DWMC;  //单位名称 

   private  String    DWZH;  //单位账号 

   private  String    CZY;  //操作员 

   private  String    DWJCRS;  //单位缴存人数

   private String BLZL;     //办理资料

   private  ArrayList<GetRemittanceInventoryDetailResDWHJQCXX>    DWHJQCXX;  //单位汇缴清册 

   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getYWLSH(){ 

       return this.YWLSH;   

   }


   public  void setYWLSH(String  YWLSH){ 

       this.YWLSH = YWLSH;   

   }


   public  String getFSE(){ 

       return this.FSE;   

   }


   public  void setFSE(String  FSE){ 

       this.FSE = FSE;   

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getQCNY(){ 

       return this.QCNY;   

   }


   public  void setQCNY(String  QCNY){ 

       this.QCNY = QCNY;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  GetRemittanceInventoryDetailResYJEZHJ getYJEZHJ(){ 

       return this.YJEZHJ;   

   }


   public  void setYJEZHJ(GetRemittanceInventoryDetailResYJEZHJ  YJEZHJ){ 

       this.YJEZHJ = YJEZHJ;   

   }


   public  String getDWFCRS(){ 

       return this.DWFCRS;   

   }


   public  void setDWFCRS(String  DWFCRS){ 

       this.DWFCRS = DWFCRS;   

   }


   public  String getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(String  FSRS){ 

       this.FSRS = FSRS;   

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


   public  String getDWJCRS(){ 

       return this.DWJCRS;   

   }


   public  void setDWJCRS(String  DWJCRS){ 

       this.DWJCRS = DWJCRS;   

   }


   public  ArrayList<GetRemittanceInventoryDetailResDWHJQCXX> getDWHJQCXX(){ 

       return this.DWHJQCXX;   

   }


   public  void setDWHJQCXX(ArrayList<GetRemittanceInventoryDetailResDWHJQCXX>  DWHJQCXX){ 

       this.DWHJQCXX = DWHJQCXX;   

   }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    @Override
    public String toString() {
        return "GetRemittanceInventoryDetailRes{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", YWLSH='" + YWLSH + '\'' +
                ", FSE='" + FSE + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", QCNY='" + QCNY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", YJEZHJ=" + YJEZHJ +
                ", DWFCRS='" + DWFCRS + '\'' +
                ", FSRS='" + FSRS + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", DWMC='" + DWMC + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", CZY='" + CZY + '\'' +
                ", DWJCRS='" + DWJCRS + '\'' +
                ", BLZL='" + BLZL + '\'' +
                ", DWHJQCXX=" + DWHJQCXX +
                '}';
    }
}