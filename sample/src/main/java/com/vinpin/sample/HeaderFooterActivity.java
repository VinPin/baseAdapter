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
import android.view.ViewGroup;
import android.widget.TextView;

import com.vinpin.adapter.CommonAdapter;
import com.vinpin.adapter.base.ViewHolder;
import com.vinpin.adapter.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;

/**
 * 添加头尾布局列表界面
 *
 * @author zwp
 *         create at 2018/01/24 9:44
 */
public class HeaderFooterActivity extends Activity {

    RecyclerView mRecyclerView;

    private Context mContext;
    private ArrayList<String> mInfos;

    /**
     * 创建意图
     *
     * @param context 上下文
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HeaderFooterActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_footer);
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

    private void setRecyclerViewData(@NonNull ArrayList<String> infos) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CommonAdapter<String> mCommonAdapter = new CommonAdapter<String>(mContext, R.layout.item_common, infos) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.txt_content, s);
            }
        };
        HeaderAndFooterWrapper<String> mHeaderAndFooterWrapper = new HeaderAndFooterWrapper<String>(mCommonAdapter);
        TextView header = new TextView(this);
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        header.setGravity(Gravity.CENTER);
        header.setText("我是新增的头布局");
        mHeaderAndFooterWrapper.addHeaderView(header);

        TextView footer = new TextView(this);
        footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footer.setGravity(Gravity.CENTER);
        footer.setText("我是新增的脚布局");
        mHeaderAndFooterWrapper.addFootView(footer);
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }
}
