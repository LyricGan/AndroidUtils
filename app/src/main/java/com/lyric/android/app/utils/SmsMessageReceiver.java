package com.lyric.android.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信监听广播，需要注册权限android.permission.RECEIVE_SMS和android.permission.READ_SMS
 * @author Lyric Gan
 */
public class SmsMessageReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private String mSmsTag;

    public SmsMessageReceiver(String smsTag) {
        this.mSmsTag = smsTag;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        if (SMS_RECEIVED_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus == null || pdus.length == 0) {
                return;
            }
            for (Object pdu : pdus) {
                // 读取所有信息到SmsMessage
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                if (smsMessage == null) {
                    continue;
                }
                String confirmCode = getConfirmCode(smsMessage.getMessageBody(), smsMessage.getOriginatingAddress(), mSmsTag);
                if (!TextUtils.isEmpty(confirmCode)) {
                    handleConfirmCode(confirmCode);
                    break;
                }
            }
        }
    }

    /**
     * 通过短信获取验证码
     * @param smsContent 短信内容
     * @param smsAddress 短信发送号码
     * @param smsTag 短信验证码标识
     * @return 短信验证码
     */
    private String getConfirmCode(String smsContent, String smsAddress, String smsTag) {
        if (!TextUtils.isEmpty(smsContent) && !TextUtils.isEmpty(smsAddress)) {
            if (!TextUtils.isEmpty(smsTag) && smsContent.contains(smsTag)) {
                int index = smsContent.indexOf(smsTag) + smsTag.length();
                return getDigitalFromText(smsContent.substring(index, index + 6));
            }
        }
        return null;
    }

    /**
     * 从字符串中获取数字
     * @param str 字符串
     * @return 数字
     */
    private String getDigitalFromText(String str) {
        final String regularFloat = "(-?[0-9]+)(,[0-9]+)*(\\.[0-9]+)?(\\%)?";
        Pattern pattern = Pattern.compile(regularFloat);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void handleConfirmCode(String confirmCode) {

    }

    public static void register(Context context, String smsTag) {
        SmsMessageReceiver receiver = new SmsMessageReceiver(smsTag);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsMessageReceiver.SMS_RECEIVED_ACTION);
        context.registerReceiver(receiver, intentFilter);
    }
}
