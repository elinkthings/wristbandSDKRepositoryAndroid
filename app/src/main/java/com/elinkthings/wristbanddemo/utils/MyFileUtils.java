package com.elinkthings.wristbanddemo.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class MyFileUtils {
    private static String TAG = MyFileUtils.class.getName();
    private static String filesDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String FILE_DIR="wristbandDemo";
    public static String FILE_NAME="updateName.img";



    public static void init(){
        filesDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        createFileDirectories(FILE_DIR);
    }


    public static String getByFileName(){
        return filesDir + File.separator ;
    }



    /**
     * 创建文件夹
     */
    private static void createFileDirectories(String directoryName) {
        filesDir = filesDir + File.separator + directoryName;
        File directory = new File(filesDir);
        if (!directory.exists()) {
            boolean mkdirs = directory.mkdirs();
            L.i(TAG,"创建OTA文件:"+mkdirs);
        }
    }


    public static ArrayList<String> list() {
        File f = new File(filesDir);
        File file[] = f.listFiles();
        if (file==null)
            return new ArrayList<>();
        Arrays.sort(file, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getPath().compareToIgnoreCase(rhs.getPath());
            }
        });
        L.d("Files", "Size: " + file.length);
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            L.d("Files", "FileName:" + file[i].getName());
            names.add(file[i].getName());
        }
        return names;
    }

}
