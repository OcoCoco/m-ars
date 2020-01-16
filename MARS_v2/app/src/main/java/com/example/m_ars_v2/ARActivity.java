package com.example.m_ars_v2;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

public class ARActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ARActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

//    TextView textView;
    ArFragment arFragment;
    ModelRenderable modelRenderable;
    Snackbar loadingMessageSnackbar = null;
    Snackbar achievementMessageSnackBar;
//    CoordinatorLayout coordinatorLayout;
//    GestureDetector gestureDetector;
//    private boolean hasFinishedLoading = false; // True once scene is loaded
//    private boolean hasPlacedModel = false; // True once the model has been placed

    String modelName;

//    private int scanCounter = 0;
    private int modelCounter = 0;
    private int fetchModelCounter = 0;
//    AchievementActivity achievementActivity = new AchievementActivity();
    public boolean isachievementUnlocked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        setContentView(R.layout.activity_ar);

        findViewById(R.id.moreImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName("com.example.m_arsemotionai", "com.example.m_arsemotionai.DefaultInfoActivity"));
                startActivity(intent);
            }
        });

//        // Implement help page
////        findViewById(R.id.helpImageButton).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Intent intent = new Intent(ARActivity.this, ARHelpActivity.class);
////                startActivity(intent);
////            }
////        });

        modelName = getIntent().getStringExtra("artifactName");

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        if (arFragment != null) {
            showLoadingMessage();
        }

        fetchModel();
        placeModel();

        achievementConditions();

//        if (scanCounter == 1){
//            isachievementUnlocked = true;
//            achievementActivity.isARScanAchievementUnlocked();
////            Toast.makeText(this, "Achievement Unlocked: Step 1 Complete", Toast.LENGTH_LONG).show();
//        }


////         Tap gesture detector
//        gestureDetector = new GestureDetector(
//                this, new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                onSingleTap(e);
//                return true;
//            }
//
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return true;
//            }
//        });
//
//        // Touch listener on the scene to listen for taps
//        arFragment.getArSceneView().getScene().setOnTouchListener(
//                (HitTestResult hitTestResult, MotionEvent event) -> {
//                    // If model hasn't been placed yet, detect a tap and then check if the tap
//                    // occurred on an ARCore plane to place the model
//                    if (!hasPlacedModel) {
//                        return gestureDetector.onTouchEvent(event);
//                    }
//                    return false;
//                });

        //Will hide the loading message once a plane is detected
        arFragment.getArSceneView().getScene().addOnUpdateListener(
                frameTime -> {
                    if (loadingMessageSnackbar == null)
                        return;

                    Frame frame = arFragment.getArSceneView().getArFrame();
                    if (frame == null)
                        return;

                    if (frame.getCamera().getTrackingState() != TrackingState.TRACKING)
                        return;

                    for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                        if (plane.getTrackingState() == TrackingState.TRACKING)
                            hideLoadingMessage();
                    }
                }
        );

    } // End of OnCreate method

    private void fetchModel(){
        //Fetch model
        ModelRenderable.builder()
                .setSource(this, Uri.parse(modelName + ".sfb"))
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load" + modelName +" renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        fetchModelCounter++;
    }

    //This method will load the model into the scene when the user taps the screen
    private void placeModel(){
        arFragment.setOnTapArPlaneListener(
        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
            if (modelRenderable == null) {
                return;
            }

            // Create the Anchor.
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            anchorNode.setRenderable(modelRenderable);

            // Create the transformable andy and add it to the anchor.
            TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
            andy.setParent(anchorNode);
            andy.setRenderable(modelRenderable);
            andy.select();
        });

        modelCounter++;
    }

//    private void onSingleTap(MotionEvent tap) {
//        if (!hasFinishedLoading) {
//            // Do nothing
//            return;
//        }
//
//        Frame frame = arFragment.getArSceneView().getArFrame();
//        if (frame != null) {
//            if (!hasPlacedModel && tryPlacingModel(tap, frame)) {
//                hasPlacedModel = true;
//            }
//        }
//    }
//
//    private boolean tryPlacingModel(MotionEvent tap, Frame frame) {
//        if (tap != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING) {
//            for (HitResult hitResult : frame.hitTest(tap)) {
//                Trackable trackable = hitResult.getTrackable();
//
//                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hitResult.getHitPose())) {
//                    //Create Anchor
//                    Anchor anchor = hitResult.createAnchor();
//                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//                    anchorNode.setRenderable(modelRenderable);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private void hideLoadingMessage() {
        if (loadingMessageSnackbar == null) {
            return;
        }
        loadingMessageSnackbar.dismiss();
        loadingMessageSnackbar = null;
    }

    private void showLoadingMessage() {
        if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
            return;
        }
        loadingMessageSnackbar =
                Snackbar.make(
                        ARActivity.this.findViewById(android.R.id.content),
                        R.string.plane_finding,
                        Snackbar.LENGTH_INDEFINITE);
        loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        loadingMessageSnackbar.show();
    }

    /*
     * Returns false and displays an error if the app cannot turn
     * Returns true if the app can run on the device
     * Finishes activity if the app cannot run
     */
    private boolean checkIsSupportedDeviceOrFinish(ARActivity arActivity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "M-ARS requires Android N or later");
            Toast.makeText(arActivity, "M-ARS requires Android N or later", Toast.LENGTH_LONG).show();
            arActivity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) arActivity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "M-ARS requires OpenGL ES 3.0 later");
            Toast.makeText(arActivity, "M-ARS requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            arActivity.finish();
            return false;
        }
        return true;
    }

    String achievementString = "";
//    String c = getIntent().getStringExtra("scanCounter"); //Try using Bundle or make a public get method for the counter and call it
//    int scounter = Integer.parseInt(c);

    Bundle bundle;


    private String achievementConditions() {
//        achievementArrList = new ArrayList<String>();
        bundle = getIntent().getExtras();
        int scounter = bundle.getInt("scanCounter");

        if (scounter == 1 && modelCounter == 1){
//            isachievementUnlocked = true;
            achievementString = "You're getting the hang of this!";
//            achievementArrList.add(achievementString);
//            achievementArrAdapater = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, achievementArrList);
            showAchievementMesseage();
        } else if (modelCounter == 2 && fetchModelCounter == 2) {
//            isachievementUnlocked = true;
            achievementString = "Artifacts discovered";
            showAchievementMesseage();
        }

        return achievementString;
    }

    private void showAchievementMesseage () {
        achievementMessageSnackBar = Snackbar.make(findViewById(R.id.coordinatorLayout), "Achievement Unlocked", Snackbar.LENGTH_LONG);
        achievementMessageSnackBar.setAction("VIEW", this);
        achievementMessageSnackBar.setActionTextColor(Color.WHITE);
        achievementMessageSnackBar.show();
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ARActivity.this, AchievementActivity.class);
        intent.putExtra("achievementString", achievementString);
        startActivity(intent);
    }
}

