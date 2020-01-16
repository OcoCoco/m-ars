package com.example.m_arsemotionai;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultInfoTesting extends Activity implements Detector.ImageListener, CameraDetector.CameraEventListener {

    final String LOG_TAG = "CameraDetectorDemo";

    Button moreButton;
    Button toggleButton;
    TextView engagement_textview;
    TextView valence_textview;
    SurfaceView cameraPreview;
    Face face;
    RelativeLayout mainLayout;
    CameraDetector detector;

    private boolean hasSDKStarted = true;
    private int previewWidth = 0;
    private int previewHeight = 0;
    private float noFaceDetected;

    //    float[] arr;
    ArrayList<Float> arr = new ArrayList<>();
    private static  final String FILE_NAME = "example.txt";
    FileOutputStream fileOutputStream = null;
    String arrOutput;
    int arrSize;
    float rndv = 0;
    float eValue;
    float vValue;
    float ee;
    float vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_info);

        engagement_textview = findViewById(R.id.engagement_textview);
        valence_textview = findViewById(R.id.valence_textview);

        ee = eValue;
        vv = vValue;

        //this is the read more button
        moreButton = findViewById(R.id.sdk_start_button);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSDKStarted) {
                    hasSDKStarted = false;
                    stopDetector();
                    Intent intent = new Intent(DefaultInfoTesting.this, TailoredInfoActivity.class);
                    intent.putExtra("noFaceDetected", noFaceDetected);
                    intent.putExtra("engagement", getEngagement());
                    intent.putExtra("valence", getValence());
//                    intent.putExtra("arr", arr);
                    startActivity(intent);
                } else {
                    hasSDKStarted = true;
                    startDetector();
                }
            }
        });

        surfaceView();
        faceDetector();
        float t = 0;

//        while(hasSDKStarted) {
////            e = getEngagement();
////            va = getValence();
////            noFaceDetected = 0;
////            engagement_textview.setText(String.format("engagement\n%.2f", getEngagement()));
////            valence_textview.setText(String.format("Valence\n%.2f", getValence()));
//            t++;
//            arr.add(t);
//            arr.add(getValence());
//            arrOutput = arr.toString();
//            arrSize = arr.size();
//            Log.d("STATE", arr.toString());
//        }

        for(int i = 0; i < 20; i++){
            t++;
            arr.add(t);
            arr.add(getValence());
            arrOutput = arr.toString();
            arrSize = arr.size();
//            Log.d("STATE", arr.toString());
        }
        Log.d("STATE", arr.toString());
//            String a = Integer.toString(arrSize);
//            Log.d("STATE2", String.valueOf(arr.get(arrSize - 1)));

//            try {
//                fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
//                fileOutputStream.write(arrOutput.getBytes());
//
//                Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fileOutputStream != null) {
//                        try {
//                            fileOutputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        }

//        Log.d("STATE", "SDK has started");

//        try {
//            fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE);
//            fileOutputStream.write(arrOutput.getBytes());
//
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


    } // End of onCreate method

    private void surfaceView(){
        //Add into surfaceView method
        // a surface view is required for the SDK to work
        mainLayout = findViewById(R.id.main_layout);
        cameraPreview = new SurfaceView(this) {
            @Override
            public void onMeasure(int widthSpec, int heightSpec) {
                int w = 1;
                int h = 1;
                setMeasuredDimension(w, h);
                cameraPreview.setAlpha(0);
            }
        };
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        cameraPreview.setLayoutParams(params);
        mainLayout.addView(cameraPreview,0);
    }

    private void faceDetector(){
        // Add into faceDetector method
        detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview);
        detector.setDetectAttention(true);
        detector.setDetectAllEmotions(true);
        detector.setImageListener(this);
        detector.setOnCameraEventListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasSDKStarted) {
            startDetector();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDetector();
    }

    private void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    private void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    private int rRange(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    private float randomGenerator() {
        float rfloat;
//        Random r = new Random();
//            rfloat = (r.nextFloat() * 100) + (-100);

        rfloat = rRange(0, 100);
        return  rfloat;
    }

    private float getEngagement(){
        float engagement;
        if (noFaceDetected == 0){
//            engagement = randomGenerator();
            engagement = 0;
        } else {
//            engagement = randomGenerator();
            engagement = face.emotions.getEngagement();
        }
        return engagement;
    }

    private float getValence(){
        float valence;
        if (noFaceDetected == 0){
            valence = 0;
//            valence = randomGenerator();
//            arr.add((float) 10.0);
        } else {
//            valence = randomGenerator();
            valence = face.emotions.getValence();
        }
        return valence;
    }


    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {

        if (list == null) {
            return;
        }
        if (list.size() == 0) {
//            eValue = getEngagement();
//            vValue = getValence();
            noFaceDetected = 0;
//            engagement_textview.setText(String.format("engagement\n%.2f", getEngagement()));
//            valence_textview.setText(String.format("Valence\n%.2f", getValence()));
            engagement_textview.setText("Detecting Engagement ... ");
            valence_textview.setText("Detecting Valence ... ");
        } else {
            face = list.get(0);
//            getEngagement();
//            getValence();
            engagement_textview.setText(String.format("engagement\n%.2f",getEngagement()));
            valence_textview.setText(String.format("Valence\n%.2f",getValence()));

        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onCameraSizeSelected(int width, int height, Frame.ROTATE rotate) {
        if (rotate == Frame.ROTATE.BY_90_CCW || rotate == Frame.ROTATE.BY_90_CW) {
            previewWidth = height;
            previewHeight = width;
        } else {
            previewHeight = height;
            previewWidth = width;
        }
        cameraPreview.requestLayout();
    }
}