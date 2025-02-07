package ru.otus.prof.db.interaction;

public class DbMigrator {
    private DataSource dataSource;

    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    public void migrate() {
        // читаем файл dbinit.sql
        // выполняем все запросы, чтобы проинициализировать БД
    }
}
