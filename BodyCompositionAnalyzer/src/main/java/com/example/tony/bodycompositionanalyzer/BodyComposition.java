package com.example.tony.bodycompositionanalyzer;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by tony on 16-6-22.
 */
public class BodyComposition {
    private static final String LOG_TAG = "BodyComposition";
    private String 姓名;
    private String 身高;
    private String 体重_1;
    private String 性别;
    private String 年龄;
    private String 身体电阻值;

    private short _50k下la电阻值;
    private short _50k下ra电阻值;
    private short _50k下tr电阻值;
    private short _50k下ll电阻值;
    private short _50k下rl电阻值;
    private short _5k下la电阻值;
    private short _5k下ra电阻值;
    private short _5k下tr电阻值;
    private short _5k下ll电阻值;
    private short _5k下rl电阻值;
    private short _250k下la电阻值;
    private short _250k下ra电阻值;
    private short _250k下tr电阻值;
    private short _250k下ll电阻值;
    private short _250k下rl电阻值;

    private String 测试时间;
    private int 体重_2;
    private int 体重标准;
    private int 体重标准值;
    private int 去脂肪体重;
    private int 去脂肪体重标准;
    private int 体脂肪量;
    private int 体脂肪量标准;
    private int 肌肉量;
    private int 肌肉标准;
    private int 躯干脂肪;
    private int 躯干脂肪标准;
    private int 左上肢脂肪量;
    private int 左上肢脂肪标准;
    private int 左下肢脂肪量;
    private int 左下肢脂肪标准;
    private int 右下肢脂肪量;
    private int 右下肢脂肪标准;
    private int 内脏脂肪指数;
    private int 内脏指数正常范围;
    /* 节段分析， */
    private int 躯干肌肉;
    private int 躯干肌肉标准;
    private int 左上肢肌肉含量;
    private int 左上肢肉正常范围;
    private int 右上肢肌肉含量;
    private int 右上肢肌肉含量正常范围;
    private int 左下肢肌肉含量;
    private int 左下肢肌肉正常范围;
    private int 右下肢肌肉含量;
    private int 右下肢肌肉含量正常范围;

    private int 身体总水分;
    private int 身体总水分正常范围;
    private int 蛋白质含量;
    private int 蛋白质正常范围;
    private int 无机盐含量;
    private int 细胞外液含量;
    private int 细胞外液正常范围;
    private int 细胞内液含量;
    private int 细胞内液正常范围;
    private int 身体总评分;
    private int 身体年龄;
    private int 体重调节;
    private int 脂肪调节;
    private int 肌肉调节;
    private int 基础代谢量;
    private int 总能量消耗;

    /* 从机回复语句 */
    public static final byte[] ACK = {(byte)0xCC, 0x00, 0x00, (byte)0xE0, (byte)0xDE, 0x00};
    public static final int ACK_START = 0;
    public static final int ACK_LENGTH = 6;
    public static final int DATA_START = 6;
    public static final int DATA_LENGTH = 217;

    /* 在数据中的位置 注：小端方式 */
    public static final int 姓名_START       = 0;
    public static final int 身高_START       = 2;
    public static final int 体重_START       = 4;
    public static final int 年龄_START       = 6;
    public static final int 性别_START       = 7;
    public static final int 身体电阻值_START  = 8;


    public static final int DATE_START = 40;

    public static final int 姓名_LENGTH = 2;
    public static final int 身高_LENGTH = 2;
    public static final int 体重_LENGTH = 2;
    public static final int 性别_LENGTH = 1;
    public static final int 年龄_LENGTH = 1;

    /* 女 */
    public static final byte FEMALE = 0x00;
    /* 男 */
    public static final byte MALE   = 0x01;

    /**
     * 从数据段进行解析各项参数
     * @param data
     */
    public BodyComposition(final byte[] data) {
        int tmpInt = 0;
        float tmpFloat = 0;
        byte[] two = new byte[2];
        byte[] one = new byte[1];
        // 1. 姓名 编号 3号
        two = new byte[]{0x03, 0x00};
        System.arraycopy(data, 姓名_START, two, 0, two.length);
        tmpInt = ByteBuffer.wrap(two).order(ByteOrder.LITTLE_ENDIAN).getShort();
        姓名 = String.format("%04d", tmpInt);

        // 2. 身高 180.0cm 保留一位小数
        two = new byte[]{0x08, (byte)0x07};
        System.arraycopy(data, 身高_START, two, 0, two.length);
        tmpFloat = (ByteBuffer.wrap(two).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10);
        身高 = String.format("%.1f",tmpFloat);

        // 3. 体重 80.8kg
        two = new byte[]{0x28, 0x03};
        System.arraycopy(data, 体重_START, two, 0, two.length);
        tmpFloat = (ByteBuffer.wrap(two).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10);
        体重_1 = String.format("%.1f",tmpFloat);

        // 4. 性别
        性别 = data[性别_START] == MALE ? "男" : "女";

        // 5. 年龄
        年龄 = String.valueOf(data[年龄_START]);

        // 6. 身体电阻值
        two = new byte[]{(byte)0xF8, 0x18};
        System.arraycopy(data, 身体电阻值_START, two, 0, two.length);
        tmpFloat = (ByteBuffer.wrap(two).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10);
        身体电阻值 = String.format("%.1f",tmpFloat);

        // n. 测试时间(默认：2016-01-24 13:42)
        byte[] date = {0x31, 0x36, 0x30, 0x31, 0x32, 0x34, 0x31, 0x33, 0x34, 0x32};
        System.arraycopy(data, DATE_START, date, 0, 10);
        测试时间 = "20" + new String(new byte[]{date[0],date[1]}) + "-" + /* 年 */
                new String(new byte[]{date[2],date[3]}) + "-" +   /* 月 */
                new String(new byte[]{date[4],date[5]}) + " " +   /* 日 */
                new String(new byte[]{date[6],date[7]}) + ":" +   /* 时 */
                new String(new byte[]{date[8],date[9]});          /* 分 */

        Log.i(LOG_TAG, "测试时间: " + 测试时间);
        Log.i(LOG_TAG, toString());
    }

