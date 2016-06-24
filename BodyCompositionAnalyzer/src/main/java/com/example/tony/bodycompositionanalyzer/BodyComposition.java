package com.example.tony.bodycompositionanalyzer;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by tony on 16-6-22.
 */
public class BodyComposition {
    private static final String LOG_TAG = "BodyComposition";
    public final String 姓名;
    public final String 身高;
    public final String 体重1;
    public final String 性别;
    public final String 年龄;
    public final String 身体电阻值;

    public final String _50k下la电阻值;
    public final String _50k下ra电阻值;
    public final String _50k下tr电阻值;
    public final String _50k下ll电阻值;
    public final String _50k下rl电阻值;
    public final String _5k下la电阻值;
    public final String _5k下ra电阻值;
    public final String _5k下tr电阻值;
    public final String _5k下ll电阻值;
    public final String _5k下rl电阻值;
    public final String _250k下la电阻值;
    public final String _250k下ra电阻值;
    public final String _250k下tr电阻值;
    public final String _250k下ll电阻值;
    public final String _250k下rl电阻值;

    public final String 测试日期;
    public final String 体重2;
    public final String 体重标准范围;
    public final String 体重标准值;
    public final String 去脂肪体重;
    public final String 去脂肪体重标准;
    public final String 体脂肪量;
    public final String 体脂肪量标准;
    public final String 肌肉量;
    public final String 肌肉标准;
    public final String 躯干脂肪;
    public final String 躯干脂肪标准;
    public final String 左上肢脂肪量;
    public final String 左上肢脂肪标准;
    public final String 右上肢脂肪量;
    public final String 右上肢脂肪标准;
    public final String 左下肢脂肪量;
    public final String 左下肢脂肪标准;
    public final String 右下肢脂肪量;
    public final String 右下肢脂肪标准;
    public final String 内脏脂肪指数;
    public final String 内脏指数正常范围;
    /* 节段分析， */
    public final String 躯干肌肉含量;
    public final String 躯干肌肉标准;
    public final String 左上肢肌肉含量;
    public final String 左上肢肉正常范围;
    public final String 右上肢肌肉含量;
    public final String 右上肢肌肉含量正常范围;
    public final String 左下肢肌肉含量;
    public final String 左下肢肌肉正常范围;
    public final String 右下肢肌肉含量;
    public final String 右下肢肌肉含量正常范围;

    public final String 身体总水分;
    public final String 身体总水分正常范围;
    public final String 蛋白质含量;
    public final String 蛋白质正常范围;
    public final String 无机盐含量;
    public final String 无机盐含量正常范围;
    public final String 细胞外液含量;
    public final String 细胞外液正常范围;
    public final String 细胞内液含量;
    public final String 细胞内液正常范围;
    // ...
    public final String BMI结果;
    public final String 脂肪率;

    // ...
    public final String 身体总评分;
    public final String 身体年龄;
    public final String 体重调节;
    public final String 脂肪调节;
    public final String 肌肉调节;
    public final String 基础代谢量;
    public final String 总能量消耗;

    /* 从机回复语句 */
    public static final byte[] ACK = {(byte)0xCC, 0x00, 0x00, (byte)0xE0, (byte)0xDE, 0x00};
    public static final int ACK_START = 0;
    public static final int ACK_LENGTH = 6;
    public static final int DATA_START = 6;
    public static final int DATA_LENGTH = 217;

    /* 在数据中的位置 注：小端方式 */
    public static final int 姓名_START             = 0;
    public static final int 身高_START             = 2;
    public static final int 体重1_START             = 4;
    public static final int 年龄_START             = 6;
    public static final int 性别_START             = 7;
    public static final int 身体电阻值_START       = 8;
    public static final int _50k下la电阻值_START   = 10;
    public static final int _50k下ra电阻值_START   = 12;
    public static final int _50k下tr电阻值_START   = 14;
    public static final int _50k下ll电阻值_START   = 16;
    public static final int _50k下rl电阻值_START   = 18;
    public static final int _5k下la电阻值_START    = 20;
    public static final int _5k下ra电阻值_START    = 22;
    public static final int _5k下tr电阻值_START    = 24;
    public static final int _5k下ll电阻值_START    = 26;
    public static final int _5k下rl电阻值_START    = 28;
    public static final int _250k下la电阻值_START  = 30;
    public static final int _250k下ra电阻值_START  = 32;
    public static final int _250k下tr电阻值_START  = 34;
    public static final int _250k下ll电阻值_START  = 36;
    public static final int _250k下rl电阻值_START  = 38;

    public static final int DATE_START             = 40;

