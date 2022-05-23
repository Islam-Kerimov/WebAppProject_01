package com.kerimovikh.connection;

import java.sql.Connection;

public class ConnectionUtils {

    public static Connection getConnection() {
        return PostgresConnectionUtils.getPostgresConnection();
    }

    public static void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollbackQuietly(Connection connection) {
        try {
            connection.rollback();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
