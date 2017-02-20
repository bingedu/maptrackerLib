package com.swifty.maptracker;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.swifty.maptrackerlib.Log;
import com.swifty.maptrackerlib.MapActivity;
import com.swifty.maptrackerlib.SharedPrefsUtil;
import com.swifty.maptrackerlib.gps.LocationManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_PERMISSION = 1001;
    private static final int RC_SETTING = 1002;
    private static final String[] perms = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void enter(View view) {
        EasyPermissions.requestPermissions(this, getString(com.swifty.maptrackerlib.R.string.need_permission),
                RC_PERMISSION, perms);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (perms.size() != this.perms.length) return;
        SharedPrefsUtil.init(getApplicationContext());
        LocationManager.init(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        Log.d(list);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this, getString(R.string.need_permission))
                    .setPositiveButton(getString(com.swifty.maptrackerlib.R.string.setting))
                    .setNegativeButton(getString(com.swifty.maptrackerlib.R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTING)
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_PERMISSION)
    private void methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            startActivity(new Intent(this, MapActivity.class));
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(com.swifty.maptrackerlib.R.string.need_permission),
                    RC_PERMISSION, perms);
        }
    }
}
