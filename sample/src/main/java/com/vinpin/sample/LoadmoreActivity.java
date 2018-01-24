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
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinpin.adapter.CommonAdapter;
import com.vinpin.adapter.base.ViewHolder;
import com.vinpin.adapter.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

/**
 * 加载更多列表界面
 *
 * @author zwp
 *         create at 2018/01/24 9:44
 */
public class LoadmoreActivity extends Activity {

    RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<String> mInfos;

    /**
     * 创建意图
     *
     * @param context 上下文
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoadmoreActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadmore);
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
        for (int i = 0; i < 10; i++) {
            mInfos.add(i + "、Android 封装的RecyclerView的适配器，支持多种type，添加头部尾部，空布局，点击事件等，简化adapter的使用。");
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

    private LoadMoreWrapper<String> mLoadMoreWrapper;

    private void setRecyclerViewData(@NonNull ArrayList<String> infos) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CommonAdapter<String> mCommonAdapter = new CommonAdapter<String>(mContext, R.layout.item_common, infos) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.txt_content, s);
            }
        };
        mLoadMoreWrapper = new LoadMoreWrapper<>(mCommonAdapter);
        TextView loadmore = new TextView(this);
        loadmore.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        loadmore.setGravity(Gravity.CENTER);
        loadmore.setText("正在加载更多…");
        mLoadMoreWrapper.setLoadMoreView(loadmore);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                // 模拟添加数据
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            mInfos.add("1" + i + "、Android 封装的RecyclerView的适配器，支持多种type，添加头部尾部，空布局，点击事件等，简化adapter的使用。");
                        }
                        // 加载更多完成，不再显示加载更多布局
                        // 若要继续显示加载更多，则设置false
                        mLoadMoreWrapper.setLoadOver(true);
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }
}
