package ru.darvell.android.meetingclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ru.darvell.android.meetingclient.R;
import ru.darvell.android.meetingclient.api.entitys.Member;

import java.util.ArrayList;

public class MembersAdapter extends BaseAdapter{

    ArrayList<Member> members;
    Context ctx;
    LayoutInflater lInflater;

    public MembersAdapter(Context ctx, ArrayList<Member> members){
        this.ctx = ctx;
        this.members = members;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.friends_item, viewGroup, false);
        }

        Member member = getMember(pos);

//        ((TextView) view.findViewById(R.id.scheduleTitle)).setText(schedule.getTitle());
//        ((TextView) view.findViewById(R.id.scheduleComment)).setText(schedule.getComment());
        return view;
    }

    Member getMember(int pos){
        return this.members.get(pos);
    }


}
