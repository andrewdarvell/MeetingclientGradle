package ru.darvell.android.meetingclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ru.darvell.android.meetingclient.R;
import ru.darvell.android.meetingclient.api.entitys.FriendRequest;
import ru.darvell.android.meetingclient.api.entitys.Member;

import java.util.ArrayList;


public class FriendRequestsAdapter extends BaseAdapter {

    ArrayList<FriendRequest> friendRequests;
    Context context;
    LayoutInflater inflater;

    public FriendRequestsAdapter(Context ctx, ArrayList<FriendRequest> friendRequests){
        this.context = ctx;
        this.friendRequests = friendRequests;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return friendRequests.size();
    }

    @Override
    public Object getItem(int i) {
        return friendRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.friends_item, viewGroup, false);
        }
        FriendRequest friendRequest = getFriendRequest(i);
        Member from = friendRequest.getFromMember();
        Member to = friendRequest.getToMember();
        if (from != null){
            ((TextView) view.findViewById(R.id.friendLogin)).setText(from.getLogin());
        }else{
            ((TextView) view.findViewById(R.id.friendLogin)).setText(to.getLogin());
        }
        return view;
    }

    FriendRequest getFriendRequest(int pos){
        return this.friendRequests.get(pos);
    }
}
