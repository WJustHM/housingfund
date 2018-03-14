package com.handge.housingfund.finance.service;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.common.service.util.RedisCache;
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

    RedisCache redisCache = RedisCache.getRedisCacheInstance();

    @Pointcut("execution(* com.handge.housingfund.finance.service.*..*(..))")
    public void appPointCut() {
    }

    @Around("appPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Object result;

        try {

            result = pjp.proceed();

        } catch (Exception e) {
            //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //回滚
            if (e instanceof ErrorException) {

//                System.err.println(e.getMessage());
                throw e;

            } else {

//                System.err.println(e.getMessage());
                throw new ErrorException(e);
            }

        }
        return result;
    }
}
