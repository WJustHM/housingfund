package com.handge.housingfund.common.service.account;

import com.handge.housingfund.common.service.account.model.UserInfo;

/**
 * Created by xuefei_wang on 17-9-5.
 */
public interface IUserService {

	public UserInfo getEmployeeInfo(String userId) throws Exception;

	public UserInfo getPersonInfo(String userId);

	public UserInfo getUnitInfo(String userId);

	public UserInfo getHousdeveloperInfo(String userId);

}
