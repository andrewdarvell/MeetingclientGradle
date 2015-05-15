package ru.darvell.android.meetingclient.api;

public class RequestResult {

    String json;
    String code;

    public RequestResult(String json, String code) {
        this.json = json;
        this.code = code;
    }
}
