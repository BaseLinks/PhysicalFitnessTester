package com.kangear.bca;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kangear.bodycompositionanalyzer.Protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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

    /* 女 */
    public static final byte FEMALE = 0x00;
    /* 男 */
    public static final byte MALE   = 0x01;

    public static final Third 姓名        = new Third("姓名", 0, 2, 0, UNIT_EMPTY);
    public static final Third 身高        = new Third("身高", 2, 2, 1, UNIT_CM);
    public static final Third 年龄        = new Third("年龄",6, 1, 0, UNIT_EMPTY);
    public static final Third 性别        = new Third("性别", 7, 1, 0, UNIT_EMPTY);
    public static final Third 身体电阻值   = new Third("身体电阻值", 8, 2, 0, UNIT_R); //TODO

    public static final Third _50k电阻LA     = new Third("50k电阻LA", 10, 2, 1, UNIT_R);//TODO
    public static final Third _50k电阻RA     = new Third("50k电阻RA", 12, 2, 1, UNIT_R);//TODO
    public static final Third _50k电阻TR     = new Third("50k电阻TR", 14, 2, 1, UNIT_R);//TODO
    public static final Third _50k电阻LL     = new Third("50k电阻TR", 16, 2, 1, UNIT_R);//TODO
    public static final Third _50k电阻RL     = new Third("50k电阻TR", 18, 2, 1, UNIT_R);//TODO

    public static final Third _5k电阻LA    = new Third("5k电阻LA", 20, 2, 1, UNIT_R);//TODO
    public static final Third _5k电阻RA    = new Third("5k电阻RA", 22, 2, 1, UNIT_R);//TODO
    public static final Third _5k电阻TR    = new Third("5k电阻TR", 24, 2, 1, UNIT_R);//TODO
    public static final Third _5k电阻LL    = new Third("5k电阻LL", 26, 2, 1, UNIT_R);//TODO
    public static final Third _5k电阻RL    = new Third("5k电阻RL", 28, 2, 1, UNIT_R);//TODO

    public static final Third _250k电阻LA   = new Third("250k电阻LA", 30, 2, 1, UNIT_R);//TODO
    public static final Third _250k电阻RA   = new Third("250k电阻RA", 32, 2, 1, UNIT_R);//TODO
    public static final Third _250k电阻TR   = new Third("250k电阻TR", 34, 2, 1, UNIT_R);//TODO
    public static final Third _250k电阻LL   = new Third("250k电阻LL", 36, 2, 1, UNIT_R);//TODO
    public static final Third _250k电阻RL   = new Third("250k电阻RL", 38, 2, 1, UNIT_R);//TODO

    public static final Third 测试时间      = new Third("测试时间", 40, 10, 0, UNIT_R);//TODO
    public static final Third 体重        = new Third("体重", 50, 52, 54, 56, 2, 1, UNIT_KG);
    public static final Third 去脂体重     = new Third("去脂体重", 58, 60, 62, 2, 1, UNIT_KG);
    public static final Third 体脂肪量    = new Third("体脂肪量", 64, 66, 68, 2, 1, UNIT_KG);
    public static final Third 肌肉量      = new Third("肌肉量", 70, 72, 74, 2, 1, UNIT_KG);

    public static final Third 躯干脂肪量   = new Third("躯干脂肪量", 76, 78, 80, 2, 2, UNIT_KG);
    public static final Third 左上脂肪量   = new Third("左上脂肪量", 82, 84, 86, 2, 2, UNIT_KG);
    public static final Third 右上脂肪量   = new Third("右上脂肪量", 88, 90, 92, 2, 2, UNIT_KG);
    public static final Third 左下脂肪量   = new Third("左下脂肪量", 94, 96, 98, 2, 2, UNIT_KG);
    public static final Third 右下脂肪量   = new Third("右下脂肪量", 100, 102, 104, 2, 2, UNIT_KG);

    public static final Third 内脏脂肪指数   = new Third("内脏脂肪指数", 106, 108, 110, 2, 1, UNIT_KG);

    public static final Third 躯干肌肉量   = new Third("躯干肌肉量", 112, 114, 116, 2, 2, UNIT_KG);
    public static final Third 左上肢肌肉量 = new Third("左上肢肌肉量", 118, 120, 122, 2, 2, UNIT_KG);
    public static final Third 右上肢肌肉量 = new Third("右上肢肌肉量", 124, 126, 128, 2, 2, UNIT_KG);
    public static final Third 左下肌肉量   = new Third("左下肌肉量", 130, 132, 134, 2, 2, UNIT_KG);
    public static final Third 右下肌肉量   = new Third("右下肌肉量", 136, 138, 140, 2, 2, UNIT_KG);

    public static final Third 身体水分    = new Third("身体水分", 142, 144, 146, 2, 1, UNIT_KG);
    public static final Third 蛋白质      = new Third("蛋白质", 148, 150, 152, 2, 1, UNIT_KG);
    public static final Third 无机盐      = new Third("无机盐", 154, 156, 158, 2, 1, UNIT_KG);

    public static final Third 细胞外液含量  = new Third("细胞外液含量", 160, 162, 164, 2, 1, UNIT_KG);
    public static final Third 细胞内液含量  = new Third("细胞内液含量", 166, 168, 170, 2, 1, UNIT_KG);

    public static final Third BMI        = new Third("BMI", 178, 180, 182, 2, 0, UNIT_EMPTY);
    public static final Third 脂肪率      = new Third("脂肪率", 184, 186, 188, 2, 1, UNIT_PERCENT);

    public static final Third 评分        = new Third("评分", 198,2, 1, UNIT_SCORE);
    public static final Third 身体年龄     = new Third("身体年龄", 200,2, 0, UNIT_SCORE);
    public static final Third 体重调节     = new Third("体重调节", 202, 4, 1, UNIT_EMPTY);
    public static final Third 脂肪调节     = new Third("脂肪调节", 206, 4, 1, UNIT_EMPTY);
    public static final Third 肌肉调节     = new Third("肌肉调节", 210, 4, 1, UNIT_EMPTY);
    public static final Third 基础代谢     = new Third("基础代谢", 214, 2, 0, UNIT_KCAL);
    public static final Third 总能耗       = new Third("总能耗", 216, 2, 0, UNIT_KCAL);

    // 新增加的参数
    public static final Third 身体水分率    = new Third("身体水分率", INVALID_POSIONT, 0, 0, UNIT_EMPTY);
    public static final Third 水肿系数      = new Third("水肿系数", INVALID_POSIONT, 0, 0, UNIT_EMPTY);

    public final List<Third> mList = new ArrayList<Third>();

    public static final int LEVEL_LOW    = 0;
    public static final int LEVEL_NORMAL = 1;
    public static final int LEVEL_HIGH   = 2;

    /**
     * 数据类
     */
    public static class Third {
        String name;
        float cur;
        float min;
        float max;
        int curStart = INVALID_POSIONT;
        int minStart = INVALID_POSIONT;
        int maxStart = INVALID_POSIONT;
        int standardValueStart = INVALID_POSIONT;
        int length;
        int dot;
        String unit;

        /**
         * @param name
         * @param curStart
         * @param minStart
         * @param maxStart
         * @param length
         * @param dot　小数点几位
         */
        public Third(String name, int curStart, int minStart, int maxStart, int standardValueStart, int length, int dot, @NonNull String unit) {
            this.name = name;
            this.curStart = curStart;
            this.minStart = minStart;
            this.maxStart = maxStart;
            this.standardValueStart = standardValueStart;
            this.length = length;
            this.dot = dot;
            this.unit = unit;
        }

        /**
         * @param name
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
         * @param name
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

        public String getName() { return name; }

        public int getCurStart() {
            return curStart;
        }

        //直接还原真实的值
        public float getCur() {
            return (cur / (float)Math.pow(10, dot));
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

        public Third setUnit(String unit) {
            this.unit = unit;
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
                    "" + getName() +
                    " cur=" + String.format("%02X ", (int)cur) + getCur() +
                    ", min=" + String.format("%02X ", (int)min) + getMin() +
                    ", max=" + String.format("%02X ", (int)max) + getMax() +
                    ", curStart=" + curStart +
                    ", minStart=" + minStart +
                    ", maxStart=" + maxStart +
                    ", length=" + length +
                    ", unit=" + unit +
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
        float ret = 0; //骨骼肌.getMax() - 骨骼肌.getCur();
        return ret > 0 ? ret : 0;
    }

    /**
     * will parse 1 or 2 length number from byte array
     * @param t
     * @param data
     * @return
     */
    private boolean parse(Third t, byte[] data) {
        if (t == null || data == null) {
            return false;
        }

        // 时间额外
        if (t.equals(测试时间)) {
            //Log.i(TAG, "测试时间: " + 测试时间.toString());
            return parseTime(t, data);
        } else if (t.equals(体重调节) || t.equals(脂肪调节) || t.equals(肌肉调节)) {
            return parseTiaojie(t, data);
        } else if (t.equals(身体水分率) || t.equals(水肿系数)) {
            return parseOther(t, data);
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

    // 解析其他
    // 身体总水分 / 体重 * 100;
    private boolean parseOther(Third t, byte[] data) {
        if (t.equals(身体水分率)) {
            身体水分率.setCur(身体水分.getCur() / 体重.getCur() * 100);
        } else if (t.equals(水肿系数)) {
            水肿系数.setCur(细胞外液含量.getCur() / 身体水分.getCur());
        }
        return true;
    }

    // 解析时间
    private boolean parseTime(Third t, byte[] data) {
        byte[] b = Arrays.copyOfRange(data, t.getCurStart(), t.getCurStart() + t.getLength());
        String 测试日期 = "20" + new String(new byte[]{b[0],b[1]}) + "-" + /* 年 */
                new String(new byte[]{b[2],b[3]}) + "-" +   /* 月 */
                new String(new byte[]{b[4],b[5]}) + " " +   /* 日 */
                new String(new byte[]{b[6],b[7]}) + ":" +   /* 时 */
                new String(new byte[]{b[8],b[9]});          /* 分 */
//        Log.e(TAG, "测试时间: " + 测试日期);
        t.setUnit(测试日期);
        return true;
    }

    // 解析调节量
    private boolean parseTiaojie(Third t, byte[] data) {
        byte[] arr = Arrays.copyOfRange(data, t.getCurStart(), t.getCurStart() + t.getLength());
        int fuhao = com.kangear.bca.Protocol.getShortFromData(arr, 0)  & 0xFFFF;
        int val = com.kangear.bca.Protocol.getShortFromData(arr, 2) & 0xFFFF;
        if (fuhao == 0) // 代表负数
            val *= -1;
        t.setCur(val);
        return true;
    }

    /**
     * 从数据段进行解析各项参数
     *
     * @param data
     */
    public BodyComposition(final byte[] data) {
        mList.add(姓名);
        mList.add(身高);
        mList.add(年龄);
        mList.add(性别);

        mList.add(身体电阻值);

        mList.add(_50k电阻LA);
        mList.add(_50k电阻RA);
        mList.add(_50k电阻TR);
        mList.add(_50k电阻LL);
        mList.add(_50k电阻RL);

        mList.add(_5k电阻LA);
        mList.add(_5k电阻RA);
        mList.add(_5k电阻TR);
        mList.add(_5k电阻LL);
        mList.add(_5k电阻RL);

        mList.add(_250k电阻LA);
        mList.add(_250k电阻RA);
        mList.add(_250k电阻TR);
        mList.add(_250k电阻LL);
        mList.add(_250k电阻RL);

        mList.add(测试时间); // 需要额外整理吧?
        mList.add(体重);
        mList.add(去脂体重);
        mList.add(体脂肪量);
        mList.add(肌肉量);

        mList.add(躯干脂肪量);
        mList.add(左上脂肪量);
        mList.add(右上脂肪量);
        mList.add(左下脂肪量);
        mList.add(右下脂肪量);

        mList.add(内脏脂肪指数);

        mList.add(躯干肌肉量);
        mList.add(左上肢肌肉量);
        mList.add(右上肢肌肉量);
        mList.add(左下肌肉量);
        mList.add(右下肌肉量);

        mList.add(身体水分);
        mList.add(蛋白质);
        mList.add(无机盐);

        mList.add(细胞外液含量);
        mList.add(细胞内液含量);

        mList.add(BMI);
        mList.add(脂肪率);
        mList.add(评分);
        mList.add(身体年龄);
        mList.add(体重调节);
        mList.add(脂肪调节);
        mList.add(肌肉调节);
        mList.add(基础代谢);
        mList.add(总能耗);

        // 其他
        mList.add(身体水分率);
        mList.add(水肿系数);

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

//        // 微调
//        // 1. 女性基础代谢率=661+10.7*去脂体重(kg)+1.72*身高(cm)-4.7*年龄    * Math.pow(10, 基础代谢.dot)
//        // 男性基础代谢率=67+17.5*去脂体重(kg)+5*身高(cm)-6.9*年龄     * Math.pow(10, 基础代谢.dot)
//        Log.i(TAG, "基础代谢1: " + 基础代谢.getCur());
//        float tmp = 0;
//        if (性别.getCur() == Protocol.PROTOCAL_GENDER_FEMALE) {
//            tmp = (float) ((661 + 10.7 * 去脂体重.getCur() + 1.72 * 身高.getCur() - 4.7 * 年龄.getCur()) * (float)Math.pow(10, 基础代谢.dot));
//        } else {
//            tmp = (float) ((67 + 17.5 * 去脂体重.getCur() + 5 * 身高.getCur() - 6.9 * 年龄.getCur()) * (float)Math.pow(10, 基础代谢.dot));
//        }
//        基础代谢.setCur(tmp);
//        Log.i(TAG, "基础代谢2: " + 基础代谢.getCur());
//
//        // 2.评分=下位机传回评分x0.8+（骨骼肌映射20分）；
//        // 骨骼肌映射20分：骨骼肌进度条在第一格的，计0分。然后第二格10分，第三格10分，根据进度线段按比例映射得出具体分数。
//        //  * Math.pow(10, 评分.dot)
//        Log.i(TAG, "评分1:  " + 评分.getCur());
//        float score = (float) (骨骼肌.getProgress(0, NOMAL_LEVEL_WIDTH, MORE_LEVEL_WIDTH) * 0.2);
//        if (骨骼肌.getLevel() == LEVEL_LOW)
//            score = 0;
//        评分.setCur((float) ((评分.getCur() * 0.8 + score) * (float)Math.pow(10, 评分.dot)));
//        Log.i(TAG, "评分2:  " + 评分.getCur());

        // 将性别男女放到单位中
        性别.setUnit(性别.getCur() == 0 ?  "女": "男");

        /* 体重调节量是 脂肪调节量 和 肌肉调节量 之和 */
//        体重调节.setCur(脂肪调节.getCur() + 肌肉调节.getCur());

        // 将BMI(身体质量指数)最大值，最小值微调
        // 根据旧文档旧代码确定的值
        BMI.setMax(330); // 最大值
        BMI.setMin(154); // 最小值

        // 将脂肪率最值微调
        脂肪率.setMin(10 * 10);
        脂肪率.setMax(35 * 10); // 男性
        if ((byte)性别.getCur() == FEMALE)
            脂肪率.setMax(60 * 10); // 女性
    }
}
