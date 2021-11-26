package com.vinpin.adapter.base;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * 条目的代表
 *
 * @author zwp
 * create at 2018/01/23 13:14
 */
public abstract class ItemViewDelegate<T> {

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);

    public void convertPayloads(ViewHolder holder, T t, int position, @NonNull List<Object> payloads) {
        convert(holder, t, position);
    }
}
