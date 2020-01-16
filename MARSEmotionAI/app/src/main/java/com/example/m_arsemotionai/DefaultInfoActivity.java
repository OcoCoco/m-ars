package com.example.m_arsemotionai;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;

public class DefaultInfoActivity extends Activity implements Detector.ImageListener, CameraDetector.CameraEventListener {

    final String LOG_TAG = "CameraDetectorDemo";

    Button startSDKButton;
    Button toggleButton;
    TextView engagement_textview;
    TextView valence_textview;
    SurfaceView cameraPreview;
    Face face;
    RelativeLayout mainLayout;
    CameraDetector detector;

    private boolean isCameraFront = false;
    private boolean hasSDKStarted = true;
    private int previewWidth = 0;
    private int previewHeight = 0;
    private float noFaceDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_info);

        engagement_textview = findViewById(R.id.engagement_textview);
        valence_textview = findViewById(R.id.valence_textview);

        //this can be the more button
        startSDKButton = findViewById(R.id.sdk_start_button);
        startSDKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSDKStarted) {
                    hasSDKStarted = false;
                    stopDetector();
                    Intent intent = new Intent(DefaultInfoActivity.this, TailoredInfoActivity.class);
                    intent.putExtra("engagement", getEngagement());
                    intent.putExtra("valence", getValence());
                    intent.putExtra("noFaceDetected", noFaceDetected);
                    startActivity(intent);
                } else {
                    hasSDKStarted = true;
                    startDetector();
                }
            }
        });

        surfaceView();
        faceDetector();
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

    void switchCamera(CameraDetector.CameraType type) {
        detector.setCameraType(type);
    }

    private float getEngagement(){
        float engagement;
        if (noFaceDetected != 0){
            engagement = 0;
        } else {
            engagement = face.emotions.getEngagement();
        }
        return engagement;
    }

    private float getValence(){
        float valence;
        if (noFaceDetected != 0){
            valence = 0;
        } else {
            valence = face.emotions.getValence();
        }
        return valence;
    }

    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {
        if (list == null)
            return;
        if (list.size() == 0) {
            noFaceDetected = 0;
            engagement_textview.setText("Detecting Engagement ... ");
            valence_textview.setText("Detecting Valence ... ");
        } else {
            face = list.get(0);
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