package ru.otus.prof.db.interaction;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private DataSource dataSource;
    private Class cls;
    private PreparedStatement psInsert;
    private PreparedStatement psSelect;
    private PreparedStatement psSelectAll;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;
    private List<CustomField> cachedFields;
    private CustomField idField;
    private String tableName;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        this.cls = cls;
        prepareQueries();
    }

    public void save(T entity) {
        try {
            for (int i = 0; i < cachedFields.size(); i++) {
                psInsert.setObject(i + 1, cachedFields.get(i).getField().get(entity));
            }
            psInsert.executeUpdate();
        } catch (Exception e) {
            throw new ORMException("Что-то пошло не так при сохранении: " + entity);
        }
    }

    public T findById(int id) {
        try {
            psSelect.setObject(1, id);
            ResultSet rs = psSelect.executeQuery();
            if (rs.next()) {
                T entity = (T) cls.getDeclaredConstructor().newInstance();
                idField.getField().set(entity, rs.getObject(idField.getName()));
                for (CustomField field : cachedFields) {
                    field.getField().set(entity, rs.getObject(field.getName()));
                }
                return entity;
            } else {
                System.out.println("record not found");
            }
        } catch (Exception e) {
            throw new ORMException("Error on select query");
        }
        return null;
    }

    public List<T> findAll() {
        List<T> result = new ArrayList<>();
        try {
            ResultSet rs = psSelectAll.executeQuery();
            while (rs.next()) {
                T entity = (T) cls.getDeclaredConstructor().newInstance();
                idField.getField().set(entity, rs.getObject(idField.getName()));
                for (CustomField field : cachedFields) {
                    field.getField().set(entity, rs.getObject(field.getName()));
                }
                result.add(entity);
            }
        } catch (Exception e) {
            throw new ORMException("Error on select query");
        }
        return result;
    }

    public void update(T entity) {
        try {
            for (int i = 0; i < cachedFields.size(); i++) {
                psUpdate.setObject(i + 1, cachedFields.get(i).getField().get(entity));
            }
            psUpdate.setObject(cachedFields.size() + 1, idField.getField().get(entity));
            psUpdate.executeUpdate();
        } catch (Exception e) {
            throw new ORMException("Error on update query");
        }
    }

    public void deleteById(int id) {
        try {
            psDelete.setInt(1, id);
            psDelete.execute();
        } catch (Exception e) {
            throw new ORMException("Error on delete query");
        }
    }

    private void prepareQueries() {
        if (!cls.isAnnotationPresent(RepositoryTable.class)) {
            throw new ORMException("Класс не предназначен для создания репозитория, не хватает аннотации @RepositoryTable");
        }
        tableName = ((RepositoryTable) cls.getAnnotation(RepositoryTable.class)).title();

        List<Field> repositoryIdFields = Arrays.stream(cls.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(RepositoryIdField.class))
                .toList();
        if (repositoryIdFields.size() == 1) {
            idField = new CustomField(repositoryIdFields.get(0));
        } else {
            throw new ORMException("Multiple @RepositoryIdField");
        }

        cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .map(CustomField::new)
                .collect(Collectors.toList());

        if (cachedFields.isEmpty()) {
            throw new ORMException("No @RepositoryField fields");
        }

        for (CustomField f : cachedFields) { // TODO заменить на использование геттеров
            f.getField().setAccessible(true);
        }
        idField.getField().setAccessible(true);


        if (dataSource.getConnection() == null) {
            throw new ORMException("No database connection");
        }

        try {
            psInsert = dataSource.getConnection().prepareStatement(prepareInsert());
            psSelect = dataSource.getConnection().prepareStatement(prepareSelect());
            psSelectAll = dataSource.getConnection().prepareStatement(prepareSelectAll());
            psUpdate = dataSource.getConnection().prepareStatement(prepareUpdate());
            psDelete = dataSource.getConnection().prepareStatement(prepareDelete());
        } catch (SQLException e) {
            throw new ORMException("Не удалось проинициализировать репозиторий для класса " + cls.getName());
        }

    }

    private String prepareInsert() {
        StringBuilder insertQuery = new StringBuilder("insert into ");
        insertQuery.append(tableName).append(" (");  // 'insert into users ('
        for (CustomField f : cachedFields) {
            insertQuery.append(f.getName()).append(", ");
        }
        // 'insert into users (login, password, nickname, '
        insertQuery.setLength(insertQuery.length() - 2);
        insertQuery.append(") values (");
        // 'insert into users (login, password, nickname) values ('
        for (CustomField f : cachedFields) {
            insertQuery.append("?, ");
        }
        insertQuery.setLength(insertQuery.length() - 2);
        insertQuery.append(");");
        // 'insert into users (login, password, nickname) values (?, ?, ?);'

        return insertQuery.toString();
    }

    private String prepareSelect() {
        return "SELECT * FROM " + tableName + " WHERE " + idField.getName() + " = ?;";
    }

    private String prepareSelectAll() {
        return "SELECT * FROM " + tableName + ";";
    }


    private String prepareUpdate() {
        StringBuilder updateQuery = new StringBuilder("UPDATE " + tableName + " SET ");
        for (CustomField f : cachedFields) {
            updateQuery.append(f.getName()).append(" = ?, ");
        }
        updateQuery.setLength(updateQuery.length() - 2);
        updateQuery.append(" WHERE ").append(idField.getName()).append(" = ?;");
        return updateQuery.toString();
    }

    private String prepareDelete() {
        return "DELETE FROM " + tableName + " WHERE " + idField.getName() + " = ?;";
    }

}
