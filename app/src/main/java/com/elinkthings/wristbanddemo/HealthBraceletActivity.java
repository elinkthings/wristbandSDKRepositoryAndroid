package com.elinkthings.wristbanddemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elinkthings.distrackerlibrary.HealthBraceletBleConfig;
import com.elinkthings.distrackerlibrary.HealthBraceletDevice;
import com.elinkthings.distrackerlibrary.HealthStatusHistoryRecordBean;
import com.elinkthings.wristbanddemo.base.BleAppBaseActivity;
import com.elinkthings.wristbanddemo.dialog.DialogStringImageAdapter;
import com.elinkthings.wristbanddemo.dialog.HintDataDialogFragment;
import com.elinkthings.wristbanddemo.dialog.ShowListDialogFragment;
import com.elinkthings.wristbanddemo.utils.L;
import com.elinkthings.wristbanddemo.utils.MyFileUtils;
import com.elinkthings.wristbanddemo.utils.SP;
import com.elinkthings.wristbanddemo.utils.TimeUtils;
import com.elinkthings.wristbanddemo.view.SeekBarMin;
import com.pingwang.bluetoothlib.device.BleDevice;
import com.pingwang.bluetoothlib.listener.OnBleVersionListener;
import com.pingwang.bluetoothlib.listener.OnCallbackBle;
import com.pingwang.bluetoothlib.listener.OnDialogOTAListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * xing<br>
 * 2020/5/6<br>
 * java类作用描述
 */
