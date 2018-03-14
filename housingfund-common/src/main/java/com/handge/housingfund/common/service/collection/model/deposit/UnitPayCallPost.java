package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "UnitPayCallPost")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitPayCallPost  implements Serializable{

   private BatchSubmission  batchSubmission; //业务流水号

   private  String    CJR;  //催缴人 

   private  String    CJFS;  //催缴方式 

   private  String    DWMC;  //单位名称

    public BatchSubmission getBatchSubmission() {
        return batchSubmission;
    }

    public void setBatchSubmission(BatchSubmission batchSubmission) {
        this.batchSubmission = batchSubmission;
    }

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


   public  String getDWMC(){ 

       return this.DWMC;   

   }


   public  void setDWMC(String  DWMC){ 

       this.DWMC = DWMC;   

   }


    @Override
    public String toString() {
        return "UnitPayCallPost{" +
                "batchSubmission=" + batchSubmission +
                ", CJR='" + CJR + '\'' +
                ", CJFS='" + CJFS + '\'' +
                ", DWMC='" + DWMC + '\'' +
                '}';
    }
}