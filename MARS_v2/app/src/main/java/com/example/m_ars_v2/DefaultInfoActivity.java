package com.example.m_ars_v2;


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
import com.google.android.gms.vision.CameraSource;

import java.util.List;

public class DefaultInfoActivity extends Activity implements Detector.ImageListener, CameraDetector.CameraEventListener {

    final String LOG_TAG = "CameraDetectorDemo";

    Button startSDKButton;
    TextView smileTextView;
    TextView angerTextView;
    TextView contempt_textview;
    TextView disgust_textview;
    TextView fear_textview;
    TextView joy_textview;
    TextView sad_textview;
    TextView surprise_textview;
    TextView engagement_textview;
    TextView valence_textview;
    TextView attention_textview;
    ToggleButton toggleButton;

    SurfaceView cameraPreview;
    CameraSource cameraSource;

    Face face;

    private boolean isCameraBack = false;
    private boolean hasSDKStarted = true;

    RelativeLayout mainLayout;

    CameraDetector detector;

    int previewWidth = 0;
    int previewHeight = 0;

//    private Snackbar loadingMessageSnackbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_info);

//        smileTextView = (TextView) findViewById(R.id.smile_textview);
//        ageTextView = (TextView) findViewById(R.id.age_textview);
//        ethnicityTextView = (TextView) findViewById(R.id.ethnicity_textview);
//        angerTextView = findViewById(R.id.anger_textview);
//        contempt_textview = findViewById(R.id.contempt_textview);
//        disgust_textview = findViewById(R.id.disgust_textview);
//        fear_textview = findViewById(R.id.fear_textview);
//        joy_textview = findViewById(R.id.joy_textview);
//        sad_textview = findViewById(R.id.sad_textview);
//        surprise_textview = findViewById(R.id.surprise_textview);
        engagement_textview = findViewById(R.id.engagement_textview);
        valence_textview = findViewById(R.id.valence_textview);
//        attention_textview = findViewById(R.id.attention_textview);

        toggleButton = findViewById(R.id.front_back_toggle_button);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCameraBack = isChecked;
                switchCamera(isCameraBack? CameraDetector.CameraType.CAMERA_BACK : CameraDetector.CameraType.CAMERA_FRONT);
            }
        });

        //this can be the more button
        startSDKButton = (Button) findViewById(R.id.sdk_start_button);
        startSDKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasSDKStarted) {
                    hasSDKStarted = false;
                    stopDetector();
                    Intent intent = new Intent(DefaultInfoActivity.this, TailoredInfoActivity.class);
//                    intent.putExtra("anger", face.emotions.getAnger());
//                    intent.putExtra("contempt", face.emotions.getContempt());
//                    intent.putExtra("disgust", face.emotions.getDisgust());
//                    intent.putExtra("fear", face.emotions.getFear());
//                    intent.putExtra("joy", face.emotions.getJoy());
//                    intent.putExtra("sadness", face.emotions.getSadness());
//                    intent.putExtra("surprise", face.emotions.getSurprise());
                    intent.putExtra("engagement", face.emotions.getEngagement());
                    intent.putExtra("valence", face.emotions.getValence());
//                    intent.putExtra("attention", face.expressions.getAttention());
                    startActivity(intent);
                } else {
                    hasSDKStarted = true;
                    startDetector();
                }
            }
        });

        // a surface view is required for the SDK to work
        mainLayout = findViewById(R.id.main_layout);
        cameraPreview = new SurfaceView(this) {
            @Override
            public void onMeasure(int widthSpec, int heightSpec) {
//                int w = 1;
//                int h = 1;
//                setMeasuredDimension(w, h);
//                cameraPreview.setAlpha(0);
                int measureWidth = MeasureSpec.getSize(widthSpec);
                int measureHeight = MeasureSpec.getSize(heightSpec);
                int width;
                int height;
                if (previewHeight == 0 || previewWidth == 0) {
                    width = measureWidth;
                    height = measureHeight;
                } else {
                    float viewAspectRatio = (float)measureWidth/measureHeight;
                    float cameraPreviewAspectRatio = (float) previewWidth/previewHeight;

                    if (cameraPreviewAspectRatio > viewAspectRatio) {
                        width = measureWidth;
                        height =(int) (measureWidth / cameraPreviewAspectRatio);
                    } else {
                        width = (int) (measureHeight * cameraPreviewAspectRatio);
                        height = measureHeight;
                    }
                }
                setMeasuredDimension(width,height);
            }
        };
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        cameraPreview.setLayoutParams(params);
        mainLayout.addView(cameraPreview,0);

//        cameraSource = new CameraSource.Builder(this, detector)
//                .setAutoFocusEnabled(true)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//                .setRequestedFps(20)
//                .setRequestedPreviewSize(640, 480)
//                .build();

        detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_BACK, cameraPreview);
//        detector.setDetectSmile(true);
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

    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {
        if (list == null)
            return;
        if (list.size() == 0) {
//            smileTextView.setText("NO FACE");
//            ageTextView.setText("");
//            ethnicityTextView.setText("");
//            angerTextView.setText("Detecting Anger ... ");
//            contempt_textview.setText("Detecting Contempt ... ");
//            disgust_textview.setText("Detecting Disgust ... ");
//            fear_textview.setText("Detecting Fear ... ");
//            joy_textview.setText("Detecting Joy ... ");
//            sad_textview.setText("Detecting Sadness ... ");
//            surprise_textview.setText("Detecting Surprise ... ");
            engagement_textview.setText("Detecting Engagement ... ");
            valence_textview.setText("Detecting Valence ... ");
//            attention_textview.setText("Detecting Attention ... ");
        } else {
            face = list.get(0);
//            angerTextView.setText(String.format("Anger\n%.2f",face.emotions.getAnger()));
//            contempt_textview.setText(String.format("Contempt\n%.2f",face.emotions.getContempt()));
//            disgust_textview.setText(String.format("Disgust\n%.2f",face.emotions.getDisgust()));
//            fear_textview.setText(String.format("Fear\n%.2f",face.emotions.getFear()));
//            joy_textview.setText(String.format("Joy\n%.2f",face.emotions.getJoy()));
//            sad_textview.setText(String.format("Sadness\n%.2f",face.emotions.getSadness()));
//            surprise_textview.setText(String.format("Surprise\n%.2f",face.emotions.getSurprise()));
            engagement_textview.setText(String.format("engagement\n%.2f",face.emotions.getEngagement()));
            valence_textview.setText(String.format("Valence\n%.2f",face.emotions.getValence()));
//            attention_textview.setText(String.format("Attention\n%.2f",face.expressions.getAttention()));

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