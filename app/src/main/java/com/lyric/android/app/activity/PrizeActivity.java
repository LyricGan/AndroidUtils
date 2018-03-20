package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseCompatActivity;
import com.lyric.android.app.utils.StringUtils;
import com.lyric.android.app.utils.ToastUtils;
import com.lyric.android.app.widget.TitleBar;

/**
 * @author lyricgan
 */
public class PrizeActivity extends BaseCompatActivity {
    private EditText edit_top_01;
    private EditText edit_top_02;
    private EditText edit_top_03;
    private EditText edit_top_04;
    private EditText edit_top_05;
    private EditText edit_top_06;

    private RadioGroup radio_group_options;

    private EditText edit_bottom_01;
    private EditText edit_bottom_02;
    private EditText edit_bottom_03;
    private EditText edit_bottom_04;
    private EditText edit_bottom_05;
    private EditText edit_bottom_06;

    private TextView tv_result;

    @Override
    public int getLayoutId() {
        return R.layout.activity_prize;
    }

    @Override
    protected void onTitleBarInitialize(TitleBar titleBar, Bundle savedInstanceState) {
        super.onTitleBarInitialize(titleBar, savedInstanceState);
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        edit_top_01 = findViewByIdRes(R.id.edit_top_01);
        edit_top_02 = findViewByIdRes(R.id.edit_top_02);
        edit_top_03 = findViewByIdRes(R.id.edit_top_03);
        edit_top_04 = findViewByIdRes(R.id.edit_top_04);
        edit_top_05 = findViewByIdRes(R.id.edit_top_05);
        edit_top_06 = findViewByIdRes(R.id.edit_top_06);
        radio_group_options = findViewByIdRes(R.id.radio_group_options);
        edit_bottom_01 = findViewByIdRes(R.id.edit_bottom_01);
        edit_bottom_02 = findViewByIdRes(R.id.edit_bottom_02);
        edit_bottom_03 = findViewByIdRes(R.id.edit_bottom_03);
        edit_bottom_04 = findViewByIdRes(R.id.edit_bottom_04);
        edit_bottom_05 = findViewByIdRes(R.id.edit_bottom_05);
        edit_bottom_06 = findViewByIdRes(R.id.edit_bottom_06);
        tv_result = findViewByIdRes(R.id.tv_result);
        findViewByIdRes(R.id.btn_left).setOnClickListener(this);
        findViewByIdRes(R.id.btn_right).setOnClickListener(this);

        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_left:
                printResult();
                break;
            case R.id.btn_right:
                clearResult();
                break;
        }
    }

    private void printResult() {
        String top01String = edit_top_01.getText().toString().trim();
        String top02String = edit_top_02.getText().toString().trim();
        String top03String = edit_top_03.getText().toString().trim();
        String top04String = edit_top_04.getText().toString().trim();
        String top05String = edit_top_05.getText().toString().trim();
        String top06String = edit_top_06.getText().toString().trim();

        String bottom01String = edit_bottom_01.getText().toString().trim();
        String bottom02String = edit_bottom_02.getText().toString().trim();
        String bottom03String = edit_bottom_03.getText().toString().trim();
        String bottom04String = edit_bottom_04.getText().toString().trim();
        String bottom05String = edit_bottom_05.getText().toString().trim();
        String bottom06String = edit_bottom_06.getText().toString().trim();

        double top01Double = StringUtils.parseDouble(top01String, 0d);
        double top02Double = StringUtils.parseDouble(top02String, 0d);
        double top03Double = StringUtils.parseDouble(top03String, 0d);
        double top04Double = StringUtils.parseDouble(top04String, 0d);
        double top05Double = StringUtils.parseDouble(top05String, 0d);
        double top06Double = StringUtils.parseDouble(top06String, 0d);

        double bottom01Double = StringUtils.parseDouble(bottom01String, 0d);
        double bottom02Double = StringUtils.parseDouble(bottom02String, 0d);
        double bottom03Double = StringUtils.parseDouble(bottom03String, 0d);
        double bottom04Double = StringUtils.parseDouble(bottom04String, 0d);
        double bottom05Double = StringUtils.parseDouble(bottom05String, 0d);
        double bottom06Double = StringUtils.parseDouble(bottom06String, 0d);

        double totalValue = bottom01Double
                + bottom02Double
                + bottom03Double
                + bottom04Double
                + bottom05Double
                + bottom06Double
                ;
        StringBuilder builder = new StringBuilder();
        builder.append("总 ").append(totalValue).append("\n");
        double result1 = 0;
        double result2 = 0;
        double result3 = 0;
        double result4 = 0;
        int checkId = radio_group_options.getCheckedRadioButtonId();
        if (checkId == -1) {
            ToastUtils.showShort(AndroidApplication.getContext(), "请选择让与受让选项");
            return;
        }
        switch (checkId) {
            case R.id.radio_left:
                result1 = top01Double * bottom01Double + top05Double * bottom05Double;
                result2 = top01Double * bottom01Double + top04Double * bottom04Double;
                result3 = top02Double * bottom02Double + top06Double * bottom06Double;
                result4 = top03Double * bottom03Double + top06Double * bottom06Double;

                builder.append("主胜，让球平 \t").append(formatDecimals(result1)).append(" vs ").append(formatDecimals(result1 / totalValue)).append("\n")
                        .append("主胜，让主胜 \t").append(formatDecimals(result2)).append(" vs ").append(formatDecimals(result2 / totalValue)).append("\n")
                        .append("平局，让客胜 \t").append(formatDecimals(result3)).append(" vs ").append(formatDecimals(result3 / totalValue)).append("\n")
                        .append("客胜，让客胜 \t").append(formatDecimals(result4)).append(" vs ").append(formatDecimals(result4 / totalValue)).append("\n")
                ;
                break;
            case R.id.radio_right:
                result1 = top01Double * bottom01Double + top04Double * bottom04Double;
                result2 = top02Double * bottom02Double + top04Double * bottom04Double;
                result3 = top03Double * bottom03Double + top05Double * bottom05Double;
                result4 = top03Double * bottom03Double + top06Double * bottom06Double;

                builder.append("主胜，让主胜 \t").append(formatDecimals(result1)).append(" vs ").append(formatDecimals(result1 / totalValue)).append("\n")
                        .append("平局，让主胜 \t").append(formatDecimals(result2)).append(" vs ").append(formatDecimals(result2 / totalValue)).append("\n")
                        .append("客胜，让球平 \t").append(formatDecimals(result3)).append(" vs ").append(formatDecimals(result3 / totalValue)).append("\n")
                        .append("客胜，让客胜 \t").append(formatDecimals(result4)).append(" vs ").append(formatDecimals(result4 / totalValue)).append("\n")
                ;
                break;
        }
        double maxResult = Math.max(result1, result2);
        maxResult = Math.max(maxResult, result3);
        maxResult = Math.max(maxResult, result4);

        double minResult = Math.min(result1, result2);
        minResult = Math.min(minResult, result3);
        minResult = Math.min(minResult, result4);

        builder.append("最大值 \t").append(formatDecimals(maxResult)).append("\n");
        builder.append("最小值 \t").append(formatDecimals(minResult)).append("\n");

        tv_result.setText(builder.toString());
    }

    private String formatDecimals(double value) {
        return StringUtils.formatDecimals(value, 3, false);
    }

    private void clearResult() {
        edit_top_01.setText("");
        edit_top_02.setText("");
        edit_top_03.setText("");
        edit_top_04.setText("");
        edit_top_05.setText("");
        edit_top_06.setText("");

        edit_bottom_01.setText("");
        edit_bottom_02.setText("");
        edit_bottom_03.setText("");
        edit_bottom_04.setText("");
        edit_bottom_05.setText("");
        edit_bottom_06.setText("");

        radio_group_options.clearCheck();
        tv_result.setText("");
    }

    private void initData() {
        edit_top_01.setText("");
        edit_top_02.setText("");
        edit_top_03.setText("");
        edit_top_04.setText("");
        edit_top_05.setText("");
        edit_top_06.setText("");

        edit_bottom_01.setText("");
        edit_bottom_02.setText("");
        edit_bottom_03.setText("");
        edit_bottom_04.setText("");
        edit_bottom_05.setText("");
        edit_bottom_06.setText("");
    }
}
