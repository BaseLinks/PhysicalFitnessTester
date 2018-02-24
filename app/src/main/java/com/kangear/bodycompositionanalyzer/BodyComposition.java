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
    public static final String UNIT_PERCENT= "%";
    public static final int INVALID_POSIONT = -1;

    public final Third 性别        = new Third(20, INVALID_POSIONT, INVALID_POSIONT,1, 0, UNIT_EMPTY);
    public final Third 年龄        = new Third(21, INVALID_POSIONT, INVALID_POSIONT,1, 0, UNIT_AGE);
    public final Third 身高        = new Third(22, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_CM);
    public final Third 体重        = new Third(24, 53, 55, 2, 1, UNIT_KG);
    public final Third _5k电阻     = new Third(26, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
    public final Third _50k电阻    = new Third(28, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
    public final Third _250k电阻   = new Third(44, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_R);
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
    public final Third BMI        = new Third(157, 159, 161, 2, 1, UNIT_EMPTY);
    public final Third 体脂百分比   = new Third(163, 165, 167, 2, 1, UNIT_PERCENT);
    public final Third 腰臀比      = new Third(177, 178, 179, 1, 2, UNIT_EMPTY);
    public final Third 内脏面积     = new Third(185, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_CM2);
    public final Third 评分        = new Third(187, INVALID_POSIONT, INVALID_POSIONT,2, 1, UNIT_SCORE);
    public final Third 基础代谢     = new Third(196, INVALID_POSIONT, INVALID_POSIONT,2, 0, UNIT_KCAL);
    public final Third 总能耗       = new Third(198, INVALID_POSIONT, INVALID_POSIONT,2, 0, UNIT_KCAL);
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

        public Third setMin(int min) {
            this.min = min;
            return this;
        }

        public float getMax() {
            return (float) (max / Math.pow(10, dot));
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
        mList.add(内脏面积.setMin(0).setMax(800)); // hardcode number
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
}
