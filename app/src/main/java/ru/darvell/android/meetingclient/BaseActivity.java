package ru.darvell.android.meetingclient;

import android.app.Activity;
import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends Activity{
    private SpiceManager spiceManager = new SpiceManager(MeetingSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
