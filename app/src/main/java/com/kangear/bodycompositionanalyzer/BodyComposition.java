package com.kangear.bodycompositionanalyzer;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 16-6-22.
 */
public class BodyComposition {
    private static final String LOG_TAG = "BodyComposition";
    public static final String UNIT_KG     = "kg";
    public static final String UNIT_KCAL   = "kcal";
    public static final String UNIT_SCORE  = "分";
    public static final String UNIT_CM2    = "c㎡";
    public static final String UNIT_AGE    = "岁";
    public static final String UNIT_CM     = "cm";
    public static final String UNIT_EMPTY  = "";
    public static final String UNIT_R      = "欧";

    public final Third 性别        = new Third(20, 1, 0, UNIT_EMPTY);
    public final Third 年龄        = new Third(21, 1, 0, UNIT_AGE);
    public final Third 身高        = new Third(22, 2, 1, UNIT_CM);
    public final Third 体重        = new Third(24, 53, 55, 2, 1, UNIT_KG);
    public final Third _5k电阻     = new Third(26, 2, 1, UNIT_R);
    public final Third _50k电阻    = new Third(28, 2, 1, UNIT_R);
    public final Third _250k电阻   = new Third(44, 2, 1, UNIT_R);
    public final Third 去脂体重     = new Third(57, 59, 61, 2, 1, UNIT_KG);
    public final Third 体脂肪量    = new Third(63, 65, 67, 2, 1, UNIT_KG);
    public final Third 骨骼肌      = new Third(75, 77, 79, 2, 1, UNIT_KG);
    public final Third 身体水分    = new Third(81, 83, 85, 2, 1, UNIT_KG);
    public final Third 蛋白质      = new Third(87, 89, 91, 2, 1, UNIT_KG);
    public final Third 无机盐      = new Third(93, 94, 95, 1, 1, UNIT_KG);
    public final Third 左上肢肌肉量 = new Third(109, 110, 111, 1, 1, UNIT_KG);
    public final Third 右上肢肌肉量 = new Third(112, 113, 114, 1, 1, UNIT_KG);
    public final Third 躯干肌肉量   = new Third(115, 117, 119, 2, 1, UNIT_KG);
    public final Third 左下肌肉量   = new Third(121, 123, 125, 2, 1, UNIT_KG);
    public final Third 右下肌肉量   = new Third(127, 129, 131, 2, 1, UNIT_KG);
    public final Third 左上脂肪量   = new Third(133, 134, 135, 1, 1, UNIT_KG);
    public final Third 右上脂肪量   = new Third(136, 137, 138, 1, 1, UNIT_KG);
    public final Third 躯干脂肪量   = new Third(139, 141, 143, 2, 1, UNIT_KG);
    public final Third 左下脂肪量   = new Third(145, 146, 147, 1, 1, UNIT_KG);
    public final Third 右下脂肪量   = new Third(148, 149, 150, 1, 1, UNIT_KG);
    public final Third BMI        = new Third(157, 159, 161, 2, 1, UNIT_KG);
    public final Third 体脂百分比   = new Third(163, 165, 167, 2, 1, UNIT_KG);
    public final Third 腰臀比      = new Third(177, 178, 179, 1, 2, UNIT_KG);
    public final Third 内脏面积     = new Third(185, 2, 1, UNIT_CM2);
    public final Third 评分        = new Third(187, 2, 1, UNIT_SCORE);
    public final Third 基础代谢     = new Third(196, 2, 0, UNIT_KCAL);
    public final Third 总能耗       = new Third(198, 2, 0, UNIT_KCAL);
    public final List<Third> mList = new ArrayList<Third>();

    public static final int LEVEL_LOW    = 0;
    public static final int LEVEL_NORMAL = 1;
    public static final int LEVEL_HIGH   = 2;

    /**
     * 数据类
     */
    public static class Third {
        float cur;
        float min;
        float max;
        int curStart;
        int minStart;
        int maxStart;
        int length;
        int dot;
        String unit;

        /**
         * @param curStart
         * @param minStart
         * @param maxStart
         * @param length
         * @param dot　小数点几位
         */
        public Third(int curStart, int minStart, int maxStart, int length, int dot, @NonNull String unit) {
            this.curStart = curStart;
            this.minStart = minStart;
            this.maxStart = maxStart;
            this.length = length;
            this.dot = dot;
            this.unit = unit;
        }

        /**
         * @param curStart
         * @param length
         * @param dot　小数点几位
         */
        public Third(int curStart, int length, int dot, @NonNull String unit) {
            this.curStart = curStart;
            this.length = length;
            this.dot = dot;
            this.unit = unit;
        }

        public int getCurStart() {
            return curStart;
        }

        //直接还原真实的值
        public float getCur() {
            return (float) (cur / Math.pow(10, dot));
        }

        public void setCur(float cur) {
            this.cur = cur;
        }

        public float getMin() {
            return (float) (min / Math.pow(10, dot));
        }

        public void setMin(int min) {
            this.min = min;
        }

        public float getMax() {
            return (float) (max / Math.pow(10, dot));
        }

        public void setMax(int max) {
            this.max = max;
        }

        public void setCurStart(int curStart) {
            this.curStart = curStart;
        }

