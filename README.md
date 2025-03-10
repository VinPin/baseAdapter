# baseAdapter
Android 封装的RecyclerView的适配器，支持多种type，添加头部尾部，空布局，点击事件等，简化adapter的使用。

## 添加依赖
Gradle
```
// 支持androidx
compile 'com.vinpin:baseadapter:2.1.3'
// 不支持androidx
compile 'com.vinpin:baseadapter:1.0.1'
``` 

## 如何使用
1. 单类型条目列表
```
mRecyclerView.setAdapter(new CommonAdapter<String>(mContext, R.layout.item_common, infos) {
    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.txt_content, s);
    }
});
```
在convert方法中完成数据、事件绑定，就是这么简单。

2. 多类型条目列表
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
需要支持多少种条目，就添加多少个对应ItemViewDelegate，其中isForViewType判断当前条目是否为该类型。

3. 添加头尾布局
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

4. 添加加载更多布局
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
列表滑动到最底部时，自动显示加载更多布局并调用加载更多回调，可以做些网络请求数据。每次加载更多数据完成后，根据实际情况来判断是否还能加载更多，调用setLoadOver()来控制。

5. 添加空布局
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
添加了空布局后，当列表数据为空时就显示该空布局，有数据时则不显示。
