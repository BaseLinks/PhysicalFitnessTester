package com.kangear.bodycompositionanalyzer;

import android.util.Log;

import com.kangear.common.utils.ByteArrayUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * Created by tony on 18-1-11.
 */

public class Protocol implements IProtocol {
    private static final String TAG = "Protocol";
    protected static boolean FAKE_DATA = false;

    // 一.接口参数 接口参数—115200,8N1. 232 串口通讯
    public static final int RATE = 115200;

    // 二.通信格式
    // [引导符][消息长度] [命令/状态][项目代码][项目地址][数 据] [CRC]
    public static final int MSG_HEAD_LENGTH        = 0x02;
    public static final int MSG_LENGTH_LENGTH      = 0x01;
    public static final int MSG_CMD_LENGTH         = 0x01;
    public static final int MSG_ITEM_CODE_LENGTH   = 0x01;
    public static final int MSG_ITEM_ADDR_LENGTH   = 0x01;
    public static final int MSG_DATA_LENGTH        = 0x00; // n
    public static final int MSG_CRC_LENGTH         = 0x02;
    public static final int MSG_STATE_LENGTH       = 0x01;

    public static final int MSG_HEAD_START         = 0x00;
    public static final int MSG_LENGTH_START       = MSG_HEAD_START + MSG_HEAD_LENGTH;
    public static final int MSG_STATE_START        = MSG_LENGTH_START + MSG_LENGTH_LENGTH;
    public static final int MSG_ITEM_CODE_START    = MSG_STATE_START + MSG_STATE_LENGTH;
    public static final int MSG_ITEM_ADDR_START    = MSG_ITEM_CODE_START + MSG_ITEM_CODE_LENGTH;
    public static final int MSG_DATA_START         = MSG_ITEM_ADDR_START + MSG_ITEM_ADDR_LENGTH;
    public static final int MSG_CRC_START          = MSG_DATA_START + MSG_DATA_LENGTH; // n

    // 三.数据包说明
    // 1. 大小端
    // 所有数据都用 16 进制数据表示； 对于多字节数据，低字节在前，高字
    // 节在后，比如 16 位 CRC 校验为 0X1234,发送时的顺序为 34 12
    public static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    // 2. 引导符 上位机为： AA55 下位机为： 55AA
    public static final byte[] SEND_HEAD = {(byte) 0xAA, 0x55};
    public static final byte[] RECV_HEAD = {0x55, (byte) 0xAA};
    // 3. 命令和状态 0X00-0X7F: 正常返回 0X80-0XFE: 不正常返回 0XFF:保留

    // 4. 消息长度 指 [命令/状态][项目代码][项目地址][数据][CRC] 的总长度(字节数)
    public static final int dataLength = 0;
    // 5. 项目代码 体重 ： 0X30 体成分： 0X31
    public static final byte MSG_ITEM_CODE_WEIGHT     = 0x30;
    public static final byte MSG_ITEM_CODE_TICHENGFEN = 0x31;
    // 6. 项目地址 项目地址默认为 0X00
    public static final byte MSG_ITEM_ADDR            = 0x00;
    // 7. 数据 上位机或下位机的数据

    // 8. CRC 校验 CRC--指对前面的整个数据包（不含 CRC 字符）做 16 位 CRC
    public static final int MSG_CRC                   = 0x00;

    public static final byte PROTOCAL_GENDER_FEMALE   = 0x00; //女
    public static final byte PROTOCAL_GENDER_MALE     = 0x01; //男

    // 四.命令说明
    // 1， 0XC0： 停止命令，使下位机停止当前工作，回到空闲等待状态
    public static final byte MSG_CMD_STOP        = (byte) 0xC0;
    // 2， 0XC1： 查询命令，查询下位机的当前状态， 测试数据；如果下位机在测
    // 试中，返回测试中数据，如果测试完成，返回最终测试数据。
    public static final byte MSG_CMD_QUERY       = (byte) 0xC1;
    // 3， 0XC2： 开始测试命令，使下位机开始测试
    public static final byte MSG_CMD_START       = (byte) 0xC2;
    // 4， 0XC5： 读参数， 依次为体重系数（ 2 字节），皮重（ 2 字节，小时点 1 位）
    public static final byte MSG_CMD_READ        = (byte) 0xC5;
    // 5， 0XC6： 写参数， 依次为体重系数（ 2 字节），皮重（ 2 字节，小时点 1 位）
    public static final byte MSG_CMD_WRITE       = (byte) 0xC6;

