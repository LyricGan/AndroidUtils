package com.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelOptions;

import java.util.ArrayList;

/**
 * Created by Sai on 15/11/22.
 */
public class OptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private WheelOptions wheelOptions;
    private TextView tvTitle;

    private OnOptionsSelectListener mOptionsSelectListener;

    public OptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer);
        View btnSubmit = findViewById(R.id.btnSubmit);
        View btnCancel = findViewById(R.id.btnCancel);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        View optionsPicker = findViewById(R.id.optionspicker);

        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        wheelOptions = new WheelOptions(optionsPicker);
    }

    public void setPicker(ArrayList<T> optionsItems) {
        wheelOptions.setPicker(optionsItems, null, null, false);
    }

    public void setPicker(ArrayList<T> options1Items, ArrayList<ArrayList<T>> options2Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage);
    }

    public void setPicker(ArrayList<T> options1Items, ArrayList<ArrayList<T>> options2Items,
                          ArrayList<ArrayList<ArrayList<T>>> options3Items, boolean linkage) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items, linkage);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     */
    public void setSelectOptions(int option1) {
        wheelOptions.setCurrentItems(option1, 0, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     * @param option2
     */
    public void setSelectOptions(int option1, int option2) {
        wheelOptions.setCurrentItems(option1, option2, 0);
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     * @param option2
     * @param option3
     */
    public void setSelectOptions(int option1, int option2, int option3) {
        wheelOptions.setCurrentItems(option1, option2, option3);
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     */
    public void setLabels(String label1) {
        wheelOptions.setLabels(label1, null, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     */
    public void setLabels(String label1, String label2) {
        wheelOptions.setLabels(label1, label2, null);
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     * @param label3
     */
    public void setLabels(String label1, String label2, String label3) {
        wheelOptions.setLabels(label1, label2, label3);
    }

    /**
     * 设置是否循环滚动
     * @param cyclic true or false
     */
    public void setCyclic(boolean cyclic) {
        wheelOptions.setCyclic(cyclic);
    }

    public void setCyclic(boolean cyclic1, boolean cyclic2, boolean cyclic3) {
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
        } else {
            if (mOptionsSelectListener != null) {
                int[] optionsCurrentItems = wheelOptions.getCurrentItems();
                mOptionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
            }
            dismiss();
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setOnOptionsSelectListener(OnOptionsSelectListener listener) {
        this.mOptionsSelectListener = listener;
    }

    public interface OnOptionsSelectListener {

        void onOptionsSelect(int options1, int option2, int options3);
    }
}
