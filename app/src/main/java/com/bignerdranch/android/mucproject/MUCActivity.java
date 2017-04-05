package com.bignerdranch.android.mucproject;

import android.Manifest;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;

import java.io.File;
import java.util.logging.Logger;

public class MUCActivity extends AppCompatActivity {

    //private final int CODE_PERMISSIONS = 1;
    private IALocationManager mIALocationManager;
    private static final String TAG = "MUCProject";
    //private static final String FB_URL = "https://mucproject-78417.firebaseio.com";
    private static final String POSITION_TAG = "Position";
    private Position mPos;
    private Position [] pointsOfInterest = new Position[] {
            new Position(51.521679511262334, -0.12997129696318044),
            new Position(51.521673714259734, -0.12996489367222352)
    };

    //private Firebase mFbInstance;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Position");

    private IALocationListener mIALocationListener = new IALocationListener() {

        // Called when the location has changed.
        @Override
        public void onLocationChanged(IALocation location) {
            mPos = new Position(location.getLatitude(), location.getLongitude(), location.getTime());
            Log.d(TAG, "Latitude: " + location.getLatitude());
            Log.d(TAG, "Longitude: " + location.getLongitude());
            Log.d(TAG, "Time: "  + location.getTime());
            Log.d(POSITION_TAG, "Position Latitude: "  + mPos.getLatitude());


            //mFb.getInstance();
            //Firebase mPosFb = mFbInstance.child("Position");
            //mPosFb.setValue(mPos);
            myRef.push().setValue(mPos);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    private IAResourceManager mFloorPlanManager;
    private ImageView mFloorPlanImage;

    private IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion region) {
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                //fetchFloorPlan(region.getId());
            }
        }

        @Override
        public void onExitRegion(IARegion region) {
            // leaving a previously entered region
        }
    };

    private IATask<IAFloorPlan> mPendingAsyncResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc);
        Log.d(TAG, "onCreate() called");
        //Intialize Firebase
        //Firebase.setAndroidContext(this);
        //mFbInstance = new Firebase(FB_URL);

        //main class of indoor atlas
        mIALocationManager = IALocationManager.create(this);

        //fetch mapping
        mFloorPlanImage = (ImageView) findViewById(R.id.image);
        // Create instance of IAFloorPlanManager class
        mFloorPlanManager = IAResourceManager.create(this);


        //Permissions
       /* String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions( this, neededPermissions, CODE_PERMISSIONS );*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        IALocationRequest mRequest = IALocationRequest.create();
        //Geo Logging***Every 1 second obtain indoor position from IndoorAtlas 2.3 SDK
        mRequest.setFastestInterval(1000);
        mIALocationManager.requestLocationUpdates(mRequest, mIALocationListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        mIALocationManager.removeLocationUpdates(mIALocationListener);
    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        mIALocationManager.destroy();
        super.onDestroy();
    }


/*
    private void fetchFloorPlan(String id) {
        // Cancel pending operation, if any
        if (mPendingAsyncResult != null && !mPendingAsyncResult.isCancelled()) {
            mPendingAsyncResult.cancel();
        }

        mPendingAsyncResult = mFloorPlanManager.fetchFloorPlanWithId(id);
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    //Logger.d(TAG, "onResult: %s", result);

                    if (result.isSuccess()) {
                        handleFloorPlanChange(result.getResult());
                    } else {
                        // do something with error
                        Toast.makeText(FloorPlanManagerActivity.this,
                                "loading floor plan failed: " + result.getError(), Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }*/

    /*private void fetchFloorPlan(String id) {
        //cancelPendingNetworkCalls();
        final IATask<IAFloorPlan> asyncResult = mFloorPlanManager.fetchFloorPlanWithId(id);
        mPendingAsyncResult = asyncResult;
        if (mPendingAsyncResult != null) {
            mPendingAsyncResult.setCallback(new IAResultCallback<IAFloorPlan>() {
                @Override
                public void onResult(IAResult<IAFloorPlan> result) {
                    Log.d(TAG, "fetch floor plan result:" + result);
                    if (result.isSuccess() && result.getResult() != null) {
                        mFloorPlan = result.getResult();
                        String fileName = mFloorPlan.getId() + ".img";
                        String filePath = Environment.getExternalStorageDirectory() + "/"
                                + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;
                        File file = new File(filePath);
                        if (!file.exists()) {
                            DownloadManager.Request request =
                                    new DownloadManager.Request(Uri.parse(mFloorPlan.getUrl()));
                            request.setDescription("IndoorAtlas floor plan");
                            request.setTitle("Floor plan");
                            // requires android 3.2 or later to compile
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.
                                        Request.VISIBILITY_HIDDEN);
                            }
                            request.setDestinationInExternalPublicDir(Environment.
                                    DIRECTORY_DOWNLOADS, fileName);

                            mDownloadId = mDownloadManager.enqueue(request);
                        } else {
                            showFloorPlanImage(filePath);
                        }
                    } else {
                        // do something with error
                        if (!asyncResult.isCancelled()) {
                            Toast.makeText(ImageViewActivity.this,
                                    (result.getError() != null
                                            ? "error loading floor plan: " + result.getError()
                                            : "access to floor plan denied"), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }, Looper.getMainLooper()); // deliver callbacks in main thread
        }
    }

//...

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Handle if any of the permissions are denied, in grantResults
    }*/




    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc);
    }*/

    public void isPositionWithinRange(){

    }

}
