package com.handge.housingfund.statemachine;

import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.service.StateMachineService;
import com.handge.housingfund.statemachine.trigger.IIntercepter;
import com.handge.housingfund.statemachine.trigger.IPersitListener;
import com.handge.housingfund.statemachine.trigger.IStateMachineListener;
import com.handge.housingfund.statemachine.trigger.IStateMachineRepository;
import org.springframework.messaging.Message;

/**
 * Created by 周君 on 2017/7/25.
 * 状态机服务类
 * 外部使用，通过调用该接口，实现对应功能
 */
public interface IStateMachineService {


    /**
     * 设置业务的类型以及业务子类型
     * @param type
     * @param subType
     */
    @Deprecated
    void setUp(BusinessType type, String subType);

    /**
     * 设置持久化类，根据自己的业务需求设置，实现 IPersitListener 接口
     * @param hander IPersitListener 实现类
     */
    void registerPersistHandler(IPersitListener hander);

    /**
     * 状态机监听器，在状态的改变之间的处理，根据业务需求设置，非必要设置
     * @param stateMachineListener  IStateMachineListener 实现类
     */
    void  registerStateMachineListener(IStateMachineListener stateMachineListener);


    /**
     *
      * @param stateMachineInterceptor
     */
    void registerStateMachineInterceptor(IIntercepter stateMachineInterceptor);


    /**
     * 设置状态机的仓库类
     * @param stateMachineRepository IStateMachineRepository实现类
     */
    void registerStateMachineRepository(IStateMachineRepository stateMachineRepository);

    /**
     * 打印业务的执行流程
     */
    void printTransitionContext();

    /**
     * 重新加载状态机服务
     * @param taskId
     * @return StateMachineService
     * @throws Exception
     */
    StateMachineService reload(String taskId) throws Exception;

    /**
     * 销毁当前服务
     * @throws Exception
     */
    void destroy() throws Exception;

    /**
     * 停止状态机业务
     */
    void stop();

    /**
     * 发送消息给状态机，改变状态
     * @param event 消息事件
     * @param state 该笔业务的当前状态
     * @return
     * @throws Exception
     */
    boolean  handle(Message<String> event, String state) throws Exception;

    /**
     * 发送触发事件给状态机，改变业务的状态
     * @param event 触发状态改变的状态，根据状态规范设置
     * @param taskEntity 构建传入的 TaskEntity 信息实例，其中的 taskId 属性信息为必要信息
     * @param state 该笔业务的当前状态
     * @return true/false
     * @throws Exception
     */
    boolean handle(String event, TaskEntity taskEntity , String state) throws Exception;


}
