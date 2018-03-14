package com.handge.housingfund.common;

/**
 * Created by xuefei_wang on 17-8-31.
 */
public final class Constant {

    public static final String JWT_CONF = "jwt";

    public static final String ACCOUNT_CONF = "account";

    public static final String COMMON_CONF = "common" ;

    public static final String SERVER_CONF = "server" ;

    public static final String FINANCE_CONF = "finance";

    public static final String LOAN_CONF = "loan";

    public static final String BANK_CONF = "bank";

    public static final String TASK_CONF = "task";


    public final static class JWT {
     public final static String  secret = "secret";
     public final static String  secret_defualt = "jkhKLJSDFfsmbfsJKHSDFdf897891j3hjgvgf13g2jhg312313";
     public final static String  issuer = "issuer";
     public final static String  issuer_defualt = "handge.com";
     public final static String  expires = "expires";
     public final static long    expires_defualt = 240000l;
    }

    public final static class Accout {


    }

    public final static class Common {


    }

    public final static class Server {

        public static  final  String  TOKEN_KEY = "TOKEN_CONTEXT";

        public static final  String TOKEN_HEAD = "H-TOKEN";

        public static final  String ADMIN_TOKEN = "admin_token";

        public static final String no_token_urls = "notoken_urls";

        public static final String token_expires = "token_expires";

        public static final int token_expires_defualt = 15;


    }

    public final static class Finance {


    }

    public final static class Loan {


    }

    public final static class Bank {

    }

}
