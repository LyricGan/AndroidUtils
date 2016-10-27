package com.bigkoo.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.view.BasePickerView;
import com.bigkoo.pickerview.view.WheelTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sai on 15/11/22.
 */
public class TimePickerView extends BasePickerView implements View.OnClickListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    private WheelTime wheelTime;
    private TextView tvTitle;

    private OnTimeSelectListener mTimeSelectListener;

    public TimePickerView(Context context, Type type) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer);
        View btnSubmit = findViewById(R.id.btnSubmit);
        View btnCancel = findViewById(R.id.btnCancel);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        View timePicker = findViewById(R.id.timepicker);

        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        wheelTime = new WheelTime(timePicker, type);

        // 默认选中当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 设置可以选择的时间范围
     * @param startYear 开始年份
     * @param endYear 结束年份
     */
    public void setRangeYear(int startYear, int endYear) {
        wheelTime.setStartYear(startYear);
        wheelTime.setEndYear(endYear);
    }

    /**
     * 设置选中时间
     * @param date 时间
     */
    public void setTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date == null) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        } else {
            calendar.setTime(date);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelTime.setPicker(year, month, day, hours, minute);
    }

    /**
     * 指定选中的时间，显示选择器
     * @param date 时间
     */
    public void show(Date date) {
        setTime(date);
        show();
    }

    /**
     * 设置是否循环滚动
     * @param cyclic true or false
     */
    public void setCyclic(boolean cyclic) {
        wheelTime.setCyclic(cyclic);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(TAG_CANCEL)) {
            dismiss();
        } else {
            if (mTimeSelectListener != null) {
                try {
                    Date date = WheelTime.dateFormat.parse(wheelTime.getTime());
                    String moringOrAfter = wheelTime.getMorningOrAfter();
                    mTimeSelectListener.onTimeSelect(date, moringOrAfter);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            dismiss();
        }
    }

    public void setOnTimeSelectListener(OnTimeSelectListener timeSelectListener) {
        this.mTimeSelectListener = timeSelectListener;
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public interface OnTimeSelectListener {

        void onTimeSelect(Date date, String morningAndAfter);
    }

    /**
     * 日期选择模式：所有、年月日上下午、年月日、时分、月日时分、年月
     */
    public enum Type {
        ALL, YEAR_MONTH_DAY_MORNING, YEAR_MONTH_DAY, HOUR_MINUTE, MONTH_DAY_HOUR_MINUTE, YEAR_MONTH
    }
}
