package com.example.agendadb;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.agendadb.repositories.Agenda;

import java.util.SortedMap;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private SQLiteDatabase dbWritable;
    private SQLiteDatabase dbReadable;
    private Agenda agenda;
    private SortedMap<String, String> map;

    public void init(){
        PersonsDbHelper psHelper = new PersonsDbHelper(this, "persons_db");
        dbWritable = psHelper.getWritableDatabase();
        dbReadable = psHelper.getReadableDatabase();
        agenda = Agenda.getAgenda();
        map = (SortedMap <String , String >) Agenda
                .getAgenda();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.add: addClicked(); break;
            case R.id.buscar: searchClicked(); break;
        }
    }

    private void searchClicked() {
        String writtenName = name.getText().toString().trim().toLowerCase();

        agenda.findByName(dbReadable, writtenName);

    }

    private void addClicked() {
        String writtenName = name.getText().toString().trim().toLowerCase();
    }


}
