package com.example.agendadb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.agendadb.agendaext.Agenda;
import com.example.agendadb.agendaext.SingletonMap;

public class AddContactActivity extends AppCompatActivity {

    private EditText name;
    private EditText phone;

    private Agenda agenda;

    public void onClick(View view){
        switch (view.getId()){
            case R.id.add: addClicked(); break;
            case R.id.back: backToMain(); break;
        }
    }

    private void initAgenda() {
        agenda = (Agenda) SingletonMap
                .getInstance()
                .get(MainActivity.SHARED_DATA_KEY);
    }

    private void backToMain(){
        finish();
    }


    private void addClicked() {
        String writtenName = name.getText().toString().trim();
        int writtenPhone = Integer.parseInt(phone.getText().toString());

        agenda.add(writtenName, writtenPhone);
        hideSoftKeyboard(name);
        backToMain();
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
        setContentView(R.layout.activity_add_conctact);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        initAgenda();
    }
}
