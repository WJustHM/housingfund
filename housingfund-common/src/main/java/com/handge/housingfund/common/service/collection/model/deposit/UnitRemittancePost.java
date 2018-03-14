package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;



@XmlRootElement(name = "UnitRemittancePost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitRemittancePost  implements Serializable{
   private String YWLSH;        //业务流水号

   private  String STYHZH;  //委托银行账户

   private  String    JBRZJLX;  //经办人证件类型 

   private  String    FSE;  //发生额

   private  String STYHMC;  //委托银行名称

   private  String    JBRXM;  //经办人姓名 

   private  String    YWWD;  //业务网点 

   private  String    CZLX;  //操作类型（0:保存；1:提交） 

   private  String STYHDM;  //委托银行代码

   private  ArrayList<UnitRemittancePostQCXX>    QCXX;  //清册信息 

   private  String JBRZJHM;  //经办人证件号码

   private  Integer    FSRS;  //发生人数 

   private  String    HJFS;  //汇缴方式 

   private  String    JXRQ;  //计息日期 

   private  String    HBJNY;  //汇补缴年月 

   private  String    DWZH;  //单位账号 

   private  String STYHHM;  //委托银行户名

   private  String    CZY;  //操作员 

   private  String    QCQRDH;  //清册确认单号 

   private  String    BLZL;  //办理资料 

   public  String getSTYHZH(){

       return this.STYHZH;

   }

    public String getYWLSH() {
        return YWLSH;
    }

    public void setYWLSH(String YWLSH) {
        this.YWLSH = YWLSH;
    }

    public  void setSTYHZH(String STYHZH){

       this.STYHZH = STYHZH;

   }


   public  String getJBRZJLX(){ 

       return this.JBRZJLX;   

   }


   public  void setJBRZJLX(String  JBRZJLX){ 

       this.JBRZJLX = JBRZJLX;   

   }


   public  String getFSE(){

       return this.FSE;   

   }


   public  void setFSE(String  FSE){

       this.FSE = FSE;   

   }


   public  String getSTYHMC(){

       return this.STYHMC;

   }


   public  void setSTYHMC(String STYHMC){

       this.STYHMC = STYHMC;

   }


   public  String getJBRXM(){ 

       return this.JBRXM;   

   }


   public  void setJBRXM(String  JBRXM){ 

       this.JBRXM = JBRXM;   

   }


   public  String getYWWD(){ 

       return this.YWWD;   

   }


   public  void setYWWD(String  YWWD){ 

       this.YWWD = YWWD;   

   }


   public  String getCZLX(){ 

       return this.CZLX;   

   }


   public  void setCZLX(String  CZLX){ 

       this.CZLX = CZLX;   

   }


   public  String getSTYHDM(){

       return this.STYHDM;

   }


   public  void setSTYHDM(String STYHDM){

       this.STYHDM = STYHDM;

   }


   public  ArrayList<UnitRemittancePostQCXX> getQCXX(){ 

       return this.QCXX;   

   }


   public  void setQCXX(ArrayList<UnitRemittancePostQCXX>  QCXX){ 

       this.QCXX = QCXX;   

   }


   public  String getJBRZJHM(){

       return this.JBRZJHM;

   }


   public  void setJBRZJHM(String JBRZJHM){

       this.JBRZJHM = JBRZJHM;

   }


   public  Integer getFSRS(){ 

       return this.FSRS;   

   }


   public  void setFSRS(Integer  FSRS){ 

       this.FSRS = FSRS;   

   }


   public  String getHJFS(){ 

       return this.HJFS;   

   }


   public  void setHJFS(String  HJFS){ 

       this.HJFS = HJFS;   

   }


   public  String getJXRQ(){ 

       return this.JXRQ;   

   }


   public  void setJXRQ(String  JXRQ){ 

       this.JXRQ = JXRQ;   

   }


   public  String getHBJNY(){ 

       return this.HBJNY;   

   }


   public  void setHBJNY(String  HBJNY){ 

       this.HBJNY = HBJNY;   

   }


   public  String getDWZH(){ 

       return this.DWZH;   

   }


   public  void setDWZH(String  DWZH){ 

       this.DWZH = DWZH;   

   }


   public  String getSTYHHM(){

       return this.STYHHM;

   }


   public  void setSTYHHM(String STYHHM){

       this.STYHHM = STYHHM;

   }


   public  String getCZY(){ 

       return this.CZY;   

   }


   public  void setCZY(String  CZY){ 

       this.CZY = CZY;   

   }


   public  String getQCQRDH(){ 

       return this.QCQRDH;   

   }


   public  void setQCQRDH(String  QCQRDH){ 

       this.QCQRDH = QCQRDH;   

   }


   public  String getBLZL(){ 

       return this.BLZL;   

   }


   public  void setBLZL(String  BLZL){ 

       this.BLZL = BLZL;   

   }


    @Override
    public String toString() {
        return "UnitRemittancePost{" +
                "YWLSH='" + YWLSH + '\'' +
                ", STYHZH='" + STYHZH + '\'' +
                ", JBRZJLX='" + JBRZJLX + '\'' +
                ", FSE=" + FSE +
                ", STYHMC='" + STYHMC + '\'' +
                ", JBRXM='" + JBRXM + '\'' +
                ", YWWD='" + YWWD + '\'' +
                ", CZLX='" + CZLX + '\'' +
                ", STYHDM='" + STYHDM + '\'' +
                ", QCXX=" + QCXX +
                ", JBRZJHM='" + JBRZJHM + '\'' +
                ", FSRS=" + FSRS +
                ", HJFS='" + HJFS + '\'' +
                ", JXRQ='" + JXRQ + '\'' +
                ", HBJNY='" + HBJNY + '\'' +
                ", DWZH='" + DWZH + '\'' +
                ", STYHHM='" + STYHHM + '\'' +
                ", CZY='" + CZY + '\'' +
                ", QCQRDH='" + QCQRDH + '\'' +
                ", BLZL='" + BLZL + '\'' +
                '}';
    }
}