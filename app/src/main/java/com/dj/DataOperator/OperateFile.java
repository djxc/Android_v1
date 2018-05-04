package com.dj.DataOperator;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by Administrator on 2018/5/3.
 */

public class OperateFile {
    final String TAG = "dj";

    /**
     * 向内存卡中写入数据
     * @param content
     * @param path
     */
    public void write(String content, String path){
        FileOutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try{
            outputStream = new FileOutputStream(new File(path + "test.txt"));
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            bufferedOutputStream.write(content.getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }catch (Exception e){
            Log.i(TAG, e.toString());
        }


    }

    /**
     * 读取内存卡中的数据
     * @param path
     */
    public void readFile(String path){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(path + "test.txt")));
            String tempStr = null;
            int line = 1;
            while((tempStr = reader.readLine())!=null){
                Log.i(TAG, tempStr);
                line++;
            }
            reader.close();
        }catch (Exception e){
            Log.i(TAG, e.toString());
        }finally {
            if(reader != null){
                try{
                    reader.close();
                }catch (Exception e){
                    Log.i(TAG, e.toString());
                }

            }
        }

    }

}
