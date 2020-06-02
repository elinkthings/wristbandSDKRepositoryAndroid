package com.elinkthings.wristbanddemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.elinkthings.wristbanddemo.utils.MyFileUtils;
import com.elinkthings.wristbanddemo.utils.SP;
import com.pingwang.bluetoothlib.AILinkSDK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AILinkSDK.getInstance().init(this,"845ce4ad167cc100","00f0e4a024b05089ac6a863478");
       TextView mTvVersion= findViewById(R.id.tv_version);
        String version = "";
        try {
            version = getPackageManager().getPackageInfo(this
                    .getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mTvVersion.setText("Version:" + version);
        MyFileUtils.init();
        SP.init(this);

        findViewById(R.id.btn_test_wristband).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowBleActivity.class);
                startActivity(intent);
            }
        });
        initPermissions();
    }



    private void initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat
                    .requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 1) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            if (permissions.length>0&&ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                //权限请求失败，但未选中“不再提示”选项
                new AlertDialog.Builder(this).setTitle("prompt").setMessage("Request to use location permission to search for Bluetooth devices")
                        .setPositiveButton("ok" , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户至设置页手动授权
                                Intent intent =
                                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplicationContext()
                                        .getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.cancel();
                        }

                    }
                }).show();
            } else {
                //权限请求失败，选中“不再提示”选项
//                T.showShort(MainActivity.this, "获取权限失败");
                new AlertDialog.Builder(this).setTitle("prompt").setMessage("Request to use location permission to search for Bluetooth devices")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户至设置页手动授权
                                Intent intent =
                                        new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplicationContext()
                                        .getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.cancel();
                        }

                    }
                }).show();
            }

        }

    }

}