    public static final int 体重2_START           = 50;
    public static final int 体重标准范围_START     = 52;
    public static final int 体重标准值_START       = 56;
    public static final int 去脂肪体重_START       = 58;
    public static final int 去脂肪体重标准_START   = 60;
    public static final int 体脂肪量_START         = 64;
    public static final int 体脂肪量标准_START     = 66;
    public static final int 肌肉量_START           = 70;
    public static final int 肌肉标准_START         = 72;
    public static final int 躯干脂肪_START         = 76;
    public static final int 躯干脂肪标准_START      = 78;
    public static final int 左上肢脂肪量_START      = 82;
    public static final int 左上肢脂肪标准_START    = 84;
    public static final int 右上肢脂肪量_START      = 88;
    public static final int 右上肢脂肪标准_START    = 90;
    public static final int 左下肢脂肪量_START      = 94;
    public static final int 左下肢脂肪标准_START    = 96;
    public static final int 右下肢脂肪量_START      = 100;
    public static final int 右下肢脂肪标准_START    = 102;
    public static final int 内脏脂肪指数_START      = 106;
    public static final int 内脏指数正常范围_START  = 108;
    /* 节段分析， */
    public static final int 躯干肌肉_START               = 112;
    public static final int 躯干肌肉标准_START           = 114;
    public static final int 左上肢肌肉含量_START         = 118;
    public static final int 左上肢肉正常范围_START       = 120;
    public static final int 右上肢肌肉含量_START         = 124;
    public static final int 右上肢肌肉含量正常范围_START  = 126;
    public static final int 左下肢肌肉含量_START         = 130;
    public static final int 左下肢肌肉正常范围_START      = 132;
    public static final int 右下肢肌肉含量_START          = 136;
    public static final int 右下肢肌肉含量正常范围_START  = 138;

    public static final int 身体总水分_START              = 142;
    public static final int 身体总水分正常范围_START       = 144;
    public static final int 蛋白质含量_START              = 148;
    public static final int 蛋白质正常范围_START          = 150;
    public static final int 无机盐含量_START              = 154;
    public static final int 无机盐含量正常范围_START       = 156;
    public static final int 细胞外液含量_START            = 160;
    public static final int 细胞外液正常范围_START        = 162;
    public static final int 细胞内液含量_START            = 166;
    public static final int 细胞内液正常范围_START        = 168;
    // ...
    public static final int BMI结果_START                = 178;
    public static final int 脂肪率_START                 = 184;

    // ...

    public static final int 身体总评分_START    = 198;
    public static final int 身体年龄_START      = 200;
    public static final int 体重调节_START      = 202;
    public static final int 脂肪调节_START      = 206;
    public static final int 肌肉调节_START      = 210;
    public static final int 基础代谢量_START    = 214;
    public static final int 总能量消耗_START    = 216;


    /* 数据长度 */
    public static final int 姓名_LENGTH = 2;
    public static final int 身高_LENGTH = 2;
    public static final int 体重1_LENGTH = 2;
    public static final int 性别_LENGTH = 1;
    public static final int 年龄_LENGTH = 1;
    public static final int 身体电阻值_LENGTH = 2;
    public static final int _50k下la电阻值_LENGTH   = 2;
    public static final int _50k下ra电阻值_LENGTH   = 2;
    public static final int _50k下tr电阻值_LENGTH   = 2;
    public static final int _50k下ll电阻值_LENGTH   = 2;
    public static final int _50k下rl电阻值_LENGTH   = 2;
    public static final int _5k下la电阻值_LENGTH    = 2;
    public static final int _5k下ra电阻值_LENGTH    = 2;
    public static final int _5k下tr电阻值_LENGTH    = 2;
    public static final int _5k下ll电阻值_LENGTH    = 2;
    public static final int _5k下rl电阻值_LENGTH    = 2;
    public static final int _250k下la电阻值_LENGTH  = 2;
    public static final int _250k下ra电阻值_LENGTH  = 2;
    public static final int _250k下tr电阻值_LENGTH  = 2;
    public static final int _250k下ll电阻值_LENGTH  = 2;
    public static final int _250k下rl电阻值_LENGTH  = 2;

    public static final int DATE_LENGTH             = 10;

    public static final int 体重2_LENGTH           = 2;
    public static final int 体重标准范围_LENGTH         = 4;
    public static final int 体重标准值_LENGTH       = 2;
    public static final int 去脂肪体重_LENGTH       = 2;
    public static final int 去脂肪体重标准_LENGTH   = 4;
    public static final int 体脂肪量_LENGTH         = 2;
    public static final int 体脂肪量标准_LENGTH     = 4;
    public static final int 肌肉量_LENGTH           = 2;
    public static final int 肌肉标准_LENGTH         = 4;
    public static final int 躯干脂肪_LENGTH         = 2;
    public static final int 躯干脂肪标准_LENGTH      = 4;
    public static final int 左上肢脂肪量_LENGTH      = 2;
    public static final int 左上肢脂肪标准_LENGTH    = 4;
    public static final int 右上肢脂肪量_LENGTH      = 2;
    public static final int 右上肢脂肪标准_LENGTH    = 4;
    public static final int 左下肢脂肪量_LENGTH      = 2;
    public static final int 左下肢脂肪标准_LENGTH    = 4;
    public static final int 右下肢脂肪量_LENGTH      = 2;
    public static final int 右下肢脂肪标准_LENGTH    = 4;
    public static final int 内脏脂肪指数_LENGTH      = 2;
    public static final int 内脏指数正常范围_LENGTH  = 4;
    /* 节段分析， */
    public static final int 躯干肌肉_LENGTH               = 2;
    public static final int 躯干肌肉标准_LENGTH           = 4;
    public static final int 左上肢肌肉含量_LENGTH         = 2;
    public static final int 左上肢肉正常范围_LENGTH       = 4;
    public static final int 右上肢肌肉含量_LENGTH         = 2;
    public static final int 右上肢肌肉含量正常范围_LENGTH  = 4;
    public static final int 左下肢肌肉含量_LENGTH         = 2;
    public static final int 左下肢肌肉正常范围_LENGTH      = 4;
    public static final int 右下肢肌肉含量_LENGTH          = 2;
    public static final int 右下肢肌肉含量正常范围_LENGTH  = 4;

