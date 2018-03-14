package com.handge.housingfund.common.service.collection.model.individual;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sjw on 2017/8/17.
 */

@XmlRootElement(name = "GetAcctsInfoDetailsRes")
@XmlAccessorType(XmlAccessType.FIELD)

public class GetAcctsInfoDetailsRes  implements Serializable{

    private  GetIndiAcctsInfoDetailsResJBXX  JBXX;  //基本信息

    private  ArrayList<GetIndiAcctsInfoDetailsResJCXX>  JCXX; //缴存信息

    private  ArrayList<GetIndiAcctsInfoDetailsResTQXX>  TQXX; //提取信息

    private  ArrayList<GetIndiAcctsInfoDetailsResDKXX>  DKXX; //贷款信息

    private String BLZL;

    public GetIndiAcctsInfoDetailsResJBXX getJBXX() {
        return JBXX;
    }

    public void setJBXX(GetIndiAcctsInfoDetailsResJBXX JBXX) {
        this.JBXX = JBXX;
    }

    public ArrayList<GetIndiAcctsInfoDetailsResJCXX> getJCXX() {
        return JCXX;
    }

    public void setJCXX(ArrayList<GetIndiAcctsInfoDetailsResJCXX> JCXX) {
        this.JCXX = JCXX;
    }

    public ArrayList<GetIndiAcctsInfoDetailsResTQXX> getTQXX() {
        return TQXX;
    }

    public void setTQXX(ArrayList<GetIndiAcctsInfoDetailsResTQXX> TQXX) {
        this.TQXX = TQXX;
    }

    public ArrayList<GetIndiAcctsInfoDetailsResDKXX> getDKXX() {
        return DKXX;
    }

    public void setDKXX(ArrayList<GetIndiAcctsInfoDetailsResDKXX> DKXX) {
        this.DKXX = DKXX;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public String toString(){

        return "GetAcctsInfoDetailsRes{" +

                "JBXX='" + this.JBXX + '\'' + "," +
                "JCXX='" + this.JCXX + '\'' + "," +
                "TQXX='" + this.TQXX + '\'' + "," +
                "DKXX='" + this.DKXX + '\'' + "," +
        "}";

    }
}
