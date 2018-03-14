package com.handge.housingfund.statemachine.actions;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

/**
 * Created by xuefei_wang on 17-7-11.
 */
public class Actioner implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> stateContext) {
    }
}
