package com.handge.housingfund.account.service.permission;

import com.handge.housingfund.common.service.account.IUserService;
import com.handge.housingfund.common.service.account.model.UserInfo;
import com.handge.housingfund.database.dao.*;
import com.handge.housingfund.database.entities.CAccountEmployee;
import com.handge.housingfund.database.entities.CLoanHousingCompanyBasic;
import com.handge.housingfund.database.entities.StCommonPerson;
import com.handge.housingfund.database.entities.StCommonUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xuefei_wang on 17-9-5.
 */
@Component
public class UserServiceImpl implements IUserService {

	@Autowired
	ICAccountEmployeeDAO employeeAccountDAO;

	@Autowired
	IStCommonPersonDAO commonPersonDAO;

	@Autowired
	IStCommonUnitDAO commonUnitDAO;

	@Autowired
	ICLoanHousingCompanyBasicDAO companyBasicDAO;

	@Autowired
	ICAuthDAO authDAO;

	@Override
	public UserInfo getEmployeeInfo(String userId) throws Exception {
		CAccountEmployee accountEmployees = employeeAccountDAO.get(userId);
		if (accountEmployees == null)
			return null;
		UserInfo userInfo = new UserInfo();
		userInfo.setGRXM(accountEmployees.getXingMing());
		userInfo.setCZY(accountEmployees.getXingMing());
		userInfo.setYWWD(accountEmployees.getcAccountNetwork().getId());
		userInfo.setYWWDMC(accountEmployees.getcAccountNetwork().getMingCheng());
		userInfo.setGRZH(accountEmployees.getZhangHao());

		return userInfo;
	}

	/**
	 * 个人账户
	 */
	@Override
	public UserInfo getPersonInfo(String userId) {
		StCommonPerson person = commonPersonDAO.get(userId);
		if (person == null)
			return null;
		UserInfo userInfo = new UserInfo();
		userInfo.setGRXM(person.getXingMing());
		userInfo.setGRZH(person.getGrzh());
		userInfo.setCZY(person.getXingMing());

		return userInfo;
	}

	/**
	 * 单位账户
	 */
	@Override
	public UserInfo getUnitInfo(String userId) {
		StCommonUnit unit = commonUnitDAO.get(userId);
		if (unit == null)
			return null;
		UserInfo userInfo = new UserInfo();
		userInfo.setDWMC(unit.getDwmc());
		userInfo.setDWZH(unit.getDwzh());
		return userInfo;
	}

	/**
	 * 房开账号
	 */
	@Override
	public UserInfo getHousdeveloperInfo(String userId) {
		CLoanHousingCompanyBasic company = companyBasicDAO.get(userId);
		if (company == null)
			return null;
		UserInfo userInfo = new UserInfo();
		userInfo.setFKGS(company.getFkgs());
		userInfo.setFKGSZH(company.getFkgszh());
		return userInfo;
	}
}
