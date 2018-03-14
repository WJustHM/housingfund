package com.handge.housingfund.other;


import java.sql.*;

/**
 * Created by tanyi on 2017/12/13.
 * 单位未知信息处理
 */
public class UnitTest {

    private static final String URL = "jdbc:mysql://172.18.20.100:3306/product_1209?characterEncoding=utf8&useSSL=true";
    private static final String NAME = "root";
    private static final String PASSWORD = "zlgj9YAf02zt21ZYv1QwXzVHttUAZv";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn;
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("开始更新单位信息。。。");
            long dwxxstart = System.currentTimeMillis();
            //更新单位信息
            String updateUnitSQL = "UPDATE st_common_unit scu,st_common_person scp " +
                    "SET scu.DWFRDBXM=scp.XingMing,scu.DWFRDBZJLX='01',scu.DWFRDBZJHM=scp.ZJHM,scu.JBRXM=scp.XingMing,scu.JBRZJLX='01',scu.JBRZJHM=scp.ZJHM " +
                    "WHERE scp.Unit=scu.id AND scp.ZJLX='01' AND scu.DWFRDBXM='未知法人'";
            PreparedStatement preparedStatement1 = conn.prepareStatement(updateUnitSQL);
            preparedStatement1.execute();
            preparedStatement1.close();
            System.out.println("更新单位信息完成，耗时：" + (System.currentTimeMillis() - dwxxstart) / 1000 + "秒");

            System.out.println("");

            System.out.println("开始更新缴存单位业务明细扩展表。。。");
            long jcjlstart = System.currentTimeMillis();
            //缴存单位业务明细扩展表
            String updateUnidetailtSQL = "UPDATE c_collection_unit_business_details_extension cu,st_collection_unit_business_details su,st_common_unit sc,c_account_employee ce,c_auth ca,c_auth_role car,c_role cr " +
                    "SET cu.CZY=ce.XingMing,cu.JBRXM=sc.JBRXM,cu.JBRZJLX=sc.JBRZJLX,cu.JBRZJHM=sc.JBRZJHM WHERE" +
                    " cu.id=su.extenstion AND su.DWZH=sc.DWZH AND ce.cAccountNetwork_id=cu.YWWD AND ce.id=ca.user_id AND ca.id=car.auth_id AND car.role_id=cr.id AND cr.role_name LIKE '%操作员' AND cu.CZY='system'";
            PreparedStatement preparedStatement2 = conn.prepareStatement(updateUnidetailtSQL);
            preparedStatement2.execute();
            preparedStatement2.close();
            System.out.println("更新缴存单位业务明细扩展表完成，耗时：" + (System.currentTimeMillis() - jcjlstart) / 1000 + "秒");

            //单位账户设立 c_collection_unit_information_basic_vice
            System.out.println("开始单位账户设立表。。。");
            long dwzhslstart = System.currentTimeMillis();
            String updatedwzhslSQL = "UPDATE c_collection_unit_information_basic_vice cuibv,st_common_unit scn,c_collection_unit_business_details_extension cubd,st_collection_unit_business_details scubd " +
                    "SET cuibv.CZY=cubd.CZY,cuibv.DWFRDBXM=scn.DWFRDBXM,cuibv.DWFRDBZJLX=scn.DWFRDBZJLX,cuibv.DWFRDBZJHM=scn.DWFRDBZJHM,cuibv.JBRXM=scn.JBRXM,cuibv.JBRZJLX=scn.JBRZJLX,cuibv.JBRZJHM=scn.JBRZJHM WHERE" +
                    " cuibv.DWZH=scn.DWZH AND cubd.id=scubd.extenstion AND scubd.DWZH=cuibv.DWZH AND cuibv.CZY='system'";
            PreparedStatement preparedStatement3 = conn.prepareStatement(updatedwzhslSQL);
            preparedStatement3.execute();
            preparedStatement3.close();
            System.out.println("更新单位账户设立表完成，耗时：" + (System.currentTimeMillis() - dwzhslstart) / 1000 + "秒");

            System.out.println("总耗时：" + (System.currentTimeMillis() - dwxxstart) / 1000 + "秒");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
