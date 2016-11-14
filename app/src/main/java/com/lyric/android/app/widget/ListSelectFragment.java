package com.lyric.android.app.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.adapter.BaseRecyclerAdapter;
import com.lyric.android.app.base.Constants;

import java.io.Serializable;
import java.util.List;

/**
 * @author lyricgan
 * @description 列表选择Fragment，继承于{@link BottomSheetDialogFragment}
 * @time 2016/11/14 14:31
 */
public class ListSelectFragment extends BottomSheetDialogFragment {
    private RecyclerView recyclerView;
    private SelectAdapter mAdapter;

    public static ListSelectFragment newInstance(List<SelectItemEntity> dataList) {
        ListSelectFragment fragment = new ListSelectFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRAS_DATA, (Serializable) dataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.bottom_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        List<SelectItemEntity> dataList = (List<SelectItemEntity>) bundle.getSerializable(Constants.EXTRAS_DATA);
        mAdapter = new SelectAdapter(getContext());
        mAdapter.setDataList(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
    }

    public SelectAdapter getAdapter() {
        return mAdapter;
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "list_select_tag");
    }

    class SelectAdapter extends BaseRecyclerAdapter<SelectItemEntity> {

        SelectAdapter(Context context) {
            super(context, R.layout.item_select_list);
        }

        @Override
        public void convert(View itemView, int position, SelectItemEntity item) {
            TextView tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            tvItemTitle.setText(item.getTitle());
        }
    }
}