package com.vinpin.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vinpin.adapter.MultiItemTypeAdapter;
import com.vinpin.adapter.base.ItemViewDelegate;
import com.vinpin.adapter.base.ViewHolder;
import com.vinpin.sample.bean.MultiItemTypeInfo;

import java.util.ArrayList;

/**
 * 多条目列表界面
 *
 * @author zwp
 *         create at 2018/01/24 9:44
 */
public class MultiItemTypeActivity extends Activity {

    RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<MultiItemTypeInfo> mInfos;

    /**
     * 创建意图
     *
     * @param context 上下文
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MultiItemTypeActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_item_type);
        mContext = this;
        findViewById();
        getDatas();
    }

    private void findViewById() {
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    private void getDatas() {
        mInfos = mInfos == null ? new ArrayList<MultiItemTypeInfo>() : mInfos;
        if (!mInfos.isEmpty()) {
            mInfos.clear();
        }
        for (int i = 0; i < 10; i++) {
            MultiItemTypeInfo info = new MultiItemTypeInfo();
            info.content = i % 2 == 0 ? "你说啥？？？" : "Android 封装的RecyclerView的适配器，支持多种type，添加头部尾部，空布局，点击事件等，简化adapter的使用。";
            info.type = i % 2 == 0 ? 0 : 1;
            mInfos.add(info);
        }
        setRecyclerViewData(mInfos);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mInfos != null) {
            mInfos.clear();
            mInfos = null;
        }
        super.onDestroy();
    }

    private void setRecyclerViewData(@NonNull ArrayList<MultiItemTypeInfo> infos) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        MultiItemTypeAdapter<MultiItemTypeInfo> mMultiItemTypeAdapter = new MultiItemTypeAdapter<>(mContext, infos);
        mMultiItemTypeAdapter.addItemViewDelegate(new ItemViewDelegate<MultiItemTypeInfo>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_multi_type_left;
            }

            @Override
            public boolean isForViewType(MultiItemTypeInfo item, int position) {
                return item.type == 0;
            }

            @Override
            public void convert(ViewHolder holder, MultiItemTypeInfo multiItemTypeInfo, int position) {
                holder.setText(R.id.txt_content, multiItemTypeInfo.content);
            }
        });
        mMultiItemTypeAdapter.addItemViewDelegate(new ItemViewDelegate<MultiItemTypeInfo>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_multi_type_right;
            }

            @Override
            public boolean isForViewType(MultiItemTypeInfo item, int position) {
                return item.type == 1;
            }

            @Override
            public void convert(ViewHolder holder, MultiItemTypeInfo multiItemTypeInfo, int position) {
                holder.setText(R.id.txt_content, multiItemTypeInfo.content);
            }
        });
        mRecyclerView.setAdapter(mMultiItemTypeAdapter);
    }
}
