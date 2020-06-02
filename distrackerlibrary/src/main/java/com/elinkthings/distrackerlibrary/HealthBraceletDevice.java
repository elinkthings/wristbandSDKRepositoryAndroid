package com.elinkthings.distrackerlibrary;

import android.os.Handler;
import android.os.Looper;

import com.pingwang.bluetoothlib.device.BaseBleDeviceData;
import com.pingwang.bluetoothlib.device.BleDevice;
import com.pingwang.bluetoothlib.device.BleSendCmdUtil;
import com.pingwang.bluetoothlib.device.SendBleBean;
import com.pingwang.bluetoothlib.device.SendMcuBean;
import com.pingwang.bluetoothlib.listener.OnBleVersionListener;
import com.pingwang.bluetoothlib.listener.OnDialogOTAListener;

import java.util.ArrayList;
import java.util.List;


/**
 * xing<br>
 * 2020/05/15<br>
 * ble设备对象
 */
public class HealthBraceletDevice extends BaseBleDeviceData {
    private String TAG = HealthBraceletDevice.class.getName();

    private onNotifyData mOnNotifyData;
    private static BleDevice mBleDevice = null;
    private static HealthBraceletDevice sMHealthBraceletDevice = null;
    private int mDeviceId = -1;
    private List<HealthStatusHistoryRecordBean> mList;
    private int mCid = 0x0001;

    public static HealthBraceletDevice getInstance(BleDevice bleDevice) {
        synchronized (BleDevice.class) {
            if (mBleDevice == bleDevice) {
                if (sMHealthBraceletDevice == null) {
                    sMHealthBraceletDevice = new HealthBraceletDevice(bleDevice);
                }
            } else {
                sMHealthBraceletDevice = new HealthBraceletDevice(bleDevice);
            }
        }
        return sMHealthBraceletDevice;
    }

    public void startDialogOta(String pathName, OnDialogOTAListener listener) {
        if (mBleDevice != null) {
            mBleDevice.setOnDialogOTAListener(listener);
            mBleDevice.startDialogOta(pathName);
        }
    }


    /**
     * 断开时清空单例
     * Clear singleton on disconnect
     */
    public void clear() {
        if (sMHealthBraceletDevice != null) {
            mOnNotifyData = null;
            sMHealthBraceletDevice = null;
        }
    }

    public void disconnect() {
        if (mBleDevice != null)
            mBleDevice.disconnect();
    }

    private HealthBraceletDevice(BleDevice bleDevice) {
        super(bleDevice);
        mBleDevice = bleDevice;
        mList = new ArrayList<>();
    }


    /**
     * Reset
     */
    public void reboot() {
        mBleDevice.reboot();
    }

    /**
     * 获取版本号
     * Get version number
     */
    public void getVersion() {
        SendBleBean sendBleBean = new SendBleBean();
        sendBleBean.setHex(BleSendCmdUtil.getInstance().getBleVersion());
        sendData(sendBleBean);
    }


    /**
     * 设置标定设备
     * Setting up calibration equipment
     */
    public void setStandardDev(String mac) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[7];
        data[0] = HealthBraceletBleConfig.SET_STANDARD_DEV;
        byte[] deviceMacByte = MyBleStrUtils.getDeviceMacByte(mac);
        System.arraycopy(deviceMacByte, 0, data, 1, deviceMacByte.length);
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    /**
     * 读取标定值
     * Read calibration value
     */
    public void getStandardValue() {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[2];
        data[0] = HealthBraceletBleConfig.GET_STANDARD_VALUE;
        data[1] = 0x01;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }

