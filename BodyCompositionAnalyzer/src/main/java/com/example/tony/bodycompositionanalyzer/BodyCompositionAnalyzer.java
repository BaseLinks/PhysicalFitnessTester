package com.example.tony.bodycompositionanalyzer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	/** 基于传统Linux设备节点实现的串口通信 */
	private final SerialHelper serialCtrl       = new SerialControl();
	private final SerialPortFinder serialPortFinder = new SerialPortFinder();
	/** FTDI基于Android USB Api实现的串口通信，免Root */
	private UartHelper mUartHelper;
	private final Context mContext;
	private String                 serialPort       = null;
	private static final String TRADITIONAL_TTY_DEV_NODE = "/dev/ttyUSB0";
	private static BodyComposition mBodyComposition;
	/**
	 * 发送此数据，从机会将需要的数据进行回传
	 */
	public static final byte[] SEND = new byte[]{(byte)0xAA, (byte)0xAB, 0x00, 0x04, (byte)0xDE, 0x00, (byte)0xDD, 0x6A};
	private static final int BYTE_BUFFER_ALLOCATE = 8192;

	/** 串口相关 */
	private static final String BAUDRATE_COIN = "9600";
	/** 字体大小 */
	private static final float FONT_SIZE_8 = 8;

	/** 打印机对象 */
	private Printer mPrinter;

	public BodyCompositionAnalyzer(Context context) {
		this.mContext = context;
		mUartHelper = new UartControl(context);
	}

	public void init() {
		// 打开串口(初始化结果要告知用户)
        try {
            mUartHelper.setBaudRate(9600);
            mUartHelper.open();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }

        // 初始化打印机(初始化结果要告知用户)
        mPrinter = Printer.getInstance(mContext);
        if(mPrinter.getModel() == null)
            ; // 需要告知用户
	}

	private class SerialControl extends SerialHelper {
		@Override
		protected void onDataReceived(final ComBean ComRecData) {
			handleRecData(ComRecData);
		}

		/** 处理接收到串口数据事件 */
		private void handleRecData(final ComBean ComRecData) {
			if (DEBUG) Log.i(LOG_TAG, "handleRecData:" + bytesToHex(ComRecData.bRec));
            parseData2(ComRecData.bRec);
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
			parseData2(ComRecData.bRec);
		}
	}

	public BodyComposition getBodyComposition() {
		return this.mBodyComposition;
	}

    /** 接收串口数据缓存 */
    private static byte[] cache = null;

    private void parseData2(byte[] in) {
		Log.i(LOG_TAG, "parseData2");
		/* 将in存于cache中 */
        if(cache == null) /* 第一次进入 */
            cache = Arrays.copyOfRange(in, 0, in.length);
        else {            /* 将in连接到cache后面 */
            byte[] tmp = Arrays.copyOfRange(cache, 0, cache.length);
            cache = new byte[tmp.length + in.length];
            System.arraycopy(tmp, 0, cache,          0, tmp.length);
            System.arraycopy( in, 0, cache, tmp.length, in.length);
        }

        // ECC校验
//        Log.i(LOG_TAG, "ecc:" + getBCC(Arrays.copyOfRange(cache, 6, 6 + cache.length - 4)));


        byte[]     ack                 = new byte[BodyComposition.ACK_LENGTH];
		byte[]     checksum            = new byte[BodyComposition.VERIFICATION_LENGTH];
        int        length              = BodyComposition.TOTAL_LENGTH;
        byte       dataLength          = 0;
        final byte LENGTH_START        = 1;
        final byte MSG_START           = 2;
        final byte DATA_START          = 3;
        byte       ETX_START           = 0;
        byte       CHECKSUM_START      = 0;

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
                            handleError(1.1, cache, in);
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
//				/* 取出第2位 数据长度 */
//                length = cache[LENGTH_START];
//                if (length > cache.length) {
//					/* 2.1是比较常见错误，不再打LOG信息 */
//                    // handleError(2.1, cache, in);
//                    return;
//                }
//
//				/* 3.0. 解析 MSG Type & ACK (坐标+1是长度)*/
//                if(cache.length < (MSG_START + 1)) {
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    handleError(2.5, cache, in);
//                    continue loop;
//                }
//				/* 3.1 判断 MSG Type & ACK */
//                if (!parseMsgtypeAndACK(cache[MSG_START])) {
//                    handleError(3.1, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    continue loop;
//                }
//
//				/* 4.0 填充结束符位置 */
//                ETX_START = (byte) (length - 2);
//                if (ETX_START < 0) {
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    handleError(4.0, cache, in);
//                    continue loop;
//                }
//
//				/* 4.1 判断ETX */
//                if (cache[ETX_START] != ETX) {
//                    handleError(4.1, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    continue loop;
//                }
//
//				/* 5. 总长度减去 STX MSG LENGTH ETX CHECKSUM 的长度和就是数据长度 */
//                dataLength = (byte) (length - 5);
//                if (dataLength < 0) {
//                    handleError(5, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    continue loop;
//                }
//
//				/* 6. 检验和判断 */
//                CHECKSUM_START = (byte) (length - 1);
//                if (CHECKSUM_START < 0) {
//                    if (DEBUG) Log.w(LOG_TAG, "parseCoin 无效数据(CHECKSUM_START):"
//                            + CHECKSUM_START);
//                    handleError(6, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    continue loop;
//                }
//
//				/* 6. 检验和判断 TODO:添加校验码校验 */
//				checksum = Arrays.copyOfRange(
//						cache,
//						BodyComposition.VERIFICATION_START,
//						BodyComposition.VERIFICATION_START + BodyComposition.VERIFICATION_LENGTH);
//				if (checksum[2] != 0xDD) {
//					if (DEBUG) Log.w(LOG_TAG, "parseCoin 无效数据(CHECKSUM_START):"
//							+ checksum[3]);
//					handleError(6, cache, in);
//					cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//					continue loop;
//				}
//
//				/* 7. 抽取需要进行校验的数据位进行校验 LENGTH MSG DATA */
//                byte bcc = getBCC(Arrays.copyOfRange(cache, LENGTH_START,
//                        LENGTH_START + dataLength + 2));
//                if (bcc != cache[CHECKSUM_START]) {
//                    if (DEBUG) Log.w(LOG_TAG, "parseCoin 无效数据(检验和)");
//                    handleError(7, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    continue loop;
//                }
//
				/* 8. 解析数据段 TODO:对数据段进行深度匹配 */
                final byte[] dataArray = Arrays.copyOfRange(cache, BodyComposition.DATA_START,
						BodyComposition.DATA_START + BodyComposition.DATA_LENGTH);
				/* 解析数据 */
				mBodyComposition = new BodyComposition(dataArray);
//				/*
//				 * BYTE 0
//				 * 设置为01h – 无数据上报
//				 * 设置为10h – 有数据上报
//				 * （在该数据段中，可以扩充为不同的数据，不同的硬币通道）
//				 */
//                final byte BYTE0 = dataArray[0];
//				/* BYTE1: 设置为10h */
//                final byte BYTE1 = dataArray[1];
//				/*
//				 * BYTE2 说明 TODO:[Bit 3-5 硬币数据段]可以测试成功
//				 * Bit 0 – 启动位   (= 1 如果WF-700-RELAY有了一次重新启动 ) 
//				 * Bit 1 – 错误指令 (= 1 如果接收到错误指令) 
//				 * Bit 2 - Failure (= 1 如果硬币器错误，或者钱箱满等故障) (FAE:脉冲投币器不存在故障位,该位无效)
//				 * Bit 3-5 硬币数据段
//				 * 000 = None 
//				 * 001 = 1st credit channel type 
//				 * 010 = 2nd credit channel type 
//				 * 011 = 3rd credit channel type
//				 * 100 = 4th credit channel type 
//				 * 101 = 5th credit channel type 
//				 * 110 = 6th credit channel type, Pulse channel   (WF-700-RELAY 使用该段) 
//				 * 111 = Reserved 
//				 * Bit 6- Reserved (set to 0) 
//				 */
//                final byte BYTE2 = dataArray[2];
//				/* BYTE 3, BYTE 4 堆栈中剩余的硬币信息数据 */
//                @SuppressWarnings("unused")
//                final byte BYTE3 = dataArray[3];
//                @SuppressWarnings("unused")
//                final byte BYTE4 = dataArray[4];
//				/* BYTE5 设置为 01h */
//                final byte BYTE5 = dataArray[5];
//
//				/* 判断BYTE1和BYTE5是否正确 */
//                if (BYTE1 != DATA_BYTE1 || BYTE5 != DATA_BYTE5_RECEIVE) {
//                    if (DEBUG) Log.w(LOG_TAG, "parseCoin 无效数据(BYTE0和BYTE5)");
//                    handleError(8.1, cache, in);
//                    cache = Arrays.copyOfRange(cache, 0 + length, cache.length);
//                    return;
//                }
//
//				/* 判断是否有数据上报 */
//                if (BYTE0 == HAVE_DATA_REPORT) {
//					/* 判断　BYTE2　Bit 3-5 硬币数据段 是否为WF700RELAY　 */
//                    final byte coinDataChannel = (byte) ((BYTE2 & DATA_BYTE2_35_REC_MASK) >> DATA_BYTE2_35_REC_POS);
//                    if (coinDataChannel != DATA_BYTE2_35_REC_WF700RELAY) {
//                        if (DEBUG) Log.w(LOG_TAG, "parseCoin 无效数据(BYTE2)");
//                        handleError(8.2, cache, in);
//                        cache = Arrays.copyOfRange(cache, 0 + length,
//                                cache.length);
//                        return;
//                    }
//					/* 解析硬币信息 循环读取，不用判断BYTE3 BYTE4值 不再打印调试信息 */
//                    // String s1 = String.format("%8s",
//                    // Integer.toBinaryString(BYTE1 & 0xFF)).replace(' ',
//                    // '0');
//                    // Log.i(LOG_TAG, "parseCoin: BYTE3:" + BYTE3 + " BYTE4:"
//                    // + BYTE4 + " BYTE1:" + s1);
//                    if (DEBUG) Log.i(LOG_TAG, "parseCoin: 获得一个硬币");
//					/* 调用获得一个币方法 */
//                    onGetCoin(getOrder());
//                } else if (BYTE0 == HAVE_NO_DATA_REPORT) {
//					/* 无数据上传时不再打印调试信息 */
//                    // Log.i(LOG_TAG, "parseCoin: 无数据上报");
//                } else {
//                    Log.e(LOG_TAG, "parseCoin 未知状态");
//                }
//
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

	private void parseData(byte[] inputData) {
		Log.i(LOG_TAG, "parseData" + " inputData.length =" + inputData.length);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        /* 1. 判断回复是否正常 */
        if(BodyComposition.TOTAL_LENGTH != inputData.length) {
            Log.e(LOG_TAG, "inputData.length =" + inputData.length + " but TOTAL_LENGTH need " + BodyComposition.TOTAL_LENGTH);
            return;
        }

        /* 2. 判断回复是否正常 */
		byte[] ack = new byte[BodyComposition.ACK_LENGTH];
		System.arraycopy(inputData, BodyComposition.ACK_START, ack, 0, BodyComposition.ACK_LENGTH);
		if(Arrays.equals(ack, BodyComposition.ACK)) {
            /* 提取各个数据 */
			byte[] data2 = new byte[BodyComposition.DATA_LENGTH];
			System.arraycopy(inputData, BodyComposition.DATA_START, data2, 0, BodyComposition.DATA_LENGTH);
            /* 解析数据 */
			mBodyComposition = new BodyComposition(data2);
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
		}

		return true;
	}

	/**
	 * 做一次获取数据处理
	 * @return 成功返回true,否则返回false
	 */
	public boolean doIt(boolean isDebug)  throws IOException {
		Log.i(LOG_TAG, "doIt");

		/* 0.调试模式，不使用串口，直接读取已有数据 */
		if(isDebug) {
			// 读取样本数据
			InputStream in = mContext.getResources().getAssets().open("data3.bin");
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = in.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
			byte[] bufferArray = buffer.toByteArray();
			// 解析数据
			parseData(bufferArray);

			return true;
		}

		try {
			mUartHelper.setBaudRate(9600);
			mUartHelper.open();
			mUartHelper.send(getDianjiTest());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}

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
		return false;
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
		return createPdf(bc);
	}

	/**
	 * 当API >= 19 时使用系统自带PDF API，否则使用iText
	 * @return 返回文件路径
	 */
	public String createPdf(BodyComposition bc) {
		Log.i(LOG_TAG, "createPdf");
		String pdfPath = mContext.getExternalFilesDir(Environment.DIRECTORY_DCIM)
				+ File.separator + "test.pdf";
		// TODO: Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
		if (1 + 1 == 2) {
			// 系统API
			createPdfwithAndroid(pdfPath, bc);
		} else {
			// 使用iText
			try {
				createPdfwithiText(pdfPath, bc);
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			defPaint.setTextSize(8);
            Rect rect = null;
            int high = getTextHeight("HelloWorld", defPaint);
            Log.i(LOG_TAG, "text high: " + high);
			Paint paint = new Paint(defPaint);
			Log.i(LOG_TAG, "size: " + size);

			Layout.Alignment mAlignment;

			// 文字画笔
			TextPaint textPaint = new TextPaint();
			String tmpStr;

			// 0.1 画底板 (调试对比使用，成品不画此界面)
			Bitmap bm = getBitmapFromAsset(mContext, "body_composition_negative.jpg");
			if(bm != null) {
				// 将图片拉伸至整个页面
				canvas.drawBitmap(bm, null, new Rect(
								0,
								0, PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
								PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000),
						null);
			}

			textPaint.setTextSize(8);

			// 01 写姓名/编号
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.姓名;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.姓名, mAlignment);

			// 02 身高
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.身高;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.身高, mAlignment);

			// 03 体重
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.体重1;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.体重1, mAlignment);

			// 04 测试日期
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.测试日期;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.测试日期, mAlignment);

			// 05 年龄
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.年龄;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.年龄, mAlignment);

			// 06 性别
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.性别;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.性别, mAlignment);

			/* 2x. 休成分结果 */
			// 21 体重2
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.体重2 + "kg [" + bc.体重标准范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.体重2, mAlignment);

			// 22 去脂肪体重
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.去脂肪体重 + "kg [" + bc.去脂肪体重标准 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.去脂肪体重, mAlignment);

			// 23 肌肉量
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.肌肉量 + "kg [" + bc.肌肉标准 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.肌肉量, mAlignment);

			// 24 身体总水分
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			tmpStr = bc.身体总水分 + "kg [" + bc.身体总水分正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.身体总水分, mAlignment);

			// 25 细胞内液 okay
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.细胞内液含量 + "kg [" + bc.细胞内液正常范围 + "]";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.细胞内液, mAlignment);


			// 26 细胞外液 okay
			paint.setColor(Color.BLACK);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			tmpStr = bc.细胞外液含量 + "kg [" + bc.细胞外液正常范围 + "]";
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

			/* 3x. 体成分分析　*/
            /* 31. 体重 */
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(5f);
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			float xPos = BodyComposition.Position.体成分分析_体重.getXMils() / 1000 + getProgressLength(项目_体重, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_体重.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_体重.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_体重.getYMils() / 1000,
                    paint);
			canvas.drawText(bc.体重_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_体重.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
			// 还原
			paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 32. 身体质量(BMI) */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_身体质量.getXMils() / 1000 + getProgressLength(项目_身体质量, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_身体质量.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_身体质量.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_身体质量.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.身体质量_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_身体质量.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 33. 体脂肪率 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_体脂肪率.getXMils() / 1000 + getProgressLength(项目_体脂肪率, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_体脂肪率.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_体脂肪率.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_体脂肪率.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.脂肪率_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_体脂肪率.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 34. 体脂肪量 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_体脂肪量.getXMils() / 1000 + getProgressLength(项目_体脂肪量, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_体脂肪量.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_体脂肪量.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_体脂肪量.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.体脂肪量_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_体脂肪量.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 35. 肌肉量 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_肌肉量.getXMils() / 1000 + getProgressLength(项目_肌肉量, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_肌肉量.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_肌肉量.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_肌肉量.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.肌肉量_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_肌肉量.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 36. 身体水分 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_身体水分.getXMils() / 1000 + getProgressLength(项目_身体水分, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_身体水分.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_身体水分.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_身体水分.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.脂肪率_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_身体水分.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

            /* 37. 内脏脂肪 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            mAlignment = Layout.Alignment.ALIGN_CENTER;
            xPos = BodyComposition.Position.体成分分析_内脏脂肪.getXMils() / 1000 + getProgressLength(项目_内脏脂肪, bc);
            canvas.drawLine(
                    BodyComposition.Position.体成分分析_内脏脂肪.getXMils() / 1000,
                    BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    xPos,
                    BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    paint);
            canvas.drawText(bc.内脏脂肪_CUR / 10 + "", xPos, BodyComposition.Position.体成分分析_内脏脂肪.getYMils() / 1000 + paint.getTextSize() / 2 - 2, paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

			/* 4X. 调节目标 */
			// 41 体重_标准 okay 注：根据当前值和调节量倒倒推
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			/* 体重标准是 脂肪调节量、肌肉调节量 与 体重 之和 */
			tmpStr = String.valueOf(eval(bc.体重2 + String.valueOf(eval(bc.脂肪调节 + bc.肌肉调节))));
			canvas.drawText(tmpStr,
					BodyComposition.Position.体重_标准.getXMils() / 1000,
					BodyComposition.Position.体重_标准.getYMils() / 1000,
					paint);
			// 42 体重_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体重2,
					BodyComposition.Position.体重_当前.getXMils() / 1000,
					BodyComposition.Position.体重_当前.getYMils() / 1000,
					paint);
			// 43 体重_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			/* 体重调节量是 脂肪调节量 和 肌肉调节量 之和 */
			tmpStr = String.valueOf(eval(bc.脂肪调节 + bc.肌肉调节));
			canvas.drawText(
					tmpStr,
					BodyComposition.Position.体重_调节量.getXMils() / 1000,
					BodyComposition.Position.体重_调节量.getYMils() / 1000,
					paint);
			// 44 身体脂肪量_标准 okay 注：身体脂肪量标准：取体脂肪标准值下界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体脂肪量标准.split("-")[0],
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
			canvas.drawText(
					bc.脂肪调节,
					BodyComposition.Position.身体脂肪量_调节量.getXMils() / 1000,
					BodyComposition.Position.身体脂肪量_调节量.getYMils() / 1000,
					paint);
			// 47 肌肉量_标准 okay 肌肉量标准：取标准值上界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉标准.split("-")[1],
					BodyComposition.Position.肌肉量_标准.getXMils() / 1000,
					BodyComposition.Position.肌肉量_标准.getYMils() / 1000,
					paint);
			// 48 肌肉量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉量,
					BodyComposition.Position.肌肉量_当前.getXMils() / 1000,
					BodyComposition.Position.肌肉量_当前.getYMils() / 1000,
					paint);
			// 49 肌肉量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉调节,
					BodyComposition.Position.肌肉量_调节量.getXMils() / 1000,
					BodyComposition.Position.肌肉量_调节量.getYMils() / 1000,
					paint);

			/* 5x 节段肌肉　镜像 */
			mAlignment = Layout.Alignment.ALIGN_CENTER;
			// 51 左上肢肌肉含量 okay
			tmpStr = bc.左上肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左上肢肌肉含量, mAlignment);

			// 52 左下肢肌肉含量 okay
			tmpStr = bc.左下肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左下肢肌肉含量, mAlignment);

			// 53 右上肢肌肉含量 okay
			tmpStr = bc.右上肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右上肢肌肉含量, mAlignment);

			// 54 右下肢脂肪量 okay
			tmpStr = bc.右下肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右下肢肌肉含量, mAlignment);

			// 55 躯干肌肉含量 okay
			tmpStr = bc.躯干肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.躯干肌肉含量, mAlignment);

			/* 6x 节段脂肪 */
			// 61 左上肢脂肪量 okay
			tmpStr = bc.左上肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左上肢脂肪量, mAlignment);

			// 62 左下肢脂肪量 okay
			tmpStr = bc.左下肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左下肢脂肪量, mAlignment);

			// 63 右上肢脂肪量 okay
			tmpStr = bc.右上肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右上肢脂肪量, mAlignment);

			// 64 右下肢脂肪量 okay
			tmpStr = bc.右下肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右下肢脂肪量, mAlignment);

			// 65 躯干肢脂肪量 okay
			tmpStr = bc.躯干脂肪 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.躯干肢脂肪量, mAlignment);

			/* 7x 节段电阻抗 */
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			// 71 频率 okay
			tmpStr = "5k";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.频率_5k, mAlignment);

			tmpStr = "50k";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.频率_50k, mAlignment);

			tmpStr = "250k";
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.频率_250k, mAlignment);

			// 72 右上肢 okay
			tmpStr = bc._5k下ra电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_5k, mAlignment);

			tmpStr = bc._50k下ra电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_50k, mAlignment);

			tmpStr = bc._250k下ra电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右上肢_250k, mAlignment);

			// 73 左上肢 okay
			tmpStr = bc._5k下la电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_5k, mAlignment);

			tmpStr = bc._50k下la电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_50k, mAlignment);

			tmpStr = bc._250k下la电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左上肢_250k, mAlignment);


			// 74 躯干 okay
			tmpStr = bc._5k下tr电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.躯干_5k, mAlignment);

			tmpStr = bc._50k下tr电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.躯干_50k, mAlignment);

			tmpStr = bc._250k下tr电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.躯干_250k, mAlignment);

			// 75 右下肢 okay
			tmpStr = bc._5k下rl电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_5k, mAlignment);

			tmpStr = bc._50k下rl电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_50k, mAlignment);

			tmpStr = bc._250k下rl电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.右下肢_250k, mAlignment);

			// 76 左下肢 okay
			tmpStr = bc._5k下ll电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_5k, mAlignment);

			tmpStr = bc._50k下ll电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_50k, mAlignment);

			tmpStr = bc._250k下ll电阻值;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.左下肢_250k, mAlignment);

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

			textPaint.setTextSize(20);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			drawMutilLineText(bc, "√", textPaint, canvas, bcp, mAlignment);

            // 82.肥胖评估 脂肪量
			tmpStr = getAssessment(bc.体脂肪量, bc.体脂肪量标准);
			if(tmpStr.equals("不足")) {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_不足;
			} else if(tmpStr.equals("正常")) {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_正常;
			} else {
				bcp = BodyComposition.Position.肥胖评估_脂肪量_过量;
			}
			drawMutilLineText(bc, "√", textPaint, canvas, bcp, mAlignment);

            // 83.肥胖评估 肌肉量
			tmpStr = getAssessment(bc.肌肉量, bc.肌肉标准);
			if(tmpStr.equals("不足")) {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_不足;
			} else if(tmpStr.equals("正常")) {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_正常;
			} else {
				bcp = BodyComposition.Position.肥胖评估_肌肉量_过量;
			}
			drawMutilLineText(bc, "√", textPaint, canvas, bcp, mAlignment);

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

			textPaint.setTextSize(20);
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			drawMutilLineText(bc, "√", textPaint, canvas, bcp, mAlignment);

            // 92. 无机盐
            tmpStr = getAssessment(bc.无机盐含量, bc.无机盐含量正常范围);
            if(tmpStr.equals("不足")) {
                bcp = BodyComposition.Position.营养评估_无机盐_不足;
            } else if(tmpStr.equals("正常")) {
                bcp = BodyComposition.Position.营养评估_无机盐_正常;
            } else {
                bcp = BodyComposition.Position.营养评估_无机盐_过量;
            }
			drawMutilLineText(bc, "√", textPaint, canvas, bcp, mAlignment);

			// 93.基础代谢量 okay
            textPaint.setTextSize(8);
			tmpStr = bc.基础代谢量;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.基础代谢量, mAlignment);

			// 94.总能量消耗　okay
			tmpStr = bc.总能量消耗;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.总能量消耗, mAlignment);

			// 95.身体年龄 okay
			tmpStr = bc.身体年龄;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.身体年龄, mAlignment);

            // 10x.体型分析
            drawShapeAnalysis(bc, textPaint, canvas);

			// 11x.健康评估 okay
			textPaint.setTextSize(20);
			tmpStr = bc.身体总评分;
			drawMutilLineText(bc, tmpStr, textPaint, canvas, BodyComposition.Position.健康评估, mAlignment);

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

        Log.i(LOG_TAG, "xPos: " + xPos + " yPos: " + yPos);

        xPos = BodyComposition.ORIGIN_X + xPos * BodyComposition.SINGLE_RECT_WIDTH + BodyComposition.SINGLE_RECT_WIDTH / 2;
        yPos = BodyComposition.ORIGIN_Y - yPos * BodyComposition.SINGLE_RECT_HEIGHT - BodyComposition.SINGLE_RECT_HEIGHT / 2;

        /**
         * 坐标由iso mm转换为英寸point
         */
		textPaint.setTextSize(20);
        canvas.drawText("■", (float)(xPos * 2836 - 10) / 1000, (float)(yPos * 2836 + 10) / 1000, textPaint);
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

	/**
	 * Creates a PDF document.
	 * @param pdfPath the path to the new PDF document
	 * @throws    DocumentException
	 * @throws    IOException
	 */
	public void createPdfwithiText(String pdfPath, BodyComposition bc)
			throws DocumentException, IOException {
		Log.i(LOG_TAG, "createPdfwithiText");
		// step 1
		Document document = new Document(PageSize.A4);
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
		// step 3
		document.open();
		// step 4
		document.add(new Paragraph("Hello World!"));
		// step 5
		document.close();
	}


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

	//Item  项目
	//返回

    /**
     *
     * @param item 项目
     * @param bc BodyComposition
     * @return 进度条实际长度，单位Point
     */
    public float getProgressLength(int item, BodyComposition bc) {
		float rate = 1;
		boolean Flag = false;
		int[] P_temp = new int[2];
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
				int Long_Num = bc.体脂肪量_CUR;
				Long_Num = Long_Num * 1000;

				// 将标准量设为100%进行比较
				Long_Num =  Long_Num / ((bc.体脂肪量_MIN + bc.体脂肪量_MAX)/2);
				Data_Test = Long_Num;
				Data_Min = 900;
				Data_Max =  1100;
				Flag = false;
				break;
            case 项目_肌肉量:
                Long_Num = bc.肌肉量_CUR;
                Long_Num = Long_Num * 1000;
                // 将标准量设为100%进行比较
                Long_Num = Long_Num / ((bc.肌肉量_MIN + bc.肌肉量_MAX) / 2);
                Data_Test = Long_Num;
                Data_Min = 900;
                Data_Max = 1100;
                Flag = false;

                break;
            case 项目_身体水分:
                Long_Num = bc.身体总水分_CUR;
                Long_Num = Long_Num * 1000;

                // 将标准量设为100%进行比较
                Long_Num = Long_Num / ((bc.身体总水分_MIN + bc.身体总水分_MAX) / 2);
                Data_Test = Long_Num;
                Data_Min = 900;
                Data_Max = 1100;
                Flag = false;
                break;
            case 项目_内脏脂肪:
				Data_Test = (int) (Float.valueOf(bc.内脏脂肪指数) - 2);
				Data_Min = 100; //100; //1位小数点
				Data_Max = 140;
				Flag = false;
				break;
		}

		/**
		 * 求出最外边界值
		 */
		P_temp[0] = Data_Min - ((Data_Max - Data_Min) * 3 / 2); //坐标最小值, 600
		P_temp[1] = Data_Max + ((Data_Max - Data_Min) * 2);     //坐标最大值,1500
		int range = P_temp[1] - P_temp[0];                      //整体坐标代表的最大数值,900
		int position = 0;
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
		return rate * 90 * 2836 / 1000;
	}
}
