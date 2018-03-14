package com.handge.housingfund;

import com.handge.housingfund.account.util.PasswordUtil;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by xuefei_wang on 17-9-20.
 */
public class AccountTest {

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
         String password = PasswordUtil.encryptPwd("admin");
        System.out.println(password);
         boolean isok = PasswordUtil.validatePwd("admin",password);
        System.out.println(isok);
    }
}
