package com.example.lab;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    DBHelper dbHelper;

    private Button crAccount;

    ThreadTask threadTask;
    final Looper looper = Looper.getMainLooper();

    final Handler handler = new Handler(looper) {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {

            ArrayList<String> msgList = (ArrayList<String>)msg.obj;

            if (msg.sendingUid == 1) {
                switch (msgList.get(0)){
                    case "FreeFields":
                        Toast.makeText(getApplicationContext(),
                                "Ошибка. Есть незаполненные поля", Toast.LENGTH_SHORT).show();
                        break;
                    case "UserExist":
                        Toast.makeText(getApplicationContext(),
                                "Ошибка. Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                        break;

                    case "Success":
                        Toast.makeText(getApplicationContext(),
                                "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),
                                "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                        break;

                }


            }
            crAccount.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.enter_username);
        password = (EditText) findViewById(R.id.enter_password);
        dbHelper = new DBHelper(this);

        crAccount = (Button) findViewById(R.id.add_account);

        threadTask = new ThreadTask(handler, getApplicationContext());

    }

    public void addAccount(View v){
        crAccount.setEnabled(false);
        String name = username.getText().toString();
        String pass = password.getText().toString();
        threadTask.addAccount(name, pass);

    }

    public void backToAuthorization(View view){
        finish();
    }
}