package com.handge.housingfund.statemachineV2.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ReturnEnumeration;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CBusinessStateTransformContext;
import com.handge.housingfund.database.entities.CLoanHousingBusinessProcess;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.statemachine.actions.Actioner;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.entity.TransEntity;
import com.handge.housingfund.statemachine.guards.Guarder;
import com.handge.housingfund.statemachine.repository.StateConstants;
import com.handge.housingfund.statemachineV2.IStateMachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by xuefei_wang on 17-8-10.
 */
@SuppressWarnings("Duplicates")
@Service
public class DefaultStateMachineRepository implements IStateMachineRepository {
    final private Gson gson = new Gson();
    final private Type collectionType = new TypeToken<Collection<TransEntity>>() {
    }.getType();

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

    public DefaultStateMachineRepository() {
    }

    @Override
    public void store(String taskId, Collection<TransEntity> transEntities) {
        CBusinessStateTransformContext transformContext = new CBusinessStateTransformContext();
        transformContext.setTaskId(taskId);
        transformContext.setContext(gson.toJson(transEntities, collectionType));
        icStateMachineConfigurationDAO.save(transformContext);
    }

    @Override
    public StateMachine<String, String> loadOrCreateStateMachine(final TaskEntity taskEntity, String event) {
        StateMachine<String, String> stateMachine = null;

        List<CBusinessStateTransformContext> stcs = icBusinessStateTransformContextDAO.list(new HashMap<String, Object>() {{
                                                                                                this.put("taskId", taskEntity.getTaskId());
                                                                                            }}, null, null, null, null, ListDeleted.NOTDELETED,
                SearchOption.REFINED);

        if (stcs.size() > 1) {
            throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, taskEntity.getTaskId());
        }
        if (stcs.size() == 1) {
            String transContext = stcs.get(0).getContext();
            Collection<TransEntity> transEntities = gson.fromJson(transContext, collectionType);
            boolean access = authorization(transEntities, taskEntity, event);
            if (!access) {
                throw new ErrorException(ReturnEnumeration.Authentication_Failed);
            }
            stateMachine = loadFromTrans(transEntities);
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

            boolean access = authorization(transEntities, taskEntity, event);
            if (!access) {
                throw new ErrorException(ReturnEnumeration.Authentication_Failed);
            }

            stateMachine = loadFromTrans(transEntities);
            store(taskEntity.getTaskId(), transEntities);
        }
        return stateMachine;
    }

    public String correctWorkStation(BusinessType type, String subType, String YWLSH, String workstation) {

        String ywwd = null;

        try {
            if (type.equals(BusinessType.Collection)) {

                if (subType.startsWith("个人") || subType.startsWith("提取") ) {

                    ywwd = personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : personalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();

                } else if (subType.startsWith("单位") || subType.startsWith("缴存") || subType.startsWith("转出") || subType.startsWith("转入")) {

                    ywwd = unitBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : unitBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();
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
                    ywwd = obj == null ? workstation : obj.getYwwd().getId();
                } else {
                    throw new ErrorException(ReturnEnumeration.StateMachineConfig_Unknow_Error, "业务子类型出错，请联系管理员");
                }
            }
//            else if (type.equals(BusinessType.WithDrawl)) {
//
//                if (subType.startsWith("提取")) {
//
//                    ywwd = personalBusinessDetailsDAO.getByYwlsh(YWLSH) == null ? workstation : personalBusinessDetailsDAO.getByYwlsh(YWLSH).getExtension().getYwwd().getId();
//                    if (BusinessSubType.归集_提取.getSubType().equals(subType)) {
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

            throw new ErrorException(e);
        }
    }


    /**
     * create statemachine from transentity
     *
     * @param transEntities
     * @return
     */
    private StateMachine<String, String> loadFromTrans(Collection<TransEntity> transEntities) {

        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
        try {
            builder.configureConfiguration()
                    .withConfiguration()
                    .beanFactory(new StaticListableBeanFactory());
            final String[] msgs = {null};
            HashSet<String> states = new HashSet<String>();
            transEntities.forEach(transContext -> {
                try {
                    if (transContext.getSource() != null) {
                        states.add(transContext.getSource());
                        if (!states.contains(transContext.getTarget())) states.add(transContext.getTarget());
                    }
                    if (transContext.getTransitionKind() == TransitionKind.EXTERNAL) {
                        builder.configureTransitions()
                                .withExternal()
                                .source(transContext.getSource())
                                .target(transContext.getTarget())
                                .event(transContext.getEvent())
                                .action(new Actioner())
                                .guard(new Guarder());
                    }
                    if (transContext.getTransitionKind() == TransitionKind.INTERNAL) {
                        builder.configureTransitions()
                                .withInternal()
                                .source(transContext.getSource())
                                .event(transContext.getEvent())
                                .action(new Actioner())
                                .guard(new Guarder());
                    }
                    if (transContext.getTransitionKind() == TransitionKind.LOCAL) {
                        builder.configureTransitions()
                                .withLocal()
                                .source(transContext.getSource())
                                .target(transContext.getTarget())
                                .event(transContext.getEvent())
                                .action(new Actioner())
                                .guard(new Guarder());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msgs[0] = e.getMessage();
                }
            });
            if (msgs[0] != null) throw new Exception(" 初始化状态机流程出现异常！" + msgs[0]);
            builder.configureStates()
                    .withStates()
                    .initial(StateConstants.initialState.getId())
                    .state(StateConstants.initialState.getId())
                    .end(StateConstants.endState.getId())
                    .states(states);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("！>>> 错误信息： loadFromTrans =>" + e);
        }
        return builder.build();
    }

    /**
     * 判断是否具有权限执行下面的操作
     *
     * @param transEntities
     * @param taskEntity
     * @throws Exception
     */
    @Deprecated
    private void checkPower(Collection<TransEntity> transEntities, TaskEntity taskEntity) throws Exception {
        boolean pass = false;
        for (TransEntity transEntity : transEntities) {
            if (taskEntity.getStatus().equals(transEntity.getSource()) &&
                    transEntity.getRole().contains(taskEntity.getRole())) {
                pass = true;
            }
        }
        if (!pass) {
            throw new Exception("状态机权限不通过:" + transEntities + " /n  -->  " + taskEntity);
        }
    }

    /**
     * @param transEntities
     * @param taskEntity
     * @return
     */
    private boolean authorization(Collection<TransEntity> transEntities, TaskEntity taskEntity, String event) {
        for (TransEntity transEntity : transEntities) {
            HashSet<String> roleSet = new HashSet<>(transEntity.getRole());
            if (taskEntity.getStatus().equals(transEntity.getSource()) && event.equals(transEntity.getEvent())) {
                roleSet.retainAll(taskEntity.getRoleSet());
                if (roleSet.size() > 0) return true;
            }
        }
        return false;
    }

}
