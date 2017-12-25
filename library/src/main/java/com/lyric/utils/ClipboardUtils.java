package com.lyric.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 剪贴板工具类
 * @author lyricgan
 * @date 2017/12/25 14:50
 */
public class ClipboardUtils {

    private ClipboardUtils() {
    }

    public static ClipboardManager getClipboardManager(Context context) {
        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 复制文本到剪贴板
     * @param context 上下文
     * @param label 显示文本
     * @param text 实际复制文本
     * @return true or false
     */
    public static boolean clipText(Context context, CharSequence label, CharSequence text) {
        ClipboardManager clipboardManager = getClipboardManager(context);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText(label, text);
            clipboardManager.setPrimaryClip(clipData);
            return true;
        }
        return false;
    }

    /**
     * 获取剪贴板的复制文本
     * @param context 上下文
     * @return 字符序列数组，包含显示文本和实际复制文本
     */
    public static CharSequence[] getClipboardText(Context context) {
        ClipboardManager clipboardManager = getClipboardManager(context);
        if (clipboardManager != null) {
            if (clipboardManager.hasPrimaryClip()) {
                CharSequence[] charSequences = new CharSequence[2];
                ClipData clipData = clipboardManager.getPrimaryClip();
                ClipDescription clipDescription = clipData.getDescription();
                if (clipDescription != null) {
                    CharSequence label = clipDescription.getLabel();
                    charSequences[0] = label;
                }
                if (clipData.getItemCount() > 0) {
                    CharSequence text = clipData.getItemAt(0).getText();
                    charSequences[1] = text;
                }
                return charSequences;
            }
        }
        return null;
    }
}
