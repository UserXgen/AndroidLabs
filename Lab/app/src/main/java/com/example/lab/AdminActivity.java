package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class AdminActivity extends AppCompatActivity {

    ArrayList<String> accounts = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView accountsList;
    String accountName;
    DBHelper dbHelper;
    Button delAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        dbHelper = new DBHelper(this);

        // добавляем начальные элементы
        Collections.addAll(accounts);

        // получаем элемент ListView
        accountsList = findViewById(R.id.acc_table);
        // создаем адаптер
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, accounts);
        // устанавливаем для списка адаптер
        accountsList.setAdapter(adapter);

        delAccount = (Button) findViewById(R.id.adm_del);

        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delAccount.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        removeAccount();
                        delAccount.post(new Runnable() {
                            @Override
                            public void run() {
                                delAccount.setEnabled(true);
                            }
                        });
                    }
                }).start();
            }
        });

    }

    @Override
    protected void onStop(){
        super.onStop();

    }
    @Override
    protected void onStart(){
        restoreTable();
        super.onStart();

    }
    @Override
    protected void onPause(){
        super.onPause();

    }
    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onRestart(){
        super.onRestart();

    }

    public void removeAccount(){
        EditText accountET = findViewById(R.id.adm_acc_entrie);
        String login  = accountET.getText().toString();
        if(dbHelper.deleteUser(login)) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Пользователь " + login + " успешно удалён", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Ошибка удаления", Toast.LENGTH_SHORT).show();
                }
            });

        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restoreTable();
            }
        });

    }

    private void restoreTable()
    {
        ArrayList<String> list = dbHelper.getDateList();
        adapter.clear();
        accounts.clear();
        for(int i=0; i < list.size(); i = i + 2)
        {
            accounts.add(list.get(i) + " " + list.get(i+1));
        }

    }


}