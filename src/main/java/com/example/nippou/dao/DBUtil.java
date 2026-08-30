package com.example.nippou.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB接続をまとめるユーティリティクラス。
 *
 * 環境構築後、自分のMySQL環境に合わせて DB_URL / DB_USER / DB_PASSWORD を書き換えること。
 * (本来はweb.xmlやproperties等の外部設定に出すべきだが、学習の最初のステップとして
 *  あえてコード中に直書きし、後の改修課題で「設定の外出し」を体験する余地を残している)
 */
public class DBUtil {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/nippou_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo&characterEncoding=UTF-8";
    private static final String DB_USER = "nippou_user";
    private static final String DB_PASSWORD = "nippou_pass";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBCドライバが見つかりません。lib配下にmysql-connector-jを配置してください。", e);
        }
    }

    private DBUtil() {
        // インスタンス化しない
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
