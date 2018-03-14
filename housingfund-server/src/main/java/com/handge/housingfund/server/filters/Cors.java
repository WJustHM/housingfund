package com.handge.housingfund.server.filters;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.springframework.stereotype.Service;

import javax.annotation.Priority;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * Created by xuefei_wang on 17-9-7.
 */
@Provider
@Service
@PreMatching
@Priority(900)
public class Cors extends CorsFilter {
    public Cors(){
        this.getAllowedOrigins().add("*");
        this.setAllowedHeaders("Content-Type, H-TOKEN,Pragma,Cache-Control");
        this.setAllowedMethods("GET, POST, DELETE, PUT, PATCH, OPTIONS");
    }
}
