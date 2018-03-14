package com.handge.housingfund.others.webservice.utils;

import com.handge.housingfund.common.configure.Configure;
import org.apache.commons.configuration2.Configuration;

/**
 * Created by tanyi on 2017/12/8.
 */
public class SealHelper {

    private static Configuration configuration = Configure.getInstance().getConfiguration("caservice");

    /**
     * 获取公章
     *
     * @return
     */
    static public String getOfficialSeal() {
        return configuration.getString("officialseal");
    }

    /**
     * 获取财务章
     *
     * @return
     */
    static public String getFinancialSeal() {
        return configuration.getString("financialseal");
    }

    /**
     * 获取合同章
     *
     * @return
     */
    static public String getContractSeal() {
        return configuration.getString("contractseal");
    }
}
