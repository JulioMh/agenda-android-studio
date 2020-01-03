package com.example.agendadb.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Agenda  {

    static private Agenda agenda = null;
    private Map<String, Integer> contactos;

    private Agenda(){
    }

    public static Agenda getAgenda(){
        return agenda==null ? new Agenda() : agenda;
    }

    public void add(SQLiteDatabase db, String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.insert("person", null, contentValues);
        //Setear vista
    }

    public void findByName(SQLiteDatabase db, String name){
        List<Pair<String,String>> list = new ArrayList<Pair<String,String>>();
        String[] columns = {
                "name",
                "phone"
        };
        String where = "name LIKE ?";
        String[] whereArgs = { name + "%" };
        String sortOrder = "name ASC";
        Cursor cursor = db.query("person", columns, where, whereArgs, null, null, sortOrder);
        try {
            while (cursor.moveToNext()) {
                String clave = cursor.getString(cursor.getColumnIndex("name"));
                String valor = cursor.getString(cursor.getColumnIndex("phone"));
                list.add(new Pair<String,String>(clave, valor));
            }
        } finally {
            cursor.close();
        }
        //Setear vista
    }

}
