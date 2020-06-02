package com.elinkthings.distrackerlibrary;

import java.util.UUID;

/**
 * xing<br>
 * 2019/12/11<br>
 * 指令
 */
public class HealthBraceletBleConfig {


    /**
     * 服务的uuid
     */
    public final static UUID UUID_SERVER = UUID.fromString("0000F1A0-0000-1000-8000-00805F9B34FB");
    /**
     * OTA服务
     */
    public  final static UUID SPOTA_SERVICE_UUID = UUID.fromString("0000fef5-0000-1000-8000-00805f9b34fb");
    /**
     * write
     */
    public final static UUID UUID_WRITE = UUID.fromString("0000FFB1-0000-1000-8000-00805F9B34FB");

    /**
     * Notify
     */
    public final static UUID UUID_NOTIFY = UUID.fromString("0000FFB2-0000-1000-8000-00805F9B34FB");

    /**
     * Write && Notify ( APP与BLE进行Inet交互的UUID) 独享
     */
    public final static UUID UUID_WRITE_NOTIFY = UUID
            .fromString("0000FFB1-0000-1000-8000-00805F9B34FB");

    /**
     * 名称过滤
     */
    public final static String NAME_FILTER = "DLG-BV01";


    /**
     * 下发用户
     */
    public final static int SET_USER = 0x01;
    /**
     * 设置时间
     */
    public final static int SET_TIME = 0x02;
    /**
     * 设置状态
     */
    public final static int SET_STATUS = 0x03;
    /**
     * 读取状态
     */
    public final static int GET_STATUS = 0x04;
    /**
     * 获取记录
     */
    public final static int GET_LOG = 0x05;
    /**
     * 获取记录结束
     */
    public final static int GET_LOG_END = 0x06;
    /**
     * 设置参数
     */
    public final static int SET_DEVICE_INFO = 0x07;
    /**
     * 读取参数
     */
    public final static int GET_DEVICE_INFO = 0x08;
    /**
     * 设置绑定
     */
    public final static int SET_BIND = 0x09;

    /**
     * 设置标定设备
     */
    public final static int SET_STANDARD_DEV = 0x0A;
    /**
     * 读取标定值
     */
    public final static int GET_STANDARD_VALUE = 0x0B;
    public final static int SET_STANDARD_VALUE = 0x0C;


}
