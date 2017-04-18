package com.zyhdemo.exception;

/**
 * 类名：GBRunException.java<br>
 * 描述： 自定义运行时异常<br>
 * 创建者： jack<br>
 * 创建日期：2015-4-1<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class GBRunException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GBRunException() {
        super();
    }

    public GBRunException(String msg) {
        super(msg);
    }

    public GBRunException(Throwable ex) {
        super(ex);
    }

    public GBRunException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
