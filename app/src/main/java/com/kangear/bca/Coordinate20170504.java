package com.kangear.bca;

/**
 * engseban
 */
public class Coordinate20170504 extends Coordinate {
    static int LINE1_Y = 1;
    static int LINE2_Y = 47;
    static int COW1_X = 1;
    static int COW2_X = 60; //70;
    static int COW3_X = 100; // 110;

    static {

    /** 在A4纸上方块宽度 重新排版时要改这里 */
    SINGLE_RECT_WIDTH = 66/4; //16.8;
    /** 在A4纸上方块高度 */
    SINGLE_RECT_HEIGHT = 70/5; //13.4;
    /* 原点坐标 下面的交叉点为「体型分析」原点 */
    /**
     * |
     * |
     * |_________
     */
    ORIGIN_X = 132 ;// 132 - 3; //133;
    ORIGIN_Y = 246 ;//138.5 + 2; //146;

    /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
    姓名 =
            new Position(COW1_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
    身高 =
            new Position(COW2_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
    体重1 =
            new Position(COW2_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);

    /** 姓名 position: 841mm x 1189mm (33.11" x 46.81") */
    测试日期 =
            new Position(COW3_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 35 * VALUE_72_X_1MM, 46810);
    年龄 =
            new Position(COW3_X * VALUE_72_X_1MM, LINE1_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);
    性别 =
            new Position(COW1_X * VALUE_72_X_1MM, LINE2_Y * VALUE_72_X_1MM, 20 * VALUE_72_X_1MM, 46810);


    /* 2X. 体成分结果 */
    // 21 体重
    体成分结果_X = 30 * VALUE_72_X_1MM;
    体成分结果_X_2 = 14 * VALUE_72_X_1MM;
    体成分结果_Y_BASE = 65 * VALUE_72_X_1MM;
    体成分结果_X_RANGE = (int)(22 * VALUE_72_X_1MM);
    体成分结果_Y_RANGE = (int)(6.1 * VALUE_72_X_1MM);
    体重2 =
            new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 0, 85 * VALUE_72_X_1MM, 46810);
    // 22 去脂肪体重
    去脂肪体重 =
            new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 85 * VALUE_72_X_1MM, 46810);
    // 23 肌肉量
    肌肉量 =
            new Position(体成分结果_X, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 85 * VALUE_72_X_1MM, 46810);
    // 24 身体总水分
    身体总水分 =
            new Position(体成分结果_X - 8 * VALUE_72_X_1MM, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 85 * VALUE_72_X_1MM, 46810);
    // 25 细胞内液 ? 4 or 5
    细胞内液 =
            new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 0, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
    // 26 细胞外液
    细胞外液 =
            new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 1, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 4, 20 * VALUE_72_X_1MM, 46810);
    // 27 蛋白质量
    蛋白质量 =
            new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 2, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 3, 20 * VALUE_72_X_1MM, 46810);
    // 28 无机盐量
    无机盐量 =
            new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 3, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 2, 20 * VALUE_72_X_1MM, 46810);
    // 29 体脂肪量
    体脂肪量 =
            new Position(体成分结果_X_2 + 体成分结果_X_RANGE * 4, 体成分结果_Y_BASE + 体成分结果_Y_RANGE * 1, 20 * VALUE_72_X_1MM, 46810);

    /* 3X. 体成分分析 */
    体成分分析_X = 33 * VALUE_72_X_1MM;
    体成分分析_Y_BASE = 217 * VALUE_72_X_1MM;
    体成分分析_Y_RANGE = (int)(5.4 * VALUE_72_X_1MM);

    // 32 体重
    体成分分析_体重 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 0, 33110, 46810);
    // 32 身体质量
    体成分分析_身体质量 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 1, 33110, 46810);
    // 33 体脂肪率
    体成分分析_体脂肪率 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 2, 33110, 46810);
    // 34 体脂肪量
    体成分分析_体脂肪量 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 3, 33110, 46810);
    // 35 肌肉量
    体成分分析_肌肉量 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 4, 33110, 46810);
    // 36 身体水分
    体成分分析_身体水分 =
            new Position(体成分分析_X, 体成分分析_Y_BASE + 体成分分析_Y_RANGE * 5, 33110, 46810);
    // 37 内脏脂肪
    体成分分析_内脏脂肪 =
            new Position(131* VALUE_72_X_1MM, 152 * VALUE_72_X_1MM, 33110, 46810);

    /* 4X. 调节建议 */
    // 41 体重_标准
    int 调节建议_X_POS_BASE = 153 * VALUE_72_X_1MM;
    int 调节建议_X_POS_RANGE = (int) (17 * VALUE_72_X_1MM);
    int 调节建议_Y_POS_BASE = 95 * VALUE_72_X_1MM;
    int 调节建议_Y_POS_RANGE = (int) (9.5 * VALUE_72_X_1MM);
    体重_标准 =
            new Position(
                    调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 42 体重_当前
    体重_当前 =
            new Position(
                    调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 43 体重_调试量
    体重_调节量 =
            new Position(
                    调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 0 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 44 身体脂肪量_标准
    身体脂肪量_标准 =
            new Position(
                    调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 45 身体脂肪量_当前
    身体脂肪量_当前 =
            new Position(
                    调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 46 身体脂肪量_调试量
    身体脂肪量_调节量 =
            new Position(
                    调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 1 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 47 肌肉量_标准
    肌肉量_标准 =
            new Position(
                    调节建议_X_POS_BASE + 0 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 48 肌肉量_当前
    肌肉量_当前 =
            new Position(
                    调节建议_X_POS_BASE + 1 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                    33110, 46810);
    // 49 肌肉量_调节量
    肌肉量_调节量 =
            new Position(
                    调节建议_X_POS_BASE + 2 * 调节建议_X_POS_RANGE,
                    调节建议_Y_POS_BASE + 2 * 调节建议_Y_POS_RANGE,
                    33110, 46810);

    /* 5x 节段肌肉 */
    // 51 左上肢肌肉含量
    int 节段肌肉_X_POS_BASE = 30 * VALUE_72_X_1MM;
    int 节段肌肉_X_POS_RANGE = (int) (70 * VALUE_72_X_1MM);
    int 节段肌肉_Y_POS_BASE = 163 * VALUE_72_X_1MM;
    int 节段肌肉_Y_POS_RANGE_LITTLE = (int) (4.5 * VALUE_72_X_1MM);
    int 节段肌肉_Y_POS_RANGE_LARGE  = (int) (13 * VALUE_72_X_1MM);
    左上肢肌肉含量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);
    // 52 左下肢肌肉含量
    左下肢肌肉含量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 53 右上肢肌肉含量
    右上肢肌肉含量 =
            new Position(
                    节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 54 右下肢肌肉含量
    右下肢肌肉含量 =
            new Position(
                    节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 55 躯干肌肉含量
    躯干肌肉含量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 1 * 节段肌肉_Y_POS_RANGE_LARGE + 0 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    /* 6x 节段脂肪 */
    // 61 左上肢脂肪量
    左上肢脂肪量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 62 左下肢脂肪量
    左下肢脂肪量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);
    // 63 右上肢脂肪量
    右上肢脂肪量 =
            new Position(
                    节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 0 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 64 右下肢脂肪量
    右下肢脂肪量 =
            new Position(
                    节段肌肉_X_POS_BASE + 1 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 2 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    // 65 躯干脂肪量
    躯干肢脂肪量 =
            new Position(
                    节段肌肉_X_POS_BASE + 0 * 节段肌肉_X_POS_RANGE,
                    节段肌肉_Y_POS_BASE + 1 * 节段肌肉_Y_POS_RANGE_LARGE + 1 * 节段肌肉_Y_POS_RANGE_LITTLE,
                    33110, 46810);

    /* 7x 节段电阻抗（深度科研、科研数据） */
    int 节段电阻抗_X_POS_BASE = 18 * VALUE_72_X_1MM;
    int 节段电阻抗_X_POS_RANGE = (int) (18 * VALUE_72_X_1MM);
    int 节段电阻抗_Y_POS_BASE = 269 * VALUE_72_X_1MM;
    int 节段电阻抗_Y_POS_RANGE = (int) (5.5 * VALUE_72_X_1MM);
    // 71.频率
    频率_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    频率_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    频率_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 0 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    // 72.右上肢 x+16
    右上肢_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    右上肢_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    右上肢_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 1 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    // 73.左上肢 x+16
    左上肢_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    左上肢_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    左上肢_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 2 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    // 74.躯干 x+16
    躯干_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    躯干_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    躯干_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 3 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    // 75.右下肢 x+16
    右下肢_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    右下肢_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    右下肢_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 4 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    // 76.左下肢 x+16
    左下肢_5k =
            new Position(
                    节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 0 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    左下肢_50k =
            new Position(
                    节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 1 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    左下肢_250k =
            new Position(
                    节段电阻抗_X_POS_BASE + 5 * 节段电阻抗_X_POS_RANGE,
                    节段电阻抗_Y_POS_BASE + 2 * 节段电阻抗_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);

    /* 8x 肥胖评估(身体情况评估) */
    // 81.体重
    int 肥胖评估_X_POS_BASE = 29 * VALUE_72_X_1MM;
    int 肥胖评估_X_POS_RANGE = (int) (11.8 * VALUE_72_X_1MM);
    int 肥胖评估_Y_POS_BASE = 117 * VALUE_72_X_1MM;
    int 肥胖评估_Y_POS_RANGE = (int) (11 * VALUE_72_X_1MM);
    // 71.频率
    肥胖评估_体重_不足 =
            new Position(
                    肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_体重_正常 =
            new Position(
                    肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_体重_过量 =
            new Position(
                    肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 0 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    // 82.脂肪量
    肥胖评估_脂肪量_不足 =
            new Position(
                    肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_脂肪量_正常 =
            new Position(
                    肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_脂肪量_过量 =
            new Position(
                    肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 1 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    // 83.肌肉量
    肥胖评估_肌肉量_不足 =
            new Position(
                    肥胖评估_X_POS_BASE + 0 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_肌肉量_正常 =
            new Position(
                    肥胖评估_X_POS_BASE + 1 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    肥胖评估_肌肉量_过量 =
            new Position(
                    肥胖评估_X_POS_BASE + 2 * 肥胖评估_X_POS_RANGE,
                    肥胖评估_Y_POS_BASE + 2 * 肥胖评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    /* 9x 营养评估 */
    // 91.蛋白质
    int 营养评估_X_POS_BASE = 149 * VALUE_72_X_1MM;
    int 营养评估_X_POS_RANGE = (int) (19 * VALUE_72_X_1MM);
    int 营养评估_Y_POS_BASE = 38 * VALUE_72_X_1MM;
    int 营养评估_Y_POS_RANGE = (int) (9.5 * VALUE_72_X_1MM);
    营养评估_蛋白质_不足 =
            new Position(
                    营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);

    营养评估_蛋白质_正常 =
            new Position(
                    营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);
    营养评估_蛋白质_过量 =
            new Position(
                    营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 0 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);
    // 92.无机盐
    营养评估_无机盐_不足 =
            new Position(
                    营养评估_X_POS_BASE + 0 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);
    营养评估_无机盐_正常 =
            new Position(
                    营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);
    营养评估_无机盐_过量 =
            new Position(
                    营养评估_X_POS_BASE + 2 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 1 * 营养评估_Y_POS_RANGE,
                    4 * VALUE_72_X_1MM, 46810);
    // 93.基础代谢量
    基础代谢量 =
            new Position(
                    营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 2 * 营养评估_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);
    // 94.总能量消耗
    总能量消耗 =
            new Position(
                    营养评估_X_POS_BASE + 1 * 营养评估_X_POS_RANGE,
                    营养评估_Y_POS_BASE + 3 * 营养评估_Y_POS_RANGE,
                    12 * VALUE_72_X_1MM, 46810);
    // 95.身体年龄
    身体年龄 =
            new Position(157 * VALUE_72_X_1MM, 277 * VALUE_72_X_1MM, 12 * VALUE_72_X_1MM, 46810);
    // 10x.健康评估
    健康评估 =
            new Position(157 * VALUE_72_X_1MM, 269 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);

    // 11x.水肿分析
    水肿分析_身体水分率 =
            new Position(88 * VALUE_72_X_1MM, 118 * VALUE_72_X_1MM, 12 * VALUE_72_X_1MM, 46810);
    // 11.细胞内液
    水肿分析_细胞内液 =
            new Position(99 * VALUE_72_X_1MM, 125 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
    // 11.水肿系数
    水肿分析_水肿系数 =
            new Position(93 * VALUE_72_X_1MM, 130 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
    // 11.细胞外液
    水肿分析_细胞外液 =
            new Position((int)(90.5 * VALUE_72_X_1MM), (int) (140 * VALUE_72_X_1MM), 18 * VALUE_72_X_1MM, 46810);
    // 11.细胞外液
    水肿分析_细胞外液_干燥 =
            new Position(108 * VALUE_72_X_1MM, 118 * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
    水肿分析_细胞外液_正常 =
            new Position(108 * VALUE_72_X_1MM, (int)(118 + 1*11.5) * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);
    水肿分析_细胞外液_浮肿 =
            new Position(108 * VALUE_72_X_1MM, (int)(118 + 2*11.5) * VALUE_72_X_1MM, 18 * VALUE_72_X_1MM, 46810);

    }
}