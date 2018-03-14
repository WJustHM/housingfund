package com.handge.housingfund.statemachine.trigger;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

/**
 * Created by xuefei_wang on 17-7-13.
 */
public abstract class IPersitListener implements PersistStateMachineHandler.PersistStateChangeListener {

    /**
     * do not change it
     *
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    public void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
        if (message != null && message.getHeaders().containsKey(TaskEntity.key)) {
            TaskEntity taskEntity = (TaskEntity) message.getHeaders().get(TaskEntity.key);
             String event = message.getPayload();
            try {
                persist(state,event,taskEntity);
            } catch (Exception e) {
                stateMachine.stop();
                e.printStackTrace();
                throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "状态机执行出错");
            }
        }
    }


    /**
     * 写入到数据库持久化
     * @param state
     * @param event
     * @param taskEntity
     */
    public abstract void   persist(State<String, String> state,String event, TaskEntity taskEntity);

}
