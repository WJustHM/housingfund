package com.handge.housingfund.statemachine.repository;

import com.google.gson.Gson;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.*;
import com.handge.housingfund.statemachine.actions.Actioner;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.entity.TransEntity;
import com.handge.housingfund.statemachine.entity.TranstitionsContext;
import com.handge.housingfund.statemachine.guards.Guarder;
import com.handge.housingfund.statemachine.service.TaskTransitionContextService;
import com.handge.housingfund.statemachine.trigger.IStateMachineRepository;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xuefei on 17-7-9.
 */
@Service
public class StateMachineRepository extends TaskTransitionContextService implements IStateMachineRepository {

	public StateMachineRepository() throws ConfigurationException {
	}

	/**
	 *
	 * @param role
	 * @param type
	 * @param subType
	 * @return
	 */
	public List<CStateMachineConfiguration> listTransitionContext(String taskId, String role, BusinessType type, String subType) {
		HashMap<String, Object> filter = new HashMap();
		filter.put("type", type);
		filter.put("subType", subType);
		List<CStateMachineConfiguration> transContexts = icStateMachineConfigurationDAO.list(filter, null, null, null,
				Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED);
		saveTransitionContext(taskId,transContexts);
		return transContexts.stream()
				.filter(cStateMachineConfiguration ->
						Arrays.stream(cStateMachineConfiguration.getRole().split(","))
								.anyMatch(s -> s.trim().matches(role.trim())))
				.distinct()
				.collect(Collectors.toList());
	}


	/**
     *
     * @param type 业务类型
     * @param subType　业务子类型
     * @return
     * @throws Exception
     */
    public  StateMachine<String,String> createStateMachine(String taskId, BusinessType type, String subType, String role) throws Exception {
        StateMachineBuilder.Builder<String,String> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .beanFactory(new StaticListableBeanFactory());

		List<CStateMachineConfiguration> transContexts = listTransitionContext(taskId, role, type,subType);
       return createStateMachine(transContexts);
    }

	/**
	 * 业务流水号存在时候，使用根据业务流水号存再数据库中的流程创建状态机
	 * 
	 * @param taskId
	 *            业务流水号
	 * @return
	 */
	public StateMachine<String, String> loadStateMachine(String taskId, String role) {
		 if (!taskTransitionContextExist(taskId)){
		 	return null;
		 }
		StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
		HashSet<String> states = new HashSet<String>();
		try {
			builder.configureConfiguration().withConfiguration().beanFactory(new StaticListableBeanFactory());

            List<TransEntity> collection = getTaskTransitionContext(taskId, role);
			for (TransEntity context : collection) {
				if (context.getSource() != null) {
					states.add(context.getSource());
					if(!states.contains(context.getTarget())) states.add(context.getTarget());
				}
				if (TransitionKind.valueOf(context.getTransitionKind().toString()) == TransitionKind.EXTERNAL) {
					builder.configureTransitions().withExternal().source(context.getSource())
							.target(context.getTarget()).event(context.getEvent())
							.action(new Actioner()).guard(new Guarder());
				}
				if (TransitionKind.valueOf(context.getTransitionKind().toString()) == TransitionKind.INTERNAL) {
					builder.configureTransitions().withInternal().source(context.getSource())
							.event(context.getEvent()).action(new Actioner()).guard(new Guarder());
				}
				if (TransitionKind.valueOf(context.getTransitionKind().toString()) == TransitionKind.LOCAL) {
					builder.configureTransitions().withLocal().source(context.getSource())
							.target(context.getTarget()).event(context.getEvent())
							.action(new Actioner()).guard(new Guarder());
				}
			}
			builder.configureStates().withStates().initial(StateConstants.initialState.getId())
					.end(StateConstants.endState.getId()).states(states);
		} catch (Exception e) {
			System.out.println(">>>>>>class:StateMachineRepository, method:loadStateMachine ---> Exception: " + e.getMessage());
		}
		return builder.build();
	}


    /**
     * 检测对应业务流水号的状态机流程上下文是否存在
     * @param taskId 业务流吹号
     * @return
     */
	public boolean exist(String taskId) {
		return taskTransitionContextExist(taskId);
	}

	public void save(String taskId, String role, StateMachine<String, String> stateMachine) {
        saveTransitionContext(taskId, role, stateMachine.getTransitions());
	}

	@Override
	public StateMachine<String, String> loadOrCreateStateMachine(TaskEntity taskEntity) throws Exception  {
        List<CStateMachineConfiguration> configs = new ArrayList<>();
        if(!checkIsLoad(taskEntity, configs)){
            configs = listTransitionContext(taskEntity.getTaskId(), taskEntity.getRole(), taskEntity.getType(), taskEntity.getSubtype())
                    .stream()
                    .filter(cStateMachineConfiguration ->
                            Arrays.stream(cStateMachineConfiguration.getRole().split(",")).anyMatch(s -> s.equals(taskEntity.getRole()))
                                    && cStateMachineConfiguration.getWorkstation().equals(taskEntity.getWorkstation()))
                    .collect(Collectors.toList());
            System.out.println("loadOrCreateStateMachine ===>    list : ");
            configs.stream().forEach(System.out::println);
        }
        if(configs.size() == 0 )
            throw new Exception("没有该业务或对应业务网点或角色的操作流程！");
        else return createStateMachine(configs);
	}

