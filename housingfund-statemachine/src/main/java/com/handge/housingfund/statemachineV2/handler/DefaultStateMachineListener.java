package com.handge.housingfund.statemachineV2.handler;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.ObjectState;
import org.springframework.statemachine.state.State;

import java.util.Optional;

/**
 * Created by xuefei_wang on 17-8-10.
 */
public class DefaultStateMachineListener extends StateMachineListenerAdapter<String, String> {
    @Override
    public void stateChanged(State<String, String> from, State<String, String> to) {
        Optional<State<String, String>> fromOp = from == null ? Optional.empty() : Optional.of(from);
        Optional<State<String, String>> toOp = to == null ? Optional.empty() : Optional.of(to);
        System.out.println(" from: " + toOp.orElse(new ObjectState<String, String>("No state")).getId() +"  ==>  to: "
                + toOp.orElse(new ObjectState<String, String>("No state")).getId());
        System.out.println("状态转换后做什么？");
    }

    @Override
    public void stateEntered(State<String, String> state) {
        super.stateEntered(state);
    }

    @Override
    public void stateExited(State<String, String> state) {
        System.out.println("状态退出：  ====>  "  + state.getId());

    }

    @Override
    public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {

        try {
            throw exception;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