    // 五，状态说明
    // 1， 正常状态（ 0X00-----0X7F）
    // 1， 0X00：指令正常返回（除查询指令外，其它指令的正常返回）
    public static final byte MSG_STATE_OK        = 0x00;
    // 2， 0X01：开机空闲状态（无测试数据，查询时返回的数据为空）
    public static final byte MSG_STATE_IDLE      = 0x01;
    // 3， 0X02：测试完成状态（有测试数据，查询时返回测试数据）
    public static final byte MSG_STATE_DONE      = 0x02;
    // 4， 0X03：等待测试者就绪状态（无测试数据，查询时返回的数据为空）
    public static final byte MSG_STATE_WAIT      = 0x03;
    // 5， 0X10：体重测试中（只对体重，查询时返回测试中数据）
    public static final byte MSG_STATE_WEIGHTING = 0x10;
    // 6， 0X20：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_1 = 0x20;
    // 7， 0X21：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_2 = 0x21;
    // 8， 0X22：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_3 = 0x22;
    // 9， 0X23：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_4 = 0x23;

    // 2，错误状态（ 0X80-----0XFE）
    // 1， 0X80： 数据包错误
    public static final byte MSG_STATE_ERR_DATA_PACK = (byte) 0X80;
    // 2， 0X81：性别错误
    public static final byte MSG_STATE_ERR_GENDER    = (byte) 0X81;
    // 3， 0X82：年龄错误
    public static final byte MSG_STATE_ERR_AGE       = (byte) 0X82;
    // 4， 0X83：身高错误
    public static final byte MSG_STATE_ERR_HEIGHT    = (byte) 0X83;
    // 5， 0X84：体重错误
    public static final byte MSG_STATE_ERR_WEIGHT    = (byte) 0X84;
    // 6， 0X85： 标准源错误
    public static final byte MSG_STATE_ERR_SOURCE    = (byte) 0X85;
    // 7， 0X90：测试体成分时，有错误
    public static final byte MSG_STATE_ERR_TEST_1    = (byte) 0X90;
    // 8， 0X91：测试体成分时，有错误
    public static final byte MSG_STATE_ERR_TEST_2    = (byte) 0X91;
    // 9， 0X92：测试体成分时，有错误
    public static final byte MSG_STATE_ERR_TEST_3    = (byte) 0X92;
    // 10， 0X93：测试体成分时，有错误
    public static final byte MSG_STATE_ERR_TEST_4    = (byte) 0X93;



    // 六，命令的详细解释
    // [引导符][消息长度] [命令/状态][项目代码][项目地址][数 据] [CRC]
    public static byte[] createCmd(byte cmd, byte code, byte[] data) {
        byte[] tmpDataArray = {};
        if (data != null) {
            tmpDataArray = data;
        }
        int dataLength = tmpDataArray.length;
        // 消息长度 指 [命令/状态][项目代码][项目地址][数据][CRC] 的总长度(字节数)
        int msgLength = MSG_CMD_LENGTH
                        + MSG_ITEM_CODE_LENGTH
                        + MSG_ITEM_ADDR_LENGTH
                        + dataLength
                        + MSG_CRC_LENGTH;
        int BYTE_BUFFER_ALLOCATE = 1024;
        byte[] byteArray = null;
        ByteBuffer target = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
        target.put(SEND_HEAD);
        target.put((byte)(msgLength & 0xFF));
        target.put(cmd);
        target.put(code);
        target.put(MSG_ITEM_ADDR);
        target.put(tmpDataArray);
        int crcEnd = MSG_HEAD_LENGTH + MSG_LENGTH_LENGTH + msgLength - MSG_CRC_LENGTH;
        target.put(calcCRC(Arrays.copyOfRange(target.array(), 0, crcEnd)));

        target.limit(target.remaining());
        target.rewind();
		/* 提取 */
        byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
        target.get(byteArray);
		/* 将byteBuffer清理 */
        target.clear();

        //Log.i(TAG, "" + ByteArrayUtils.bytesToHex(byteArray));
        return byteArray;
    }

