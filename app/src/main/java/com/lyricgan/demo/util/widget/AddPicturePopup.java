package com.lyricgan.demo.util.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lyricgan.demo.util.R;
import com.lyricgan.util.DisplayUtils;

/**
 * @author Lyric Gan
 * @since 2016/7/5 13:39
 */
public class AddPicturePopup extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View addPictureMenuView;

    private OnMenuClickListener mMenuClickListener;

    public interface OnMenuClickListener {

        void takePhoto(PopupWindow window);

        void openPhotoAlbum(PopupWindow window);
    }

    public AddPicturePopup(Context context) {
        this(context, null);
    }

    public AddPicturePopup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddPicturePopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mContext = context;
        FrameLayout rootLayout = new FrameLayout(context);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addPictureMenuView = View.inflate(context, R.layout.view_menu_add_picture, new FrameLayout(context));
        TextView tv_take_photo = (TextView) addPictureMenuView.findViewById(R.id.tv_take_photo);
        TextView tv_photo_album = (TextView) addPictureMenuView.findViewById(R.id.tv_photo_album);
        Button btn_cancel = (Button) addPictureMenuView.findViewById(R.id.btn_cancel);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dip_10 = DisplayUtils.dip2px(context, 10);
        btnParams.leftMargin = dip_10;
        btnParams.rightMargin = dip_10;
        btnParams.topMargin = dip_10 / 2;
        btnParams.bottomMargin = dip_10 / 2;
        btn_cancel.setLayoutParams(btnParams);

        tv_take_photo.setOnClickListener(this);
        tv_photo_album.setOnClickListener(this);
        btn_cancel.setOnClickListener(mDismissClickListener);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        addPictureMenuView.setLayoutParams(params);
        rootLayout.setOnKeyListener(mOnKeyListener);
        rootLayout.setOnClickListener(mDismissClickListener);
        rootLayout.addView(addPictureMenuView);

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setContentView(rootLayout);
        this.setAnimationStyle(R.style.popup_style);
        this.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.half_transparent)));
        this.update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_photo: {
                dismiss();
                if (mMenuClickListener != null) {
                    mMenuClickListener.takePhoto(this);
                }
            }
                break;
            case R.id.tv_photo_album: {
                dismiss();
                if (mMenuClickListener != null) {
                    mMenuClickListener.openPhotoAlbum(this);
                }
            }
                break;
        }
    }

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
                    && isShowing() && event.getAction() == KeyEvent.ACTION_UP) {
                dismiss();
            }
            return true;
        }
    };

    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.mMenuClickListener = listener;
    }

    public void show(View parent) {
        this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

    public void show(View parent, int x, int y) {
        this.showAtLocation(parent, Gravity.BOTTOM, x, y);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        startShowAnimation();
    }

    @Override
    public void showAsDropDown(View anchor) {
        this.showAsDropDown(anchor, 0, 0);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        this.showAsDropDown(anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        startShowAnimation();
    }

    private void startShowAnimation() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_push_bottom_in);
        animation.setFillAfter(true);
        addPictureMenuView.setAnimation(animation);
        animation.start();
    }

    @Override
    public void dismiss() {
        AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_push_bottom_out);
        animation.setFillAfter(true);
        addPictureMenuView.clearAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        AddPicturePopup.super.dismiss();
                    }
                });
            }
        });
        addPictureMenuView.setAnimation(animation);
        addPictureMenuView.invalidate();
        animation.startNow();
    }

    private View.OnClickListener mDismissClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
