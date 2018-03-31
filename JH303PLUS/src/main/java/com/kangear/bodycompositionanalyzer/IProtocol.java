package com.kangear.bodycompositionanalyzer;

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
