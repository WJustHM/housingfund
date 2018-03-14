package com.handge.housingfund.statemachine.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.handge.housingfund.database.dao.ICBusinessStateTransformContextDAO;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.entities.CBusinessStateTransformContext;
import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.enums.ListDeleted;
import com.handge.housingfund.database.enums.Order;
import com.handge.housingfund.database.enums.SearchOption;
import com.handge.housingfund.statemachine.entity.TransEntity;
import com.handge.housingfund.statemachine.entity.TranstitionsContext;
import com.handge.housingfund.statemachine.guards.Guarder;
import com.handge.housingfund.statemachine.repository.StateConstants;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.state.ObjectState;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.*;
import org.springframework.statemachine.trigger.EventTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Created by xuefei_wang on 17-7-12.
 */
@Service
public class TaskTransitionContextService implements ITaskTransitionContextService {

	static Cache<String, Collection<Transition<String, String>>> cache = CacheBuilder.newBuilder().initialCapacity(1000)
			.build();

	private static TaskTransitionContextService instance = null;
	@Autowired
	protected ICBusinessStateTransformContextDAO icBusinessStateTransformContextDAO;

	@Autowired
    protected ICStateMachineConfigurationDAO icStateMachineConfigurationDAO;

	protected TaskTransitionContextService() throws ConfigurationException {

	}