public class HealthBraceletActivity extends BleAppBaseActivity implements OnCallbackBle,  HealthBraceletDevice.onNotifyData, View.OnClickListener, OnBleVersionListener, SeekBar.OnSeekBarChangeListener, OnDialogOTAListener,
        ShowListDialogFragment.onDialogListener {


    private final int REFRESH_DATA = 1;
    private final int CONNECT_SUCCESS = 2;
    private final int CONNECT_FAILURE = 3;


    @BindView(R.id.button_time)
    Button mButtonTime;
    @BindView(R.id.button_user)
    Button mButtonUser;
    @BindView(R.id.button_status)
    Button mButtonStatus;
    @BindView(R.id.button_log)
    Button mButtonLog;
    @BindView(R.id.button_close)
    Button mButtonClose;
    @BindView(R.id.button_set)
    Button mButtonSet;
    @BindView(R.id.button_clear)
    Button button_clear;
    @BindView(R.id.listview)
    ListView mListview;
    @BindView(R.id.button_activation)
    Button mButtonActivation;
    @BindView(R.id.button_close_activation)
    Button mButtonCloseActivation;
    @BindView(R.id.et_set_user)
    EditText mEtSetUser;
    @BindView(R.id.button_connect)
    Button mButtonConnect;
    @BindView(R.id.button_dis)
    Button mButtonDis;
    @BindView(R.id.et_set_user_status)
    EditText mEtSetUserStatus;

    /**
     * 靠近报警阀值
     * Near the alarm threshold
     */
    @BindView(R.id.seekBar1)
    SeekBarMin mSeekBar1;
    @BindView(R.id.text1)
    TextView mText1;

    /**
     * 接收的阀值次数
     * Threshold times received
     */
    @BindView(R.id.seekBar2)
    SeekBarMin mSeekBar2;
    @BindView(R.id.text2)
    TextView mText2;

    /**
     * 脱离报警时间
     * Breakaway alarm time
     */
    @BindView(R.id.seekBar3)
    SeekBarMin mSeekBar3;
    @BindView(R.id.text3)
    TextView mText3;

    /**
     * 脱离报警次数
     * Breakaway alarm times
     */
    @BindView(R.id.seekBar4)
    SeekBarMin mSeekBar4;
    @BindView(R.id.text4)
    TextView mText4;


    /**
     * Gsensor 灵敏度，值越小越灵敏
     * Gsensor sensitivity, the smaller the value, the more sensitive
     */
    @BindView(R.id.seekBar5)
    SeekBarMin mSeekBar5;
    @BindView(R.id.text5)
    TextView mText5;


    /**
     * 脱机时间，Gsensor 不动时间认为是脱机
     * Offline time, Gsensor fixed time is considered offline
     */
    @BindView(R.id.seekBar6)
    SeekBarMin mSeekBar6;
    @BindView(R.id.text6)
    TextView mText6;

    /**
     * 马达震动时间
     * Motor vibration time
     */
    @BindView(R.id.seekBar7)
    SeekBarMin mSeekBar7;
    @BindView(R.id.text7)
    TextView mText7;

    @BindView(R.id.button_info)
    Button mButtonInfo;
    @BindView(R.id.button_bind)
    Button mButtonBind;
    @BindView(R.id.button_ota)
    Button mButtonOta;
    @BindView(R.id.button_version)
    Button mButtonVersion;
    @BindView(R.id.tv_version)
    TextView mTvVersion;
    @BindView(R.id.button_reboot)
    Button mButtonReboot;


    private String mAddress;
    private HealthBraceletDevice mHealthBraceletDevice;
    private HintDataDialogFragment mHintDataDialogFragment;
    private List<String> mList;
    private ArrayAdapter listAdapter;
    /**
     * 靠近报警阀值
     * Near the alarm threshold
     */
    private int mSeekBarData1 = 65;
    /**
     * 接收的阀值次数
     * Threshold times received
     */
    private int mSeekBarData2 = 2;
    /**
     * 脱离报警时间
     * Breakaway alarm time
     */
    private int mSeekBarData3 = 2;
    /**
     * 脱离报警次数
     * Breakaway alarm times
     */
    private int mSeekBarData4 = 10;
    /**
     * 灵敏度
     * Gsensor sensitivity, the smaller the value, the more sensitive
     */
    private int mSeekBarData5 = 10;
    /**
     * 脱机时间
     * Offline time, Gsensor fixed time is considered offline
     */
    private int mSeekBarData6 = 60;
    /**
     * 马达时间
     * Motor vibration time
     */
    private int mSeekBarData7 = 500;

    private String mOTAFileName;


    private ArrayList<DialogStringImageAdapter.DialogStringImageBean> mDialogList;

    @Override
    protected void uiHandlerMessage(Message msg) {
        switch (msg.what) {
            case CONNECT_SUCCESS:
                //连接成功

                mList.add(TimeUtils.getTime() + "connection succeeded");
                mHandler.sendEmptyMessage(REFRESH_DATA);
                String userIdString = mEtSetUser.getText().toString().trim();
                int id = 0;
                if (!TextUtils.isEmpty(userIdString)) {
                    try {
                        id = Integer.valueOf(userIdString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mList.add(TimeUtils.getTime() + "Binding code:" + id);
                        mHandler.sendEmptyMessage(REFRESH_DATA);
                        return;
                    }
                    mHealthBraceletDevice.setBind(id);
                }

                mList.add(TimeUtils.getTime() + "Binding code:" + id);
                mHandler.sendEmptyMessage(REFRESH_DATA);
                break;

            case CONNECT_FAILURE:
//                finish();

                break;


            case REFRESH_DATA:
                if (listAdapter != null)
                    listAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_bracelet;
    }

    @Override
    protected void initListener() {
        mButtonTime.setOnClickListener(this);
        mButtonUser.setOnClickListener(this);
        mButtonStatus.setOnClickListener(this);
        mButtonLog.setOnClickListener(this);
        mButtonClose.setOnClickListener(this);
        mButtonSet.setOnClickListener(this);
        button_clear.setOnClickListener(this);
        mButtonActivation.setOnClickListener(this);
        mButtonCloseActivation.setOnClickListener(this);
        mButtonConnect.setOnClickListener(this);
        mButtonDis.setOnClickListener(this);
        mButtonInfo.setOnClickListener(this);
        mButtonBind.setOnClickListener(this);
        mButtonOta.setOnClickListener(this);
        mButtonVersion.setOnClickListener(this);
        mTvVersion.setOnClickListener(this);
        mButtonReboot.setOnClickListener(this);

        mSeekBar1.setOnSeekBarChangeListener(this);
        mSeekBar2.setOnSeekBarChangeListener(this);
        mSeekBar3.setOnSeekBarChangeListener(this);
        mSeekBar4.setOnSeekBarChangeListener(this);
        mSeekBar5.setOnSeekBarChangeListener(this);
        mSeekBar6.setOnSeekBarChangeListener(this);
        mSeekBar7.setOnSeekBarChangeListener(this);

        mOTAFileName = SP.getInstance().getOtaFileName();
        if (mOTAFileName.isEmpty())
            mTvVersion.setText("xxxxxxxx");
        else
            mTvVersion.setText(mOTAFileName);
        mDialogList = new ArrayList<>();


    }


    @Override
    public void onItemListener(int position) {
        if (mDialogList.size() > position) {
            DialogStringImageAdapter.DialogStringImageBean dialogStringImageBean = mDialogList
                    .get(position);
            String name = dialogStringImageBean.getName();
            mOTAFileName = name;
            SP.getInstance().putOtaFileName(name);
            mTvVersion.setText(mOTAFileName);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_clear) {
            mList.clear();
            mHandler.sendEmptyMessage(REFRESH_DATA);
        } else if (v.getId() == R.id.button_connect) {
            if (mHealthBraceletDevice == null) {
                mList.add(TimeUtils.getTime() + "Start to connect");
                mHandler.sendEmptyMessage(REFRESH_DATA);
                connectBle(mAddress);
            }
        } else if (v.getId() == R.id.tv_version) {
            mDialogList.clear();
            ArrayList<String> list = MyFileUtils.list();
            for (String s : list) {
                mDialogList.add(new DialogStringImageAdapter.DialogStringImageBean(s, 0));
            }

            ShowListDialogFragment.newInstance().setTitle("").setCancel("", 0).setCancelBlank(true)
                    .setBackground(true).setBottom(false).setList(mDialogList)
                    .setOnDialogListener(this).show(getSupportFragmentManager());

        }

        if (mHealthBraceletDevice == null) {
            L.i(TAG, "mHealthBraceletDevice==null");
            return;
        }
        String str;
        switch (v.getId()) {

            case R.id.button_dis:
                mHealthBraceletDevice.disconnect();
                mList.add(TimeUtils.getTime() + "Actively disconnect");
                mHandler.sendEmptyMessage(REFRESH_DATA);
                break;
            case R.id.button_time:
                mHealthBraceletDevice.setSynTime();
                break;
            case R.id.button_reboot:

                mHealthBraceletDevice.reboot();
                break;
            case R.id.button_version:
                mHealthBraceletDevice.getVersion();
                break;
            case R.id.button_ota:
                if (mOTAFileName.isEmpty()) {
                    Toast.makeText(mContext, "Please select the file first", Toast.LENGTH_SHORT).show();
                    return;
                }
                String byFileName = MyFileUtils.getByFileName() + mOTAFileName;
                mList.add(TimeUtils.getTime() + "OTA has started, please be patient");
                mHandler.sendEmptyMessage(REFRESH_DATA);
                mHealthBraceletDevice.startDialogOta(byFileName, this);

                break;
            case R.id.button_bind:
                String userIdString = mEtSetUser.getText().toString().trim();
                int id = 0;
                if (!TextUtils.isEmpty(userIdString)) {
                    try {
                        id = Integer.valueOf(userIdString);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mList.add(TimeUtils.getTime() + "Binding code:" + id);
                        mHandler.sendEmptyMessage(REFRESH_DATA);
                        return;
                    }
                    mHealthBraceletDevice.setBind(id);
                }

                mList.add(TimeUtils.getTime() + "Binding code:" + id);
                mHandler.sendEmptyMessage(REFRESH_DATA);

                break;
            case R.id.button_user:
                str = "The filled data is abnormal";
                String userIdStr = mEtSetUser.getText().toString().trim();
                String statusStr = mEtSetUserStatus.getText().toString().trim();
                int userId;
                int status;
                if (!TextUtils.isEmpty(userIdStr) && !TextUtils.isEmpty(statusStr)) {
                    try {
                        userId = Integer.valueOf(userIdStr);
                        status = Integer.valueOf(statusStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        mList.add(TimeUtils.getTime() + "Set up users:" + str);
                        mHandler.sendEmptyMessage(REFRESH_DATA);
                        return;
                    }
                    mHealthBraceletDevice.setUser(userId, status);
                    str = "User ID=" + userId + "\n";
                    str += "Status=" + status;
                }

                mList.add(TimeUtils.getTime() + "Set up users:" + str);
                mHandler.sendEmptyMessage(REFRESH_DATA);

                break;
            case R.id.button_status:
                mHealthBraceletDevice.getStatus();
                break;
            case R.id.button_log:
                mHealthBraceletDevice.getHistoryRecord();
                break;
            case R.id.button_close:
                mHealthBraceletDevice.setStatus(3);
                break;
            case R.id.button_activation:
                mHealthBraceletDevice.setStatus(1);
                break;
            case R.id.button_close_activation:
                mHealthBraceletDevice.setStatus(2);
                break;
            case R.id.button_info:
                mHealthBraceletDevice.getDeviceParameter();
                break;
            case R.id.button_set:
                int nearAlarmDb = mSeekBarData1;
                int nearAlarmNumber = mSeekBarData2;
                int detachAlarmTime = mSeekBarData3;
                int detachAlarmNumber = mSeekBarData4;
                int sensorSensitivity = mSeekBarData5;
                int offlineTime = mSeekBarData6;
                int motorVibrationTime = mSeekBarData7;

                mHealthBraceletDevice
                        .setDeviceParameter(nearAlarmDb, nearAlarmNumber, detachAlarmTime,
                                detachAlarmNumber, sensorSensitivity, offlineTime,
                                motorVibrationTime);
                str = "Near the alarm threshold=-" + nearAlarmDb + "db\n";
                str += "Threshold times received=" + nearAlarmNumber + "\n";
                str += "Breakaway alarm time=" + detachAlarmTime + "S\n";
                str += "Breakaway alarm times=" + detachAlarmNumber + "\n";
                str += "Sensitivity, the smaller the value, the more sensitive=" + sensorSensitivity + "\n";
                str += "Offline time=" + offlineTime + "S\n";
                str += "Motor vibration time=" + motorVibrationTime + "ms";

                mList.add(TimeUtils.getTime() + "Setting parameters:\n" + str);
                mHandler.sendEmptyMessage(REFRESH_DATA);
                break;

        }


    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar1:
                mSeekBarData1 = progress;
                break;
            case R.id.seekBar2:
                mSeekBarData2 = progress;
                break;
            case R.id.seekBar3:
                mSeekBarData3 = progress;
                break;
            case R.id.seekBar4:
                mSeekBarData4 = progress;
                break;
            case R.id.seekBar5:
                mSeekBarData5 = progress;
                break;
            case R.id.seekBar6:
                mSeekBarData6 = progress;
                break;
            case R.id.seekBar7:
                mSeekBarData7 = progress;
                break;

        }
        setCurrentParameter(mSeekBarData1, mSeekBarData2, mSeekBarData3, mSeekBarData4,
                mSeekBarData5, mSeekBarData6, mSeekBarData7);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void initData() {
        mAddress = getIntent().getStringExtra("mac");
        mList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListview.setAdapter(listAdapter);
        setCurrentParameter(mSeekBarData1, mSeekBarData2, mSeekBarData3, mSeekBarData4,
                mSeekBarData5, mSeekBarData6, mSeekBarData7);
    }


    public void setCurrentParameter(int seekBarData1, int seekBarData2, int seekBarData3,
                                    int seekBarData4, int seekBarData5, int seekBarData6,
                                    int seekBarData7) {
        mSeekBarData1 = seekBarData1;
        mSeekBarData2 = seekBarData2;
        mSeekBarData3 = seekBarData3;
        mSeekBarData4 = seekBarData4;
        mSeekBarData5 = seekBarData5;
        mSeekBarData6 = seekBarData6;
        mSeekBarData7 = seekBarData7;

        if (mSeekBar1 != null && mText1 != null) {
            int min = 30;
            int max = 100;
            mSeekBar1.setMin(min);
            mSeekBar1.setMax(max);
            mSeekBar1.setProgress(mSeekBarData1);
            mText1.setText("-" + mSeekBarData1 + "dbm");
        }


        if (mSeekBar2 != null && mText2 != null) {
            int min = 1;
            int max = 5;
            mSeekBar2.setMin(min);
            mSeekBar2.setMax(max);
            mSeekBar2.setProgress(mSeekBarData2);
            mText2.setText(mSeekBarData2 + "");
        }

        if (mSeekBar3 != null && mText3 != null) {
            int min = 1;
            int max = 10;
            mSeekBar3.setMin(min);
            mSeekBar3.setMax(max);
            mSeekBar3.setProgress(mSeekBarData3);
            mText3.setText(mSeekBarData3 + "S");
        }

        if (mSeekBar4 != null && mText4 != null) {
            int min = 5;
            int max = 10;
            mSeekBar4.setMin(min);
            mSeekBar4.setMax(max);
            mSeekBar4.setProgress(mSeekBarData4);
            mText4.setText(mSeekBarData4 +"");
        }

        if (mSeekBar5 != null && mText5 != null) {
            int min = 1;
            int max = 50;
            mSeekBar5.setMin(min);
            mSeekBar5.setMax(max);
            mSeekBar5.setProgress(mSeekBarData5);
            mText5.setText(String.valueOf(mSeekBarData5));
        }

        if (mSeekBar6 != null && mText6 != null) {
            int min = 10;
            int max = 255;
            mSeekBar6.setMin(min);
            mSeekBar6.setMax(max);
            mSeekBar6.setProgress(mSeekBarData6);
            mText6.setText(mSeekBarData6 + "S");
        }
        if (mSeekBar7 != null && mText7 != null) {
            int min = 250;
            int max = 2000;
            mSeekBar7.setMin(min);
            mSeekBar7.setMax(max);
            mSeekBar7.setProgress(mSeekBarData7);
            mText7.setText(mSeekBarData7 + "ms");
        }

    }

    @Override
    protected void initView() {

    }

    //------------------------ble------------------------


    @Override
    public void onServiceSuccess() {
        if (mBluetoothService != null) {
            mBluetoothService.setOnCallback(this);
            BleDevice device = mBluetoothService.getBleDevice(mAddress);
            if (device != null) {
                mHealthBraceletDevice = HealthBraceletDevice.getInstance(device);
                mHealthBraceletDevice.setOnNotifyData(this);
                mHealthBraceletDevice.setOnBleVersionListener(this);
                mHandler.sendEmptyMessage(CONNECT_SUCCESS);
            } else {
                onServiceErr();
            }
        }
    }

    @Override
    public void onServiceErr() {
        myFinish();
    }

    @Override
    public void unbindServices() {
    }


    @Override
    public void onConnecting(String mac) {
        if (mac.equalsIgnoreCase(mAddress)) {
            mList.add(TimeUtils.getTime() + "connecting");
            mHandler.sendEmptyMessage(REFRESH_DATA);
        }
    }

    @Override
    public void onDisConnected(String mac, int code) {
        if (mac.equalsIgnoreCase(mAddress)) {
            mList.add(TimeUtils.getTime() + "Disconnect:" + code);
            mHandler.sendEmptyMessage(REFRESH_DATA);
            mHealthBraceletDevice = null;
        }

    }

    @Override
    public void onServicesDiscovered(String mac) {
        if (mac.equalsIgnoreCase(mAddress)) {
            if (mBluetoothService != null) {
                mBluetoothService.setOnCallback(this);
                BleDevice device = mBluetoothService.getBleDevice(mAddress);
                if (device != null) {
                    mHealthBraceletDevice = HealthBraceletDevice.getInstance(device);
                    mHealthBraceletDevice.setOnNotifyData(this);
                    mHealthBraceletDevice.setOnBleVersionListener(this);
                    mHandler.sendEmptyMessage(CONNECT_SUCCESS);
                } else {
                    onServiceErr();
                }
            }
        }

    }


    @Override
    public void bleOpen() {

    }

    @Override
    public void bleClose() {
        if (mHintDataDialogFragment == null)
            mHintDataDialogFragment = HintDataDialogFragment.newInstance().setTitle(null, 0)
                    .setContent("Bluetooth is not turned on", true).setCancel("cancel", 0).setOk("ok", 0).setBackground(true)
                    .setBottom(true).setDialogListener(new HintDataDialogFragment.DialogListener() {
                        @Override
                        public void tvSucceedListener(View v) {
                            Intent enableBtIntent =
                                    new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivity(enableBtIntent);
                        }
                    });

        mHintDataDialogFragment.show(getSupportFragmentManager());
    }


    @Override
    public void onHistoryRecord(List<HealthStatusHistoryRecordBean> list) {
        mList.add(TimeUtils.getTime() + "Start of history");
        if (!list.isEmpty()) {
            for (HealthStatusHistoryRecordBean healthStatusHistoryRecordBean : list) {
                mList.add(TimeUtils.getTime() + healthStatusHistoryRecordBean.toString());
            }
        }
        mList.add(TimeUtils.getTime() + "End of history:" + list.size());
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }


    @Override
    public void onDeviceStatus(int batteryStatus, int batteryPercentage, int wordStatus,
                               int userStatus) {
        String data;
        if (batteryStatus == 0) {
            data = "Battery status:normal";
        } else {
            data = "Battery status:Charge";

        }
        data += "\n";
        data += "battery power:" + batteryPercentage + "%";
        data += "\n";
        if (wordStatus == 1) {
            data += "Working state:activated";
        } else {
            data += "Working state:inactivated";

        }
        data += "\n";
        switch (userStatus) {

            case 0:
                data += "health status:0";
                break;
            case 1:
                data += "health status:1";
                break;
            case 2:
                data += "health status:2";
                break;

        }

        mList.add(TimeUtils.getTime() + data);
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }

    @Override
    public void onSetBack(int cmd, int status) {
        String cmdStr = "";
        switch (cmd) {
            case HealthBraceletBleConfig.SET_TIME:
                cmdStr = "synchronised time:";

                break;
            case HealthBraceletBleConfig.SET_USER:
                cmdStr = "Set up users:";
                break;

            case HealthBraceletBleConfig.SET_STATUS:
                cmdStr = "Set status:";
                break;
            case HealthBraceletBleConfig.GET_STATUS:
                cmdStr = "Get status:";
                break;
            case HealthBraceletBleConfig.GET_LOG:
                cmdStr = "Get records:";
                break;
            case HealthBraceletBleConfig.GET_LOG_END:
                cmdStr = "End of recording:";
                break;
            case HealthBraceletBleConfig.SET_DEVICE_INFO:
                cmdStr = "Setting parameters:";
                break;
            case HealthBraceletBleConfig.GET_DEVICE_INFO:
                cmdStr = "Get parameters:";
                break;
            case HealthBraceletBleConfig.SET_BIND:
                cmdStr = "Binding code judgment result:";
                if (status == 0) {
                    cmdStr += "The binding code is consistent, the binding is successful";
                } else if (status == 1) {
                    cmdStr += "The binding code is inconsistent, wait for the key";
                } else if (status == 2) {
                    cmdStr += "The binding code is inconsistent, the button has been confirmed, the binding is successful";
                }
                break;

            default:
                cmdStr = "instruction:" + cmd;
                break;

        }
        mList.add(TimeUtils.getTime() + "Reply:" + cmdStr + " status:" + status);
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }


    @Override
    public void onDeviceInfo(int nearAlarmDb, int nearAlarmNumber, int detachAlarmTime,
                             int detachAlarmNumber, int sensorSensitivity, int offlineTime,
                             int motorVibrationTime) {
        mSeekBarData1 = nearAlarmDb;
        mSeekBarData2 = nearAlarmNumber;
        mSeekBarData3 = detachAlarmTime;
        mSeekBarData4 = detachAlarmNumber;
        mSeekBarData5 = sensorSensitivity;
        mSeekBarData6 = offlineTime;
        mSeekBarData7 = motorVibrationTime;
        setCurrentParameter(mSeekBarData1, mSeekBarData2, mSeekBarData3, mSeekBarData4,
                mSeekBarData5, mSeekBarData6, mSeekBarData7);
        String str = "Near the alarm threshold=-" + nearAlarmDb + "db\n";
        str += "Threshold times received=" + nearAlarmNumber + "\n";
        str += "Breakaway alarm time=" + detachAlarmTime + "S\n";
        str += "Breakaway alarm times=" + detachAlarmNumber + "\n";
        str += "Sensitivity, the smaller the value, the more sensitive=" + sensorSensitivity + "\n";
        str += "Offline time=" + offlineTime + "S\n";
        if (motorVibrationTime != 0)
            str += "Motor vibration time=" + motorVibrationTime + "ms";
        mList.add(TimeUtils.getTime() + "Reply: Get parameters:\n" + str);
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }




    @Override
    public void onOtaSuccess() {
        mList.add(TimeUtils.getTime() + "OTA success");
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }

    @Override
    public void onOtaFailure(int code,String err) {
        mList.add(TimeUtils.getTime() + "OTA failed:" + err);
        mHandler.sendEmptyMessage(REFRESH_DATA);
        if (mHealthBraceletDevice != null) {
            mHealthBraceletDevice.disconnect();
        }
    }

    private int progressOld;

    @Override
    public void onOtaProgress(float progress) {
        int progressInt = (int) progress;
        if (progressOld != progressInt) {
            progressOld = progressInt;
            mList.add(TimeUtils.getTime() + "OTA progress:" + progressInt);
            mHandler.sendEmptyMessage(REFRESH_DATA);
        }

    }


    @Override
    public void onBmVersion(String version) {
        mList.add(TimeUtils.getTime() + "version number:" + version);
        mHandler.sendEmptyMessage(REFRESH_DATA);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
