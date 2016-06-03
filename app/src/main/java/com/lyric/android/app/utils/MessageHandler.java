package com.lyric.android.app.utils;

import android.os.Message;

import com.lyric.android.app.activity.SplashActivity;
import com.lyric.android.app.handler.WeakHandler;

/**
 * @author lyric
 * @description 自定义Handler，处理异步消息
 * @time 2015/11/19 20:41
 */
public class MessageHandler extends WeakHandler<Object> {

    public MessageHandler(Object object) {
        super(object);
    }

    /**
     * 消息标识
     */
    public final class MessageCode {
        /** 消息标识：首页启动 */
        public static final int ACTIVITY_START = 0x1011;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int code = msg.what;
        if (MessageCode.ACTIVITY_START == code) {// 首页启动
            Object object = get();
            if (object instanceof SplashActivity) {
                SplashActivity activity = (SplashActivity) object;
                activity.start();
            }
        }
    }
}
