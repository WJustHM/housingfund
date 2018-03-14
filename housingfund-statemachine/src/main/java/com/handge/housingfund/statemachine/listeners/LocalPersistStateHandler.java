package com.handge.housingfund.statemachine.listeners;

import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.trigger.IPersitListener;
import org.springframework.statemachine.state.State;

/**

 * Created by xuefei on 17-7-9.
 */
public class LocalPersistStateHandler extends IPersitListener {

    /**
     *
     * @param state
     * @param event
     * @param taskEntity
     */
    @Override
    public void persist(State<String, String> state, String event, TaskEntity taskEntity) {
        System.out.println("********写入业务流水数据库开始");
        System.out.println(state.getId());
        System.out.println(event);
        System.out.println(taskEntity.toString());
        System.out.println("*********写入业务流水数据库结束");
    }
}

