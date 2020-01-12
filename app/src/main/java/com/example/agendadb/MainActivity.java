package com.example.agendadb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.agendadb.agendaext.Agenda;
import com.example.agendadb.agendaext.SingletonMap;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    static final String SHARED_DATA_KEY = " SHARED_MAP_KEY ";

    private EditText name;
    private ListView contacts;
    ContactsHelper contactsHelper;

    private Agenda agenda;

    private void initList(){

        ArrayAdapter adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        agenda.getContactsList());
        contacts.setAdapter(adapter);
    }

    private void setList(List<String> newContacts){
        ArrayAdapter adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        newContacts);
        contacts.setAdapter(adapter);
    }

    private void initContactsHelper(){
        contactsHelper = new ContactsHelper(this, "persons_db");
    }

    private void initAgenda() {
        SQLiteDatabase readable = contactsHelper.getReadableDatabase();
        SQLiteDatabase writable = contactsHelper.getWritableDatabase();


        agenda = (Agenda) SingletonMap
                .getInstance()
                .get(MainActivity.SHARED_DATA_KEY);

        if (agenda == null) {
            agenda = new Agenda(readable, writable);
            SingletonMap.getInstance().put(SHARED_DATA_KEY, agenda);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.add: addNewContact(); break;
            case R.id.search: searchClicked(); break;
        }
    }

    private void addNewContact(){
        Intent intent = new Intent(this, AddContactActivity.class);
        startActivity(intent);
    }

    private void searchClicked() {
        String writtenName = name.getText().toString().trim();
        List<String> contacts = agenda.findByName(writtenName);
        if(contacts.size()==0){
            Toast.makeText(this, R.string.notFound, Toast.LENGTH_SHORT).show();
        }
        setList(contacts);
        hideSoftKeyboard(name);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        name = findViewById(R.id.name);
        contacts = findViewById(R.id.listContacts);

        initContactsHelper();
        initAgenda();
        initList();
    }
}
