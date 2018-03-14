package com.handge.housingfund.other;

import com.handge.housingfund.common.service.util.ErrorException;
import com.handge.housingfund.others.service.CaServiceImpl;

/**
 * Created by tanyi on 2017/12/5.
 * 测试ca认证
 */
public class CaTest {

    public static void main(String[] args) {
        CaServiceImpl caService = new CaServiceImpl();
        String CertBase64 = "MIIDZDCCAwegAwIBAgIQYTr4StuhtUJm0Msgc5O/TDAMBggqgRzPVQGDdQUAMIGCMQswCQYDVQQGEwJDTjESMBAGA1UECAwJR3Vhbmdkb25nMREwDwYDVQQHDAhTaGVuemhlbjEnMCUGA1UECgweU2hlblpoZW4gQ2VydGlmaWNhdGUgQXV0aG9yaXR5MQ0wCwYDVQQLDARzemNhMRQwEgYDVQQDDAtTWkNBIFNNMiBDQTAeFw0xNzA5MTgwMjE0MjFaFw0xODA5MTgwMjE0MjFaMHgxCzAJBgNVBAYTAkNOMRIwEAYDVQQIDAnotLXlt57nnIExEjAQBgNVBAcMCei0temYs+W4gjETMBEGA1UECgwKU00y5rWL6K+VMjEbMBkGA1UECwwSNTIwMTAzMTk4NjA5MDY2NDE3MQ8wDQYDVQQDDAbnjovkuowwWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATm174DOEqYz1xks7baGyP9raLqIkF70//Ewp/o96jv6WpRWKKDRk66iZSvCVqO8PVKmy0ZsZHdl/OR6uKB4nBTo4IBZDCCAWAwCwYDVR0PBAQDAgbAMB0GA1UdDgQWBBToYa753+cr79e3XgyAIE9FxCZsszAfBgNVHSMEGDAWgBSKe0ZazhFl2FO5UXpC70c9Coy46DA4BgNVHSAEMTAvMC0GBFUdIAAwJTAjBggrBgEFBQcCARYXaHR0cDovL3d3dy5zemNhLm5ldC9jcHMwNAYDVR0fBC0wKzApoCegJYYjaHR0cDovL2NybC5zemNlcnQuY29tL2VjYy9jcmw2Ny5jcmwwDAYDVR0TBAUwAwEBADAgBggqVgsHg8zpfwQUDBI1MjIzMjExOTg5MTEyMTYxMzUwFAYIKlYLB4PM6gAECAwGR1lTR0pKMCEGCSpWCweDzOojAQQUDBI1MjAxMDMxOTg2MDkwNjY0MTcwEQYIKlYLB4PM6X0EBQwDQkpTMBQGCCpWCweDzOl8BAgMBjAxMjM1NjAPBggqVgsHg8zqYwQDDAExMAwGCCqBHM9VAYN1BQADSQAwRgIhAOy39kT3m/tpM8IejcB2boEHUeRHee3Cf+X0dmOVcVAUAiEA+nbMTX3CiQ7dJFMFYLlVX/r3Pl8beY98jf+ECmDkTag=";
        String indata = "oYFdatDk9nyi7bUp3WiPvYHwyyE9z1Dg,1512455166423";
        String SignDate = "MEUCIQDgq9IepI862O51eMbCcx7IN/LanJD6/NeZgFMZxuKTGwIgDJJKc+a5VnnksnDBB7facajn6oVXoYX3HMON/Pt5Rsc=";
        try {
            boolean res = caService.VerifySignP1(CertBase64, indata, SignDate);
            if (res) {
                System.out.println("ca证书验证成功");
            } else {
                System.out.println("ca证书验证失败");
            }
        } catch (ErrorException e) {
            System.out.println(e.getMsg());
        }

    }
}
