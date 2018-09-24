package com.kangear.bca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kangear.PrinterSdk.Printer;
import com.kangear.bca.report.CreateReport;
import com.kangear.bca.report.GPIO;
import com.kangear.bodycompositionanalyzer.R;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.ComBean;
import android_serialport_api.SerialHelper;
import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.ShellUtils;

import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * Created by tony on 16-6-23.
 */
public class BcaManager {
 	private static final boolean DEBUG = true;
	private static final String TAG = "BcaManager";
	/** 基于传统Linux设备节点实现的串口通信 */
	private final SerialHelper serialCtrl       = new SerialControl();
	private static Context mContext =  null;
	private String                 serialPort       = null;
	// 这个要改成可以设置的
	private static final String TRADITIONAL_TTY_DEV_NODE = "/dev/ttyS0";

	/** 串口相关 */
	private static final String BAUDRATE_COIN = "9600";

	/** 打印机对象 */
	private Printer mPrinter;

    /* PDF文件存放路径 */
    private final String mPdfPath;
    /* PDL文件存放路径 */
    private final String mRasterPath;

	public static final String KEY_IS_DRAW_NEGATIVE = "IS_DRAW_NEGATIVE";
	public static final String KEY_DO_NOT_PRINT     = "DO_NOT_PRINT";
	public static final String REPORT_ID = "REPORT_ID";
	public static final int REPORT_ID_20160610 = R.id._20160610;
	public static final int REPORT_ID_20170504 = R.id._20170504;
	public static final int REPORT_ID_20170610 = R.id._20170610;
	private final Protocol mProtocol = new Protocol();

	public int getReportId() {
		// 读取配置，刷新UI
		int def = REPORT_ID_20160610;
		int reportId = PreferencesUtils.getInt(mContext, BcaManager.REPORT_ID, def);
		return reportId;
	}

	public void setReportId(int value) {
		PreferencesUtils.putInt(mContext, BcaManager.REPORT_ID, value);
	}

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static BcaManager singleton = null;

