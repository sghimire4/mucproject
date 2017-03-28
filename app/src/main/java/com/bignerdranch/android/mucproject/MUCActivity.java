package com.bignerdranch.android.mucproject;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.resources.IAResourceManager;

public class MUCActivity extends AppCompatActivity {

    private final int CODE_PERMISSIONS = 1;
    private IALocationManager mIALocationManager;
    private static final String TAG = "MUCProject";

    private IALocationListener mIALocationListener = new IALocationListener() {

        // Called when the location has changed.
        @Override
        public void onLocationChanged(IALocation location) {

            Log.d(TAG, "Latitude: " + location.getLatitude());
            Log.d(TAG, "Longitude: " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    private IAResourceManager mResourceManager;
    private ImageView mFloorPlanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc);
        Log.d(TAG, "onCreate() called");
        //main class of indoor atlas
        mIALocationManager = IALocationManager.create(this);



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
}
