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

public interface IProtocol {
    /**
     * @param buf
     * @param timeout millisecond
     * @return
     */
    boolean send(byte[] buf, int timeout);

    /**
     * @param timeout millisecond
     * @return
     */
    byte[] recv(int timeout);
}
