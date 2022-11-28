package com.example.lab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity {

    // Объявляем об использовании следующих объектов:
    private EditText username;
    private EditText password;
    private TextView attempts;
    private TextView numberOfAttempts;
    private Button loginBttn;

    private SharedPreferences sharedPref;
    private final String saveLoginKey = "save_login";

    DBHelper dbHelper;

    // Число для подсчета попыток залогиниться:
    int numberOfRemainingLoginAttempts = 5;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreLocale();
        setContentView(R.layout.activity_main);

        sharedPref = this.getSharedPreferences("login", Context.MODE_PRIVATE);

        // Связываемся с элементами нашего интерфейса:
        username = (EditText) findViewById(R.id.edit_user);
        password = (EditText) findViewById(R.id.edit_password);
        attempts = (TextView) findViewById(R.id.attempts);
        numberOfAttempts = (TextView) findViewById(R.id.number_of_attempts);
        numberOfAttempts.setText(Integer.toString(numberOfRemainingLoginAttempts));
        dbHelper = new DBHelper(this);
        loginBttn = (Button) findViewById(R.id.button_login);

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBttn.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        login();
                        loginBttn.post(new Runnable() {
                            @Override
                            public void run() {
                                loginBttn.setEnabled(true);
                            }
                        });

                    }
                }).start();
            }
        });


        Toast.makeText(MainActivity.this,
                "Переопределение onCreate у MainActivity", Toast.LENGTH_SHORT).show();
        Log.i("AppLogger", "Переопределение onCreate у MainActivity");


    }

    @Override
    protected void onStop(){
        saveAuthorization();
        super.onStop();
        Log.i("AppLogger", "Переопределение onStop у MainActivity");
    }
    @Override
    protected void onStart(){
        restoreAuthorization();
        super.onStart();


        Log.i("AppLogger", "Переопределение onStart у MainActivity");
    }
    @Override
    protected void onPause(){
        Log.i("AppLogger", "Locale до паузы: " + Locale.getDefault());
        super.onPause();
        Log.i("AppLogger", "Переопределение onPause у MainActivity");
        Log.i("AppLogger", "Locale после паузы: " + Locale.getDefault());
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("AppLogger", "Переопределение onResume у MainActivity");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i("AppLogger", "Переопределение onRestart у MainActivity");
    }

    public void login() {

        // показываем Toast сообщение об успешном входе:
        String name = username.getText().toString();
        String pass = password.getText().toString();

        if(name.equals("admin") && pass.equals("admin")){
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);

            return;
        }

        if (dbHelper.checkUsernamePassword(name, pass)) {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Вход выполнен!",Toast.LENGTH_SHORT).show();
                }
            });

            // Выполняем переход на другой экран:
            Intent intent = new Intent(MainActivity.this,TableActivity.class);
            intent.putExtra("Lab3", name);

            startActivity(intent);
        }

        // В другом случае выдаем сообщение с ошибкой:
        else {

            this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "Неправильные данные!",Toast.LENGTH_SHORT).show();
                }
            });


            numberOfRemainingLoginAttempts--;
            if(numberOfRemainingLoginAttempts == 0)
                finish();

            // Делаем видимыми текстовые поля, указывающие на количество оставшихся попыток:

            attempts.post(new Runnable() {
                @Override
                public void run() {
                    attempts.setVisibility(View.VISIBLE);

                }
            });

            numberOfAttempts.post(new Runnable() {
                @Override
                public void run() {
                    numberOfAttempts.setVisibility(View.VISIBLE);
                    numberOfAttempts.setText(Integer.toString(numberOfRemainingLoginAttempts));

                }
            });

        }
    }

    public void signUp(View view) {

            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);

    }

    private void  saveAuthorization(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(saveLoginKey, username.getText().toString());
        editor.apply();
    }

    private void restoreAuthorization(){
        username.setText(sharedPref.getString(saveLoginKey,""));
    }


    public void changeLocale(View view){
        String lang;
        Locale current = Locale.getDefault();
        if(Locale.getDefault().getLanguage().contains("en"))
            lang = "ru";

        else
            lang = "en";

        Locale myLocale = new Locale(lang);

        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        saveLocale(lang);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    protected void saveLocale(String localeCode){
        Locale current = Locale.getDefault();
        if(current.getLanguage().contains("en"))
            localeCode = "en";
        else
            localeCode = "ru";

        SharedPreferences sharedLocPref = getSharedPreferences("locale", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedLocPref.edit();
        editor.putString("locale_key", localeCode);
        editor.apply();

    }

    protected void restoreLocale(){
        SharedPreferences sharedLocPref = getSharedPreferences("locale", Context.MODE_PRIVATE);
        String lang = sharedLocPref.getString("locale_key", "ru");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
    }

}