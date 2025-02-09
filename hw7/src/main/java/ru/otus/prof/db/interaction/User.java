package ru.otus.prof.db.interaction;

@RepositoryTable(title = "users")
public class User {
    @RepositoryIdField(column = "id")
    private Long userId;
    @RepositoryField
    private String login;
    @RepositoryField
    private String password;
    @RepositoryField(columnName = "nickname")
    private String username;

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return username;
    }

    public void setNickname(String nickname) {
        this.username = nickname;
    }

    public User() {
    }

    public User(Long id, String login, String password, String nickname) {
        this.userId = id;
        this.login = login;
        this.password = password;
        this.username = nickname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + username + '\'' +
                '}';
    }
}
