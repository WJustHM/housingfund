package com.handge.housingfund.common.service.collection.service.common;

/**
 * Created by 凡 on 2017/8/2.
 * 工作流：业务系统流程的自动化,需根据当前的业务状态，传入参数，后面的工作就交给work来执行
 * 为各个业务专注各自业务逻辑，对业务需要的公共功能统一处理，降低业务的复杂度
 * 需实现功能：
 * 1、状态的更新
 * 2、业务流程步骤正确性检查、防重复提交验证
 * 3、流程每一步骤的记录
 * 4、执行某一步后的方法调用（目前只支持对办结方法调用,后期完善）
 * 5、在途验证
 * 6、业务间相互影响统一验证
 */
public interface WorkFlow {

    void doWork(WorkCondition condition) throws Exception;

}
