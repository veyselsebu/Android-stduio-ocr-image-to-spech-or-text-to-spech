package com.example.veysel.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Locale;

public class reimOkuyucu extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private Button okut;
    private Button durdur;
    private SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int requestPermissionID = 1001;
    private TextToSpeech okuyucu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reim_okuyucu);
        cameraView=(SurfaceView)findViewById(R.id.surfaceView);
        textView=(TextView)findViewById(R.id.text_view);
        okut=(Button)findViewById(R.id.okut);
        durdur=(Button)findViewById(R.id.durdur);
        startCameraSource();
        Intent intent = new Intent();
        intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent,44);

        okut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String metin = textView.getText().toString();

                if ((metin != null)) {

                    okuyucu.speak(metin, TextToSpeech.QUEUE_ADD, null);
                    okuyucu.setLanguage(Locale.getDefault());
                }

            }
        });

        durdur.setOnClickListener(new View.OnClickListener() {
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

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {

        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(reimOkuyucu.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0 ){

                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i=0;i<items.size();i++){
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                textView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }

}
