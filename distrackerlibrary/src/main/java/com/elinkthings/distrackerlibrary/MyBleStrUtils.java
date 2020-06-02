package com.elinkthings.distrackerlibrary;

/**
 * xing<br>
 * 2019/3/7<br>
 * 广播解析工具类
 */
public class MyBleStrUtils {


    /**
     * (16进制)
     * BLE蓝牙返回的byte[]
     * byte[]转字符串
     */
    public static String byte2HexStr(byte[] b) {
        if (b == null)
            return "";
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (byte aB : b) {
            int a = aB & 0XFF;
            stmp = getHexString(a);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
            hs.append(" ");
        }
        return hs.toString();
    }

    /**
     * int  10进制转16进制(返回大写字母)
     *
     * @return 16进制String
     */
    public static String getHexString(int number) {
        return Integer.toHexString(number);
    }


    /**
     * int到byte[] 由高位到低位
     * @param i 需要转换为byte数组的整行值。
     * @return byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    /**
     * 时间戳(秒)转byte数组 由高位到低位
     * @param time 时间戳,精确到秒
     * @return byte[5]数组
     */
    public static byte[] getTimestampByte(long time) {
        byte[] result = new byte[5];
        result[4] = (byte)((time >> 32) & 0xFF);
        result[3] = (byte)((time >> 24) & 0xFF);
        result[2] = (byte)((time >> 16) & 0xFF);
        result[1] = (byte)((time >> 8) & 0xFF);
        result[0] = (byte)(time & 0xFF);
        return result;
    }


    /**
     * byte[]转long
     * @param bytes 需要转换的数组
     * @param isBig 是否为大端序
     * @return 数值
     */
    public static long byteArrayToLong(byte[] bytes,boolean isBig) {
        long value=0;
        int length = bytes.length;
        if (isBig){
            for(int i = length; i >0; i--) {
                int shift= ((length-1)-(length-i)) * 8;
                value +=(bytes[(i-1)] & 0xFF) << shift;
            }
        }else {
            for(int i = 0; i <length; i++) {
                int shift= ((length-1)-i) * 8;
                value +=(bytes[i] & 0xFF) << shift;
            }
        }
        return value;
    }


    /**
     * (16进制)
     * BLE蓝牙返回的byte[]
     * byte[]转字符串
     */
    public static String getMac(byte[] b,boolean isBig) {
        if (b == null)
            return "";
        StringBuilder hs = new StringBuilder();
        String stmp;
        if (isBig){
            for (byte aB : b) {
                int a = aB & 0XFF;
                stmp = getHexString(a);
                if (stmp.length() == 1)
                    hs.append("0").append(stmp);
                else
                    hs.append(stmp);
                hs.append(":");
            }
        }else {

            for (int i = b.length - 1; i >= 0; i--) {
                byte aB=b[i];
                int a = aB & 0XFF;
                stmp = getHexString(a);
                if (stmp.length() == 1)
                    hs.append("0").append(stmp);
                else
                    hs.append(stmp);
                hs.append(":");
            }
        }

        hs.deleteCharAt(hs.length()-1);
        return hs.toString();
    }



    /**
     * mac地址转byte
     * @param mac
     * @return
     */
    public static byte[] getDeviceMacByte(String mac) {
        byte[] macByte = new byte[6];
        if (mac.contains(":")) {
            String[] macArr = mac.split(":");
            for (int i = 0; i < macArr.length; i++) {
                macByte[macArr.length - i - 1] = (byte) Integer.parseInt(macArr[i], 16);
            }
        }
        return macByte;
    }

}
