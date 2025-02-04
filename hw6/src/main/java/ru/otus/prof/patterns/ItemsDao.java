package ru.otus.prof.patterns;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsDao {

    private final DataSource dataSource;

    public ItemsDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createItem(Item item) throws SQLException {
        String query = "INSERT INTO items (id, title, price) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, item.getId());
            statement.setString(2, item.getTitle());
            statement.setDouble(3, item.getPrice());
            statement.executeUpdate();
        }
    }

    public Item getItemById(int id) throws SQLException {
        String query = "SELECT id, title, price FROM items WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Item(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getDouble("price"));
                }
            }
        }
        return null;
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String query = "SELECT id, title, price FROM items";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                items.add(new Item(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getDouble("price")));
            }
        }
        return items;
    }

    public void updateItem(Item item) throws SQLException {
        String query = "UPDATE items SET title = ?, price = ? WHERE id = ? ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, item.getTitle());
            statement.setDouble(2, item.getPrice());
            statement.setInt(3, item.getId());
            statement.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

}
