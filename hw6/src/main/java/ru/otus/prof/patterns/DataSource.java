package ru.otus.prof.patterns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataSource {
    private static final DataSource INSTANCE = new DataSource();
    private final Connection connection;

    private DataSource() {
        try {
            String url = "jdbc:postgresql://localhost:3333/postgres";
            String user = "postgres";
            String password = "postgres";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error while establish a connection: ", e);
        }
    }

    public static DataSource getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

}
