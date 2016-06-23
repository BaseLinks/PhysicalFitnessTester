package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.Arrays;

import android_serialport_api.ComBean;
import android_serialport_api.SerialHelper;
import android_serialport_api.SerialPortFinder;

/**
 * Created by tony on 16-6-23.
 */
public class BodyCompositionAnalyzer {
 	private static final boolean DEBUG = true;
	private static final String LOG_TAG = "BodyCompositionAnalyzer";
	/** */
	private final SerialHelper serialCtrl       = new SerialControl();;
	private final SerialPortFinder serialPortFinder = new SerialPortFinder();
	private String                 serialPort       = null;

	/**
	 * 需要发送的数据
	 */
	public static final byte[] SEND = new byte[]{(byte)0xAA, (byte)0xAB, 0x00, 0x04, (byte)0xDE, 0x00, (byte)0xDD, 0x6A};
	private static final int BYTE_BUFFER_ALLOCATE = 8192;

	/** 串口相关 */
	private static final String BAUDRATE_COIN = "9600";

	private class SerialControl extends SerialHelper {
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			handleRecData(ComRecData);
		}

		/** 处理接收到串口数据事件 */
		private void handleRecData(final ComBean ComRecData) {
			if (DEBUG) Log.i(LOG_TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
			if (DEBUG) Log.i(LOG_TAG, "handleRecStri:" + new String(ComRecData.bRec));
			parseData(ComRecData.bRec);

		}
	}

	private void parseData(byte[] inputData) {
		Log.i(LOG_TAG, "parseData");
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        /* 1. 判断回复是否正常 */
		byte[] ack = new byte[BodyComposition.ACK_LENGTH];
		System.arraycopy(inputData, BodyComposition.ACK_START, ack, 0, BodyComposition.ACK_LENGTH);
		if(Arrays.equals(ack, BodyComposition.ACK)) {
            /* 提取各个数据 */
			byte[] data2 = new byte[BodyComposition.DATA_LENGTH];
			System.arraycopy(inputData, BodyComposition.DATA_START, data2, 0, BodyComposition.DATA_LENGTH);
            /* 解析数据 */
			new BodyComposition(data2);
		}
	}

	public static String bytesToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x ", b));
	    }
	    return builder.toString();
	}

	/**
	 * 获取帮助信息
	 */
	public static final byte[] getDianjiTest() {
		byte[] byteArray = null;
		ByteBuffer target = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
		/* 1. 初始化 */
		target.put(SEND);
		target.limit(target.remaining());
		target.rewind();
		/* 提取 */
		byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
		target.get(byteArray);
		/* 将byteBuffer清理 */
		target.clear();
		return byteArray;
	}

	private boolean updatePort() {
		serialPort = "/dev/ttyS2";
		return true;
	}

	/** 初始化串口 */
	private boolean initCoinMachine() {
		boolean ret = false;
		/* 如果已经打开状态，则不再重新打开，这样可以节省不少时间 */
		if (serialCtrl.isOpen()) {
			return true;
		}

		/* 1. 打开串口 */
		if (serialPort == null) {
			ret = updatePort();
			if (!ret) {
				if (DEBUG) Log.w(LOG_TAG, "UpdatePort failure!");
				return false;
			}
		}

		ret = serialCtrl.setPort(serialPort);
		if (!ret) {
			if (DEBUG) Log.w(LOG_TAG, "SetPort serialProt failure!");
			return false;
		}
		ret = serialCtrl.setBaudRate(BAUDRATE_COIN);
		if (!ret) {
			if (DEBUG) Log.w(LOG_TAG, "setBaudRate serialProt failure!");
			return false;
		}
		try {
			serialCtrl.open();
		} catch (SecurityException e) {
			if (DEBUG) Log.w(LOG_TAG, "打开串口失败:没有串口读/写权限!");
			return false;
		} catch (IOException e) {
			if (DEBUG) Log.w(LOG_TAG, "打开串口失败:未知错误!");
			return false;
		} catch (InvalidParameterException e) {
			if (DEBUG) Log.w(LOG_TAG, "打开串口失败:参数错误!");
			return false;
		}

		return true;
	}

	/**
	 * 做一次获取数据处理
	 * @return
	 */
	public boolean doIt() {
		Log.i(LOG_TAG, "doPrint");

		/* 1. 打开串口 */
		if (!initCoinMachine()) {
			if (DEBUG)
				Log.w(LOG_TAG, "init Thermal Printer fail!");
			return false;
		}
		/* 2. 发送数据 */
		serialCtrl.send(getDianjiTest());

		/* 3. 关闭串口 */
//		serialCtrl.close();
		return false;
	}
}
