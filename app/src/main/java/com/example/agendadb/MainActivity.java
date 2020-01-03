package com.example.agendadb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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
        SQLiteDatabase db = contactsHelper.getReadableDatabase();

        ArrayAdapter adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        agenda.getContactsList(db));
        contacts.setAdapter(adapter);
    }

    private void setList(List<String> newContacts){
        ArrayAdapter adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        newContacts);
        contacts.setAdapter(adapter);

    }

    private void initContactsHelper(){
        contactsHelper = new ContactsHelper(this, "persons_db");
    }

    private void initAgenda() {
        agenda = (Agenda) SingletonMap
                .getInstance()
                .get(MainActivity.SHARED_DATA_KEY);

        if (agenda == null) {
            agenda = new Agenda();
            SingletonMap.getInstance().put(SHARED_DATA_KEY, agenda);
        }
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.add: addClicked(); break;
            case R.id.search: searchClicked(); break;
        }
    }

    private void searchClicked() {
        String writtenName = name.getText().toString().trim();
        SQLiteDatabase db = contactsHelper.getReadableDatabase();

        setList(agenda.findByName(db, writtenName));

        hideSoftKeyboard(name);
    }

    private void addClicked() {
        String writtenName = name.getText().toString().trim();
        SQLiteDatabase db = contactsHelper.getWritableDatabase();

        agenda.add(db, writtenName);

        initList();
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
