package com.handge.housingfund.loan.service;

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

        @Pointcut("execution(* com.handge.housingfund.loan.service..*.*(..))&&!execution(* com.handge.housingfund.loan.service.ExceptionMethod.exception*(..))&&!execution(* com.handge.housingfund.loan.service.ClearingBank.*(..))&&!execution(* com.handge.housingfund.loan.service.BankCallService.*(..))&&!execution(* com.handge.housingfund.loan.service.LoanTaskService.*(..))&&!execution(* com.handge.housingfund.loan.service.CommCheckMethod.*(..))")
//    @Pointcut("execution(* com.handge.housingfund.loan.service..*.*(..))")
//        @Pointcut("execution(* com.handge.housingfund.loan.service..*.*(..))&&!execution(* com.handge.housingfund.loan.service.ClearingBank.*(..))&&!execution(* com.handge.housingfund.loan.service.BankCallService.*(..))&&!execution(* com.handge.housingfund.loan.service.LoanTaskService.*(..))&&!execution(* com.handge.housingfund.loan.service.CommCheckMethod.*(..))")

        public void appPointCut() {
    }

    @Around("appPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result;

        try {

            result = ResUtils.autoNoneAdductionValue(pjp.proceed());

        } catch (Exception e) {

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