    // for test
    // [引导符][消息长度] [命令/状态][项目代码][项目地址][数 据] [CRC]
    public static byte[] createResponse(byte state, byte code, byte[] data) {
        byte[] tmpDataArray = {};
        if (data != null) {
            tmpDataArray = data;
        }
        int dataLength = tmpDataArray.length;
        // 消息长度 指 [命令/状态][项目代码][项目地址][数据][CRC] 的总长度(字节数)
        int msgLength = MSG_STATE_LENGTH
                + MSG_ITEM_CODE_LENGTH
                + MSG_ITEM_ADDR_LENGTH
                + dataLength
                + MSG_CRC_LENGTH;
        int BYTE_BUFFER_ALLOCATE = 1024;
        byte[] byteArray = null;
        ByteBuffer target = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
        target.put(RECV_HEAD);
        target.put((byte)(msgLength & 0xFF));
        target.put(state);
        target.put(code);
        target.put(MSG_ITEM_ADDR);
        target.put(tmpDataArray);
        int crcEnd = MSG_HEAD_LENGTH + MSG_LENGTH_LENGTH + msgLength - MSG_CRC_LENGTH;
        target.put(calcCRC(Arrays.copyOfRange(target.array(), 0, crcEnd)));

        target.limit(target.remaining());
        target.rewind();
		/* 提取 */
        byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
        target.get(byteArray);
		/* 将byteBuffer清理 */
        target.clear();

        Log.i(TAG, "createResponse: " + ByteArrayUtils.bytesToHex(byteArray));
        return byteArray;
    }

