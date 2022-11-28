package com.example.lab;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class TableActivity extends MainActivity {

    ArrayList<String> entries = new ArrayList<String>();
    ArrayList<String> selectedEntries = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView entriesList;

    private SharedPreferences sharedPref;
    private final String saveTableKey = "save_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreLocale();
        setContentView(R.layout.activity_table);

        sharedPref = this.getSharedPreferences("table", Context.MODE_PRIVATE);


        // добавляем начальные элементы
        Collections.addAll(entries);

        Log.i("AppLogger", "Переопределение onCreate у TableActivity");
        String accountName = getIntent().getExtras().getString("Lab3");
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(accountName);

        // получаем элемент ListView
        entriesList = findViewById(R.id.entriesList);
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

}