	/**
	 * 单利实现，获取该类的实例
	 * 
	 * @return
	 * @throws ConfigurationException
	 */
	public static TaskTransitionContextService getInstance() {
		if (instance == null) {
			synchronized (TaskTransitionContextService.class) {
				if (null == instance) {
					try {
						instance = new TaskTransitionContextService();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		}
		return instance;
	}

	/**
	 * 获取该笔业务之前数据库中存在的执行流程上下文信息
	 * 
	 * @param taskId
	 *            业务流水号
	 * @return Collection<Transition<String, String>>
	 */
	@Override
	public List<TransEntity> getTaskTransitionContext(String taskId, String role) {
		HashMap<String, Object> filterMap = new HashMap<>();
		filterMap.put("taskId", taskId);
		List<CBusinessStateTransformContext> transList = icBusinessStateTransformContextDAO.list(filterMap, null, null, null, null,
				ListDeleted.NOTDELETED, SearchOption.REFINED);
        final TranstitionsContext[] transtitionsContext = {new TranstitionsContext()};
        if (transList.size() == 1) {
			transList.forEach( cBusinessStateTransformContext -> transtitionsContext[0] = new Gson().fromJson(cBusinessStateTransformContext.getContext(), TranstitionsContext.class));
        }
		return transtitionsContext[0].getTranstionEntitys()
                .stream()
//                .filter(transEntity -> transEntity.getRole().contains(role))
                .collect(Collectors.toList());
	}

	@Override
	public Collection<Transition<String, String>> getIfPresent(final String taskId, final String role) throws ExecutionException {
		return cache.get(taskId, new Callable<Collection<Transition<String, String>>>() {
			@Override
			public Collection<Transition<String, String>> call() throws Exception {
				// from database get context by taskId
				HashMap<String, Object> filterMap = new HashMap<String, Object>();
				filterMap.put("taskId", taskId);
				List<CBusinessStateTransformContext> trans = icBusinessStateTransformContextDAO.list(filterMap, null, null, null, null,
						null, null);
				final TranstitionsContext[] transtitionsContext = {new TranstitionsContext()};
				if (trans.size() == 1) {
                    trans.forEach( cBusinessStateTransformContext -> transtitionsContext[0] = new Gson().fromJson(cBusinessStateTransformContext.getContext(), TranstitionsContext.class));
                }
				return convertToTrans(transtitionsContext[0], role);
			}
		});
	}

	/**
	 * 检查该笔业务是否存在上下文
	 * 
	 * @param taskId
	 *            业务流水号
	 * @return true/false
	 */
	@Override
	public boolean taskTransitionContextExist(String taskId) {
		HashMap<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("taskId", taskId);
		long count = icBusinessStateTransformContextDAO.count(filterMap, null, null, null, null, ListDeleted.NOTDELETED,
				SearchOption.REFINED);
		if (count != 0)
			return true;
		else
			return false;
	}

	/**
	 * 保存业务的状态上下文到数据库
	 * 
	 * @param taskId
	 *            业务流水号
	 * @param transitions
	 *            当前业务状态机的执行流程上下文
	 */
	@Override
	public void saveTransitionContext(String taskId, String role,
                                      Collection<Transition<String, String>> transitions) {
		CBusinessStateTransformContext context = new CBusinessStateTransformContext();
		final TranstitionsContext transtitionsContext = new TranstitionsContext();
		transtitionsContext.setTaskId(taskId);

//		transitions.stream().forEach( transition ->
//				transtitionsContext.addEntity(new TransEntity(transition.getKind(), transition.getSource().getId(),
//						transition.getTarget().getId(), transition.getTrigger().getEvent()), null, null));
		context.setCreated_at(new Date());
		context.setTaskId(taskId);
		context.setContext(new Gson().toJson(transtitionsContext));
        icBusinessStateTransformContextDAO.save(context);
	}

	/**
	 *
	 * @param taskId
	 * @param configurations
	 */
	@Override
	public void saveTransitionContext(String taskId, List<CStateMachineConfiguration> configurations){
		CBusinessStateTransformContext context = new CBusinessStateTransformContext();
		final TranstitionsContext transtitionsContext = new TranstitionsContext();
		transtitionsContext.setTaskId(taskId);

		configurations.stream().forEach( configuration ->
            transtitionsContext.addEntity(new TransEntity(TransitionKind.valueOf(configuration.getTransitionKind().toString()),
                    configuration.getSource(), configuration.getTarget(), configuration.getEvent(), Arrays.asList(configuration.getRole().split(",")),configuration.getWorkstation())));
        context.setCreated_at(new Date());
		context.setTaskId(taskId);
		context.setContext(new Gson().toJson(transtitionsContext));
		icBusinessStateTransformContextDAO.save(context);
	}



	/**
	 * 根据业务流水号，获取初始化的状态配置信息
	 * 
	 * @param taskId
	 *            业务流水号
	 * @return
	 */
	@Deprecated
	public Collection<String> getStates(String taskId) {

		HashSet<String> states = new HashSet<String>();
		List<CBusinessStateTransformContext> trans = icBusinessStateTransformContextDAO.list(new HashMap<String, Object>(){{
		    this.put("taskId", taskId);}}, null, null, null, null, null, null);
        final TranstitionsContext[] transtitionsContext = {new TranstitionsContext()};
		if (trans.size() == 1) {
		    trans.forEach( cBusinessStateTransformContext -> transtitionsContext[0] = new Gson().fromJson(cBusinessStateTransformContext.getContext(), TranstitionsContext.class));
		}
		return states;
	}

    private Collection<Transition<String, String>> convertToTrans(TranstitionsContext transtitionsContextEntity, String role) {
        ArrayList<TransEntity> entitys = transtitionsContextEntity.getTranstionEntitys();
        HashSet<Transition<String, String>> transitions = new HashSet<Transition<String, String>>();
        entitys.stream()
				.filter(transEntity -> transEntity.getRole().stream()
						.anyMatch(s -> s.trim().matches(role.trim())))
				.distinct()
				.forEach(transEntity ->
						trans(transitions, new ObjectState<String, String>(transEntity.getSource()),
								new ObjectState<String, String>(transEntity.getTarget()), transEntity.getEvent(),
								transEntity.getTransitionKind()));
        return transitions;
    }

    private Collection<Transition<String, String>> getTransFromConfig(BusinessType type, String subType) {
        List<CStateMachineConfiguration> transContexts = icStateMachineConfigurationDAO.list(new HashMap<String, Object>(){{
            this.put("type", type);
            this.put("subType", subType);
        }}, null, null, null,
                Order.ASC, ListDeleted.NOTDELETED, SearchOption.REFINED);
        HashSet<Transition<String, String>> transitions = new HashSet<Transition<String, String>>();
        transContexts.forEach(stconfig ->
                trans(transitions, new ObjectState<String, String>(stconfig.getSource()),
                        new ObjectState<String, String>(stconfig.getTarget()), stconfig.getEvent(),
                        TransitionKind.valueOf(stconfig.getTransitionKind().toString())));
        return transitions;
    }

    private Collection<Transition<String, String>> trans(Collection<Transition<String, String>> transitions,
                                                         State<String, String> source, State<String, String> target, String event, TransitionKind transType) {
        switch (transType) {
            case LOCAL:
                transitions.add(new DefaultLocalTransition<String, String>(source, target, null, event, null,
                        new EventTrigger<String, String>(event)));
                break;
            case INITIAL:
                transitions.add(new InitialTransition<String, String>(StateConstants.initialState));
                break;
            case INTERNAL:
                transitions.add(new DefaultInternalTransition<String, String>(source, null, event, new Guarder(),
                        new EventTrigger<String, String>(event)));
                break;
            default:
                transitions.add(new DefaultExternalTransition<String, String>(source, target, null, event, new Guarder(),
                        new EventTrigger<String, String>(event)));
				break;
        }
        return transitions;
    }


}
