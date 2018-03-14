package com.handge.housingfund.statemachine.listeners;

import com.handge.housingfund.database.dao.ICBusinessStateTransformContextDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.service.TaskTransitionContextService;
import com.handge.housingfund.statemachine.trigger.IIntercepter;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.PseudoStateKind;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by xuefei on 17-7-9.
 */
@Service
public class StateMachineInterceptor implements IIntercepter<String, String> {

    TaskTransitionContextService repository = TaskTransitionContextService.getInstance();

    @Autowired
    ICBusinessStateTransformContextDAO icBusinessStateTransformContextDAO;
    @Autowired
    ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

    public StateMachineInterceptor() throws ConfigurationException {
    }


    /**
     * 事件发生前：
     * 　验证该业务是否已经存在上下文信息．如果有，则改变当前的上下文流程，初始化为原始的上下文信息，继续完成原始流程逻辑；
     * @param message
     * @param stateMachine
     * @return
     */
    public void beforStateChange(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
        log("业务执行状态转换前开始");
        log("beforStateChange: " + stateMachine.getUuid());
        log(transition.getSource().getId() + " ----->   (" +transition.getTrigger().getEvent() + " ) ---->"+ transition.getTarget().getId());
        log("业务执行状态转换前结束");
        try{
            message.getHeaders().forEach((key, value) -> {
                if(key.trim().equals(TaskEntity.key)){
                    TaskEntity task = (TaskEntity)value;
                    icStateMachineConfigurationDAO.list(new HashMap <String, Object>(){{
                        this.put("type", task.getType());
                        this.put("subType",task.getSubtype());
                        this.put("source", state.getId());
                    }},null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
                            .stream()
                            .forEach(cStateMachineConfiguration ->{
                                if(task.getRole() != null && !task.getRole().equals("") && Arrays.stream(task.getRole().split(",")).anyMatch(s ->
                                        s.trim().equals(cStateMachineConfiguration.getRole().trim())))
                                    return;
                                else task.setRole(cStateMachineConfiguration.getRole());
                            });
                    value = task;
                }
            });
            log("添加角色:  " + message.getHeaders().get(TaskEntity.key));
        }catch (Exception e){
            log(" !! erorr:  " + e.getMessage() );
        }
    }

    /**
     * 状态改变之后的操作
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    public void afterStatechange(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {
        log("业务执行状态转换后开始");
        log("afterStatechange: " + stateMachine.getUuid());
        log(transition.getSource().getId() + " ----->   (" +transition.getTrigger().getEvent() + " ) ---->"+ transition.getTarget().getId());
        TaskEntity taskEntity = (TaskEntity) message.getHeaders().get(TaskEntity.key);
        String taskId = taskEntity.getTaskId();
        try {
            if(stateMachine.getTransitions().stream()
                    .filter( trans -> trans.getSource().getId().equals(state.getId()))
                    .anyMatch(trans -> trans.getTarget().getId().equals("结束状态"))) {
                icBusinessStateTransformContextDAO.list(new HashMap<String, Object>(){{this.put("taskId", taskId);}},
                        null, null, null, Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED)
                        .stream()
                        .forEach(cBusinessStateTransformContext -> {
                            icBusinessStateTransformContextDAO.delete(cBusinessStateTransformContext);
                            log("删除数据库的上下文！");
                        });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log("业务执行状态转换后结束");



    }

    /**
     *
     * @param message 消息体　
     */
    protected void finishedStateLife(Message<String> message) {
        TaskEntity taskEntity = (TaskEntity) message.getHeaders().get(TaskEntity.key);
        message.getHeaders().clear();
        log("结束状态的生命周期，回收任务的初始上下文");

    }

    public Exception stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
        stateMachine.stop();
        return exception;
    }

    /**
     *
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    @Override
    public void preStateChange(State<String, String> state, Message<String> message, Transition<String, String> transition,
                               StateMachine<String, String> stateMachine) {
        beforStateChange(state, message, transition, stateMachine);
    }

    /**
     *
     * @param state
     * @param message
     * @param transition
     * @param stateMachine
     */
    @Override
    public void postStateChange(State<String, String> state, Message<String> message, Transition<String, String> transition,
                                StateMachine<String, String> stateMachine) {
        afterStatechange(state, message, transition, stateMachine);

        if(state.getPseudoState().getKind() == PseudoStateKind.END){
            finishedStateLife(message);
        }
    }


    @Override
    public Message<String> preEvent(Message<String> message, StateMachine<String, String> stateMachine) {
        return message;
    }

    @Override
    public StateContext<String, String> preTransition(StateContext<String, String> stateContext) {
        return stateContext;
    }

    @Override
    public StateContext<String, String> postTransition(StateContext<String, String> stateContext) {
        return stateContext;
    }
}
