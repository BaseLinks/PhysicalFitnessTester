package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import cn.trinea.android.common.util.ShellUtils;

/**
 * Created by tony on 16-7-20.
 */
public class GPIO {

    private static final String LOG_TAG = "GPIO";
    public String port;
    public int pin;

    public static final int GPIOB_BASE = 32;
    public static final int GPIOB30 = GPIOB_BASE + 30;
    public static final int GPIOB31 = GPIOB_BASE + 31;
    public static final int GPIO_PRINTER_STATE = GPIOB30;

    public static final String DIRECTION_IN = "in";
    public static final String DIRECTION_OUT = "out";

    public static final int LOW = 0;
    public static final int HIGH = 1;

    //get direction of gpio
    public String getInOut()
    {
        String command = String.format("cat /sys/class/gpio/%s/direction",this.port);
        try {
            Process p = Runtime.getRuntime().exec(new String[] {"su", "-c", command});
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder text = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                text.append(line);
                text.append("\n");
            }
            String retour= text.toString();
            return retour;
        } catch (IOException e) {
            return "";
        }
    }

    // get state of gpio for input and output
    //test if gpio is configurate
    public int getState()
    {
        String command = String.format("cat /sys/class/gpio/%s/value",this.port);
        try {
            Process p = Runtime.getRuntime().exec(new String[] {"su", "-c", command});
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder text = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                text.append(line);
                text.append("\n");
            }
            try {
                String retour= text.toString();
                if(retour.equals("")){
                    return -1;
                } else 	{
                    return Integer.parseInt(retour.substring(0, 1));
                }
            } catch(NumberFormatException nfe) {
                return -1;
            }
        } catch (IOException e) {
            return -1;
        }
    }

    //set value of the output
    public boolean setState(int value){
        String command = String.format("echo %d > /sys/class/gpio/%s/value", value,this.port);
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, true);
        Log.e(LOG_TAG, "cr.result:" + cr.result + " cr.successMsg: " + cr.successMsg + " cr.errorMsg: " + cr.errorMsg);
        return true;
    }

    // set direction
    public boolean setInOut(String direction){
        String command = String.format("echo %s > /sys/class/gpio/%s/direction", direction,this.port);
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, true);
        Log.e(LOG_TAG, "cr.result:" + cr.result + " cr.successMsg: " + cr.successMsg + " cr.errorMsg: " + cr.errorMsg);
        return true;
    }

    //export gpio
    public boolean activationPin(){
        String command = String.format("echo %d > /sys/class/gpio/export", this.pin);
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, true);
        Log.e(LOG_TAG, "cr.result:" + cr.result + " cr.successMsg: " + cr.successMsg + " cr.errorMsg: " + cr.errorMsg);
        return true;
    }

    // unexport gpio
    public boolean desactivationPin(){
        String command = String.format("echo %d > /sys/class/gpio/unexport", this.pin);
        ShellUtils.CommandResult cr = ShellUtils.execCommand(command, true);
        Log.e(LOG_TAG, "cr.result:" + cr.result + " cr.successMsg: " + cr.successMsg + " cr.errorMsg: " + cr.errorMsg);
        return true;
    }

    //init the pin
    public int initPin(String direction){
        int retour=0;
        boolean ret=true;

        // see if gpio is already set
        retour=getState();
        if (retour==-1) {
            // unexport the gpio
            ret=desactivationPin();
            if(ret==false){ retour=-1; }

            //export the gpio
            ret=activationPin();
            if(ret==false){ retour=-2; }
        }

        // get If gpio direction is define
        String ret2 = getInOut();
        if (!ret2.contains(direction))
        {
            // set the direction (in or out)
            ret=setInOut(direction);
            if(ret==false){ retour=-3; }
        }

        return retour;
    }

    //Constructor
    private GPIO(int pin){
        this.port = "gpio"+pin;
        this.pin = pin;
    }

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static GPIO singleton = null;
    public static GPIO getInstance(int pin)   {
        if (singleton== null)  {
            synchronized (Printer.class) {
                if (singleton== null)  {
                    singleton= new GPIO(pin);
                }
            }
        }
        return singleton;
    }
}
