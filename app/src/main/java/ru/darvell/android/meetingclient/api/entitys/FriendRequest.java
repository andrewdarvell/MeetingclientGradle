package ru.darvell.android.meetingclient.api.entitys;

public class FriendRequest {

    long id;
    Member fromMember;
    Member toMember;

    public FriendRequest() {
    }

    public FriendRequest(long id, Member fromMember, Member toMember) {
        this.id = id;
        this.fromMember = fromMember;
        this.toMember = toMember;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Member getFromMember() {
        return fromMember;
    }

    public void setFromMember(Member fromMember) {
        this.fromMember = fromMember;
    }

    public Member getToMember() {
        return toMember;
    }

    public void setToMember(Member toMember) {
        this.toMember = toMember;
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "id=" + id +
                ", fromMember=" + fromMember +
                ", toMember=" + toMember +
                '}';
    }
}
