package com.kangear.bodycompositionanalyzer;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.ResultActivity.MORE_LEVEL_WIDTH;
import static com.kangear.bodycompositionanalyzer.ResultActivity.NOMAL_LEVEL_WIDTH;

/**
 * Created by tony on 16-6-22.
 */
public class BodyComposition {
    private static final String TAG = "BodyComposition";
    private static final String LOG_TAG = "BodyComposition";
    public static final String UNIT_KG     = "kg";
    public static final String UNIT_KCAL   = "kcal";
    public static final String UNIT_SCORE  = "分";
    public static final String UNIT_CM2    = "c㎡";
    public static final String UNIT_AGE    = "岁";
    public static final String UNIT_CM     = "cm";
    public static final String UNIT_EMPTY  = "";
    public static final String UNIT_R      = "欧";
    public static final String UNIT_PERCENT= "%";
    public static final int INVALID_POSIONT = -1;

    public static final int GENDER_FEMALE = 0x00;
    public static final int GENDER_MALE   = 0x01;

    public static final byte[] TESTDATA = {
            0x55, (byte)0xAA, (byte)0xCD, 0x02, 0x31, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x01, 0x28, (byte)0xA4, 0x06, 0x20, 0x03, (byte)0x9D, 0x1A, 0x7D, 0x16,
            0x50, 0x00, 0x74, 0x0B, 0x48, 0x0B, 0x65, 0x01, (byte)0xF9, 0x09, 0x78, 0x09,
            0x13, 0x15, 0x53, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7B, 0x02, 0x16,
            0x02,(byte) 0xB5, 0x02, 0x33, 0x02, (byte)0xFC, 0x01, 0x1B, 0x02, (byte)0xED, 0x00, 0x5F,
            0x00, 0x7F, 0x00, 0x10, 0x02, (byte)0xD5, 0x01, (byte)0xF5, 0x01, 0x2F, 0x01, 0x0A,
            0x01, 0x4A, 0x01, (byte)0x99, 0x01, 0x6D, 0x01, (byte)0x84, 0x01, 0x77, 0x00, 0x64,
            0x00, 0x71, 0x00, 0x23, 0x24, 0x26, 0x00, (byte)0x81, 0x00, (byte)0x8F, 0x00, (byte)0xA3,
            0x00, 0x18, 0x01, (byte)0xF5, 0x00, 0x09, 0x01, 0x23, 0x1F, 0x21, 0x24, 0x1F,
            0x21, 0x19, 0x01, (byte)0xEA, 0x00, (byte)0xF9, 0x00, 0x56, 0x00, 0x56, 0x00, 0x5C,
            0x00, 0x5A, 0x00, 0x56, 0x00, 0x5C, 0x00, 0x0F, 0x06, 0x08, 0x0E, 0x06,
            0x08, (byte)0x8C, 0x00, 0x2F, 0x00, 0x3F, 0x00, 0x23, 0x11, 0x17, 0x21, 0x11,
            0x17, (byte)0xEB, 0x04, (byte)0x84, 0x03, 0x4C, 0x04, 0x14, 0x01, (byte)0xB9, 0x00, (byte)0xF0,
            0x00, 0x28, 0x01, (byte)0x96, 0x00, (byte)0xC8, 0x00, 0x40, 0x04, 0x3E, 0x04, 0x5D,
            0x04, (byte)0xB1, 0x03, 0x5D, 0x50, 0x5A, 0x1F, 0x1E, 0x23, 0x03, 0x04, (byte)0xF0,
            0x04, (byte)0xA4, 0x02, 0x2B, (byte)0xA5, (byte)0x80, 0x7E, (byte)0x80, 0x2B, (byte)0x80, 0x0F, 0x07,
            (byte)0xDC, 0x0A, (byte)0x81, (byte)0xA8
    };

