package com.handge.housingfund.collection.service.common;

import com.handge.housingfund.collection.utils.ComUtils;
import com.handge.housingfund.common.service.collection.service.common.WorkCondition;
import com.handge.housingfund.common.service.collection.service.common.WorkFlow;
import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.database.dao.ICStateMachineConfigurationDAO;
import com.handge.housingfund.database.dao.IStCollectionPersonalBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCollectionUnitBusinessDetailsDAO;
import com.handge.housingfund.database.dao.IStCommonUnitDAO;
import com.handge.housingfund.database.entities.*;
import com.handge.housingfund.database.enums.BusinessType;
import com.handge.housingfund.statemachine.entity.TaskEntity;
import com.handge.housingfund.statemachine.trigger.IPersitListener;
import com.handge.housingfund.statemachineV2.service.StateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.state.State;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by 凡 on 2017/8/2.
 */

public abstract class BaseService implements WorkFlow{

    @Autowired
    private ICStateMachineConfigurationDAO stateMachineDao;

    @Autowired
    private IStCollectionUnitBusinessDetailsDAO unitBusinessDAO;

    @Autowired
    private IStCollectionPersonalBusinessDetailsDAO personBusinessDAO;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private IStCommonUnitDAO unitDAO;

    private final PersitListener persitListener = new PersitListener();

    @Override
    public void doWork(WorkCondition condition){
        //对参数进行检查
        check(condition);
        //在途业务验证
        doingCheck(condition);
        //业务关系在途验证
        workRelationChech(condition);
        //接入状态机,对状态进行更新
        stateReflesh(condition);

        //根据config对业务状态进行更新,如果下一步是办结，对办结结果调用
        //methodCall(condition);
        //新增一张表，记录当前业务流程的步骤变化
        //workRecord(condition);
        //return 如何构建返回消息
    }

    private void stateReflesh(WorkCondition condition) {
        String ywlsh = condition.getYwlsh();
        String event = condition.getEvent();
        String state = condition.getStatus();
        TaskEntity task = new TaskEntity();
        task.setStatus(state);
        task.setType(condition.getType());
        task.setSubtype(condition.getSubType());
        task.setStatus(condition.getStatus());
        task.setTaskId(ywlsh);
        task.setOperator(condition.getCzy());
        task.setRoleSet(new HashSet<>(condition.getRoleList()));
        task.setWorkstation(condition.getYwwd());
        try {
            //状态机执行
            stateMachineInvoke(event,task);
            //办结调用
        } catch (Exception e) {
            e.printStackTrace();
            throw new ErrorException("业务权限不足！");
        }
        doFinalInvoke(ywlsh);
    }

    private HashSet<String> getHashSet(WorkCondition condition){
        List<String> roleList = condition.getRoleList();
        if(roleList==null || roleList.size() == 0){
            HashSet<String> strings = new HashSet<>();
            strings.add("1");
            return strings;
        }else{
            return new HashSet<>(condition.getRoleList());
        }
    }

    private void stateMachineInvoke(String event,TaskEntity task) throws Exception {
        stateMachineService.registerPersistHandler(persitListener);
        boolean flag = stateMachineService.handle(event, task);
        if(flag == false){
            throw new ErrorException("状态机执行出现异常！流程配置错误！");
        }
    }

    private void doFinalInvoke(String ywlsh){
        StCollectionUnitBusinessDetails uBus = unitBusinessDAO.getByYwlsh(ywlsh);
        if(uBus != null){
            String step = uBus.getExtension().getStep();
            if("办结|已入账分摊|已入账".indexOf(step) >= 0){
                doFinal(ywlsh);
            }
            return;
        }
        StCollectionPersonalBusinessDetails pBus = personBusinessDAO.getByYwlsh(ywlsh);
        if(pBus != null){
            String step = pBus.getExtension().getStep();
            if("办结".equals(step)){
                doFinal(ywlsh);
            }
            return;
        }
        throw new ErrorException("当前业务不存在:"+ywlsh);
    }

    private void workRelationChech(WorkCondition condition) {

    }

    private void doingCheck(WorkCondition condition) {
        if(!"初始状态".equals(condition.getStatus())){
            return;
        }
        if("1".equals(condition.getZtlx())){

        }
        if("2".equals(condition.getZtlx())){

        }
    }

    /**
     * 办理方法如何定义?
     * 参数统一使用map或ywlsh，能满足需求就行
     */
    private void methodCall(WorkCondition condition){
        String methodcall = "unitDepositInventory|dofinal"; //config.getMethodCall();
        //根据定义解析字符串，反射调用
        if(!ComUtils.isEmpty(methodcall)){
            String[] split = methodcall.split("\\|");
            String beanName = split[0];
            String methodName = split[1];
            Object bean = SpringContextUtil.getBean(beanName);
            if(bean == null){
                throw new ErrorException("方法调用，配置错误，没有找到对应的service："+beanName);
            }
            Class clazz = bean.getClass();
            Method method = null;
            try {
                method = clazz.getMethod(methodName ,new Class[]{String.class});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new ErrorException("方法配置错误！没有找到需要执行的方法!");
            }
            try {
                Object invoke = method.invoke(bean, condition.getYwlsh());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new ErrorException("方法配置错误！该方法是私有的！");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                throw new ErrorException("方法执行错误："+e.getMessage());
            }
        }
    }

