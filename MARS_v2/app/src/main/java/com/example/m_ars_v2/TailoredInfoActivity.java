package com.example.m_ars_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class TailoredInfoActivity extends AppCompatActivity {

    TextView engagementtextView;
    TextView valencetextView;
    TextView outputtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailored_info);

        Bundle bundle = getIntent().getExtras();
//        float anger = bundle.getFloat("anger");
        float engagementf = bundle.getFloat("engagement");
        float valencef = bundle.getFloat("valence");

        engagementtextView = findViewById(R.id.engagementOutput);
        valencetextView = findViewById(R.id.valenceOutput);
        outputtextView = findViewById(R.id.output);

        engagementtextView.setText("Engagement Output: " + Float.toString(engagementf));
        valencetextView.setText("Valence Output: " + Float.toString(valencef));

        if (valencef > 0) {
            outputtextView.setText("Complex information will be displayed here");
        } else {
            outputtextView.setText("Simplified text will be displayed here");
        }

//        ArrayList<Float> emotionOutput = new ArrayList<>();
//        emotionOutput.add(anger);
    }
}
