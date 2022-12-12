package com.example.lab;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class TableActivity extends AppCompatActivity {

    ArrayList<String> entries = new ArrayList<String>();
    ArrayList<String> selectedEntries = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView entriesList;
    String accountName;
    DBHelper dbHelper;
    Button changePass;

    private SharedPreferences sharedPref;
    private final String saveTableKey = "save_table";

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
                    case "IncorrectPass":
                        Toast.makeText(getApplicationContext(),
                                "Ошибка. Неправильный пароль.", Toast.LENGTH_SHORT).show();
                        break;

                    case "Success":
                        Toast.makeText(getApplicationContext(),
                                "Пароль успешно изменён", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(getApplicationContext(),
                                "Ошибка при изменении пароля", Toast.LENGTH_SHORT).show();
                        break;

                }
                changePass.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        dbHelper = new DBHelper(this);
        threadTask = new ThreadTask(handler, getApplicationContext());

        // добавляем начальные элементы
        Collections.addAll(entries);

        Log.i("AppLogger", "Переопределение onCreate у TableActivity");
        accountName = getIntent().getExtras().getString("Lab3");
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(accountName);
        sharedPref = this.getSharedPreferences(accountName, Context.MODE_PRIVATE);

        // получаем элемент ListView
        entriesList = findViewById(R.id.acc_table);
        // создаем адаптер
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, entries);
        // устанавливаем для списка адаптер
        entriesList.setAdapter(adapter);

        // обработка установки и снятия отметки в списке
        entriesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // получаем нажатый элемент
                String entrie = adapter.getItem(position);
                if(entriesList.isItemChecked(position))
                    selectedEntries.add(entrie);
                else
                    selectedEntries.remove(entrie);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.table_menu, menu);
        return true;
    }

    @Override
    protected void onStop(){
        saveTable();
        super.onStop();
        Log.i("AppLogger", "Переопределение onStop у TableActivity");
    }
    @Override
    protected void onStart(){
        restoreTable();
        super.onStart();
        Log.i("AppLogger", "Переопределение onStart у TableActivity");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i("AppLogger", "Переопределение onPause у TableActivity");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("AppLogger", "Переопределение onResume у TableActivity");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("AppLogger", "Переопределение onRestart у TableActivity");
    }

    public void addEntrie(View view){

        EditText entrieET = findViewById(R.id.entrie);
        String entrie  = entrieET.getText().toString();
        if(!entrie.isEmpty()){
            adapter.add(entrie);
            entrieET.setText("");
            adapter.notifyDataSetChanged();
        }
    }
    public void removeEntrie(View view){
        // получаем и удаляем выделенные элементы

        if(selectedEntries.size() == 0 && entries.size() != 0) {
            int numOfLastEntrie = entries.size() - 1;
            adapter.remove(entries.get(numOfLastEntrie));
        }

        for(int i=0; i < selectedEntries.size();i++){
            adapter.remove(selectedEntries.get(i));
        }
        // снимаем все ранее установленные отметки
        entriesList.clearChoices();
        // очищаем массив выбраных объектов
        selectedEntries.clear();

        adapter.notifyDataSetChanged();
    }

    private void saveTable(){
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("table_size", entries.size());
        for(int i=0;i < entries.size(); i++)
        {
            editor.remove("table_ent_" + i);
            editor.putString("table_ent_" + i, entries.get(i));
        }

        editor.apply();
    }

    private void restoreTable()
    {
        entries.clear();
        int size = sharedPref.getInt("table_size", 0);
        for(int i=0;i < size;i++)
        {
            entries.add(sharedPref.getString("table_ent_" + i, null));
        }

    }

    public void showChangePasswordDialog(MenuItem menuItem){

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_table);

        final EditText rePassword = dialog.findViewById(R.id.repass);
        final EditText newPassword = dialog.findViewById(R.id.new_pass);
        Button changePass = dialog.findViewById(R.id.change_pass_bttn);
        this.changePass = changePass;
        Button exitDialog = dialog.findViewById(R.id.ch_pass_close_dialog);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass.setEnabled(false);
                String repass = rePassword.getText().toString();
                String pass = newPassword.getText().toString();
                threadTask.changePass(accountName, repass, pass);

            }
        });

        exitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout((6 * width)/7, height/2);
        dialog.show();
    }

}