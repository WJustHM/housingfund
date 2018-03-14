package com.handge.housingfund.statemachineV2.service;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CBusinessStateTransformContext;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.entity.TransEntity;
import com.handge.housingfund.statemachineV2.IStateMachineRepository;
import com.handge.housingfund.statemachineV2.IStateMachineService;
import com.handge.housingfund.statemachineV2.handler.DefaultPersistStateHandler;
import com.handge.housingfund.statemachineV2.handler.DefaultStateMachineInterceptor;
import com.handge.housingfund.statemachineV2.handler.DefaultStateMachineListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xuefei_wang on 17-8-10.
 */
@SuppressWarnings("Duplicates")
@Component(value="stateMachineServiceV2")
@Scope("prototype")
public class StateMachineService implements IStateMachineService {

    private PersistStateChangeListener persistStateChangeListener;

    private StateMachineListener stateMachineListener;

    @Autowired
    private IStateMachineRepository stateMachineRepository;
    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO personalBusinessDetailsDAO;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO unitBusinessDetailsDAO;

    @Autowired
    private ICLoanHousingBusinessProcessDAO loanHousingBusinessProcessDAO;

    @Autowired
    protected ICBusinessStateTransformContextDAO icBusinessStateTransformContextDAO;

    @Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;
    @Autowired
    @Resource(name = "defaultStateMachineInterceptor")
    private StateMachineInterceptor stateMachineInterceptor;

    final private Gson gson = new Gson();
    final private Type collectionType = new TypeToken<Collection<TransEntity>>() {
    }.getType();
    public StateMachineService() {
    }

    @Override
    public void registerPersistHandler(PersistStateChangeListener persistStateChangeListener) {
        this.persistStateChangeListener = persistStateChangeListener;
    }

    @Override
    public void registerStateMachineListener(StateMachineListenerAdapter stateMachineListener) {
        this.stateMachineListener = stateMachineListener;
    }

    @Override
    public void registerStateMachineInterceptor(StateMachineInterceptor stateMachineInterceptor) {
        this.stateMachineInterceptor = stateMachineInterceptor;
    }

    @Override
    public void registerStateMachineRepository(IStateMachineRepository stateMachineRepository) {
        this.stateMachineRepository = stateMachineRepository;
    }

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public boolean handle(String event, TaskEntity taskEntity) {
        if( !taskEntity.verifyTask()) {
            throw  new ErrorException(ReturnEnumeration.Authentication_Failed,"请确保业务参数的有效性"/* + taskEntity*/);
        };
        PersistStateMachineHandler handlerContext  =  this.loadHandlerContext(taskEntity,event);
        Preconditions.checkNotNull(handlerContext,"handler must Not to be null");
        MessageHeaderAccessor messageHeaderAccessor = new MessageHeaderAccessor();
        messageHeaderAccessor.setHeader(TaskEntity.key,taskEntity);
        Message eventMsg = MessageBuilder.withPayload(event).setHeaders(messageHeaderAccessor).build();
        boolean result = handlerContext.handleEventWithState(eventMsg, taskEntity.getStatus());
        handlerContext.stop();
        return result;
    }

    @Override
    public  boolean handle(String event, TaskEntity taskEntity ,String state) throws Exception {
        taskEntity.setStatus(state);
        return  handle(event,taskEntity);
    }

    @Override
    public boolean checkpermission(TaskEntity taskEntity) {
        boolean access = false;
        List<CBusinessStateTransformContext> stcs = icBusinessStateTransformContextDAO.list(new HashMap<String, Object>() {{
                                                                                                this.put("taskId", taskEntity.getTaskId());
                                                                                            }}, null, null, null, null, ListDeleted.NOTDELETED,
                SearchOption.REFINED);

        if (stcs.size() > 1) {
//            throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, taskEntity.getTaskId());
            return access;
        }
        if (stcs.size() == 1) {
            String transContext = stcs.get(0).getContext();
            Collection<TransEntity> transEntities = gson.fromJson(transContext, collectionType);
             access = authorization(transEntities, taskEntity);
           return access;

        } else {
            HashMap<String, Object> filter = new HashMap();
            filter.put("type", taskEntity.getType());
            filter.put("subType", taskEntity.getSubtype());
            String workstation = taskEntity.getWorkstation();
            if ("1".equals(taskEntity.getWorkstation()) && !BusinessType.Finance.equals(taskEntity.getType())) {
                workstation = correctWorkStation(taskEntity.getType(), taskEntity.getSubtype(), taskEntity.getTaskId(), workstation);
            }
            filter.put("workstation", workstation);
            filter.put("flag", true);
            List<CStateMachineConfiguration> transContexts = icStateMachineConfigurationDAO.list(filter, null, null, null,
                    Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED);

            Collection<TransEntity> transEntities = transContexts.stream().map(new Function<CStateMachineConfiguration, TransEntity>() {
                @Override
                public TransEntity apply(CStateMachineConfiguration cStateMachineConfiguration) {
                    TransEntity transEntity = new TransEntity();
                    transEntity.setEvent(cStateMachineConfiguration.getEvent());
                    transEntity.setRole(Arrays.asList(cStateMachineConfiguration.getRole().split(",")));
                    transEntity.setSource(cStateMachineConfiguration.getSource());
                    transEntity.setTarget(cStateMachineConfiguration.getTarget());
                    transEntity.setTransitionKind(TransitionKind.valueOf(cStateMachineConfiguration.getTransitionKind().toString()));
                    transEntity.setWorkstation(cStateMachineConfiguration.getWorkstation());
                    return transEntity;
                }
            }).distinct().collect(Collectors.toSet());

            access = authorization(transEntities, taskEntity);
        }
        return access;
    }

