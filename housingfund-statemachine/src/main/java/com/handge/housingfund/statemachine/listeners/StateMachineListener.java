package com.handge.housingfund.statemachine.listeners;

import com.handge.housingfund.statemachine.trigger.IStateMachineListener;
import org.springframework.messaging.MessageHeaders;

/**
 * Created by xuefei on 17-7-9.
 */
public class StateMachineListener extends IStateMachineListener {

    @Override
    public void afterStateChanged(String form, String to) {
        System.out.println("状态转换后做什么？");
    }

    @Override
    public void stateEntered(String state) {
        System.out.println("状态进入　做什么？" +state);
    }

    @Override
    public void stateExited(String state) {
        System.out.println("状态退出　做什么？" +state);
    }

    @Override
    protected void eventNotAccepted(String event, MessageHeaders headers) {
        System.out.println("事件不被接受　做什么？" +event);
    }
}