    @Override
    public String toString() {
        return "BodyComposition{" +
                "\n姓名=" + 姓名 +
                "\n身高=" + 身高 +
                "\n体重_1=" + 体重_1 +
                "\n性别=" + 性别 +
                "\n年龄=" + 年龄 +
                "\n身体电阻值=" + 身体电阻值 +
                "\n_50k下la电阻值=" + _50k下la电阻值 +
                "\n_50k下ra电阻值=" + _50k下ra电阻值 +
                "\n_50k下tr电阻值=" + _50k下tr电阻值 +
                "\n_50k下ll电阻值=" + _50k下ll电阻值 +
                "\n_50k下rl电阻值=" + _50k下rl电阻值 +
                "\n_5k下la电阻值=" + _5k下la电阻值 +
                "\n_5k下ra电阻值=" + _5k下ra电阻值 +
                "\n_5k下tr电阻值=" + _5k下tr电阻值 +
                "\n_5k下ll电阻值=" + _5k下ll电阻值 +
                "\n_5k下rl电阻值=" + _5k下rl电阻值 +
                "\n_250k下la电阻值=" + _250k下la电阻值 +
                "\n_250k下ra电阻值=" + _250k下ra电阻值 +
                "\n_250k下tr电阻值=" + _250k下tr电阻值 +
                "\n_250k下ll电阻值=" + _250k下ll电阻值 +
                "\n_250k下rl电阻值=" + _250k下rl电阻值 +
                "\n测试时间='" + 测试时间 + '\'' +
                "\n体重_2=" + 体重_2 +
                "\n体重标准=" + 体重标准 +
                "\n体重标准值=" + 体重标准值 +
                "\n去脂肪体重=" + 去脂肪体重 +
                "\n去脂肪体重标准=" + 去脂肪体重标准 +
                "\n体脂肪量=" + 体脂肪量 +
                "\n体脂肪量标准=" + 体脂肪量标准 +
                "\n肌肉量=" + 肌肉量 +
                "\n肌肉标准=" + 肌肉标准 +
                "\n躯干脂肪=" + 躯干脂肪 +
                "\n躯干脂肪标准=" + 躯干脂肪标准 +
                "\n左上肢脂肪量=" + 左上肢脂肪量 +
                "\n左上肢脂肪标准=" + 左上肢脂肪标准 +
                "\n左下肢脂肪量=" + 左下肢脂肪量 +
                "\n左下肢脂肪标准=" + 左下肢脂肪标准 +
                "\n右下肢脂肪量=" + 右下肢脂肪量 +
                "\n右下肢脂肪标准=" + 右下肢脂肪标准 +
                "\n内脏脂肪指数=" + 内脏脂肪指数 +
                "\n内脏指数正常范围=" + 内脏指数正常范围 +
                "\n躯干肌肉=" + 躯干肌肉 +
                "\n躯干肌肉标准=" + 躯干肌肉标准 +
                "\n左上肢肌肉含量=" + 左上肢肌肉含量 +
                "\n左上肢肉正常范围=" + 左上肢肉正常范围 +
                "\n右上肢肌肉含量=" + 右上肢肌肉含量 +
                "\n右上肢肌肉含量正常范围=" + 右上肢肌肉含量正常范围 +
                "\n左下肢肌肉含量=" + 左下肢肌肉含量 +
                "\n左下肢肌肉正常范围=" + 左下肢肌肉正常范围 +
                "\n右下肢肌肉含量=" + 右下肢肌肉含量 +
                "\n右下肢肌肉含量正常范围=" + 右下肢肌肉含量正常范围 +
                "\n身体总水分=" + 身体总水分 +
                "\n身体总水分正常范围=" + 身体总水分正常范围 +
                "\n蛋白质含量=" + 蛋白质含量 +
                "\n蛋白质正常范围=" + 蛋白质正常范围 +
                "\n无机盐含量=" + 无机盐含量 +
                "\n细胞外液含量=" + 细胞外液含量 +
                "\n细胞外液正常范围=" + 细胞外液正常范围 +
                "\n细胞内液含量=" + 细胞内液含量 +
                "\n细胞内液正常范围=" + 细胞内液正常范围 +
                "\n身体总评分=" + 身体总评分 +
                "\n身体年龄=" + 身体年龄 +
                "\n体重调节=" + 体重调节 +
                "\n脂肪调节=" + 脂肪调节 +
                "\n肌肉调节=" + 肌肉调节 +
                "\n基础代谢量=" + 基础代谢量 +
                "\n总能量消耗=" + 总能量消耗 +
                '}';
    }
}
