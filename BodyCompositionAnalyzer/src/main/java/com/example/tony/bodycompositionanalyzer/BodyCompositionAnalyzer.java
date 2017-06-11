package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.Exchanger;

import android_serialport_api.ComBean;
import android_serialport_api.SerialHelper;
import android_serialport_api.SerialPortFinder;
import cn.trinea.android.common.util.PreferencesUtils;
import cn.trinea.android.common.util.ShellUtils;

/**
 * Created by tony on 16-6-23.
 */
public class BodyCompositionAnalyzer {
 	private static final boolean DEBUG = true;
	private static final String LOG_TAG = "BodyCompositionAnalyzer";
	/** 基于传统Linux设备节点实现的串口通信 */
	private final SerialHelper serialCtrl       = new SerialControl();
	private final SerialPortFinder serialPortFinder = new SerialPortFinder();
	/** FTDI基于Android USB Api实现的串口通信，免Root */
	private UartHelper mUartHelper;
	private static Context mContext =  null;
	private String                 serialPort       = null;
	private static final String TRADITIONAL_TTY_DEV_NODE = "/dev/ttyAMA2";
	private static BodyComposition mBodyComposition;
	private static final boolean IS_USB_UART = false;
	/**
	 * 发送此数据，从机会将需要的数据进行回传
	 */
	public static final byte[] SEND = new byte[]{(byte)0xAA, (byte)0xAB, 0x00, 0x04, (byte)0xDE, 0x00, (byte)0xDD, 0x6A};
	private static final int BYTE_BUFFER_ALLOCATE = 8192;

	/** 串口相关 */
	private static final String BAUDRATE_COIN = "9600";

	/** 打印机对象 */
	private Printer mPrinter;

    /* PDF文件存放路径 */
    private final String mPdfPath;
    /* PDL文件存放路径 */
    private final String mRasterPath;
    /** 接收到的完整数据 */
    private byte[] mFullData;
	private static final String DEFAULT_DATA_BIN_NAME = "fe2.bin";

	public static final String KEY_IS_DRAW_NEGATIVE = "IS_DRAW_NEGATIVE";
	public static final String KEY_DO_NOT_PRINT     = "DO_NOT_PRINT";

	private static final int TEXT_SIZE_DEF     = 9;
    private static final int TEXT_SIZE_体成分结果 = 8;
	private static final int TEXT_SIZE_健康评估 = 16;
	private static final int TEXT_SIZE_体型分析 = 20;
	private static final String TEXT_TICK    = "√";
	private static final String TEXT_体型分析 = "✔";

    /** 使用自定义字体：宋体 */
    private static Typeface mFontTypeface;

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static BodyCompositionAnalyzer singleton = null;

