package com.housingfund;


import com.housingfund.webservice.CaService;
import com.housingfund.webservice.CaServiceService;

/**
 * Created by tanyi on 2017/9/1.
 */
public class TestMain {
    public static void main(String args[]) {
        CaServiceService caServiceService = new CaServiceService();
        CaService caService = caServiceService.getCaServicePort();
        System.out.println(caService.getGJJCompanyInfo("9817120400001").toString());
    }
}
