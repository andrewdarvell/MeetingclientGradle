package ru.darvell.android.meetingclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ru.darvell.android.meetingclient.R;
import ru.darvell.android.meetingclient.api.Schedule;

import java.util.ArrayList;

public class ScheduleAdapter extends BaseAdapter {

    Context ctx;
    ArrayList<Schedule> schedules;
    LayoutInflater lInflater;

    public ScheduleAdapter(Context ctx, ArrayList<Schedule> schedules){
        this.ctx = ctx;
        this.schedules = schedules;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return schedules.size();
    }

    @Override
    public Object getItem(int i) {
        return schedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.schedulle_item, viewGroup, false);
        }

        Schedule schedule = getSchedule(pos);

        ((TextView) view.findViewById(R.id.scheduleTitle)).setText(schedule.getTitle());
        ((TextView) view.findViewById(R.id.scheduleComment)).setText(schedule.getComment());
        return view;
    }

    Schedule getSchedule(int pos){
        return this.schedules.get(pos);
    }

}
