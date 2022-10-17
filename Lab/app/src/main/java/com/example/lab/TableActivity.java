package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

public class TableActivity extends AppCompatActivity {

    ArrayList<String> entries = new ArrayList<String>();
    ArrayList<String> selectedEntries = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView entriesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // добавляем начальные элементы
        Collections.addAll(entries);
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
}