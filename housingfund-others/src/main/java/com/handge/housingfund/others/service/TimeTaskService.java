package com.handge.housingfund.others.service;

import com.handge.housingfund.common.service.schedule.ITimeTask;
import com.handge.housingfund.common.service.schedule.enums.TimeTaskType;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.ICTimeTaskDao;
import com.handge.housingfund.database.entities.CTimeTask;
import com.handge.housingfund.database.enums.BusinessQuartzType;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.database.utils.DAOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimeTaskService implements ITimeTask{
    @Autowired
    private  ICTimeTaskDao timeTaskDao;
    @Override
    public void addTaskLog(BusinessQuartzType type, TimeTaskType timeTaskType, Date date, boolean SFCG, String SBYY){
        CTimeTask timeTask = new CTimeTask();
        timeTask.setModule(type.name());
        timeTask.setSubModule(timeTaskType.getName());
        timeTask.setZXSJ(date==null?new Date():date);
        timeTask.setSFCG(SFCG);
        timeTask.setSBYY(SBYY);
        DAOBuilder.instance(timeTaskDao).entity(timeTask).save(new DAOBuilder.ErrorHandler() {
            @Override
            public void error(Exception e) {
                throw new ErrorException(e);
            }
        });
    }
}
