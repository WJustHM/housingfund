package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CBankCenterInfo;

/**
 * Created by gxy on 17-12-7.
 */
public interface ICBankCenterInfoDAO extends IBaseDAO<CBankCenterInfo>  {

    public String getCenterNameByNum(String number);

    public String getCenterNodeByNum(String number);


}