    public String correctWorkStation(BusinessType type, String subType, String YWLSH,String workstation) {

        String ywwd = null;

        try {
            if (type.equals(BusinessType.Collection)) {

                if (subType.startsWith("个人")) {

                    ywwd =  personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : personalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();
                    if(BusinessSubType.归集_个人账户信息变更.getSubType().equals(subType)){
                        ywwd = personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : personalBusinessDetailsDAO.getByYwlsh(YWLSH).getUnit().getExtension().getKhwd();
                    }
                } else if (subType.startsWith("提取") || subType.startsWith("单位") || subType.startsWith("缴存") || subType.startsWith("转出") || subType.startsWith("转入")) {

                    ywwd =  unitBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation :unitBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();
                } else {

                    throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "业务子类型出错，请联系管理员");
                }
            } else if (type.equals(BusinessType.Loan)) {

                if (subType.startsWith("房开") || subType.startsWith("楼盘") || subType.startsWith("合同") || subType.startsWith("贷款")) {

                    CLoanHousingBusinessProcess obj = loanHousingBusinessProcessDAO.list(new HashMap<String, Object>() {
                        {
                            this.put("ywlsh", YWLSH);
                        }
                    }, null, null, null, null, null, null).get(0);
                    ywwd =obj == null ?workstation : obj .getYwwd().getId() ;
                } else {
                    throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "业务子类型出错，请联系管理员");
                }
            }
//            else if (type.equals(BusinessType.WithDrawl)) {
//
//                if (subType.startsWith("提取")) {
//
//                    ywwd = personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation :personalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();
//                    if(BusinessSubType.归集_提取.getSubType().equals(subType)){
//                        ywwd = personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : personalBusinessDetailsDAO.getByYwlsh(YWLSH).getUnit().getExtension().getKhwd();
//                    }
//                } else {
//
//                    throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "业务子类型出错，请联系管理员");
//                }
//            }

            if (ywwd == null) {

                throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "业务类型或子类型出错，请联系管理员");
            } else {

                return ywwd;
            }
        } catch (Exception e) {

            throw new  ErrorException(e);
        }
    }

    private boolean authorization(Collection<TransEntity> transEntities, TaskEntity taskEntity ) {
        for (TransEntity transEntity : transEntities) {
            HashSet<String> roleSet = new HashSet<>(transEntity.getRole());
            if (taskEntity.getStatus().equals(transEntity.getSource()) && transEntity.getEvent().equals(Events.通过.getEvent())) {
                roleSet.retainAll(taskEntity.getRoleSet());
                if (roleSet.size() > 0) return true;
            }
        }
        return false;
    }

    private PersistStateMachineHandler loadHandlerContext(TaskEntity taskEntity,String event) {
        if(this.stateMachineRepository == null){
            this.stateMachineRepository = new DefaultStateMachineRepository();
        }
        StateMachine<String, String> stateMachine = this.stateMachineRepository.loadOrCreateStateMachine(taskEntity ,event);
        printTransitionContext(stateMachine);
        if(this.stateMachineInterceptor == null){
            this.stateMachineInterceptor = new DefaultStateMachineInterceptor();
        }
        stateMachine.getStateMachineAccessor()
                .withRegion()
                .addStateMachineInterceptor(this.stateMachineInterceptor);
        if(this.stateMachineListener == null){
            this.stateMachineListener  = new DefaultStateMachineListener();
        }
        stateMachine.addStateListener(this.stateMachineListener);

        if (this.persistStateChangeListener ==null){
            this.persistStateChangeListener = new DefaultPersistStateHandler();
        }
        /**
         * this handlerContext is local var
         */
        PersistStateMachineHandler handlerContext = new PersistStateMachineHandler(stateMachine);
        handlerContext.addPersistStateChangeListener(this.persistStateChangeListener);
        handlerContext.afterPropertiesSet();
        return handlerContext;
    }

    /**
     * 调试打印当前状态机的转移上下文
     */
    public void printTransitionContext(StateMachine stateMachine){
        Collection<Transition<String, String>> transitions = stateMachine.getTransitions();
        for (Transition transition : transitions){
            System.out.println(transition.getSource().getId() + " ----->   (" +transition.getTrigger().getEvent() + " ) ---->"+ transition.getTarget().getId());
        }
    }
}
