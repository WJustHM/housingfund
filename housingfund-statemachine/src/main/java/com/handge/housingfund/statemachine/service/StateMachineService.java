package com.handge.housingfund.statemachine.service;

import com.google.common.base.Preconditions;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.statemachine.IStateMachineService;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.listeners.LocalPersistStateHandler;
import com.handge.housingfund.statemachine.listeners.StateMachineInterceptor;
import com.handge.housingfund.statemachine.listeners.StateMachineListener;
import com.handge.housingfund.statemachine.repository.StateMachineRepository;
import com.handge.housingfund.statemachine.trigger.IIntercepter;
import com.handge.housingfund.statemachine.trigger.IPersitListener;
import com.handge.housingfund.statemachine.trigger.IStateMachineListener;
import com.handge.housingfund.statemachine.trigger.IStateMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by xuefei on 17-7-9.
 */
@Component(value = "stateMachineService")
@Scope("prototype")
public class StateMachineService implements IStateMachineService {


    private  String subType ;
    private  BusinessType type;
    private volatile String taskId = "";
    private volatile String role = "Admin"; //默认角色

    private IPersitListener persistListeners;
    private IStateMachineListener stateMachineListener;
    @Autowired
    private IIntercepter interceptor;
    @Autowired
    private IStateMachineRepository stateMachineRepository;

    private volatile PersistStateMachineHandler handler;
    private volatile StateMachine<String, String> stateMachine;

    public StateMachineService(){

    }

    @Override
    @Deprecated
    public void setUp(BusinessType type, String subType) {
        this.type = type;
        this.subType = subType;
    }

    /**
     *
     * @param taskId
     * @throws Exception
     */
    private void startTask(String taskId)throws Exception{
        Preconditions.checkNotNull(taskId,"taskId must be init ,Not to null");
        if(this.stateMachineRepository == null){
            this.stateMachineRepository = new StateMachineRepository();
        }
        if (this.stateMachineRepository.exist(taskId)){
            this.stateMachine = this.stateMachineRepository.loadStateMachine(taskId, this.role);
        }else {
            this.stateMachine = this.stateMachineRepository.createStateMachine(this.taskId, this.type,this.subType, this.role);
        }
        if(this.interceptor == null){
            this.interceptor = new StateMachineInterceptor();
        }
        this.stateMachine.getStateMachineAccessor()
                .withRegion()
                .addStateMachineInterceptor(this.interceptor);

        if(this.stateMachineListener == null){
            this.stateMachineListener  = new StateMachineListener();
        }
        this.stateMachine.addStateListener(this.stateMachineListener);

        this.handler = new PersistStateMachineHandler(this.stateMachine);

        if (this.persistListeners ==null){
            this.persistListeners = new LocalPersistStateHandler();
        }
        printTransitionContext();
        this.handler.addPersistStateChangeListener(this.persistListeners);
        this.handler.afterPropertiesSet();
    }


    private PersistStateMachineHandler loadHandlerContext(TaskEntity taskEntity) throws Exception {
        if(this.stateMachineRepository == null){
            this.stateMachineRepository = new StateMachineRepository();
        }
        this.stateMachine = this.stateMachineRepository.loadOrCreateStateMachine(taskEntity);

        if(this.interceptor == null){
            this.interceptor = new StateMachineInterceptor();
        }

        this.stateMachine.getStateMachineAccessor()
                .withRegion()
                .addStateMachineInterceptor(this.interceptor);

        if(this.stateMachineListener == null){
            this.stateMachineListener  = new StateMachineListener();
        }
        this.stateMachine.addStateListener(this.stateMachineListener);


        if (this.persistListeners ==null){
            this.persistListeners = new LocalPersistStateHandler();
        }
        /**
         * this handlerContext is local var
         */
        PersistStateMachineHandler handlerContext = new PersistStateMachineHandler(this.stateMachine);
        printTransitionContext();
        handlerContext.addPersistStateChangeListener(this.persistListeners);
        handlerContext.afterPropertiesSet();

        return handlerContext;
    }


    /**
     * 状态注册持久化助手　
     * @param hander
     */
    public void registerPersistHandler(IPersitListener hander){
        this.persistListeners = hander;
    }



    /**
     *注册状态机事件监听器
     * @param stateMachineListener
     */
    public void  registerStateMachineListener(IStateMachineListener stateMachineListener){
            this.stateMachineListener = stateMachineListener;
    }

