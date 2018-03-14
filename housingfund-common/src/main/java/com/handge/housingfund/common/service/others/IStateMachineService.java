package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.TokenContext;
import com.handge.housingfund.common.service.collection.model.withdrawlModel.CommonReturn;
import com.handge.housingfund.common.service.others.model.StateMachineConfig;
import com.handge.housingfund.database.enums.BusinessType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuefei_wang on 17-8-8.
 */
public interface IStateMachineService< T extends Object > {
    ArrayList<StateMachineConfig> list(TokenContext tokenContext, String type ,String ywwd,String subtype);

    List<StateMachineConfig> listDetails(TokenContext tokenContext, String type,String subtype );

    List<String> listTypes(TokenContext tokenContext );

    List<String> listSubTypes(TokenContext tokenContext, String type);

    CommonReturn update(TokenContext tokenContext, StateMachineConfig stateMachineConfig,String ywwd);

    CommonReturn updateStatemachineConfig();

    boolean addNewConfig(String subtype);

    void deleteConfig(BusinessType type, String subtype);

//    public  T  listTypes();
//
//    public  T  listSubTypes(String type);
//
//    public  T  listStateMachineConfs(String type, String subType);
//
//    public  T  addStateMachineConf(StateMachineConfModel stateMachineConfModel);
//
//    public  T  addStateMachineConfs(ArrayList<StateMachineConfModel> confModels);
//
//    public  T  updateStateMachineConf(String id , StateMachineConfModel stateMachineConfModel);
//
//    public  T  updateStateMachineConfs(String type, String subtype, List<StateMachineConfModel> confModels);
//
//    public  T  deleteStateMachineConf(String id);
//
//
//    public T listWorkstation(String type, String subtype);
//
//    public T listWorkstationBusinessType(String workstation);
//
//    public T listWorkstationBusinessSubType(String workstation,String type);
//
//    public T listWorkstationBusinessStateMachineConfs(String workstation,String type,String subType);
//
//    public T listAuditConfsBySubType(String subType, String role, String workstation);
//
//    public T listAuditConfs(String type, String subType, String role, String workstation);
//
//    public T addWorkstationBusinessStateMachineConf(String workstation, StateMachineConfModel stateMachineConfModel);
//
//    public T addWorkstationBusinessStateMachineConfs(String workstation, ArrayList<StateMachineConfModel> confModels);
//
//    public T updateWorkstationBusinessStateMachineConf(String workstation, StateMachineConfModel stateMachineConfModel);
//
//    public T updateWorkstationBusinessStateMachineConfs(String workstation, ArrayList<StateMachineConfModel> confModels);
//
//    public T addWorkStation(String workstation);


}
