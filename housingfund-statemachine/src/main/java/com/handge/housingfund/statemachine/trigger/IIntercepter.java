package com.handge.housingfund.statemachine.trigger;

import org.springframework.statemachine.support.StateMachineInterceptor;

import java.util.Date;

/**
 * Created by 周君 on 2017/8/2.
 */
public interface IIntercepter<S, E> extends StateMachineInterceptor<S, E> {
    default void log(String message){
        System.out.println(">>>>>>>>  Interceptor>>>: INFO: " + new Date() + " : >>>>> message:  " + message);
    }
}
