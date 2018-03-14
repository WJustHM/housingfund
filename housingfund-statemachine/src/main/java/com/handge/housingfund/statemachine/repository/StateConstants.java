package com.handge.housingfund.statemachine.repository;

import org.springframework.statemachine.state.DefaultPseudoState;
import org.springframework.statemachine.state.ObjectState;
import org.springframework.statemachine.state.PseudoStateKind;

/**
 * Created by xuefei_wang on 17-7-14.
 */
public final class StateConstants {

    public static final ObjectState<String,String> initialState = new ObjectState<String, String>("初始状态",new DefaultPseudoState(PseudoStateKind.INITIAL));

    public static final ObjectState<String,String> failedState = new ObjectState<String, String>("审核不通过");

    public static final ObjectState<String,String> endState = new ObjectState<String, String>("办结",new DefaultPseudoState(PseudoStateKind.END));

    public static final ObjectState<String,String> abandonState = new ObjectState<String, String>("丢弃");


}
