package com.kangear.bodycompositionanalyzer.entry;

import com.google.gson.annotations.SerializedName;

public class HttpResult<T> {
    @SerializedName("data")
    public T data;
    @SerializedName("msg")
    public String msg;
    @SerializedName("ret")
    public int ret;

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public int getRet() {
        return ret;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "data=" + data +
                ", msg='" + msg + '\'' +
                ", ret=" + ret +
                '}';
    }
}
