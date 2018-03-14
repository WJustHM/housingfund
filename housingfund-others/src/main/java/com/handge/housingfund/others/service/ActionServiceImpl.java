package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.others.IActionService;
import com.handge.housingfund.common.service.others.model.Action;
import com.handge.housingfund.common.service.util.ActionUtil;
import com.handge.housingfund.database.dao.ICActionDAO;
import com.handge.housingfund.database.dao.ICAuthDAO;
import com.handge.housingfund.database.dao.ICRoleDAO;
import com.handge.housingfund.database.entities.CAction;
import com.handge.housingfund.database.entities.CAuth;
import com.handge.housingfund.database.entities.CPermission;
import com.handge.housingfund.database.entities.CRole;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ActionServiceImpl implements IActionService {

	@Autowired
	ICActionDAO actionDAO;
	@Autowired
	ICAuthDAO authDAO;
	@Autowired
	ICRoleDAO roleDAO;

	public void update(List<Action> actions) {
		List<CAction> cActions = actionDAO.list(null, null, null, null, null, null, null);
		for (CAction cAction : cActions) {
			boolean deleteFlag = true;
			for (Action action : actions) {
				if (cAction.getAction_method().equals(action.getAction_method())
						&& cAction.getAction_url().equals(action.getAction_url())) {
					deleteFlag = false;
				}
			}
			if (deleteFlag) {
				actionDAO.delete(cAction);
			}
		}
		for (Action action : actions) {
			boolean addFlag = true;
			for (CAction cAction : cActions) {
				if (action.getAction_method().equals(cAction.getAction_method())
						&& action.getAction_url().equals(cAction.getAction_url())) {
					addFlag = false;
				}
			}
			if (addFlag) {
				CAction newAction = new CAction();
				newAction.setAction_method(action.getAction_method());
				newAction.setAction_url(action.getAction_url());
				actionDAO.save(newAction);
			}
		}
	}

	public HashSet<String> getActionKeywords() {
		HashSet<String> keywords = new HashSet<>();
		List<CAction> cActions = actionDAO.list(null, null, null, null, null, null, null);
		for (CAction cAction : cActions) {
			String url = cAction.getAction_url();
			String[] keys = url.split("/");
			for (String key : keys) {
				if (!key.startsWith("{")) {
					keywords.add(key);
				}
			}
		}
		return keywords;
	}

	public boolean compareActionMD5(String actionMD5) {
		HashMap<String, String> filters = new HashMap<String, String>();
		filters.put("action_code", actionMD5);
		List<CAction> cActions = actionDAO.list(null, null, null, null, null, null, null);
		if (cActions.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updateActionCode() {
		HashSet<String> keywords = getActionKeywords();
		List<CAction> cActions = actionDAO.list(null, null, null, null, null, null, null);
		for (CAction cAction : cActions) {
			cAction.setAction_code(ActionUtil.getUrlMD5(cAction.getAction_method(), cAction.getAction_url(), keywords));
			actionDAO.update(cAction);
		}
	}

	public List<Action> getActionListByUser(String userID) {
		List<Action> actions = new ArrayList<Action>();
		HashMap<String, Object> authFilter = new HashMap<String, Object>();
		authFilter.put("user_id", userID);
		List<CAuth> cAuths = authDAO.list(authFilter, null, null, null, null, null, null);
		if (!cAuths.isEmpty()) {
			CAuth cauth = cAuths.get(0);
			List<CRole> roles = cauth.getRoles();
			for (CRole cRole : roles) {
				List<CPermission> cPermissions = cRole.getPermissions();
				for (CPermission cPermission : cPermissions) {
					List<CAction> cActions = cPermission.getActions();
					for (CAction cAction : cActions) {
						Action action = new Action();
						action.setAction_url(cAction.getAction_url());
						action.setAction_method(cAction.getAction_method());
						action.setAction_code(cAction.getAction_code());
						actions.add(action);
					}
				}
			}
		}
		return actions;
	}

	public HashSet<String> getActionCodesByUser(String userID) {
		HashSet<String> actionCodes = new HashSet<String>();
		HashMap<String, Object> authFilter = new HashMap<String, Object>();
		authFilter.put("user_id", userID);
		List<CAuth> cAuths = authDAO.list(authFilter, null, null, null, null, null, null);
		if (!cAuths.isEmpty()) {
			CAuth cauth = cAuths.get(0);
			List<CRole> roles = cauth.getRoles();
			for (CRole cRole : roles) {
				List<CPermission> cPermissions = cRole.getPermissions();
				for (CPermission cPermission : cPermissions) {
					List<CAction> cActions = cPermission.getActions();
					for (CAction cAction : cActions) {
						actionCodes.add(cAction.getAction_code());
					}
				}
			}
		}
		return actionCodes;
	}

	public HashSet<String> getActionCodesByRole(String roleID) {
		HashSet<String> actionCodes = new HashSet<String>();
		CRole role = roleDAO.get(roleID);
		if (role != null) {
			List<CPermission> cPermissions = role.getPermissions();
			for (CPermission cPermission : cPermissions) {
				List<CAction> cActions = cPermission.getActions();
				for (CAction cAction : cActions) {
					actionCodes.add(cAction.getAction_code());
				}
			}
		}
		return actionCodes;
	}
}
