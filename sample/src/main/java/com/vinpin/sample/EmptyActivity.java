package com.vinpin.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinpin.adapter.CommonAdapter;
import com.vinpin.adapter.base.ViewHolder;
import com.vinpin.adapter.wrapper.EmptyWrapper;

import java.util.ArrayList;

/**
 * 添加空布局列表界面
 *
 * @author zwp
 *         create at 2018/01/24 9:44
 */
public class EmptyActivity extends Activity {

    RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<String> mInfos;

    /**
     * 创建意图
     *
     * @param context 上下文
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, EmptyActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        mContext = this;
        findViewById();
        getDatas();
    }

    private void findViewById() {
        mRecyclerView = findViewById(R.id.recyclerView);
    }

    private void getDatas() {
        mInfos = mInfos == null ? new ArrayList<String>() : mInfos;
        if (!mInfos.isEmpty()) {
            mInfos.clear();
        }
        for (int i = 0; i < 5; i++) {
            mInfos.add("Android 封装的RecyclerView的适配器，支持多种type，添加头部尾部，空布局，点击事件等，简化adapter的使用。");
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

    private EmptyWrapper mEmptyWrapper;

    private void setRecyclerViewData(final @NonNull ArrayList<String> infos) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CommonAdapter<String> mCommonAdapter = new CommonAdapter<String>(mContext, R.layout.item_common_delete, infos) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                holder.setText(R.id.txt_content, s);
                holder.setOnClickListener(R.id.img_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (infos.size() > position) {
                            infos.remove(position);
                            mEmptyWrapper.notifyItemRemoved(position);
                            mEmptyWrapper.notifyItemRangeChanged(position, mEmptyWrapper.getItemCount());
                        }
                    }
                });
            }
        };
        mEmptyWrapper = new EmptyWrapper(mCommonAdapter);
        TextView empty = new TextView(this);
        empty.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        empty.setGravity(Gravity.CENTER);
        empty.setText("我是空布局");
        mEmptyWrapper.setEmptyView(empty);
        mRecyclerView.setAdapter(mEmptyWrapper);
    }
}
