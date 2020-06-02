package com.elinkthings.wristbanddemo.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.elinkthings.wristbanddemo.dialog.HintDataDialogFragment;
import com.elinkthings.wristbanddemo.utils.AppStart;
import com.pingwang.bluetoothlib.BleBaseActivity;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * xing
 * 2019/4/16 11:55
 * activity的基类, 共有的功能都可以写在这里
 */
public abstract class BleAppBaseActivity extends BleBaseActivity {
    /**
     * 上下文对象
     */
    protected Context mContext;
    protected String TAG = this.getClass().getName();
    protected Handler mHandler = new MyHandler(this);
    @Nullable
    protected TextView mTvTopTitle;
    @Nullable
    protected Toolbar mToolbar;
    private Unbinder bind;
    //--------------ble---------------
    /**
     * 需要申请的权限
     */
    private String[] LOCATION_PERMISSION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * 权限请求返回
     */
    private final int PERMISSION = 101;
    /**
     * 定位服务返回
     */
    protected final int LOCATION_SERVER = 102;

    private HintDataDialogFragment mHintDataDialog = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            initWindows();
            setContentView(getLayoutId());
            SaveActivityData(savedInstanceState);
            mContext = this;
            init();
        } else if (getLayoutView() != null) {
            initWindows();
            setContentView(getLayoutView());
            SaveActivityData(savedInstanceState);
            mContext = this;
            init();
        }

    }

    protected void SaveActivityData(Bundle savedInstanceState) {

    }


    /**
     * handler消息,使用弱引用,避免泄露问题
     */
    private static class MyHandler extends Handler {
        private WeakReference<BleAppBaseActivity> mActivity;

        MyHandler(BleAppBaseActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mActivity.get() == null) {
                return;
            }
            mActivity.get().uiHandlerMessage(msg);
        }
    }

    /**
     * handler消息处理
     */
    protected abstract void uiHandlerMessage(Message msg);

    /**
     * 在绑定布局前的操作（状态，任务栏等的设置）
     */
    protected void initWindows() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        //设置状态栏文字为黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }


    /**
     * 获取当前activity的布局
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 获取当前activity的布局
     *
     * @return View
     */
    protected View getLayoutView() {
        return null;
    }

    /**
     * 初始化
     */
    protected final void init() {
        bind = ButterKnife.bind(this);
        addInit();
        initView();
        initData();
        initListener();
    }





    protected void addInit() {

    }


//-----------------------权限----------------------------------------


    protected void initPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onPermissionsOk();
            return;
        }
        if (ContextCompat.checkSelfPermission(this, LOCATION_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSION, PERMISSION);
        } else {
            boolean bleStatus = AppStart.isLocServiceEnable(mContext);
            if (!bleStatus) {
                //没有开启定位服务
                mHintDataDialog = HintDataDialogFragment.newInstance()
                        .setTitle("提示", 0)
                        .setCancel("取消",0)
                        .setOk("确定",0)
                        .setContent("请求开启定位服务", true)
                        .setDialogListener(new HintDataDialogFragment.DialogListener() {
                            @Override
                            public void tvSucceedListener(View v) {
                                startLocationActivity();
                            }
                        });
                mHintDataDialog.show(getSupportFragmentManager());


            } else {
                onPermissionsOk();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //请求权限被拒绝
        if (requestCode != PERMISSION)
            return;
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initPermissions();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(BleAppBaseActivity.this,
                    LOCATION_PERMISSION[0])) {
                //权限请求失败，但未选中“不再提示”选项,再次请求
                ActivityCompat.requestPermissions(this, LOCATION_PERMISSION, PERMISSION);
            } else {
                //权限请求失败，选中“不再提示”选项
                mHintDataDialog = HintDataDialogFragment.newInstance()
                        .setTitle("提示", 0)
                        .setCancel("取消",0)
                        .setOk("确定",0)
                        .setContent("请求开启定位权限", true)
                        .setDialogListener(new HintDataDialogFragment.DialogListener() {
                            @Override
                            public void tvSucceedListener(View v) {
                                AppStart.startUseSetActivity(mContext);
                            }
                        });
                mHintDataDialog.show(getSupportFragmentManager());

            }

        }
    }


    /**
     * 启动去设置定位服务
     */
    protected void startLocationActivity() {

        AppStart.startLocationActivity(BleAppBaseActivity.this, LOCATION_SERVER);

    }


    /**
     * 权限ok
     */
    protected void onPermissionsOk() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SERVER) {
            //定位服务页面返回
            initPermissions();
        }
    }

    /**
     * 初始化事件
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化，绑定布局
     */
    protected abstract void initView();

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.public_toolbar_menu, menu);
//        MenuItem item = menu.findItem(R.id.img_public_right);
//        if (item != null) {
//            item.setIcon(R.drawable.me_manssage);
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            myFinish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            myFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回
     */
    protected void myFinish() {
        finish();
    }

    protected void onClickRight() {

    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (bind != null)
            bind.unbind();
    }
}
