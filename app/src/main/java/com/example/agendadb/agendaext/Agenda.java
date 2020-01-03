package com.example.agendadb.agendaext;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.agendadb.entities.Contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Agenda  {

    private List<Contacts> contacts;
    private List<String> contactsInfo;
    private final String[] columns = {
            "name",
            "phone"
    };

    public Agenda(){
    }

    public List<String> getContactsList(SQLiteDatabase db){
        Contacts contact = null;
        contacts = new ArrayList<>();

        Cursor cursor = db.query("contacts", columns, null, null, null, null, null);

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

    public void add(SQLiteDatabase db, String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        db.insert("contacts", null, contentValues);
    }

    public void modify(SQLiteDatabase db, String name, int phone){
        
    }

    public List<String> findByName(SQLiteDatabase db, String name){
        Contacts contact = null;
        contacts = new ArrayList<>();

        String where = "name LIKE ?";
        String[] whereArgs = { name + "%" };
        String sortOrder = "name ASC";
        Cursor cursor = db.query("contacts", columns, where, whereArgs, null, null, sortOrder);
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
                            +"\n Phone: " +
                            (contacts.get(i).getPhoneNumber()== 0 ? "Not found" : contacts.get(i).getPhoneNumber()));
        }
    }
}
