package com.kangear.bca.report;

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
import android.print.PrintAttributes;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.kangear.bca.BodyComposition;
import com.kangear.bca.Coordinate;
import com.kangear.bca.Coordinate.Position;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.kangear.bca.Coordinate.TEXT_SIZE_DEF;
import static com.kangear.bca.Coordinate.TEXT_SIZE_体型分析;
import static com.kangear.bca.Coordinate.TEXT_SIZE_体成分结果;
import static com.kangear.bca.Coordinate.TEXT_SIZE_健康评估;
import static com.kangear.bca.Coordinate.TEXT_SIZE_身体年龄;
import static com.kangear.bca.Coordinate.VALUE_72_X_1MM;

/**
 * 不处理数据，只接收BodyComposition对象
 * 可以接收不同的Coordinate对象
 */
public class CreateReport {
 	private static final boolean DEBUG = true;
	private static final String LOG_TAG = "CreateReport";
	private static final String TAG = "CreateReport";
	private static Context mContext =  null;

	private static final String TEXT_TICK    = "√";
	private static final String TEXT_体型分析 = "✔";

	// 坐标系: 可以设置不现的坐标
	private Coordinate mCd = new Coordinate();
	private boolean mBackground = false;

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static CreateReport singleton = null;

	public static CreateReport getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (CreateReport.class) {
                if (singleton== null)  {
                    singleton= new CreateReport(context);
                }
            }
        }
        return singleton;
    }

    public void setCoordinate(Coordinate value) {
		mCd = value;
	}

	public void setBackground(boolean value) {
		mBackground = value;
	}

    private CreateReport(Context context) {
        this.mContext = context;
        if (mContext == null) {
        	Log.e(LOG_TAG, "mContext can not be null!");
		}
    }

	/**
	 * 将BodayComposition生成PDF
	 * @param bc BodyComposition对象
	 * @return path
	 */
	public String toPdf(BodyComposition bc) {
		Log.i(LOG_TAG, "toPdf");

		/* 2. 生成PDF两种方法：Android Api或者iText Api */
//		String pdf = createPdf(mPdfPath, bc);
//		MyIntentService.startActionPdfToOpen(mContext, "", "");
//		if(!PreferencesUtils.getBoolean(mContext, KEY_DO_NOT_PRINT))
//			MyIntentService.startActionPdfToPrinter(mContext, "", "");
        return "";
	}

	/**
	 * 当API >= 19 时使用系统自带PDF API，否则使用iText
	 * @return 返回文件路径
	 */
	public String createPdf(BodyComposition bc) {
		Log.i(LOG_TAG, "createPdf");
		String pdf = mContext.getCacheDir() + File.separator + "test.pdf";
		// 系统API
		createPdfwithAndroid(pdf, bc);
		return pdf;
	}

    public int getTextHeight(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.bottom + bounds.height();
        return height;
    }

    public static final String FLOAT_2_FORMAT                      = "%.2f";
    public static final String FLOAT_1_FORMAT                      = "%.1f";
    public static final String FLOAT_0_FORMAT                      = "%.0f";

	private void drawSimpleWithoutUnit(Paint paint, TextPaint textPaint, Canvas canvas, BodyComposition.Third t, Position p, String format) {
		paint.setColor(Color.BLACK);
		String tmpStr = String.format(format, t.getCur());
		drawLineText(tmpStr, textPaint, canvas, p);
	}

    private void drawSimple(Paint paint, TextPaint textPaint, Canvas canvas, BodyComposition.Third t, Position p, String format) {
		paint.setColor(Color.BLACK);
        String tmpStr = String.format(format, t.getCur()) + t.getUnit();
		drawLineText(tmpStr, textPaint, canvas, p);
	}

	private void drawRange(Paint paint, TextPaint textPaint, Canvas canvas, BodyComposition.Third t, Position p, String format, boolean isRow) {
		paint.setColor(Color.BLACK);
		String separator = isRow ? " " : "\n";
		String tmpStr = String.format(format + t.getUnit() + separator + "[" + format + "-" + format + "]", t.getCur(), t.getMin(), t.getMax());
		if (isRow)
			drawLineText(tmpStr, textPaint, canvas, p);
		else {
			StaticLayout textLayout = new StaticLayout(
					tmpStr,
					textPaint,
					p.getWidthMils() / 1000,
					Layout.Alignment.ALIGN_CENTER,
					1.0f,
					0.0f,
					false);
			canvas.save();
			canvas.translate(p.getXMils() / 1000, p.getYMils() / 1000);
			textLayout.draw(canvas);
			canvas.restore();
		}
    }

    private void drawJindutiao(Paint paint, TextPaint textPaint, Canvas canvas, BodyComposition.Third t, Position p, Paint defPaint) {
		float STROCK = 5f;
		textPaint.setTextSize(TEXT_SIZE_DEF); // 还原字体大小
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(STROCK);
		Layout.Alignment mAlignment = Layout.Alignment.ALIGN_CENTER;
		float xPos = p.getXMils() / 1000 + mCd.getProgressLength3(t.getCur(), t.getMin(), t.getMax());
		drawProgress(canvas, p, xPos, paint);
		canvas.drawText(
				String.format(FLOAT_1_FORMAT, t.getCur()),
				xPos,
				p.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
				textPaint);
		// 还原
		paint.setStrokeWidth(defPaint.getStrokeWidth());
		paint.setShader(defPaint.getShader());
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
            //defPaint.setTypeface(mFontTypeface);
            Rect rect = null;
            int high = getTextHeight("HelloWorld", defPaint);
            Log.i(LOG_TAG, "text high: " + high);
			Paint paint = new Paint(defPaint);
			Log.i(LOG_TAG, "size: " + size);

			Layout.Alignment mAlignment;

			// 文字画笔
			TextPaint textPaint = new TextPaint();
            //textPaint.setTypeface(mFontTypeface);
			String tmpStr;

			// 0.1 画底板 (调试对比使用，成品不画此界面)
			if(mBackground) {
				Bitmap bm = getBitmapFromAsset(mContext, mCd.BACKGROUND);
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
			drawSimple(paint, textPaint, canvas, bc.姓名, mCd.姓名, FLOAT_0_FORMAT);

			// 02 身高
			drawSimpleWithoutUnit(paint, textPaint, canvas, bc.身高, mCd.身高, FLOAT_0_FORMAT);

			// 03 体重
			drawSimpleWithoutUnit(paint, textPaint, canvas, bc.体重, mCd.体重1, FLOAT_1_FORMAT);

			// 04 测试日期 (无法使用drawSimple)
			tmpStr = String.valueOf(bc.测试时间.getUnit());
			drawLineText(tmpStr, textPaint, canvas, mCd.测试日期);

			// 05 年龄
			drawSimple(paint, textPaint, canvas, bc.年龄, mCd.年龄, FLOAT_0_FORMAT);

			// 06 性别
			tmpStr = String.valueOf(bc.性别.getUnit());
			drawLineText(tmpStr, textPaint, canvas, mCd.性别);

			/* 2x. 体成分结果 */
			// 21 体重2
            textPaint.setTextSize(TEXT_SIZE_体成分结果);
			drawRange(paint, textPaint, canvas, bc.体重, mCd.体重2, FLOAT_1_FORMAT, true);

			// 22 去脂肪体重
			drawRange(paint, textPaint, canvas, bc.去脂体重, mCd.去脂肪体重, FLOAT_1_FORMAT, true);

			// 23 肌肉量
			drawRange(paint, textPaint, canvas, bc.肌肉量, mCd.肌肉量, FLOAT_1_FORMAT, true);

			// 24 身体总水分
			drawRange(paint, textPaint, canvas, bc.身体水分, mCd.身体总水分, FLOAT_1_FORMAT, true);

			// 25 细胞内液 okay
			drawRange(paint, textPaint, canvas, bc.细胞内液含量, mCd.细胞内液, FLOAT_1_FORMAT, false);

			// 26 细胞外液 okay
			drawRange(paint, textPaint, canvas, bc.细胞外液含量, mCd.细胞外液, FLOAT_1_FORMAT, false);

			// 27 蛋白质量 ok
			drawRange(paint, textPaint, canvas, bc.蛋白质, mCd.蛋白质量, FLOAT_1_FORMAT, false);

			// 28 无机盐量 ok
			drawRange(paint, textPaint, canvas, bc.无机盐, mCd.无机盐量, FLOAT_1_FORMAT, false);

			// 29 体脂肪量 ok
			drawRange(paint, textPaint, canvas, bc.体脂肪量, mCd.体脂肪量, FLOAT_1_FORMAT, false);

			/* 3x. 体成分分析　*/
            /* 31. 体重 */
			drawJindutiao(paint, textPaint, canvas, bc.体重, mCd.体成分分析_体重, defPaint);

            /* 32. 身体质量(BMI) */
			drawJindutiao(paint, textPaint, canvas, bc.BMI, mCd.体成分分析_身体质量, defPaint);

            /* 33. 体脂肪率 */
			drawJindutiao(paint, textPaint, canvas, bc.脂肪率, mCd.体成分分析_体脂肪率, defPaint);

            /* 34. 体脂肪量 */
			drawJindutiao(paint, textPaint, canvas, bc.体脂肪量, mCd.体成分分析_体脂肪量, defPaint);

            /* 35. 肌肉量 */
			drawJindutiao(paint, textPaint, canvas, bc.肌肉量, mCd.体成分分析_肌肉量, defPaint);

            /* 36. 身体水分 */
			drawJindutiao(paint, textPaint, canvas, bc.身体水分, mCd.体成分分析_身体水分, defPaint);

            /* 37. 内脏脂肪 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5f);
            float xPos = mCd.体成分分析_内脏脂肪.getXMils() / 1000 + mCd.getNeizangProgress(bc);
            canvas.drawLine(
					mCd.体成分分析_内脏脂肪.getXMils() / 1000,
					mCd.体成分分析_内脏脂肪.getYMils() / 1000,
                    xPos,
					mCd.体成分分析_内脏脂肪.getYMils() / 1000,
                    paint);
            canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.内脏脂肪指数.getCur()),
					xPos,
					mCd.体成分分析_内脏脂肪.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

			/* 4X. 调节目标 */
			// 41 体重_标准 okay 注：根据当前值和调节量倒倒推
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(String.format(FLOAT_1_FORMAT, bc.体重.getCur() + bc.体重调节.getCur()),
					mCd.体重_标准.getXMils() / 1000,
					mCd.体重_标准.getYMils() / 1000,
					paint);
			// 42 体重_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体重.getCur()),
					mCd.体重_当前.getXMils() / 1000,
					mCd.体重_当前.getYMils() / 1000,
					paint);
			// 43 体重_调节量 okay
			/* 体重调节量是 脂肪调节量 和 肌肉调节量 之和 */
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			float tmpFloat = bc.脂肪调节.getCur() + bc.肌肉调节.getCur();
			// 显示规则：大于[+],其他显示[-]
			canvas.drawText(
                    String.format(((tmpFloat > 0) ? "+" : "-") + FLOAT_1_FORMAT, ((tmpFloat < 0) ? -tmpFloat : tmpFloat)),
					mCd.体重_调节量.getXMils() / 1000,
					mCd.体重_调节量.getYMils() / 1000,
					paint);
			// 44 身体脂肪量_标准 okay 注：身体脂肪量标准：取体脂肪标准值下界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体脂肪量.getMin()),
					mCd.身体脂肪量_标准.getXMils() / 1000,
					mCd.身体脂肪量_标准.getYMils() / 1000,
					paint);
			// 45 身体脂肪量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体脂肪量.getCur()),
					mCd.身体脂肪量_当前.getXMils() / 1000,
					mCd.身体脂肪量_当前.getYMils() / 1000,
					paint);
			// 46 身体脂肪量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = bc.脂肪调节.getCur();
			// 显示规则：_大于_0显示[+],其他显示[-]
			canvas.drawText(
					String.format(((tmpFloat > 0) ? "+" : "-") + FLOAT_1_FORMAT, ((tmpFloat < 0) ? -tmpFloat : tmpFloat)),
					mCd.身体脂肪量_调节量.getXMils() / 1000,
					mCd.身体脂肪量_调节量.getYMils() / 1000,
					paint);

			// 47 肌肉量_标准 okay 肌肉量标准：取标准值上界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.肌肉量.getMax()),
					mCd.肌肉量_标准.getXMils() / 1000,
					mCd.肌肉量_标准.getYMils() / 1000,
					paint);
			// 48 肌肉量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.肌肉量.getCur()),
					mCd.肌肉量_当前.getXMils() / 1000,
					mCd.肌肉量_当前.getYMils() / 1000,
					paint);
			// 49 肌肉量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = bc.肌肉调节.getCur();
			// 显示规则：_大于等于_0显示[+],其他显示[-]
			canvas.drawText(
					String.format(((tmpFloat >= 0) ? "+" : "-") + FLOAT_1_FORMAT, ((tmpFloat < 0) ? -tmpFloat : tmpFloat)),
					mCd.肌肉量_调节量.getXMils() / 1000,
					mCd.肌肉量_调节量.getYMils() / 1000,
					paint);

			/* 5x 节段肌肉　镜像 */
			// 51 左上肢肌肉量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左上肢肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左上肢肌肉含量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.左上肢肌肉量.getLevelAsChinese(), textPaint, canvas, mCd.左上肢肌肉含量);

			// 52 左下肢肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左下肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左下肢肌肉含量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.左下肌肉量.getLevelAsChinese(), textPaint, canvas, mCd.左下肢肌肉含量);

			// 53 右上肢肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右上肢肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右上肢肌肉含量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.右上肢肌肉量.getLevelAsChinese(), textPaint, canvas, mCd.右上肢肌肉含量);

			// 54 右下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右下肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右下肢肌肉含量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.右下肌肉量.getLevelAsChinese(), textPaint, canvas, mCd.右下肢肌肉含量);

			// 55 躯干肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.躯干肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.躯干肌肉含量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.躯干肌肉量.getLevelAsChinese(), textPaint, canvas, mCd.躯干肌肉含量);

			/* 6x 节段脂肪 */
			// 61 左上肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左上脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左上肢脂肪量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.左上脂肪量.getLevelAsChinese(), textPaint, canvas, mCd.左上肢脂肪量);

			// 62 左下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左下脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左下肢脂肪量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.左下脂肪量.getLevelAsChinese(), textPaint, canvas, mCd.左下肢脂肪量);

			// 63 右上肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右上脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右上肢脂肪量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.右上脂肪量.getLevelAsChinese(), textPaint, canvas, mCd.右上肢脂肪量);

			// 64 右下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右下脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右下肢脂肪量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.右下脂肪量.getLevelAsChinese(), textPaint, canvas, mCd.右下肢脂肪量);

			// 65 躯干肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.躯干脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.躯干肢脂肪量);
			if (mCd.isShowJieduanChinese)
				drawLineTextUint(bc.躯干脂肪量.getLevelAsChinese(), textPaint, canvas, mCd.躯干肢脂肪量);

			/* 7x 节段电阻抗 */
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			// 71 频率 okay
			tmpStr = "5k";
			drawLineText(tmpStr, textPaint, canvas, mCd.频率_5k);

			tmpStr = "50k";
			drawLineText(tmpStr, textPaint, canvas, mCd.频率_50k);

			tmpStr = "250k";
			drawLineText(tmpStr, textPaint, canvas, mCd.频率_250k);

			// 72 右上肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右上肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右上肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右上肢_250k);

			// 73 左上肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左上肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左上肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左上肢_250k);


			// 74 躯干 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.躯干_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.躯干_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.躯干_250k);

			// 75 右下肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻RL.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右下肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻RL.getCur());
			drawLineText( tmpStr, textPaint, canvas, mCd.右下肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻RL.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.右下肢_250k);

			// 76 左下肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左下肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左下肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, mCd.左下肢_250k);

			// 8x 肥胖评估 [身体情况] 体重 写「√」根据上下界判断
            // 81 体重
            Position bcp = mCd.肥胖评估_体重_正常;
            switch (bc.体重.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = mCd.肥胖评估_体重_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = mCd.肥胖评估_体重_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = mCd.肥胖评估_体重_过量;
					break;
			}
