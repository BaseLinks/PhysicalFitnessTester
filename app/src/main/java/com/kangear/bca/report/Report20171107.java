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
import com.kangear.bca.Position;
import com.kangear.bodycompositionanalyzer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 不处理数据，只接收BodyComposition对象
 */
public class Report20171107 {
 	private static final boolean DEBUG = true;
	private static final String LOG_TAG = "Report20171107";
	private static Context mContext =  null;

	private static final int TEXT_SIZE_DEF     = 9;
    private static final int TEXT_SIZE_体成分结果 = 8;
	private static final int TEXT_SIZE_健康评估 = 16;
	private static final int TEXT_SIZE_体型分析 = 20;
	private static final String TEXT_TICK    = "√";
	private static final String TEXT_体型分析 = "✔";

    /**
     * 单例模式: http://coolshell.cn/articles/265.html
     */
    private volatile static Report20171107 singleton = null;

	public static Report20171107 getInstance(Context context)   {
        if (singleton== null)  {
            synchronized (Report20171107.class) {
                if (singleton== null)  {
                    singleton= new Report20171107(context);
                }
            }
        }
        return singleton;
    }

    private Report20171107(Context context) {
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
		float STROCK = 3f;
		textPaint.setTextSize(TEXT_SIZE_DEF); // 还原字体大小
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(STROCK);
		Layout.Alignment mAlignment = Layout.Alignment.ALIGN_CENTER;
		float xPos = p.getXMils() / 1000 +
				getProgressLength3(t.getCur(), t.getMin(), t.getMax());
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
//			if(PreferencesUtils.getBoolean(mContext, Report20171107.KEY_IS_DRAW_NEGATIVE)) {
//				Bitmap bm = getBitmapFromAsset(mContext, "body_composition_negative_20170504.jpg");
//				if(bm != null) {
//					// 将图片拉伸至整个页面
//					canvas.drawBitmap(bm, null, new Rect(
//									0,
//									0,
//									PrintAttributes.MediaSize.ISO_A4.getWidthMils() * 72 / 1000,
//									PrintAttributes.MediaSize.ISO_A4.getHeightMils() * 72 / 1000),
//							null);
//				}
//			}

			textPaint.setTextSize(TEXT_SIZE_DEF);

			// 01 写姓名/编号
			drawSimple(paint, textPaint, canvas, bc.姓名, Position.姓名, FLOAT_0_FORMAT);

			// 02 身高
			drawSimple(paint, textPaint, canvas, bc.身高, Position.身高, FLOAT_0_FORMAT);

			// 03 体重
			drawSimple(paint, textPaint, canvas, bc.体重, Position.体重1, FLOAT_1_FORMAT);

			// 04 测试日期 (无法使用drawSimple)
			tmpStr = String.valueOf(bc.测试时间.getUnit());
			drawLineText(tmpStr, textPaint, canvas, Position.测试日期);

			// 05 年龄
			drawSimple(paint, textPaint, canvas, bc.年龄, Position.年龄, FLOAT_0_FORMAT);

			// 06 性别
			// drawSimple(paint, textPaint, canvas, bc.性别, Position.性别);
			tmpStr = String.valueOf(bc.性别.getUnit());
			drawLineText(tmpStr, textPaint, canvas, Position.性别);

			/* 2x. 体成分结果 */
			// 21 体重2
            textPaint.setTextSize(TEXT_SIZE_体成分结果);
			drawRange(paint, textPaint, canvas, bc.体重, Position.体重2, FLOAT_1_FORMAT, true);

			// 22 去脂肪体重
			drawRange(paint, textPaint, canvas, bc.去脂体重, Position.去脂肪体重, FLOAT_1_FORMAT, true);

			// 23 肌肉量
			drawRange(paint, textPaint, canvas, bc.肌肉量, Position.肌肉量, FLOAT_1_FORMAT, true);

			// 24 身体总水分
			drawRange(paint, textPaint, canvas, bc.身体水分, Position.身体总水分, FLOAT_1_FORMAT, true);

			// 25 细胞内液 okay
			drawRange(paint, textPaint, canvas, bc.细胞内液含量, Position.细胞内液, FLOAT_1_FORMAT, false);

			// 26 细胞外液 okay
			drawRange(paint, textPaint, canvas, bc.细胞外液含量, Position.细胞外液, FLOAT_1_FORMAT, false);

			// 27 蛋白质量 ok
			drawRange(paint, textPaint, canvas, bc.蛋白质, Position.蛋白质量, FLOAT_1_FORMAT, false);

			// 28 无机盐量 ok
			drawRange(paint, textPaint, canvas, bc.无机盐, Position.无机盐量, FLOAT_1_FORMAT, false);

			// 29 体脂肪量 ok
			drawRange(paint, textPaint, canvas, bc.体脂肪量, Position.体脂肪量, FLOAT_1_FORMAT, false);

			/* 3x. 体成分分析　*/
            /* 31. 体重 */
			drawJindutiao(paint, textPaint, canvas, bc.体重, Position.体成分分析_体重, defPaint);

            /* 32. 身体质量(BMI) */
			drawJindutiao(paint, textPaint, canvas, bc.BMI, Position.体成分分析_身体质量, defPaint);

            /* 33. 体脂肪率 */
			drawJindutiao(paint, textPaint, canvas, bc.脂肪率, Position.体成分分析_体脂肪率, defPaint);

            /* 34. 体脂肪量 */
			drawJindutiao(paint, textPaint, canvas, bc.体脂肪量, Position.体成分分析_体脂肪量, defPaint);

            /* 35. 肌肉量 */
			drawJindutiao(paint, textPaint, canvas, bc.肌肉量, Position.体成分分析_肌肉量, defPaint);

            /* 36. 身体水分 */
			drawJindutiao(paint, textPaint, canvas, bc.身体水分, Position.体成分分析_身体水分, defPaint);

            /* 37. 内脏脂肪 */
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3f);
            float xPos = Position.体成分分析_内脏脂肪.getXMils() / 1000 + getProgressLength2(bc);
            canvas.drawLine(
                    Position.体成分分析_内脏脂肪.getXMils() / 1000,
                    Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    xPos,
                    Position.体成分分析_内脏脂肪.getYMils() / 1000,
                    paint);
            canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.内脏脂肪指数.getCur()),
					xPos,
					Position.体成分分析_内脏脂肪.getYMils() / 1000 + paint.getTextSize() / 2 - 2,
					paint);
            // 还原
            paint.setStrokeWidth(defPaint.getStrokeWidth());

			/* 4X. 调节目标 */
			// 41 体重_标准 okay 注：根据当前值和调节量倒倒推
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(String.format(FLOAT_1_FORMAT, bc.体重.getCur() + bc.体重调节.getCur()),
					Position.体重_标准.getXMils() / 1000,
					Position.体重_标准.getYMils() / 1000,
					paint);
			// 42 体重_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体重.getCur()),
					Position.体重_当前.getXMils() / 1000,
					Position.体重_当前.getYMils() / 1000,
					paint);
			// 43 体重_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			float tmpFloat = bc.体重调节.getCur();
			canvas.drawText(
                    String.format(((tmpFloat < 0) ? "" : "+") + FLOAT_1_FORMAT, tmpFloat),
					Position.体重_调节量.getXMils() / 1000,
					Position.体重_调节量.getYMils() / 1000,
					paint);
			// 44 身体脂肪量_标准 okay 注：身体脂肪量标准：取体脂肪标准值下界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体脂肪量.getMin()),
					Position.身体脂肪量_标准.getXMils() / 1000,
					Position.身体脂肪量_标准.getYMils() / 1000,
					paint);
			// 45 身体脂肪量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.体脂肪量.getCur()),
					Position.身体脂肪量_当前.getXMils() / 1000,
					Position.身体脂肪量_当前.getYMils() / 1000,
					paint);
			// 46 身体脂肪量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = bc.脂肪调节.getCur();
			canvas.drawText(
					String.format(((tmpFloat < 0) ? "" : "+") + FLOAT_1_FORMAT, tmpFloat),
					Position.身体脂肪量_调节量.getXMils() / 1000,
					Position.身体脂肪量_调节量.getYMils() / 1000,
					paint);

			// 47 肌肉量_标准 okay 肌肉量标准：取标准值上界
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.肌肉量.getMax()),
					Position.肌肉量_标准.getXMils() / 1000,
					Position.肌肉量_标准.getYMils() / 1000,
					paint);
			// 48 肌肉量_当前 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.肌肉量.getCur()),
					Position.肌肉量_当前.getXMils() / 1000,
					Position.肌肉量_当前.getYMils() / 1000,
					paint);
			// 49 肌肉量_调节量 okay
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			tmpFloat = bc.肌肉调节.getCur();
			canvas.drawText(
					String.format(FLOAT_1_FORMAT, bc.肌肉量.getMax()),
					Position.肌肉量_调节量.getXMils() / 1000,
					Position.肌肉量_调节量.getYMils() / 1000,
					paint);

			/* 5x 节段肌肉　镜像 */
			// 51 左上肢肌肉量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左上肢肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左上肢肌肉含量);

			// 52 左下肢肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左下肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左下肢肌肉含量);

			// 53 右上肢肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右上肢肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右上肢肌肉含量);

			// 54 右下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右下肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右下肢肌肉含量);

			// 55 躯干肌肉含量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.躯干肌肉量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.躯干肌肉含量);

			/* 6x 节段脂肪 */
			// 61 左上肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左上脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左上肢脂肪量);

			// 62 左下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.左下脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左下肢脂肪量);

			// 63 右上肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右上脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右上肢脂肪量);

			// 64 右下肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.右下脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右下肢脂肪量);

			// 65 躯干肢脂肪量 okay
			tmpStr = String.format(FLOAT_2_FORMAT, bc.躯干脂肪量.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.躯干肢脂肪量);

			/* 7x 节段电阻抗 */
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			// 71 频率 okay
			tmpStr = "5k";
			drawLineText(tmpStr, textPaint, canvas, Position.频率_5k);

			tmpStr = "50k";
			drawLineText(tmpStr, textPaint, canvas, Position.频率_50k);

			tmpStr = "250k";
			drawLineText(tmpStr, textPaint, canvas, Position.频率_250k);

			// 72 右上肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右上肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右上肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻RA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右上肢_250k);

			// 73 左上肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左上肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左上肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻LA.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左上肢_250k);


			// 74 躯干 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.躯干_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.躯干_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻TR.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.躯干_250k);

			// 75 右下肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻RL.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右下肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻RL.getCur());
			drawLineText( tmpStr, textPaint, canvas, Position.右下肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻RL.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.右下肢_250k);

			// 76 左下肢 okay
			tmpStr = String.format(FLOAT_1_FORMAT, bc._5k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左下肢_5k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._50k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左下肢_50k);

			tmpStr = String.format(FLOAT_1_FORMAT, bc._250k电阻LL.getCur());
			drawLineText(tmpStr, textPaint, canvas, Position.左下肢_250k);

			// 8x 肥胖评估 体重 写「√」根据上下界判断
            // 81 体重
            Position bcp = Position.肥胖评估_体重_正常;
            switch (bc.体重.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = Position.肥胖评估_体重_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = Position.肥胖评估_体重_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = Position.肥胖评估_体重_过量;
					break;
			}
