package com.handge.housingfund.other;

import java.sql.*;

/**
 * Created by tanyi on 2017/12/13.
 * 增值收益专户清理
 */
public class FinanceTest {

    private static final String URL = "jdbc:mysql://172.18.20.100:3306/product_1207?characterEncoding=utf8&useSSL=true";
    private static final String NAME = "root";
    private static final String PASSWORD = "zlgj9YAf02zt21ZYv1QwXzVHttUAZv";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String[] kmbhs = {"10203", "10204", "10205", "10206", "10207", "10208", "10209"};
        Connection conn;
        try {
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            conn.createStatement();
            for (String kmbh : kmbhs) {
                //银行专户信息表 st_settlement_special_bank_account
                String yhzhxxb1SQL = "SELECT extension,YHZHHM FROM st_settlement_special_bank_account WHERE KMBH=?";
                PreparedStatement preparedStatement7 = conn.prepareStatement(yhzhxxb1SQL);
                preparedStatement7.setString(1, kmbh);
                ResultSet resultSet1 = preparedStatement7.executeQuery();
                while (resultSet1.next()) {
                    String yhzhxxb2SQL = "DELETE FROM c_settlement_special_bank_account_extension WHERE id=?";
                    PreparedStatement preparedStatement6 = conn.prepareStatement(yhzhxxb2SQL);
                    preparedStatement6.setString(1, resultSet1.getString("extension"));
                    preparedStatement6.execute();
                    preparedStatement6.close();

                    String yhzhxxb3SQL = "DELETE FROM st_finance_bank_deposit_journal WHERE YHZHHM=?";
                    PreparedStatement preparedStatement11 = conn.prepareStatement(yhzhxxb3SQL);
                    preparedStatement11.setString(1, resultSet1.getString("YHZHHM"));
                    preparedStatement11.execute();
                    preparedStatement11.close();
                }
                preparedStatement7.close();
                resultSet1.close();
                String yhzhxxbSQL = "DELETE FROM st_settlement_special_bank_account WHERE KMBH=?";
                PreparedStatement preparedStatement = conn.prepareStatement(yhzhxxbSQL);
                preparedStatement.setString(1, kmbh);
                preparedStatement.execute();
                preparedStatement.close();

                //明细账信息 st_finance_subsidiary_accounts
                String mxzSQL = "UPDATE st_finance_subsidiary_accounts SET KMBH = '10201' WHERE KMBH=?";
                PreparedStatement preparedStatement1 = conn.prepareStatement(mxzSQL);
                preparedStatement1.setString(1, kmbh);
                preparedStatement1.execute();
                preparedStatement1.close();

                //记账凭证信息 st_finance_recording_voucher
                String jzpzSQL = "UPDATE st_finance_recording_voucher SET KMBH = '10201' WHERE KMBH=?";
                PreparedStatement preparedStatement2 = conn.prepareStatement(jzpzSQL);
                preparedStatement2.setString(1, kmbh);
                preparedStatement2.execute();
                preparedStatement2.close();

                //科目信息 st_finance_subjects
                String kmxx1SQL = "SELECT extension FROM st_finance_subjects WHERE KMBH=?";
                PreparedStatement preparedStatement8 = conn.prepareStatement(kmxx1SQL);
                preparedStatement8.setString(1, kmbh);
                ResultSet resultSet2 = preparedStatement8.executeQuery();
                while (resultSet2.next()) {
                    String kmxxSQL = "DELETE FROM c_finance_subjects_extension WHERE id=?";
                    PreparedStatement preparedStatement9 = conn.prepareStatement(kmxxSQL);
                    preparedStatement9.setString(1, resultSet2.getString("extension"));
                    preparedStatement9.execute();
                    preparedStatement9.close();
                }
                preparedStatement8.close();
                resultSet2.close();

                String kmxxSQL = "DELETE FROM st_finance_subjects WHERE KMBH=?";
                PreparedStatement preparedStatement3 = conn.prepareStatement(kmxxSQL);
                preparedStatement3.setString(1, kmbh);
                preparedStatement3.execute();
                preparedStatement3.close();

                //科目余额表 c_finance_subjects_balance
                String kmyebSql = "SELECT SYYE,BYZJ,BYJS,BYYE,cFinanceAccountPeriod,id FROM c_finance_subjects_balance WHERE KMBH=?";
                PreparedStatement preparedStatement4 = conn.prepareStatement(kmyebSql);
                preparedStatement4.setString(1, kmbh);
                ResultSet resultSet = preparedStatement4.executeQuery();
                while (resultSet.next()) {
                    String kmyeb1Sql = "UPDATE c_finance_subjects_balance SET SYYE=SYYE+?,BYZJ=BYZJ+?,BYJS=BYJS+?,BYYE=BYYE+? WHERE cFinanceAccountPeriod=? AND KMBH='10201'";
                    PreparedStatement preparedStatement5 = conn.prepareStatement(kmyeb1Sql);
                    preparedStatement5.setBigDecimal(1, resultSet.getBigDecimal("SYYE"));
                    preparedStatement5.setBigDecimal(2, resultSet.getBigDecimal("BYZJ"));
                    preparedStatement5.setBigDecimal(3, resultSet.getBigDecimal("BYJS"));
                    preparedStatement5.setBigDecimal(4, resultSet.getBigDecimal("BYYE"));
                    preparedStatement5.setString(5, resultSet.getString("cFinanceAccountPeriod"));
                    preparedStatement5.execute();
                    preparedStatement5.close();

                    String kmyeb2Sql = "DELETE FROM c_finance_subjects_balance WHERE id=?";
                    PreparedStatement preparedStatement10 = conn.prepareStatement(kmyeb2Sql);
                    preparedStatement10.setString(1, resultSet.getString("id"));
                    preparedStatement10.execute();
                    preparedStatement10.close();
                }
                preparedStatement4.close();
                resultSet.close();
                System.out.println(kmbh);
            }
            //删除银行流水日记账表
            // DELETE sj FROM st_finance_bank_deposit_journal sj LEFT JOIN st_settlement_special_bank_account sa ON (sj.YHZHHM=sa.YHZHHM) WHERE sa.YHZHHM is NULL

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
