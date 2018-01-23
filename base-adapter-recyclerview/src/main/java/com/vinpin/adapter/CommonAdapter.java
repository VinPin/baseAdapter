package com.vinpin.adapter;

import android.content.Context;

import com.vinpin.adapter.base.ItemViewDelegate;
import com.vinpin.adapter.base.ViewHolder;

import java.util.List;

/**
 * 单种Item布局的Adapter，简化代码
 *
 * @author zwp
 *         create at 2018/01/23 13:14
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    private int mLayoutId;

    public CommonAdapter(final Context context, int layoutId, List<T> datas) {
        super(context, datas);
        mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return mLayoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

}
