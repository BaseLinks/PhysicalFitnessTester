package bodycompositionanalyzer;

import android.util.Log;

import com.kangear.common.utils.ByteArrayUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.kangear.common.utils.ByteArrayUtils.bytesToHex;

/**
 * Created by tony on 18-1-11.
 */

public class Protocol {
    private static final String TAG = "Protocol";

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
    public static final int MSG_CRC             = 0x00;

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
    public static final byte MSG_STATE_IDLE      = 0X01;
    // 3， 0X02：测试完成状态（有测试数据，查询时返回测试数据）
    public static final byte MSG_STATE_DONE      = 0X02;
    // 4， 0X03：等待测试者就绪状态（无测试数据，查询时返回的数据为空）
    public static final byte MSG_STATE_WAIT      = 0X03;
    // 5， 0X10：体重测试中（只对体重，查询时返回测试中数据）
    public static final byte MSG_STATE_WEIGHTING = 0X10;
    // 6， 0X20：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_1 = 0X20;
    // 7， 0X21：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_2 = 0X21;
    // 8， 0X22：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_3 = 0X22;
    // 9， 0X23：测试中正常返回（只对体成分，查询时返回空数据）
    public static final byte MSG_STATE_TESTING_4 = 0X23;

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
    /**
     * query weight
     * @return
     */
    public static byte[] createQueryWeight() {
        return createCmd(MSG_CMD_QUERY, MSG_ITEM_CODE_WEIGHT);
    }

    public static byte[] createStartWeight() {
        return createCmd(MSG_CMD_START, MSG_ITEM_CODE_WEIGHT);
    }

    public static byte[] createStopWeight() {
        return createCmd(MSG_CMD_STOP, MSG_ITEM_CODE_WEIGHT);
    }

    public static byte[] createReadWeight() {
        return createCmd(MSG_CMD_READ, MSG_ITEM_CODE_WEIGHT);
    }

    public static byte[] createWriteWeight() {
        return createCmd(MSG_CMD_WRITE, MSG_ITEM_CODE_WEIGHT);
    }

    /**
     * query weight
     * @return
     */
    public static byte[] createQueryTichengfen() {
        return createCmd(MSG_CMD_QUERY, MSG_ITEM_CODE_TICHENGFEN);
    }

    public static byte[] createStartTichengfen() {
        return createCmd(MSG_CMD_START, MSG_ITEM_CODE_TICHENGFEN);
    }

    public static byte[] createStopTichengfen() {
        return createCmd(MSG_CMD_STOP, MSG_ITEM_CODE_TICHENGFEN);
    }

    public static byte[] createReadTichengfen() {
        return createCmd(MSG_CMD_READ, MSG_ITEM_CODE_TICHENGFEN);
    }

    public static byte[] createWriteTichengfen() {
        return createCmd(MSG_CMD_WRITE, MSG_ITEM_CODE_TICHENGFEN);
    }

    // [引导符][消息长度] [命令/状态][项目代码][项目地址][数 据] [CRC]
    public static byte[] createCmd(byte cmd, byte code) {
        byte[] data = {};
        int dataLength = 0;
        // 消息长度 指 [命令/状态][项目代码][项目地址][数据][CRC] 的总长度(字节数)
        byte msgLength = (byte) ((MSG_CMD_LENGTH
                        + MSG_ITEM_CODE_LENGTH
                        + MSG_ITEM_ADDR_LENGTH
                        + dataLength
                        + MSG_CRC_LENGTH) & 0xFF);
        int BYTE_BUFFER_ALLOCATE = 1024;
        byte[] byteArray = null;
        ByteBuffer target = ByteBuffer.allocate(BYTE_BUFFER_ALLOCATE);
        target.put(SEND_HEAD);
        target.put(msgLength);
        target.put(cmd);
        target.put(code);
        target.put(MSG_ITEM_ADDR);
        target.put(data);
        CRC_XModem(new byte[] {(byte) 0xAA, 0x55, 0x05, (byte) 0xC0, 0x31, 0x00});
        int crcEnd = MSG_HEAD_LENGTH + MSG_LENGTH_LENGTH + msgLength - MSG_CRC_LENGTH;
        target.put(calcCRC(Arrays.copyOfRange(target.array(), 0, crcEnd)));

        target.limit(target.remaining());
        target.rewind();
		/* 提取 */
        byteArray = new byte[BYTE_BUFFER_ALLOCATE - target.remaining()];
        target.get(byteArray);
		/* 将byteBuffer清理 */
        target.clear();

        Log.i(TAG, "" + ByteArrayUtils.bytesToHex(byteArray));
        return byteArray;
    }