    public static final int 身体总水分_LENGTH              = 2;
    public static final int 身体总水分正常范围_LENGTH       = 4;
    public static final int 蛋白质含量_LENGTH              = 2;
    public static final int 蛋白质正常范围_LENGTH          = 4;
    public static final int 无机盐含量_LENGTH              = 2;
    public static final int 无机盐含量正常范围_LENGTH      = 4;
    public static final int 细胞外液含量_LENGTH            = 2;
    public static final int 细胞外液正常范围_LENGTH        = 4;
    public static final int 细胞内液含量_LENGTH           = 2;
    public static final int 细胞内液正常范围_LENGTH        = 4;
    // ...
    public static final int BMI结果_LENGTH               = 6;
    public static final int 脂肪率_LENGTH                = 6;

    // ...

    public static final int 身体总评分_LENGTH    = 2;
    public static final int 身体年龄_LENGTH      = 2;
    public static final int 体重调节_LENGTH      = 4;
    public static final int 脂肪调节_LENGTH      = 4;
    public static final int 肌肉调节_LENGTH      = 4;
    public static final int 基础代谢量_LENGTH    = 2;
    public static final int 总能量消耗_LENGTH    = 2;

    /* 女 */
    public static final byte FEMALE = 0x00;
    /* 男 */
    public static final byte MALE   = 0x01;


    /* 位置 */
    /**
     * This class specifies a supported media size. Media size is the
     * dimension of the media on which the content is printed. For
     * example, the {@link #NA_LETTER} media size designates a page
     * with size 8.5" x 11".
     */
    public static final class Posistion {
        private static final String LOG_TAG = "Posistion";

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Posistion 姓名 =
                new Posistion(35 * 2836, 49 * 2836, 33110, 46810);
        public static final Posistion 身高 =
                new Posistion(35 * 2836, 56 * 2836, 33110, 46810);
        public static final Posistion 体重1 =
                new Posistion(35 * 2836, 62 * 2836, 33110, 46810);

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Posistion 测试日期 =
                new Posistion(90 * 2836, 49 * 2836, 33110, 46810);
        public static final Posistion 年龄 =
                new Posistion(90 * 2836, 56 * 2836, 33110, 46810);
        public static final Posistion 性别 =
                new Posistion(90 * 2836, 62 * 2836, 33110, 46810);

        /* 2X. 休成分结果 */
        // 21 体重
        public static final Posistion 体重2 =
                new Posistion(36 * 2836, 83 * 2836, 33110, 46810);
        // 22 去脂肪体重
        public static final Posistion 去脂肪体重 =
                new Posistion(36 * 2836, 89 * 2836, 33110, 46810);
        // 23 肌肉量
        public static final Posistion 肌肉量 =
                new Posistion(36 * 2836, 96 * 2836, 33110, 46810);
        // 24 身体总水分
        public static final Posistion 身体总水分 =
                new Posistion(36 * 2836, 102 * 2836, 33110, 46810);
        // 25 细胞内液
        public static final Posistion 细胞内液 =
                new Posistion(15 * 2836, 115 * 2836, 33110, 46810);
        // 26 细胞外液
        public static final Posistion 细胞外液 =
                new Posistion(38 * 2836, 115 * 2836, 33110, 46810);
        // 27 蛋白质量
        public static final Posistion 蛋白质量 =
                new Posistion(60 * 2836, 500 * 2836, 33110, 46810);
        // 28 无机盐量
        public static final Posistion 无机盐量 =
                new Posistion(82 * 2836, 500 * 2836, 33110, 46810);
        // 29 体脂肪量
        public static final Posistion 体脂肪量 =
                new Posistion(104 * 2836, 500 * 2836, 33110, 46810);