        public int getMinStart() {
            return minStart;
        }

        public void setMinStart(int minStart) {
            this.minStart = minStart;
        }

        public int getMaxStart() {
            return maxStart;
        }

        public void setMaxStart(int maxStart) {
            this.maxStart = maxStart;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getUnit() {
            return unit;
        }

        /**
         *
         * @param lessWidth
         * @param normalWidth
         * @param moreWidth
         * @return percent from Third obj
         */
        public int getProgress(float lessWidth, float normalWidth, float moreWidth) {
            float rate;
            int percent;
            float[] P_temp = new float[2];
            float Data_Test = getCur();
            float Data_Min = getMin();
            float Data_Max = getMax();

            P_temp[0] = Data_Min - ((Data_Max - Data_Min) * lessWidth / normalWidth);     //坐标最小值, 600
            P_temp[1] = Data_Max + ((Data_Max - Data_Min) * moreWidth / normalWidth);     //坐标最大值,1500

            float range = P_temp[1] - P_temp[0];                      //整体坐标代表的最大数值,900
            float position;
            // 如果小于最最小值，设定一默认值5
            if (Data_Test < P_temp[0]) //低于最小值坐标
                position = 5;
            else {
                position = Data_Test - P_temp[0];
                if (position > range) // 超出最大坐标，以最大为准
                    position = range;
            }
            rate = position / range;
            percent = (int) (rate * 100);
            // Log.i(LOG_TAG, "getProgressLength percent: " + percent + " rate: " + rate + " position: " + position + " range: " + range);
            return percent;
        }

        /**
         * @return level
         */
        public int getLevel() {
            float cur = getCur();
            float min = getMin();
            float max = getMax();

            if (cur < min) {
                return LEVEL_LOW;
            } else if (cur > max){
                return LEVEL_HIGH;
            } else {
                return LEVEL_NORMAL;
            }
        }

        /**
         * 低标准 正常 高标准
         * @return
         */
        public String getLevelAsChinese() {
            switch (getLevel()) {
                case LEVEL_LOW:
                    return "低标准";
                case LEVEL_NORMAL:
                    return "正常";
                case LEVEL_HIGH:
                    return "高标准";

            }
            return "正常";
        }

        @Override
        public String toString() {
            return "Third{" +
                    "cur=" + String.format("%02X", (int)cur) +
                    ", min=" + String.format("%02X", (int)min) +
                    ", max=" + String.format("%02X", (int)max) +
                    ", curStart=" + curStart +
                    ", minStart=" + minStart +
                    ", maxStart=" + maxStart +
                    ", length=" + length +
                    '}';
        }
    }

    /**
     * 脂肪调整量
     * @return
     */
    public float getZhifangAdjustment() {
        float ret = 体脂肪量.getCur() - 体脂肪量.getMin();
        return ret > 0 ? ret : 0;
    }

    /**
     * 肌肉调整量
     * @return
     */
    public float getJirouAdjustment() {
        float ret = 骨骼肌.getMax() - 骨骼肌.getCur();
        return ret > 0 ? ret : 0;
    }

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
     * will parse 1 or 2 length number from byte array
     * @param t
     * @param data
     * @return
     */
    boolean parse(Third t, byte[] data) {
        if (t == null) {
            return false;
        }

        if (t.getLength() == 1) {
            t.setCur(data[t.getCurStart()]);
            t.setMin(data[t.getMinStart()]);
            t.setMax(data[t.getMaxStart()]);
        } else if (t.getLength() == 2) {
            t.setCur(Protocol.getShortFromData(data, t.getCurStart()));
            t.setMin(Protocol.getShortFromData(data, t.getMinStart()));
            t.setMax(Protocol.getShortFromData(data, t.getMaxStart()));
        } else {
            return false;
        }

        return true;
    }

    /**
     * 从数据段进行解析各项参数
     *
     * @param data
     */
    public BodyComposition(final byte[] data) {
        mList.add(性别);
        mList.add(年龄);
        mList.add(身高);
        mList.add(体重);
        mList.add(_5k电阻);
        mList.add(_50k电阻);
        mList.add(_250k电阻);
        mList.add(体脂肪量);
        mList.add(骨骼肌);
        mList.add(身体水分);
        mList.add(蛋白质);
        mList.add(无机盐);
        mList.add(左上肢肌肉量);
        mList.add(右上肢肌肉量);
        mList.add(躯干肌肉量);
        mList.add(左下肌肉量);
        mList.add(右下肌肉量);
        mList.add(左上脂肪量);
        mList.add(右上脂肪量);
        mList.add(躯干脂肪量);
        mList.add(左下脂肪量);
        mList.add(右下脂肪量);
        mList.add(BMI);
        mList.add(体脂百分比);
        mList.add(腰臀比);
        mList.add(内脏面积);
        mList.add(评分);
        mList.add(基础代谢);
        mList.add(总能耗);
        mList.add(去脂体重);

        int i = 0;
        for (Third t : mList) {
            boolean ret = parse(t, data);
            if (!ret) {
                // TODO: 严肃处理
                Log.e(LOG_TAG, i + " parse error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                break;
            }
            Log.i(LOG_TAG, t.toString());
            i++;
        }
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

}
