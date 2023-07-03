package com.enes.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Enes extends AppCompatActivity {

    TextView t2;
    String gelsin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enes);

    t2=(TextView)findViewById(R.id.mailad3);
    Intent gel=getIntent();
    gelsin=gel.getStringExtra("txtKullanici");
    t2.setText(gelsin);


    }
}