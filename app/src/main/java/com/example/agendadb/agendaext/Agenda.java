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

    public void add(String name, int phone){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        writableDb.insert("contacts", null, contentValues);
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
                            +"\nPhone: " +
                            (contacts.get(i).getPhoneNumber()== 0 ? "Not found" : contacts.get(i).getPhoneNumber()));
        }
    }
}
