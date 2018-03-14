package com.handge.housingfund.statemachineV2.handler;

import com.handge.housingfund.database.dao.ICBusinessStateTransformContextDAO;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.repository.StateConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xuefei_wang on 17-8-10.
 */
@Service(value = "defaultStateMachineInterceptor")
public class DefaultStateMachineInterceptor extends StateMachineInterceptorAdapter<String, String> {

    @Autowired
    private ICBusinessStateTransformContextDAO icBusinessStateTransformContextDAO;

    @Override
    public void postStateChange(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
        log("业务执行状态转换后开始");
        log("afterStatechange: " + stateMachine.getUuid());
        log(transition.getSource().getId() + " ----->   (" +transition.getTrigger().getEvent() + " ) ---->"+ transition.getTarget().getId());
        TaskEntity taskEntity = (TaskEntity) message.getHeaders().get(TaskEntity.key);
        String taskId = taskEntity.getTaskId();
        try {
            if(stateMachine.getTransitions().stream()
                    .filter( trans -> trans.getSource().getId().equals(state.getId()))
                    .anyMatch(trans -> trans.getTarget().getId().equals(StateConstants.endState.getId()))) {
                icBusinessStateTransformContextDAO.list(new HashMap<String, Object>(){{
                    this.put("taskId", taskId);
                }}, null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
                        .forEach(cBusinessStateTransformContext -> {
                            icBusinessStateTransformContextDAO.delete(cBusinessStateTransformContext);
                            log("删除数据库的上下文！");
                        });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log("业务执行状态转换后结束");
//*******************************************************************************************
        HashSet<String> begain = new HashSet<>();
        HashSet<String> end = new HashSet<>();
        Collection<Transition<String, String>> transitions = stateMachine.getTransitions();
        for (Transition<String,String> t : transitions){
            begain.add(t.getSource().getId());
            end.add(t.getTarget().getId());
        }
        HashSet<String> intersection = new HashSet<>();
        intersection.addAll(begain);
        intersection.retainAll(end);
        for (String i : begain){
            if (! intersection.contains(i))  begain.remove(i);
        }
        for (String i : end){
            if (! intersection.contains(i))  end.remove(i);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(" 起点状态集合 －－－＞"+begain);
        System.out.println(" 结束状态集合 －－－＞"+end);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

    }

    private void log(String message){
        System.out.println(">>>>>>>>  defaultStateMachineInterceptor>>>: INFO: " + new Date() + " : >>>>> message:  " + message);
    }

}
