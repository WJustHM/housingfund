package com.handge.housingfund.collection.utils;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.HashMap;

@SuppressWarnings({"unchecked", "Duplicates", "Convert2Lambda"})
public  class StateMachineUtils {

    public interface StateChangeHandler {

        void onStateChange(boolean succeed, String next, Exception e);
    }

    public static void updateState(IStateMachineService stateMachineService, String event, TaskEntity taskEntity, StateChangeHandler handler){

        try {

            HashMap map = new HashMap<String,Object>();

            stateMachineService.registerPersistHandler(new PersistStateMachineHandler.PersistStateChangeListener() {

                @Override
                public void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
                    map.put("state",state);
                    map.put("message",message);
                    map.put("transition",transition);
                    map.put("stateMachine",stateMachine);
                }
            });

            if(!stateMachineService.handle(event,taskEntity)){

                handler.onStateChange(false,null,new ErrorException(ReturnEnumeration.Authentication_Failed,"状态转换失败"));

            }else {

                State<String, String> state = (State<String, String>) map.get("state");

                Message<String> message = (Message<String>) map.get("message");

                if (message!=null&&message.getPayload().equals(event)) {

                    if (state == null || state.getId() == null) {
                        handler.onStateChange(false,null,new ErrorException(ReturnEnumeration.Data_MISS,"业务状态"));
                        return;
                    }
                    handler.onStateChange(true, state.getId(), null);
                }else {
                    handler.onStateChange(false, null, new ErrorException(ReturnEnumeration.StateMachineConfig_Retryable_Error,"状态转换失败"));
                }


            }
        } catch (Exception e) {

            handler.onStateChange(false, taskEntity.getStatus(), e);

            e.printStackTrace();
        }
    }
}
