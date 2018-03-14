package com.handge.housingfund.statemachine.trigger;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.ObjectState;
import org.springframework.statemachine.state.State;

import java.util.Optional;

/**
 * Created by xuefei_wang on 17-7-13.
 */
public abstract class IStateMachineListener extends StateMachineListenerAdapter<String,String> {

    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        Optional<State<String, String>> fromOp = from == null ? Optional.empty() : Optional.of(from);
        Optional<State<String, String>> toOp = to == null ? Optional.empty() : Optional.of(to);
        afterStateChanged(fromOp.orElse(new ObjectState<String, String>("No state")).getId(),
                toOp.orElse(new ObjectState<String, String>("No state")).getId());
    }

    @Override
    public void stateEntered(State<String, String> state) {
        stateEntered(state.getId());
    }

    @Override
    public void stateExited(State<String, String> state) {
        stateExited(state.getId());
    }

    @Override
    public void eventNotAccepted(Message<String> event) {

        eventNotAccepted(event.getPayload(),event.getHeaders());
    }


    /**
     *
     * @param form 当前状态
     * @param to　目标状态
     */
    public abstract void afterStateChanged(String form ,String to);

    /**
     * 进入目标状态后
     * @param state 目标状态
     */
    public abstract void stateEntered(String state);

    /**
     * 离开目标状态时
     * @param state
     */
    public abstract void stateExited(String state);


    /**
     * 事件未处理时
     * @param event
     * @param headers
     */
    protected abstract void eventNotAccepted(String event, MessageHeaders headers);




}
