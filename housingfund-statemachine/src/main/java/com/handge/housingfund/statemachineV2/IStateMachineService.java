package com.handge.housingfund.statemachineV2;

import com.handge.housingfund.statemachine.entity.TaskEntity;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.support.StateMachineInterceptor;

/**
 * Created by 周君 on 2017/7/25.
 * 状态机服务类
 * 外部使用，通过调用该接口，实现对应功能
 */
public interface IStateMachineService {

    /**
     * 设置持久化类，根据自己的业务需求设置，实现 IPersitListener 接口
     * @param hander IPersitListener 实现类
     */
    void registerPersistHandler(PersistStateMachineHandler.PersistStateChangeListener hander);

    /**
     * 状态机监听器，在状态的改变之间的处理，根据业务需求设置，非必要设置
     * @param stateMachineListener  IStateMachineListener 实现类
     */
    void  registerStateMachineListener(StateMachineListenerAdapter stateMachineListener);


    /**
     *
      * @param stateMachineInterceptor
     */
    void registerStateMachineInterceptor(StateMachineInterceptor stateMachineInterceptor);


    /**
     * 设置状态机的仓库类
     * @param stateMachineRepository IStateMachineRepository实现类
     */
    void registerStateMachineRepository(IStateMachineRepository stateMachineRepository);



    /**
     * 销毁当前服务
     * @throws Exception
     */
    void destroy() throws Exception;



    /**
     * 发送触发事件给状态机，改变业务的状态
     * @param event 触发状态改变的状态，根据状态规范设置
     * @param taskEntity 构建传入的 TaskEntity 信息实例，其中的 taskId 属性信息为必要信息
     * @return true/false
     * @throws Exception
     */
    boolean handle(String event, TaskEntity taskEntity);


    public  boolean handle(String event, TaskEntity taskEntity ,String state) throws Exception;

    /**
     * 检查权限
     * @param taskEntity
     * @return
     */
    boolean checkpermission(TaskEntity taskEntity);

}
