package ru.otus.prof.db.interaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {
    private String url;
    private Connection connection;
    private Statement statement;
    private String user;
    private String password;

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public DataSource(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;

    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        statement = connection.createStatement();
        System.out.println("Установлено соединение с БД: " + url);
    }

    public void close() {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("От БД отключились");
    }
}
