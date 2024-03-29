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
    public final float 体重; // 原始值，未做任何计算的值
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
    public final int    体重_STD;
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
    public final int    体脂肪量_STD;
    public final String 肌肉量;
    public final int    肌肉量_CUR;
    public final int    肌肉量_MIN;
    public final int    肌肉量_MAX;
    /** 肌肉标准值 */
    public final int    肌肉量_STD;
    public final String 肌肉标准;
    public final String 躯干脂肪;
    public final String 躯干脂肪标准;
    public final float  躯干脂肪_MIN;
    public final float  躯干脂肪_MAX;
    public final float  躯干脂肪_CUR;
    public final String 躯干脂肪_RESULT;
    public final String 左上肢脂肪量;
    public final String 左上肢脂肪标准;
    public final float  左上肢脂肪量_MIN;
    public final float  左上肢脂肪量_MAX;
    public final float  左上肢脂肪量_CUR;
    public final String 左上肢脂肪量_RESULT;
    public final String 右上肢脂肪量;
    public final String 右上肢脂肪标准;
    public final float  右上肢脂肪量_MIN;
    public final float  右上肢脂肪量_MAX;
    public final float  右上肢脂肪量_CUR;
    public final String 右上肢脂肪量_RESULT;
    public final String 左下肢脂肪量;
    public final String 左下肢脂肪标准;
    public final float  左下肢脂肪量_MIN;
    public final float  左下肢脂肪量_MAX;
    public final float  左下肢脂肪量_CUR;
    public final String 左下肢脂肪量_RESULT;
    public final String 右下肢脂肪量;
    public final String 右下肢脂肪标准;
    public final float  右下肢脂肪量_MIN;
    public final float  右下肢脂肪量_MAX;
    public final float  右下肢脂肪量_CUR;
    public final String 右下肢脂肪量_RESULT;
    public final String 内脏脂肪指数;
    public final String 内脏指数正常范围;
    public final float  内脏脂肪_CUR;
    public final float  内脏脂肪_MAX;
    public final float  内脏脂肪_MIN;
    /* 节段分析， */
    public final float  躯干肌肉含量_MIN;
    public final float  躯干肌肉含量_MAX;
    public final float  躯干肌肉含量_CUR;
    public final String 躯干肌肉含量_RESULT;
    public final String 躯干肌肉含量;
    public final String 躯干肌肉含量正常范围;
    public final String 左上肢肌肉含量;
    public final String 左上肢肌肉含量正常范围;
    public final float  左上肢肌肉含量_MIN;
    public final float  左上肢肌肉含量_MAX;
    public final float  左上肢肌肉含量_CUR;
    public final String 左上肢肌肉含量_RESULT;
    public final String 右上肢肌肉含量;
    public final String 右上肢肌肉含量正常范围;
    public final float  右上肢肌肉含量_MIN;
    public final float  右上肢肌肉含量_MAX;
    public final float  右上肢肌肉含量_CUR;
    public final String 右上肢肌肉含量_RESULT;
    public final String 左下肢肌肉含量;
    public final String 左下肢肌肉含量正常范围;
    public final float  左下肢肌肉含量_MIN;
    public final float  左下肢肌肉含量_MAX;
    public final float  左下肢肌肉含量_CUR;
    public final String 左下肢肌肉含量_RESULT;
    public final String 右下肢肌肉含量;
    public final String 右下肢肌肉含量正常范围;
    public final float  右下肢肌肉含量_MIN;
    public final float  右下肢肌肉含量_MAX;
    public final float  右下肢肌肉含量_CUR;
    public final String 右下肢肌肉含量_RESULT;

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
    public final float  细胞外液_MIN;
    public final float  细胞外液_MAX;
    public final float  细胞外液_CUR;
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
    /** 体重调节量 */
    public final int    体重_REG;
    public final String 体重调节;
    /** 脂肪调节量 */
    public final int    脂肪量_REG;
    public final String 脂肪调节;
    /** 肌肉调节量 */
    public final int    肌肉量_REG;
    public final String 肌肉调节;
    public final String 基础代谢量;
    public final String 总能量消耗;

    /* 从机回复语句 */
    public static final byte[] ACK = {(byte)0xCC, 0x00, 0x00, (byte)0xE1, (byte)0xDE, 0x00, (byte)0xFE};
    public static final int ACK_START = 0;
    /** 只有前3位是固定的数值 */
    public static final int ACK_PIPEI_LENGTH = 3;
    public static final byte[] ACK_PIPEI = {(byte)0xCC, 0x00, 0x00};
    /* ACK坐标3处是数据长度:包含ACK和DATA的长度 */
    public static final int LENGTH_START = 3;
    public static final int ACK_LENGTH = ACK.length;
    public static final int DATA_START = ACK.length;
    public static final int DATA_LENGTH = 218;
    public static final int VERIFICATION_START = 223;
    public static final int VERIFICATION_LENGTH = 4;
    public static int 结束符_START; //        = ACK_LENGTH + DATA_LENGTH;
    public static int 校验和_START; //        = 结束符_START + 1;
    public static final int TOTAL_LENGTH = ACK_LENGTH + DATA_LENGTH + VERIFICATION_LENGTH; // 7+218+4=229
    /** 新的数据长度，从数据中提取+检验位的总的数据长度 */
    public static int TOTAL_LENGTH_NEW = 0; // 7+218+4=229

    /** 需要校验数据起始坐标 */
    public static int NEED_CHECKSUM_DATA_START = 1; // 7+218+4=229
    /** 需要校验数据长度 */
    public static int NEED_CHECKSUM_DATA_LENGTH; // TOTAL_LENGTH_NEW - 2;

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
    public static final int 左上肢肌肉正常范围_START       = 120;
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
    public static final int 左上肢肌肉正常范围_LENGTH       = 4;
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

    /** 结束符默认值 */
    public static final byte 结束符_DEF = (byte) 0xDD;

    /** 对一些参数进行补救 */
    /* 1. 添加 */
    public static final int 体型分析_X_MIN = 0;
    public static final int 体型分析_Y_MIN = 0;
    public static final int 体型分析_X_MAX = 3;
    public static final int 体型分析_Y_MAX = 4;

    /* 水肿分析 */
    public String 身体水分率;
    public String 水肿系数;
    public String 细胞外液结果 = ""; // 干燥，正常，水肿；

    /* 位置 */
    /**
     * This class specifies a supported media size. Media size is the
     * dimension of the media on which the content is printed. For
     * example, the media size designates a page
     * with size 8.5" x 11".
     */
    public static final class Position {
        private static final String LOG_TAG = "Posistion";
        static int VALUE_72_X_1MM = 2836;
        static int LINE1_Y = 38;
        static int LINE2_Y = 47;
        static int COW1_X = 26;
        static int COW2_X = 60; //70;
        static int COW3_X = 100; // 110;

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Position 姓名 =
                new Position(COW1_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        public static final Position 身高 =
                new Position(COW2_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        public static final Position 体重1 =
                new Position(COW2_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);

        /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
        public static final Position 测试日期 =
                new Position(COW3_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 35 * VALUE_72_X_1MM, 46810);
        public static final Position 年龄 =
                new Position(COW3_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        public static final Position 性别 =
                new Position(COW1_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);

        /* 2X. 体成分结果 */
        // 21 体重
        public static final int 体成分结果_X = 30 * VALUE_72_X_1MM;
        public static final int 体成分结果_X_2 = 14 * VALUE_72_X_1MM;
        public static final int 体成分结果_Y_BASE = 65 * VALUE_72_X_1MM;
        public static final int 体成分结果_X_RANGE = (int)(22 * VALUE_72_X_1MM);
        public static final int 体成分结果_Y_RANGE = (int)(6.1 * VALUE_72_X_1MM);
        public static final Position 体重2 =
                new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 0, 85 * VALUE_72_X_1MM, 46810);
        // 22 去脂肪体重
        public static final Position 去脂肪体重 =
                new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 85 * VALUE_72_X_1MM, 46810);
        // 23 肌肉量
        public static final Position 肌肉量 =
                new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 85 * VALUE_72_X_1MM, 46810);
        // 24 身体总水分
        public static final Position 身体总水分 =
                new Position(体成分结果_X - 8 * VALUE_72_X_1MM, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 85 * VALUE_72_X_1MM, 46810);
        // 25 细胞内液 ? 4 or 5
        public static final Position 细胞内液 =
                new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 0, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
        // 26 细胞外液
        public static final Position 细胞外液 =
                new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 1, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
        // 27 蛋白质量
        public static final Position 蛋白质量 =
                new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 2, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 20 * VALUE_72_X_1MM, 46810);
        // 28 无机盐量
        public static final Position 无机盐量 =
                new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 3, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 20 * VALUE_72_X_1MM, 46810);
        // 29 体脂肪量
        public static final Position 体脂肪量 =
                new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 4, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 20 * VALUE_72_X_1MM, 46810);

        /* 3X. 体成分分析 */
        public static final int 体成分分析_X = 33 * VALUE_72_X_1MM;
        public static final int 体成分分析_Y_BASE = 217 * VALUE_72_X_1MM;
        public static final int 体成分分析_Y_RANGE = (int)(5.4 * VALUE_72_X_1MM);
        // 32 体重
        public static final Position 体成分分析_体重 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 0, 33110, 46810);
        // 32 身体质量
        public static final Position 体成分分析_身体质量 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 1, 33110, 46810);
        // 33 体脂肪率
        public static final Position 体成分分析_体脂肪率 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 2, 33110, 46810);
        // 34 体脂肪量
        public static final Position 体成分分析_体脂肪量 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 3, 33110, 46810);
        // 35 肌肉量
        public static final Position 体成分分析_肌肉量 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 4, 33110, 46810);
        // 36 身体水分
        public static final Position 体成分分析_身体水分 =
                new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 5, 33110, 46810);
        // 37 内脏脂肪
        public static final Position 体成分分析_内脏脂肪 =
                new Position(131* VALUE_72_X_1MM, 152 * VALUE_72_X_1MM, 33110, 46810);

        /* 4X. 调节建议 */
        // 41 体重_标准
        static int 调节建议_X_POS_BASE = 153 * VALUE_72_X_1MM;
        static int 调节建议_X_POS_RANGE = (int) (17 * VALUE_72_X_1MM);
        static int 调节建议_Y_POS_BASE = 95 * VALUE_72_X_1MM;
        static int 调节建议_Y_POS_RANGE = (int) (9.5 * VALUE_72_X_1MM);
        public static final Position 体重_标准 =
                new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 42 体重_当前
        public static final Position 体重_当前 =
                new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 43 体重_调试量
        public static final Position 体重_调节量 =
                new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 44 身体脂肪量_标准
        public static final Position 身体脂肪量_标准 =
                new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 45 身体脂肪量_当前
        public static final Position 身体脂肪量_当前 =
                new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 46 身体脂肪量_调试量
        public static final Position 身体脂肪量_调节量 =
                new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 47 肌肉量_标准
        public static final Position 肌肉量_标准 =
                new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 48 肌肉量_当前
        public static final Position 肌肉量_当前 =
                new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 49 肌肉量_调节量
        public static final Position 肌肉量_调节量 =
                new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);

        /* 5x 节段肌肉 */
        // 51 左上肢肌肉含量
        static int 节段肌肉_X_POS_BASE = 30 * VALUE_72_X_1MM;
        static int 节段肌肉_X_POS_RANGE = (int) (70 * VALUE_72_X_1MM);
        static int 节段肌肉_Y_POS_BASE = 163 * VALUE_72_X_1MM;
        static int 节段肌肉_Y_POS_RANGE_LITTLE = (int) (4.5 * VALUE_72_X_1MM);
        static int 节段肌肉_Y_POS_RANGE_LARGE  = (int) (13 * VALUE_72_X_1MM);
        public static final Position 左上肢肌肉含量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);
        // 52 左下肢肌肉含量
        public static final Position 左下肢肌肉含量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 53 右上肢肌肉含量
        public static final Position 右上肢肌肉含量 =
                new Position(
                        节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 54 右下肢肌肉含量
        public static final Position 右下肢肌肉含量 =
                new Position(
                        节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 55 躯干肌肉含量
        public static final Position 躯干肌肉含量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 1 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        /* 6x 节段脂肪 */
        // 61 左上肢脂肪量
        public static final Position 左上肢脂肪量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 62 左下肢脂肪量
        public static final Position 左下肢脂肪量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);
        // 63 右上肢脂肪量
        public static final Position 右上肢脂肪量 =
                new Position(
                        节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 64 右下肢脂肪量
        public static final Position 右下肢脂肪量 =
                new Position(
                        节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        // 65 躯干脂肪量
        public static final Position 躯干肢脂肪量 =
                new Position(
                        节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                        节段肌肉_Y_POS_BASE + 1 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                        33110, 46810);

        /* 7x 节段电阻抗（深度科研、科研数据） */
        static int 节段电阻抗_X_POS_BASE = 18 * VALUE_72_X_1MM;
        static int 节段电阻抗_X_POS_RANGE = (int) (18 * VALUE_72_X_1MM);
        static int 节段电阻抗_Y_POS_BASE = 269 * VALUE_72_X_1MM;
        static int 节段电阻抗_Y_POS_RANGE = (int) (5.5 * VALUE_72_X_1MM);
        // 71.频率
        public static final Position 频率_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 频率_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 频率_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 72.右上肢 x+16
        public static final Position 右上肢_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 右上肢_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 右上肢_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 73.左上肢 x+16
        public static final Position 左上肢_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 左上肢_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 左上肢_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 74.躯干 x+16
        public static final Position 躯干_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 躯干_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 躯干_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 75.右下肢 x+16
        public static final Position 右下肢_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 右下肢_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 右下肢_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 76.左下肢 x+16
        public static final Position 左下肢_5k =
                new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 左下肢_50k =
                new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        public static final Position 左下肢_250k =
                new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        /* 8x 肥胖评估(身体情况评估) */
        // 81.体重
        static int 肥胖评估_X_POS_BASE = 29 * VALUE_72_X_1MM;
        static int 肥胖评估_X_POS_RANGE = (int) (11.8 * VALUE_72_X_1MM);
        static int 肥胖评估_Y_POS_BASE = 117 * VALUE_72_X_1MM;
        static int 肥胖评估_Y_POS_RANGE = (int) (11 * VALUE_72_X_1MM);
        // 71.频率
        public static final Position 肥胖评估_体重_不足 =
                new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_体重_正常 =
                new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_体重_过量 =
                new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        // 82.脂肪量
        public static final Position 肥胖评估_脂肪量_不足 =
                new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_脂肪量_正常 =
                new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_脂肪量_过量 =
                new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        // 83.肌肉量
        public static final Position 肥胖评估_肌肉量_不足 =
                new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_肌肉量_正常 =
                new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 肥胖评估_肌肉量_过量 =
                new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        /* 9x 营养评估 */
        // 91.蛋白质
        static int 营养评估_X_POS_BASE = 149 * VALUE_72_X_1MM;
        static int 营养评估_X_POS_RANGE = (int) (19 * VALUE_72_X_1MM);
        static int 营养评估_Y_POS_BASE = 38 * VALUE_72_X_1MM;
        static int 营养评估_Y_POS_RANGE = (int) (9.5 * VALUE_72_X_1MM);
        public static final Position 营养评估_蛋白质_不足 =
                new Position(
                        营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        public static final Position 营养评估_蛋白质_正常 =
                new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        public static final Position 营养评估_蛋白质_过量 =
                new Position(
                        营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        // 92.无机盐
        public static final Position 营养评估_无机盐_不足 =
                new Position(
                        营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        public static final Position 营养评估_无机盐_正常 =
                new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        public static final Position 营养评估_无机盐_过量 =
                new Position(
                        营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        // 93.基础代谢量
        public static final Position 基础代谢量 =
                new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 2 * 营养评估_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);
        // 94.总能量消耗
        public static final Position 总能量消耗 =
                new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 3 * 营养评估_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);
        // 95.身体年龄
        public static final Position 身体年龄 =
                new Position(157 * VALUE_72_X_1MM, 277 * VALUE_72_X_1MM, 12 * VALUE_72_X_1MM, 46810);
        // 10x.健康评估
        public static final Position 健康评估 =
                new Position(157 * VALUE_72_X_1MM, 269 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);

        // 11x.水肿分析
        public static final Position 水肿分析_身体水分率 =
                new Position(88 * VALUE_72_X_1MM, 118 * VALUE_72_X_1MM, 12 * VALUE_72_X_1MM, 46810);
        // 11.细胞内液
        public static final Position 水肿分析_细胞内液 =
                new Position(99 * VALUE_72_X_1MM, 125 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        // 11.水肿系数
        public static final Position 水肿分析_水肿系数 =
                new Position(93 * VALUE_72_X_1MM, 130 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        // 11.细胞外液
        public static final Position 水肿分析_细胞外液 =
                new Position((int)(90.5 * VALUE_72_X_1MM), (int) (140 * VALUE_72_X_1MM), 18 * VALUE_72_X_1MM, 46810);
        // 11.细胞外液
        public static final Position 水肿分析_细胞外液_干燥 =
                new Position(108 * VALUE_72_X_1MM, 118 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        public static final Position 水肿分析_细胞外液_正常 =
                new Position(108 * VALUE_72_X_1MM, (int)(118 + 1*11.5) * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        public static final Position 水肿分析_细胞外液_浮肿 =
                new Position(108 * VALUE_72_X_1MM, (int)(118 + 2*11.5) * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);



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

        // 0. 姓名 编号 3号 注：十进制数不显示无意义的0，除非0号，“0002”改为“2”
        byte[] b;
        b = Arrays.copyOfRange(data, 姓名_START, 姓名_START + 姓名_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        姓名 = String.valueOf(tmpInt);

        // 1. 身高 180.0cm 保留一位小数
        b = Arrays.copyOfRange(data, 身高_START, 身高_START + 身高_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        身高 = String.format("%.1f", tmpFloat);

        // 2. 体重 80.8kg
        b = Arrays.copyOfRange(data, 体重1_START, 体重1_START + 体重1_LENGTH);
        体重 = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
        体重1 = String.format("%.1f", 体重 / 10);

        // 3. 性别
        SEX = data[性别_START];
        性别 = SEX == MALE ? "男" : "女";

        // 4. 年龄
        年龄 = String.valueOf(data[年龄_START]);

        // 5. 身体电阻值
        b = Arrays.copyOfRange(data, 身体电阻值_START, 身体电阻值_START + 身体电阻值_LENGTH);
        tmpFloat = ((ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f);
        身体电阻值 = String.format("%.1f", tmpFloat);

        // 6. _50k下la电阻值
        b = Arrays.copyOfRange(data, _50k下la电阻值_START, _50k下la电阻值_START + _50k下la电阻值_LENGTH);
        tmpFloat = ((ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f);
        _50k下la电阻值 = String.format("%.1f", tmpFloat);

        // 7. _50k下ra电阻值
        b = Arrays.copyOfRange(data, _50k下ra电阻值_START, _50k下ra电阻值_START + _50k下ra电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _50k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 8. _50k下tr电阻值
        b = Arrays.copyOfRange(data, _50k下tr电阻值_START, _50k下tr电阻值_START + _50k下tr电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _50k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 9. _50k下ll电阻值
        b = Arrays.copyOfRange(data, _50k下ll电阻值_START, _50k下ll电阻值_START + _50k下ll电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _50k下ll电阻值 = String.format("%.1f", tmpFloat);

        // 10. _50k下rl电阻值
        b = Arrays.copyOfRange(data, _50k下rl电阻值_START, _50k下rl电阻值_START + _50k下rl电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _50k下rl电阻值 = String.format("%.1f", tmpFloat);

        // 11. _5k下la电阻值
        b = Arrays.copyOfRange(data, _5k下la电阻值_START, _5k下la电阻值_START + _5k下la电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _5k下la电阻值 = String.format("%.1f", tmpFloat);

        // 12. _5k下ra电阻值
        b = Arrays.copyOfRange(data, _5k下ra电阻值_START, _5k下ra电阻值_START + _5k下ra电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _5k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 13. _5k下tr电阻值
        b = Arrays.copyOfRange(data, _5k下tr电阻值_START, _5k下tr电阻值_START + _5k下tr电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _5k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 14. _5k下ll电阻值
        b = Arrays.copyOfRange(data, _5k下ll电阻值_START, _5k下ll电阻值_START + _5k下ll电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _5k下ll电阻值 = String.format("%.1f", tmpFloat);

        // 15. _5k下rl电阻值
        b = Arrays.copyOfRange(data, _5k下rl电阻值_START, _5k下rl电阻值_START + _5k下rl电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _5k下rl电阻值 = String.format("%.1f", tmpFloat);

        // 16. _250k下la电阻值
        b = Arrays.copyOfRange(data, _250k下la电阻值_START, _250k下la电阻值_START + _250k下la电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _250k下la电阻值 = String.format("%.1f", tmpFloat);

        // 17. _250k下ra电阻值
        b = Arrays.copyOfRange(data, _250k下ra电阻值_START, _250k下ra电阻值_START + _250k下ra电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _250k下ra电阻值 = String.format("%.1f", tmpFloat);

        // 18. _250k下tr电阻值
        b = Arrays.copyOfRange(data, _250k下tr电阻值_START, _250k下tr电阻值_START + _250k下tr电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _250k下tr电阻值 = String.format("%.1f", tmpFloat);

        // 19. _250k下ll电阻值
        b = Arrays.copyOfRange(data, _250k下ll电阻值_START, _250k下ll电阻值_START + _250k下ll电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _250k下ll电阻值 = String.format("%.1f", tmpFloat);


        // 20. _250k下rl电阻值
        b = Arrays.copyOfRange(data, _250k下rl电阻值_START, _250k下rl电阻值_START + _250k下rl电阻值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        _250k下rl电阻值 = String.format("%.1f", tmpFloat);


        // 21. 测试时间(默认：2016-01-24 13:42)
        b = Arrays.copyOfRange(data, DATE_START, DATE_START + DATE_LENGTH);
        测试日期 = "20" + new String(new byte[]{b[0],b[1]}) + "-" + /* 年 */
                new String(new byte[]{b[2],b[3]}) + "-" +   /* 月 */
                new String(new byte[]{b[4],b[5]}) + " " +   /* 日 */
                new String(new byte[]{b[6],b[7]}) + ":" +   /* 时 */
                new String(new byte[]{b[8],b[9]});          /* 分 */


        // 22. 体重 80.8kg 注:当前值为串口发出的实际值
        b = Arrays.copyOfRange(data, 体重2_START, 体重2_START + 体重2_LENGTH);
        体重_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        tmpFloat = (float) 体重_CUR / 10f;
        体重2 = String.format("%.1f", tmpFloat);

        // 23. 体重标准范围
        b = Arrays.copyOfRange(data, 体重标准范围_START, 体重标准范围_START + 体重标准范围_LENGTH);
        体重_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        体重_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        体重标准范围 = String.format("%.1f-%.1f", (float) 体重_MIN / 10f, (float) 体重_MAX / 10f);

        // 24. 体重标准值
        b = Arrays.copyOfRange(data, 体重标准值_START, 体重标准值_START + 体重标准值_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        体重标准值 = String.format("%.1f", tmpFloat);

        // 25. 去脂肪体重
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 26. 去脂肪体重标准
        b = Arrays.copyOfRange(data, 去脂肪体重标准_START, 去脂肪体重标准_START + 去脂肪体重标准_LENGTH);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        去脂肪体重标准 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 27. 体脂肪量 注:当前值为串口发出的实际值
        b = Arrays.copyOfRange(data, 体脂肪量_START, 体脂肪量_START + 体脂肪量_LENGTH);
        体脂肪量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        体脂肪量 = String.format("%.1f", (float) 体脂肪量_CUR / 10f);

        // 28. 体脂肪量标准
        b = Arrays.copyOfRange(data, 体脂肪量标准_START, 体脂肪量标准_START + 体脂肪量标准_LENGTH);
        体脂肪量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        体脂肪量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        体脂肪量标准 = String.format("%.1f-%.1f", (float) 体脂肪量_MIN / 10f, (float) 体脂肪量_MAX / 10f);
        // 注：身体脂肪量标准值为标准值下界
        体脂肪量_STD = 体脂肪量_MIN;

        // 29. 肌肉量 注:当前值为串口发出的实际值
        b = Arrays.copyOfRange(data, 肌肉量_START, 肌肉量_START + 肌肉量_LENGTH);
        肌肉量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        肌肉量 = String.format("%.1f", (float) 肌肉量_CUR / 10f);

        // 30. 肌肉标准
        b = Arrays.copyOfRange(data, 肌肉标准_START, 肌肉标准_START + 肌肉标准_LENGTH);
        肌肉量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        肌肉量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        肌肉标准 = String.format("%.1f-%.1f", (float) 肌肉量_MIN / 10f, (float) 肌肉量_MAX / 10f);
        // 肌肉量标准值为标准值上界
        肌肉量_STD = 肌肉量_MAX;

        // 31. 躯干脂肪 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干脂肪_START, 躯干脂肪_START + 躯干脂肪_LENGTH);
        躯干脂肪_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干脂肪 = String.format("%.2f", 躯干脂肪_CUR);

        // 32. 躯干脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干脂肪标准_START, 躯干脂肪标准_START + 躯干脂肪标准_LENGTH);
        躯干脂肪_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干脂肪_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干脂肪_RESULT = getResultFromRange(躯干脂肪_CUR, 躯干脂肪_MIN, 躯干脂肪_MAX);
        躯干脂肪标准 = String.format("%.2f-%.2f", 躯干脂肪_MIN, 躯干脂肪_MAX);

        // 33. 左上肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢脂肪量_START, 左上肢脂肪量_START + 左上肢脂肪量_LENGTH);
        左上肢脂肪量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢脂肪量 = String.format("%.2f", 左上肢脂肪量_CUR);

        // 34. 左上肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢脂肪标准_START, 左上肢脂肪标准_START + 左上肢脂肪标准_LENGTH);
        左上肢脂肪量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢脂肪量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢脂肪量_RESULT = getResultFromRange(左上肢脂肪量_CUR, 左上肢脂肪量_MIN, 左上肢脂肪量_MAX);
        左上肢脂肪标准 = String.format("%.2f-%.2f", 左上肢脂肪量_MIN, 左上肢脂肪量_MAX);

        // 35. 右上肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢脂肪量_START, 右上肢脂肪量_START + 右上肢脂肪量_LENGTH);
        右上肢脂肪量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢脂肪量 = String.format("%.2f", 右上肢脂肪量_CUR);

        // 36. 右上肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢脂肪标准_START, 右上肢脂肪标准_START + 右上肢脂肪标准_LENGTH);
        右上肢脂肪量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢脂肪量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢脂肪标准 = String.format("%.2f-%.2f", 右上肢脂肪量_MIN, 右上肢脂肪量_MAX);
        右上肢脂肪量_RESULT = getResultFromRange(右上肢脂肪量_CUR, 右上肢脂肪量_MIN, 右上肢脂肪量_MAX);

        // 37. 左下肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢脂肪量_START, 左下肢脂肪量_START + 左下肢脂肪量_LENGTH);
        左下肢脂肪量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢脂肪量 = String.format("%.2f", 左下肢脂肪量_CUR);

        // 38. 左下肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢脂肪标准_START, 左下肢脂肪标准_START + 左下肢脂肪标准_LENGTH);
        左下肢脂肪量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢脂肪量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢脂肪标准 = String.format("%.2f-%.2f", 左下肢脂肪量_MIN, 左下肢脂肪量_MAX);
        左下肢脂肪量_RESULT = getResultFromRange(左下肢脂肪量_CUR, 左下肢脂肪量_MIN, 左下肢脂肪量_MAX);

        // 39. 右下肢脂肪量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢脂肪量_START, 右下肢脂肪量_START + 右下肢脂肪量_LENGTH);
        右下肢脂肪量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢脂肪量 = String.format("%.2f", 右下肢脂肪量_CUR);

        // 40. 右下肢脂肪标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢脂肪标准_START, 右下肢脂肪标准_START + 右下肢脂肪标准_LENGTH);
        右下肢脂肪量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢脂肪量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢脂肪标准 = String.format("%.2f-%.2f", 右下肢脂肪量_MIN, 右下肢脂肪量_MAX);
        右下肢脂肪量_RESULT = getResultFromRange(右下肢脂肪量_CUR, 右下肢脂肪量_MIN, 右下肢脂肪量_MAX);

        // 41. 内脏脂肪指数
        b = Arrays.copyOfRange(data, 内脏脂肪指数_START, 内脏脂肪指数_START + 内脏脂肪指数_LENGTH);
        内脏脂肪_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        内脏脂肪指数 = String.format("%.1f", (float) 内脏脂肪_CUR / 10f);

        // 42. 内脏指数正常范围
        b = Arrays.copyOfRange(data, 内脏指数正常范围_START, 内脏指数正常范围_START + 内脏指数正常范围_LENGTH);
        内脏脂肪_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        内脏脂肪_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        内脏指数正常范围 = String.format("%.1f-%.1f", (float) 内脏脂肪_MIN / 10f, (float) 内脏脂肪_MAX / 10f);

        // 43. 躯干肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干肌肉_START, 躯干肌肉_START + 躯干肌肉_LENGTH);
        躯干肌肉含量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干肌肉含量 = String.format("%.2f", 躯干肌肉含量_CUR);

        // 44. 躯干肌肉标准 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 躯干肌肉标准_START, 躯干肌肉标准_START + 躯干肌肉标准_LENGTH);
        躯干肌肉含量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干肌肉含量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        躯干肌肉含量_RESULT = getResultFromRange(躯干肌肉含量_CUR, 躯干肌肉含量_MIN, 躯干肌肉含量_MAX);
        躯干肌肉含量正常范围 = String.format("%.2f-%.2f", 躯干肌肉含量_MIN, 躯干肌肉含量_MAX);

        // 45. 左上肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢肌肉含量_START, 左上肢肌肉含量_START + 左上肢肌肉含量_LENGTH);
        左上肢肌肉含量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢肌肉含量 = String.format("%.2f", 左上肢肌肉含量_CUR);

        // 46. 左上肢肌肉正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左上肢肌肉正常范围_START, 左上肢肌肉正常范围_START + 左上肢肌肉正常范围_LENGTH);
        左上肢肌肉含量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢肌肉含量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左上肢肌肉含量_RESULT = getResultFromRange(左上肢肌肉含量_CUR, 左上肢肌肉含量_MIN, 左上肢肌肉含量_MAX);
        左上肢肌肉含量正常范围 = String.format("%.2f-%.2f", 左上肢肌肉含量_MIN, 左上肢肌肉含量_MAX);

        // 47. 右上肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢肌肉含量_START, 右上肢肌肉含量_START + 右上肢肌肉含量_LENGTH);
        右上肢肌肉含量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢肌肉含量 = String.format("%.2f", 右上肢肌肉含量_CUR);

        // 48. 右上肢肌肉含量正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右上肢肌肉含量正常范围_START, 右上肢肌肉含量正常范围_START + 右上肢肌肉含量正常范围_LENGTH);
        右上肢肌肉含量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢肌肉含量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右上肢肌肉含量_RESULT = getResultFromRange(右上肢肌肉含量_CUR, 右上肢肌肉含量_MIN, 右上肢肌肉含量_MAX);
        右上肢肌肉含量正常范围 = String.format("%.2f-%.2f", 右上肢肌肉含量_MIN, 右上肢肌肉含量_MAX);

        // 49. 左下肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢肌肉含量_START, 左下肢肌肉含量_START +  左下肢肌肉含量_LENGTH);
        左下肢肌肉含量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢肌肉含量 = String.format("%.2f", 左下肢肌肉含量_CUR);

        // 50. 左下肢肌肉正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 左下肢肌肉正常范围_START, 左下肢肌肉正常范围_START + 左下肢肌肉正常范围_LENGTH);
        左下肢肌肉含量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢肌肉含量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        左下肢肌肉含量_RESULT = getResultFromRange(左下肢肌肉含量_CUR, 左下肢肌肉含量_MIN, 左下肢肌肉含量_MAX);
        左下肢肌肉含量正常范围 = String.format("%.2f-%.2f", 左下肢肌肉含量_MIN, 左下肢肌肉含量_MAX);

        // 51. 右下肢肌肉含量 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢肌肉含量_START, 右下肢肌肉含量_START + 右下肢肌肉含量_LENGTH);
        右下肢肌肉含量_CUR = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢肌肉含量 = String.format("%.2f", 右下肢肌肉含量_CUR);


        // 52. 右下肢肌肉含量正常范围 单位 kg 两位小数
        b = Arrays.copyOfRange(data, 右下肢肌肉含量正常范围_START, 右下肢肌肉含量正常范围_START + 右下肢肌肉含量正常范围_LENGTH);
        右下肢肌肉含量_MIN = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢肌肉含量_MAX = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 100f;
        右下肢肌肉含量_RESULT = getResultFromRange(右下肢肌肉含量_CUR, 右下肢肌肉含量_MIN, 右下肢肌肉含量_MAX);
        右下肢肌肉含量正常范围 = String.format("%.2f-%.2f", 右下肢肌肉含量_MIN, 右下肢肌肉含量_MAX);

        // 53. 身体总水分
        b = Arrays.copyOfRange(data, 身体总水分_START, 身体总水分_START + 身体总水分_LENGTH);
        身体总水分_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        身体总水分 = String.format("%.1f", (float) 身体总水分_CUR / 10f);

        // 54. 身体总水分正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 身体总水分正常范围_START, 身体总水分正常范围_START + 身体总水分正常范围_LENGTH);
        身体总水分_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        身体总水分_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        身体总水分正常范围 = String.format("%.1f-%.1f", (float)身体总水分_MIN / 10, (float)身体总水分_MAX / 10f);

        // 55. 蛋白质含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质含量_START, 蛋白质含量_START + 蛋白质含量_LENGTH);
        tmpFloat = (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        蛋白质含量 = String.format("%.1f", tmpFloat);

        // 56. 蛋白质正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质正常范围_START, 蛋白质正常范围_START + 蛋白质正常范围_LENGTH);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        蛋白质正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 57. 无机盐量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量_START, 无机盐含量_START + 无机盐含量_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        无机盐含量 = String.format("%.1f", tmpFloat);

        // 58. 无机盐含量正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量正常范围_START, 无机盐含量正常范围_START + 无机盐含量正常范围_LENGTH);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        无机盐含量正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 59. 细胞外液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液含量_START, 细胞外液含量_START + 细胞外液含量_LENGTH);
        细胞外液_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        细胞外液含量 = String.format("%.1f", 细胞外液_CUR / 10f);

        // 60. 细胞外液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液正常范围_START, 细胞外液正常范围_START + 细胞外液正常范围_LENGTH);
        细胞外液_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        细胞外液_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        细胞外液正常范围 = String.format("%.1f-%.1f", 细胞外液_MIN / 10f, 细胞外液_MAX / 10f);

        // 细胞外液结果
        if (细胞外液_CUR < 细胞外液_MIN) {
            细胞外液结果 = "干燥";
        } else if (细胞外液_CUR > 细胞外液_MAX) {
            细胞外液结果 = "浮肿";
        } else {
            细胞外液结果 = "正常";
        }

        // 61. 细胞内液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液含量_START, 细胞内液含量_START + 细胞内液含量_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        细胞内液含量 = String.format("%.1f", tmpFloat);

        // 62. 细胞内液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液正常范围_START, 细胞内液正常范围_START + 细胞内液正常范围_LENGTH);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        tmpStr = String.format("%.1f", tmpFloat);
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        细胞内液正常范围 = String.format("%s-%.1f", tmpStr, tmpFloat);

        // 63. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 64. BMI结果 注:将byte[]转化为short[]
        b = Arrays.copyOfRange(data, BMI结果_START, BMI结果_START + BMI结果_LENGTH);
        BMI结果 = new short[b.length / 2];
        ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(BMI结果);

        // 64. 身体质量
        b = Arrays.copyOfRange(data, 身体质量_START, 身体质量_START + 身体质量_LENGTH);
        身体质量_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;

        // 64. 身体质量范围
        b = Arrays.copyOfRange(data, 身体质量范围_START, 身体质量范围_START + 身体质量范围_LENGTH);
        身体质量_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        身体质量_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;

        // 65. 脂肪率
        b = Arrays.copyOfRange(data, 脂肪率_START, 脂肪率_START + 脂肪率_LENGTH);
        脂肪率_CUR = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;

        b = Arrays.copyOfRange(data, 脂肪率范围_START, 脂肪率范围_START + 脂肪率范围_LENGTH);
        脂肪率_MIN = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        脂肪率_MAX = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;

        // 66. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 67. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
//        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 68. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
//        去脂肪体重 = String.format("%d", tmpInt);

        // 69. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
//        去脂肪体重 = String.format("%d", tmpInt);

        // 70. 身体总评分 okay 保留一位小数
        b = Arrays.copyOfRange(data, 身体总评分_START, 身体总评分_START + 身体总评分_LENGTH);
        tmpFloat = (ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        身体总评分 = String.format("%.1f", tmpFloat);

        // 71. 身体年龄 okay 整数
        b = Arrays.copyOfRange(data, 身体年龄_START, 身体年龄_START + 身体年龄_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        身体年龄 = String.format("%d", tmpInt);

        // 72. 体重调节 okay 保留一位小数 kg 注：体重=肌肉调节值+脂肪调节值
        b = Arrays.copyOfRange(data, 体重调节_START, 体重调节_START + 体重调节_LENGTH);
        tmpStr = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) == 0x00 ? "-" : "+";
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        体重调节 = String.format("%s%.1f", tmpStr, tmpFloat);

        // 73. 脂肪调节 okay 保留一位小数 kg　注：脂肪=标准值-当前值（结果为+的，显示0）
        b = Arrays.copyOfRange(data, 脂肪调节_START, 脂肪调节_START + 脂肪调节_LENGTH);
        tmpStr = (ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) == 0x00 ? "-" : "+";
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        脂肪调节 = String.format("%s%.1f", tmpStr, tmpFloat);
        // 注: 脂肪=标准值-当前值（结果为+的，显示0）
        tmpInt = 体脂肪量_STD - 体脂肪量_CUR;
        脂肪量_REG = tmpInt > 0 ? 0 : tmpInt;

        // 74. 肌肉调节 okay 保留一位小数 kg 注：根据标准和当前值计算而非直接解析值
        b = Arrays.copyOfRange(data, 肌肉调节_START, 肌肉调节_START + 肌肉调节_LENGTH);
        tmpStr = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = (ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF) / 10f;
        肌肉调节 = String.format("%s%.1f", tmpStr, tmpFloat);
        // 注: 肌肉量 = 标准值-当前值（结果为-的，显示0）
        tmpInt = 肌肉量_STD - 肌肉量_CUR;
        肌肉量_REG = tmpInt < 0 ? 0 : tmpInt;

        // 注: 体重标准值=当前值+调节量值
        体重_REG = 肌肉量_REG + 脂肪量_REG;

        // 注: 体重标准值=当前值+调节量值
        体重_STD = 体重_CUR + 体重_REG;

        // 75. 基础代谢量 okay
        b = Arrays.copyOfRange(data, 基础代谢量_START, 基础代谢量_START + 基础代谢量_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        基础代谢量 = String.format("%d", tmpInt);

        // 76. 总能量消耗
        b = Arrays.copyOfRange(data, 总能量消耗_START, 总能量消耗_START + 总能量消耗_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF;
        总能量消耗 = String.format("%d", tmpInt);

        // 77. 水肿分析
        tmpFloat = (身体总水分_CUR / 体重) * 100;
        身体水分率 = String.format("%.2f", tmpFloat);
        tmpFloat = 细胞外液_CUR / 身体总水分_CUR;
        水肿系数 = String.format("%.2f", tmpFloat);
        Log.i(LOG_TAG, toString());
    }

    private static String getResultFromRange(float cur, float min, float max) {
        String ret = "";
        if (cur < min)
            ret = "低标准";
        else if (cur >= min && cur <= max)
            ret = "正常";
        else if (cur > max)
            ret = "高标准";
        return ret;
    }

    @Override
    public String toString() {
        return "BodyComposition{" +
                "姓名='" + 姓名 + '\'' +
                "\n身高='" + 身高 + '\'' +
                "\n体重1='" + 体重1 + '\'' +
                "\nSEX=" + SEX +
                "\n性别='" + 性别 + '\'' +
                "\n年龄='" + 年龄 + '\'' +
                "\n身体电阻值='" + 身体电阻值 + '\'' +
                "\n_50k下la电阻值='" + _50k下la电阻值 + '\'' +
                "\n_50k下ra电阻值='" + _50k下ra电阻值 + '\'' +
                "\n_50k下tr电阻值='" + _50k下tr电阻值 + '\'' +
                "\n_50k下ll电阻值='" + _50k下ll电阻值 + '\'' +
                "\n_50k下rl电阻值='" + _50k下rl电阻值 + '\'' +
                "\n_5k下la电阻值='" + _5k下la电阻值 + '\'' +
                "\n_5k下ra电阻值='" + _5k下ra电阻值 + '\'' +
                "\n_5k下tr电阻值='" + _5k下tr电阻值 + '\'' +
                "\n_5k下ll电阻值='" + _5k下ll电阻值 + '\'' +
                "\n_5k下rl电阻值='" + _5k下rl电阻值 + '\'' +
                "\n_250k下la电阻值='" + _250k下la电阻值 + '\'' +
                "\n_250k下ra电阻值='" + _250k下ra电阻值 + '\'' +
                "\n_250k下tr电阻值='" + _250k下tr电阻值 + '\'' +
                "\n_250k下ll电阻值='" + _250k下ll电阻值 + '\'' +
                "\n_250k下rl电阻值='" + _250k下rl电阻值 + '\'' +
                "\n测试日期='" + 测试日期 + '\'' +
                "\n体重_STD=" + 体重_STD +
                "\n体重_CUR=" + 体重_CUR +
                "\n体重2='" + 体重2 + '\'' +
                "\n体重_MIN=" + 体重_MIN +
                "\n体重_MAX=" + 体重_MAX +
                "\n体重标准范围='" + 体重标准范围 + '\'' +
                "\n体重标准值='" + 体重标准值 + '\'' +
                "\n去脂肪体重='" + 去脂肪体重 + '\'' +
                "\n去脂肪体重标准='" + 去脂肪体重标准 + '\'' +
                "\n体脂肪量='" + 体脂肪量 + '\'' +
                "\n体脂肪量标准='" + 体脂肪量标准 + '\'' +
                "\n体脂肪量_CUR=" + 体脂肪量_CUR +
                "\n体脂肪量_MIN=" + 体脂肪量_MIN +
                "\n体脂肪量_MAX=" + 体脂肪量_MAX +
                "\n体脂肪量_STD=" + 体脂肪量_STD +
                "\n肌肉量='" + 肌肉量 + '\'' +
                "\n肌肉量_CUR=" + 肌肉量_CUR +
                "\n肌肉量_MIN=" + 肌肉量_MIN +
                "\n肌肉量_MAX=" + 肌肉量_MAX +
                "\n肌肉量_STD=" + 肌肉量_STD +
                "\n肌肉标准='" + 肌肉标准 + '\'' +
                "\n躯干脂肪='" + 躯干脂肪 + '\'' +
                "\n躯干脂肪标准='" + 躯干脂肪标准 + '\'' +
                "\n躯干脂肪_MIN=" + 躯干脂肪_MIN +
                "\n躯干脂肪_MAX=" + 躯干脂肪_MAX +
                "\n躯干脂肪_CUR=" + 躯干脂肪_CUR +
                "\n躯干脂肪_RESULT='" + 躯干脂肪_RESULT + '\'' +
                "\n左上肢脂肪量='" + 左上肢脂肪量 + '\'' +
                "\n左上肢脂肪标准='" + 左上肢脂肪标准 + '\'' +
                "\n左上肢脂肪量_MIN=" + 左上肢脂肪量_MIN +
                "\n左上肢脂肪量_MAX=" + 左上肢脂肪量_MAX +
                "\n左上肢脂肪量_CUR=" + 左上肢脂肪量_CUR +
                "\n左上肢脂肪量_RESULT='" + 左上肢脂肪量_RESULT + '\'' +
                "\n右上肢脂肪量='" + 右上肢脂肪量 + '\'' +
                "\n右上肢脂肪标准='" + 右上肢脂肪标准 + '\'' +
                "\n右上肢脂肪量_MIN=" + 右上肢脂肪量_MIN +
                "\n右上肢脂肪量_MAX=" + 右上肢脂肪量_MAX +
                "\n右上肢脂肪量_CUR=" + 右上肢脂肪量_CUR +
                "\n右上肢脂肪量_RESULT='" + 右上肢脂肪量_RESULT + '\'' +
                "\n左下肢脂肪量='" + 左下肢脂肪量 + '\'' +
                "\n左下肢脂肪标准='" + 左下肢脂肪标准 + '\'' +
                "\n左下肢脂肪量_MIN=" + 左下肢脂肪量_MIN +
                "\n左下肢脂肪量_MAX=" + 左下肢脂肪量_MAX +
                "\n左下肢脂肪量_CUR=" + 左下肢脂肪量_CUR +
                "\n左下肢脂肪量_RESULT='" + 左下肢脂肪量_RESULT + '\'' +
                "\n右下肢脂肪量='" + 右下肢脂肪量 + '\'' +
                "\n右下肢脂肪标准='" + 右下肢脂肪标准 + '\'' +
                "\n右下肢脂肪量_MIN=" + 右下肢脂肪量_MIN +
                "\n右下肢脂肪量_MAX=" + 右下肢脂肪量_MAX +
                "\n右下肢脂肪量_CUR=" + 右下肢脂肪量_CUR +
                "\n右下肢脂肪量_RESULT='" + 右下肢脂肪量_RESULT + '\'' +
                "\n内脏脂肪指数='" + 内脏脂肪指数 + '\'' +
                "\n内脏指数正常范围='" + 内脏指数正常范围 + '\'' +
                "\n内脏脂肪_CUR=" + 内脏脂肪_CUR +
                "\n内脏脂肪_MAX=" + 内脏脂肪_MAX +
                "\n内脏脂肪_MIN=" + 内脏脂肪_MIN +
                "\n躯干肌肉含量_MIN=" + 躯干肌肉含量_MIN +
                "\n躯干肌肉含量_MAX=" + 躯干肌肉含量_MAX +
                "\n躯干肌肉含量_CUR=" + 躯干肌肉含量_CUR +
                "\n躯干肌肉含量_RESULT='" + 躯干肌肉含量_RESULT + '\'' +
                "\n躯干肌肉含量='" + 躯干肌肉含量 + '\'' +
                "\n躯干肌肉含量正常范围='" + 躯干肌肉含量正常范围 + '\'' +
                "\n左上肢肌肉含量='" + 左上肢肌肉含量 + '\'' +
                "\n左上肢肌肉含量正常范围='" + 左上肢肌肉含量正常范围 + '\'' +
                "\n左上肢肌肉含量_MIN=" + 左上肢肌肉含量_MIN +
                "\n左上肢肌肉含量_MAX=" + 左上肢肌肉含量_MAX +
                "\n左上肢肌肉含量_CUR=" + 左上肢肌肉含量_CUR +
                "\n左上肢肌肉含量_RESULT='" + 左上肢肌肉含量_RESULT + '\'' +
                "\n右上肢肌肉含量='" + 右上肢肌肉含量 + '\'' +
                "\n右上肢肌肉含量正常范围='" + 右上肢肌肉含量正常范围 + '\'' +
                "\n右上肢肌肉含量_MIN=" + 右上肢肌肉含量_MIN +
                "\n右上肢肌肉含量_MAX=" + 右上肢肌肉含量_MAX +
                "\n右上肢肌肉含量_CUR=" + 右上肢肌肉含量_CUR +
                "\n右上肢肌肉含量_RESULT='" + 右上肢肌肉含量_RESULT + '\'' +
                "\n左下肢肌肉含量='" + 左下肢肌肉含量 + '\'' +
                "\n左下肢肌肉含量正常范围='" + 左下肢肌肉含量正常范围 + '\'' +
                "\n左下肢肌肉含量_MIN=" + 左下肢肌肉含量_MIN +
                "\n左下肢肌肉含量_MAX=" + 左下肢肌肉含量_MAX +
                "\n左下肢肌肉含量_CUR=" + 左下肢肌肉含量_CUR +
                "\n左下肢肌肉含量_RESULT='" + 左下肢肌肉含量_RESULT + '\'' +
                "\n右下肢肌肉含量='" + 右下肢肌肉含量 + '\'' +
                "\n右下肢肌肉含量正常范围='" + 右下肢肌肉含量正常范围 + '\'' +
                "\n右下肢肌肉含量_MIN=" + 右下肢肌肉含量_MIN +
                "\n右下肢肌肉含量_MAX=" + 右下肢肌肉含量_MAX +
                "\n右下肢肌肉含量_CUR=" + 右下肢肌肉含量_CUR +
                "\n右下肢肌肉含量_RESULT='" + 右下肢肌肉含量_RESULT + '\'' +
                "\n身体总水分='" + 身体总水分 + '\'' +
                "\n身体总水分正常范围='" + 身体总水分正常范围 + '\'' +
                "\n身体总水分_CUR=" + 身体总水分_CUR +
                "\n身体总水分_MIN=" + 身体总水分_MIN +
                "\n身体总水分_MAX=" + 身体总水分_MAX +
                "\n蛋白质含量='" + 蛋白质含量 + '\'' +
                "\n蛋白质正常范围='" + 蛋白质正常范围 + '\'' +
                "\n无机盐含量='" + 无机盐含量 + '\'' +
                "\n无机盐含量正常范围='" + 无机盐含量正常范围 + '\'' +
                "\n细胞外液含量='" + 细胞外液含量 + '\'' +
                "\n细胞外液正常范围='" + 细胞外液正常范围 + '\'' +
                "\n细胞内液含量='" + 细胞内液含量 + '\'' +
                "\n细胞内液正常范围='" + 细胞内液正常范围 + '\'' +
                "\nBMI结果=" + Arrays.toString(BMI结果) +
                "\n身体质量_CUR=" + 身体质量_CUR +
                "\n身体质量_MAX=" + 身体质量_MAX +
                "\n身体质量_MIN=" + 身体质量_MIN +
                "\n脂肪率_CUR=" + 脂肪率_CUR +
                "\n脂肪率_MAX=" + 脂肪率_MAX +
                "\n脂肪率_MIN=" + 脂肪率_MIN +
                "\n身体总评分='" + 身体总评分 + '\'' +
                "\n身体年龄='" + 身体年龄 + '\'' +
                "\n体重_REG=" + 体重_REG +
                "\n体重调节='" + 体重调节 + '\'' +
                "\n脂肪量_REG=" + 脂肪量_REG +
                "\n脂肪调节='" + 脂肪调节 + '\'' +
                "\n肌肉量_REG=" + 肌肉量_REG +
                "\n肌肉调节='" + 肌肉调节 + '\'' +
                "\n基础代谢量='" + 基础代谢量 + '\'' +
                "\n总能量消耗='" + 总能量消耗 + '\'' +
                "\n身体水分率='" + 身体水分率 + '\'' +
                "\n水肿系数='" + 水肿系数 + '\'' +
                "\n细胞外液结果='" + 细胞外液结果 + '\'' +
                '}';
    }
}
