package ru.otus.prof.db.interaction;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DbMigrator {
    private DataSource dataSource;
    private String migrationScriptsDirectory = "hw7/src/main/resources/";

    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void migrate() throws SQLException {
        createMigrationTable();
        checkAndExecute(getAppliedMigrations(), getMigrationScripts());
    }

    private void createMigrationTable() throws SQLException {
        dataSource.getStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS migration_history (" +
                        "id bigserial PRIMARY KEY," +
                        "query VARCHAR(255) UNIQUE NOT NULL, " +
                        "applied_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP);"
        );
    }

    private List<String> getAppliedMigrations() throws SQLException {
        List<String> migrationList = new ArrayList<>();
        ResultSet rs = dataSource.getStatement().executeQuery("SELECT query FROM migration_history;");
        while (rs.next()) {
            migrationList.add(rs.getString("query"));
        }
        return migrationList;
    }

    private List<Path> getMigrationFiles() {
        Path path = Paths.get(migrationScriptsDirectory);
        List<Path> fileList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.sql")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    fileList.add(entry);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return fileList;
    }

    private void checkAndExecute(List<String> appliedMigrations, List<String> migrationScripts) throws SQLException {
        for (String query : migrationScripts){
            if (!appliedMigrations.contains(query)) {
                executeMigrationScript(query);
                markMigration(query);
            } else {
                System.out.println(String.format("Script: \" %s \" already applied", query));
            }
        }

    }

    private void executeMigrationScript(String query) throws SQLException {
        dataSource.getStatement().executeUpdate(query);
        System.out.println(String.format("Script: \" %s \" executed", query));
    }

    private List<String> getMigrationScripts() {
        List<Path> migrationFiles = getMigrationFiles();
        StringBuilder queryBuilder = new StringBuilder();
        for (Path path : migrationFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
                String str;
                while ((str = reader.readLine()) != null) {
                    queryBuilder.append(str);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return new ArrayList<>(Arrays.asList(queryBuilder.toString().split(";")));
    }

    private void markMigration(String query) throws SQLException {
        dataSource.getStatement().execute("INSERT INTO migration_history (query) VALUES ('" + query + "');");
        System.out.println(String.format("Script: \" %s \" marked as applied", query));
    }
}
