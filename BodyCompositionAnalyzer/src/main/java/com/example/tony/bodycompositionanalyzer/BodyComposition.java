package com.example.tony.bodycompositionanalyzer;

import android.util.Log;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by tony on 16-6-22.
 */
public class BodyComposition {
    private static final String LOG_TAG = "BodyComposition";
    private String 姓名;
    private String 身高;
    private String 体重1;
    private String 性别;
    private String 年龄;
    private String 身体电阻值;

    private String _50k下la电阻值;
    private String _50k下ra电阻值;
    private String _50k下tr电阻值;
    private String _50k下ll电阻值;
    private String _50k下rl电阻值;
    private String _5k下la电阻值;
    private String _5k下ra电阻值;
    private String _5k下tr电阻值;
    private String _5k下ll电阻值;
    private String _5k下rl电阻值;
    private String _250k下la电阻值;
    private String _250k下ra电阻值;
    private String _250k下tr电阻值;
    private String _250k下ll电阻值;
    private String _250k下rl电阻值;

    private String 测试时间;
    private String 体重2;
    private String 体重标准范围;
    private String 体重标准值;
    private String 去脂肪体重;
    private String 去脂肪体重标准;
    private String 体脂肪量;
    private String 体脂肪量标准;
    private String 肌肉量;
    private String 肌肉标准;
    private String 躯干脂肪;
    private String 躯干脂肪标准;
    private String 左上肢脂肪量;
    private String 左上肢脂肪标准;
    private String 右上肢脂肪量;
    private String 右上肢脂肪标准;
    private String 左下肢脂肪量;
    private String 左下肢脂肪标准;
    private String 右下肢脂肪量;
    private String 右下肢脂肪标准;
    private String 内脏脂肪指数;
    private String 内脏指数正常范围;
    /* 节段分析， */
    private String 躯干肌肉;
    private String 躯干肌肉标准;
    private String 左上肢肌肉含量;
    private String 左上肢肉正常范围;
    private String 右上肢肌肉含量;
    private String 右上肢肌肉含量正常范围;
    private String 左下肢肌肉含量;
    private String 左下肢肌肉正常范围;
    private String 右下肢肌肉含量;
    private String 右下肢肌肉含量正常范围;

    private String 身体总水分;
    private String 身体总水分正常范围;
    private String 蛋白质含量;
    private String 蛋白质正常范围;
    private String 无机盐含量;
    private String 无机盐含量正常范围;
    private String 细胞外液含量;
    private String 细胞外液正常范围;
    private String 细胞内液含量;
    private String 细胞内液正常范围;
    // ...
    private String BMI结果;
    private String 脂肪率;

    // ...
    private String 身体总评分;
    private String 身体年龄;
    private String 体重调节;
    private String 脂肪调节;
    private String 肌肉调节;
    private String 基础代谢量;
    private String 总能量消耗;

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

