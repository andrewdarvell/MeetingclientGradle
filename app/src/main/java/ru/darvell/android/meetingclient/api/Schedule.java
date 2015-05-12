package ru.darvell.android.meetingclient.api;

import org.json.JSONObject;

import java.sql.Date;

public class Schedule {

    private long id;
    private Date startDate;
    private Date endDate;
    private String title;
    private String comment;
    private long userId;

    public Schedule() {
    }

    public Schedule(long id, Date startDate, Date endDate, String title, String comment, long userId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.comment = comment;
        this.userId = userId;
    }

    public Schedule(JSONObject jsonObject) throws Exception{
        this.id = jsonObject.getInt("scheduleId");
        this.startDate = new Date(jsonObject.getLong("startDate"));
        this.endDate = new Date(jsonObject.getLong("endDate"));
        this.comment = jsonObject.getString("comment");
        this.title = jsonObject.getString("title");
        this.userId = jsonObject.getInt("userId");

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", userId=" + userId +
                '}';
    }
}
