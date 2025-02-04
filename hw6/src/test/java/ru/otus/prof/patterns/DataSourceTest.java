package ru.otus.prof.patterns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceTest {

    @Test
    public void testGetInstance(){
        DataSource instance1 = DataSource.getInstance();
        DataSource instance2 = DataSource.getInstance();

        assertNotNull(instance1, "instance must be not null");
        assertSame(instance1, instance2, "instances must be same");
    }

    @Test
    public void testGetConnection () throws SQLException{
        Connection mockConnection = mock(Connection.class);
        mockStatic(DriverManager.class);
        when(DriverManager.getConnection("jdbc:postgresql://localhost:3333/postgres", "postgres", "postgres"))
                .thenReturn(mockConnection);

        DataSource dataSource = DataSource.getInstance();
        Connection connection = dataSource.getConnection();

        assertNotNull(connection, "connection must be not null");
        assertSame(mockConnection, connection, "connection must match mockConnection");

        verify(DriverManager.class, times(1));
        DriverManager.getConnection("jdbc:postgresql://localhost:3333/postgres", "postgres", "postgres");

    }


}
