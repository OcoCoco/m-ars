package com.example.m_arsemotionai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.nio.channels.FileLock;
import java.util.ArrayList;

public class TailoredInfoActivity extends AppCompatActivity {

    TextView engagementtextView;
    TextView valencetextView;
    TextView nulltextView;
    TextView outputtextView;

    String result;
    Bundle bundle;

    float engagementf;
    float valencef;
//float valencef = bundle.getFloat("valence");
    float noFaceDetectedf;
    float[] arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailored_info);

        bundle = getIntent().getExtras();

        engagementf = bundle.getFloat("engagement");
        valencef = bundle.getFloat("valence");
        noFaceDetectedf = bundle.getFloat("noFaceDetected");
//        arrayList = bundle.getFloatArray("arr");

        engagementtextView = findViewById(R.id.engagementOutput);
        valencetextView = findViewById(R.id.valenceOutput);
        nulltextView = findViewById(R.id.nullOutput);
        outputtextView = findViewById(R.id.output);

        engagementtextView.setText("Engagement Output: " + Float.toString(engagementf));
        valencetextView.setText("Valence Output: " + Float.toString(valencef));
        nulltextView.setText("Null Output: " + Float.toString(noFaceDetectedf));

//        nulltextView.setText("Array Output: " + arrayList);

        emotionOutput();
//        Log.d("TEST", "onCreate: Valence" + valencef);
//        testOutput();
//        Log.d("TEST2", "onCreate: Valence" + valencef);
//
//        if (valencef == 0){
//            Log.d("TEST", "onCreate: Valence" + valencef);
//            outputtextView.setText("test");
//        }

    }

    private void emotionOutput(){
        if(valencef == noFaceDetectedf){
//            result = "Default information will be displayed since no face was not detected";
            outputtextView.setText("Default information will be displayed since valence was not detected");
        } else if (valencef > 0) {
//            result = "Complex information will be displayed here";
            outputtextView.setText("Complex information will be displayed here");
        } else {
//            result = "Simplified text will be displayed here";
            outputtextView.setText("Simplified text will be displayed here");
        }
//        return result;
    }

    private void testOutput() {
        if (valencef > 0) {
//            result = "Complex information will be displayed here";
            outputtextView.setText("Complex information will be displayed here");
        } else {
//            result = "Simplified text will be displayed here";
            outputtextView.setText("Simplified text will be displayed here");
        }
    }

}
