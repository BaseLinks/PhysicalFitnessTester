package com.kangear.bodycompositionanalyzer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import cn.trinea.android.common.util.ShellUtils;
import flipagram.assetcopylib.AssetCopier;

/**
 * Created by tony on 18-1-22.
 */

public class BcaService extends Service {
    private static final String TAG = "BcaService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // bootanimation.zip路径有两个：
    // /data/local/bootanimation.zip
    // /system/media/bootanimation.zip
    // drwxr-x--x root     root              2018-01-21 19:13 local
    // 需要root cmd实现
    public static void installBootAnimation(Context context) throws Exception {
        // 将printer.tar.gz解包
        // 1. re
        remount();

        // 读取busybox 写入/system/bin/busybox
        int count = -1;
        // This will fail if the user didn't allow the permissions
        File destDir = context.getCacheDir();
        count = new AssetCopier(context)
                .withFileScanning()
                .copy("system/bootanimation", destDir);

        ShellUtils.CommandResult cr;
        String cmd = "busybox cp " + context.getCacheDir() + "/bootanimation.zip /data/local/";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }

        cmd = "busybox chmod 777 /data/local/bootanimation.zip";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }
    }

    // bootanimation.zip路径有两个：
    // /data/local/bootanimation.zip
    // /system/media/bootanimation.zip
    // drwxr-x--x root     root              2018-01-21 19:13 local
    // 需要root cmd实现
    public static void installApks(Context context) throws Exception {
        // 将printer.tar.gz解包
        // 1. re
        remount();

        // 读取busybox 写入/system/bin/busybox
        int count = -1;
        // This will fail if the user didn't allow the permissions
        File destDir = new File("/sdcard");
        count = new AssetCopier(context)
                .withFileScanning()
                .copy("system/apk", destDir);

        ShellUtils.CommandResult cr;
        String cmd;

        cmd = "pm install -r " + destDir.getAbsolutePath() + "/wifi.apk";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }


        cmd = "busybox rm " + destDir.getAbsolutePath() + "/wifi.apk";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }
    }

    // 安装字体
    public static void installNotoFonts(Context context) throws Exception {
        if (true)
            return;

        // 将printer.tar.gz解包
        // 1. re
        remount();

        // 读取busybox 写入/system/bin/busybox
        int count = -1;
        // This will fail if the user didn't allow the permissions
        File destDir = context.getCacheDir();
        count = new AssetCopier(context)
                .withFileScanning()
                .copy("system/fonts", destDir);

        ShellUtils.CommandResult cr;
        String cmd = "busybox tar xvf " + context.getCacheDir() + "/NotoSourceChinese.tgz -C /";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }
    }


    // 安装Printer Driver
    public static void installPrinterDriver(Context context) throws Exception {
        // 将printer.tar.gz解包
        // 1. re
        remount();

        // 读取busybox 写入/system/bin/busybox
        int count = -1;
        // This will fail if the user didn't allow the permissions
        File destDir = context.getCacheDir();
        count = new AssetCopier(context)
                .withFileScanning()
                .copy("system/printer", destDir);

        ShellUtils.CommandResult cr;
        String cmd = "busybox tar xvf " + context.getCacheDir() + "/phaser_3020_driver.tgz -C /";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }
    }

    /**
     * 安装busybox
     * @return
     */
    public static boolean installBusybox(Context context) throws Exception {
        boolean ret = false;
        // 将printer.tar.gz解包
        // 1. re
        remount();

        // 读取busybox 写入/system/bin/busybox
        int count = -1;
        // This will fail if the user didn't allow the permissions
        File destDir = context.getCacheDir();
        count = new AssetCopier(context)
                .withFileScanning()
                .copy("system/xbin", destDir);

        ShellUtils.CommandResult cr;
        String cmd = "cp " + context.getCacheDir() + "/busybox" + " /system/bin";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }

        cmd = "chmod 777 /system/bin/busybox";
        cr = ShellUtils.execCommand(cmd, true);
        if(cr.result != 0) {
            throw new Exception(cmd +" fail");
        }

        return ret;
    }

    /**
     * return
     * @return
     */
    public static void remount() throws Exception {
        // 将printer.tar.gz解包
        // 1. re
        ShellUtils.CommandResult cr;
        if (!ShellUtils.checkRootPermission()) {
            throw new Exception("无root权限");
        }

        cr = ShellUtils.execCommand("mount -o remount /system", true);
        if(cr.result != 0) {
            throw new Exception("mount -o remount /system fail");
        }

        cr = ShellUtils.execCommand("mount -o remount /", true);
        if(cr.result != 0) {
            throw new Exception("mount -o remount /fail");
        }
    }
}
