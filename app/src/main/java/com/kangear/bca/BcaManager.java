package com.kangear.bca;

import android.content.Context;
import android.util.Log;

import com.kangear.PrinterSdk.Printer;
import com.kangear.bca.report.CreateReport;
import com.kangear.bca.report.UartHelper;
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
	private UartHelper mUartHelper;
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
    }

	public void init() {
        Log.i(TAG, "init");

		mPrinter.init();
		initSerial();
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

	private void doPrint(byte[] data) {
		try {
			// 1. 解析
			byte[] pureData = Protocol.parseMsg(data, (byte)0x00, (byte)0x00);
			// 2. 生成对象
			BodyComposition bc = new BodyComposition(pureData);
			// 3. 生成PDF
			CreateReport report = CreateReport.getInstance(mContext);
			report.setCoordinate(new Coordinate20170504());
			String pdf = report.createPdf(bc);
			Log.i(TAG, "PDF: " + pdf);
			// 4. 打印
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
