package com.lyric.android.app.utils;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder工具类
 * @author lyricgan
 * @date 2017/8/11 10:41
 */
public class ViewHolderUtils {

    public static <T extends View> T get(View convertView, int id) {
        if (convertView == null) {
            return null;
        }
        SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            convertView.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = convertView.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public static void setText(View convertView, int id, int textId) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            textView.setText(textId);
        }
    }

    public static void setText(View convertView, int id, String text) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public static void setTextWithColor(View convertView, int id, String text, int color) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            textView.setText(text);
            textView.setTextColor(color);
        }
    }

    public static void setTextWithBackgroundResource(View convertView, int id, String text, int backgroundId) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            textView.setText(text);
            textView.setBackgroundResource(backgroundId);
        }
    }

    public static void setTextWithBackgroundColor(View convertView, int id, String text, int color) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            textView.setText(text);
            textView.setBackgroundColor(color);
        }
    }

    public static void setTextWithVisibility(View convertView, int id, String text, boolean isInvisible) {
        TextView textView = get(convertView, id);
        if (textView != null) {
            if (TextUtils.isEmpty(text)) {
                if (isInvisible) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                }
            } else {
                textView.setVisibility(View.VISIBLE);
                textView.setText(text);
            }
        }
    }

    public static void setViewBackgroundResource(View convertView, int id, int backgroundId) {
        View view = get(convertView, id);
        if (view != null) {
            view.setBackgroundResource(backgroundId);
        }
    }

    public static void setViewBackgroundColor(View convertView, int id, int color) {
        View view = get(convertView, id);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    public static void setImageResource(View convertView, int id, int resId) {
        ImageView imageView = get(convertView, id);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }
}
