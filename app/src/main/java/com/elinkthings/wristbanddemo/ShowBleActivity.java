package com.elinkthings.wristbanddemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.elinkthings.distrackerlibrary.HealthBraceletBleConfig;
import com.elinkthings.wristbanddemo.dialog.LoadingIosDialogFragment;
import com.pingwang.bluetoothlib.bean.BleValueBean;
import com.pingwang.bluetoothlib.listener.CallbackDisIm;
import com.pingwang.bluetoothlib.listener.OnCallbackBle;
import com.pingwang.bluetoothlib.listener.OnScanFilterListener;
import com.pingwang.bluetoothlib.server.ELinkBleServer;
import com.pingwang.bluetoothlib.utils.BleLog;
import com.pingwang.bluetoothlib.utils.BleStrUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * xing<br>
 * 2020/5/6<br>
 * java类作用描述
 */
public class ShowBleActivity extends AppCompatActivity implements OnCallbackBle, OnScanFilterListener {

    private static String TAG = ShowBleActivity.class.getName();

    private final int BIND_SERVER_OK = 1;
    private final int BIND_SERVER_ERR = 2;
    private final int REFRESH_DATA = 3;
    private List<String> mList;
    private ArrayAdapter listAdapter;
    private ELinkBleServer mBluetoothService;

    private Intent bindIntent;
    private Context mContext;
    private int mType;
    private boolean mFilter = true;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case BIND_SERVER_OK:

                    break;

                case REFRESH_DATA:
                    listAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ble);
        Intent mUserService = new Intent(this.getApplicationContext(), ELinkBleServer.class);
        startService(mUserService);
        mContext = this;
        init();
        initData();


    }

    private void initData() {
        bindService();

    }

    private void init() {

        mList = new ArrayList<>();
        ListView listView = findViewById(R.id.listview);
        Button btn = findViewById(R.id.btn);
        Button btn1 = findViewById(R.id.btn1);
        Button clear = findViewById(R.id.clear);
        final Button filter = findViewById(R.id.filter);
        filter.setTag(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothService != null) {
                    mBluetoothService.scanLeDevice(0, HealthBraceletBleConfig.UUID_SERVER);
                    mList.clear();
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothService != null) {
                    mBluetoothService.stopScan();
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothService != null) {
                    mList.clear();
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean m = (Boolean) filter.getTag();
                filter.setTag(!m);
                mFilter = !m;
                filter.setText("Filter:" + mFilter);
            }
        });

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemStr = mList.get(position);
                String mac = itemStr.split("=")[0];
                if (mBluetoothService != null) {
                    mBluetoothService.stopScan();
                    mBluetoothService.connectDevice(mac);
                    showLoading();
                }
            }
        });


    }


    //---------------------------------服务---------------------------------------------------

    private void bindService() {
        if (bindIntent == null) {
            bindIntent = new Intent(mContext, ELinkBleServer.class);
            if (mFhrSCon != null)
                this.bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
        }
    }


    private void unbindService() {
        CallbackDisIm.getInstance().removeListener(this);
        if (mFhrSCon != null)
            this.unbindService(mFhrSCon);
        bindIntent = null;
    }


    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((ELinkBleServer.BluetoothBinder) service).getService();
            if (mBluetoothService != null) {
                mBluetoothService.setOnCallback(ShowBleActivity.this);
                mBluetoothService.setOnScanFilterListener(ShowBleActivity.this);
                mHandler.sendEmptyMessage(BIND_SERVER_OK);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };


    @Override
    public void onStartScan() {

    }

    @Override
    public void onScanning(@NonNull BleValueBean data) {
        String mAddress = data.getMac();
        if (!mList.contains(mAddress + "=" + data.getName())) {
            mList.add(mAddress + "=" + data.getName());
            listAdapter.notifyDataSetChanged();

        }


        List<byte[]> manufacturerDataList = data.getManufacturerDataList();
        if (manufacturerDataList == null || manufacturerDataList.isEmpty()) {
            return;
        }
        byte[] data1 =manufacturerDataList.get(0);
        byte[] data2 =null;
        if (manufacturerDataList.size()>1){
            data2=manufacturerDataList.get(1);
        }
        BleLog.i(TAG, "设备地址||厂商数据:" + data.getMac() + "||" + BleStrUtils.byte2HexStr(data1) + "||" + getMacAddress(data2).toUpperCase());

    }


    private String getMacAddress(byte[] macByte){
        StringBuilder hs = new StringBuilder();
        for(int i = macByte.length-1; i >=0;i--) {
            byte aB = macByte[i];
            int a = aB & 0xFF;
            String stmp = Integer.toHexString(a);
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
            hs.append(":");
        }
        if (hs.length()>0)
            hs.deleteCharAt(hs.length()-1);
        return hs.toString();
    }


    @Override
    public void onConnecting(@NonNull String mac) {

    }

    @Override
    public void onDisConnected(@NonNull String mac, int code) {
        dismissLoading();
        Toast.makeText(mContext, "Disconnect:" + code, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onServicesDiscovered(@NonNull String mac) {
        dismissLoading();
        Intent intent = new Intent();
        intent.setClass(ShowBleActivity.this, HealthBraceletActivity.class);
        intent.putExtra("mac", mac);
        startActivity(intent);

    }


    @Override
    public void bleOpen() {

    }

    @Override
    public void bleClose() {
        Toast.makeText(this, "Bluetooth is not turned on", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFilter(BleValueBean bleValueBean) {
        return true;

    }

    @Override
    public void onScanRecord(BleValueBean mBle) {
    }


    //--------------------------start Loading--------------------------
    private LoadingIosDialogFragment mDialogFragment;

    private void showLoading() {
        if (mDialogFragment == null)
            mDialogFragment = new LoadingIosDialogFragment();
        mDialogFragment.show(getSupportFragmentManager());
    }

    private void dismissLoading() {
        if (mDialogFragment != null)
            mDialogFragment.dismiss();
    }

    //--------------------------end Loading--------------------------


    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothService != null) {
            mBluetoothService.setOnCallback(ShowBleActivity.this);
            mBluetoothService.setOnScanFilterListener(ShowBleActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }
}
