package com.lyric.android.app.widget.moremenu;

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
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.lyric.android.app.R;
import com.lyric.android.app.sharesdk.ShareListAdapter;
import com.lyric.android.app.sharesdk.ShareSdkHelper;

import java.util.ArrayList;
import java.util.List;

public class MoreMenuPopup extends PopupWindow {
    private Context mContext;
    private MoreMenuView mMoreMenuView;
    private ShareSdkHelper.ShareContentParams mShareParams;

    public MoreMenuPopup(Context context) {
        this(context, null);
    }

    public MoreMenuPopup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreMenuPopup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mContext = context;
        FrameLayout rootLayout = new FrameLayout(mContext);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mMoreMenuView = new MoreMenuView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mMoreMenuView.setLayoutParams(params);
        rootLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
                        && isShowing() && event.getAction() == KeyEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
        mMoreMenuView.getShareAdapter().setOnItemClickListener(mOnInnerItemClickListener);
        rootLayout.setOnClickListener(mDismissClickListener);
        rootLayout.addView(mMoreMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setContentView(rootLayout);
        this.setAnimationStyle(R.style.popup_style);
        this.setBackgroundDrawable(new ColorDrawable(0x99000000));
        this.update();
    }

    public void setShareParams(String title, String content, String imageUrl, String targetUrl) {
        this.setShareParams(title, content, imageUrl, targetUrl, "", "", "", "");
    }

    public void setShareParams(String title, String content, String imageUrl, String targetUrl, String type, String targetId) {
        this.setShareParams(title, content, imageUrl, targetUrl, type, targetId, "", "");
    }

    public void setShareParams(String title, String content, String imageUrl, String targetUrl, String type, String targetId, String subtitle) {
        this.setShareParams(title, content, imageUrl, targetUrl, type, targetId, "", subtitle);
    }

    /**
     * 设置分享数据
     * @param title 分享标题
     * @param content 分享内容
     * @param imageUrl 分享图片网络地址
     * @param targetUrl 分享内容网页地址
     * @param type 分享类型
     * @param targetId 分享目标id
     * @param userType 分享的用户类型，只有机构主页和个人主页分享到私信用到
     * @param subtitle 分享到私信的标题和医生圈文献的来源，其他分享不用
     */
    public void setShareParams(String title, String content, String imageUrl, String targetUrl, String type, String targetId, String userType, String subtitle) {
        ShareSdkHelper.ShareContentParams params = new ShareSdkHelper.ShareContentParams();
        params.setTitle(title);
        params.setContent(content);
        params.setImageUrl(imageUrl);
        params.setTargetUrl(targetUrl);
        params.setType(type);
        params.setTargetId(targetId);
        params.setUserType(userType);
        params.setSubtitle(subtitle);
        setShareParams(params);
    }

    public void setShareParams(ShareSdkHelper.ShareContentParams params) {
        mShareParams = params;
    }

    public ShareSdkHelper.ShareContentParams getShareParams() {
        return mShareParams;
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

    void startShowAnimation() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_push_bottom_in);
        animation.setFillAfter(true);
        mMoreMenuView.setAnimation(animation);
        animation.start();
    }

    @Override
    public void dismiss() {
        AnimationSet animation = (AnimationSet) AnimationUtils.loadAnimation(mContext, R.anim.anim_push_bottom_out);
        animation.setFillAfter(true);
        mMoreMenuView.clearAnimation();
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
                        MoreMenuPopup.super.dismiss();
                    }
                });
            }
        });
        mMoreMenuView.setAnimation(animation);
        mMoreMenuView.invalidate();
        animation.startNow();
    }

    private View.OnClickListener mDismissClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private ShareListAdapter.OnRecyclerViewItemClickListener mOnInnerItemClickListener = new ShareListAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View view, ShareSdkHelper.PlatformType platformType) {
            dismiss();
            ShareSdkHelper.getInstance(mContext).share(platformType, mShareParams);
        }
    };

    public void showShare() {
        mMoreMenuView.showShare();
    }

    public void hideShare() {
        mMoreMenuView.hideShare();
    }

    public void setMenuItem(MenuItemType... itemTypes) {
        if (itemTypes == null || itemTypes.length == 0) {
            return;
        }
        List<MenuItemEntity> menuItemEntityList = new ArrayList<>(itemTypes.length);
        MenuItemEntity itemEntity;
        for (MenuItemType itemType : itemTypes) {
            itemEntity = new MenuItemEntity();
            itemEntity.setItemType(itemType);
            menuItemEntityList.add(itemEntity);
        }
        mMoreMenuView.setMenuItemList(menuItemEntityList);
    }

    public void updateMenuItem(MenuItemType itemType, int textId) {
        this.updateMenuItem(itemType, mContext.getString(textId));
    }

    public void updateMenuItem(MenuItemType itemType, String text) {
        List<MenuItemEntity> menuItemEntityList = mMoreMenuView.getMenuItemList();
        if (menuItemEntityList != null && menuItemEntityList.size() > 0) {
            for (MenuItemEntity itemEntity : menuItemEntityList) {
                if (itemType == itemEntity.getItemType()) {
                    itemEntity.setText(text);
                    mMoreMenuView.setMenuItemList(menuItemEntityList);
                    break;
                }
            }
        }
    }

    public void addMenuItem(MenuItemType itemType) {
        List<MenuItemEntity> menuItemEntityList = mMoreMenuView.getMenuItemList();
        if (menuItemEntityList == null) {
            menuItemEntityList = new ArrayList<>();
        }
        MenuItemEntity itemEntity = new MenuItemEntity();
        itemEntity.setItemType(itemType);
        menuItemEntityList.add(itemEntity);

        mMoreMenuView.setMenuItemList(menuItemEntityList);
    }

    public void removeMenuItem(MenuItemType itemType) {
        List<MenuItemEntity> menuItemEntityList = mMoreMenuView.getMenuItemList();
        if (menuItemEntityList != null && menuItemEntityList.size() > 0) {
            for (MenuItemEntity itemEntity : menuItemEntityList) {
                if (itemType == itemEntity.getItemType()) {
                    menuItemEntityList.remove(itemEntity);
                    mMoreMenuView.setMenuItemList(menuItemEntityList);
                    break;
                }
            }
        }
    }

    public void setOnMenuItemClickListener(MenuItemListAdapter.OnMenuItemClickListener listener) {
        mMoreMenuView.getMenuItemAdapter().setOnMenuItemClickListener(listener);
    }

    public void setShareAdapterClickListener(ShareListAdapter.OnRecyclerViewItemClickListener listener) {
        mMoreMenuView.getShareAdapter().setOnItemClickListener(listener);
    }
}
