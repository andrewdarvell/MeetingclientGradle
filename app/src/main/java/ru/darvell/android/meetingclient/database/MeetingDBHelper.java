package ru.darvell.android.meetingclient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MeetingDBHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "meetingDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public MeetingDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "CREATE TABLE requests(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", result VARCHAR NOT NULL" +
                ", type INTEGER NOT NULL" +
                ", act_id INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
