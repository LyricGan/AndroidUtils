package com.lyric.android.app.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.activity.MainDetailsActivity;
import com.lyric.common.BaseRecyclerAdapter;
import com.lyric.android.app.utils.ActivityUtils;

public class RecyclerViewAdapter extends BaseRecyclerAdapter {

    public RecyclerViewAdapter(Context context) {
        super(context, R.layout.list_item_card_main);
    }

    @Override
    public void convert(final View itemView, int position, Object item) {
        TextView tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
        TextView tvItemContent = (TextView) itemView.findViewById(R.id.tv_item_content);

        tvItemTitle.setText("标题");
        tvItemContent.setText("一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容一段内容");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnimator(itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    private void addAnimator(View itemView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "translationZ", 20, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ActivityUtils.startActivity(getContext(), MainDetailsActivity.class);
            }
        });
        animator.start();
    }
}
