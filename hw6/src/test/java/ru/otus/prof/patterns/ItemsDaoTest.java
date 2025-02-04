package ru.otus.prof.patterns;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ItemsDaoTest {
    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPrepStatement;

    @Mock
    private ResultSet mockResultSet;

    private ItemsDao itemsDao;

    @BeforeAll
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        itemsDao = new ItemsDao(mockDataSource);
    }

    @Test
    public void createItemTest() throws SQLException {
        Item item = new Item(1, "Test Item", 100.0);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepStatement);

        itemsDao.createItem(item);

        verify(mockPrepStatement).setInt(1, item.getId());
        verify(mockPrepStatement).setString(2, item.getTitle());
        verify(mockPrepStatement).setDouble(3, item.getPrice());
        verify(mockPrepStatement).executeUpdate();
    }

    @Test
    public void getItemByIdTest() throws SQLException {
        Item expectedItem = new Item(1, "Test Item", 100.0);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepStatement);
        when(mockPrepStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("Test Item");
        when(mockResultSet.getDouble("price")).thenReturn(100.0);

        Item result = itemsDao.getItemById(1);

        assertNotNull(result);
        assertEquals(expectedItem, result);
    }

    @Test
    public void getAllItemsTest() throws SQLException {
        Item item1 = new Item(1, "Test Item", 100.0);
        Item item2 = new Item(2, "Test Item2", 200.0);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepStatement);
        when(mockPrepStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("id")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString("title")).thenReturn("Test Item").thenReturn("Test Item2");
        when(mockResultSet.getDouble("price")).thenReturn(100.0).thenReturn(200.0);

        List<Item> result = itemsDao.getAllItems();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(item1, result.get(0));
        assertEquals(item2, result.get(1));
    }

    @Test
    public void updateItemTest() throws SQLException {
        Item item = new Item(1, "Test Item", 100.0);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepStatement);

        itemsDao.updateItem(item);

        verify(mockPrepStatement).setString(1, item.getTitle());
        verify(mockPrepStatement).setDouble(2, item.getPrice());
        verify(mockPrepStatement).setInt(3, item.getId());
        verify(mockPrepStatement).executeUpdate();
    }

    @Test
    public void deleteItemTest() throws SQLException {
        int itemId = 1;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrepStatement);

        itemsDao.deleteItem(itemId);

        verify(mockPrepStatement).setInt(1, itemId);
        verify(mockPrepStatement).executeUpdate();
    }


}