    /**
     * STOP
     * @return
     * @throws ProtocalExcption
     */
    private boolean stop(byte item) throws ProtocalExcption {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_STOP, item, null));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }

    /**
     * 停止体重测试
     * @return
     * @throws ProtocalExcption
     */
    public boolean stopWeight() throws ProtocalExcption {
        return stop(MSG_ITEM_CODE_WEIGHT);
    }

    /**
     * 停止体成分测试
     * @return
     * @throws ProtocalExcption
     */
    public boolean stopTichengfen() throws ProtocalExcption {
        return stop(MSG_ITEM_CODE_TICHENGFEN);
    }

    /**
     * 开始体重测试
     * @return
     * @throws ProtocalExcption
     */
    public boolean startWeight() throws ProtocalExcption {
        return start(MSG_ITEM_CODE_WEIGHT, null);
    }

    /**
     * 开始体成分测试
     * 这些硬性条件需要界面做对应的设置
     * @param gender 性别(男-1, 女-0)
     * @param age    年龄(7-99岁)
     * @param height 身高: 900-2200厘米,小数点一位2byte
     * @param weight 体重：100-2000千克，小数点一位2byte
     * @return
     * @throws ProtocalExcption
     */
    public boolean startTichengfen(byte gender, byte age, short height, short weight) throws ProtocalExcption {
        // 判断Gender
        if (!WelcomeActivity.checkGender(gender)) {
            throw new ProtocalExcption.GenderExcetion("need 0->1, but arg is: " + gender);
        }

        if (!WelcomeActivity.checkAge(age)) {
            throw new ProtocalExcption.AgeExcetion("need 7->99, but arg is: " + age);
        }

        if (!WelcomeActivity.checkHeight(height/10)) {
            throw new ProtocalExcption.HeightExcetion("need 900->2200, but arg is: " + height);
        }

        if (!WelcomeActivity.checkWeight(weight/10)) {
            throw new ProtocalExcption.WeightExcetion("need 100->2000, but arg is: " + weight);
        }

        // create data
        byte[] tmpHeight = ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (height & 0xFFFF)).array();
        byte[] tmpWeight = ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (weight & 0xFFFF)).array();
        byte[] data = {gender, age, tmpHeight[0], tmpHeight[1], tmpWeight[0], tmpWeight[1]};
        return start(MSG_ITEM_CODE_TICHENGFEN, data);
    }

    /**
     * 开始体重测试
     * @return
     * @throws ProtocalExcption
     */
    public QueryResult qeuryWeight() throws ProtocalExcption {
        return query(MSG_ITEM_CODE_WEIGHT);
    }

    /**
     * 开始体成分测试
     * @return
     * @throws ProtocalExcption
     */
    public QueryResult qeuryTichengfen() throws ProtocalExcption {
        return query(MSG_ITEM_CODE_TICHENGFEN);
    }

    /**
     * 开始体重测试
     * @return
     * @throws ProtocalExcption
     */
    public boolean readWeight() throws ProtocalExcption {
        byte[] data = read(MSG_ITEM_CODE_WEIGHT);
        return true;
    }

    /**
     * 系数
     */
    public static class Radio {
        /**
         * 体重系数
         */
        int weigthRatio;
        /**
         * 皮重
         */
        int tare;

        public Radio(int weigthRatio, int tare) {
            this.weigthRatio = weigthRatio;
            this.tare = tare;
        }

        public int getWeigthRatio() {
            return weigthRatio;
        }

        public int getTare() {
            return tare;
        }
    }

    /**
     * 开始体成分测试
     * @return
     * @throws ProtocalExcption
     */
    public Radio readTichengfen() throws ProtocalExcption {
        byte[] data = read(MSG_ITEM_CODE_TICHENGFEN);
        Radio radio = null;
        if (data != null && data.length == 4) {
            int weigthRatio = getShortFromData(data, 0);
            int tare = getShortFromData(data, 2);
            radio = new Radio(weigthRatio, tare);
        } else {
            throw new ProtocalExcption.UnkownExcetion();
        }
        return radio;
    }


    /**
     * 开始体重测试
     * @return
     * @throws ProtocalExcption
     */
    public boolean writeWeight() throws ProtocalExcption {
        return write(MSG_ITEM_CODE_WEIGHT, null);
    }

    /**
     * 开始体成分测试
     * @return
     * @throws ProtocalExcption
     */
    public void writeTichengfen(Radio radio) throws ProtocalExcption {
        if (radio == null)
            throw new ProtocalExcption("radio can not be null");
        // create data
        byte[] tmpWeightRadio = ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (radio.getWeigthRatio() & 0xFFFF)).array();
        byte[] tmpTare = ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (radio.getTare() & 0xFFFF)).array();

        byte[] data = {tmpWeightRadio[0], tmpWeightRadio[1], tmpTare[0], tmpTare[1]};

        if (!write(MSG_ITEM_CODE_TICHENGFEN, data)) {
            throw new ProtocalExcption("write error!");
        }
    }

    @Override
    public boolean send(byte[] buf, int timeout) {
        return false;
    }

    @Override
    public byte[] recv(int timeout) {
        return new byte[0];
    }

    /**
     * ery result
     */
    public static class QueryResult {
        byte state;
        byte[] data;

        public void setState(byte state) {
            this.state = state;
        }

        public byte getState() {
            return state;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        public short getShortFromData() {
            if (data == null || data.length == 0)
                return 0;

            byte[] b;
            b = Arrays.copyOfRange(getData(), 0, 2);
            return (short) (ByteBuffer.wrap(b).order(BYTE_ORDER).getShort() & 0xFFFF);
        }
    }

    /**
     * @param data
     * @param start
     * @return
     */
    public static short getShortFromData(byte[] data, int start) {
        if (data == null || data.length < 2)
            return 0;

        if (start > (data.length - 2)) {
            return 0;
        }

        byte[] b;
        b = Arrays.copyOfRange(data, start, start + 2);
        return (short) (ByteBuffer.wrap(b).order(BYTE_ORDER).getShort() & 0xFFFF);
    }

    /**
     * Query
     * @return
     * @throws ProtocalExcption
     */
    private QueryResult query(byte item) throws ProtocalExcption {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            throw new ProtocalExcption.UnkownExcetion();

        // 1. send msg
        if (!send(createCmd(MSG_CMD_QUERY, item, null)))
            throw new ProtocalExcption.UnkownExcetion();

        // 2. recv msg
        return parseQueryMsg(recv(), item);
    }

    /**
     * Start
     * @return
     * @throws ProtocalExcption
     */
    private boolean start(byte item, byte[] data) throws ProtocalExcption {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_START, item, data));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }


    /**
     * Read
     * @return
     * @throws ProtocalExcption
     */
    private byte[] read(byte item) throws ProtocalExcption {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN) {
            throw new ProtocalExcption.UnkownExcetion();
        }

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_READ, item, null));
        if (!ret) {
            throw new ProtocalExcption.UnkownExcetion();
        }

        // 2. recv msg
        return parseMsg(recv(), item, MSG_STATE_OK);
    }


    /**
     * Write
     * @return
     * @throws ProtocalExcption
     */
    private boolean write(byte item, byte[] data) throws ProtocalExcption {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_WRITE, item, data));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }

    private static final byte[] tmpdata = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x28, (byte)0xA4, 0x06, 0x20, 0x03, (byte)0x9D, 0x1A, (byte)0x7D, 0x16, 0x50, 0x00, 0x74, 0x0B,
            0x48, 0x0B, 0x65, 0x01, (byte)0xF9, 0x09, (byte)0x78, 0x09, (byte)0x13, 0x15, (byte)0x53, 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7B, 0x02, 0x16,
            0x02, (byte)0xB5, 0x02, 0x33, 0x02, (byte)0xFC, 0x01, 0x1B, 0x02, (byte)0xED, 0x00, 0x5F, 0x00, 0x7F, 0x00, 0x10, 0x02, (byte)0xD5, 0x01, (byte)0xF5,
            0x01, 0x2F, 0x01, 0x0A, 0x01, 0x4A, 0x01, (byte)0x99, 0x01, 0x6D, 0x01, (byte)0x84, 0x01, 0x77, 0x00, 0x64, 0x00, 0x71, 0x00, (byte)0x23,
            0x24, 0x26, 0x00, (byte)0x81, 0x00, (byte)0x8F, 0x00, (byte)0xA3, 0x00, 0x18, 0x01, (byte)0xF5, 0x00, 0x09, 0x01, 0x23, 0x1F, 0x21, 0x24, 0x1F,
            0x21, 0x19, 0x01, (byte)0xEA, 0x00, (byte)0xF9, 0x00, 0x56, 0x00, 0x56, 0x00, 0x5C, 0x00, 0x5A, 0x00, 0x56, 0x00, 0x5C, 0x00, 0x0F,
            0x06, 0x08, 0x0E, 0x06, 0x08, (byte)0x8C, 0x00, 0x2F, 0x00, 0x3F, 0x00, 0x23, 0x11, 0x17, 0x21, 0x11, 0x17, (byte)0xEB, 0x04, (byte)0x84,
            0x03, 0x4C, 0x04, 0x14, 0x01, (byte)0xB9, 0x00, (byte)0xF0, 0x00, 0x28, 0x01, (byte)0x96, 0x00, (byte)0xC8, 0x00, 0x40, 0x04, 0x3E, 0x04, 0x5D,
            0x04, (byte)0xB1, 0x03, 0x5D, 0x50, 0x5A, 0x1F, 0x1E, 0x23, 0x03, 0x04, (byte)0xF0, 0x04, (byte)0xA4, 0x02, 0x2B, (byte)0xA5, (byte)0x80, 0x7E, (byte)0x80,
            0x2B, (byte)0x80, 0x0F, 0x07, (byte)0xDC, 0x0A};
    private static byte[] sendMsg = null;


    private boolean send(byte[] msg) {
        Log.i(TAG, "send msg: " + bytesToHex(msg));
        if (FAKE_DATA) {
            sendMsg = new byte[msg.length];
            System.arraycopy(msg, 0, sendMsg, 0, msg.length);
        } else {
            return send(msg, 10 * 1000);
        }
        return true;
    }

    public static boolean equals(byte[] a, byte[] a2, int length) {
        if (a==a2)
            return true;
        if (a==null || a2==null)
            return false;

        for (int i=0; i<length; i++)
            if (a[i] != a2[i])
                return false;

        return true;
    }

    private static int times = 0;
    private byte[] recv() {
        if (!FAKE_DATA) {
            return recv(10 * 1000);
        }

        byte[] ret = null;

        // start weight test
        if (Arrays.equals(sendMsg, Protocol.createCmd(MSG_CMD_START, MSG_ITEM_CODE_WEIGHT, null))) {
            return Protocol.createResponse(MSG_STATE_OK, MSG_ITEM_CODE_WEIGHT, null);
        }

        // query weight
        else if (Arrays.equals(sendMsg, Protocol.createCmd(MSG_CMD_QUERY, MSG_ITEM_CODE_WEIGHT, null))) {
            int weight;
            byte state = MSG_STATE_WEIGHTING;
            byte[] arr;
            switch (times) {
                case 1:
                    weight = 728;
                    times ++;
                    break;
                case 2:
                    weight = 730;
                    times ++;
                    break;
                case 3:
                    weight = 1205;
                    state = MSG_STATE_DONE;
                    times = 0;
                    break;
                default:
                    times ++;
                    weight = 0;
                    break;
            }

            arr = ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (weight & 0xFFFF)).array();
            ret = Protocol.createResponse(state, MSG_ITEM_CODE_WEIGHT, arr);
        }

        // query tichengfen
        else if (Arrays.equals(sendMsg, Protocol.createCmd(MSG_CMD_QUERY, MSG_ITEM_CODE_TICHENGFEN, null))) {
            byte state = MSG_STATE_WAIT;
            byte[] data = null;
            Log.i(TAG, "times: " + times);
            switch (times) {
                case 33:
                    state = MSG_STATE_TESTING_1;
                    times ++;
                    break;
                case 54:
                    times ++;
                    state = MSG_STATE_TESTING_2;
                    break;
                case 80:
                    times ++;
                    state = MSG_STATE_TESTING_3;
                    break;
                case 90:
                    times ++;
                    state = MSG_STATE_TESTING_4;
                    break;
                case 100:
                    state = MSG_STATE_DONE;
                    times = 0;
                    data = tmpdata;
                    break;
                default:
                    times ++;
                    break;
            }

            ret = Protocol.createResponse(state, MSG_ITEM_CODE_TICHENGFEN, data);
        }

        else if (Arrays.equals(sendMsg, Protocol.createCmd(MSG_CMD_READ, MSG_ITEM_CODE_TICHENGFEN, null))) {
            byte[] data = new byte[]{0x54, 0x0B, 0x00, 0x00};
            ret = Protocol.createResponse(MSG_STATE_OK, MSG_ITEM_CODE_TICHENGFEN, data);
        }

        else if (Arrays.equals(sendMsg, Protocol.createCmd(MSG_CMD_STOP, MSG_ITEM_CODE_TICHENGFEN, null))) {
            ret = Protocol.createResponse(MSG_STATE_OK, MSG_ITEM_CODE_TICHENGFEN, null);
        }

        else {
            // start tichengfen test
            int length = 6; // 只比前６位
            byte[] tmp1 = Protocol.createCmd(MSG_CMD_START, MSG_ITEM_CODE_TICHENGFEN, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00});
            Log.i(TAG, "create: " + bytesToHex(tmp1));
            if (equals(sendMsg, tmp1, length)) {
                // TODO: 将体成分参数保存下来 for debug
                return Protocol.createResponse(MSG_STATE_OK, MSG_ITEM_CODE_TICHENGFEN, null);
            }
        }
        return ret;
    }

    /**
     * @param msg Message
     * @return QueryResult
     */
    public static QueryResult parseQueryMsg(byte msg[], byte code) throws ProtocalExcption {
        QueryResult qr = new QueryResult();
        byte[] b;
        byte[] ret;
        int tmpInt;
        int end = 0;
        int start = 0;
        int length = 0;
        byte tmpByte;

        if (msg == null)
            throw new ProtocalExcption("parsePackage: msg can not be null");

        // 1. Package Head check
        start = MSG_HEAD_START;
        end = start + MSG_HEAD_LENGTH;
        if (msg.length < end)
            throw new ProtocalExcption("parsePackage: msg.length < end error");
        b = Arrays.copyOfRange(msg, start, end);
        if (!Arrays.equals(b, RECV_HEAD)) {
            Log.e(TAG, "parsePackage: RESPONSE_PACK_HEAD error");
            throw new ProtocalExcption("parsePackage: RESPONSE_PACK_HEAD error");
        }

        // 2. Package Length
        start = MSG_LENGTH_START;
        end = start + MSG_LENGTH_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            throw new ProtocalExcption("parsePackage: response.length error");
        }
        length = msg[start] & 0xFF;

        // 3. state
        start = MSG_STATE_START;
        end = start + MSG_STATE_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            throw new ProtocalExcption("parsePackage: response.length error");
        }
        tmpByte = msg[MSG_STATE_START];
        qr.setState(tmpByte);

        // 4. CODE
        start = MSG_ITEM_CODE_START;
        end = start + MSG_ITEM_CODE_LENGTH;
        if ((msg.length < end) || (msg[MSG_ITEM_CODE_START]  != code)) {
            Log.e(TAG, "parsePackage: response.length error or code error");
            throw new ProtocalExcption("parsePackage: response.length error or code error");
        }

        // 5. ADDR
        start = MSG_ITEM_ADDR_START;
        end = start + MSG_ITEM_ADDR_LENGTH;
        if ((msg.length < end) || (msg[MSG_ITEM_ADDR_START]  != MSG_ITEM_ADDR)) {
            Log.e(TAG, "parsePackage: response.length error or MSG_ITEM_ADDR error");
            throw new ProtocalExcption("parsePackage: response.length error or MSG_ITEM_ADDR error");
        }

        // 6. Package Data
        // 消息长度 指 [命令/状态][项目代码][项目地址][数据][CRC] 的总长度(字节数)
        // total length
        tmpInt = MSG_LENGTH_START + MSG_LENGTH_LENGTH + length;
        int dataLength = tmpInt - (MSG_HEAD_LENGTH + MSG_LENGTH_LENGTH + MSG_STATE_LENGTH + MSG_ITEM_CODE_LENGTH + MSG_ITEM_ADDR_LENGTH + MSG_CRC_LENGTH);
        start = MSG_DATA_START;
        end = start + dataLength;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            throw new ProtocalExcption("parsePackage: response.length error");
        }
        qr.setData(Arrays.copyOfRange(msg, start, end));

        // 8. Package crc
        int startSum = MSG_HEAD_START;
        int endSum = startSum + (tmpInt - MSG_CRC_LENGTH) ;
        start = MSG_DATA_START + dataLength;
        end = start + MSG_CRC_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            throw new ProtocalExcption("parsePackage: response.length error");
        }
        b = Arrays.copyOfRange(msg, start, end);
        byte[] sum = calcCRC(Arrays.copyOfRange(msg, startSum, endSum));
        if (!Arrays.equals(b, sum)) {
            String m = "parsePackage: MSG_CRC error read: " + bytesToHex(b) + " but cal: " + bytesToHex(sum) + "( " + startSum + "-" + endSum + " )";
            Log.e(TAG, m);
            throw new ProtocalExcption(m);
        }

        //Log.e(TAG, "parsePackage: " + bytesToHex(ret));
        return qr;
    }

    private static final int BYTE_BUFFER_ALLOCATE = 1024;
    private ByteBuffer mByteBuffer = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
    /**
     * 将数据连接
     * @param b
     */
    public synchronized byte[] connectData(byte[] b) {
        // 获取目前长度
        mByteBuffer.put(b);
        int len = BYTE_BUFFER_ALLOCATE - mByteBuffer.remaining();
        mByteBuffer.limit(mByteBuffer.remaining());
        mByteBuffer.rewind();
        byte[] msg = new byte[len];
        mByteBuffer.get(msg);

        // 获取head length 字节(3)
        // 1. Package Head check
        int start = MSG_HEAD_START;
        int end = start + MSG_HEAD_LENGTH;
        if (msg.length < end)
            return null;
        b = Arrays.copyOfRange(msg, start, end);
        if (!Arrays.equals(b, RECV_HEAD)) {
            Log.e(TAG, "parsePackage: RESPONSE_PACK_HEAD error");
            mByteBuffer.clear();
            return null;
        }

        // 2. Package Length
        start = MSG_LENGTH_START;
        end = start + MSG_LENGTH_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        int length = msg[start] & 0xFF;
        int totalLength = length + MSG_HEAD_LENGTH + MSG_LENGTH_LENGTH;

        // 总长度不符合
        if (len < totalLength) {
            return null;
        }

        mByteBuffer.limit(mByteBuffer.remaining());
        mByteBuffer.rewind();
        /* 提取 */
        byte[] mCache = new byte[totalLength];
        mByteBuffer.get(mCache);
        /* 将byteBuffer清理 */
        mByteBuffer.clear();
        return mCache;
    }

    /**
     * @param msg response
     * @return data
     */
    public static byte[] parseMsg(byte msg[], byte code, byte state) throws ProtocalExcption {
        QueryResult qr = parseQueryMsg(msg, code);
        if (qr == null)
            return null;

        if (qr.getState() != state) {
            parseErrorState(qr.getState());
            return null;
        }
        return qr.getData();
    }

    public static void parseErrorState(byte state) throws ProtocalExcption {
        switch (state) {
            case MSG_STATE_ERR_DATA_PACK:
                throw new ProtocalExcption.PackageException();
            case MSG_STATE_ERR_GENDER:
                throw new ProtocalExcption.GenderExcetion();
            case MSG_STATE_ERR_AGE:
                throw new ProtocalExcption.AgeExcetion();
            case MSG_STATE_ERR_HEIGHT:
                throw new ProtocalExcption.HeightExcetion();
            case MSG_STATE_ERR_WEIGHT:
                throw new ProtocalExcption.WeightExcetion();
            case MSG_STATE_ERR_SOURCE:
                throw new ProtocalExcption.SourceExcetion();
            case MSG_STATE_ERR_TEST_1:
            case MSG_STATE_ERR_TEST_2:
            case MSG_STATE_ERR_TEST_3:
            case MSG_STATE_ERR_TEST_4:
                throw new ProtocalExcption.TichengfenTestExcetion();
        }
    }

    public static class ProtocalExcption extends Exception {
        public ProtocalExcption() {
        }

        public ProtocalExcption(String message) {
            super(message);
        }

        public static class PackageException extends ProtocalExcption {
            public PackageException() {
            }

            public PackageException(String message) {
                super(message);
            }
        }

        public static class GenderExcetion extends ProtocalExcption {
            public GenderExcetion() {
            }

            public GenderExcetion(String message) {
                super(message);
            }
        }

        public static class AgeExcetion extends ProtocalExcption {
            public AgeExcetion() {
            }

            public AgeExcetion(String message) {
                super(message);
            }
        }

        public static class HeightExcetion extends ProtocalExcption {
            public HeightExcetion(String s) {
                super(s);
            }

            public HeightExcetion() {
            }
        }

        public static class WeightExcetion extends ProtocalExcption {
            public WeightExcetion() {
            }

            public WeightExcetion(String message) {
                super(message);
            }
        }

        public static class SourceExcetion extends ProtocalExcption {
            public SourceExcetion() {
            }

            public SourceExcetion(String message) {
                super(message);
            }
        }

        public static class TichengfenTestExcetion extends ProtocalExcption {
            public TichengfenTestExcetion() {
            }

            public TichengfenTestExcetion(String message) {
                super(message);
            }
        }

        public static class UnkownExcetion extends ProtocalExcption {
            public UnkownExcetion() {
            }

            public UnkownExcetion(String message) {
                super(message);
            }
        }
    }

    /**
     * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
     * @param  buf
     */
    public static byte[] calcCRC(byte[] buf) {
        int crc = CRC_XModem(buf);
        return ByteBuffer.allocate(MSG_CRC_LENGTH).order(BYTE_ORDER).putShort((short) (crc & 0xFFFF)).array();
    }

    public static int CRC_XModem(byte[] bytes){
        int crc = 0x00;          // initial value
        int polynomial = 0x1021;
        for (int index = 0 ; index< bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        //Log.d(TAG, "CRC: " + crc + " SRC: " + bytesToHex(bytes));
        return crc;
    }


    // 七， 16 位的 crc 校验函数
//    short calcrc(unsigned char *ptr, short count)
//    {
//        short crc = 0;
//        char i;
//        while (--count >= 0)
//        {
//            crc = crc ^ (short) *ptr++ << 8;
//            i = 8;
//            do
//            {
//                if (crc & 0x8000)
//                    crc = crc << 1 ^ 0x1021;
//                else
//                    crc = crc << 1;
//            } while(--i);
//        }
//        return (crc);
//    }

    public boolean selfCheck() {
        boolean ret = false;
        try {
            ret = stopTichengfen();
        } catch (ProtocalExcption protocalExcption) {
            protocalExcption.printStackTrace();
        }
        return ret;
    }
}
