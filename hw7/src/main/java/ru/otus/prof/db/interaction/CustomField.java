package ru.otus.prof.db.interaction;

import java.lang.reflect.Field;

public class CustomField {
    private final Field field;
    private final String name;

    public CustomField(Field field) {
        this.field = field;
        this.field.setAccessible(true);
        this.name = extractName(field);

    }

    private String extractName(Field field){
        RepositoryField repositoryField = field.getAnnotation(RepositoryField.class);
        if (repositoryField != null && !repositoryField.columnName().isEmpty()) {
            return repositoryField.columnName();
        }

        RepositoryIdField repositoryIdField = field.getAnnotation(RepositoryIdField.class);
        if (repositoryIdField != null && !repositoryIdField.column().isEmpty()) {
            return repositoryIdField.column();
        }
        return field.getName();
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }
}
