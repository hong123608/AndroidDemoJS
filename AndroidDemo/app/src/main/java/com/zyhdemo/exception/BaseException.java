package com.zyhdemo.exception;

/**
 * 类名：BaseException.java<br>
 * 描述： 基本异常<br>
 * 创建者： jack<br>
 * 创建日期：2015-4-1<br>
 * 版本：  <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class BaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public BaseException() {
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
    }

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BaseException(Throwable throwable) {
        super(throwable);
    }
}
