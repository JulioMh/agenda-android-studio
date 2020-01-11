package com.example.agendadb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void addClicked() {
        String writtenName = name.getText().toString().trim();
        String writtenPhone = phone.getText().toString();
        if(writtenName.equals("") ||writtenPhone.equals("")){
            errorDialog();
        }else{
            long res = agenda.add(writtenName, Integer.parseInt(writtenPhone));
            if(res==0){
                Toast.makeText(this, "Contact Successfully Created", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard(name);
                backToMain();
            }else if(res==2){
                errorDialog();
            }else if(res==1){
                Toast.makeText(this, "Contact Updated", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard(name);
                backToMain();
            }

        }
    }

    private void errorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Data Error")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
