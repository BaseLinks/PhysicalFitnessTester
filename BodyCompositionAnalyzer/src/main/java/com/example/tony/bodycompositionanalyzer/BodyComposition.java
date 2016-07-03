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
    public final int    SEX;
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
    /** 单位*10 */
    public final int    体重_CUR;
    public final String 体重2;
    public final int    体重_MIN;
    public final int    体重_MAX;
    public final String 体重标准范围;
    public final String 体重标准值;
    public final String 去脂肪体重;
    public final String 去脂肪体重标准;
    public final String 体脂肪量;
    public final String 体脂肪量标准;
    public final int    体脂肪量_CUR;
    public final int    体脂肪量_MIN;
    public final int    体脂肪量_MAX;
    public final String 肌肉量;
    public final int    肌肉量_CUR;
    public final int    肌肉量_MIN;
    public final int    肌肉量_MAX;
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
    public final int    内脏脂肪_CUR;
    public final int    内脏脂肪_MAX;
    public final int    内脏脂肪_MIN;
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
    public final int    身体总水分_CUR;
    public final int    身体总水分_MIN;
    public final int    身体总水分_MAX;
    public final String 蛋白质含量;
    public final String 蛋白质正常范围;
    public final String 无机盐含量;
    public final String 无机盐含量正常范围;
    public final String 细胞外液含量;
    public final String 细胞外液正常范围;
    public final String 细胞内液含量;
    public final String 细胞内液正常范围;
    // ...
    public short[] BMI结果;
    public final int    身体质量_CUR;
    public final int    身体质量_MAX;
    public final int    身体质量_MIN;
    public final int    脂肪率_CUR;
    public final int    脂肪率_MAX;
    public final int    脂肪率_MIN;

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
    public static final int DATA_LENGTH = 218;
    public static final int VERIFICATION_START = 223;
    public static final int VERIFICATION_LENGTH = 4;
    public static final int TOTAL_LENGTH = ACK_LENGTH + DATA_LENGTH + VERIFICATION_LENGTH; // 6+218+4=228

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
    public static final int 身体质量_START                = 178;
    public static final int 身体质量范围_START           = 180;
    public static final int 脂肪率_START                 = 184;
    public static final int 脂肪率范围_START             = 186;

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
    public static final int 身体质量_LENGTH              = 2;
    public static final int 身体质量范围_LENGTH          = 4;
    public static final int 脂肪率_LENGTH                = 2;
    public static final int 脂肪率范围_LENGTH             = 4;

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
    /** 脂肪率下限 */
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

    /** 在A4纸上方块宽度 */
    public static final double SINGLE_RECT_WIDTH = 16.8;
    /** 在A4纸上方块高度 */
    public static final double SINGLE_RECT_HEIGHT = 13.4;
    /** 原点坐标 */
    public static final double ORIGIN_X = 133;
    public static final double ORIGIN_Y = 146;


    /* 位置 */
    /**
     * This class specifies a supported media size. Media size is the
     * dimension of the media on which the content is printed. For
     * example, the {@link #NA_LETTER} media size designates a page
     * with size 8.5" x 11".
     */
    public static final class Position {
        private static final String LOG_TAG = "Posistion";

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Position 姓名 =
                new Position(35 * 2836, 46 * 2836, 20 * 2836, 46810);
        public static final Position 身高 =
                new Position(35 * 2836, 52 * 2836, 20 * 2836, 46810);
        public static final Position 体重1 =
                new Position(35 * 2836, 58 * 2836, 20 * 2836, 46810);

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Position 测试日期 =
                new Position(90 * 2836, 46 * 2836, 35 * 2836, 46810);
        public static final Position 年龄 =
                new Position(90 * 2836, 52 * 2836, 20 * 2836, 46810);
        public static final Position 性别 =
                new Position(90 * 2836, 58 * 2836, 20 * 2836, 46810);

        /* 2X. 休成分结果 */
        // 21 体重
        public static final Position 体重2 =
                new Position(36 * 2836, 80 * 2836, 85 * 2836, 46810);
        // 22 去脂肪体重
        public static final Position 去脂肪体重 =
                new Position(36 * 2836, 86 * 2836, 85 * 2836, 46810);
        // 23 肌肉量
        public static final Position 肌肉量 =
                new Position(36 * 2836, 93 * 2836, 85 * 2836, 46810);
        // 24 身体总水分
        public static final Position 身体总水分 =
                new Position(36 * 2836, 99 * 2836, 85 * 2836, 46810);
        // 25 细胞内液
        public static final Position 细胞内液 =
                new Position(15 * 2836, 111 * 2836, 20 * 2836, 46810);
        // 26 细胞外液
        public static final Position 细胞外液 =
                new Position(38 * 2836, 111 * 2836, 20 * 2836, 46810);
        // 27 蛋白质量
        public static final Position 蛋白质量 =
                new Position(60 * 2836, 104 * 2836, 20 * 2836, 46810);
        // 28 无机盐量
        public static final Position 无机盐量 =
                new Position(82 * 2836, 104 * 2836, 20 * 2836, 46810);
        // 29 体脂肪量
        public static final Position 体脂肪量 =
                new Position(104 * 2836, 104 * 2836, 20 * 2836, 46810);

        /* 3X. 体成分分析 */
        // 32 体重
        public static final Position 体成分分析_体重 =
                new Position(34 * 2836, 140 * 2836, 33110, 46810);
        // 32 身体质量
        public static final Position 体成分分析_身体质量 =
                new Position(34 * 2836, 149 * 2836, 33110, 46810);
        // 33 体脂肪率
        public static final Position 体成分分析_体脂肪率 =
                new Position(34 * 2836, 158 * 2836, 33110, 46810);
        // 34 体脂肪量
        public static final Position 体成分分析_体脂肪量 =
                new Position(34 * 2836, 168 * 2836, 33110, 46810);
        // 35 肌肉量
        public static final Position 体成分分析_肌肉量 =
                new Position(34 * 2836, 176 * 2836, 33110, 46810);
        // 36 身体水分
        public static final Position 体成分分析_身体水分 =
                new Position(34 * 2836, 185 * 2836, 33110, 46810);
        // 37 内脏脂肪
        public static final Position 体成分分析_内脏脂肪 =
                new Position(34 * 2836, 194 * 2836, 33110, 46810);

        /* 4X. 调节建议 */
        // 41 体重_标准
        public static final Position 体重_标准 =
                new Position(58 * 2836, 219 * 2836, 33110, 46810);
        // 42 体重_当前
        public static final Position 体重_当前 =
                new Position(85 * 2836, 219 * 2836, 33110, 46810);
        // 43 体重_调试量
        public static final Position 体重_调节量 =
                new Position(112 * 2836, 219 * 2836, 33110, 46810);
        // 44 身体脂肪量_标准
        public static final Position 身体脂肪量_标准 =
                new Position(58 * 2836, 227 * 2836, 33110, 46810);
        // 45 身体脂肪量_当前
        public static final Position 身体脂肪量_当前 =
                new Position(85 * 2836, 227 * 2836, 33110, 46810);
        // 46 身体脂肪量_调试量
        public static final Position 身体脂肪量_调节量 =
                new Position(112 * 2836, 227 * 2836, 33110, 46810);
        // 47 肌肉量_标准
        public static final Position 肌肉量_标准 =
                new Position(58 * 2836, 235 * 2836, 33110, 46810);
        // 48 肌肉量_当前
        public static final Position 肌肉量_当前 =
                new Position(85 * 2836, 235 * 2836, 33110, 46810);
        // 49 肌肉量_调节量
        public static final Position 肌肉量_调节量 =
                new Position(112 * 2836, 235 * 2836, 33110, 46810);

        /* 5x 节段肌肉 */
        // 51 左上肢肌肉含量
        public static final Position 左上肢肌肉含量 =
                new Position(13 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 52 左下肢肌肉含量
        public static final Position 左下肢肌肉含量 =
                new Position(13 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 53 右上肢肌肉含量
        public static final Position 右上肢肌肉含量 =
                new Position(41 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 54 右下肢肌肉含量
        public static final Position 右下肢肌肉含量 =
                new Position(41 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 55 躯干肌肉含量
        public static final Position 躯干肌肉含量 =
                new Position(28 * 2836, 262 * 2836, 12 * 2836, 46810);

        /* 6x 节段脂肪 */
        // 61 左上肢脂肪量
        public static final Position 左上肢脂肪量 =
                new Position(58 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 62 左下肢脂肪量
        public static final Position 左下肢脂肪量 =
                new Position(58 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 63 右上肢脂肪量
        public static final Position 右上肢脂肪量 =
                new Position(87 * 2836, 253 * 2836, 12 * 2836, 46810);

        // 64 右下肢脂肪量
        public static final Position 右下肢脂肪量 =
                new Position(87 * 2836, 275 * 2836, 12 * 2836, 46810);

        // 65 躯干脂肪量
        public static final Position 躯干肢脂肪量 =
                new Position(72 * 2836, 262 * 2836, 12 * 2836, 46810);

        /* 7x 节段电阻抗 */
        // 71.频率
        public static final Position 频率_5k =
                new Position(105 * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 频率_50k =
                new Position(105 * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 频率_250k =
                new Position(105 * 2836, 272 * 2836, 12 * 2836, 46810);

        // 72.右上肢 x+16
        public static final Position 右上肢_5k =
                new Position((105 + 1*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 右上肢_50k =
                new Position((105 + 1*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 右上肢_250k =
                new Position((105 + 1*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 73.左上肢 x+16
        public static final Position 左上肢_5k =
                new Position((105 + 2*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 左上肢_50k =
                new Position((105 + 2*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 左上肢_250k =
                new Position((105 + 2*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 74.躯干 x+16
        public static final Position 躯干_5k =
                new Position((105 + 3*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 躯干_50k =
                new Position((105 + 3*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 躯干_250k =
                new Position((105 + 3*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 75.右下肢 x+16
        public static final Position 右下肢_5k =
                new Position((105 + 4*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 右下肢_50k =
                new Position((105 + 4*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 右下肢_250k =
                new Position((105 + 4*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        // 76.左下肢 x+16
        public static final Position 左下肢_5k =
                new Position((105 + 5*17) * 2836, 264 * 2836, 12 * 2836, 46810);

        public static final Position 左下肢_50k =
                new Position((105 + 5*17) * 2836, 268 * 2836, 12 * 2836, 46810);

        public static final Position 左下肢_250k =
                new Position((105 + 5*17) * 2836, 272 * 2836, 12 * 2836, 46810);

        /* 8x 肥胖评估 */
        // 81.体重
        public static final Position 肥胖评估_体重_不足 =
                new Position(155 * 2836, 220 * 2836, 4 * 2836, 46810);

        public static final Position 肥胖评估_体重_正常 =
                new Position((155 + 17) * 2836, 220 * 2836, 4 * 2836, 46810);

        public static final Position 肥胖评估_体重_过量 =
                new Position((155 + 35) * 2836, 220 * 2836, 4 * 2836, 46810);

        // 82.脂肪量
        public static final Position 肥胖评估_脂肪量_不足 =
                new Position(155 * 2836, 228 * 2836, 12 * 2836, 46810);

        public static final Position 肥胖评估_脂肪量_正常 =
                new Position((155 + 17) * 2836, 228 * 2836, 12 * 2836, 46810);

        public static final Position 肥胖评估_脂肪量_过量 =
                new Position((155 + 35) * 2836, 228 * 2836, 12 * 2836, 46810);

        // 83.肌肉量
        public static final Position 肥胖评估_肌肉量_不足 =
                new Position(155 * 2836, 236 * 2836, 12 * 2836, 46810);

        public static final Position 肥胖评估_肌肉量_正常 =
                new Position((155 + 17) * 2836, 236 * 2836, 12 * 2836, 46810);

        public static final Position 肥胖评估_肌肉量_过量 =
                new Position((155 + 35) * 2836, 236 * 2836, 12 * 2836, 46810);

        /* 9x 营养评估 */
        // 91.蛋白质
        public static final Position 营养评估_蛋白质_不足 =
                new Position(155 * 2836, 163 * 2836, 4 * 2836, 46810);

        public static final Position 营养评估_蛋白质_正常 =
                new Position((155 + 17) * 2836, 163 * 2836, 4 * 2836, 46810);

        public static final Position 营养评估_蛋白质_过量 =
                new Position((155 + 35) * 2836, 163 * 2836, 4 * 2836, 46810);

        // 92.无机盐
        public static final Position 营养评估_无机盐_不足 =
                new Position(155 * 2836, 171 * 2836, 12 * 2836, 46810);

        public static final Position 营养评估_无机盐_正常 =
                new Position((155 + 17) * 2836, 171 * 2836, 12 * 2836, 46810);

        public static final Position 营养评估_无机盐_过量 =
                new Position((155 + 35) * 2836, 171 * 2836, 12 * 2836, 46810);

        // 93.基础代谢量
        public static final Position 基础代谢量 =
                new Position(171 * 2836, 184 * 2836, 12 * 2836, 46810);
        // 94.总能量消耗
        public static final Position 总能量消耗 =
                new Position(171 * 2836, 192 * 2836, 12 * 2836, 46810);
        // 95.身体年龄
        public static final Position 身体年龄 =
                new Position(171 * 2836, 200 * 2836, 12 * 2836, 46810);

        // 10x.健康评估
        public static final Position 健康评估 =
                new Position(135 * 2836, 280 * 2836, 18 * 2836, 46810);
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
        public Position(int xMils, int yMils, int widthMils, int heightMils) {

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
        SEX = data[性别_START];
        性别 = SEX == MALE ? "男" : "女";

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
        体重_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        tmpFloat = (float) 体重_CUR / 10;
        体重2 = String.format("%.1f", tmpFloat);

        // 23. 体重标准范围
        b = Arrays.copyOfRange(data, 体重标准范围_START, 体重标准范围_START + 体重标准范围_LENGTH);
        体重_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        体重_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        体重标准范围 = String.format("%.1f-%.1f", (float) 体重_MIN / 10, (float) 体重_MAX / 10);

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
        体脂肪量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        体脂肪量 = String.format("%.1f", (float) 体脂肪量_CUR / 10);

        // 28. 体脂肪量标准
        b = Arrays.copyOfRange(data, 体脂肪量标准_START, 体脂肪量标准_START + 体脂肪量标准_LENGTH);
        体脂肪量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        体脂肪量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        体脂肪量标准 = String.format("%.1f-%.1f", (float) 体脂肪量_MIN / 10, (float) 体脂肪量_MAX / 10);

        // 29. 肌肉量
        b = Arrays.copyOfRange(data, 肌肉量_START, 肌肉量_START + 肌肉量_LENGTH);
        肌肉量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        肌肉量 = String.format("%.1f", (float) 肌肉量_CUR / 10);

        // 30. 肌肉标准
        b = Arrays.copyOfRange(data, 肌肉标准_START, 肌肉标准_START + 肌肉标准_LENGTH);
        肌肉量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        肌肉量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        肌肉标准 = String.format("%.1f-%.1f", (float) 肌肉量_MIN / 10, (float) 肌肉量_MAX / 10);

        // 31. 躯干脂肪 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干脂肪_START, 躯干脂肪_START + 躯干脂肪_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        躯干脂肪 = String.format("%.2f", tmpFloat);

        // 32. 躯干脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干脂肪标准_START, 躯干脂肪标准_START + 躯干脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        躯干脂肪标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 33. 左上肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢脂肪量_START, 左上肢脂肪量_START + 左上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左上肢脂肪量 = String.format("%.2f", tmpFloat);

        // 34. 左上肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢脂肪标准_START, 左上肢脂肪标准_START + 左上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左上肢脂肪标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 35. 右上肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢脂肪量_START, 右上肢脂肪量_START + 右上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右上肢脂肪量 = String.format("%.2f", tmpFloat);

        // 36. 右上肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢脂肪标准_START, 右上肢脂肪标准_START + 右上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右上肢脂肪标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 37. 左下肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢脂肪量_START, 左下肢脂肪量_START + 左下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左下肢脂肪量 = String.format("%.2f", tmpFloat);

        // 38. 左下肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢脂肪标准_START, 左下肢脂肪标准_START + 左下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左下肢脂肪标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 39. 右下肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢脂肪量_START, 右下肢脂肪量_START + 右下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右下肢脂肪量 = String.format("%.2f", tmpFloat);

        // 40. 右下肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢脂肪标准_START, 右下肢脂肪标准_START + 右下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右下肢脂肪标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 41. 内脏脂肪指数
        b = Arrays.copyOfRange(data, 内脏脂肪指数_START, 内脏脂肪指数_START + 内脏脂肪指数_LENGTH);
        内脏脂肪_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        内脏脂肪指数 = String.format("%.1f", (float) 内脏脂肪_CUR / 10);

        // 42. 内脏指数正常范围
        b = Arrays.copyOfRange(data, 内脏指数正常范围_START, 内脏指数正常范围_START + 内脏指数正常范围_LENGTH);
        内脏脂肪_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        tmpStr = String.format("%.1f", tmpFloat);
        内脏脂肪_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        内脏指数正常范围 = String.format("%.1f-%.1f", (float) 内脏脂肪_MIN / 10, (float) 内脏脂肪_MAX / 10);

        // 43. 躯干肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干肌肉_START, 躯干肌肉_START + 躯干肌肉_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        躯干肌肉含量 = String.format("%.2f", tmpFloat);

        // 44. 躯干肌肉标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干肌肉标准_START, 躯干肌肉标准_START + 躯干肌肉标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        躯干肌肉标准 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 45. 左上肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢肌肉含量_START, 左上肢肌肉含量_START + 左上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左上肢肌肉含量 = String.format("%.2f", tmpFloat);

        // 46. 左上肢肉正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢肉正常范围_START, 左上肢肉正常范围_START + 左上肢肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左上肢肉正常范围 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 47. 右上肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢肌肉含量_START, 右上肢肌肉含量_START + 右上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右上肢肌肉含量 = String.format("%.2f", tmpFloat);

        // 48. 右上肢肌肉含量正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢肌肉含量正常范围_START, 右上肢肌肉含量正常范围_START + 右上肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右上肢肌肉含量正常范围 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 49. 左下肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢肌肉含量_START, 左下肢肌肉含量_START +  左下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左下肢肌肉含量 = String.format("%.2f", tmpFloat);

        // 50. 左下肢肌肉正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢肌肉正常范围_START, 左下肢肌肉正常范围_START + 左下肢肌肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        左下肢肌肉正常范围 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 51. 右下肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢肌肉含量_START, 右下肢肌肉含量_START + 右下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右下肢肌肉含量 = String.format("%.2f", tmpFloat);

        // 52. 右下肢肌肉含量正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢肌肉含量正常范围_START, 右下肢肌肉含量正常范围_START + 右下肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        tmpStr = String.format("%.2f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        右下肢肌肉含量正常范围 = String.format("%s-%.2f", tmpStr, tmpFloat);

        // 53. 身体总水分
        b = Arrays.copyOfRange(data, 身体总水分_START, 身体总水分_START + 身体总水分_LENGTH);
        身体总水分_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        身体总水分 = String.format("%.1f", (float) 身体总水分_CUR / 10);

        // 54. 身体总水分正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 身体总水分正常范围_START, 身体总水分正常范围_START + 身体总水分正常范围_LENGTH);
        身体总水分_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        身体总水分_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        身体总水分正常范围 = String.format("%.1f-%.1f", (float)身体总水分_MIN / 10, (float)身体总水分_MAX / 10);

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

        // 64. BMI结果 注:将byte[]转化为short[]
        b = Arrays.copyOfRange(data, BMI结果_START, BMI结果_START + BMI结果_LENGTH);
        BMI结果 = new short[b.length / 2];
        ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(BMI结果);

        // 64. 身体质量
        b = Arrays.copyOfRange(data, 身体质量_START, 身体质量_START + 身体质量_LENGTH);
        身体质量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();

        // 64. 身体质量范围
        b = Arrays.copyOfRange(data, 身体质量范围_START, 身体质量范围_START + 身体质量范围_LENGTH);
        身体质量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        身体质量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();

        // 65. 脂肪率
        b = Arrays.copyOfRange(data, 脂肪率_START, 脂肪率_START + 脂肪率_LENGTH);
        脂肪率_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();

        b = Arrays.copyOfRange(data, 脂肪率范围_START, 脂肪率范围_START + 脂肪率范围_LENGTH);
        脂肪率_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
        脂肪率_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort();

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
                "\n脂肪率_CUR='" + 脂肪率_CUR + '\'' +
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
