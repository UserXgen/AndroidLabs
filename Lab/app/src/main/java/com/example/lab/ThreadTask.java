package com.example.lab;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ThreadTask {

    Handler thr_handler;
    DBHelper dbHelper;

    public void setActiveThreadCount(int activeThreadCount) {
        this.activeThreadCount = activeThreadCount;
    }

    private int activeThreadCount = 0;

    ThreadTask(Handler main_handler, Context context){
        this.thr_handler = main_handler;
        dbHelper = new DBHelper(context);
    }


    public void login(String name, String pass){
        if(activeThreadCount > 0)
            return;
        activeThreadCount++;

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {

                if(activeThreadCount > 1)
                    return;

                final Message message = Message.obtain();
                ArrayList<String> resultList = new ArrayList<String>();
                message.sendingUid = 1;
                // admin activity
                if(name.equals("admin") && pass.equals("admin")){
                    resultList.add("AdminActivity");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;

                }

                // accounts
                if (dbHelper.checkUsernamePassword(name, pass)) {

                    resultList.add("TableActivity");
                    resultList.add(name);
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;

                }
                // В другом случае выдаем сообщение с ошибкой:
                else {
                    resultList.add("Error");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }

            }
        }).start();

    }

    public void addAccount(String name, String pass){

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {


                final Message message = Message.obtain();
                ArrayList<String> resultList = new ArrayList<String>();
                message.sendingUid = 1;

                if(name.equals("") || pass.equals("")){
                    resultList.add("FreeFields");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }

                if(dbHelper.checkUsername(name)){
                    resultList.add("UserExist");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }

                if(dbHelper.insertData(name, pass)) {

                    resultList.add("Success");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;

                }
                else {
                    resultList.add("Error");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }


            }
        }).start();




    }

    public void changePass(String accountName, String rePass, String pass){

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {


                final Message message = Message.obtain();
                ArrayList<String> resultList = new ArrayList<String>();
                message.sendingUid = 1;

                Log.i("AppLogger", "rePass = " + rePass + " ; pass = " + pass);

                if(rePass.equals("") || pass.equals("")){
                    resultList.add("FreeFields");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }

                if(!dbHelper.checkUsernamePassword(accountName, rePass)){
                    resultList.add("IncorrectPass");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;
                }

                if (dbHelper.changePassword(accountName, rePass, pass)){

                    resultList.add("Success");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;

                } else {

                    resultList.add("Error");
                    message.obj = resultList;
                    thr_handler.sendMessage(message);
                    return;

                }


            }
        }).start();

    }

    public void restoreAdminTable(){

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {


                final Message message = Message.obtain();
                ArrayList<String> resultList = new ArrayList<String>();
                message.sendingUid = 1;

                ArrayList<String> list = dbHelper.getDateList();
                ArrayList<String> accounts = new ArrayList<String>();
                for(int i=0; i < list.size(); i = i + 2)
                {
                    accounts.add(list.get(i) + " " + list.get(i+1));
                }

                message.obj = accounts;
                thr_handler.sendMessage(message);
                return;


            }
        }).start();


    }

    public void removeAccount(String login){

        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {


                final Message message = Message.obtain();
                ArrayList<String> resultList = new ArrayList<String>();
                message.sendingUid = 2;
                if(dbHelper.deleteUser(login)) {

                    resultList.add("DeleteSuccess");
                    resultList.add(login);


                }
                else {
                    resultList.add("Error");

                }

                message.obj = resultList;
                thr_handler.sendMessage(message);
                return;


            }
        }).start();


    }

}

