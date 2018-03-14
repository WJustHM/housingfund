package com.handge.housingfund.common.service.collection.model.individual;

import com.handge.housingfund.common.service.collection.model.deposit.GetPersonRadixBeforeResJCJSTZXX;
import com.handge.housingfund.common.service.collection.model.unit.AutoUnitAcctActionResDWGJXX;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sjw on 2017/11/9.
 */
@XmlRootElement(name = "PersonRadixExcelRes")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRadixExcelRes implements Serializable {

    private AutoUnitAcctActionResDWGJXX DWGJXX;  // 单位基本信息

    private ArrayList<GetPersonRadixBeforeResJCJSTZXX> JCJSTZXX;  //缴存基数调整信息

    private String SXNY; //生效年月

    public AutoUnitAcctActionResDWGJXX getDWGJXX() {
        return DWGJXX;
    }

    public void setDWGJXX(AutoUnitAcctActionResDWGJXX DWGJXX) {
        this.DWGJXX = DWGJXX;
    }

    public ArrayList<GetPersonRadixBeforeResJCJSTZXX> getJCJSTZXX() {
        return JCJSTZXX;
    }

    public void setJCJSTZXX(ArrayList<GetPersonRadixBeforeResJCJSTZXX> JCJSTZXX) {
        this.JCJSTZXX = JCJSTZXX;
    }

    public String getSXNY() {
        return SXNY;
    }

    public void setSXNY(String SXNY) {
        this.SXNY = SXNY;
    }
}
