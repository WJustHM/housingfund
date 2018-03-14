package com.handge.housingfund.common.service.others;

import com.handge.housingfund.common.service.others.model.Action;

import java.util.HashSet;
import java.util.List;

public interface IActionService {
	public void update(List<Action> actions);

	public HashSet<String> getActionKeywords();

	public boolean compareActionMD5(String actionMD5);

	public void updateActionCode();

	public List<Action> getActionListByUser(String userID);

	public HashSet<String> getActionCodesByUser(String userID);

	public HashSet<String> getActionCodesByRole(String roleID);
}
