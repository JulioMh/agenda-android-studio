package com.example.agendadb.agendaext;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.agendadb.entities.Contacts;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Agenda  {

    private List<Contacts> contacts;
    private List<String> contactsInfo;
    private final String[] columns = {
            "name",
            "phone"
    };
    private SQLiteDatabase readableDd;
    private SQLiteDatabase writableDb;

    public Agenda(SQLiteDatabase readable, SQLiteDatabase writable){
        readableDd = readable;
        writableDb = writable;
    }

    public List<String> getContactsList(){
        Contacts contact = null;
        contacts = new ArrayList<>();

        Cursor cursor = readableDd.query("contacts", columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            contact = new Contacts();
            contact.setName(cursor.getString(0));
            contact.setPhoneNumber(cursor.getInt(1));
            contacts.add(contact);
        }
        cursor.close();

        getContactsInfo();

        return contactsInfo;
    }


    public long add(String name, int phone){
        long l = 2;
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        String where = "name = ?";
        String[] whereArgs = { name };
        writableDb.beginTransaction();
        try {
            long cnt = writableDb.update("contacts", values, where, whereArgs);
            l = 1;
            if (cnt == 0) {
                writableDb.insert("contacts", null, values);
                l = 0;
            }
            writableDb.setTransactionSuccessful();
        } finally {
            writableDb.endTransaction();
        }
        return l;
    }

    public List<String> findByName(String name){
        Contacts contact = null;
        contacts = new ArrayList<>();

        String where = "name LIKE ?";
        String[] whereArgs = { name + "%" };
        String sortOrder = "name ASC";
        Cursor cursor = readableDd.query("contacts", columns, where, whereArgs, null, null, sortOrder);
        while (cursor.moveToNext()) {
            contact = new Contacts();
            contact.setName(cursor.getString(0));
            contact.setPhoneNumber(cursor.getInt(1));
            contacts.add(contact);
        }
        cursor.close();

        getContactsInfo();
        return contactsInfo;
    }

    private void getContactsInfo() {
        contactsInfo = new ArrayList<>();
        for(int i = 0; i<contacts.size(); i++){
            contactsInfo.add(
                    "Name: " + contacts.get(i).getName()
                            +"\nPhone: " + contacts.get(i).getPhoneNumber());
        }
    }
}
