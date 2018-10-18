package com.example.veysel.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class giris extends AppCompatActivity {
    private Button resimOkut;

    private Button metinOkut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        resimOkut=(Button)findViewById(R.id.resimOkut);

        metinOkut=(Button)findViewById(R.id.metinOkut);


        resimOkut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(giris.this, reimOkuyucu.class);
                startActivity(a);
            }
        });

        metinOkut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(giris.this,MainActivity.class);
                startActivity(a);
            }
        });



    }
}
