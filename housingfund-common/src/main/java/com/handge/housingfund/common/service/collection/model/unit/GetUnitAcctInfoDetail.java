package com.handge.housingfund.common.service.collection.model.unit;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;



@XmlRootElement(name = "GetUnitAcctInfoDetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetUnitAcctInfoDetail  implements Serializable{

   private  GetUnitAcctInfoDetailDWDJXX    DWDJXX;  // 

   private  GetUnitAcctInfoDetailDWLXFS    DWLXFS;  // 

   private  GetUnitAcctInfoDetailDWGJXX    DWGJXX;  // 

   private  GetUnitAcctInfoDetailJBRXX    JBRXX;  // 

   private  GetUnitAcctInfoDetailWTYHXX    WTYHXX;  // 

   private String CZY;

   private String YWWD;

   private String BLZL;

   public  GetUnitAcctInfoDetailDWDJXX getDWDJXX(){ 

       return this.DWDJXX;   

   }


   public  void setDWDJXX(GetUnitAcctInfoDetailDWDJXX  DWDJXX){ 

       this.DWDJXX = DWDJXX;   

   }


   public  GetUnitAcctInfoDetailDWLXFS getDWLXFS(){ 

       return this.DWLXFS;   

   }


   public  void setDWLXFS(GetUnitAcctInfoDetailDWLXFS  DWLXFS){ 

       this.DWLXFS = DWLXFS;   

   }


   public  GetUnitAcctInfoDetailDWGJXX getDWGJXX(){ 

       return this.DWGJXX;   

   }


   public  void setDWGJXX(GetUnitAcctInfoDetailDWGJXX  DWGJXX){ 

       this.DWGJXX = DWGJXX;   

   }


   public  GetUnitAcctInfoDetailJBRXX getJBRXX(){ 

       return this.JBRXX;   

   }


   public  void setJBRXX(GetUnitAcctInfoDetailJBRXX  JBRXX){ 

       this.JBRXX = JBRXX;   

   }


   public  GetUnitAcctInfoDetailWTYHXX getWTYHXX(){ 

       return this.WTYHXX;   

   }

   public  void setWTYHXX(GetUnitAcctInfoDetailWTYHXX  WTYHXX){ 

       this.WTYHXX = WTYHXX;   

   }

    public String getCZY() {
        return CZY;
    }

    public void setCZY(String CZY) {
        this.CZY = CZY;
    }

    public String getYWWD() {
        return YWWD;
    }

    public void setYWWD(String YWWD) {
        this.YWWD = YWWD;
    }

    public String getBLZL() {
        return BLZL;
    }

    public void setBLZL(String BLZL) {
        this.BLZL = BLZL;
    }

    public String toString(){

       return "GetUnitAcctInfoDetail{" +  

              "DWDJXX='" + this.DWDJXX + '\'' + "," +
              "DWLXFS='" + this.DWLXFS + '\'' + "," +
              "DWGJXX='" + this.DWGJXX + '\'' + "," +
              "JBRXX='" + this.JBRXX + '\'' + "," +
              "WTYHXX='" + this.WTYHXX + '\'' + 

      "}";

  } 
}