    private void workRecord(WorkCondition condition, CStateMachineConfiguration config){

    }

    private CStateMachineConfiguration getNextStepConfig(WorkCondition condition){
        BusinessType type = condition.getType();
        String subType = condition.getSubType();
        String status = condition.getStatus();
        String event = condition.getEvent();
        List<CStateMachineConfiguration> config = stateMachineDao.getConfig(type, subType, status, event);
        if(null == config || config.size() == 0){
            throw new ErrorException("根据当前条件返回的流程配置信息为空");
        }
        if(config.size()>1){
            throw new ErrorException("根据当前条件返回的流程配置信息有"+config.size()+"条，请检查配置表！");
        }
        return config.get(0);
    }

    /**
     * 必要参数的检查
     */
    private void check(WorkCondition condition){
        if(ComUtils.isEmpty(condition)){
            throw new ErrorException("传入的参数不能为空!");
        }
        if(ComUtils.isEmpty(condition.getYwlsh())){
            throw new ErrorException("业务流水号不能为空!");
        }
        if(ComUtils.isEmpty(condition.getType())){
            throw new ErrorException("业务类型不能为空!");
        }
        if(ComUtils.isEmpty(condition.getSubType())){
            throw new ErrorException("业务子类型不能为空!");
        }
        if(ComUtils.isEmpty(condition.getStatus())){
            throw new ErrorException("当前状态不能为空!");
        }
        if(ComUtils.isEmpty(condition.getEvent())){
            throw new ErrorException(" 触发事件不能为空!");
        }
        if(ComUtils.isEmpty(condition.getZtlx())){
            throw new ErrorException("操作的主体类型不能为空!");
        }
        if(!("1".equals(condition.getZtlx()) || "2".equals(condition.getZtlx()))){
            throw new ErrorException("操作的主体类型只能为1:单位或2:个人!");
        }
        if(ComUtils.isEmpty(condition.getCzy())){
            throw new ErrorException("当前办理人员不能为空!");
        }
        if(ComUtils.isEmpty(condition.getYwwd())){
            throw new ErrorException("当前办理的业务网点不能为空!");
        }
    }

    //只限于归集模块,归集业务统一在此更改状态
    private class PersitListener extends IPersitListener{
        @Override
        public void persist(State<String, String> state, String event, TaskEntity taskEntity) {
            String ywlsh = taskEntity.getTaskId();
            String step = state.getId();
            //如果是单位业务，更新单位业务
            StCollectionUnitBusinessDetails dwyw = unitBusinessDAO.getByYwlsh(ywlsh);
            if(dwyw != null){
                CCollectionUnitBusinessDetailsExtension extension = dwyw.getExtension();
                extension.setStep(step);
                extension.setBjsj(new Date());
                unitBusinessDAO.update(dwyw);
                return;
            }
            //如果是个人业务，更新个人业务
            StCollectionPersonalBusinessDetails gryw = personBusinessDAO.getByYwlsh(ywlsh);
            if(gryw != null){
                CCollectionPersonalBusinessDetailsExtension extension = gryw.getExtension();
                extension.setStep(step);
                extension.setBjsj(new Date());
                unitBusinessDAO.update(gryw);
                return;
            }
            //否则抛出异常
            throw new ErrorException("当前的业务流水号不存在！");
        }
    }

    public abstract void doFinal(String ywlsh);

    /**
     * 归集业务在途验证：单位
     * 返回true表示单位已存在正在办理的指定czmc的业务
     */
    public boolean isExistDoingUnitBus(String dwzh,String czmc){
        return unitBusinessDAO.isExistDoingUnitBus(dwzh,czmc);
    }

    /**
     * 归集业务在途验证：个人
     * 返回值：
     * true表示个人已存在正在办理的指定czmc的业务
     * false表示个人没有正在办理的业务
     */
    public boolean isExistDoingPersonBus(String grzh,String czmc){
        return personBusinessDAO.isExistDoingPersonBus(grzh,czmc);
    }

    /**
     * 当前业务是否处于可修改的状态（单位业务）
     * 注意：没有判断ywlsh是否存在
     */
    public boolean isCouldModifyUnitBus(String ywlsh){
        return unitBusinessDAO.isCouldModifyUnitBus(ywlsh);
    }

    /**
     * 当前业务是否处于可修改的状态（个人业务）
     * 注意：没有判断ywlsh是否存在
     */
    public boolean isCouldModifyPersonBus(String ywlsh){
        return personBusinessDAO.isCouldModifyPersonBus(ywlsh);
    }

}