//			textPaint.setTextSize(20); 不再加大字体了
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 82.肥胖评估 脂肪量
			switch (bc.体脂肪量.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = mCd.肥胖评估_脂肪量_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = mCd.肥胖评估_脂肪量_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = mCd.肥胖评估_脂肪量_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 83.肥胖评估 肌肉量
			switch (bc.肌肉量.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = mCd.肥胖评估_肌肉量_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = mCd.肥胖评估_肌肉量_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = mCd.肥胖评估_肌肉量_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 9.营养评估 写「√」
            // 91. 蛋白质
			switch (bc.蛋白质.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = mCd.营养评估_蛋白质_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = mCd.营养评估_蛋白质_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = mCd.营养评估_蛋白质_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 92. 无机盐
			switch (bc.无机盐.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = mCd.营养评估_无机盐_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = mCd.营养评估_无机盐_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = mCd.营养评估_无机盐_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 93.基础代谢量 okay
            textPaint.setTextSize(TEXT_SIZE_DEF);
			tmpStr = String.format(FLOAT_0_FORMAT, bc.基础代谢.getCur());
			bcp = mCd.基础代谢量;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 94.总能量消耗　okay
			tmpStr = String.format(FLOAT_0_FORMAT, bc.总能耗.getCur());
			bcp = mCd.总能量消耗;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 95.身体年龄 okay
			tmpStr = String.format(FLOAT_0_FORMAT, bc.身体年龄.getCur());
			bcp = mCd.身体年龄;
            textPaint.setTextSize(TEXT_SIZE_身体年龄);
			drawLineText(tmpStr, textPaint, canvas, bcp);
            textPaint.setTextSize(defPaint.getTextSize());

            // 10x.体型分析
            drawShapeAnalysis(bc, textPaint, canvas);

			// 11x.健康评估 okay
//            if (mContext.getResources().getBoolean(R.bool.is_print_total_score)) {
                textPaint.setTextSize(TEXT_SIZE_健康评估);
                tmpStr = String.format(FLOAT_1_FORMAT, bc.评分.getCur());
				drawLineText(tmpStr, textPaint, canvas, mCd.健康评估);
//            }

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

	/**
	 * 水肿分析
	 * @param bc
	 * @param textPaint
	 * @param canvas
	 */
	private void drawEdema(BodyComposition bc, TextPaint textPaint, Canvas canvas) {
        Position bcp = mCd.水肿分析_细胞外液_正常;
		switch (bc.细胞外液含量.getLevel()) {
			case BodyComposition.LEVEL_LOW:
				bcp = mCd.水肿分析_细胞外液_干燥;
				break;
			case BodyComposition.LEVEL_NORMAL:
				bcp = mCd.水肿分析_细胞外液_正常;
				break;
			case BodyComposition.LEVEL_HIGH:
				bcp = mCd.水肿分析_细胞外液_浮肿;
				break;
		}
		textPaint.setTextSize(TEXT_SIZE_DEF);
		drawLineText(TEXT_TICK, textPaint, canvas, bcp);

		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(
				String.format(FLOAT_2_FORMAT, bc.水肿系数.getCur()),
				mCd.水肿分析_水肿系数.getXMils() / 1000,
				mCd.水肿分析_水肿系数.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_2_FORMAT, bc.身体水分率.getCur()),
				mCd.水肿分析_身体水分率.getXMils() / 1000,
				mCd.水肿分析_身体水分率.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_1_FORMAT, bc.细胞内液含量.getCur()),
				mCd.水肿分析_细胞内液.getXMils() / 1000,
				mCd.水肿分析_细胞内液.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_1_FORMAT, bc.细胞外液含量.getCur()),
				mCd.水肿分析_细胞外液.getXMils() / 1000,
				mCd.水肿分析_细胞外液.getYMils() / 1000,
				textPaint);
	}

	/**
     * 绘制[体成分分析]
     * 需要分三段，第一段和第三段一种颜色，第二段另外一种颜色
     */
	public void drawProgress(Canvas canvas, Position bp, float xPos, Paint paint) {
        int oldColor = Color.BLACK;
        // 得出每一段长度
        final float RANGE_1_START = bp.getXMils() / 1000;
        final float RANGE_2_START = bp.getXMils() / 1000 + Coordinate.体成分分析_SECOND_START_PX / 1000;
        final float RANGE_3_START = bp.getXMils() / 1000 + Coordinate.体成分分析_THIRD_START_PX / 1000;

        // 死值
        final float RANGE_1_LENGTH = Coordinate.体成分分析_SECOND_START_PX / 1000;
        final float RANGE_2_LENGTH = Coordinate.体成分分析_THIRD_START_PX / 1000;
        final float RANGE_3_LENGTH = Coordinate.体成分分析_TOTAL_LENGTH_PX / 1000;

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

	/** 对一些参数进行补救 */
	/* 1. 添加 */
	private static final int 体型分析_X_MIN = 0;
	private static final int 体型分析_Y_MIN = 0;
	private static final int 体型分析_X_MAX = 3;
	private static final int 体型分析_Y_MAX = 4;

    /**
     * 10x
     * 绘制 体型分析
     */
	private void drawShapeAnalysis(BodyComposition bc,
                                  TextPaint textPaint,
                                  Canvas canvas) {
        // bmi结果
        double xPos = 0;
        double yPos = 0;
        // 比较是否出界线，如果出最小值，则迂回进来
        if ((int)BodyComposition.BMI.getOriginCur() > BodyComposition.BMI_MIN) {
            xPos = ((int)BodyComposition.BMI.getOriginCur() - BodyComposition.BMI_MIN) / BodyComposition.BMI_RECT_WIDTH;
        } else
            xPos = 0;

        if ((int)BodyComposition.脂肪率.getOriginCur() < BodyComposition.BFR_MIN) {
            yPos = 0;
        }
        // MALE
        if ((int)BodyComposition.性别.getOriginCur() == BodyComposition.MALE) {
            yPos = ((int)BodyComposition.脂肪率.getOriginCur() - BodyComposition.BMI_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            if ((int)BodyComposition.脂肪率.getOriginCur() > (int)BodyComposition.脂肪率.getOriginMax()) //测试值超过标准
            {
                yPos = (((int)BodyComposition.脂肪率.getOriginCur() - (int)BodyComposition.脂肪率.getOriginMax()) / 2 + (int)BodyComposition.脂肪率.getOriginMax() - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            } else {
                yPos = ((int)BodyComposition.脂肪率.getOriginCur() - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_MALE;
            }
        } else if ((int)BodyComposition.性别.getOriginCur() == BodyComposition.FEMALE) { // FEMALE
            yPos = ((int)BodyComposition.脂肪率.getOriginCur() - BodyComposition.BFR_MIN) / BodyComposition.BFR_RECT_WIDTH_FEMALE;
        }

		Log.i(LOG_TAG, "before xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");
		// 设置最小值
		xPos = xPos < 体型分析_X_MIN ? 体型分析_X_MIN : xPos;
		yPos = yPos < 体型分析_Y_MIN ? 体型分析_Y_MIN : yPos;

		// 设置最大值
		xPos = xPos > 体型分析_X_MAX ? 体型分析_X_MAX : xPos;
		yPos = yPos > 体型分析_Y_MAX ? 体型分析_Y_MAX : yPos;

		// 将x y向下取整数
		xPos = (int)xPos;
		yPos = (int)yPos;

		Log.i(LOG_TAG, "after  xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");
		String xPosStr = String.format(FLOAT_1_FORMAT, xPos);
		String yPosStr = String.format(FLOAT_1_FORMAT, yPos);

		// xPos yPos 只找小方格原点，不找对应的中心了
        xPos = Coordinate.ORIGIN_X + xPos * Coordinate.SINGLE_RECT_WIDTH; // + mCd.SINGLE_RECT_WIDTH / 2;
		// yPos + 1 : 是为了找出该当前表格的左上角，因为绘图是从左上角开始的
        yPos = Coordinate.ORIGIN_Y - (yPos + 1) * Coordinate.SINGLE_RECT_HEIGHT; // - mCd.SINGLE_RECT_HEIGHT / 2;
		Log.i(LOG_TAG, "after2  xPos: " + xPos + " yPos: " + yPos);

        /*
         * 坐标由iso mm转换为英寸point
         */
		textPaint.setTextSize(TEXT_SIZE_体型分析);
        //canvas.drawText(TEXT_体型分析, (float)((xPos - TEXT_SIZE_体型分析/2) * VALUE_72_X_1MM) / 1000, (float)(yPos * VALUE_72_X_1MM + TEXT_SIZE_体型分析 / 2) / 1000, textPaint);
		//textPaint.setTextSize(8);
		//canvas.drawText("xPos: " + xPosStr + " yPos: " + yPosStr , (float)((xPos - 10) * 2836 - 10) / 1000, (float)((yPos + 10) * 2836 + 10) / 1000, textPaint);
		//textPaint.setTextSize(TEXT_SIZE_体型分析);

		textPaint.setTextSize(8);
		canvas.drawText(
				".",
				(float) (xPos * VALUE_72_X_1MM / 1000),
				(float) (yPos * VALUE_72_X_1MM / 1000),
				textPaint);


		textPaint.setTextSize(8);
		canvas.drawText(
				".",
				(float) (Coordinate.ORIGIN_X * VALUE_72_X_1MM / 1000),
				(float) (Coordinate.ORIGIN_Y * VALUE_72_X_1MM / 1000),
				textPaint);

		textPaint.setTextSize(TEXT_SIZE_体型分析);
		Log.i(TAG, "Coordinate.SINGLE_RECT_HEIGHT: " + Coordinate.SINGLE_RECT_HEIGHT);
		// yPos + 2: 属于微调，不微调会偏上
		Position p = new Position((int)(xPos * VALUE_72_X_1MM), (int)((yPos + 2) * VALUE_72_X_1MM), (int)(Coordinate.SINGLE_RECT_WIDTH * VALUE_72_X_1MM), (int)(Coordinate.SINGLE_RECT_HEIGHT * VALUE_72_X_1MM));
		drawMutilLineText(TEXT_体型分析, textPaint, canvas, p, Layout.Alignment.ALIGN_CENTER);
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
								   Position pos) {
		canvas.drawText(
				tmpStr,
				pos.getXMils() / 1000,
				pos.getYMils() / 1000,
				textPaint);
	}

	/**
	 *
	 * @param tmpStr 文本
	 * @param textPaint 文字画笔
	 * @param canvas 画布
	 * @param pos 位置对象
	 */
	private void drawLineTextUint(String tmpStr,
							  TextPaint textPaint,
							  Canvas canvas,
							  Position pos) {
		canvas.drawText(
				tmpStr,
				pos.getXMils() / 1000,
				pos.getYMils() / 1000 + 10,
				textPaint);
	}

	/**
	 * @param tmpStr 文本
	 * @param textPaint 文字画笔
	 * @param canvas 画布
	 * @param pos 位置对象
	 */
	private void drawMutilLineText(String tmpStr,
								   TextPaint textPaint,
								   Canvas canvas,
								   Position pos,
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
	 * 暂时不再使用
     */
    public float getProgressLength(int item, BodyComposition bc) {
    	return 0;
//		float rate = 1;
//		boolean Flag = false;
//		float[] P_temp = new float[2];
//		int Data_Test = 0, Data_Min = 0, Data_Max = 0;
//		switch (item) {
//			case 项目_体重: // 体重
//				Data_Test = bc.体重_CUR;
//				Data_Min = bc.体重_MIN;
//				Data_Max = bc.体重_MAX;
//				Flag = false;
//				break;
//			case 项目_身体质量: // BMI
//				Data_Test = bc.身体质量_CUR;
//				Data_Min = bc.身体质量_MIN;
//				Data_Max =  bc.身体质量_MAX;
//				Flag = false;
//				break;
//            case 项目_体脂肪率: // 体脂肪率
//                Data_Test = bc.脂肪率_CUR;
//                Data_Min = bc.脂肪率_MIN;
//                Data_Max = bc.脂肪率_MAX;
//                if (bc.SEX == BodyComposition.MALE) //男
//                    Flag = true;
//                else if (bc.SEX == BodyComposition.FEMALE)
//                    Flag = false;
//                break;
//			case 项目_体脂肪量:// 脂肪量
//				Data_Test = bc.体脂肪量_CUR;
//				Data_Min = bc.体脂肪量_MIN;
//				Data_Max =  bc.体脂肪量_MAX;
//				Flag = false;
//				break;
//            case 项目_肌肉量:
//				Data_Test = bc.肌肉量_CUR;
//                Data_Min = bc.肌肉量_MIN;
//                Data_Max = bc.肌肉量_MAX;
//                Flag = false;
//                break;
//            case 项目_身体水分:
//				Data_Test = bc.身体总水分_CUR;
//                Data_Min = bc.身体总水分_MIN;
//                Data_Max = bc.身体总水分_MAX;
//                Flag = false;
//                break;
//		}
//
//		/**
//		 * 求出最外边界值
//		 * 根据实测来填写比例值，而不是3/2了
//		 */
//		P_temp[0] = Data_Min - ((Data_Max - Data_Min) * 35.5f / 21.5f);     //坐标最小值, 600
//		P_temp[1] = Data_Max + ((Data_Max - Data_Min) * 32.5f / 21.5f);     //坐标最大值,1500
//
//		float range = P_temp[1] - P_temp[0];                      //整体坐标代表的最大数值,900
//		float position = 0;
//		// 如果小于最最小值，设定一默认值5
//		if (Data_Test < P_temp[0]) //低于最小值坐标
//			position = 5;
//		else {
//			if (Flag) { // 脂肪量 脂肪率
//				if (Data_Test > Data_Max)
//					position = ((Data_Test - Data_Max) / 2 + Data_Max) - P_temp[0];
//				else
//					position = Data_Test - P_temp[0];
//			} else // 其它
//				position = Data_Test - P_temp[0];
//
//			if (position > range) // 超出最大坐标，以最大为准
//				position = range;
//		}
//		rate = (float) position / range;
//		return rate * 体成分分析_TOTAL_LENGTH * 2836 / 1000;
	}
}
