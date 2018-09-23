package com.kangear.bca;

import android.util.Log;

/**
 * engseban
 */
public class Coordinate20170504 extends Coordinate {
    private static final String TAG = "Coordinate20170504";
    private static final int LINE1_Y = 38;
    private static final int LINE2_Y = 47;
    private static final int COW1_X = 26;
    private static final int COW2_X = 60; //70;
    private static final int COW3_X = 100; // 110;

    static {
        // n.n 体型分析
        /* 在A4纸上方块宽度 重新排版时要改这里 */
        SINGLE_RECT_WIDTH = 66/4; //16.8;
        /* 在A4纸上方块高度 */
        SINGLE_RECT_HEIGHT = 70/5; //13.4;
        /* 原点坐标 下面的交叉点为「体型分析」原点 */
        /*
         * |
         * |
         * |_________
         */
        ORIGIN_X = 132 - 3; //133;
        ORIGIN_Y = 138.5 + 2; //146;

        /* 第一行：姓名 测试日期 */
        姓名 = new Position(COW1_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        身高 = new Position(COW2_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        体重1 = new Position(COW2_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);

        /* 第二行：身高 年龄 */
        测试日期 = new Position(COW3_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 35 * VALUE_72_X_1MM, 46810);
        年龄 = new Position(COW3_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
        性别 = new Position(COW1_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);

        /* 2X. 体成分结果 */
        // 21 体重
        体成分结果_X         = 52 * VALUE_72_X_1MM;
        体成分结果_X_2       = 32 * VALUE_72_X_1MM;
        体成分结果_Y_BASE    = 74 * VALUE_72_X_1MM;
        体成分结果_X_RANGE   = (int)(18 * VALUE_72_X_1MM);
        体成分结果_Y_RANGE   = (int)(6.1 * VALUE_72_X_1MM);
        体重2     = new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 0, 85 * VALUE_72_X_1MM, 46810);
        // 22 去脂肪体重
        去脂肪体重 = new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 85 * VALUE_72_X_1MM, 46810);
        // 23 肌肉量
        肌肉量     = new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 85 * VALUE_72_X_1MM, 46810);
        // 24 身体总水分
        身体总水分 = new Position(体成分结果_X - 8 * VALUE_72_X_1MM, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 85 * VALUE_72_X_1MM, 46810);
        // 25 细胞内液 ? 4 or 5
        细胞内液   = new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 0, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
        // 26 细胞外液
        细胞外液   = new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 1, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
        // 27 蛋白质量
        蛋白质量   = new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 2, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 20 * VALUE_72_X_1MM, 46810);
        // 28 无机盐量
        无机盐量   = new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 3, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 20 * VALUE_72_X_1MM, 46810);
        // 29 体脂肪量
        体脂肪量   = new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 4, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 20 * VALUE_72_X_1MM, 46810);

        /* 3X. 体成分分析 */
        体成分分析_X = 35 * VALUE_72_X_1MM;
        体成分分析_Y_BASE = 138 * VALUE_72_X_1MM;
        体成分分析_Y_RANGE = (int)(9.5 * VALUE_72_X_1MM);

        // 32 体重
        体成分分析_体重    = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 0, 33110, 46810);
        // 32 身体质量
        体成分分析_身体质量 = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 1, 33110, 46810);
        // 33 体脂肪率
        体成分分析_体脂肪率 = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 2, 33110, 46810);
        // 34 体脂肪量
        体成分分析_体脂肪量 = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 3, 33110, 46810);
        // 35 肌肉量
        体成分分析_肌肉量   = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 4, 33110, 46810);
        // 36 身体水分
        体成分分析_身体水分 = new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 5, 33110, 46810);
        // 37 内脏脂肪
        体成分分析_内脏脂肪 = new Position((132 - 1)* VALUE_72_X_1MM, 270 * VALUE_72_X_1MM,  33110, 46810);

        // 表格尺寸
        体成分分析_TOTAL_LENGTH      = 89;
        内脏指数_TOTAL_LENGTH        = 66; //89;
        体成分分析_SECOND_START_MM   = 35.8f;
        体成分分析_SECOND_START_PX   = 体成分分析_SECOND_START_MM * VALUE_72_X_1MM;
        体成分分析_THIRD_START_MM    = 56.8f;
        体成分分析_THIRD_START_PX    = 体成分分析_THIRD_START_MM * VALUE_72_X_1MM;
        体成分分析_TOTAL_LENGTH_PX   = 体成分分析_TOTAL_LENGTH * VALUE_72_X_1MM;

        /* 4X. 调节建议 */
        // 41 体重_标准
        int 调节建议_X_POS_BASE = 151 * VALUE_72_X_1MM;
        int 调节建议_X_POS_RANGE = (int) (17 * VALUE_72_X_1MM);
        int 调节建议_Y_POS_BASE = 218 * VALUE_72_X_1MM;
        int 调节建议_Y_POS_RANGE = (int) (9.5 * VALUE_72_X_1MM);
        体重_标准 = new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 42 体重_当前
        体重_当前 = new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 43 体重_调试量
        体重_调节量 = new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 44 身体脂肪量_标准
        身体脂肪量_标准 = new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 45 身体脂肪量_当前
        身体脂肪量_当前 = new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 46 身体脂肪量_调试量
        身体脂肪量_调节量 = new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 47 肌肉量_标准
        肌肉量_标准 = new Position(
                        调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 48 肌肉量_当前
        肌肉量_当前 = new Position(
                        调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);
        // 49 肌肉量_调节量
        肌肉量_调节量 = new Position(
                        调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                        调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                        33110, 46810);

        /* 5x 节段肌肉 */
        // 51 左上肢肌肉含量
        int 节段肌肉_X_POS_BASE = -500 * VALUE_72_X_1MM;
        int 节段肌肉_X_POS_RANGE = (int) (28 * VALUE_72_X_1MM);
        int 节段肌肉_Y_POS_BASE = -500 * VALUE_72_X_1MM;
        int 节段肌肉_Y_POS_RANGE_LITTLE = (int) (4.5 * VALUE_72_X_1MM);
        int 节段肌肉_Y_POS_RANGE_LARGE  = (int) (13 * VALUE_72_X_1MM);
        左上肢肌肉含量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);
        // 52 左下肢肌肉含量
        左下肢肌肉含量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        // 53 右上肢肌肉含量
        右上肢肌肉含量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        // 54 右下肢肌肉含量
        右下肢肌肉含量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                        33110, 46810);

        // 55 躯干肌肉含量
        躯干肌肉含量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        /* 6x 节段脂肪 */
        // 61 左上肢脂肪量
        左上肢脂肪量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        // 62 左下肢脂肪量
        左下肢脂肪量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);
        // 63 右上肢脂肪量
        右上肢脂肪量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        // 64 右下肢脂肪量
        右下肢脂肪量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        // 65 躯干脂肪量
        躯干肢脂肪量 = new Position(
                -500 * VALUE_72_X_1MM,
                -500 * VALUE_72_X_1MM,
                33110, 46810);

        /* 7x 节段电阻抗（深度科研、科研数据） */
        int 节段电阻抗_X_POS_BASE = 18 * VALUE_72_X_1MM;
        int 节段电阻抗_X_POS_RANGE = (int) (18 * VALUE_72_X_1MM);
        int 节段电阻抗_Y_POS_BASE = 260 * VALUE_72_X_1MM;
        int 节段电阻抗_Y_POS_RANGE = (int) (5.5 * VALUE_72_X_1MM);
        // 71.频率
        频率_5k = new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        频率_50k = new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        频率_250k = new Position(
                        节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 72.右上肢 x+16
        右上肢_5k = new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        右上肢_50k = new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        右上肢_250k = new Position(
                        节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 73.左上肢 x+16
        左上肢_5k = new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        左上肢_50k = new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        左上肢_250k = new Position(
                        节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 74.躯干 x+16
        躯干_5k = new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        躯干_50k = new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        躯干_250k = new Position(
                        节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 75.右下肢 x+16
        右下肢_5k = new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        右下肢_50k = new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        右下肢_250k = new Position(
                        节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        // 76.左下肢 x+16
        左下肢_5k = new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        左下肢_50k = new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        左下肢_250k = new Position(
                        节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                        节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);

        /* 8x 肥胖评估(身体情况评估) */
        // 81.体重
        int 肥胖评估_X_POS_BASE = 48 * VALUE_72_X_1MM;
        int 肥胖评估_X_POS_RANGE = (int) (27 * VALUE_72_X_1MM);
        int 肥胖评估_Y_POS_BASE = 210 * VALUE_72_X_1MM;
        int 肥胖评估_Y_POS_RANGE = (int) (10 * VALUE_72_X_1MM);
        // 71.频率
        肥胖评估_体重_不足 = new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_体重_正常 = new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_体重_过量 = new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        // 82.脂肪量
        肥胖评估_脂肪量_不足 = new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_脂肪量_正常 = new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_脂肪量_过量 = new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        // 83.肌肉量
        肥胖评估_肌肉量_不足 = new Position(
                        肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_肌肉量_正常 = new Position(
                        肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        肥胖评估_肌肉量_过量 = new Position(
                        肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                        肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        /* 9x 营养评估 */
        // 91.蛋白质
        int 营养评估_X_POS_BASE = 148 * VALUE_72_X_1MM;
        int 营养评估_X_POS_RANGE = (int) (17.3 * VALUE_72_X_1MM);
        int 营养评估_Y_POS_BASE = 158 * VALUE_72_X_1MM;
        int 营养评估_Y_POS_RANGE = (int) (9 * VALUE_72_X_1MM);
        营养评估_蛋白质_不足 = new Position(
                        营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);

        营养评估_蛋白质_正常 = new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        营养评估_蛋白质_过量 = new Position(
                        营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        // 92.无机盐
        营养评估_无机盐_不足 = new Position(
                        营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        营养评估_无机盐_正常 = new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        营养评估_无机盐_过量 = new Position(
                        营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                        4 * VALUE_72_X_1MM, 46810);
        // 93.基础代谢量
        基础代谢量 = new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 2 * 营养评估_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);
        // 94.总能量消耗
        总能量消耗 = new Position(
                        营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                        营养评估_Y_POS_BASE + 3 * 营养评估_Y_POS_RANGE,
                        12 * VALUE_72_X_1MM, 46810);
        // 95.身体年龄
        身体年龄 = new Position(
                174 * VALUE_72_X_1MM,
                49 * VALUE_72_X_1MM,
                12 * VALUE_72_X_1MM, 46810);

        // 10x.健康评估
        健康评估 = new Position(174 * VALUE_72_X_1MM, 40 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);

        // 11x.水肿分析
        水肿分析_身体水分率 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 12 * VALUE_72_X_1MM, 46810);
        // 11.细胞内液
        水肿分析_细胞内液 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        // 11.水肿系数
        水肿分析_水肿系数 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        // 11.细胞外液
        水肿分析_细胞外液 = new Position((int)(-100 * VALUE_72_X_1MM), (int) (-100 * VALUE_72_X_1MM), 18 * VALUE_72_X_1MM, 46810);
        // 11.细胞外液
        水肿分析_细胞外液_干燥 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        水肿分析_细胞外液_正常 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
        水肿分析_细胞外液_浮肿 = new Position(-100 * VALUE_72_X_1MM, -100 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
    }


    // 表格尺寸
//    体成分分析_TOTAL_LENGTH      = 89;
//    内脏指数_TOTAL_LENGTH        = 89;
//    体成分分析_SECOND_START_MM   = 35.8f;
//    体成分分析_SECOND_START_PX   = 体成分分析_SECOND_START_MM * VALUE_72_X_1MM;
//    体成分分析_THIRD_START_MM    = 56.8f;
//    体成分分析_THIRD_START_PX    = 体成分分析_THIRD_START_MM * VALUE_72_X_1MM;
//    体成分分析_TOTAL_LENGTH_PX   = 体成分分析_TOTAL_LENGTH * VALUE_72_X_1MM;

    /**
     * 体成分分析　内脏脂肪　这个是一个比较复杂的计算方式
     * 由于在A4纸上的表格并没有按照比例进行划分，所以需要进行分段以及微调才能达到效果
     * @param bc BodyComposition
     * @return 进度条实际长度，单位Point
     */
    @Override
    public float getNeizangProgress(BodyComposition bc) {
        Log.i(TAG, "getNeizangProgress");
        if (true) // 新版本已经符合比例了，不需要复杂的分段计算了
            return (float) (65.5 / 17 * bc.内脏脂肪指数.getCur() * 2836 / 1000);
		float[] P_temp = new float[2];
		float cur = 0, min = 0, max = 0;
		cur = bc.内脏脂肪指数.getCur(); // 10
		min = bc.内脏脂肪指数.getMin(); // 最小1
		max = bc.内脏脂肪指数.getMax(); // 最大17
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

		Log.i(TAG, "r: " + r);

		return r * 2836 / 1000;
    }
}