        /* 4X. 调节建议 */
        // 41 体重_标准
        public static final Posistion 体重_标准 =
                new Posistion(58 * 2836, 219 * 2836, 33110, 46810);
        // 42 体重_当前
        public static final Posistion 体重_当前 =
                new Posistion(85 * 2836, 219 * 2836, 33110, 46810);
        // 43 体重_调试量
        public static final Posistion 体重_调节量 =
                new Posistion(112 * 2836, 219 * 2836, 33110, 46810);
        // 44 身体脂肪量_标准
        public static final Posistion 身体脂肪量_标准 =
                new Posistion(58 * 2836, 227 * 2836, 33110, 46810);
        // 45 身体脂肪量_当前
        public static final Posistion 身体脂肪量_当前 =
                new Posistion(85 * 2836, 227 * 2836, 33110, 46810);
        // 46 身体脂肪量_调试量
        public static final Posistion 身体脂肪量_调节量 =
                new Posistion(112 * 2836, 227 * 2836, 33110, 46810);
        // 47 肌肉量_标准
        public static final Posistion 肌肉量_标准 =
                new Posistion(58 * 2836, 235 * 2836, 33110, 46810);
        // 48 肌肉量_当前
        public static final Posistion 肌肉量_当前 =
                new Posistion(85 * 2836, 235 * 2836, 33110, 46810);
        // 49 肌肉量_调节量
        public static final Posistion 肌肉量_调节量 =
                new Posistion(112 * 2836, 235 * 2836, 33110, 46810);

