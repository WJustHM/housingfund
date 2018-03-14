package com.handge.housingfund.common.service.collection.model.deposit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitPayHoldResQCQR")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitPayHoldResQCQR  implements Serializable{

   private  String qianNianKS;  //前年亏损

   private  String qianNianRJGZ;  //前年月平均工资

   private  String qianNianYL;  //前年盈利

    private  String quNianKS;  //去年亏损

    private  String quNianRJGZ;  //去年月平均工资

    private  String quNianYL;  //去年盈利


    public String getQianNianKS() {
        return qianNianKS;
    }

    public void setQianNianKS(String qianNianKS) {
        this.qianNianKS = qianNianKS;
    }

    public String getQianNianRJGZ() {
        return qianNianRJGZ;
    }

    public void setQianNianRJGZ(String qianNianRJGZ) {
        this.qianNianRJGZ = qianNianRJGZ;
    }

    public String getQianNianYL() {
        return qianNianYL;
    }

    public void setQianNianYL(String qianNianYL) {
        this.qianNianYL = qianNianYL;
    }

    public String getQuNianKS() {
        return quNianKS;
    }

    public void setQuNianKS(String quNianKS) {
        this.quNianKS = quNianKS;
    }

    public String getQuNianRJGZ() {
        return quNianRJGZ;
    }

    public void setQuNianRJGZ(String quNianRJGZ) {
        this.quNianRJGZ = quNianRJGZ;
    }

    public String getQuNianYL() {
        return quNianYL;
    }

    public void setQuNianYL(String quNianYL) {
        this.quNianYL = quNianYL;
    }

    @Override
    public String toString() {
        return "GetUnitPayHoldResQCQR{" +
                "qianNianKS=" + qianNianKS +
                ", qianNianRJGZ=" + qianNianRJGZ +
                ", qianNianYL=" + qianNianYL +
                ", quNianKS=" + quNianKS +
                ", quNianRJGZ=" + quNianRJGZ +
                ", quNianYL=" + quNianYL +
                '}';
    }
}