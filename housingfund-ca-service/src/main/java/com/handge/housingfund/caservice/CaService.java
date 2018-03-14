package com.handge.housingfund.caservice;

import com.handge.housingfund.common.service.ca.model.UnitInfo;
import com.handge.housingfund.common.service.util.ErrorException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by tanyi on 2017/7/4.
 */
@WebService
public class CaService {

    @SuppressWarnings("resource")
    @WebMethod
    public UnitInfo GetGJJCompanyInfo(String dwgjjzh) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo.xml");
            context.start();
            com.handge.housingfund.common.service.ca.CaService rpcService = (com.handge.housingfund.common.service.ca.CaService) context
                    .getBean("caService");
            return rpcService.GetGJJCompanyInfo(dwgjjzh);
        } catch (ErrorException e) {
            System.out.println(e.getMsg());
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
