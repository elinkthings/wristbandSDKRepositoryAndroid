package com.elinkthings.wristbanddemo.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * 打印日志工具
 */
public class L {
    /**
     * 是否开启log日志
     */
    private static boolean isLog = true;
    private static String TAG = "TagOther";

    /**
     * 日志初始化
     *
     * @param isShowLog 是否打印日志
     */
    public static void init(boolean isShowLog) {
        isLog = isShowLog;
    }

    /**
     * 详细日志
     */
    public static void v(String tag, String msg) {
        if (isLog) {
            logContent(tag, msg, 4);
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    /**
     * 错误日志
     */
    public static void e(String tag, String msg) {
        if (isLog) {
            logContent(tag, msg, 1);
        }
    }


    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * 警告日志
     */
    public static void w(String tag, String msg) {
        if (isLog) {
            logContent(tag, msg, 3);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }


    /**
     * 信息日志
     */
    public static void i(String tag, String msg) {
        if (isLog) {
            logContent(tag, msg, 0);
        }
    }


    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * 调试日志
     */
    public static void d(String tag, String msg) {
        if (isLog) {
            logContent(tag, msg, 2);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }


    private static void logContent(String msg) {
        logContent("", msg, 0);
    }


    /**
     * @param tag   tag
     * @param msg   内容
     * @param level 0=i;1=e;2=d;3=w;其他=v;
     */
    private static void logContent(String tag, String msg, int level) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int methodCount = 1;
        int stackOffset = getStackOffset(trace);
        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StackTraceElement element = trace[stackIndex];
            switch (level) {
                case 0:
                    Log.i(tag, getLogContent(msg, element));
                    break;
                case 1:
                    Log.e(tag, getLogContent(msg, element));
                    break;
                case 2:
                    Log.d(tag, getLogContent(msg, element));
                    break;
                case 3:
                    Log.w(tag, getLogContent(msg, element));
                    break;
                default:
                    Log.v(tag, msg);
                    break;
            }

        }
    }


    private static String getLogContent(String msg, StackTraceElement element) {
        StringBuilder builder = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = sdf.format(new Date());
        builder.append("\n")
                .append("-------------------------------------------------------\n")
                .append("|")
                .append(time)
                .append("\n")
                .append("|")
                .append(getSimpleClassName(element.getClassName()))
                .append(".")
                .append(element.getMethodName())
                .append(" ")
                .append(" (")
                .append(element.getFileName())
                .append(":")
                .append(element.getLineNumber())
                .append(")")
                .append("\n")
                .append("||==>")
                .append(msg)
                .append("\n")
                .append("-------------------------------------------------------\n");
        return builder.toString();
    }


    private static int getStackOffset(StackTraceElement[] trace) {
        for (int i = 2; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            String l = L.class.getName();
            if (!name.equals(l)) {
                return --i;
            }
        }
        return -1;
    }

    private static String getSimpleClassName(@NonNull String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

}
