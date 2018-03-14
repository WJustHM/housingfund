package com.handge.housingfund.common.service.collection.model.individual;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "AutoIndiAcctActionRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoIndiAcctActionRes  implements Serializable{

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    JBRZJHM;  //经办人证件号码 

   private  String    JBRXM;  //经办人姓名 

   private  String CZY;  //操作人员

   private String YWWD;  //业务网点

   private  AutoIndiAcctActionResGRZHXX    GRZHXX;  //个人账户信息 

   private  AutoIndiAcctActionResDWXX    DWXX;  //单位信息

   private  GetIndiAcctAlterResGRXX GRXX;    //个人信息

    public GetIndiAcctAlterResGRXX getGRXX() {
        return GRXX;
    }

    public void setGRXX(GetIndiAcctAlterResGRXX GRXX) {
        this.GRXX = GRXX;
    }

    public  String getJBRZJLX(){

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getJBRZJHM(){ 

       return this.JBRZJHM;   

   }


   public  void setJBRZJHM(String  JBRZJHM){ 

       this.JBRZJHM = JBRZJHM;   

   }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public  String getJBRXM(){

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getCZY(){

       return this.CZY;

   }


   public  void setCZY(String CZY){

       this.CZY = CZY;

   }


   public  AutoIndiAcctActionResGRZHXX getGRZHXX(){ 

       return this.GRZHXX;   

   }


   public  void setGRZHXX(AutoIndiAcctActionResGRZHXX  GRZHXX){ 

       this.GRZHXX = GRZHXX;   

   }


   public  AutoIndiAcctActionResDWXX getDWXX(){ 

       return this.DWXX;   

   }


   public  void setDWXX(AutoIndiAcctActionResDWXX  DWXX){ 

       this.DWXX = DWXX;   

   }

    @Override
    public String toString() {
        return "AutoIndiAcctActionRes{" +
                "JBRZJLX='" + JBRZJLX + '\'' +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", CZY='" + CZY + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", GRZHXX=" + GRZHXX +
                ", DWXX=" + DWXX +
                ", GRXX=" + GRXX +
                '}';
    }
}