    public boolean validate(TaskEntity taskEntity){
        final boolean[] flag = {false, false}; // flag标识该业务TaskEntity 对应的状态机流程是否存在
        final TranstitionsContext[] transtitionsContext = {new TranstitionsContext()};
        icBusinessStateTransformContextDAO.list(new HashMap<String, Object>(){{
            this.put("taskId", taskEntity.getTaskId());
        }}, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED)
                .stream()
                .map(cBusinessStateTransformContext -> {
                    flag[0] = true;
                    return transtitionsContext[0] = new Gson().fromJson(cBusinessStateTransformContext.getContext(), TranstitionsContext.class);
                }).forEach(transContext -> {
                    transContext.getTranstionEntitys()
                            .stream()
                            .filter(transEntity -> transEntity.getRole().contains(taskEntity.getRole())
                                    && transEntity.getWorkstation().equals(taskEntity.getWorkstation()))
                            .forEach(transEntity -> {
                                flag[1] = true;
                            });
        });
        long count = icStateMachineConfigurationDAO.count(new HashMap<String, Object>(){{
            this.put("type", taskEntity.getType());
            this.put("subType", taskEntity.getSubtype());
            this.put("role",taskEntity.getRole());
            this.put("workstation", taskEntity.getWorkstation());
        }}, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED);
        if((flag[0] && flag[1]) || count != 0) return true;
        return false;
    }

	private boolean checkIsLoad(TaskEntity taskEntity,  List<CStateMachineConfiguration> transContexts ) throws Exception {
        final boolean[] flag = {false, false}; // flag标识该业务TaskEntity 对应的状态机流程是否存在
        final TranstitionsContext[] transtitionsContext = {new TranstitionsContext()};
        icBusinessStateTransformContextDAO.list(new HashMap<String, Object>(){{
            this.put("taskId", taskEntity.getTaskId());
        }}, null, null, null, null, ListDeleted.NOTDELETED, SearchOption.REFINED)
                .stream()
                .map(cBusinessStateTransformContext -> {
                    flag[0] = true;
                    return transtitionsContext[0] = new Gson().fromJson(cBusinessStateTransformContext.getContext(), TranstitionsContext.class);
                }).forEach(transContext -> {
            transContext.getTranstionEntitys()
                    .stream()
                    .filter(transEntity -> transEntity.getRole().contains(taskEntity.getRole())
                            && transEntity.getWorkstation().equals(taskEntity.getWorkstation()))
                    .forEach(transEntity -> {
                        transContexts.add(convertToCStateMachineConfiguration(transEntity));
                        flag[1] = true;
                    });
        });
        if(flag[0] && flag[1]) return true;
        else if(flag[0] && !flag[1]) {
            throw new Exception("该业务网点或操作员角色没有权限进行操作！");
        }
        return false;
    }

    private StateMachine<String, String> createStateMachine(List<CStateMachineConfiguration> transContexts) throws Exception{
        StateMachineBuilder.Builder<String,String> builder = StateMachineBuilder.builder();
        builder.configureConfiguration()
                .withConfiguration()
                .beanFactory(new StaticListableBeanFactory());
        HashSet<String> states = new HashSet<String>();
        transContexts.forEach(transContext -> {
            try {
                if (transContext.getSource() != null) {
                    states.add(transContext.getSource());
                    if(!states.contains(transContext.getTarget())) states.add(transContext.getTarget());
                }
                if (transContext.getTransitionKind() == TransitionKind.EXTERNAL){
                    builder.configureTransitions()
                            .withExternal()
                            .source(transContext.getSource())
                            .target(transContext.getTarget())
                            .event(transContext.getEvent())
                            .action(new Actioner())
                            .guard(new Guarder());
                }
                if (transContext.getTransitionKind() == TransitionKind.INTERNAL){
                    builder.configureTransitions()
                            .withInternal()
                            .source(transContext.getSource())
                            .event(transContext.getEvent())
                            .action(new Actioner())
                            .guard(new Guarder());
                }
                if (transContext.getTransitionKind() == TransitionKind.LOCAL){
                    builder.configureTransitions()
                            .withLocal()
                            .source(transContext.getSource())
                            .target(transContext.getTarget())
                            .event(transContext.getEvent())
                            .action(new Actioner())
                            .guard(new Guarder());
                }
            }catch (Exception e) {
                System.out.println("异常： " + e );
            }
            if ( !transContext.isFlag()){
                return;
            }

        });
        builder.configureStates()
                .withStates()
                .initial(StateConstants.initialState.getId())
                .state(StateConstants.initialState.getId())
                .end(StateConstants.endState.getId())
                .states(states);
        return builder.build();
    }

    private CStateMachineConfiguration convertToCStateMachineConfiguration(TransEntity transEntity){
        CStateMachineConfiguration cStateMachineConfiguration = new CStateMachineConfiguration();
        cStateMachineConfiguration.setWorkstation(transEntity.getWorkstation());
        cStateMachineConfiguration.setSource(transEntity.getSource());
        cStateMachineConfiguration.setTarget(transEntity.getTarget());
        cStateMachineConfiguration.setEvent(transEntity.getEvent());
        cStateMachineConfiguration.setRole(String.join(",",transEntity.getRole()));
        cStateMachineConfiguration.setTransitionKind(TransitionKind.valueOf(transEntity.getTransitionKind().toString()));
        return cStateMachineConfiguration;
    }

}

