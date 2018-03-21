package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseCompatActivity;
import com.lyric.android.app.utils.ActivityUtils;
import com.lyric.android.app.utils.StringUtils;
import com.lyric.android.app.utils.ToastUtils;

/**
 * @author lyricgan
 */
public class PrizeActivity extends BaseCompatActivity {
    private EditText edit_top_total;
    private EditText edit_bottom_total;
    private EditText edit_name;

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
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        edit_top_total = findViewByIdRes(R.id.edit_top_total);
        edit_bottom_total = findViewByIdRes(R.id.edit_bottom_total);
        edit_name = findViewByIdRes(R.id.edit_name);

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

        findViewByIdRes(R.id.btn_record).setOnClickListener(this);
        findViewByIdRes(R.id.btn_show_all).setOnClickListener(this);
        findViewByIdRes(R.id.btn_new_page).setOnClickListener(this);
        findViewByIdRes(R.id.btn_left).setOnClickListener(this);
        findViewByIdRes(R.id.btn_right).setOnClickListener(this);

        initViews();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_record:
                break;
            case R.id.btn_show_all:
                break;
            case R.id.btn_new_page:
                ActivityUtils.startActivity(this, PrizeActivity.class);
                break;
            case R.id.btn_left:
                printResult();
                break;
            case R.id.btn_right:
                clearResult();
                break;
        }
    }

    private void printResult() {
        String topTotalString = edit_top_total.getText().toString().trim();
        String bottomTotalString = edit_bottom_total.getText().toString().trim();
        String nameString = edit_name.getText().toString().trim();

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

        double topTotalValue = StringUtils.parseDouble(topTotalString, 0d);
        double bottomTotalValue = StringUtils.parseDouble(bottomTotalString, 0d);

        double totalValue = (bottom01Double + bottom02Double + bottom03Double) * topTotalValue;
        double letTotalValue = (bottom04Double + bottom05Double + bottom06Double) * bottomTotalValue;

        double homeWin = top01Double * bottom01Double;
        double flat = top02Double * bottom02Double;
        double awayWin = top03Double * bottom03Double;

        double letHomeWin = top04Double * bottom04Double;
        double letFlat = top05Double * bottom05Double;
        double letAwayWin = top06Double * bottom06Double;

        StringBuilder builder = new StringBuilder();
        builder.append("场次 ").append(nameString).append("\n");
        builder.append("胜平负总 ").append(totalValue).append("\t\t").append("让胜平负总 ").append(letTotalValue).append("\n");

        builder.append("\t\t\t\t\t\t主胜\t\t\t\t").append("\t\t\t\t\t\t\t平局\t\t\t\t\t\t").append("\t\t\t\t\t\t客胜\t\t\t\t").append("\n");
        builder.append("\t\t").append(formatDecimals(homeWin)).append("(").append(formatDecimals(homeWin / totalValue)).append(")").append("\t\t")
                .append("\t\t").append(formatDecimals(flat)).append("(").append(formatDecimals(flat / totalValue)).append(")").append("\t\t")
                .append("\t\t").append(formatDecimals(awayWin)).append("(").append(formatDecimals(awayWin / totalValue)).append(")").append("\t\t")
                .append("\n");

        builder.append("\t\t\t\t\t让主胜\t\t\t\t").append("\t\t\t\t\t让平局\t\t\t\t").append("\t\t\t\t\t让客胜\t\t\t\t").append("\n");
        builder.append("\t\t").append(formatDecimals(letHomeWin)).append("(").append(formatDecimals(letHomeWin / letTotalValue)).append(")").append("\t\t")
                .append("\t\t").append(formatDecimals(letFlat)).append("(").append(formatDecimals(letFlat / letTotalValue)).append(")").append("\t\t")
                .append("\t\t").append(formatDecimals(letAwayWin)).append("(").append(formatDecimals(letAwayWin / letTotalValue)).append(")").append("\t\t")
                .append("\n")
                .append("\n");
        double result1 = 0;
        double result2 = 0;
        double result3 = 0;
        double result4 = 0;
        int checkId = radio_group_options.getCheckedRadioButtonId();
        if (checkId == -1) {
            ToastUtils.showShort(AndroidApplication.getContext(), "请选择— +选项");
            return;
        }
        double allTotalValue = totalValue + letTotalValue;
        switch (checkId) {
            case R.id.radio_left:
                result1 = homeWin + letFlat;
                result2 = homeWin + letHomeWin;
                result3 = flat + letAwayWin;
                result4 = awayWin + letAwayWin;

                builder.append("主胜 让球平\t").append("\t主胜 让主胜\t").append("\t平局 让客胜\t").append("\t客胜 让客胜").append("\n");
                break;
            case R.id.radio_right:
                result1 = homeWin + letHomeWin;
                result2 = flat + letHomeWin;
                result3 = awayWin + letFlat;
                result4 = awayWin + letAwayWin;

                builder.append("主胜 让主胜\t").append("\t平局 让主胜\t").append("\t客胜 让球平\t").append("\t客胜 让客胜").append("\n");
                break;
        }
        builder.append("\t\t").append(formatDecimals(result1)).append("\t\t\t\t")
                .append("\t\t").append(formatDecimals(result2)).append("\t\t\t\t")
                .append("\t\t").append(formatDecimals(result3)).append("\t\t\t\t")
                .append("\t\t").append(formatDecimals(result4)).append("\t\t\t\t")
                .append("\n");
        builder.append("\t\t").append(formatDecimals(result1 / allTotalValue)).append("\t\t\t\t\t\t")
                .append("\t\t").append(formatDecimals(result2 / allTotalValue)).append("\t\t\t\t\t\t")
                .append("\t\t").append(formatDecimals(result3 / allTotalValue)).append("\t\t\t\t\t\t")
                .append("\t\t").append(formatDecimals(result4 / allTotalValue)).append("\t\t\t\t\t\t")
                .append("\n");
        double maxResult = Math.max(result1, result2);
        maxResult = Math.max(maxResult, result3);
        maxResult = Math.max(maxResult, result4);

        double minResult = Math.min(result1, result2);
        minResult = Math.min(minResult, result3);
        minResult = Math.min(minResult, result4);

        builder.append("max \t").append(formatDecimals(maxResult)).append("\t min \t").append(formatDecimals(minResult)).append("\n");

        tv_result.setText(builder.toString());
    }

    private String formatDecimals(double value) {
        return StringUtils.formatDecimals(value, 2, false);
    }

    private void clearResult() {
        edit_top_total.setText("1");
        edit_bottom_total.setText("1");
        edit_name.setText("");

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

    private void initViews() {
        edit_top_total.setText("1");
        edit_bottom_total.setText("1");
        edit_name.setText("");
        edit_name.requestFocus();

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