    /**
     * STOP
     * @return
     * @throws Exception
     */
    private static boolean stop(byte item) throws Exception {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_STOP, item));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }

    public static boolean stopWeight() throws Exception {
        return stop(MSG_ITEM_CODE_WEIGHT);
    }

    public static boolean stopTichengfen() throws Exception {
        return stop(MSG_ITEM_CODE_TICHENGFEN);
    }

    /**
     * Query
     * @return
     * @throws Exception
     */
    public static boolean query(byte item) throws Exception {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_QUERY, item));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }

    /**
     * Start
     * @return
     * @throws Exception
     */
    public static boolean start(byte item) throws Exception {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_START, item));
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
     * @throws Exception
     */
    public static boolean read(byte item) throws Exception {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_READ, item));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }


    /**
     * Write
     * @return
     * @throws Exception
     */
    public static boolean write(byte item) throws Exception {
        if (item != MSG_ITEM_CODE_WEIGHT && item != MSG_ITEM_CODE_TICHENGFEN)
            return false;

        // 1. send msg
        boolean ret = send(createCmd(MSG_CMD_WRITE, item));
        if (!ret) {
            Log.e(TAG, "send error");
            return false;
        }
        // 2. recv msg
        parseMsg(recv(), item, MSG_STATE_OK);
        return true;
    }

    public static boolean send(byte[] msg) {
        Log.i(TAG, "send msg: " + bytesToHex(msg));
        return true;
    }

    public static byte[] recv() {
        return null;
    }

    /**
     * @param msg response
     * @return data
     */
    public static byte[] parseMsg(byte msg[], byte code, byte state) throws Exception {
        byte[] b;
        byte[] ret;
        int tmpInt;
        int end = 0;
        int start = 0;
        int length = 0;
        byte tmpByte;

        if (msg == null)
            return null;

        // 1. Package Head check
        start = MSG_HEAD_START;
        end = start + MSG_HEAD_LENGTH;
        if (msg.length < end)
            return null;
        b = Arrays.copyOfRange(msg, start, end);
        if (!Arrays.equals(b, RECV_HEAD)) {
            Log.e(TAG, "parsePackage: RESPONSE_PACK_HEAD error");
            return null;
        }

        // 2. Package Length
        start = MSG_LENGTH_START;
        end = start + MSG_LENGTH_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(msg, start, end);
        length = ByteBuffer.wrap(b).order(BYTE_ORDER).getShort() & 0xFFFF;

        // 3. state
        start = MSG_STATE_START;
        end = start + MSG_STATE_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        tmpByte = msg[MSG_STATE_START];
        if (tmpByte != state) {
            parseErrorState(tmpByte);
        }

        // 4. CODE
        start = MSG_ITEM_CODE_START;
        end = start + MSG_ITEM_CODE_LENGTH;
        if ((msg.length < end) || (msg[MSG_ITEM_CODE_START]  != code)) {
            Log.e(TAG, "parsePackage: response.length error or code error");
            return null;
        }

        // 5. ADDR
        start = MSG_ITEM_ADDR_START;
        end = start + MSG_ITEM_ADDR_LENGTH;
        if ((msg.length < end) || (msg[MSG_ITEM_ADDR_START]  != MSG_ITEM_ADDR)) {
            Log.e(TAG, "parsePackage: response.length error or MSG_ITEM_ADDR error");
            return null;
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
            return null;
        }
        ret = Arrays.copyOfRange(msg, start, end);

        // 8. Package crc
        int startSum = MSG_HEAD_START;
        int endSum = startSum + (tmpInt - MSG_CRC_LENGTH) ;
        start = MSG_CRC_START;
        end = start + MSG_CRC_LENGTH;
        if (msg.length < end) {
            Log.e(TAG, "parsePackage: response.length error");
            return null;
        }
        b = Arrays.copyOfRange(msg, start, end);
        byte[] sum = calcCRC(Arrays.copyOfRange(msg, startSum, endSum));
        if (!Arrays.equals(b, sum)) {
            Log.e(TAG, "parsePackage: RESPONSE_SUM error read: " + bytesToHex(b) + " but cal: " + bytesToHex(sum) + "( " + startSum + "-" + endSum + " )");
            return null;
        }

        //Log.e(TAG, "parsePackage: " + bytesToHex(ret));
        return ret;
    }

    public static void parseErrorState(byte state) throws Exception {
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

    public static class ProtocalExcption {
        public static class PackageException extends Exception {
        }

        public static class GenderExcetion extends Exception {
        }

        public static class AgeExcetion extends Exception {
        }

        public static class HeightExcetion extends Exception {
        }

        public static class WeightExcetion extends Exception {
        }

        public static class SourceExcetion extends Exception {
        }

        public static class TichengfenTestExcetion extends Exception {
        }
    }

    /**
     * 对buf中offset以前crcLen长度的字节作crc校验，返回校验结果
     * @param  buf
     */
    public static byte[] calcCRC(byte[] buf) {
        int crc = CRC_XModem(buf);
        // 根据字节序直接返回
        Log.d(TAG, "CRC: " + crc + " SRC: " + bytesToHex(buf));
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
        Log.d(TAG, "CRC: " + crc + " SRC: " + bytesToHex(bytes));
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
}
