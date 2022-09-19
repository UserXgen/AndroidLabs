package com.example.lab1;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


public class OlegActivity extends Activity {
    private Button button1;
    private RelativeLayout.LayoutParams linnear_lay1;

    private Button button2;
    private RelativeLayout.LayoutParams linnear_lay2;

    private TextView text_view1;

    private int clicks_count1 = 0;
    private int clicks_count2 = 0;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_helloact);

            button1 = findViewById(R.id.button1);
            linnear_lay1 = new RelativeLayout.LayoutParams(button1.getLayoutParams());

            button2 = findViewById(R.id.button2);
            linnear_lay2 = new RelativeLayout.LayoutParams(button2.getLayoutParams());

            text_view1 = findViewById(R.id.text_view1);


            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //button1.setText("НАЖАТО!");
                    click1();
                }

            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click2();
                }

            });
        }

        public void click1(){
            clicks_count1++;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            linnear_lay1.topMargin += 350;
            if(linnear_lay1.topMargin > (metrics.heightPixels - button1.getHeight()))
                linnear_lay1.topMargin = 50;
            button1.setLayoutParams(linnear_lay1);
            text_view1.setText("Кнопки были нажаты " + clicks_count1 + " " + clicks_count2 + " раз(а)");
        }

        public void click2(){
            clicks_count2++;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            linnear_lay2.leftMargin += 350;
            if(linnear_lay2.leftMargin > (metrics.widthPixels - button2.getWidth()))
                linnear_lay2.leftMargin = 50;
            button2.setLayoutParams(linnear_lay2);
            text_view1.setText("Кнопки были нажаты " + clicks_count1 + " " + clicks_count2 + " раз(а)");
        }



}