        /* 5x 节段肌肉 */
        // 51 右上肢肌肉含量
        public static final Posistion 右上肢肌肉含量 =
                new Posistion(13 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 52 右下肢肌肉含量
        public static final Posistion 右下肢肌肉含量 =
                new Posistion(13 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 53 左上肢肌肉含量
        public static final Posistion 左上肢肌肉含量 =
                new Posistion(41 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 54 左下肢肌肉含量
        public static final Posistion 左下肢肌肉含量 =
                new Posistion(41 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 55 躯干肌肉含量
        public static final Posistion 躯干肌肉含量 =
                new Posistion(28 * 2836, 262 * 2836, 12 * 2836, 46810);

        /* 6x 节段脂肪 */
        // 61 左上肢脂肪量
        public static final Posistion 右上肢脂肪量 =
                new Posistion(58 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 62 右下肢脂肪量
        public static final Posistion 右下肢脂肪量 =
                new Posistion(58 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 63 左上肢脂肪量
        public static final Posistion 左上肢脂肪量 =
                new Posistion(87 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 64 左下肢脂肪量
        public static final Posistion 左下肢脂肪量 =
                new Posistion(87 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 65 躯干脂肪量
        public static final Posistion 躯干肢脂肪量 =
                new Posistion(72 * 2836, 262 * 2836, 12 * 2836, 46810);

        /* 7x 节段电阻抗 */
        // 71.频率
        public static final Posistion 频率_5k =
                new Posistion(105 * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 频率_50k =
                new Posistion(105 * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 频率_250k =
                new Posistion(105 * 2836, 272 * 2836, 12 * 2836, 46810);

        // 72.右上肢 x+16
        public static final Posistion 右上肢_5k =
                new Posistion((105 + 1*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 右上肢_50k =
                new Posistion((105 + 1*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 右上肢_250k =
                new Posistion((105 + 1*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 73.左上肢 x+16
        public static final Posistion 左上肢_5k =
                new Posistion((105 + 2*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 左上肢_50k =
                new Posistion((105 + 2*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 左上肢_250k =
                new Posistion((105 + 2*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 74.躯干 x+16
        public static final Posistion 躯干_5k =
                new Posistion((105 + 3*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 躯干_50k =
                new Posistion((105 + 3*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 躯干_250k =
                new Posistion((105 + 3*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 75.右下肢 x+16
        public static final Posistion 右下肢_5k =
                new Posistion((105 + 4*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 右下肢_50k =
                new Posistion((105 + 4*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 右下肢_250k =
                new Posistion((105 + 4*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 76.左下肢 x+16
        public static final Posistion 左下肢_5k =
                new Posistion((105 + 5*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Posistion 左下肢_50k =
                new Posistion((105 + 5*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Posistion 左下肢_250k =
                new Posistion((105 + 5*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        /**
         * Creates a new instance.
         * @param widthMils The width in mils (thousands of an inch).
         * @param heightMils The height in mils (thousands of an inch).
         *
         * @throws IllegalArgumentException If the id is empty or the label
         * is empty or the widthMils is less than or equal to zero or the
         * heightMils is less than or equal to zero.
         *
         * @hide
         */
        public Posistion(int xMils, int yMils, int widthMils, int heightMils) {

            if (xMils <= 0) {
                throw new IllegalArgumentException("xMils "
                        + "cannot be less than or equal to zero.");
            }
            if (yMils <= 0) {
                throw new IllegalArgumentException("yMils "
                        + "cannot be less than or euqual to zero.");
            }
            if (widthMils <= 0) {
                throw new IllegalArgumentException("widthMils "
                        + "cannot be less than or equal to zero.");
            }
            if (heightMils <= 0) {
                throw new IllegalArgumentException("heightMils "
                        + "cannot be less than or euqual to zero.");
            }
            mXMils = xMils;
            mYMils = yMils;
            mWidthMils = widthMils;
            mHeightMils = heightMils;
        }

        private final int mXMils;
        private final int mYMils;
        private final int mWidthMils;
        private final int mHeightMils;
        /**
         * Gets the media width in mils (thousands of an inch).
         *
         * @return The media width.
         */
        public int getXMils() {
            return mXMils;
        }

        /**
         * Gets the media height in mils (thousands of an inch).
         *
         * @return The media height.
         */
        public int getYMils() {
            return mYMils;
        }
        /**
         * Gets the media width in mils (thousands of an inch).
         *
         * @return The media width.
         */
        public int getWidthMils() {
            return mWidthMils;
        }

        /**
         * Gets the media height in mils (thousands of an inch).
         *
         * @return The media height.
         */
        public int getHeightMils() {
            return mHeightMils;
        }
    }

    /**
     * 从数据段进行解析各项参数
     * @param data
     */
    public BodyComposition(final byte[] data) {
        int tmpInt = 0;
        //
        float tmpFloat = 0;
        // 符号
        String tmpStr = "";

        // 0. 姓名 编号 3号
        byte[] b = null;
        b = Arrays.copyOfRange(data, 姓名_START, 姓名_START + 姓名_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        姓名 = String.format("%04d", tmpInt);

        // 1. 身高 180.0cm 保留一位小数
        b = Arrays.copyOfRange(data, 身高_START, 身高_START + 身高_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身高 = String.format("%.1f", tmpFloat);

        // 2. 体重 80.8kg
        b = Arrays.copyOfRange(data, 体重1_START, 体重1_START + 体重1_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重1 = String.format("%.1f", tmpFloat);

        // 3. 性别
        性别 = data[性别_START] == MALE ? "男" : "女";

        // 4. 年龄
        年龄 = String.valueOf(data[年龄_START]);

        // 5. 身体电阻值
        b = Arrays.copyOfRange(data, 身体电阻值_START, 身体电阻值_START + 身体电阻值_LENGTH);
        tmpFloat = (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10);
        身体电阻值 = String.format("%.1f", tmpFloat);

        // 6. _50k下la电阻值
        b = Arrays.copyOfRange(data, _50k下la电阻值_START, _50k下la电阻值_START + _50k下la电阻值_LENGTH);
        tmpFloat = (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10);
        _50k下la电阻值 = String.format("%.1f", tmpFloat);

        // 7. _50k下ra电阻值
        b = Arrays.copyOfRange(data, _50k下ra电阻值_START, _50k下ra电阻值_START + _50k下ra电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _50k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 8. _50k下tr电阻值
        b = Arrays.copyOfRange(data, _50k下tr电阻值_START, _50k下tr电阻值_START + _50k下tr电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _50k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 9. _50k下ll电阻值
        b = Arrays.copyOfRange(data, _50k下ll电阻值_START, _50k下ll电阻值_START + _50k下ll电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _50k下ll电阻值 = String.format("%.1f", tmpFloat);

        // 10. _50k下rl电阻值
        b = Arrays.copyOfRange(data, _50k下rl电阻值_START, _50k下rl电阻值_START + _50k下rl电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _50k下rl电阻值 = String.format("%.1f", tmpFloat);

        // 11. _5k下la电阻值
        b = Arrays.copyOfRange(data, _5k下la电阻值_START, _5k下la电阻值_START + _5k下la电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _5k下la电阻值 = String.format("%.1f", tmpFloat);

        // 12. _5k下ra电阻值
        b = Arrays.copyOfRange(data, _5k下ra电阻值_START, _5k下ra电阻值_START + _5k下ra电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _5k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 13. _5k下tr电阻值
        b = Arrays.copyOfRange(data, _5k下tr电阻值_START, _5k下tr电阻值_START + _5k下tr电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _5k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 14. _5k下ll电阻值
        b = Arrays.copyOfRange(data, _5k下ll电阻值_START, _5k下ll电阻值_START + _5k下ll电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _5k下ll电阻值 = String.format("%.1f", tmpFloat);

        // 15. _5k下rl电阻值
        b = Arrays.copyOfRange(data, _5k下rl电阻值_START, _5k下rl电阻值_START + _5k下rl电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _5k下rl电阻值 = String.format("%.1f", tmpFloat);

        // 16. _250k下la电阻值
        b = Arrays.copyOfRange(data, _250k下la电阻值_START, _250k下la电阻值_START + _250k下la电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _250k下la电阻值 = String.format("%.1f", tmpFloat);

        // 17. _250k下ra电阻值
        b = Arrays.copyOfRange(data, _250k下ra电阻值_START, _250k下ra电阻值_START + _250k下ra电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _250k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 18. _250k下tr电阻值
        b = Arrays.copyOfRange(data, _250k下tr电阻值_START, _250k下tr电阻值_START + _250k下tr电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _250k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 19. _250k下ll电阻值
        b = Arrays.copyOfRange(data, _250k下ll电阻值_START, _250k下ll电阻值_START + _250k下ll电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _250k下ll电阻值 = String.format("%.1f", tmpFloat);


        // 20. _250k下rl电阻值
        b = Arrays.copyOfRange(data, _250k下rl电阻值_START, _250k下rl电阻值_START + _250k下rl电阻值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        _250k下rl电阻值 = String.format("%.1f", tmpFloat);


        // 21. 测试时间(默认：2016-01-24 13:42)
        b = Arrays.copyOfRange(data, DATE_START, DATE_START + DATE_LENGTH);
        测试日期 = "20" + new String(new byte[]{b[0],b[1]}) + "-" + /* 年 */
                new String(new byte[]{b[2],b[3]}) + "-" +   /* 月 */
                new String(new byte[]{b[4],b[5]}) + " " +   /* 日 */
                new String(new byte[]{b[6],b[7]}) + ":" +   /* 时 */
                new String(new byte[]{b[8],b[9]});          /* 分 */


        // 22. 体重 80.8kg
        b = Arrays.copyOfRange(data, 体重2_START, 体重2_START + 体重2_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重2 = String.format("%.1f", tmpFloat);

        // 23. 体重标准范围
        b = Arrays.copyOfRange(data, 体重标准范围_START, 体重标准范围_START + 体重标准范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重标准范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 24. 体重标准值
        b = Arrays.copyOfRange(data, 体重标准值_START, 体重标准值_START + 体重标准值_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重标准值 = String.format("%.1f", tmpFloat);

        // 25. 去脂肪体重
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 26. 去脂肪体重标准
        b = Arrays.copyOfRange(data, 去脂肪体重标准_START, 去脂肪体重标准_START + 去脂肪体重标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        去脂肪体重标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 27. 体脂肪量
        b = Arrays.copyOfRange(data, 体脂肪量_START, 体脂肪量_START + 体脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体脂肪量 = String.format("%.1f", tmpFloat);

        // 28. 体脂肪量标准
        b = Arrays.copyOfRange(data, 体脂肪量标准_START, 体脂肪量标准_START + 体脂肪量标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体脂肪量标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 29. 肌肉量
        b = Arrays.copyOfRange(data, 肌肉量_START, 肌肉量_START + 肌肉量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉量 = String.format("%.1f", tmpFloat);

        // 30. 肌肉标准
        b = Arrays.copyOfRange(data, 肌肉标准_START, 肌肉标准_START + 肌肉标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 31. 躯干脂肪
        b = Arrays.copyOfRange(data, 躯干脂肪_START, 躯干脂肪_START + 躯干脂肪_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干脂肪 = String.format("%.1f", tmpFloat);

        // 32. 躯干脂肪标准
        b = Arrays.copyOfRange(data, 躯干脂肪标准_START, 躯干脂肪标准_START + 躯干脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干脂肪标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 33. 左上肢脂肪量
        b = Arrays.copyOfRange(data, 左上肢脂肪量_START, 左上肢脂肪量_START + 左上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢脂肪量 = String.format("%.1f", tmpFloat);

        // 34. 左上肢脂肪标准
        b = Arrays.copyOfRange(data, 左上肢脂肪标准_START, 左上肢脂肪标准_START + 左上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢脂肪标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 35. 右上肢脂肪量
        b = Arrays.copyOfRange(data, 右上肢脂肪量_START, 右上肢脂肪量_START + 右上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢脂肪量 = String.format("%.1f", tmpFloat);

        // 36. 右上肢脂肪标准
        b = Arrays.copyOfRange(data, 右上肢脂肪标准_START, 右上肢脂肪标准_START + 右上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢脂肪标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 37. 左下肢脂肪量
        b = Arrays.copyOfRange(data, 左下肢脂肪量_START, 左下肢脂肪量_START + 左下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢脂肪量 = String.format("%.1f", tmpFloat);

        // 38. 左下肢脂肪标准
        b = Arrays.copyOfRange(data, 左下肢脂肪标准_START, 左下肢脂肪标准_START + 左下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢脂肪标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 39. 右下肢脂肪量
        b = Arrays.copyOfRange(data, 右下肢脂肪量_START, 右下肢脂肪量_START + 右下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢脂肪量 = String.format("%.1f", tmpFloat);

        // 40. 右下肢脂肪标准
        b = Arrays.copyOfRange(data, 右下肢脂肪标准_START, 右下肢脂肪标准_START + 右下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢脂肪标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 41. 内脏脂肪指数
        b = Arrays.copyOfRange(data, 内脏脂肪指数_START, 内脏脂肪指数_START + 内脏脂肪指数_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        内脏脂肪指数 = String.format("%.1f", tmpFloat);

        // 42. 内脏指数正常范围
        b = Arrays.copyOfRange(data, 内脏指数正常范围_START, 内脏指数正常范围_START + 内脏指数正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        内脏指数正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 43. 躯干肌肉含量
        b = Arrays.copyOfRange(data, 躯干肌肉_START, 躯干肌肉_START + 躯干肌肉_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干肌肉含量 = String.format("%.1f", tmpFloat);

        // 44. 躯干肌肉标准
        b = Arrays.copyOfRange(data, 躯干肌肉标准_START, 躯干肌肉标准_START + 躯干肌肉标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干肌肉标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 45. 左上肢肌肉含量
        b = Arrays.copyOfRange(data, 左上肢肌肉含量_START, 左上肢肌肉含量_START + 左上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 46. 左上肢肉正常范围
        b = Arrays.copyOfRange(data, 左上肢肉正常范围_START, 左上肢肉正常范围_START + 左上肢肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢肉正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 47. 右上肢肌肉含量
        b = Arrays.copyOfRange(data, 右上肢肌肉含量_START, 右上肢肌肉含量_START + 右上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 48. 右上肢肌肉含量正常范围
        b = Arrays.copyOfRange(data, 右上肢肌肉含量正常范围_START, 右上肢肌肉含量正常范围_START + 右上肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢肌肉含量正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 49. 左下肢肌肉含量
        b = Arrays.copyOfRange(data, 左下肢肌肉含量_START, 左下肢肌肉含量_START +  左下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 50. 左下肢肌肉正常范围
        b = Arrays.copyOfRange(data, 左下肢肌肉正常范围_START, 左下肢肌肉正常范围_START + 左下肢肌肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢肌肉正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 51. 右下肢肌肉含量
        b = Arrays.copyOfRange(data, 右下肢肌肉含量_START, 右下肢肌肉含量_START + 右下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 52. 右下肢肌肉含量正常范围
        b = Arrays.copyOfRange(data, 右下肢肌肉含量正常范围_START, 右下肢肌肉含量正常范围_START + 右下肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢肌肉含量正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 53. 身体总水分
        b = Arrays.copyOfRange(data, 身体总水分_START, 身体总水分_START + 身体总水分_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总水分 = String.format("%.1f", tmpFloat);

        // 54. 身体总水分正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 身体总水分正常范围_START, 身体总水分正常范围_START + 身体总水分正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总水分正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 55. 蛋白质含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质含量_START, 蛋白质含量_START + 蛋白质含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        蛋白质含量 = String.format("%.1f", tmpFloat);

        // 56. 蛋白质正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质正常范围_START, 蛋白质正常范围_START + 蛋白质正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        蛋白质正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 57. 无机盐量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量_START, 无机盐含量_START + 无机盐含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        无机盐含量 = String.format("%.1f", tmpFloat);

        // 58. 无机盐含量正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量正常范围_START, 无机盐含量正常范围_START + 无机盐含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        无机盐含量正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 59. 细胞外液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液含量_START, 细胞外液含量_START + 细胞外液含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞外液含量 = String.format("%.1f", tmpFloat);

        // 60. 细胞外液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液正常范围_START, 细胞外液正常范围_START + 细胞外液正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞外液正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 61. 细胞内液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液含量_START, 细胞内液含量_START + 细胞内液含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞内液含量 = String.format("%.1f", tmpFloat);

        // 62. 细胞内液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液正常范围_START, 细胞内液正常范围_START + 细胞内液正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞内液正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 63. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 64. BMI结果
        b = Arrays.copyOfRange(data, BMI结果_START, BMI结果_START + BMI结果_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        BMI结果 = String.format("%.1f", tmpFloat);

        // 65. 脂肪率
        b = Arrays.copyOfRange(data, 脂肪率_START, 脂肪率_START + 脂肪率_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        脂肪率 = String.format("%.1f", tmpFloat);

        // 66. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 67. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 68. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
//        去脂肪体重 = String.format("%d", tmpInt);

        // 69. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
//        去脂肪体重 = String.format("%d", tmpInt);

        // 70. 身体总评分 okay 保留一位小数
        b = Arrays.copyOfRange(data, 身体总评分_START, 身体总评分_START + 身体总评分_LENGTH);
        tmpFloat = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总评分 = String.format("%.1f", tmpFloat);

        // 71. 身体年龄 okay 整数
        b = Arrays.copyOfRange(data, 身体年龄_START, 身体年龄_START + 身体年龄_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        身体年龄 = String.format("%d", tmpInt);

        // 72. 体重调节 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 体重调节_START, 体重调节_START + 体重调节_LENGTH);
        tmpStr = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重调节 = String.format("%s%.1f", tmpStr, tmpFloat);

        // 73. 脂肪调节 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 脂肪调节_START, 脂肪调节_START + 脂肪调节_LENGTH);
        tmpStr = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        脂肪调节 = String.format("%s%.1f", tmpStr, tmpFloat);

        // 74. 肌肉调节 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 肌肉调节_START, 肌肉调节_START + 肌肉调节_LENGTH);
        tmpStr = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉调节 = String.format("%s%.1f", tmpStr, tmpFloat);

        // 75. 基础代谢量 okay
        b = Arrays.copyOfRange(data, 基础代谢量_START, 基础代谢量_START + 基础代谢量_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        基础代谢量 = String.format("%d", tmpInt);

        // 76. 总能量消耗
        b = Arrays.copyOfRange(data, 总能量消耗_START, 总能量消耗_START + 总能量消耗_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        总能量消耗 = String.format("%d", tmpInt);
        Log.i(LOG_TAG, toString());
    }

    @Override
    public String toString() {
        return "BodyComposition{" +
                "\n姓名='" + 姓名 + '\'' +
                "\n身高='" + 身高 + '\'' +
                "\n体重1='" + 体重1 + '\'' +
                "\n性别='" + 性别 + '\'' +
                "\n年龄='" + 年龄 + '\'' +
                "\n身体电阻值='" + 身体电阻值 + '\'' +
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
                "\n测试日期='" + 测试日期 + '\'' +
                "\n体重2='" + 体重2 + '\'' +
                "\n体重标准范围='" + 体重标准范围 + '\'' +
                "\n体重标准值='" + 体重标准值 + '\'' +
                "\n去脂肪体重='" + 去脂肪体重 + '\'' +
                "\n去脂肪体重标准='" + 去脂肪体重标准 + '\'' +
                "\n体脂肪量='" + 体脂肪量 + '\'' +
                "\n体脂肪量标准='" + 体脂肪量标准 + '\'' +
                "\n肌肉量='" + 肌肉量 + '\'' +
                "\n肌肉标准='" + 肌肉标准 + '\'' +
                "\n躯干脂肪='" + 躯干脂肪 + '\'' +
                "\n躯干脂肪标准='" + 躯干脂肪标准 + '\'' +
                "\n左上肢脂肪量='" + 左上肢脂肪量 + '\'' +
                "\n左上肢脂肪标准='" + 左上肢脂肪标准 + '\'' +
                "\n右上肢脂肪量='" + 右上肢脂肪量 + '\'' +
                "\n右上肢脂肪标准='" + 右上肢脂肪标准 + '\'' +
                "\n左下肢脂肪量='" + 左下肢脂肪量 + '\'' +
                "\n左下肢脂肪标准='" + 左下肢脂肪标准 + '\'' +
                "\n右下肢脂肪量='" + 右下肢脂肪量 + '\'' +
                "\n右下肢脂肪标准='" + 右下肢脂肪标准 + '\'' +
                "\n内脏脂肪指数='" + 内脏脂肪指数 + '\'' +
                "\n内脏指数正常范围='" + 内脏指数正常范围 + '\'' +
                "\n躯干肌肉含量='" + 躯干肌肉含量 + '\'' +
                "\n躯干肌肉标准='" + 躯干肌肉标准 + '\'' +
                "\n左上肢肌肉含量='" + 左上肢肌肉含量 + '\'' +
                "\n左上肢肉正常范围='" + 左上肢肉正常范围 + '\'' +
                "\n右上肢肌肉含量='" + 右上肢肌肉含量 + '\'' +
                "\n右上肢肌肉含量正常范围='" + 右上肢肌肉含量正常范围 + '\'' +
                "\n左下肢肌肉含量='" + 左下肢肌肉含量 + '\'' +
                "\n左下肢肌肉正常范围='" + 左下肢肌肉正常范围 + '\'' +
                "\n右下肢肌肉含量='" + 右下肢肌肉含量 + '\'' +
                "\n右下肢肌肉含量正常范围='" + 右下肢肌肉含量正常范围 + '\'' +
                "\n身体总水分='" + 身体总水分 + '\'' +
                "\n身体总水分正常范围='" + 身体总水分正常范围 + '\'' +
                "\n蛋白质含量='" + 蛋白质含量 + '\'' +
                "\n蛋白质正常范围='" + 蛋白质正常范围 + '\'' +
                "\n无机盐含量='" + 无机盐含量 + '\'' +
                "\n无机盐含量正常范围='" + 无机盐含量正常范围 + '\'' +
                "\n细胞外液含量='" + 细胞外液含量 + '\'' +
                "\n细胞外液正常范围='" + 细胞外液正常范围 + '\'' +
                "\n细胞内液含量='" + 细胞内液含量 + '\'' +
                "\n细胞内液正常范围='" + 细胞内液正常范围 + '\'' +
                "\nBMI结果='" + BMI结果 + '\'' +
                "\n脂肪率='" + 脂肪率 + '\'' +
                "\n身体总评分='" + 身体总评分 + '\'' +
                "\n身体年龄='" + 身体年龄 + '\'' +
                "\n体重调节='" + 体重调节 + '\'' +
                "\n脂肪调节='" + 脂肪调节 + '\'' +
                "\n肌肉调节='" + 肌肉调节 + '\'' +
                "\n基础代谢量='" + 基础代谢量 + '\'' +
                "\n总能量消耗='" + 总能量消耗 + '\'' +
                '}';
    }
}
