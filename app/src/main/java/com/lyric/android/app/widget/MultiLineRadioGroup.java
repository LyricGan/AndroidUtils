package com.lyric.android.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 自定义多行RadioGroup
 * @author Lyric Gan
 */
public class MultiLineRadioGroup extends RadioGroup {
    private boolean mChildCancelable;

    public MultiLineRadioGroup(Context context) {
        super(context);
    }

    public MultiLineRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof LinearLayout) {
            LinearLayout childLayout = (LinearLayout) child;
            for (int i = 0; i < childLayout.getChildCount(); i++) {
                View childView = childLayout.getChildAt(i);
                if (!(childView instanceof RadioButton)) {
                    continue;
                }
                RadioButton radioButton = (RadioButton) childView;
                addItemClickListener(radioButton);
            }
        }
        super.addView(child, index, params);
    }

    public void setChildCancelable(boolean cancelable) {
        this.mChildCancelable = cancelable;
    }

    public boolean isChildCancelable() {
        return this.mChildCancelable;
    }

    private void addItemClickListener(final RadioButton radioButton) {
        radioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonId = radioButton.getId();
                if (isChildCancelable()) {
                    int checkId = getCheckedRadioButtonId();
                    if (checkId != -1 && checkId == radioButtonId) {
                        clearCheck();
                        return;
                    }
                }
                check(radioButtonId);
                refreshRadioButtons(radioButton);
            }
        });
    }

    private void refreshRadioButtons(RadioButton currentRadioButton) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView instanceof RadioButton) {
                if (childView != currentRadioButton) {
                    ((RadioButton) childView).setChecked(false);
                }
            } else if (childView instanceof LinearLayout) {
                int childCount = ((LinearLayout) childView).getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View itemView = ((LinearLayout) childView).getChildAt(j);
                    if (itemView instanceof RadioButton) {
                        RadioButton radioButton = (RadioButton) itemView;
                        if (radioButton != currentRadioButton) {
                            radioButton.setChecked(false);
                        }
                    }
                }
            }
        }
    }
}