	public static BodyCompositionAnalyzer getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (BodyCompositionAnalyzer.class) {
                if (singleton== null)  {
                    singleton= new BodyCompositionAnalyzer(context);
                }
            }
        }
        return singleton;
    }

    private BodyCompositionAnalyzer(Context context) {
        this.mContext = context;
        mFontTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/simsun.ttf");
        mPdfPath = mContext.getCacheDir()
                + File.separator + "test.pdf";
        mRasterPath = mContext.getCacheDir()
                + File.separator + "test.bin";
		// 初始化打印机(初始化结果要告知用户)
		mPrinter = Printer.getInstance(context);
    }

	public void init() {
        Log.i(LOG_TAG, "init");

		mPrinter.init();
		initSerial();
	}

	public void initSerial() {
		boolean is =  false;
		if(IS_USB_UART) {
			// 打开串口(初始化结果要告知用户)
			try {
				mUartHelper = new UartControl(mContext);
				mUartHelper.setBaudRate(9600);
				mUartHelper.open();
				is = true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidParameterException e) {
				e.printStackTrace();
			}
		} else {
			/** 有root权限才会打开，没有的情况下不打开，解决在HuaWei P8 ANR的问题 */
			if(ShellUtils.checkRootPermission())
				is = initCoinMachine();
		}

		if(!is) {
			BodyCompositionAnalyzerService.startActionNoneSerial(mContext);
		} else {
			BodyCompositionAnalyzerService.startActionAddSerial(mContext);
		}
	}



    /** 返初始化 */
    public void unInit() {
		/* 反初始化串口 */
		uninitSerial();

		/* 打印机反初始化 */
        mPrinter.uninit();
    }

	private void uninitSerial() {
		if(IS_USB_UART) {
		/* 关闭串口 */
			mUartHelper.close();
		} else {
			serialCtrl.close();
		}
	}

	public byte[] getFullData() {
        return mFullData;
    }

    private class SerialControl extends SerialHelper {
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			handleRecData(ComRecData);
		}

		/** 处理接收到串口数据事件 */
		private void handleRecData(final ComBean ComRecData) {
			if (DEBUG) Log.i(LOG_TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
            connectSegmentAsFullData(ComRecData.bRec);
		}
	}

	/**
	 * FTDI 类
	 */
	private class UartControl extends UartHelper {
		/**
		 * 主要是设置context对象
		 * @param context
		 */
		public UartControl(Context context) {
			super.init(context);
		}

		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			handleRecData(ComRecData);
		}
		/** 处理接收到串口数据事件 */
		private void handleRecData(final ComBean ComRecData) {
			Log.i(LOG_TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
            connectSegmentAsFullData(ComRecData.bRec);
		}
	}

	public BodyComposition getBodyComposition() {
		return this.mBodyComposition;
	}

    /** 接收串口数据缓存 */
    private static byte[] cache = null;

	/**
	 * 处理串口接收函数接收到的数据段，并将其拼接成完成数据
	 * @param in 待拼接的数据片段
     */
    public void connectSegmentAsFullData(byte[] in) {
		Log.i(LOG_TAG, "connectSegmentAsFullData");
		/* 将in存于cache中 */
        if(cache == null) /* 第一次进入 */
            cache = Arrays.copyOfRange(in, 0, in.length);
        else {            /* 将in连接到cache后面 */
            byte[] tmp = Arrays.copyOfRange(cache, 0, cache.length);
            cache = new byte[tmp.length + in.length];
            System.arraycopy(tmp, 0, cache,          0, tmp.length);
            System.arraycopy( in, 0, cache, tmp.length, in.length);
        }

        /**
         * 这里进行ack校验的使用是为了找到数据头部
         */
        byte[]     ack;
        int        length              = BodyComposition.TOTAL_LENGTH;

		/* loop循环用于迭代处理 */
        loop: while (cache.length > 0) {
            try {

				/* 1.1 判断数据是否极小，小于ACK的长度值 */
				if(cache.length < BodyComposition.ACK_LENGTH) {
					handleError(1.0, cache, in);
					return;
				}

				/* 1.2 取出第ACK_LENGTH位ack */
                ack = Arrays.copyOfRange(cache, BodyComposition.ACK_START, BodyComposition.ACK_START + BodyComposition.ACK_LENGTH);
				/* 1. 锁定STX位置 */
                if (!Arrays.equals(ack, BodyComposition.ACK)) {
                    handleError(1.1, cache, in);
					/* 找出 STX 前内容删除 */
                    boolean hasSTX = false;
                    int i = 0;
                    for (i = 0; i < cache.length - BodyComposition.ACK_LENGTH; i++)
                        ack = Arrays.copyOfRange(cache, BodyComposition.ACK_START + i, BodyComposition.ACK_START + i + BodyComposition.ACK_LENGTH);
                        if (Arrays.equals(ack, BodyComposition.ACK)) {
                            cache = Arrays.copyOfRange(cache, 0 + i, cache.length);
                            handleError(1.2, cache, in);
                            hasSTX = true;
                            break;
                        }
                    if (hasSTX) {
						/* 迭代 */
                        continue loop;
                    } else {
						/* 没有查到STX，直接清除全部cache */
                        handleError(1.3, cache, in);
                        cache = Arrays.copyOfRange(cache, 0, 0);
                        return;
                    }
                }


				/* 2. 这里判断数据的总长度是否符合不需要清除cache (坐标+1是长度) */
                if(cache.length < BodyComposition.TOTAL_LENGTH) {
                    handleError(2.0, cache, in);
                    return;
                }

                /** 数据长度够了，进行解析,这样调用这个函数立刻会结束 */
                mFullData = Arrays.copyOfRange(cache, 0, BodyComposition.TOTAL_LENGTH);
                MyIntentService.startActionParseData(mContext, "", "");

				/* 将处理过数据删除 进行递归 */
                cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
                if (cache.length != 0) {
					/* 数据未处理完时不再打印调试信息 */
                    // handleError(10, cache, in);
                    continue loop;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(LOG_TAG, "parseCoin: ArrayIndexOutOfBoundsException");
                e.printStackTrace();
            }
        }
    }

    /** 生成BCC(异或)校验码 */
    public static byte getBCC(byte[] data) {
        byte crc = 0x00;
        for (byte b : data)
            crc ^= b;
        return crc;
    }

    /** 处理错误 */
    private static void handleError(double d, byte[] cache, byte[] in) {
        if (DEBUG) Log.w(LOG_TAG, "parseCoin    in: " + d + " (" + in.length + ") "
                + bytesToHex(in));
        if (DEBUG) Log.w(LOG_TAG, "parseCoin cache: " + d + " (" + cache.length + ") "
                + bytesToHex(cache));
    }

	/**
	 * Parse single time full data
     * 进来的数据是已经被完整的数据
	 * @param inputData
     */
	public static synchronized void parseData(byte[] inputData) {
		Log.i(LOG_TAG, "parseData" + " inputData.length =" + inputData.length);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        /* 0. 从数据中获取数据总长度 */
        BodyComposition.TOTAL_LENGTH_NEW = (inputData[BodyComposition.LENGTH_START] & 0xFF) + BodyComposition.VERIFICATION_LENGTH;
        /* 需要检验的数据长度:总长度减去开头CC以及结尾检验位共2位 */
        BodyComposition.NEED_CHECKSUM_DATA_LENGTH = BodyComposition.TOTAL_LENGTH_NEW - 2;


        /* 1. 判断数据长度是否正常 */
        if(BodyComposition.TOTAL_LENGTH_NEW != inputData.length) {
            Log.e(LOG_TAG, "inputData.length =" + inputData.length +
                    " but TOTAL_LENGTH_NEW need " + BodyComposition.TOTAL_LENGTH_NEW);
            return;
        }

        /* 2. 判断回复响应是否正常 */
        byte[] ack = new byte[BodyComposition.ACK_PIPEI_LENGTH];
        System.arraycopy(inputData, BodyComposition.ACK_START, ack, 0, BodyComposition.ACK_PIPEI_LENGTH);
        if(!Arrays.equals(ack, BodyComposition.ACK_PIPEI)) {
            Log.e(LOG_TAG, "ACK不匹配！");
            return;
        }

        /**
         * 3. 结束符判断
         */
        /* 总长度-2 */
        BodyComposition.结束符_START = BodyComposition.TOTAL_LENGTH_NEW - 2;
        final byte EOF = inputData[BodyComposition.结束符_START];
        if(EOF != BodyComposition.结束符_DEF) {
            Log.e(LOG_TAG, String.format("结束符不匹配：结束符不匹配为 %02X，但需要的结束符为 %02X。", EOF, BodyComposition.结束符_DEF));
            return;
        }

        /**
         * 4. 检验和判断
         * 校验方法如下： 从cc_后_开始加，一直加到截止符dd，取低八位。
         */
        BodyComposition.校验和_START = BodyComposition.TOTAL_LENGTH_NEW - 1;
        /* 2. 判断回复响应是否正常 */
        byte[] checksumCache = new byte[BodyComposition.NEED_CHECKSUM_DATA_LENGTH];
        System.arraycopy(inputData, BodyComposition.NEED_CHECKSUM_DATA_START, checksumCache, 0, BodyComposition.NEED_CHECKSUM_DATA_LENGTH);
        byte sum = checkSum(checksumCache);
        final byte CHECKSUM = inputData[BodyComposition.校验和_START];
        if(sum != CHECKSUM) {
            Log.e(LOG_TAG, String.format("数据校验失败：计算校验和为 %04X 但数据中提取校验和为 %02X。", sum, CHECKSUM));
            return;
        }

        /* 提取各个数据 */
        byte[] data2 = new byte[BodyComposition.DATA_LENGTH];
        System.arraycopy(inputData, BodyComposition.DATA_START, data2, 0, BodyComposition.DATA_LENGTH);
        /* 解析数据 */
        mBodyComposition = new BodyComposition(data2);
        MyIntentService.startActionDataToPdf(mContext, "", "");
	}

    public static final byte checkSum(byte[] bytes) {
        byte sum = 0;
        for (byte b : bytes) {
            sum += b;
        }
        return sum;
    }

	public static String bytesToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    for(byte b : in) {
	        builder.append(String.format("%02x ", b));
	    }
	    return builder.toString();
	}

	public void doPrint() {
		try {
			mPrinter.printPdf(mRasterPath, mPdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		serialPort = TRADITIONAL_TTY_DEV_NODE;
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 做一次获取数据处理 调试模式，不使用串口，直接读取已有数据
	 * @return 成功返回true,否则返回false
	 */
	public boolean test()  throws IOException {
		Log.i(LOG_TAG, "doIt");

        // 读取样本数据
        InputStream in = mContext.getResources().getAssets().open(DEFAULT_DATA_BIN_NAME);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] bufferArray = buffer.toByteArray();

        /** 数据长度够了，进行解析,这样调用这个函数立刻会结束 */
        mFullData = Arrays.copyOfRange(bufferArray, 0, BodyComposition.TOTAL_LENGTH);
        MyIntentService.startActionParseData(mContext, "", "");
        return true;

//		/* 1. 打开串口 */
//		if (!initCoinMachine()) {
//			if (DEBUG)
//				Log.w(LOG_TAG, "init Thermal Printer fail!");
//			return false;
//		}
//		/* 2. 发送数据 */
//		serialCtrl.send(getDianjiTest());

		/* 3. 关闭串口 */
//		serialCtrl.close();
	}

	void tmp () {
		try {
			mUartHelper.setBaudRate(9600);
			mUartHelper.open();
			mUartHelper.send(new byte[]{'H', 'E', 'L', 'L', 'O'});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将BodayComposition生成PDF
	 * @param bc BodyComposition对象
	 * @return path
	 */
	public String toPdf(BodyComposition bc) {
		Log.i(LOG_TAG, "toPdf");

		/* 1. 打开串口 */
//		if (bc == null) {
//			return false;
//		}

		/* 2. 生成PDF两种方法：Android Api或者iText Api */
		String pdf = createPdf(mPdfPath, bc);
		MyIntentService.startActionPdfToOpen(mContext, "", "");
		if(!PreferencesUtils.getBoolean(mContext, KEY_DO_NOT_PRINT))
			MyIntentService.startActionPdfToPrinter(mContext, "", "");
        return mPdfPath;
	}

	/**
	 * 当API >= 19 时使用系统自带PDF API，否则使用iText
	 * @return 返回文件路径
	 */
	public String createPdf(String pdfPath, BodyComposition bc) {
		Log.i(LOG_TAG, "createPdf");
		// TODO: Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
		if (1 + 1 == 2) {
			// 系统API
			createPdfwithAndroid(pdfPath, bc);
		} else {
			// 使用iText
//			try {
//				createPdfwithiText(pdfPath, bc);
//			} catch (DocumentException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		return pdfPath;
	}

    public int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

	/**
	 * Android 4.4+ 版本的官方PDF Api
	 * @param pdfPath pdf文件路径
	 * @param bc BodyComposition对象
	 * 我去，官方API 21才可以打开已经存在的PDF文件，有可能得用iText了
	 */
	private void createPdfwithAndroid(String pdfPath, BodyComposition bc) {
		Log.i(LOG_TAG, "createPdfwithAndroid");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// create a new document
			PdfDocument document = new PdfDocument();

			// crate a page description
			PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(
					PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
					PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000, 1)
					.create();

			// start a page
			PdfDocument.Page page = document.startPage(pageInfo);
			// canvas
			Canvas canvas = page.getCanvas();

			// 画笔
			Paint defPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			float size = defPaint.getTextSize();
			defPaint.setTextSize(TEXT_SIZE_DEF);
            defPaint.setTypeface(mFontTypeface);
            Rect rect = null;
            int high = getTextHeight("HelloWorld", defPaint);
            Log.i(LOG_TAG, "text high: " + high);
			Paint paint = new Paint(defPaint);
			Log.i(LOG_TAG, "size: " + size);

			Layout.Alignment mAlignment;

			// 文字画笔
			TextPaint textPaint = new TextPaint();
            textPaint.setTypeface(mFontTypeface);
			String tmpStr;

			// 0.1 画底板 (调试对比使用，成品不画此界面)
			if(PreferencesUtils.getBoolean(mContext, BodyCompositionAnalyzer.KEY_IS_DRAW_NEGATIVE)) {
				Bitmap bm = getBitmapFromAsset(mContext, "body_composition_negative_20170504.jpg");
				if(bm != null) {
					// 将图片拉伸至整个页面
					canvas.drawBitmap(bm, null, new Rect(
									0,
									0,
									PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
									PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000),
							null);
				}
			}

			textPaint.setTextSize(TEXT_SIZE_DEF);

			// 01 写姓名/编号
			paint.setColor(Color.BLACK);
			tmpStr = bc.姓名;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.姓名);

			// 02 身高
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.身高;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.身高);

			// 03 体重
			paint.setColor(Color.BLACK);
			tmpStr = bc.体重1;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.体重1);

			// 04 测试日期
			paint.setColor(Color.BLACK);
			tmpStr = bc.测试日期;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.测试日期);

			// 05 年龄
			paint.setColor(Color.BLACK);
			tmpStr = bc.年龄;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.年龄);

			// 06 性别
			paint.setColor(Color.BLACK);
			tmpStr = bc.性别;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.性别);

			/* 2x. 体成分结果 */
			// 21 体重2
            textPaint.setTextSize(TEXT_SIZE_体成分结果);
			paint.setColor(Color.BLACK);
			tmpStr = bc.体重2 + "kg [" + bc.体重标准范围 + "]";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.体重2);

			// 22 去脂肪体重
			paint.setColor(Color.BLACK);
			tmpStr = bc.去脂肪体重 + "kg [" + bc.去脂肪体重标准 + "]";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.去脂肪体重);

			// 23 肌肉量
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.肌肉量 + "kg [" + bc.肌肉标准 + "]";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.肌肉量);

			// 24 身体总水分
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.身体总水分 + "kg [" + bc.身体总水分正常范围 + "]";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.身体总水分);

			// 25 细胞内液 okay
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.细胞内液含量 + "kg\n[" + bc.细胞内液正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.细胞内液, mAlignment);

			// 26 细胞外液 okay
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.细胞外液含量 + "kg\n[" + bc.细胞外液正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.细胞外液, mAlignment);

			// 27 蛋白质量 ok
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.蛋白质含量 + "kg\n[" + bc.蛋白质正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.蛋白质量, mAlignment);

			// 28 无机盐量 ok
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.无机盐含量 + "kg\n[" + bc.无机盐含量正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.无机盐量, mAlignment);

			// 29 体脂肪量 ok
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.体脂肪量 + "kg\n[" + bc.体脂肪量标准 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.体脂肪量, mAlignment);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;

			/* 3x. 体成分分析　*/
            /* 31. 体重 */
            float STROCK = 3f;
            textPaint.setTextSize(TEXT_SIZE_DEF); // 还原字体大小
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(STROCK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			float xPos = BodyComposition.Position.体成分分析_体重.getXMils() / 1000 +
					getProgressLength3(bc.体重_CUR, bc.体重_MIN, bc.体重_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_体重, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.体重_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_体重.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					textPaint);
			// 还原
			paint.setStrokeWidth(defPaint.getStrokeWidth());
            paint.setShader(defPaint.getShader());

            /* 32. 身体质量(BMI) */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_身体质量.getXMils() / 1000 +
					getProgressLength3(bc.身体质量_CUR, bc.身体质量_MIN, bc.身体质量_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_身体质量, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.身体质量_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_身体质量.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 33. 体脂肪率 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_体脂肪率.getXMils() / 1000 +
					getProgressLength3(bc.脂肪率_CUR, bc.脂肪率_MIN, bc.脂肪率_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_体脂肪率, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.脂肪率_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_体脂肪率.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 34. 体脂肪量 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_体脂肪量.getXMils() / 1000 +
					getProgressLength3(bc.体脂肪量_CUR, bc.体脂肪量_MIN, bc.体脂肪量_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_体脂肪量, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.体脂肪量_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_体脂肪量.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 35. 肌肉量 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_肌肉量.getXMils() / 1000 +
					getProgressLength3(bc.肌肉量_CUR, bc.肌肉量_MIN, bc.肌肉量_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_肌肉量, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.肌肉量_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_肌肉量.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 36. 身体水分 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_身体水分.getXMils() / 1000 +
					getProgressLength3(bc.身体总水分_CUR, bc.身体总水分_MIN, bc.身体总水分_MAX);
            drawProgress(canvas, BodyComposition.Position.体成分分析_身体水分, xPos, paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.身体总水分_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_身体水分.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 37. 内脏脂肪 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(STROCK);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_内脏脂肪.getXMils() / 1000 + getProgressLength2(bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_内脏脂肪.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    paint);
            canvas.drawText(
					String.format("%.1f", (float) bc.内脏脂肪_CUR / 10),
					xPos,
					BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

			/* 4X. 调节目标 */
			// 41 体重_标准 okay 注：根据当前值和调节量倒倒推
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(String.format("%.1f", (float) bc.体重_STD / 10),
					BodyComposition.Position.体重_标准.getXMils() / 1000,
					BodyComposition.Position.体重_标准.getYMils() / 1000,
					paint);
			// 42 体重_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format("%.1f", (float) bc.体重_CUR / 10),
					BodyComposition.Position.体重_当前.getXMils() / 1000,
					BodyComposition.Position.体重_当前.getYMils() / 1000,
					paint);
			// 43 体重_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			/* 体重调节量是 脂肪调节量 和 肌肉调节量 之和 */
			float tmpFloat = bc.体重_REG / 10f;
			canvas.drawText(
					((tmpFloat < 0) ? "" : "+") + tmpFloat,
					BodyComposition.Position.体重_调节量.getXMils() / 1000,
					BodyComposition.Position.体重_调节量.getYMils() / 1000,
					paint);
			// 44 身体脂肪量_标准 okay 注：身体脂肪量标准：取体脂肪标准值下界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format("%.1f", (float) bc.体脂肪量_STD / 10),
					BodyComposition.Position.身体脂肪量_标准.getXMils() / 1000,
					BodyComposition.Position.身体脂肪量_标准.getYMils() / 1000,
					paint);
			// 45 身体脂肪量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体脂肪量,
					BodyComposition.Position.身体脂肪量_当前.getXMils() / 1000,
					BodyComposition.Position.身体脂肪量_当前.getYMils() / 1000,
					paint);
			// 46 身体脂肪量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = (float) bc.脂肪量_REG / 10;
			canvas.drawText(
					((tmpFloat < 0) ? "" : "+") + tmpFloat,
					BodyComposition.Position.身体脂肪量_调节量.getXMils() / 1000,
					BodyComposition.Position.身体脂肪量_调节量.getYMils() / 1000,
					paint);
			// 47 肌肉量_标准 okay 肌肉量标准：取标准值上界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format("%.1f", (float) bc.肌肉量_STD / 10),
					BodyComposition.Position.肌肉量_标准.getXMils() / 1000,
					BodyComposition.Position.肌肉量_标准.getYMils() / 1000,
					paint);
			// 48 肌肉量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format("%.1f", (float) bc.肌肉量_CUR / 10),
					BodyComposition.Position.肌肉量_当前.getXMils() / 1000,
					BodyComposition.Position.肌肉量_当前.getYMils() / 1000,
					paint);
			// 49 肌肉量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = bc.肌肉量_REG / 10f;
			canvas.drawText(
					((tmpFloat < 0) ? "" : "+") + tmpFloat,
					BodyComposition.Position.肌肉量_调节量.getXMils() / 1000,
					BodyComposition.Position.肌肉量_调节量.getYMils() / 1000,
					paint);

			/* 5x 节段肌肉　镜像 */
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			// 51 左上肢肌肉含量 okay
			tmpStr = bc.左上肢肌肉含量; //  + "kg\n" + bc.左上肢肌肉含量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左上肢肌肉含量);

			// 52 左下肢肌肉含量 okay
			tmpStr = bc.左下肢肌肉含量;// + "kg\n" + bc.左下肢肌肉含量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左下肢肌肉含量);

			// 53 右上肢肌肉含量 okay
			tmpStr = bc.右上肢肌肉含量;// + "kg\n" + bc.右上肢肌肉含量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右上肢肌肉含量);

			// 54 右下肢脂肪量 okay
			tmpStr = bc.右下肢肌肉含量;// + "kg\n" + bc.右下肢肌肉含量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右下肢肌肉含量);

			// 55 躯干肌肉含量 okay
			tmpStr = bc.躯干肌肉含量;// + "kg\n" + bc.躯干肌肉含量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.躯干肌肉含量);

			/* 6x 节段脂肪 */
			// 61 左上肢脂肪量 okay
			tmpStr = bc.左上肢脂肪量;// + "kg\n" + bc.左上肢脂肪量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左上肢脂肪量);

			// 62 左下肢脂肪量 okay
			tmpStr = bc.左下肢脂肪量;// + "kg\n" + bc.左下肢脂肪量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左下肢脂肪量);

			// 63 右上肢脂肪量 okay
			tmpStr = bc.右上肢脂肪量;// + "kg\n" + bc.右上肢脂肪量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右上肢脂肪量);

			// 64 右下肢脂肪量 okay
			tmpStr = bc.右下肢脂肪量;// + "kg\n" + bc.右下肢脂肪量_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右下肢脂肪量);

			// 65 躯干肢脂肪量 okay
			tmpStr = bc.躯干脂肪;// + "kg\n" + bc.躯干脂肪_RESULT;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.躯干肢脂肪量);

			/* 7x 节段电阻抗 */
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			// 71 频率 okay
			tmpStr = "5k";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.频率_5k);

			tmpStr = "50k";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.频率_50k);

			tmpStr = "250k";
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.频率_250k);

			// 72 右上肢 okay
			tmpStr = bc._5k下ra电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_5k);

			tmpStr = bc._50k下ra电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_50k);

			tmpStr = bc._250k下ra电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_250k);

			// 73 左上肢 okay
			tmpStr = bc._5k下la电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_5k);

			tmpStr = bc._50k下la电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_50k);

			tmpStr = bc._250k下la电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_250k);


			// 74 躯干 okay
			tmpStr = bc._5k下tr电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.躯干_5k);

			tmpStr = bc._50k下tr电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.躯干_50k);

			tmpStr = bc._250k下tr电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.躯干_250k);

			// 75 右下肢 okay
			tmpStr = bc._5k下rl电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_5k);

			tmpStr = bc._50k下rl电阻值;
			drawLineText( tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_50k);

			tmpStr = bc._250k下rl电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_250k);

			// 76 左下肢 okay
			tmpStr = bc._5k下ll电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_5k);

			tmpStr = bc._50k下ll电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_50k);

			tmpStr = bc._250k下ll电阻值;
			drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_250k);

			// 8x 肥胖评估 体重 写「√」根据上下界判断
            // 81 体重
            BodyComposition.Position bcp;
            tmpStr = getAssessment(bc.体重2, bc.体重标准范围);
            if(tmpStr.equals("不足")) {
                bcp = BodyComposition.Position.肥胖评估_体重_不足;
            } else if(tmpStr.equals("正常")) {
                bcp = BodyComposition.Position.肥胖评估_体重_正常;
            } else {
                bcp = BodyComposition.Position.肥胖评估_体重_过量;
            }