	public static BcaManager getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (BcaManager.class) {
                if (singleton== null)  {
                    singleton= new BcaManager(context);
                }
            }
        }
        return singleton;
    }

    private BcaManager(Context context) {
        this.mContext = context;
        if (mContext == null) {
        	Log.e(TAG, "mContext can not be null!");
		}
        //mFontTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/simsun.ttf");
        mPdfPath = mContext.getCacheDir() + File.separator + "test.pdf";
        mRasterPath = mContext.getCacheDir() + File.separator + "test.bin";
		// 初始化打印机(初始化结果要告知用户)
		mPrinter = Printer.getInstance(context);

		init();
    }

	public void init() {
        Log.i(TAG, "init");
		// 监听广播
		IntentFilter itf = new IntentFilter();
		itf.addAction(Printer.ACTION_PRINER_ADD);
		itf.addAction(Printer.ACTION_PRINER_REMOVE);
		LocalBroadcastManager.getInstance(mContext).registerReceiver(
				mBroadcastReceiver,
				itf);

		mPrinter.init();
		initSerial();

		// 设置底版
		CreateReport report = CreateReport.getInstance(mContext);
		switch (getReportId()) {
			case REPORT_ID_20160610:
				report.setCoordinate(new Coordinate20160601());
				break;
			case REPORT_ID_20170504:
				report.setCoordinate(new Coordinate20170504());
				break;
			case REPORT_ID_20170610:
				report.setCoordinate(new Coordinate());
				break;
		}
	}

	private void initSerial() {
		boolean is =  false;
		/* 有root权限才会打开，没有的情况下不打开，解决在HuaWei P8 ANR的问题 */
		if(ShellUtils.checkRootPermission())
			is = initCoinMachine();

//		if(!is) {
//			BcaService.startActionNoneSerial(mContext);
//		} else {
//			BcaService.startActionAddSerial(mContext);
//		}
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
				if (DEBUG) Log.w(TAG, "UpdatePort failure!");
				return false;
			}
		}

		ret = serialCtrl.setPort(serialPort);
		if (!ret) {
			if (DEBUG) Log.w(TAG, "SetPort serialProt failure!");
			return false;
		}
		ret = serialCtrl.setBaudRate(BAUDRATE_COIN);
		if (!ret) {
			if (DEBUG) Log.w(TAG, "setBaudRate serialProt failure!");
			return false;
		}
		try {
			serialCtrl.open();
		} catch (SecurityException e) {
			if (DEBUG) Log.w(TAG, "打开串口失败:没有串口读/写权限!");
			return false;
		} catch (IOException e) {
			if (DEBUG) Log.w(TAG, "打开串口失败:未知错误!");
			return false;
		} catch (InvalidParameterException e) {
			if (DEBUG) Log.w(TAG, "打开串口失败:参数错误!");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		Log.w(TAG, "打开串成功!");

		return true;
	}

	private boolean updatePort() {
		serialPort = TRADITIONAL_TTY_DEV_NODE;
		return true;
	}

    /** 返初始化 */
    public void unInit() {
		/* 反初始化串口 */
		uninitSerial();

		/* 打印机反初始化 */
        mPrinter.uninit();

        //
		if (mContext != null)
			LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
    }

	private void uninitSerial() {
    	serialCtrl.close();
	}

    private class SerialControl extends SerialHelper {
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			handleRecData(ComRecData);
		}

		/** 处理接收到串口数据事件 */
		private void handleRecData(final ComBean ComRecData) {
			if (DEBUG) Log.i(TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
			byte[] ret = mProtocol.connectData(ComRecData.bRec);
			if (ret != null) {
				Log.i(TAG, "接收完整了");
				doPrint(ret);
			}
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			handleEvent(intent);
		}
	};

	private void handleEvent(Intent intent) {
		Log.i(TAG, "handleEvent");
		if (intent == null)
			return;

		String act = intent.getAction();
		if (act == null)
			return;

		String cmd = "echo 0 > /sys/class/gpio_sw/PH7/data";
		switch (act) {
			case Printer.ACTION_PRINER_ADD:
				cmd = "echo 0 > /sys/class/gpio_sw/PH7/data";
				break;
			case Printer.ACTION_PRINER_REMOVE:
				cmd = "echo 1 > /sys/class/gpio_sw/PH7/data";
				break;
		}

		Log.i(TAG, cmd);
		ShellUtils.execCommand(cmd, true);
	}

	private static final byte[] TESTDATA = {
			(byte)0xCC, 0x00, 0x00, (byte)0xE1, (byte)0xDE, 0x00, (byte)0xFE,
			0x01, 0x00, 0x4E, 0x07, (byte)0xE8, 0x02, 0x57, 0x00, (byte)0xF3, 0x1B,
			0x01, 0x0D, 0x7B, 0x17, (byte)0x9A, 0x02, (byte)0x92, 0x13, (byte)0x9E,
			0x08, (byte)0xFF, 0x0C, 0x76, 0x17, (byte)0x9A, 0x02, (byte)0x8E, 0x13,
			(byte)0xA0, 0x08, (byte)0xBE, 0x0C, (byte)0xD6, 0x16, (byte)0x98, 0x02, (byte)0xD9,
			0x12, 0x71, 0x08, 0x31, 0x37, 0x30, 0x32, 0x32, 0x36, 0x31, 0x37, 0x30,
			0x33, (byte)0xE8, 0x02, (byte)0x86, 0x02, 0x47, 0x03, (byte)0xF3, 0x02,
			(byte)0xF8, 0x01, 0x1A, 0x02, 0x67, 0x02, (byte)0xF0, 0x00, (byte)0x99,
			0x00, (byte)0xE6, 0x00, (byte)0xD7, 0x01, (byte)0xF0, 0x01, 0x39, 0x02,
			(byte)0xCA, 0x04, 0x0D, 0x03, (byte)0x97, 0x04, (byte)0x9B, 0x00, 0x62,
			0x00, (byte)0x94, 0x00, (byte)0x99, 0x00, 0x62, 0x00, (byte)0x94, 0x00,
			(byte)0xA8, 0x01, 0x10, 0x01, (byte)0x99, 0x01, (byte)0xAB, 0x01, 0x10,
			0x01, (byte)0x99, 0x01, 0x3C, 0x00, 0x0A, 0x00, 0x5A, 0x00, 0x76, 0x09,
			(byte)0xF6, 0x09, 0x6E, 0x0B, 0x31, 0x01, 0x41, 0x01, 0x71, 0x01, 0x34,
			0x01, 0x41, 0x01, 0x71, 0x01, 0x6C, 0x03, (byte)0x92, 0x03, 0x18, 0x04,
			0x64, 0x03, (byte)0x92, 0x03, 0x18, 0x04, 0x6A, 0x01, (byte)0x82, 0x01,
			(byte)0xBA, 0x01, 0x6D, 0x00, 0x6A, 0x00, 0x7B, 0x00, 0x21, 0x00, 0x29,
			0x00, 0x2C, 0x00, (byte)0x88, 0x00, 0x7E, 0x00, (byte)0x90, 0x00, (byte)0xE2,
			0x00, (byte)0xD9, 0x00, (byte)0xEB, 0x00, (byte)0xC7, 0x03, (byte)0x84,
			0x03, 0x4C, 0x04, (byte)0xD4, 0x00, (byte)0xB9, 0x00, (byte)0xF0, 0x00, 0x42, 0x01, (byte)0xC8,
			0x00, 0x2C, 0x01, 0x74, 0x03, 0x6A, 0x03, (byte)0xB7, 0x03, 0x08, 0x03, (byte)0xFD,
			0x02, 0x59, 0x00, 0x01, 0x00, 0x0B, 0x00, 0x00, 0x00, 0x57, 0x00, 0x01,
			0x00, 0x62, 0x00, 0x7A, 0x05, 0x6C, 0x08, 0x00, (byte)0xDD, (byte)0xDD, 0x12};

	public void test() {
		doPrint(TESTDATA);
	}

	private void doPrint(byte[] data) {
		try {
			// 1. 解析
			byte[] pureData = Protocol.parseMsg(data, (byte)0x00, (byte)0x00);
			// 2. 生成对象
			BodyComposition bc = new BodyComposition(pureData);
			// 3. 生成PDF
			CreateReport report = CreateReport.getInstance(mContext);
			String pdf = report.createPdf(bc);
			Log.i(TAG, "PDF: " + pdf);
			// 5. 打印
			mPrinter.printPdf(mContext.getCacheDir().getAbsolutePath() + File.separator + "test.raster", pdf);
		} catch (Protocol.ProtocalExcption protocalExcption) {
			protocalExcption.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    /** 处理错误 */
    private static void handleError(double d, byte[] cache, byte[] in) {
        if (DEBUG) Log.w(TAG, "parseCoin    in: " + d + " (" + in.length + ") "
                + bytesToHex(in));
        if (DEBUG) Log.w(TAG, "parseCoin cache: " + d + " (" + cache.length + ") "
                + bytesToHex(cache));
    }

    public String getPdfPath() {
        return mPdfPath;
    }

    public String getRasterPath() {
        return mRasterPath;
    }
}
