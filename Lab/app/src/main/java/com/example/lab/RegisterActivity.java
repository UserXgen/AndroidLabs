package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    DBHelper dbHelper;

    private Button crAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.enter_username);
        password = (EditText) findViewById(R.id.enter_password);
        dbHelper = new DBHelper(this);

        crAccount = (Button) findViewById(R.id.add_account);

        crAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crAccount.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addAccount();
                        crAccount.post(new Runnable() {
                            @Override
                            public void run() {
                                crAccount.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });

    }

    public void addAccount(){
        String name = username.getText().toString();
        String pass = password.getText().toString();

        if(name.equals("") || pass.equals("")){
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка. Есть незаполненные поля", Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        if(dbHelper.checkUsername(name)){
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка. Такой пользователь уже существует", Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }

        if(dbHelper.insertData(name, pass)) {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                }
            });

        }


    }

    public void backToAuthorization(View view){
        finish();
    }
}