package ru.otus.prof.db.interaction;

public class BonusService {
    private DataSource dataSource;

    public BonusService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createBonus(String login, int amount) {
        // dataSource.getStatement()...
    }
}
