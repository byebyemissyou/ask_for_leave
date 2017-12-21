package com.kade.lyx.ask_for_leave;

import android.app.Application;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

/**
 * Created by zh on 2017/7/20.
 */

public class MyApplication extends Application {
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    public static MyApplication getInstence() {
        if (myApplication == null) {
            return new MyApplication();
        } else {
            return myApplication;
        }
    }

    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
//            mSerialPort = new SerialPort(new File("/dev/" + "ttyS4"), 115200, 0);//安卓主板3288最老款那个设备采用的是连接ttyS4
            mSerialPort = new SerialPort(new File("/dev/" + "ttyS1"), 115200, 0);//最新设备都是安装ttyS1
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
