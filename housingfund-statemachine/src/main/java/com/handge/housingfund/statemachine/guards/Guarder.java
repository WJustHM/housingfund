package com.handge.housingfund.statemachine.guards;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

/**
 * Created by xuefei_wang on 17-7-11.
 */
public class Guarder implements Guard<String, String> {

    @Override
    public boolean evaluate(StateContext<String, String> stateContext) {
        return true;
    }
}
