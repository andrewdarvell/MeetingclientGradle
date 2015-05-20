package ru.darvell.android.meetingclient.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public class DBWorkerImpl implements DBWorker{

    private Context context;

    public DBWorkerImpl(Context context) {
        this.context = context;
    }

    public void putRequest(String result, int type, int act_id ){
        String sql = "INSERT INTO requests(result, type, act_id)" +
                    "VALUES ("+result+","+type+","+act_id+")";
        SQLiteDatabase sqLiteDatabase = getDBHelper();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    public Map<String, String> getRequests(int act_id){
        String sql = "SELECT *" +
                    "FROM requests" +
                    "WHERE act_id = "+act_id;

        Map<String, String> result = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = getDBHelper();
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        while (cursor.moveToNext()){
            result.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
            result.put("result", cursor.getString(cursor.getColumnIndex("result")));
            result.put("type", String.valueOf(cursor.getInt(cursor.getColumnIndex("type"))));
        }
        sqLiteDatabase.close();
        return result;
    }

    public void delRequest(int id){
        String sql = "DELETE FROM requests WHERE id = "+id;
        SQLiteDatabase sqLiteDatabase = getDBHelper();
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.close();
    }

    private SQLiteDatabase getDBHelper(){
        return new MeetingDBHelper(context).getWritableDatabase();
    }
}
