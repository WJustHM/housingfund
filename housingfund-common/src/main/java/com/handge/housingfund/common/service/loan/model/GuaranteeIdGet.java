package com.handge.housingfund.common.service.loan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;


@XmlRootElement(name = "GuaranteeIdGet")
@XmlAccessorType(XmlAccessType.FIELD)
public class GuaranteeIdGet  implements Serializable {

    private Integer ZCZJ;  //注册资金

    private String ZZDJ;  //资质等级（0：一级 1：二级 2：三级 3：四级）

    private Integer FRDBZJLX;  //法人代表证件类型

    private ArrayList<GuaranteeIdGetManagerInformation> managerInformation;  //经办人信息

    private String LXR;  //联系人

    private String WD_id;  //网点id

    private String LXDH;  //联系电话

    private String FRDBZJHM;  //法人代表证件号码

    private String GSDZ;  //公司地址

    private String ZZJGDM;  //组织机构代码

    private String FRDB;  //法人代表

    private String BeiZhu;  //备注

    private  String    BLZL;   //办理资料  // 办理资料

    private String DBGS;  //担保公司

    public Integer getZCZJ() {

        return this.ZCZJ;

    }


    public void setZCZJ(Integer ZCZJ) {

        this.ZCZJ = ZCZJ;

    }


    public String getZZDJ() {

        return this.ZZDJ;

    }


    public void setZZDJ(String ZZDJ) {

        this.ZZDJ = ZZDJ;

    }


    public Integer getFRDBZJLX() {

        return this.FRDBZJLX;

    }


    public void setFRDBZJLX(Integer FRDBZJLX) {

        this.FRDBZJLX = FRDBZJLX;

    }


    public ArrayList<GuaranteeIdGetManagerInformation> getmanagerInformation() {

        return this.managerInformation;

    }


    public void setmanagerInformation(ArrayList<GuaranteeIdGetManagerInformation> managerInformation) {

        this.managerInformation = managerInformation;

    }


    public String getLXR() {

        return this.LXR;

    }


    public void setLXR(String LXR) {

        this.LXR = LXR;

    }


    public String getWD_id() {

        return this.WD_id;

    }


    public void setWD_id(String WD_id) {

        this.WD_id = WD_id;

    }


    public String getLXDH() {

        return this.LXDH;

    }


    public void setLXDH(String LXDH) {

        this.LXDH = LXDH;

    }


    public String getFRDBZJHM() {

        return this.FRDBZJHM;

    }


    public void setFRDBZJHM(String FRDBZJHM) {

        this.FRDBZJHM = FRDBZJHM;

    }


    public String getGSDZ() {

        return this.GSDZ;

    }


    public void setGSDZ(String GSDZ) {

        this.GSDZ = GSDZ;

    }


    public String getZZJGDM() {

        return this.ZZJGDM;

    }


    public void setZZJGDM(String ZZJGDM) {

        this.ZZJGDM = ZZJGDM;

    }


    public String getFRDB() {

        return this.FRDB;

    }


    public void setFRDB(String FRDB) {

        this.FRDB = FRDB;

    }


    public String getBeiZhu() {

        return this.BeiZhu;

    }


    public void setBeiZhu(String BeiZhu) {

        this.BeiZhu = BeiZhu;

    }


    public String getSCZL() {

        return this.BLZL;

    }


    public void setSCZL(String SCZL) {

        this.BLZL = SCZL;

    }


    public String getDBGS() {

        return this.DBGS;

    }


    public void setDBGS(String DBGS) {

        this.DBGS = DBGS;

    }


    public String toString() {

        return "GuaranteeIdGet{" +

                "ZCZJ='" + this.ZCZJ + '\'' + "," +
                "ZZDJ='" + this.ZZDJ + '\'' + "," +
                "FRDBZJLX='" + this.FRDBZJLX + '\'' + "," +
                "managerInformation='" + this.managerInformation + '\'' + "," +
                "LXR='" + this.LXR + '\'' + "," +
                "WD_id='" + this.WD_id + '\'' + "," +
                "LXDH='" + this.LXDH + '\'' + "," +
                "FRDBZJHM='" + this.FRDBZJHM + '\'' + "," +
                "GSDZ='" + this.GSDZ + '\'' + "," +
                "ZZJGDM='" + this.ZZJGDM + '\'' + "," +
                "FRDB='" + this.FRDB + '\'' + "," +
                "BeiZhu='" + this.BeiZhu + '\'' + "," +
                "BLZL='" + this.BLZL + '\'' + "," +
                "DBGS='" + this.DBGS + '\'' +

                "}";

    }
}