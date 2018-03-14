package com.handge.housingfund.statemachine.trigger;

import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.springframework.statemachine.StateMachine;

/**
 * Created by xuefei_wang on 17-7-14.
 */
public interface IStateMachineRepository {

    /**
     *
     * @param taskId 业务流水号
     * @param type　业务类型
     * @param subType　业务子类型
     * @return
     * @throws Exception
     */
    StateMachine<String,String> createStateMachine(String taskId, BusinessType type, String subType, String role) throws Exception;


    StateMachine<String,String> loadStateMachine(String taskId, String role);

    boolean exist(String taskId);

    void save(String taskId, String role, StateMachine<String, String> stateMachine);


    StateMachine<String,String> loadOrCreateStateMachine(TaskEntity taskEntity) throws Exception ;

    /**
     * 权限验证
     * @param taskEntity
     * @return
     */
    boolean validate(TaskEntity taskEntity);
}
