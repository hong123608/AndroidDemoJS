package com.zyhdemo.exception;

import com.zyhdemo.network.DataMessage;

/**
 * 类名： HttpException.java<br>
 * 描述： http异常<br>
 * 创建者： jack<br>
 * 创建日期：2014-5-22<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class HttpException extends BaseException {

    private static final long serialVersionUID = 1L;

    public static final String ERROR_UNKNOW = "unknowError";
    public static final String ERROR_JSONFORMAT = "jsonFormatError";

    protected String mCode = HttpException.ERROR_UNKNOW;

    @Override
    public String getMessage() {
        return DataMessage.getMessage(mCode, super.getMessage());
    }

    public HttpException(boolean useAsMessage, String code, Throwable cause) {
        super(useAsMessage ? code : DataMessage.getMessage(code), cause);
        mCode = useAsMessage ? null : code;
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }

    public HttpException(boolean useAsMessage, String code) {
        super(useAsMessage ? code : DataMessage.getMessage(code));
        mCode = useAsMessage ? null : code;
    }

    public HttpException(String code, Throwable cause) {
        this(false, code, cause);
    }

    public HttpException(String code) {
        this(false, code);
    }

    public HttpException() {
    }

    public HttpException(int code) {
        this("" + code);
    }

    public HttpException(String code, String arg, Throwable cause) {
        super(DataMessage.getMessage(code), cause);
        mCode = code;
    }

    public HttpException(String code, String arg) {
        super(DataMessage.getMessage(code));
        mCode = code;
    }

    public String getCode() {
        return mCode;
    }
}
