# BaseAdapter

Android 中 RecyclerView 适配器的高效封装库，支持多条目类型、头部/尾部布局、空布局、加载更多等等，并简化视图绑定与点击事件处理，大幅减少重复适配器代码，提升开发效率。。

1. 简化单类型列表适配器编写，无需手动创建 ViewHolder 和重写繁琐的 RecyclerView.Adapter 方法
2. 灵活支持多条目类型，通过委托模式解耦不同类型条目的逻辑
3. 快速添加多个头部 / 尾部布局，无需手动处理布局类型与位置计算
4. 内置空布局适配，数据为空时自动切换，无需手动判断数据状态
5. 自带加载更多功能，滑动到底部自动触发回调，支持控制加载状态
6. 封装 ViewHolder，提供便捷的视图查找、数据绑定（文本、图片等）、点击事件绑定方法
7. 兼容 RecyclerView 的原有刷新机制，无缝衔接 notifyDataSetChanged/notifyItemXXX 系列方法

## 添加依赖

```kotlin
implementation("com.github.VinPin:baseAdapter:2.1.3")
``` 

## 快速使用

### 1. 单类型条目列表

在convert方法中完成数据、事件绑定，就是这么简单。

```
mRecyclerView.setAdapter(new CommonAdapter<String>(mContext, R.layout.item_common, infos) {
    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.txt_content, s);
    }
});
```

### 2. 多类型条目列表

需要支持多少种条目，就添加多少个对应ItemViewDelegate，其中isForViewType判断当前条目是否为该类型。

```
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
```

### 3. 添加头尾布局

```
CommonAdapter<String> mCommonAdapter = new CommonAdapter<String>(mContext, R.layout.item_common, infos) {
    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.txt_content, s);
    }
};
HeaderAndFooterWrapper<String> mHeaderAndFooterWrapper = new HeaderAndFooterWrapper<String>(mCommonAdapter);
// 添加头布局       
TextView header = new TextView(this);
header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
header.setGravity(Gravity.CENTER);
header.setText("我是新增的头布局");
mHeaderAndFooterWrapper.addHeaderView(header);
// 添加脚布局
TextView footer = new TextView(this);
footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
footer.setGravity(Gravity.CENTER);
footer.setText("我是新增的脚布局");
mHeaderAndFooterWrapper.addFootView(footer);
        
mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
mHeaderAndFooterWrapper.notifyDataSetChanged();
```

可添加多个头尾布局，只需调用addHeaderView和addFootView方法即可。

### 4. 添加加载更多布局

列表滑动到最底部时，自动显示加载更多布局并调用加载更多回调，可以做些网络请求数据。每次加载更多数据完成后，根据实际情况来判断是否还能加载更多，调用setLoadOver()
来控制。

```
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
        // 加载更多完成的开关，用于继续显示或隐藏加载更多布局；若要继续显示加载更多则设置false
        mLoadMoreWrapper.setLoadOver(true);
        mLoadMoreWrapper.notifyDataSetChanged();
    }
});
        
mRecyclerView.setAdapter(mLoadMoreWrapper);
```

### 5. 添加空布局

添加了空布局后，当列表数据为空时就显示该空布局，有数据时则不显示。

```
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
```

## 注意事项

1. 所有 Wrapper 包装适配器后，刷新数据请调用 Wrapper 实例的 notifyXXX 方法，而非原始适配器
2. 多类型条目适配中，isForViewType 方法的判断逻辑需唯一，避免同一数据匹配多个条目类型
3. 加载更多功能中，setLoadOver 方法的调用时机需在数据加载完成后，否则会导致加载状态异常
4. 组合使用 Wrapper 时，建议遵循「空布局 → 头尾布局 → 加载更多」的嵌套顺序，避免布局显示异常
5. 条目点击事件推荐使用 ViewHolder 的 setOnClickListener 方法，无需手动查找控件，简化代码