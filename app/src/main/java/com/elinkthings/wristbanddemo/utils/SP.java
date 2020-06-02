package com.elinkthings.wristbanddemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SP {
    public static final int INT_DEFAULT = 0;
    public static final String STR_DEFAULT = "";
    private static SP instance;
    private SharedPreferences sp;
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "app_data";



    public static String OTA_FILE_NAME = "OTA_FILE_NAME";


    //-------------------------------------


    private SP(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SP getInstance() {
        return instance;
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SP.class) {
                if (instance == null) {
                    instance = new SP(context);
                }
            }
        }
    }




    public String getOtaFileName() {
        return sp.getString(OTA_FILE_NAME, STR_DEFAULT);
    }

    public void putOtaFileName(String name) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(OTA_FILE_NAME, name);
        apply(editor);
    }


    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        apply(editor);
    }

    /**
     * 清除用户相关的所有数据
     */
    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(OTA_FILE_NAME);

        apply(editor);
    }


    /**
     * 清除所有数据
     */
    public void clearAll() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        apply(editor);
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public int put(String key, Object object) {
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        apply(editor);
        return 0;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public Object get(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return defaultObject;
    }


    /**
     * 查询某个key是否已经存在
     */
    public boolean contains(Context context, String key) {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll(Context context) {
        return sp.getAll();
    }


    /**
     * 使用apply执行，否则使用commit
     */
    private static void apply(SharedPreferences.Editor editor) {
        try {
            editor.apply();
        } catch (Exception e) {
            editor.commit();
            e.printStackTrace();
        }

    }

}