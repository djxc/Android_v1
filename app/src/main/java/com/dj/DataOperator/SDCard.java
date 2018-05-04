package com.dj.DataOperator;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杜杰 on 2018/4/14.
 */

public class SDCard {

    public SDCard(Activity activity){
        getPermission(activity);
    }

    public String getSDPath() {
        return getSDDir().toString();
    }

    //获取手机内存卡位置
    public File getSDDir(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir;
    }

    //获取sd卡大小
    public static String getSpace(File sdDir){
        long totalSpace = sdDir.getTotalSpace()/(1024*1024*1024);
        return Long.toString(totalSpace);
    }

    /**
     * 获取外置SD卡路径
     * @return  应该就一条记录或空
     */
    public List<String> getExtSDCardPath()
    {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    /**
     * 获取外置SD卡路径以及TF卡的路径
     * <p>
     * 返回的数据：paths.get(0)肯定是外置SD卡的位置，因为它是primary external storage.
     *
     * @return 所有可用于存储的不同的卡的位置，用一个List来保存
     */
    public List<String> getExtSDCardPathList() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        //首先判断一下外置SD卡的状态，处于挂载状态才能获取的到
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            //外置SD卡的路径
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            while ((line = br.readLine()) != null) {
                // format of sdcard file system: vfat/fuse
                if ((!line.contains("fat") && !line.contains("fuse") && !line.contains("storage"))
                        || line.contains("secure")
                        || line.contains("asec")
                        || line.contains("firmware")
                        || line.contains("shell")
                        || line.contains("obb")
                        || line.contains("legacy") || line.contains("data")) {
                    continue;
                }
                String[] parts = line.split(" ");
                int length = parts.length;
                if (mountPathIndex >= length) {
                    continue;
                }
                String mountPath = parts[mountPathIndex];
                if (!mountPath.contains("/") || mountPath.contains("data")
                        || mountPath.contains("Data")) {
                    continue;
                }
                File mountRoot = new File(mountPath);
                if (!mountRoot.exists() || !mountRoot.isDirectory()
                        || !mountRoot.canWrite()) {
                    continue;
                }
                boolean equalsToPrimarySD = mountPath.equals(extFile
                        .getAbsolutePath());
                if (equalsToPrimarySD) {
                    continue;
                }
                //扩展存储卡即TF卡或者SD卡路径
                paths.add(mountPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public String readFile(String path){

        String result = "";
        try {
            File file = new File(new File(path),
                    "ab.txt");
            if(file!=null){
                FileInputStream inputStream = new FileInputStream(file);
                byte[] b = new byte[inputStream.available()];
                inputStream.read(b);
                result = new String(b);
                System.out.println("读取成功："+result);
            }else {
                result = "读取失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

     /**
     * 获取手机存储介质路径，包括手机内存，tf卡
     * @return
     */
    public List getStorages(StorageManager mStorageManager){
        String TAG = "dj";
        List<String> paths = new ArrayList();
        StorageVolume[] volumes = null;
        Object invokeVolumeList = null;
        Method getVolumeList= null;

        try {
            Log.d(TAG, "getSdDirectory2  mStorageManager="+mStorageManager);
            getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Log.d(TAG, "getSdDirectory2  getVolumeList="+getVolumeList);
            try {
                final Class<?>storageValumeClazz = Class.forName("android.os.storage.StorageVolume");
                final Method getPath= storageValumeClazz.getMethod("getPath");
                Method isRemovable = storageValumeClazz.getMethod("isRemovable");
                Method mGetState = null;
                if (Build.VERSION.SDK_INT >Build.VERSION_CODES.KITKAT) {
                    try {
                        mGetState = storageValumeClazz.getMethod("getState");
                    } catch(NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    invokeVolumeList= getVolumeList.invoke(mStorageManager);
                }catch (Exception e) {
                    Log.e(TAG,"couldn't talkto volumes", e);
                }
                final int length = Array.getLength(invokeVolumeList);
                Log.d(TAG,"getSdDirectory2 length="+length);
                for(int i = 0; i<length ;i++) {
                    final Object storageValume= Array.get(invokeVolumeList, i);//
                    final String path =(String) getPath.invoke(storageValume);
                    final boolean removable =(Boolean) isRemovable.invoke(storageValume);
                    String state = null;
                    if (mGetState !=null) {
                        state = (String) mGetState.invoke(storageValume);
                    }
                    Log.d(TAG,"getSdDirectory2path="+path+"  removable="+removable+" getState="+state);
                    paths.add(path);
                }
            } catch (Exception e) {
                Log.e(TAG,"couldn't talkto MountService", e);
            }
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
        return paths;
    }

    public void writeFile(Activity activity, String path){
        try {
            File file = new File(new File(path),
                    "ab1.txt");
            try{
                if(!file.exists()) {
                    file.mkdirs();//多级目录
                }
            }catch (Exception e){
                Log.d("dj",e.toString());
            }
            if(file!=null){
                String filepath = file.getPath();
                FileOutputStream outputStream = new FileOutputStream(filepath);// activity.openFileOutput(filepath, Context.MODE_PRIVATE);//
                outputStream.write("xcxc".getBytes());
                Log.d("dj","写入成功");
            }else {
                Log.d("dj","写入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermission(Activity activity){
        int write = activity.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = activity.checkCallingOrSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && read!= PackageManager.PERMISSION_GRANTED
                && write!= PackageManager.PERMISSION_GRANTED ) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    public void writeF(Activity activity){
        try{
            FileOutputStream fos = activity.openFileOutput("ab.txt", activity.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write("xcxc".toString());
            //osw.write("hello Wrold !");
            osw.flush();
            fos.flush();
            osw.close();
            fos.close();
            Toast.makeText(activity, "写入成功", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }
    }

    public void readF(Activity activity){
        String result = "";
        try{
            File file = new File("ab.txt");
            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            result = new String(b);
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d("dj",e.toString());
        }
    }

}
