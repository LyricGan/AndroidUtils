package com.lyric.android.app.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.activity.MainDetailsActivity;

public class RecyclerViewAdapter extends BaseRecyclerAdapter {

    public RecyclerViewAdapter(Context context) {
        super(context, R.layout.list_item_card_main);
    }

    @Override
    public void convert(final View itemView, int position, Object item) {
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
                getContext().startActivity(new Intent(getContext(), MainDetailsActivity.class));
            }
        });
        animator.start();
    }
}