//			textPaint.setTextSize(20); 不再加大字体了
			mAlignment = Layout.Alignment.ALIGN_NORMAL;
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 82.肥胖评估 脂肪量
			switch (bc.体脂肪量.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = Position.肥胖评估_脂肪量_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = Position.肥胖评估_脂肪量_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = Position.肥胖评估_脂肪量_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 83.肥胖评估 肌肉量
			switch (bc.肌肉量.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = Position.肥胖评估_肌肉量_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = Position.肥胖评估_肌肉量_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = Position.肥胖评估_肌肉量_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 9.营养评估 写「√」
            // 91. 蛋白质
			switch (bc.蛋白质.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = Position.营养评估_蛋白质_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = Position.营养评估_蛋白质_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = Position.营养评估_蛋白质_过量;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

            // 92. 无机盐
			switch (bc.无机盐.getLevel()) {
				case BodyComposition.LEVEL_LOW:
					bcp = Position.营养评估_无机盐_不足;
					break;
				case BodyComposition.LEVEL_NORMAL:
					bcp = Position.营养评估_无机盐_正常;
					break;
				case BodyComposition.LEVEL_HIGH:
					bcp = Position.营养评估_无机盐_正常;
					break;
			}
			drawLineText(TEXT_TICK, textPaint, canvas, bcp);

			// 93.基础代谢量 okay
            textPaint.setTextSize(TEXT_SIZE_DEF);
			tmpStr = String.format(FLOAT_0_FORMAT, bc.基础代谢.getCur());
			bcp = Position.基础代谢量;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 94.总能量消耗　okay
			tmpStr = String.format(FLOAT_0_FORMAT, bc.总能耗.getCur());
			bcp = Position.总能量消耗;
			drawLineText(tmpStr, textPaint, canvas, bcp);

			// 95.身体年龄 okay
			tmpStr = String.format(FLOAT_0_FORMAT, bc.身体年龄.getCur());
			bcp = Position.身体年龄;
			drawLineText(tmpStr, textPaint, canvas, bcp);

            // 10x.体型分析
            drawShapeAnalysis(bc, textPaint, canvas);

			// 11x.健康评估 okay
//            if (mContext.getResources().getBoolean(R.bool.is_print_total_score)) {
                textPaint.setTextSize(TEXT_SIZE_健康评估);
                tmpStr = String.format(FLOAT_1_FORMAT, bc.评分.getCur());
				drawLineText(tmpStr, textPaint, canvas, Position.健康评估);
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
        Position bcp = Position.水肿分析_细胞外液_正常;
		switch (bc.细胞外液含量.getLevel()) {
			case BodyComposition.LEVEL_LOW:
				bcp = Position.水肿分析_细胞外液_干燥;
				break;
			case BodyComposition.LEVEL_NORMAL:
				bcp = Position.水肿分析_细胞外液_正常;
				break;
			case BodyComposition.LEVEL_HIGH:
				bcp = Position.水肿分析_细胞外液_浮肿;
				break;
		}
		textPaint.setTextSize(TEXT_SIZE_DEF);
		drawLineText(TEXT_TICK, textPaint, canvas, bcp);

		textPaint.setColor(Color.BLACK);
		textPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(
				String.format(FLOAT_2_FORMAT, bc.水肿系数.getCur()),
				Position.水肿分析_水肿系数.getXMils() / 1000,
				Position.水肿分析_水肿系数.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_2_FORMAT, bc.身体水分率.getCur()),
				Position.水肿分析_身体水分率.getXMils() / 1000,
				Position.水肿分析_身体水分率.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_1_FORMAT, bc.细胞内液含量.getCur()),
				Position.水肿分析_细胞内液.getXMils() / 1000,
				Position.水肿分析_细胞内液.getYMils() / 1000,
				textPaint);

		canvas.drawText(
				String.format(FLOAT_1_FORMAT, bc.细胞外液含量.getCur()),
				Position.水肿分析_细胞外液.getXMils() / 1000,
				Position.水肿分析_细胞外液.getYMils() / 1000,
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

	/**  10x.体型分析 */
	/** BMI下限 */
	public static final short BMI_MIN = 154;
	/** BMI上限 */
	public static final short BMI_MAX = 330;
	/** BMI范围宽度 */
	public static final short BMI_RANGE = BMI_MAX - BMI_MIN; // 176
	/** 方块个数 */
	public static final short BMI_RECT_NUM = 4;
	/** 方块宽度 */
	public static final short BMI_RECT_WIDTH = BMI_RANGE / BMI_RECT_NUM; //44

	/** 脂肪率结果 */
	/** 脂肪率下限 数据哪里来的呢？ */
	public static final short BFR_MIN = 100;
	/** 男性脂肪率上限 */
	public static final short BFR_MAX_MALE = 350;
	/** 女性脂肪率上限 */
	public static final short BFR_MAX_FEMALE = 600;
	/** 男性脂肪率范围宽度 */
	public static final short BFR_RANGE_MALE = BFR_MAX_MALE - BFR_MIN; // 150
	/** 女性脂肪率范围宽度 */
	public static final short BFR_RANGE_FEMALE = BFR_MAX_FEMALE - BFR_MIN; // 500
	/** 方块个数 */
	public static final short BFR_RECT_NUM = 5;
	/** 方块宽度 */
	public static final short BFR_RECT_WIDTH_MALE = BFR_RANGE_MALE / BFR_RECT_NUM; //50
	/** 方块宽度 */
	public static final short BFR_RECT_WIDTH_FEMALE = BFR_RANGE_FEMALE / BFR_RECT_NUM; //100

	/** 在A4纸上方块宽度 重新排版时要改这里 */
	public static final double SINGLE_RECT_WIDTH = 66/4; //16.8;
	/** 在A4纸上方块高度 */
	public static final double SINGLE_RECT_HEIGHT = 70/5; //13.4;
	/** 原点坐标 下面的交叉点为「体型分析」原点 */
	/**
	 * |
	 * |
	 * |_________
	 */
	public static final double ORIGIN_X = 132 ;// 132 - 3; //133;
	public static final double ORIGIN_Y = 246 ;//138.5 + 2; //146;

	/** 对一些参数进行补救 */
	/* 1. 添加 */
	public static final int 体型分析_X_MIN = 0;
	public static final int 体型分析_Y_MIN = 0;
	public static final int 体型分析_X_MAX = 3;
	public static final int 体型分析_Y_MAX = 4;

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
        if (bc.BMI.getCur() > BMI_MIN) {
            xPos = (bc.BMI.getCur() - BMI_MIN) / BMI_RECT_WIDTH;
        } else
            xPos = 0;

        if (bc.脂肪率.getCur() < BFR_MIN) {
            yPos = 0;
        }
        // MALE
        if (bc.性别.getUnit().equals("男")) {
            yPos = (bc.脂肪率.getCur() - BMI_MIN) / BFR_RECT_WIDTH_MALE;
            if (bc.脂肪率.getCur() > bc.脂肪率.getMax()) //测试值超过标准
            {
                yPos = ((bc.脂肪率.getCur() - bc.脂肪率.getMax()) / 2 + bc.脂肪率.getMax() - BFR_MIN) / BFR_RECT_WIDTH_MALE;
            } else {
                yPos = (bc.脂肪率.getCur() - BFR_MIN) / BFR_RECT_WIDTH_MALE;
            }
        } else if (bc.性别.getUnit().equals("女")) { // FEMALE
            yPos = (bc.脂肪率.getCur() - BFR_MIN) / BFR_RECT_WIDTH_FEMALE;
        }

		Log.i(LOG_TAG, "before xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");
		// 设置最小值
		xPos = xPos < 体型分析_X_MIN ? 体型分析_X_MIN : xPos;
		yPos = yPos < 体型分析_Y_MIN ? 体型分析_Y_MIN : yPos;

		// 设置最大值
		xPos = xPos > 体型分析_X_MAX ? 体型分析_X_MAX : xPos;
		yPos = yPos > 体型分析_Y_MAX ? 体型分析_Y_MAX : yPos;
		Log.i(LOG_TAG, "after  xPos: " + xPos + " yPos: " + yPos + " xPos(0~3), yPos(0~4)");

        xPos = ORIGIN_X + xPos * SINGLE_RECT_WIDTH + SINGLE_RECT_WIDTH / 2;
        yPos = ORIGIN_Y - yPos * SINGLE_RECT_HEIGHT - SINGLE_RECT_HEIGHT / 2;

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
								   Position pos) {
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

	public static final int 体成分分析_TOTAL_LENGTH = 89;
	public static final int 内脏指数_TOTAL_LENGTH = 66; //89;
    public static final float 体成分分析_SECOND_START_MM = 27f;
    public static final float 体成分分析_SECOND_START_PX = 体成分分析_SECOND_START_MM * Position.VALUE_72_X_1MM;
    public static final float 体成分分析_THIRD_START_MM = 49f;
    public static final float 体成分分析_THIRD_START_PX = 体成分分析_THIRD_START_MM * Position.VALUE_72_X_1MM;
    public static final float 体成分分析_TOTAL_LENGTH_PX = 体成分分析_TOTAL_LENGTH * Position.VALUE_72_X_1MM;

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


	/**
	 * 体成分分析　内脏脂肪　这个是一个比较复杂的计算方式
	 * 由于在A4纸上的表格并没有按照比例进行划分，所以需要进行分段以及微调才能达到效果
	 * @param bc BodyComposition
	 * @return 进度条实际长度，单位Point
	 */
	public float getProgressLength2(BodyComposition bc) {
        if (true) // 新版本已经符合比例了，不需要复杂的分段计算了
            return (float) (65.5 / 17 * bc.内脏脂肪指数.getCur() * 2836 / 1000);
//		float[] P_temp = new float[2];
//		float cur = 0, min = 0, max = 0;
//		cur = bc.内脏脂肪_CUR / 10f; // 10
//		min = bc.内脏脂肪_MIN / 10f; // 最小1
//		max = bc.内脏脂肪_MAX / 10f; // 最大17
//		final float NORMAL_START = 0f;
//		final float TOO_HIGH_START = 10f;
//		final float HIGH_START = 14f;
//		final float HIGH_END   = 17f;
//
//		final float NORMAL_START_MM = 0f;
//		final float TOO_HIGH_START_MM = 39.5f; //38.3f;
//		final float HIGH_START_MM = 34.5f; // 53.5f;
//
//		final float NORMAL_LENGTH_MM = 39.5f;
//		final float TOO_HIGH_LENGTH_MM = 15.8f;
//		final float HIGH_LENGTH_MM = 7; //36f;
//		final float TOTAL_LENGTH_MM = 内脏指数_TOTAL_LENGTH;
//
//		float base = 0f;
//		float r = 0.1f; // 相对长度单位mm
//		if(cur >= min && cur < TOO_HIGH_START) { // 正常范围内 normal
//			base = NORMAL_START_MM;
//			r = NORMAL_LENGTH_MM / (TOO_HIGH_START - NORMAL_START) * (cur - NORMAL_START) + base;
//		} else if (cur >= TOO_HIGH_START && cur < HIGH_START) {  // 过高 too high
//			base = TOO_HIGH_START_MM;
//			r = TOO_HIGH_LENGTH_MM / (HIGH_START - TOO_HIGH_START)  * (cur - TOO_HIGH_START) + base;
//		} else if (cur >= HIGH_START && cur < HIGH_END) { // 高 high
//			base = HIGH_START_MM;
//			r = HIGH_LENGTH_MM / (HIGH_END - HIGH_START) *  (cur - HIGH_START) + base;
//		} else if (cur >= HIGH_END) {
//			r = TOTAL_LENGTH_MM;
//		}
//
//		Log.i(LOG_TAG, "r: " + r);

//		return r * 2836 / 1000;
		return 0;
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

		return r * Position.VALUE_72_X_1MM / 1000;
	}
}
