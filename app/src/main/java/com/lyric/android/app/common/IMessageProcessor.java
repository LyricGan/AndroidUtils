package com.lyric.android.app.common;

import android.os.Handler;
import android.os.Message;

/**
 * 消息事件处理接口
 * @author lyricgan
 */
public interface IMessageProcessor {

    /**
     * 获取消息处理器
     * @return 消息处理器
     */
    Handler getHandler();

    /**
     * 处理消息回调
     * @param msg 消息实体
     */
    void handleMessage(Message msg);
}
