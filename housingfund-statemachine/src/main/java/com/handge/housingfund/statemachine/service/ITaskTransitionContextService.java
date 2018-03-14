package com.handge.housingfund.statemachine.service;

import com.handge.housingfund.database.entities.CStateMachineConfiguration;
import com.handge.housingfund.statemachine.entity.TransEntity;
import org.springframework.statemachine.transition.Transition;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by xuefei_wang on 17-7-13.
 */
public interface ITaskTransitionContextService {

//	ObjectMapper mapper = new ObjectMapper();
	/**
	 * @param taskId
	 *            业务流水号
	 * @return
	 */
	 List<TransEntity> getTaskTransitionContext(String taskId, String role);

	/**
	 * if has key ,return value ; else return null
	 *
	 * @param taskId
	 * @return
	 */
	 Collection<Transition<String, String>> getIfPresent(String taskId, String role) throws ExecutionException;

	/**
	 * @param taskId
	 *            业务流水号
	 * @return
	 */
     boolean taskTransitionContextExist(String taskId);

	/**
	 * @param taskId
	 * @param transitions
	 */
	void saveTransitionContext(String taskId, String role,
			Collection<Transition<String, String>> transitions);

	void saveTransitionContext(String taskId, List<CStateMachineConfiguration> configurations);

}
