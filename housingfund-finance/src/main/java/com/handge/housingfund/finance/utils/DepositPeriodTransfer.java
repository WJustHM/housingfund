package com.handge.housingfund.finance.utils;

import java.math.BigDecimal;

/**
 * Created by gxy on 17-9-21.
 */
public class DepositPeriodTransfer {
    public static String daysToFlag(BigDecimal ckqx){
        String deposit_period = null;

        String days = ckqx.toPlainString();
        switch (days) {
            case "90":
                deposit_period = "0";
                break;
            case "180":
                deposit_period = "1";
                break;
            case "360":
                deposit_period = "2";
                break;
            case "720":
                deposit_period = "3";
                break;
            case "1080":
                deposit_period = "4";
                break;
            case "1800":
                deposit_period = "5";
                break;
        }
        return deposit_period;
    }

    public static BigDecimal flagToDays(String ckqx){
        BigDecimal deposit_period = null;
        switch (ckqx) {
            case "0":
                deposit_period = new BigDecimal(90);
                break;
            case "1":
                deposit_period = new BigDecimal(180);
                break;
            case "2":
                deposit_period = new BigDecimal(360);
                break;
            case "3":
                deposit_period = new BigDecimal(720);
                break;
            case "4":
                deposit_period = new BigDecimal(1080);
                break;
            case "5":
                deposit_period = new BigDecimal(1800);
                break;
        }
        return deposit_period;
    }
}
