package ru.otus.prof.patterns;

import java.sql.Connection;
import java.sql.SQLException;

public class ItemsServiceProxy {
    private final ItemsService itemsService;
    private final DataSource dataSource;

    public ItemsServiceProxy(ItemsService itemsService) {
        this.itemsService = itemsService;
        this.dataSource = DataSource.getInstance();
    }

    public void save100Items() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            itemsService.save100Items();
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Transaction failed during save100Items: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    System.err.println("Failed to close connection: " + closeEx.getMessage());
                }
            }
        }
    }

    public void doublePriceAll(){
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            itemsService.doublePriceAll();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error on doublePriceAll: " + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    System.err.println("Failed to close connection: " + closeEx.getMessage());
                }
            }
        }


    }
}