    /**
     * 从数据段进行解析各项参数
     * @param data
     */
    public BodyComposition(final byte[] data) {
        int tmpInt = 0;
        //
        float tmpFloat = 0;
        // 符号
        String opt = "";

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
        测试时间 = "20" + new String(new byte[]{b[0],b[1]}) + "-" + /* 年 */
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
        体重标准范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重标准范围 = String.format("%s-%.1f", 体重标准范围, tmpFloat);

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
        去脂肪体重标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        去脂肪体重标准 = String.format("%s-%.1f", 去脂肪体重标准, tmpFloat);

        // 27. 体脂肪量
        b = Arrays.copyOfRange(data, 体脂肪量_START, 体脂肪量_START + 体脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体脂肪量 = String.format("%.1f", tmpFloat);

        // 28. 体脂肪量标准
        b = Arrays.copyOfRange(data, 体脂肪量标准_START, 体脂肪量标准_START + 体脂肪量标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体脂肪量标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体脂肪量标准 = String.format("%s-%.1f", 体脂肪量标准, tmpFloat);

        // 29. 肌肉量
        b = Arrays.copyOfRange(data, 肌肉量_START, 肌肉量_START + 肌肉量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉量 = String.format("%.1f", tmpFloat);

        // 30. 肌肉标准
        b = Arrays.copyOfRange(data, 肌肉标准_START, 肌肉标准_START + 肌肉标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉标准 = String.format("%s-%.1f", 肌肉标准, tmpFloat);

        // 31. 躯干脂肪
        b = Arrays.copyOfRange(data, 躯干脂肪_START, 躯干脂肪_START + 躯干脂肪_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干脂肪 = String.format("%.1f", tmpFloat);

        // 32. 躯干脂肪标准
        b = Arrays.copyOfRange(data, 躯干脂肪标准_START, 躯干脂肪标准_START + 躯干脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干脂肪标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干脂肪标准 = String.format("%s-%.1f", 躯干脂肪标准, tmpFloat);

        // 33. 左上肢脂肪量
        b = Arrays.copyOfRange(data, 左上肢脂肪量_START, 左上肢脂肪量_START + 左上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢脂肪量 = String.format("%.1f", tmpFloat);

        // 34. 左上肢脂肪标准
        b = Arrays.copyOfRange(data, 左上肢脂肪标准_START, 左上肢脂肪标准_START + 左上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢脂肪标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢脂肪标准 = String.format("%s-%.1f", 左上肢脂肪标准, tmpFloat);

        // 35. 右上肢脂肪量
        b = Arrays.copyOfRange(data, 右上肢脂肪量_START, 右上肢脂肪量_START + 右上肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢脂肪量 = String.format("%.1f", tmpFloat);

        // 36. 右上肢脂肪标准
        b = Arrays.copyOfRange(data, 右上肢脂肪标准_START, 右上肢脂肪标准_START + 右上肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢脂肪标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢脂肪标准 = String.format("%s-%.1f", 右上肢脂肪标准, tmpFloat);

        // 37. 左下肢脂肪量
        b = Arrays.copyOfRange(data, 左下肢脂肪量_START, 左下肢脂肪量_START + 左下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢脂肪量 = String.format("%.1f", tmpFloat);

        // 38. 左下肢脂肪标准
        b = Arrays.copyOfRange(data, 左下肢脂肪标准_START, 左下肢脂肪标准_START + 左下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢脂肪标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢脂肪标准 = String.format("%s-%.1f", 左下肢脂肪标准, tmpFloat);

        // 39. 右下肢脂肪量
        b = Arrays.copyOfRange(data, 右下肢脂肪量_START, 右下肢脂肪量_START + 右下肢脂肪量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢脂肪量 = String.format("%.1f", tmpFloat);

        // 40. 右下肢脂肪标准
        b = Arrays.copyOfRange(data, 右下肢脂肪标准_START, 右下肢脂肪标准_START + 右下肢脂肪标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢脂肪标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢脂肪标准 = String.format("%s-%.1f", 右下肢脂肪标准, tmpFloat);

        // 41. 内脏脂肪指数
        b = Arrays.copyOfRange(data, 内脏脂肪指数_START, 内脏脂肪指数_START + 内脏脂肪指数_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        内脏脂肪指数 = String.format("%.1f", tmpFloat);

        // 42. 内脏指数正常范围
        b = Arrays.copyOfRange(data, 内脏指数正常范围_START, 内脏指数正常范围_START + 内脏指数正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        内脏指数正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        内脏指数正常范围 = String.format("%s-%.1f", 内脏指数正常范围, tmpFloat);

        // 43. 躯干肌肉
        b = Arrays.copyOfRange(data, 躯干肌肉_START, 躯干肌肉_START + 躯干肌肉_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干肌肉 = String.format("%.1f", tmpFloat);

        // 44. 躯干肌肉标准
        b = Arrays.copyOfRange(data, 躯干肌肉标准_START, 躯干肌肉标准_START + 躯干肌肉标准_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干肌肉标准 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        躯干肌肉标准 = String.format("%s-%.1f", 躯干肌肉标准, tmpFloat);

        // 45. 左上肢肌肉含量
        b = Arrays.copyOfRange(data, 左上肢肌肉含量_START, 左上肢肌肉含量_START + 左上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 46. 左上肢肉正常范围
        b = Arrays.copyOfRange(data, 左上肢肉正常范围_START, 左上肢肉正常范围_START + 左上肢肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢肉正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左上肢肉正常范围 = String.format("%s-%.1f", 左上肢肉正常范围, tmpFloat);

        // 47. 右上肢肌肉含量
        b = Arrays.copyOfRange(data, 右上肢肌肉含量_START, 右上肢肌肉含量_START + 右上肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 48. 右上肢肌肉含量正常范围
        b = Arrays.copyOfRange(data, 右上肢肌肉含量正常范围_START, 右上肢肌肉含量正常范围_START + 右上肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢肌肉含量正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右上肢肌肉含量正常范围 = String.format("%s-%.1f", 右上肢肌肉含量正常范围, tmpFloat);

        // 49. 左下肢肌肉含量
        b = Arrays.copyOfRange(data, 左下肢肌肉含量_START, 左下肢肌肉含量_START +  左下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 50. 左下肢肌肉正常范围
        b = Arrays.copyOfRange(data, 左下肢肌肉正常范围_START, 左下肢肌肉正常范围_START + 左下肢肌肉正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢肌肉正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        左下肢肌肉正常范围 = String.format("%s-%.1f", 左下肢肌肉正常范围, tmpFloat);

        // 51. 右下肢肌肉含量
        b = Arrays.copyOfRange(data, 右下肢肌肉含量_START, 右下肢肌肉含量_START + 右下肢肌肉含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢肌肉含量 = String.format("%.1f", tmpFloat);

        // 52. 右下肢肌肉含量正常范围
        b = Arrays.copyOfRange(data, 右下肢肌肉含量正常范围_START, 右下肢肌肉含量正常范围_START + 右下肢肌肉含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢肌肉含量正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        右下肢肌肉含量正常范围 = String.format("%s-%.1f", 右下肢肌肉含量正常范围, tmpFloat);

        // 53. 身体总水分
        b = Arrays.copyOfRange(data, 身体总水分_START, 身体总水分_START + 身体总水分_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总水分 = String.format("%.1f", tmpFloat);

        // 54. 身体总水分正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 身体总水分正常范围_START, 身体总水分正常范围_START + 身体总水分正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总水分正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        身体总水分正常范围 = String.format("%s-%.1f", 身体总水分正常范围, tmpFloat);

        // 55. 蛋白质含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质含量_START, 蛋白质含量_START + 蛋白质含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        蛋白质含量 = String.format("%.1f", tmpFloat);

        // 56. 蛋白质正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 蛋白质正常范围_START, 蛋白质正常范围_START + 蛋白质正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        蛋白质正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        蛋白质正常范围 = String.format("%s-%.1f", 蛋白质正常范围, tmpFloat);

        // 57. 无机盐含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量_START, 无机盐含量_START + 无机盐含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        无机盐含量 = String.format("%.1f", tmpFloat);

        // 58. 无机盐含量正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 无机盐含量正常范围_START, 无机盐含量正常范围_START + 无机盐含量正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        无机盐含量正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        无机盐含量正常范围 = String.format("%s-%.1f", 无机盐含量正常范围, tmpFloat);

        // 59. 细胞外液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液含量_START, 细胞外液含量_START + 细胞外液含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞外液含量 = String.format("%.1f", tmpFloat);

        // 60. 细胞外液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞外液正常范围_START, 细胞外液正常范围_START + 细胞外液正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞外液正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞外液正常范围 = String.format("%s-%.1f", 细胞外液正常范围, tmpFloat);

        // 61. 细胞内液含量 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液含量_START, 细胞内液含量_START + 细胞内液含量_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞内液含量 = String.format("%.1f", tmpFloat);

        // 62. 细胞内液正常范围 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 细胞内液正常范围_START, 细胞内液正常范围_START + 细胞内液正常范围_LENGTH);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞内液正常范围 = String.format("%.1f", tmpFloat);
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        细胞内液正常范围 = String.format("%s-%.1f", 细胞内液正常范围, tmpFloat);

        // 63. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        去脂肪体重 = String.format("%.1f", tmpFloat);

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
        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 67. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpFloat = (float) ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        去脂肪体重 = String.format("%.1f", tmpFloat);

        // 68. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        去脂肪体重 = String.format("%d", tmpInt);

        // 69. 无解释
        b = Arrays.copyOfRange(data, 去脂肪体重_START, 去脂肪体重_START + 去脂肪体重_LENGTH);
        tmpInt = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
        去脂肪体重 = String.format("%d", tmpInt);

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
        opt = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        体重调节 = String.format("%s%.1f", opt, tmpFloat);

        // 73. 脂肪调节 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 脂肪调节_START, 脂肪调节_START + 脂肪调节_LENGTH);
        opt = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        脂肪调节 = String.format("%s%.1f", opt, tmpFloat);

        // 74. 肌肉调节 okay 保留一位小数 kg
        b = Arrays.copyOfRange(data, 肌肉调节_START, 肌肉调节_START + 肌肉调节_LENGTH);
        opt = ByteBuffer.wrap(Arrays.copyOfRange(b, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() == 0x00 ? "-" : "+";
        tmpFloat = ByteBuffer.wrap(Arrays.copyOfRange(b, 2, 4)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 10;
        肌肉调节 = String.format("%s%.1f", opt, tmpFloat);

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
                "\n测试时间='" + 测试时间 + '\'' +
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
                "\n躯干肌肉='" + 躯干肌肉 + '\'' +
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
