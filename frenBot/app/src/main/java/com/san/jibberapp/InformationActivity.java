package com.san.jibberapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class InformationActivity extends AppCompatActivity {

    EditText bot_name;
    ImageView iv_back;
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        bot_name = findViewById(R.id.bot_name);
        iv_back = findViewById(R.id.iv_back);
        sharedPreference = new SharedPreference(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("bot_name.getText().toString() = " + bot_name.getText().toString());
                sharedPreference.saveName(bot_name.getText().toString());

                startActivity(new Intent(InformationActivity.this,MainActivity.class));
                finish();

                //shared pref
            }
        });
    }
}