//			textPaint.setTextSize(20); 不再加大字体了
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 82.肥胖评估 脂肪量
			tmpStr = getAssessment(bc.体脂肪量, bc.体脂肪量标准);
			if(tmpStr.equals("不足")) {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_不足;
			} else if(tmpStr.equals("正常")) {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_正常;
			} else {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_过量;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 83.肥胖评估 肌肉量
			tmpStr = getAssessment(bc.肌肉量, bc.肌肉标准);
			if(tmpStr.equals("不足")) {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_不足;
			} else if(tmpStr.equals("正常")) {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_正常;
			} else {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_过量;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 9.营养评估 写「√」
            // 91. 蛋白质
            tmpStr = getAssessment(bc.蛋白质含量, bc.蛋白质正常范围);
            if(tmpStr.equals("不足")) {
                bcp = BodyComposition.Position.营养评估_蛋白质_不足;
            } else if(tmpStr.equals("正常")) {
                bcp = BodyComposition.Position.营养评估_蛋白质_正常;
            } else {
                bcp = BodyComposition.Position.营养评估_蛋白质_过量;
            }
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);
            // 92. 无机盐
            tmpStr = getAssessment(bc.无机盐含量, bc.无机盐含量正常范围);
            if(tmpStr.equals("不足")) {
                bcp = BodyComposition.Position.营养评估_无机盐_不足;
            } else if(tmpStr.equals("正常")) {
                bcp = BodyComposition.Position.营养评估_无机盐_正常;
            } else {
                bcp = BodyComposition.Position.营养评估_无机盐_过量;
            }
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 93.基础代谢量 okay
            textPaint.setTextSize(TEXT_SIZE_DEF);
			tmpStr = bc.基础代谢量;
			bcp = BodyComposition.Position.基础代谢量;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 94.总能量消耗　okay
			tmpStr = bc.总能量消耗;
			bcp = BodyComposition.Position.总能量消耗;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 95.身体年龄 okay
			tmpStr = bc.身体年龄;
			bcp = BodyComposition.Position.身体年龄;
			drawLineText(tmpStr, textPaint, canvas, bcp);

            // 10x.体型分析
            drawShapeAnalysis(bc, textPaint, canvas);

			// 11x.健康评估 okay
            if (mContext.getResources().getBoolean(R.bool.is_print_total_score)) {
                textPaint.setTextSize(TEXT_SIZE_健康评估);
                tmpStr = bc.身体总评分;
				drawLineText(tmpStr, textPaint, canvas, BodyComposition.Position.健康评估);
            }

            // 水肿分析
			drawEdema(bc, textPaint, canvas);

			// finish the page
			document.finishPage(page);
			// add more pages
			// write the document content
			FileOutputStream os;
			try {
				Log.i(LOG_TAG, "String:" + pdfPath);
				os = new FileOutputStream(pdfPath);
				document.writeTo(os);
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// close the document
				document.close();
			}
		}
	}


	private void drawEdema(BodyComposition bc, TextPaint textPaint, Canvas canvas) {
        String tmpStr = bc.细胞外液结果;
		Layout.Alignment mAlignment = Layout.Alignment.ALIGN_NORMAL;
        BodyComposition.Position bcp;
        if(tmpStr.equals("干燥")) {
            bcp = BodyComposition.Position.水肿分析_细胞外液_干燥;
        } else if(tmpStr.equals("正常")) {
            bcp = BodyComposition.Position.水肿分析_细胞外液_正常;
        } else {
            bcp = BodyComposition.Position.水肿分析_细胞外液_浮肿;
        }
		textPaint.setTextSize(TEXT_SIZE_DEF);
		drawLineText(TEXT_TICK, textPaint, canvas, bcp);

		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(
				bc.水肿系数,
				BodyComposition.Position.水肿分析_水肿系数.getXMils() / 1000,
				BodyComposition.Position.水肿分析_水肿系数.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				bc.身体水分率,
				BodyComposition.Position.水肿分析_身体水分率.getXMils() / 1000,
				BodyComposition.Position.水肿分析_身体水分率.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				bc.细胞内液含量,
				BodyComposition.Position.水肿分析_细胞内液.getXMils() / 1000,
				BodyComposition.Position.水肿分析_细胞内液.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				bc.细胞外液含量,
				BodyComposition.Position.水肿分析_细胞外液.getXMils() / 1000,
				BodyComposition.Position.水肿分析_细胞外液.getYMils() / 1000,
				textPaint);
	}

	/**
     * 绘制[体成分分析]
     * 需要分三段，第一段和第三段一种颜色，第二段另外一种颜色
     */
	public void drawProgress(Canvas canvas, BodyComposition.Position bp, float xPos, Paint paint) {
        int oldColor = Color.BLACK;
        // 得出每一段长度
        final float RANGE_1_START = bp.getXMils() / 1000;
        final float RANGE_2_START = bp.getXMils() / 1000 + 体成分分析_SECOND_START_PX / 1000;
        final float RANGE_3_START = bp.getXMils() / 1000 + 体成分分析_THIRD_START_PX / 1000;

        // 死值
        final float RANGE_1_LENGTH = 体成分分析_SECOND_START_PX / 1000;
        final float RANGE_2_LENGTH = 体成分分析_THIRD_START_PX / 1000;
        final float RANGE_3_LENGTH = 体成分分析_TOTAL_LENGTH_PX / 1000;

        float RANGE_1 = xPos - RANGE_1_START;
        float RANGE_2 = xPos - RANGE_2_START;
        float RANGE_3 = xPos - RANGE_3_START;
        if (RANGE_1 > RANGE_1_LENGTH) RANGE_1 = RANGE_1_LENGTH;
        if (RANGE_2 > RANGE_2_LENGTH) RANGE_2 = RANGE_2_LENGTH;
        if (RANGE_3 > RANGE_3_LENGTH) RANGE_3 = RANGE_3_LENGTH;

        Log.i(LOG_TAG, "RANGE_1: " + RANGE_1 + " RANGE_2: " + RANGE_2 + " RANGE_3: " + RANGE_3);

        // 深色
        if (RANGE_1 > 0) {
            canvas.drawLine(
                    RANGE_1_START,
                    bp.getYMils() / 1000,
                    RANGE_1_START + RANGE_1,
                    bp.getYMils() / 1000,
                    paint);
        }
        // 浅色
        paint.setColor(Color.GRAY);
        if (RANGE_2 > 0) {
            canvas.drawLine(
                    RANGE_2_START,
                    bp.getYMils() / 1000,
                    RANGE_2_START + RANGE_2,
                    bp.getYMils() / 1000,
                    paint);
        }
        // 深色
        paint.setColor(oldColor);
        if (RANGE_3 > 0) {
            canvas.drawLine(
                    RANGE_3_START,
                    bp.getYMils() / 1000,
                    RANGE_3_START + RANGE_3,
                    bp.getYMils() / 1000,
                    paint);
        }
    }

    /**
     * 10x
     * 绘制 体型分析
     */
    public void drawShapeAnalysis(BodyComposition bc,
                                  TextPaint textPaint,
                                  Canvas canvas) {
        // bmi结果
        double xPos = 0;
        double yPos = 0;
        if (bc.身体质量_CUR > BodyComposition.BMI_MIN) {
            xPos = (bc.身体质量_CUR - BodyComposition.BMI_MIN) / BodyComposition.BMI_RECT_WIDTH;
        } else
            xPos = 0;

        if (bc.脂肪率_CUR < BodyComposition.BFR_MIN) {
            yPos = 0;
        }
        // MALE
        if (bc.性别.equals("男")) {
            yPos = (bc.脂肪率_CUR - BodyComposition.BMI_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            if (bc.脂肪率_CUR > bc.脂肪率_MAX) //测试值超过标准
            {
                yPos = ((bc.脂肪率_CUR - bc.脂肪率_MAX) / 2 + bc.脂肪率_MAX - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            } else {
                yPos = (bc.脂肪率_CUR - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            }
        } else if (bc.性别.equals("女")) { // FEMALE
            yPos = (bc.脂肪率_CUR - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_FEMALE;
        }

		Log.i(LOG_TAG, "before xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");
		// 设置最小值
		xPos = xPos < bc.体型分析_X_MIN ? bc.体型分析_X_MIN : xPos;
		yPos = yPos < bc.体型分析_Y_MIN ? bc.体型分析_Y_MIN : yPos;

		// 设置最大值
		xPos = xPos > bc.体型分析_X_MAX ? bc.体型分析_X_MAX : xPos;
		yPos = yPos > bc.体型分析_Y_MAX ? bc.体型分析_Y_MAX : yPos;
		Log.i(LOG_TAG, "after  xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");

        xPos = BodyComposition.ORIGIN_X + xPos * BodyComposition.SINGLE_RECT_WIDTH + BodyComposition.SINGLE_RECT_WIDTH / 2;
        yPos = BodyComposition.ORIGIN_Y - yPos * BodyComposition.SINGLE_RECT_HEIGHT - BodyComposition.SINGLE_RECT_HEIGHT / 2;

        /**
         * 坐标由iso mm转换为英寸point
         */
		textPaint.setTextSize(TEXT_SIZE_体型分析);
        canvas.drawText(TEXT_体型分析, (float)(xPos * 2836 - 10) / 1000, (float)(yPos * 2836 + 10) / 1000, textPaint);
    }

    private static final String SEPARATOR = "-";

    /**
     * function: Evaluating a math expression given in string form
     * @param str
     * @return
     * from: http://stackoverflow.com/a/26227947/2193455
     */
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    /**
     * 只处理精度不高于6位小数大小
     * @param currentVal
     * @param range
     * @return
     */
    private static String getAssessment(String currentVal, String range) {
        if(currentVal == null || range == null)
            throw new NullPointerException();

        String[] rangeArray = range.split(SEPARATOR);
        if(rangeArray.length != 2)
            throw new IllegalArgumentException("range must be splite as 2 length array!");

        float curVal = Float.valueOf(currentVal);
        float minVal = Float.valueOf(rangeArray[0]);
        float maxVal = Float.valueOf(rangeArray[1]);
        String ret = "正常";

        if(curVal < minVal)
            ret = "不足";
        else if ((curVal >= minVal) && (curVal <= maxVal)) {
            ret = "正常";
        } else {
            ret = "过量";
        }
        return ret;
    }

	/**
	 *
	 * @param tmpStr 文本
	 * @param textPaint 文字画笔
	 * @param canvas 画布
	 * @param pos 位置对象
	 */
	private void drawLineText(String tmpStr,
								   TextPaint textPaint,
								   Canvas canvas,
								   BodyComposition.Position pos) {
		canvas.drawText(
				tmpStr,
				pos.getXMils() / 1000,
				pos.getYMils() / 1000,
				textPaint);
	}

	/**
	 *
	 * @param bc BodyComposition对象
	 * @param tmpStr 文本
	 * @param textPaint 文字画笔
	 * @param canvas 画布
	 * @param pos 位置对象
	 */
	private void drawMutilLineText(BodyComposition bc,
								   String tmpStr,
								   TextPaint textPaint,
								   Canvas canvas,
								   BodyComposition.Position pos,
								   Layout.Alignment la) {
		StaticLayout textLayout = new StaticLayout(
				tmpStr,
				textPaint,
				pos.getWidthMils() / 1000,
				la, //Layout.Alignment.ALIGN_CENTER,
				1.0f,
				0.0f,
				false);
		canvas.save();
		canvas.translate(pos.getXMils() / 1000, pos.getYMils() / 1000);
		textLayout.draw(canvas);
		canvas.restore();
	}

//	/**
//	 * Creates a PDF document.
//	 * @param pdfPath the path to the new PDF document
//	 * @throws    DocumentException
//	 * @throws    IOException
//	 */
//	public void createPdfwithiText(String pdfPath, BodyComposition bc)
//			throws DocumentException, IOException {
//		Log.i(LOG_TAG, "createPdfwithiText");
//		// step 1
//		Document document = new Document(PageSize.A4);
//		// step 2
//		PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
//		// step 3
//		document.open();
//		// step 4
//		document.add(new Paragraph("Hello World!"));
//		// step 5
//		document.close();
//	}


	public static Bitmap getBitmapFromAsset(Context context, String filePath) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			// handle exception
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 体成分分析项目表
	 * 体重
	 */
    public static final int 项目_体重      = 1;
	/**
	 * BMI 身体质量
	 */
    public static final int 项目_身体质量   = 2;
	/**
	 * 体脂肪率
	 */
    public static final int 项目_体脂肪率   = 3;
	/**
	 * 体脂肪量
	 */
    public static final int 项目_体脂肪量   = 4;
	/**
	 * 肌肉量
	 */
    public static final int 项目_肌肉量    = 5;
	/**
	 * 身体水分
	 */
    public static final int 项目_身体水分  = 6;
	/**
	 * 内脏脂肪
	 */
	public static final int 项目_内脏脂肪  = 7;

	public static final int 体成分分析_TOTAL_LENGTH = 89;
	public static final int 内脏指数_TOTAL_LENGTH = 66; //89;
    public static final float 体成分分析_SECOND_START_MM = 27f;
    public static final float 体成分分析_SECOND_START_PX = 体成分分析_SECOND_START_MM * BodyComposition.Position.VALUE_72_X_1MM;
    public static final float 体成分分析_THIRD_START_MM = 49f;
    public static final float 体成分分析_THIRD_START_PX = 体成分分析_THIRD_START_MM * BodyComposition.Position.VALUE_72_X_1MM;
    public static final float 体成分分析_TOTAL_LENGTH_PX = 体成分分析_TOTAL_LENGTH * BodyComposition.Position.VALUE_72_X_1MM;

	//Item  项目
	//返回

    /**
     *
     * @param item 项目
     * @param bc BodyComposition
     * @return 进度条实际长度，单位Point
	 * 暂时不再使用
     */
    public float getProgressLength(int item, BodyComposition bc) {
		float rate = 1;
		boolean Flag = false;
		float[] P_temp = new float[2];
		int Data_Test = 0, Data_Min = 0, Data_Max = 0;
		switch (item) {
			case 项目_体重: // 体重
				Data_Test = bc.体重_CUR;
				Data_Min = bc.体重_MIN;
				Data_Max = bc.体重_MAX;
				Flag = false;
				break;
			case 项目_身体质量: // BMI
				Data_Test = bc.身体质量_CUR;
				Data_Min = bc.身体质量_MIN;
				Data_Max =  bc.身体质量_MAX;
				Flag = false;
				break;
            case 项目_体脂肪率: // 体脂肪率
                Data_Test = bc.脂肪率_CUR;
                Data_Min = bc.脂肪率_MIN;
                Data_Max = bc.脂肪率_MAX;
                if (bc.SEX == BodyComposition.MALE) //男
                    Flag = true;
                else if (bc.SEX == BodyComposition.FEMALE)
                    Flag = false;
                break;
			case 项目_体脂肪量:// 脂肪量
				Data_Test = bc.体脂肪量_CUR;
				Data_Min = bc.体脂肪量_MIN;
				Data_Max =  bc.体脂肪量_MAX;
				Flag = false;
				break;
            case 项目_肌肉量:
				Data_Test = bc.肌肉量_CUR;
                Data_Min = bc.肌肉量_MIN;
                Data_Max = bc.肌肉量_MAX;
                Flag = false;
                break;
            case 项目_身体水分:
				Data_Test = bc.身体总水分_CUR;
                Data_Min = bc.身体总水分_MIN;
                Data_Max = bc.身体总水分_MAX;
                Flag = false;
                break;
		}

		/**
		 * 求出最外边界值
		 * 根据实测来填写比例值，而不是3/2了
		 */
		P_temp[0] = Data_Min - ((Data_Max - Data_Min) * 35.5f / 21.5f);     //坐标最小值, 600
		P_temp[1] = Data_Max + ((Data_Max - Data_Min) * 32.5f / 21.5f);     //坐标最大值,1500

		float range = P_temp[1] - P_temp[0];                      //整体坐标代表的最大数值,900
		float position = 0;
		// 如果小于最最小值，设定一默认值5
		if (Data_Test < P_temp[0]) //低于最小值坐标
			position = 5;
		else {
			if (Flag) { // 脂肪量 脂肪率
				if (Data_Test > Data_Max)
					position = ((Data_Test - Data_Max) / 2 + Data_Max) - P_temp[0];
				else
					position = Data_Test - P_temp[0];
			} else // 其它
				position = Data_Test - P_temp[0];

			if (position > range) // 超出最大坐标，以最大为准
				position = range;
		}
		rate = (float) position / range;
		return rate * 体成分分析_TOTAL_LENGTH * 2836 / 1000;
	}


	/**
	 * 体成分分析　内脏脂肪　这个是一个比较复杂的计算方式
	 * 由于在A4纸上的表格并没有按照比例进行划分，所以需要进行分段以及微调才能达到效果
	 * @param bc BodyComposition
	 * @return 进度条实际长度，单位Point
	 */
	public float getProgressLength2(BodyComposition bc) {
        if (true) // 新版本已经符合比例了，不需要复杂的分段计算了
            return (float) (65.5 / 17 * bc.内脏脂肪_CUR / 10 * 2836 / 1000);
		float[] P_temp = new float[2];
		float cur = 0, min = 0, max = 0;
		cur = bc.内脏脂肪_CUR / 10f; // 10
		min = bc.内脏脂肪_MIN / 10f; // 最小1
		max = bc.内脏脂肪_MAX / 10f; // 最大17
		final float NORMAL_START = 0f;
		final float TOO_HIGH_START = 10f;
		final float HIGH_START = 14f;
		final float HIGH_END   = 17f;

		final float NORMAL_START_MM = 0f;
		final float TOO_HIGH_START_MM = 39.5f; //38.3f;
		final float HIGH_START_MM = 34.5f; // 53.5f;

		final float NORMAL_LENGTH_MM = 39.5f;
		final float TOO_HIGH_LENGTH_MM = 15.8f;
		final float HIGH_LENGTH_MM = 7; //36f;
		final float TOTAL_LENGTH_MM = 内脏指数_TOTAL_LENGTH;

		float base = 0f;
		float r = 0.1f; // 相对长度单位mm
		if(cur >= min && cur < TOO_HIGH_START) { // 正常范围内 normal
			base = NORMAL_START_MM;
			r = NORMAL_LENGTH_MM / (TOO_HIGH_START - NORMAL_START) * (cur - NORMAL_START) + base;
		} else if (cur >= TOO_HIGH_START && cur < HIGH_START) {  // 过高 too high
			base = TOO_HIGH_START_MM;
			r = TOO_HIGH_LENGTH_MM / (HIGH_START - TOO_HIGH_START)  * (cur - TOO_HIGH_START) + base;
		} else if (cur >= HIGH_START && cur < HIGH_END) { // 高 high
			base = HIGH_START_MM;
			r = HIGH_LENGTH_MM / (HIGH_END - HIGH_START) *  (cur - HIGH_START) + base;
		} else if (cur >= HIGH_END) {
			r = TOTAL_LENGTH_MM;
		}

		Log.i(LOG_TAG, "r: " + r);

		return r * 2836 / 1000;
	}

	/**
	 * 体成分分析　内脏脂肪　这个是一个比较复杂的计算方式
	 * 由于在A4纸上的表格并没有按照比例进行划分，所以需要进行分段以及微调才能达到效果
	 * @return 进度条实际长度，单位Point
	 */
	public float getProgressLength3(final float inCur, final float inMin, final float inMax) {
		float[] P_temp = new float[2];
		float cur = inCur, min = inMin, max = inMax;
		final float FIRST_START = 0f;
		final float SECOND_START = min;
		final float THIRD_START = max + 0.1f;
		final float HIGH_END   = THIRD_START + max;

		final float FIRST_START_MM = 0f;
		final float SECOND_START_MM = 体成分分析_SECOND_START_MM;// 35.8f;
		final float THIRD_START_MM = 体成分分析_THIRD_START_MM; // 56.8f;
		final float TOTAL_LENGTH_MM = 体成分分析_TOTAL_LENGTH;

		final float FIRST_LENGTH_MM = SECOND_START_MM - FIRST_START_MM;
		final float SECOND_LENGTH_MM = THIRD_START_MM - SECOND_START_MM;
		final float THIRD_LENGTH_MM = TOTAL_LENGTH_MM - THIRD_START_MM;

		float base = 0f;
		float r = 0.1f; // 第一格
		if(cur >= FIRST_START && cur < SECOND_START) {
			// 第一格
			base = FIRST_START_MM;
			r = FIRST_LENGTH_MM / (SECOND_START - FIRST_START) * (cur - FIRST_START) + base;
		} else if (cur >= min && cur < THIRD_START) {
			// 第二格
			base = SECOND_START_MM;
			r = SECOND_LENGTH_MM / (THIRD_START - SECOND_START)  * (cur - SECOND_START) + base;
		} else if (cur >= THIRD_START) {
			// 第三格
			base = THIRD_START_MM;
			r = THIRD_LENGTH_MM / (HIGH_END - THIRD_START) *  (cur - THIRD_START) + base;
		}

		/* 如果计算出来的大于最大值，刚按照最大值计算 */
		r = (r > TOTAL_LENGTH_MM) ? TOTAL_LENGTH_MM : r;

		Log.i(LOG_TAG, "r: " + r);

		return r * BodyComposition.Position.VALUE_72_X_1MM / 1000;
	}

    public String getPdfPath() {
        return mPdfPath;
    }

    public String getRasterPath() {
        return mRasterPath;
    }
}
