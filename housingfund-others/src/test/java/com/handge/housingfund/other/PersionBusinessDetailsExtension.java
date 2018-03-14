package com.handge.housingfund.other;

import java.sql.*;

/**
 * Created by tanyi on 2017/12/18.
 */
public class PersionBusinessDetailsExtension {
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
            System.out.println("开始更新个人业务明细信息。。。");
            long dwxxstart = System.currentTimeMillis();
            //更新单位信息
            String uitSQL = "SELECT id,DWMC FROM st_common_unit scu";
            PreparedStatement preparedStatement1 = conn.prepareStatement(uitSQL);
            ResultSet resultSet = preparedStatement1.executeQuery();
            int curtime = 0;
            while (resultSet.next()) {
                System.out.println(resultSet.getString("DWMC") + "---" + curtime);
                String updateUnidetailtSQL = "update c_collection_personal_business_details_extension cpbde, st_collection_personal_business_details spbd," +
                        "st_common_person scp,c_account_employee ce,c_auth ca,c_auth_role car,c_role cr " +
                        "set cpbde.CZY=ce.XingMing where cpbde.YWWD=ce.cAccountNetwork_id and ce.id=ca.user_id AND " +
                        "ca.id=car.auth_id AND car.role_id=cr.id AND cr.role_name LIKE '%操作员' AND cpbde.CZY='system' AND cpbde.id=spbd.extension" +
                        " AND spbd.Person=scp.id AND scp.Unit=?";
                PreparedStatement preparedStatement2 = conn.prepareStatement(updateUnidetailtSQL);
                preparedStatement2.setString(1, resultSet.getString("id"));
                preparedStatement2.execute();
                preparedStatement2.close();
                curtime++;
            }
            preparedStatement1.close();
            resultSet.close();

            System.out.println("总耗时：" + (System.currentTimeMillis() - dwxxstart) / 1000 + "秒");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
