package com.handge.housingfund.common.service.finance.model.enums;

/**
 * Created by gxy on 18-2-26.
 */
public enum Subject2Account {
    建中活6556("52001694036050006556", "10101", "活"),
    建中定6556("52001694036050006556", "10102", "定"),
    建中贷0058("52001694036052500058", "10110", "活"),
    农分银活4956("23873001040004956", "10104", "活"),
    农分银定4956("23873001040004956", "10105", "定"),
    农贷基金6175("23873001040006175", "10111", "活"),
    中天活1001("132005902189", "10108", "活"),
    中天定1001("132005902189", "10109", "定"),
    中银贷基金2967("132008752967", "10113", "活"),
    工洪活5732("2406070529200005732", "10106", "活"),
    工洪定5732("2406070529200005732", "10107", "定"),
    工贷基金0635("2406070529200020635", "10112", "活"),
    交银贷利息3677("525080000018010013677", "10115", "活"),
    交银贷利息定3677("525080000018010013677", "10116", "定"),
    交银贷基金3504("525080000018010013504", "10114", "活"),

    建中活0065("52001694036052500065", "10201", "活"),
    建中定0065("52001694036052500065", "10202", "定");

    private String account;
    private String subject;
    private String type;

    Subject2Account(String account, String subject, String type) {
        this.account = account;
        this.subject = subject;
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public static String getSubjectByAccount(String account, String type) {

        Subject2Account[] values = Subject2Account.values();
        for (Subject2Account subject2Account : values) {
            if (subject2Account.getAccount().equals(account) && subject2Account.getType().equals(type)) {
                return subject2Account.getSubject();
            }
        }

        return "";
    }
}
