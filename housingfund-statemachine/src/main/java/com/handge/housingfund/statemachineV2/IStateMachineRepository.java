package com.handge.housingfund.statemachineV2;

import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.entity.TransEntity;
import org.springframework.statemachine.StateMachine;

import java.util.Collection;

/**
 * Created by xuefei_wang on 17-7-14.
 */
public interface IStateMachineRepository {

    void store(String taskId, Collection<TransEntity> transEntities);

    StateMachine<String,String> loadOrCreateStateMachine(TaskEntity taskEntity ,String event);
}
