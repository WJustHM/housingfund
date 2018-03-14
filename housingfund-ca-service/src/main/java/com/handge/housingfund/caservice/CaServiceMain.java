package com.handge.housingfund.caservice;

import javax.xml.ws.Endpoint;

/**
 * Created by tanyi on 2017/9/1.
 */
public class CaServiceMain {
    public static void main(String args[]) {
        CaService implementor = new CaService();
        String address = "http://0.0.0.0:20001/housingfund/services/CaService";
        Endpoint.publish(address, implementor);
        System.out.println("发布成功");
    }
}
