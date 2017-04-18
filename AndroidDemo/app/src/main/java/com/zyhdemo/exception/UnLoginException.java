package com.zyhdemo.exception;
/**
 * 类名： UnLoginException.java<br>
 * 描述： 没有登录异常<br>
 * 创建者： jack<br>
 * 创建日期：2014-5-22<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public class UnLoginException extends HttpException {
    private static final long serialVersionUID = 4410187250003059942L;

    public UnLoginException() {
        super(-3);
    }

}
