package com.handge.housingfund.database.dao;

import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.BusinessType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ICStateMachineConfigurationDAO extends IBaseDAO<CStateMachineConfiguration> {

    List<CStateMachineConfiguration> getConfig(BusinessType type, String subType, String status, String event);

    CStateMachineConfiguration getDefinedConfig(String ywwd,String type, String subType, String source, String event);

    @Deprecated
    List<String> getTargets(BusinessType type, String subType, String ywwd, String role);

    boolean isSpecialReview(String source, String subType, String role, String ywwd);

    @Deprecated
    boolean isSpecialReview(String source);

    List<String> getSpecialReviewSource(String subType);

    ArrayList<String> getReviewSources(String role, BusinessType type, String subModule, Collection subTypes, String ywwd);

    ArrayList<String> getSubTypesByAuth(String ywwd, String role, BusinessType type, String prefix);

    /**
     * 用于在审核加锁时，先验证是否有审核资格，避免“死锁”
     * @param ywwd
     * @param role
     * @param subType
     * @param source
     * @return
     */
    public boolean checkIsPermission(String ywwd, String role, String subType, String source);
}
