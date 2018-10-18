package com.example.veysel.myapplication;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech okuyucu;
    private Button okut;
    private EditText metin;
    private TextView sonuc;
    private Button durdur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        okut=(Button)findViewById(R.id.btn);
        metin=(EditText)findViewById(R.id.metin);
        sonuc=(TextView)findViewById(R.id.textView3);
        durdur=(Button)findViewById(R.id.durdur);
        sonuc.setText( "text yok ...");
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent,44);

        okut.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if ((metin.getText().toString() != null)) {
                                            sonuc.setText(metin.getText().toString());
                                          okuyucu.speak(metin.getText().toString(),TextToSpeech.QUEUE_ADD,null);

                                          okuyucu.setLanguage(Locale.getDefault());

                        }


            }
               }
        );
            durdur.setOnClickListener(new OnClickListener() {
              @Override
               public void onClick(View v) {
                     okuyucu.stop();
                                 }
                        });

    }

    @Override
    protected void onActivityResult (int requestCode,int resultCode ,Intent data){
        if(requestCode == 44){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                okuyucu=new TextToSpeech(this , (TextToSpeech.OnInitListener) this);
            }else{
                Intent intent2=new Intent();
                intent2.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
                startActivity(intent2);
            }

        }
    }


    @Override
    public void onInit(int status) {

    }
}