    /**
     * 注册状态机拦截器
     * @param stateMachineInterceptor
     */
    @Deprecated
    public void registerStateMachineInterceptor(IIntercepter stateMachineInterceptor){
        this.interceptor = stateMachineInterceptor;
    }

    /**
     * 注册状态机仓库
     * @param stateMachineRepository
     */
    public void registerStateMachineRepository(IStateMachineRepository stateMachineRepository){
        this.stateMachineRepository = stateMachineRepository;
    }



    /**
     * 调试打印当前状态机的转移上下文
     */
    public void printTransitionContext(){
        Collection<Transition<String, String>> transitions = this.stateMachine.getTransitions();
        for (Transition transition : transitions){
            System.out.println(transition.getSource().getId() + " ----->   (" +transition.getTrigger().getEvent() + " ) ---->"+ transition.getTarget().getId());
        }
    }

    /**
     *  重新加载构架服务
     * @param taskId
     * @return
     * @throws Exception
     */
    public StateMachineService reload(String taskId) throws Exception {
        Preconditions.checkNotNull(this.type,"type must be init ,Not to null");
        Preconditions.checkNotNull(this.subType,"subType must be init,Not to null");
        stop();
        if(taskId == null ||taskId.equals("")) throw new Exception("taskId is null, this is not accepted !  taskId = " + this.taskId);
        startTask(taskId);
        return this;
    }


    /**
     * 销毁状态机
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        this.handler.destroy();
    }

    /**
     * 停止状态机和持久化操作服务
     */
    public void stop(){
        if(stateMachine != null)
            stateMachine.stop();
        if(handler != null)
            handler.stop();
    }

    /**
     * 修改状态机的执行序列可能是一个恐怖的操作，谨慎使用。
     *
     * 拦截器中实现
     *
     * 如果你有什么更好的意见，请联系我们
     *
     * @param event  事件类型，应该包含的信息（task,TaskEntity）
     * @param state
     */
    public boolean handle(Message<String> event, String state) throws Exception {
        if(event.getHeaders().containsKey(TaskEntity.key) && event.getPayload()!=null && state != null){
            return this.handler.handleEventWithState(event,state);
        }
        return false;
    }

    /**
     * 触发状态的改变
     * @param event  触发事件
     * @param taskEntity   TaskEntity 任务实例
     * @param state 当前状态
     * @return
     */

    public synchronized boolean handle(String event, TaskEntity taskEntity ,String state) throws Exception {
        Preconditions.checkNotNull(taskEntity.getTaskId(),"taskId must be init,Not to null");

        if(taskEntity.getType() == null) taskEntity.setType(this.type);
        else this.type = taskEntity.getType();
        if(taskEntity.getSubtype() == null) taskEntity.setSubtype(this.subType);
        else this.subType = taskEntity.getSubtype();
        this.taskId = taskEntity.getTaskId();
        if(taskEntity.getRole() != null && !taskEntity.getRole().equals(""))
            this.role = taskEntity.getRole();
        if ( !validate(taskEntity)){
            throw  new Exception("权限认证没有通过！！");
        }
        this.reload(this.taskId);
        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        messageHeaderAccessor.setHeader(TaskEntity.key,taskEntity);
        messageHeaderAccessor.setHeader("BusinessType",this.type.toString());
        messageHeaderAccessor.setHeader("subType", this.subType);
        Message eventMsg = MessageBuilder.withPayload(event).setHeaders(messageHeaderAccessor).build();
        return this.handle(eventMsg, state);
    }


    /**
     *
     * @param event
     * @param taskEntity
     * @return
     * @throws Exception
     */
    public  boolean handleEventWithState(String event , TaskEntity taskEntity) throws Exception {
        if( !taskEntity.verifyTask()) return false;
        PersistStateMachineHandler handlerContext  =  this.loadHandlerContext(taskEntity);
        Preconditions.checkNotNull(handlerContext,"handler must Not to be null");
        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        messageHeaderAccessor.setHeader(TaskEntity.key,taskEntity);
        Message eventMsg = MessageBuilder.withPayload(event).setHeaders(messageHeaderAccessor).build();
        boolean result = handlerContext.handleEventWithState(eventMsg, taskEntity.getStatus());
        handlerContext.stop();
        return result;
    }


    /**
     * 权限验证
     * @return
     */
    private boolean validate(TaskEntity taskEntity){
        /**
         * 这里先不使用，避免现有正常业务失败
         */
//        return this.stateMachineRepository.validate(taskEntity);
        return  true;
    }

}
