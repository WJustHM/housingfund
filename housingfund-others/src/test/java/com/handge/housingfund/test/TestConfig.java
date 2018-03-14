package com.handge.housingfund.test;

import com.handge.housingfund.common.service.others.IStateMachineService;
import com.handge.housingfund.others.service.StateMachineServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xuefei_wang on 17-8-9.
 */

@Configuration
public class TestConfig {

    @Bean
    IStateMachineService getStateMachineServiceImpl(){
        return  new StateMachineServiceImpl();
    }

//    @Bean
//    ICStateMachineConfigurationDAO getCStateMachineConfigurationDAO(){
//        return new CStateMachineConfigurationDAO();
//    }
}
