package com.serialportdemo;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jiang on 2017/12/28.
 */

public class SerialPort {
    //加载so文件
    static {
        System.loadLibrary("native-lib");
    }
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;
    private final String TAG = "SerialPort:";
    /**
     * @param  path 串口文件路径
     * @param baudrate 波特率，不同设备波特率有区别
     * */
    public SerialPort(String path, int baudrate) throws SecurityException, IOException {
        File device = new File(path);
        Logger.d(serialPortMsg());
        if(!device.canRead() || !device.canWrite()) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }

            } catch (Exception e) {
                e.getMessage();
            }

        }

        mFd = open(device.getAbsolutePath(), baudrate);
        Logger.d(TAG+"open commplete");
        if (mFd == null) {
            Logger.e(TAG, "native open returns null");
            throw new IOException();
        }

        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    //定义本地方法
    public native FileDescriptor open(String path, int baudrate);//, int flags
    public native void close();
    public native String serialPortMsg();
    public native String stringFromJNI();

}
