package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.enter_username);
        password = (EditText) findViewById(R.id.enter_password);
        dbHelper = new DBHelper(this);

    }

    public void addAccount(View view){
        String name = username.getText().toString();
        String pass = password.getText().toString();

        if(name.equals("") || pass.equals("")){
            Toast.makeText(this,
                    "Ошибка. Есть незаполненные поля", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dbHelper.checkUsername(name)){
            Toast.makeText(this,
                    "Ошибка. Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dbHelper.insertData(name, pass))
            Toast.makeText(this,
                    "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,
                    "Ошибка регистрации", Toast.LENGTH_SHORT).show();


    }

    public void backToAuthorization(View view){
        finish();
    }
}