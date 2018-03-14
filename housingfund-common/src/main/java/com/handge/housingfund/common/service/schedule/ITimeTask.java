package com.handge.housingfund.common.service.schedule;

import com.handge.housingfund.common.service.schedule.enums.TimeTaskType;
import com.handge.housingfund.database.enums.BusinessQuartzType;
import com.handge.housingfund.database.enums.BusinessType;

import java.util.Date;

public interface ITimeTask {
    public void addTaskLog(BusinessQuartzType type, TimeTaskType timeTaskType, Date date, boolean SFCG, String SBYY);
}
