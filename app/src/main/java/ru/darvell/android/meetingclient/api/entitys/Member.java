package ru.darvell.android.meetingclient.api.entitys;

import org.json.JSONObject;

public class Member {

    private long id;
    private String login;
    private String email;

    public Member(Long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public Member(JSONObject member) throws Exception{
        id = member.getInt("id");
        login = member.getString("login");
        email = member.getString("email");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
