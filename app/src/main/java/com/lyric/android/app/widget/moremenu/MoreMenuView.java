package com.lyric.android.app.widget.moremenu;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.lyric.android.app.R;
import com.lyric.android.app.sharesdk.ShareListAdapter;
import com.lyric.android.app.sharesdk.ShareSdkHelper;

import java.util.List;

public class MoreMenuView extends FrameLayout {
    private RecyclerView recycler_platform_list;

    // 分享平台适配器
    private ShareListAdapter mSharePlatformAdapter;
    // 菜单项列表适配器
    private MenuItemListAdapter mMenuItemListAdapter;

    public MoreMenuView(Context context) {
        this(context, null);
    }

    public MoreMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoreMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        ShareSdkHelper.getInstance(getContext());
        View view = View.inflate(getContext(), R.layout.view_menu_more_layout, this);
        recycler_platform_list = (RecyclerView) view.findViewById(R.id.recycler_platform_list);
        RecyclerView recycler_item_list = (RecyclerView) view.findViewById(R.id.recycler_item_list);

        recycler_platform_list.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recycler_platform_list.setHasFixedSize(true);
        recycler_item_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSharePlatformAdapter = new ShareListAdapter();
        recycler_platform_list.setAdapter(mSharePlatformAdapter);
        mMenuItemListAdapter = new MenuItemListAdapter(getContext());
        recycler_item_list.setAdapter(mMenuItemListAdapter);
    }

    public void showShare() {
        recycler_platform_list.setVisibility(VISIBLE);
    }

    public void hideShare() {
        recycler_platform_list.setVisibility(GONE);
    }

    public void setMenuItemList(List<MenuItemEntity> menuItemEntityList) {
        mMenuItemListAdapter.setDataList(menuItemEntityList);
        mMenuItemListAdapter.notifyDataSetChanged();
    }

    public List<MenuItemEntity> getMenuItemList() {
        return mMenuItemListAdapter.getDataList();
    }

    public ShareListAdapter getShareAdapter() {
        return mSharePlatformAdapter;
    }

    public MenuItemListAdapter getMenuItemAdapter() {
        return mMenuItemListAdapter;
    }
}
