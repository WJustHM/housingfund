package com.handge.housingfund.collection.service;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.ResUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Liujuhao on 2017/8/16.
 */
@Component
@Aspect
public class ReturnAopService {

    @Pointcut("execution(* com.handge.housingfund.collection.service.*..*.*(..))")
    public void appPointCut() {
    }

    @Around("appPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        try {

            // TODO: 2017/11/17 字段检查的开关 
//            com.handge.housingfund.common.annotation.Annotation.checkValue(pjp.getArgs());

            result = ResUtils.autoNoneAdductionValue(pjp.proceed());

        } catch (Exception e) {
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚
            if (e instanceof ErrorException) {

                e.printStackTrace();
                throw e;

            } else {

                e.printStackTrace();
                throw new ErrorException(e);
            }

        }
        return result;
    }
}
