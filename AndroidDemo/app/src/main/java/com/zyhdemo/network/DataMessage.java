package com.zyhdemo.network;

import java.util.HashMap;
import java.util.Map;
/**
 * 类名： GBMessage.java<br>
 * 描述： 提示信息封装抽象类，负责加载常用提示<br>
 * 创建者： jack<br>
 * 创建日期：2012-11-13<br>
 * 版本： <br>
 * 修改者： <br>
 * 修改日期：<br>
 */
public abstract class DataMessage {
    // 消息集合
    protected static final Map<String, String> mMsgs = new HashMap<String, String>();

    /**
     * 功能描述： 加载消息的抽象方法<br>
     * 创建者： jack<br>
     * 创建日期：2012-11-13<br>
     */
    public abstract void loadMsg();
    /**
     * 功能描述： 重新加载消息的抽象方法<br>
     * 创建者： jack<br>
     * 创建日期：2012-11-13<br>
     */
    public abstract void reloadMsg();

    /**
     * 功能描述： 根据消息码获取消息内容<br>
     * 创建者： jack<br>
     * 创建日期：2012-11-13<br>
     *
     * @param code 消息码
     */
    public static String getMessage(String code, String defaultMsg) {
        String msg = mMsgs.get(code);
        return msg == null ? defaultMsg : msg;
    }
    public static String getMessage(String code) {
        return getMessage(code, "no error code");
    }
    public static String getMessage(int code) {
        return getMessage("" + code, "no error code");
    }
}
