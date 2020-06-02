package com.elinkthings.distrackerlibrary;

/**
 * xing<br>
 * 2020/5/5<br>
 * 健康状态历史记录bean
 */
public class HealthStatusHistoryRecordBean {


    /**
     * 设置Mac地址
     */
    private String mDeviceMac;
    /**
     * 用户ID,与设备绑定的
     */
    private int mUserId;
    /**
     * 健康码状态
     */
    private int mHealthStatusCode;
    /**
     * 工作状态 0：正常工作（靠近警报） 1：脱机状态 2：关机状态
     */
    private int mWordStatus;
    /**
     * 开始时间戳(精确到秒)
     */
    private long mStartTimestamp;
    /**
     * 结束时间戳(精确到秒)
     */
    private long mStopTimestamp;

    private float mTemperature;


    public HealthStatusHistoryRecordBean(String deviceMac, int userId, int healthStatusCode,
                                         int wordStatus, long startTimestamp, long stopTimestamp,
                                         float temperature) {
        mDeviceMac = deviceMac;
        mUserId = userId;
        mHealthStatusCode = healthStatusCode;
        mWordStatus = wordStatus;
        mStartTimestamp = startTimestamp;
        mStopTimestamp = stopTimestamp;
        mTemperature = temperature;
    }

    public HealthStatusHistoryRecordBean(String deviceMac, int userId, int healthStatusCode,
                                         int wordStatus, long startTimestamp, long stopTimestamp) {
        mDeviceMac = deviceMac;
        mUserId = userId;
        mHealthStatusCode = healthStatusCode;
        mWordStatus = wordStatus;
        mStartTimestamp = startTimestamp;
        mStopTimestamp = stopTimestamp;
    }

    public String getDeviceMac() {
        return mDeviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        mDeviceMac = deviceMac;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getHealthStatusCode() {
        return mHealthStatusCode;
    }

    public void setHealthStatusCode(int healthStatusCode) {
        mHealthStatusCode = healthStatusCode;
    }

    public int getWordStatus() {
        return mWordStatus;
    }

    public void setWordStatus(int wordStatus) {
        mWordStatus = wordStatus;
    }

    public long getStartTimestamp() {
        return mStartTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        mStartTimestamp = startTimestamp;
    }

    public long getStopTimestamp() {
        return mStopTimestamp;
    }

    public void setStopTimestamp(long stopTimestamp) {
        mStopTimestamp = stopTimestamp;
    }


    public float getTemperature() {
        return mTemperature;
    }

    public void setTemperature(float temperature) {
        mTemperature = temperature;
    }


    @Override
    public String toString() {
        return "HealthStatusHistoryRecordBean{" + "mDeviceMac='" + mDeviceMac + '\'' + ", mUserId" +
                "=" + mUserId + ", mHealthStatusCode=" + mHealthStatusCode + ", mWordStatus=" + mWordStatus + ", mStartTimestamp=" + mStartTimestamp + ", mStopTimestamp=" + mStopTimestamp + ", mTemperature=" + mTemperature + '}';
    }
}
