package com.alex.myproj;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 08/06/2016.
 */


public class PersonSql extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    public PersonSql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "table ");
        String query;
        query = "create table if not exists Person(pers text, level text, record long)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addNewPerson(PersonData pd) {//add person

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cn = new ContentValues();
        cn.put("pers", pd.getPerson()); //name
        cn.put("level", pd.getLevel());//level
        cn.put("record", pd.getTimeRecord());//time (in Milliseconds).


        db.insert("Person", null, cn);
        db.close();
//        Log.d(LOG_TAG, "add " + pd.getPerson().toString() + " "+pd.getLevel()+" "+pd.getTimeRecord()+" "+pd.getPlayer());
    }

    public List<PersonData> getAllPerson() {//show record table
        SQLiteDatabase db = getWritableDatabase();
        List<PersonData> listPD = new ArrayList<PersonData>();
        String query;
        query = "select * from Person";
        Cursor cr = db.rawQuery(query, null);
        if (cr.moveToFirst()) {
            do {
                PersonData pd;
                String name;
                String level;
                long record;

                name = cr.getString(0);
                level = cr.getString(1);
                record = cr.getLong(2);
                pd = new PersonData(name, level, record);
                listPD.add(pd);

            } while (cr.moveToNext());

        }
        return listPD;
    }


    public List<PersonData> getRecords(String _level) {//get records for this level.first is hide.not need be more from one.
        SQLiteDatabase db = getReadableDatabase();
        List<PersonData> listPD = new ArrayList<PersonData>();

        String query = "select * from Person where level='"+_level+"'";

        Cursor cr = db.rawQuery(query, null);

        if (cr.moveToFirst()) {
            do {
                PersonData pd;
                String name;
                String level;
                long record;
                String play;

                name = cr.getString(0);
                level = cr.getString(1);
                record = cr.getLong(2);
                pd = new PersonData(name, level, record);
                listPD.add(pd);

            } while (cr.moveToNext());
        }

        return listPD;
    }


    public void updatePlay() { //update 0 if not play now and 1 if play!
        SQLiteDatabase db = getWritableDatabase();
        List<PersonData> listPD = new ArrayList<PersonData>();
        String query;
        query = "update Person set play=0 where play=1";
        Cursor cr = db.rawQuery(query, null);
        if (cr.moveToFirst()) {
            do {
                PersonData pd;

                String pers;
                String level;
                long record;
                String play;

                pers = cr.getString(0);
                level = cr.getString(1);
                record = cr.getLong(2);
                pd = new PersonData(pers, level, record);
                listPD.add(pd);

            } while (cr.moveToNext());
        }
    }


    //delete person
    public void delPerson(String _level, long _record) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "delete from Person where level = '"+_level+"' and record > '"+_record+"'";
        db.execSQL(query);
    }

}
