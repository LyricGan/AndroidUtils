package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 * @author lyricgan
 * @time 2016/4/6 11:14
 */
public class InputMethodUtils {

    private InputMethodUtils() {
    }

    /**
     * 输入限制小数点两位
     */
    public static final InputFilter DECIMAL_INPUT_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String destValue = dest.toString();
            if (destValue.contains(".")) {
                String[] values = destValue.split("\\.");
                if (values.length > 1) {
                    String rightValue = values[1];
                    if (rightValue.length() > 1 && (dstart > (destValue.length() - 1))) {
                        source = "";
                    }
                }
            } else if (".".equals(source.toString())) {
                if (dstart < (destValue.length() - 2)) {
                    source = "";
                }
            }
            return source;
        }
    };

    private static InputMethodManager getInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static void show(View view) {
        InputMethodManager imm = getInputMethodManager(view.getContext());
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void show(View view, int flags) {
        InputMethodManager imm = getInputMethodManager(view.getContext());
        if (imm == null) {
            return;
        }
        imm.showSoftInput(view, flags);
    }

    public static void hide(EditText editText) {
        InputMethodManager imm = getInputMethodManager(editText.getContext());
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hide(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm = getInputMethodManager(activity);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggle(Context context) {
        InputMethodManager imm = getInputMethodManager(context);
        if (imm == null) {
            return;
        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isActive(EditText editText) {
        InputMethodManager imm = getInputMethodManager(editText.getContext());
        return imm != null && imm.isActive();
    }

    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangedListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }
                previousKeyboardHeight = height;
            }
        });
    }

    /**
     * 软键盘变化监听事件
     */
    public interface OnSoftKeyboardChangedListener {

        void onSoftKeyBoardChange(int height, boolean visible);
    }
}
