package com.kangear.bodycompositionanalyzer.entry;

public class SchoopiaAddResult {
    /**
     * code : -4
     * data : {}
     * desc : 数据已经存在
     * errCode : 500
     * errMsg : 数据已经存在
     * exception : com.yuhong.core.BusinessException
     * rpcMsg :
     * success : false
     * throwable : {"cause":null,"stackTrace":[{"methodName":"addData","fileName":"PhysicalService.java","lineNumber":99,"className":"com.yuhong.chiron.sport.service.PhysicalService","nativeMethod":false}],"message":"数据已经存在","localizedMessage":"数据已经存在","suppressed":[]}
     * time : 1564040990
     */

    private int code;
    private DataBean data;
    private String desc;
    private String errCode;
    private String errMsg;
    private String exception;
    private String rpcMsg;
    private boolean success;
    private String throwable;
    private int time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getRpcMsg() {
        return rpcMsg;
    }

    public void setRpcMsg(String rpcMsg) {
        this.rpcMsg = rpcMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public static class DataBean {
    }

    @Override
    public String toString() {
        return "SchoopiaAddResult{" +
                "code=" + code +
                ", data=" + data +
                ", desc='" + desc + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errMsg='" + errMsg + '\'' +
                ", exception='" + exception + '\'' +
                ", rpcMsg='" + rpcMsg + '\'' +
                ", success=" + success +
                ", throwable='" + throwable + '\'' +
                ", time=" + time +
                '}';
    }
}