    public static final Third 性别        = new Third("性别", 20, INVALID_POSIONT, INVALID_POSIONT,1, 0, UNIT_EMPTY);
    public static final Third 年龄        = new Third("年龄", 21, INVALID_POSIONT, INVALID_POSIONT,1, 0, UNIT_AGE);
    public static final Third 身高        = new Third("身高", 22, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_CM);
    public static final Third 体重        = new Third("体重", 24, 53, 55, 2, 1, UNIT_KG);
    public static final Third _5k电阻     = new Third("_5k电阻", 26, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
    public static final Third _50k电阻    = new Third("_50k电阻", 28, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
    public static final Third _250k电阻   = new Third("_250k电阻", 44, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
    public static final Third 去脂体重     = new Third("去脂体重", 57, 59, 61, 2, 1, UNIT_KG);
    public static final Third 体脂肪量    = new Third("体脂肪量", 63, 65, 67, 2, 1, UNIT_KG);
    public static final Third 肌肉量      = new Third("肌肉量", 69, 71, 73, 2, 1, UNIT_KG);
    public static final Third 骨骼肌      = new Third("骨骼肌", 75, 77, 79, 2, 1, UNIT_KG);
    public static final Third 身体水分    = new Third("身体水分", 81, 83, 85, 2, 1, UNIT_KG);
    public static final Third 蛋白质      = new Third("蛋白质", 87, 89, 91, 2, 1, UNIT_KG);
    public static final Third 无机盐      = new Third("无机盐", 93, 94, 95, 1, 1, UNIT_KG);
    public static final Third 细胞外液含量  = new Third("细胞外液含量", 97, 99, 101, 2, 1, UNIT_KG);
    public static final Third 细胞内液含量  = new Third("细胞内液含量", 103, 105, 107, 2, 1, UNIT_KG);
    public static final Third 左上肢肌肉量 = new Third("左上肢肌肉量", 109, 110, 111, 1, 1, UNIT_KG);
    public static final Third 右上肢肌肉量 = new Third("右上肢肌肉量", 112, 113, 114, 1, 1, UNIT_KG);
    public static final Third 躯干肌肉量   = new Third("躯干肌肉量", 115, 117, 119, 2, 1, UNIT_KG);
    public static final Third 左下肌肉量   = new Third("左下肌肉量", 121, 123, 125, 2, 1, UNIT_KG);
    public static final Third 右下肌肉量   = new Third("右下肌肉量", 127, 129, 131, 2, 1, UNIT_KG);
    public static final Third 左上脂肪量   = new Third("左上脂肪量", 133, 134, 135, 1, 1, UNIT_KG);
    public static final Third 右上脂肪量   = new Third("右上脂肪量", 136, 137, 138, 1, 1, UNIT_KG);
    public static final Third 躯干脂肪量   = new Third("躯干脂肪量", 139, 141, 143, 2, 1, UNIT_KG);
    public static final Third 左下脂肪量   = new Third("左下脂肪量", 145, 146, 147, 1, 1, UNIT_KG);
    public static final Third 右下脂肪量   = new Third("右下脂肪量", 148, 149, 150, 1, 1, UNIT_KG);
    public static final Third BMI        = new Third("BMI", 157, 159, 161, 2, 1, UNIT_EMPTY);
    public static final Third 体脂百分比   = new Third("体脂百分比", 163, 165, 167, 2, 1, UNIT_PERCENT);
    public static final Third 腰臀比      = new Third("腰臀比", 177, 178, 179, 1, 2, UNIT_EMPTY);
    public static final Third 水肿系数      = new Third("水肿系数", 180, 181, 182, 1, 2, UNIT_EMPTY);
    public static final Third 体型分析      = new Third("体型分析", INVALID_POSIONT, 183, 184, 1, 0, UNIT_EMPTY);
    public static final Third 内脏面积     = new Third("内脏面积", 185, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_CM2);
    public static final Third 评分        = new Third("评分", 187, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_SCORE);
    public static final Third 基础代谢     = new Third("基础代谢", 196, INVALID_POSIONT, INVALID_POSIONT,2, 0, UNIT_KCAL);
    public static final Third 总能耗       = new Third("总能耗", 198, INVALID_POSIONT, INVALID_POSIONT,2, 0, UNIT_KCAL);
    public static final Third 身体年龄     = new Third("身体年龄", 198, INVALID_POSIONT, INVALID_POSIONT,1, 0, UNIT_KCAL);
    public final List<Third> mList = new ArrayList<Third>();

    public static final int LEVEL_LOW    = 0;
    public static final int LEVEL_NORMAL = 1;
    public static final int LEVEL_HIGH   = 2;

    /**
     * 数据类
     */
    public static class Third {
        final String name;
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
        public Third(String name, int curStart, int minStart, int maxStart, int length, int dot, @NonNull String unit) {
            this.name = name;
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
        public Third(String name, int curStart, int length, int dot, @NonNull String unit) {
            this.name = name;
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
            return (float) (cur / (float)Math.pow(10, dot));
        }

        public void setCur(float cur) {
            this.cur = cur;
        }

        public float getMin() {
            return (float) (min / (float)Math.pow(10, dot));
        }

        public Third setMin(int min) {
            this.min = min;
            return this;
        }

        public float getMax() {
            return (float) (max / (float)Math.pow(10, dot));
        }

        public Third setMax(int max) {
            this.max = max;
            return this;
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

        public String formatCurUnit(int dot) {
            if (dot < 0) {
                dot = 0;
            }
            return String.format("%." + dot + "f", getCur()) + getUnit();
        }

        public String formatCurWithoutUnit(int dot) {
            if (dot < 0) {
                dot = 0;
            }
            return String.format("%." + dot + "f", getCur());
        }


        // screen
        public float getGuidelinePercent() {
            // 这些值是从UI是量得的，不同UI要按照不同的修改
            float ret = getProgress(130, 67, 223 - 70)/100f; // Of course, this default could be anything you want.
            // 微调回来
            return ret > 1f ? 1f : ret;
        }

        // pdf
        public float getPdfGuidelinePercent() {
            // 这些值是从UI是量得的，不同UI要按照不同的修改
            float ret = getProgress(71, 73, 78 - 70)/100f; // Of course, this default could be anything you want.
            // 微调回来
            return ret > 0.833333333f ? 0.833333333f : ret;
        }

        // pdf
        public float getPdf303GuidelinePercent() {
            // 这些值是从UI是量得的，不同UI要按照不同的修改
            float ret = getProgress(78, 64, 105 - 32)/100f; // Of course, this default could be anything you want.
            // 微调回来
            return ret > 1f ? 1f : ret;
        }

        public String formatValueWithUnit(int dot, float value) {
            if (dot < 0) {
                dot = 0;
            }
            return String.format("%." + dot + "f" + getUnit(), value);
        }

        public String formatValue(int dot, float value) {
            if (dot < 0) {
                dot = 0;
            }
            return String.format("%." + dot + "f", value);
        }

        // cur(min-max)
        public String formatRange1(int dot) {
            if (dot < 0) {
                dot = 0;
            }
            return formatValueWithUnit(dot, getCur()) + "(" + formatValue(dot, getMin()) + "-" + formatValue(dot, getMax()) + ")";
        }

        //    cur
        // (min-max)
        public String formatRange2(int dot) {
            if (dot < 0) {
                dot = 0;
            }
            return formatValueWithUnit(dot, getCur()) + "\n(" + formatValue(dot, getMin()) + "-" + formatValue(dot, getMax()) + ")";
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
            // 这里添加了进度条最小值补救
            if (percent < 10)
                percent = 10;
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

        public boolean levelLow() {
            return getLevel() == LEVEL_LOW;
        }

        public boolean levelHigh() {
            return getLevel() == LEVEL_HIGH;
        }

        public boolean levelNormal() {
            return getLevel() == LEVEL_NORMAL;
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

        public String getGenderAsChinese() {
            switch ((int)getCur()) {
                case GENDER_FEMALE:
                    return "女";
                default:
                    return "男";

            }
        }

        public String getAdjustmentMinWithUnit(int dot) {
            float ret = getCur() - getMin();
            ret = ret > 0 ? ret : 0;
            String fuhao = "";
            if (ret != 0) { // =0时不显示符号
                fuhao = "-";
            }
            return fuhao + formatValueWithUnit(dot, ret);
        }

        public String getAdjustmentMaxWithUnit(int dot) {
            float ret = getMax() - getCur();
            ret = ret > 0 ? ret : 0;
            String fuhao = "";
            if (ret != 0) { // =0时不显示符号
                fuhao = "+";
            }
            return fuhao + formatValueWithUnit(dot, ret);
        }

        @Override
        public String toString() {
            return "Third{" +
                    " " + this.name +
                    ", cur=" + String.format("%02X", (int)cur) +
                    ", min=" + String.format("%02X", (int)min) +
                    ", max=" + String.format("%02X", (int)max) +
                    ", curStart=" + curStart +
                    ", minStart=" + minStart +
                    ", maxStart=" + maxStart +
                    ", length=" + length +
                    ", dot=" + dot +
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

    /**
     * will parse 1 or 2 length number from byte array
     * @param t
     * @param data
     * @return
     */
    boolean parse(Third t, byte[] data) {
        if (t == null || data == null) {
            return false;
        }

        if (t.getLength() == 1) {
            if (t.getCurStart() != INVALID_POSIONT)
                t.setCur(data[t.getCurStart()] & 0xFF);
            if (t.getMinStart() != INVALID_POSIONT)
                t.setMin(data[t.getMinStart()] & 0xFF);
            if (t.getMaxStart() != INVALID_POSIONT)
                t.setMax(data[t.getMaxStart()] & 0xFF);
        } else if (t.getLength() == 2) {
            if (t.getCurStart() != INVALID_POSIONT)
                t.setCur(Protocol.getShortFromData(data, t.getCurStart()) & 0xFFFF);
            if (t.getMinStart() != INVALID_POSIONT)
                t.setMin(Protocol.getShortFromData(data, t.getMinStart()) & 0xFFFF);
            if (t.getMaxStart() != INVALID_POSIONT)
                t.setMax(Protocol.getShortFromData(data, t.getMaxStart()) & 0xFFFF);
        } else {
            return false;
        }

        return true;
    }

    // 原始数据包
    private byte[] data;
    public byte[] getData() {
        return data;
    }

    /**
     * 从数据段进行解析各项参数
     *
     * @param data
     */
    public BodyComposition(final byte[] data) {
        this.data = data;
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
        mList.add(内脏面积.setMin(0).setMax(750)); // hardcode number
        mList.add(评分);
        mList.add(基础代谢);
        mList.add(总能耗);
        mList.add(去脂体重);
        mList.add(肌肉量);
        mList.add(细胞外液含量);
        mList.add(细胞内液含量);
        mList.add(水肿系数);
        mList.add(体型分析);
        mList.add(身体年龄);

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

        // 微调
        // 1. 女性基础代谢率=661+10.7*去脂体重(kg)+1.72*身高(cm)-4.7*年龄    * Math.pow(10, 基础代谢.dot)
        // 男性基础代谢率=67+17.5*去脂体重(kg)+5*身高(cm)-6.9*年龄     * Math.pow(10, 基础代谢.dot)
        Log.i(TAG, "基础代谢1: " + 基础代谢.getCur());
        float tmp = 0;
        if (性别.getCur() == Protocol.PROTOCAL_GENDER_FEMALE) {
            tmp = (float) ((661 + 10.7 * 去脂体重.getCur() + 1.72 * 身高.getCur() - 4.7 * 年龄.getCur()) * (float)Math.pow(10, 基础代谢.dot));
        } else {
            tmp = (float) ((67 + 17.5 * 去脂体重.getCur() + 5 * 身高.getCur() - 6.9 * 年龄.getCur()) * (float)Math.pow(10, 基础代谢.dot));
        }
        基础代谢.setCur(tmp);
        Log.i(TAG, "基础代谢2: " + 基础代谢.getCur());

        // 20190322 由0.8改为*0.9 后续还得改，评分有点低
        // 2.评分=下位机传回评分x0.9+（骨骼肌映射20分）；
        // 骨骼肌映射20分：骨骼肌进度条在第一格的，计0分。然后第二格10分，第三格10分，根据进度线段按比例映射得出具体分数。
        //  * Math.pow(10, 评分.dot)
        Log.i(TAG, "评分1:  " + 评分.getCur());
        float score = (float) (骨骼肌.getProgress(0, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH) * 0.2);
        if (骨骼肌.getLevel() == LEVEL_LOW)
            score = 0;
        评分.setCur((float) ((评分.getCur() * 0.9 + score) * (float)Math.pow(10, 评分.dot)));
        Log.i(TAG, "评分2:  " + 评分.getCur());

        // 3. 身体年龄 计算
        //    评分90(含)以上的，身体年龄=输入的实际年龄-1
        //    评分90---80(含)，身体年龄=输入的实际年龄
        //    评分70(含)----80 ，身体年龄=输入的实际年龄+1
        //    评分 60(含)---70，身体年龄=输入的实际年龄+2
        //    60分以下，身体年龄=输入的实际年龄+3
        int yuhuinianling = 0;
        score = 评分.getCur();
        if (score >= 90) {
            yuhuinianling = -1;
        } else if (score >= 80) {
            yuhuinianling = 0;
        } else if (score >= 70) {
            yuhuinianling = 1;
        } else if (score >= 60) {
            yuhuinianling = 2;
        } else { // (score < 60)
            yuhuinianling = 3;
        }

        身体年龄.setCur(年龄.getCur() + yuhuinianling);
        Log.i(TAG, "身体年龄2:  " + 身体年龄.getCur());

        // yaotunbi weitiao
        // hardcode number
        腰臀比.setMin(70).setMax(80);

    }
}
