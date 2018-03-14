package com.handge.housingfund.other;


import java.sql.*;

/**
 * Created by tanyi on 2017/12/19.
 */
public class TransferRecordUnit {
    private static final String URL = "jdbc:mysql://172.18.20.100:3306/product_0103?characterEncoding=utf8&useSSL=true";
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
            System.out.println("开始迁移信息。。。");
            long dwxxstart = System.currentTimeMillis();
            //更新单位信息
            String uitSQL = "SELECT ccnn.DWZH DWZH,cbcn.amt amt,ccnn.created_at d1,cbcn.remark remark,cbcn.JZPZH JZPZH,cbcn.summary summary FROM c_change_notice_to_unit ccnn,c_bank_acc_change_notice cbcn WHERE ccnn.notice=cbcn.id";
            PreparedStatement preparedStatement1 = conn.prepareStatement(uitSQL);
            ResultSet resultSet = preparedStatement1.executeQuery();
            int curtime = 0;
            while (resultSet.next()) {
                System.out.println(resultSet.getString("DWZH") + "---" + curtime + 1);
                String insertSQL = "INSERT INTO c_finance_record_unit(id,DWZH,FSE,remark,summary,JZPZH,ZJLY,created_at) VALUES(?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement2 = conn.prepareStatement(insertSQL);
                preparedStatement2.setString(1, String.valueOf(curtime + 1));
                preparedStatement2.setString(2, resultSet.getString("DWZH"));
                preparedStatement2.setString(3, resultSet.getString("amt"));
                preparedStatement2.setString(4, resultSet.getString("remark"));
                preparedStatement2.setString(5, resultSet.getString("summary"));
                preparedStatement2.setString(6, resultSet.getString("JZPZH"));
                preparedStatement2.setString(7, "汇补缴");
                preparedStatement2.setString(8, resultSet.getString("d1"));

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
