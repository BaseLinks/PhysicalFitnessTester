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
	/** */
	private final SerialHelper serialCtrl       = new SerialControl();;
	private final SerialPortFinder serialPortFinder = new SerialPortFinder();
	private final Context mContext;
	private String                 serialPort       = null;
	private static final String TRADITIONAL_TTY_DEV_NODE = "/dev/ttyS2";
	private static BodyComposition mBodyComposition;
	/**
	 * 需要发送的数据
	 */
	public static final byte[] SEND = new byte[]{(byte)0xAA, (byte)0xAB, 0x00, 0x04, (byte)0xDE, 0x00, (byte)0xDD, 0x6A};
	private static final int BYTE_BUFFER_ALLOCATE = 8192;

	/** 串口相关 */
	private static final String BAUDRATE_COIN = "9600";
	/** 字体大小 */
	private static final float FONT_SIZE_8 = 8;

	public BodyCompositionAnalyzer(Context context) {
		this.mContext = context;
	}

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

	public BodyComposition getBodyComposition() {
		return this.mBodyComposition;
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
			InputStream in = mContext.getResources().getAssets().open("data.bin");
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

	/**
	 * 将BodayComposition生成PDF
	 * @bc BodyComposition对象
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
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			float size = paint.getTextSize();
			paint.setTextSize(8);
			Log.i(LOG_TAG, "size: " + size);

			// 文字画笔
			TextPaint textPaint = new TextPaint();
			StaticLayout mTextLayout = null;
			String tmpStr = null;

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

			// 写「Hello World」
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText("Hello World!", 0, 4, paint);

			// 01 写姓名/编号
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.姓名,
					BodyComposition.Posistion.姓名.getXMils() / 1000,
					BodyComposition.Posistion.姓名.getYMils() / 1000,
					paint);

			// 02 身高
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.身高,
					BodyComposition.Posistion.身高.getXMils() / 1000,
					BodyComposition.Posistion.身高.getYMils() / 1000,
					paint);

			// 03 体重
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.体重1,
					BodyComposition.Posistion.体重1.getXMils() / 1000,
					BodyComposition.Posistion.体重1.getYMils() / 1000,
					paint);

			// 04 测试日期
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.测试日期,
					BodyComposition.Posistion.测试日期.getXMils() / 1000,
					BodyComposition.Posistion.测试日期.getYMils() / 1000,
					paint);

			// 05 年龄
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.年龄,
					BodyComposition.Posistion.年龄.getXMils() / 1000,
					BodyComposition.Posistion.年龄.getYMils() / 1000,
					paint);

			// 06 性别
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.性别,
					BodyComposition.Posistion.性别.getXMils() / 1000,
					BodyComposition.Posistion.性别.getYMils() / 1000,
					paint);

			/* 2. 休成分结果 */
			// 21 体重2
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.体重2,
					BodyComposition.Posistion.体重2.getXMils() / 1000,
					BodyComposition.Posistion.体重2.getYMils() / 1000,
					paint);
			// 22 去脂肪体重
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.去脂肪体重 + "[" + bc.去脂肪体重标准 + "]",
					BodyComposition.Posistion.去脂肪体重.getXMils() / 1000,
					BodyComposition.Posistion.去脂肪体重.getYMils() / 1000,
					paint);
			// 23 肌肉量
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.肌肉量 + "[" + bc.肌肉标准 + "]",
					BodyComposition.Posistion.肌肉量.getXMils() / 1000,
					BodyComposition.Posistion.肌肉量.getYMils() / 1000,
					paint);
			// 24 身体总水分
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.身体总水分 + "[" + bc.身体总水分正常范围 + "]",
					BodyComposition.Posistion.身体总水分.getXMils() / 1000,
					BodyComposition.Posistion.身体总水分.getYMils() / 1000,
					paint);
			// 25 细胞内液
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.细胞内液含量 + "[" + bc.细胞内液正常范围 + "]",
					BodyComposition.Posistion.细胞内液.getXMils() / 1000,
					BodyComposition.Posistion.细胞内液.getYMils() / 1000,
					paint);
			// 26 细胞外液
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.细胞外液含量 + "[" + bc.细胞外液正常范围 + "]",
					BodyComposition.Posistion.细胞外液.getXMils() / 1000,
					BodyComposition.Posistion.细胞外液.getYMils() / 1000,
					paint);
			// 27 蛋白质量
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.蛋白质含量 + "[" + bc.蛋白质正常范围 + "]",
					BodyComposition.Posistion.蛋白质量.getXMils() / 1000,
					BodyComposition.Posistion.蛋白质量.getYMils() / 1000,
					paint);
			// 28 无机盐量
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.无机盐含量 + "[" + bc.无机盐含量正常范围 + "]",
					BodyComposition.Posistion.无机盐量.getXMils() / 1000,
					BodyComposition.Posistion.无机盐量.getYMils() / 1000,
					paint);
			// 29 体脂肪量
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(
					bc.体脂肪量 + "[" + bc.体脂肪量标准 + "]",
					BodyComposition.Posistion.体脂肪量.getXMils() / 1000,
					BodyComposition.Posistion.体脂肪量.getYMils() / 1000,
					paint);

			/* 3. 体成分分析　*/

			/* 4X. 调节建议 */
			// 41 体重_标准 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体重标准值,
					BodyComposition.Posistion.体重_标准.getXMils() / 1000,
					BodyComposition.Posistion.体重_标准.getYMils() / 1000,
					paint);
			// 42 体重_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体重2,
					BodyComposition.Posistion.体重_当前.getXMils() / 1000,
					BodyComposition.Posistion.体重_当前.getYMils() / 1000,
					paint);
			// 43 体重_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体重调节,
					BodyComposition.Posistion.体重_调节量.getXMils() / 1000,
					BodyComposition.Posistion.体重_调节量.getYMils() / 1000,
					paint);
			// 44 身体脂肪量_标准 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体脂肪量标准,
					BodyComposition.Posistion.身体脂肪量_标准.getXMils() / 1000,
					BodyComposition.Posistion.身体脂肪量_标准.getYMils() / 1000,
					paint);
			// 45 身体脂肪量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.体脂肪量,
					BodyComposition.Posistion.身体脂肪量_当前.getXMils() / 1000,
					BodyComposition.Posistion.身体脂肪量_当前.getYMils() / 1000,
					paint);
			// 46 身体脂肪量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.脂肪调节,
					BodyComposition.Posistion.身体脂肪量_调节量.getXMils() / 1000,
					BodyComposition.Posistion.身体脂肪量_调节量.getYMils() / 1000,
					paint);
			// 47 肌肉量_标准 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉标准,
					BodyComposition.Posistion.肌肉量_标准.getXMils() / 1000,
					BodyComposition.Posistion.肌肉量_标准.getYMils() / 1000,
					paint);
			// 48 肌肉量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉量,
					BodyComposition.Posistion.肌肉量_当前.getXMils() / 1000,
					BodyComposition.Posistion.肌肉量_当前.getYMils() / 1000,
					paint);
			// 49 肌肉量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					bc.肌肉调节,
					BodyComposition.Posistion.肌肉量_调节量.getXMils() / 1000,
					BodyComposition.Posistion.肌肉量_调节量.getYMils() / 1000,
					paint);

			/* 5x 节段肌肉 */
			// 51 右上肢脂肪量 okay
			tmpStr = bc.右上肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.右上肢肌肉含量);

			// 52 右下肢脂肪量 okay
			tmpStr = bc.右下肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.右下肢肌肉含量);

			// 53 左上肢脂肪量 okay
			tmpStr = bc.左上肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.左上肢肌肉含量);

			// 54 左下肢脂肪量 okay
			tmpStr = bc.左下肢肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.左下肢肌肉含量);

			// 55 躯干肌肉含量 okay
			tmpStr = bc.躯干肌肉含量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.躯干肌肉含量);

			/* 6x 节段脂肪 */
			// 61 左上肢脂肪量 okay
			tmpStr = bc.右上肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.右上肢脂肪量);

			// 62 左下肢脂肪量 okay
			tmpStr = bc.右下肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.右下肢脂肪量);

			// 63 左上肢脂肪量 okay
			tmpStr = bc.左上肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.左上肢脂肪量);

			// 64 左下肢脂肪量 okay
			tmpStr = bc.左下肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.左下肢脂肪量);

			// 65 躯干肢脂肪量 okay
			tmpStr = bc.左下肢脂肪量 + "kg\n正常";
			drawMutilLineText(bc, tmpStr, textPaint,
					mTextLayout, canvas,
					BodyComposition.Posistion.躯干肢脂肪量);

			// 写「√」
			paint.setColor(Color.BLACK);
			canvas.drawText("√", 100, 100, paint);

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
	 *
	 * @param bc
	 * @param tmpStr
	 * @param textPaint
	 * @param textLayout
	 * @param canvas
	 * @param pos
	 */
	private void drawMutilLineText(BodyComposition bc, String tmpStr, TextPaint textPaint,
								   StaticLayout textLayout, Canvas canvas,
								   BodyComposition.Posistion pos) {
		textPaint.setTextSize(FONT_SIZE_8);
		textLayout = new StaticLayout(
				tmpStr,
				textPaint,
				pos.getWidthMils() / 1000,
				Layout.Alignment.ALIGN_CENTER,
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
}
