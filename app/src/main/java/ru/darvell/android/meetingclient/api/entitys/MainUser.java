package ru.darvell.android.meetingclient.api.entitys;

public class MainUser {

    private Long userId;
    private String login;
    private String pass;
    private String sesionKey;
    private String email;

    public MainUser(Long id, String login, String pass, String sessionKey, String email) {
        this.userId = id;
        this.login = login;
        this.pass = pass;
        this.sesionKey = sessionKey;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSesionKey() {
        return sesionKey;
    }

    public void setSesionKey(String sesionKey) {
        this.sesionKey = sesionKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
