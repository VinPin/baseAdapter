package com.vinpin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vinpin.adapter.base.ItemViewDelegate;
import com.vinpin.adapter.base.ItemViewDelegateManager;
import com.vinpin.adapter.base.ViewHolder;

import java.util.List;

/**
 * 多种Item布局的Adapter，简化代码
 *
 * @author zwp
 * create at 2018/01/23 13:14
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;

    /**
     * 点击事件最小时间间隔，默认1秒。
     */
    public int MIN_CLICK_DELAY_TIME = 1000;
    /**
     * 标识是否开启防暴力点击
     */
    public boolean forbidFrequentlyClick = true;
    private long lastClickTime = 0;

    private final ItemViewDelegateManager<T> mItemViewDelegateManager;
    private OnItemClickListener mOnItemClickListener;

    private final View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag(R.id.view_tag_base_adapter_holder) == null
                    || v.getTag(R.id.view_tag_base_adapter_position) == null) return;
            ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.view_tag_base_adapter_holder);
            int position = (int) v.getTag(R.id.view_tag_base_adapter_position);
            if (mOnItemClickListener != null) {
                if (forbidFrequentlyClick) {
                    long currentTime = System.currentTimeMillis();
                    if (Math.abs(currentTime - lastClickTime) > MIN_CLICK_DELAY_TIME) {
                        lastClickTime = currentTime;
                        mOnItemClickListener.onItemClick(v, viewHolder, position);
                    }
                } else {
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        }
    };

    private final View.OnLongClickListener mViewLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if (v.getTag(R.id.view_tag_base_adapter_holder) == null
                    || v.getTag(R.id.view_tag_base_adapter_position) == null) {
                return false;
            }
            if (mOnItemClickListener != null) {
                ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.view_tag_base_adapter_holder);
                int position = (int) v.getTag(R.id.view_tag_base_adapter_position);
                return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
            }
            return false;
        }
    };

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) {
            return super.getItemViewType(position);
        }
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewDelegate<T> itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = mDatas.get(position);
        View itemView = holder.getConvertView();
        itemView.setTag(R.id.view_tag_base_adapter_holder, holder);
        itemView.setTag(R.id.view_tag_base_adapter_position, position);
        itemView.setTag(R.id.view_tag_base_adapter_item, item);
        itemView.setOnClickListener(mViewClickListener);
        itemView.setOnLongClickListener(mViewLongClickListener);
        mItemViewDelegateManager.convert(holder, item, position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            T item = mDatas.get(position);
            mItemViewDelegateManager.convertPayloads(holder, item, position, payloads);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        View itemView = holder.getConvertView();
        try {
            int position = (int) itemView.getTag(R.id.view_tag_base_adapter_position);
            T item = (T) itemView.getTag(R.id.view_tag_base_adapter_item);
            mItemViewDelegateManager.convertViewRecycled(holder, item, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        itemView.setTag(R.id.view_tag_base_adapter_holder, null);
        itemView.setTag(R.id.view_tag_base_adapter_position, null);
        itemView.setTag(R.id.view_tag_base_adapter_item, null);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public MultiItemTypeAdapter<T> setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(int viewType, ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        // do nothings.
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }
}