    /**
     * 设置标定值
     *Set calibration value
     * @param rssi 30-90
     */
    public void setStandardValue(int rssi) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[2];
        data[0] = HealthBraceletBleConfig.SET_STANDARD_VALUE;
        data[1] = (byte) rssi;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    /**
     * 设置绑定
     * Set up binding
     */
    public void setBind(int userId) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[5];
        data[0] = HealthBraceletBleConfig.SET_BIND;
        byte[] bytes = MyBleStrUtils.intToByteArray(userId);
        System.arraycopy(bytes, 0, data, 1, bytes.length);
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }

    /**
     * 同步时间
     * synchronised time
     */
    public void setSynTime() {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[7];
        data[0] = HealthBraceletBleConfig.SET_TIME;
        byte[] bytes = MyBleStrUtils.getTimestampByte((System.currentTimeMillis() / 1000L));
        System.arraycopy(bytes, 0, data, 1, bytes.length);
        data[6] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    /**
     * 下发用户
     * Send users
     */
    public void setUser(int userId, int status) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[7];
        data[0] = HealthBraceletBleConfig.SET_USER;
        byte[] bytes = MyBleStrUtils.intToByteArray(userId);
        System.arraycopy(bytes, 0, data, 1, bytes.length);
        data[5] = (byte) status;
        data[6] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }

    /**
     * 设置状态
     *  Set status
     * @param status 1：激活设备，开始工作 2：清除激活状态，等待激活 3：关机
     *               1: Activate the device and start working 2: Clear the activation status and wait for activation 3: Shut down
     */
    public void setStatus(int status) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[3];
        data[0] = HealthBraceletBleConfig.SET_STATUS;
        data[1] = (byte) status;
        data[2] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    /**
     * 读取状态
     * Read status
     */
    public void getStatus() {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[3];
        data[0] = HealthBraceletBleConfig.GET_STATUS;
        data[1] = 0x01;
        data[2] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    /**
     * 读取历史记录
     * Read history
     */
    public void getHistoryRecord() {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[3];
        data[0] = HealthBraceletBleConfig.GET_LOG;
        data[1] = 0x01;
        data[2] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
        if (mList != null) {
            mList.clear();
        }
    }

    /**
     * 设置参数
     *Setting parameters
     * @param nearAlarmDb        靠近报警阀值，单位 -dbm
     *                           Near the alarm threshold, unit -dbm
     * @param nearAlarmNumber    接收的阀值次数（最大值 10，默认 2）
     *                           Threshold times received
     * @param detachAlarmTime    脱离报警时间：默认 1s
     *                           Breakaway alarm time
     * @param detachAlarmNumber  脱离报警次数：默认 10 次
     *                           Breakaway alarm times
     * @param sensorSensitivity  Gsensor 灵敏度，值越小越灵敏 (3-70 建议范围)
     *                           Gsensor Sensitivity, the smaller the value, the more sensitive
     * @param offlineTime        脱机时间，Gsensor 不动时间认为是脱机。默认 10 ，单位 s
     *                           Offline time, Gsensor fixed time is considered offline
     * @param motorVibrationTime 马达震动时间（范围 250-2000 ，默认 500，单位 ms）
     *                           Motor vibration time
     */
    public void setDeviceParameter(int nearAlarmDb, int nearAlarmNumber, int detachAlarmTime,
                                   int detachAlarmNumber, int sensorSensitivity, int offlineTime,
                                   int motorVibrationTime) {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[10];
        data[0] = HealthBraceletBleConfig.SET_DEVICE_INFO;
        data[1] = (byte) nearAlarmDb;
        data[2] = (byte) nearAlarmNumber;
        data[3] = (byte) detachAlarmTime;
        data[4] = (byte) detachAlarmNumber;
        data[5] = (byte) sensorSensitivity;
        data[6] = (byte) offlineTime;
        data[7] = (byte) ((motorVibrationTime >> 8) & 0xff);
        data[8] = (byte) (motorVibrationTime);
        data[9] = 0x00;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    public void getDeviceParameter() {
        SendMcuBean sendMcuBean = new SendMcuBean();
        byte[] data = new byte[2];
        data[0] = HealthBraceletBleConfig.GET_DEVICE_INFO;
        data[1] = (byte) 0x01;
        sendMcuBean.setHex(mCid, data);
        sendData(sendMcuBean);
    }


    //----------

    @Override
    public void onNotifyData(byte[] hex, int type) {
        if (hex == null) {
            return;
        }
        dataCheck(hex);
    }

    //----------------解析数据------

    /**
     * 校验解析数据
     *Verify parsing data
     * @param data Payload data
     */
    private void dataCheck(byte[] data) {
        if (data == null)
            return;
        int cmd = data[0] & 0xff;
        int status;
        switch (cmd) {


            case HealthBraceletBleConfig.SET_USER:
            case HealthBraceletBleConfig.SET_DEVICE_INFO:
            case HealthBraceletBleConfig.SET_TIME:
            case HealthBraceletBleConfig.SET_STATUS:
            case HealthBraceletBleConfig.SET_BIND:
            case HealthBraceletBleConfig.SET_STANDARD_VALUE:
            case HealthBraceletBleConfig.SET_STANDARD_DEV:
                status = data[1] & 0xff;
                runOnMainThread(() -> {
                    if (mOnNotifyData != null) {
                        mOnNotifyData.onSetBack(cmd, status);
                    }
                });
                break;


            case HealthBraceletBleConfig.GET_STATUS:
                int batteryStatus = data[1] & 0xff;
                int batteryPercentage = data[2] & 0xff;
                int wordStatus = data[3] & 0xff;
                int userStatus = data[4] & 0xff;
                runOnMainThread(() -> {
                    if (mOnNotifyData != null) {
                        mOnNotifyData
                                .onDeviceStatus(batteryStatus, batteryPercentage, wordStatus,
                                        userStatus);
                    }
                });
                break;

            case HealthBraceletBleConfig.GET_LOG:
                getHistoryRecordParsing(data);

                break;

            case HealthBraceletBleConfig.GET_LOG_END:
                runOnMainThread(() -> {
                    if (mOnNotifyData != null) {
                        mOnNotifyData.onHistoryRecord(mList);
                    }
                });


                break;

            case HealthBraceletBleConfig.GET_DEVICE_INFO:
                getDeviceInfo(data);
                break;
            case HealthBraceletBleConfig.GET_STANDARD_VALUE:
                int rssi = data[1] & 0xff;
                runOnMainThread(() -> {
                    if (mOnNotifyData != null) {
                        mOnNotifyData.onStandardValue(rssi);
                    }
                });
                break;


            default:

                runOnMainThread(() -> {
                    if (mOnNotifyData != null) {
                        mOnNotifyData.onData(data);
                    }
                });

                break;
        }

    }

    /**
     * 解析历史记录
     * Parsing history
     */
    private void getHistoryRecordParsing(byte[] data) {
        int logSize = (data.length - 1) / 23;//获得有多少组记录
        for (int i = 0; i < logSize; i++) {
            int cmdIndex = i * 23 + 1;
            byte[] macByte = new byte[6];
            System.arraycopy(data, cmdIndex, macByte, 0, macByte.length);
            cmdIndex += macByte.length;
            String deviceMac = MyBleStrUtils.getMac(macByte, false);

            byte[] userIdByte = new byte[4];
            System.arraycopy(data, cmdIndex, userIdByte, 0, userIdByte.length);
            cmdIndex += userIdByte.length;
            int userId = (int) MyBleStrUtils.byteArrayToLong(userIdByte, false);
            if (userId == 0)
                continue;
            int healthStatusCode = data[cmdIndex] & 0xff;//健康码
            cmdIndex++;
            int wordStatus = data[cmdIndex] & 0xff;//工作状态
            cmdIndex++;
            byte[] startTimeByte = new byte[5];
            System.arraycopy(data, cmdIndex, startTimeByte, 0, startTimeByte.length);
            cmdIndex += startTimeByte.length;
            long startTime = MyBleStrUtils.byteArrayToLong(startTimeByte, true);
            byte[] stopTimeByte = new byte[5];
            System.arraycopy(data, cmdIndex, stopTimeByte, 0, stopTimeByte.length);
            long stopTime = MyBleStrUtils.byteArrayToLong(stopTimeByte, true);
            cmdIndex += startTimeByte.length;

            byte tempB = data[cmdIndex];
            float temp = 30F;
            temp += ((tempB & 0xFF) / 10F);

            mList.add(new HealthStatusHistoryRecordBean(deviceMac, userId, healthStatusCode,
                    wordStatus, startTime, stopTime, temp));

        }

    }


    /**
     * 设备详细参数信息解析
     * Device detailed parameter information analysis
     */
    private void getDeviceInfo(byte[] data) {

        int nearAlarmDb = data[1] & 0xff;
        int nearAlarmNumber = data[2] & 0xff;
        int detachAlarmTime = data[3] & 0xff;
        int detachAlarmNumber = data[4] & 0xff;
        int sensorSensitivity = data[5] & 0xff;
        int offlineTime = data[6] & 0xff;
        if (data.length > 8) {
            int motorVibrationTime = ((data[7] & 0xff) << 8) + (data[8] & 0xff);
            runOnMainThread(() -> {
                if (mOnNotifyData != null) {
                    mOnNotifyData
                            .onDeviceInfo(nearAlarmDb, nearAlarmNumber, detachAlarmTime,
                                    detachAlarmNumber, sensorSensitivity, offlineTime,
                                    motorVibrationTime);
                }
            });
        } else {
            runOnMainThread(() -> {
                if (mOnNotifyData != null) {
                    mOnNotifyData
                            .onDeviceInfo(nearAlarmDb, nearAlarmNumber, detachAlarmTime,
                                    detachAlarmNumber, sensorSensitivity, offlineTime, 0);
                }
            });
        }

    }


    //----------------接口------


    public interface onNotifyData {
        /**
         * 不能识别的透传数据
         * Unrecognized pass-through data
         */
        default void onData(byte[] data) {
        }


        /**
         * 历史记录
         * history record
         *
         * @param list 记录列表
         *             Record list
         */
        default void onHistoryRecord(List<HealthStatusHistoryRecordBean> list) {
        }


        /**
         * 设备状态
         * equipment status
         *
         * @param batteryStatus     电池状态： 0：正常 1：充电
         *                          Battery status: 0: normal 1: charged
         * @param batteryPercentage 电量状态： 0-100 （0%-100%）
         *                          Power status: 0-100 (0%-100%)
         * @param wordStatus        工作状态 1：已激活 2：未激活
         *                          Working status 1: activated 2: not activated
         * @param userStatus        用户状态:健康状态 0：绿码 1：黄码 2：红码
         *                          User status: Health status 0: Green code 1: Yellow code 2: Red code
         */
        default void onDeviceStatus(int batteryStatus, int batteryPercentage, int wordStatus,
                                    int userStatus) {
        }


        /**
         * 设备的详细参数信息
         *Detailed parameter information of the device
         * @param nearAlarmDb       靠近报警阀值
         *                          Near the alarm threshold
         * @param nearAlarmNumber   接收的阀值次数
         * @param detachAlarmTime   脱离报警时间
         * @param detachAlarmNumber 脱离报警次数
         * @param sensorSensitivity Gsensor 灵敏度，值越小越灵敏
         * @param offlineTime       脱机时间，Gsensor 不动时间认为是脱机
         */
        default void onDeviceInfo(int nearAlarmDb, int nearAlarmNumber, int detachAlarmTime,
                                  int detachAlarmNumber, int sensorSensitivity, int offlineTime,
                                  int motorVibrationTime) {


        }


        /**
         * 设置操作类返回
         *
         * @param cmd    指令
         * @param status 0x00：设置成功 0x01：设置失败 0x02：不支持设置
         */
        default void onSetBack(int cmd, int status) {
        }

        /**
         * 读取的标定值
         *
         * @param rssi
         */
        default void onStandardValue(int rssi) {
        }

    }


    //-----------------set/get-----------------
    public void setOnNotifyData(onNotifyData onNotifyData) {
        mOnNotifyData = onNotifyData;
    }

    public void setOnBleVersionListener(OnBleVersionListener listener) {
        if (mBleDevice != null) {
            mBleDevice.setOnBleVersionListener(listener);
        }

    }


    private Handler threadHandler = new Handler(Looper.getMainLooper());


    private void runOnMainThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            threadHandler.post(runnable);
        }
    }

}
