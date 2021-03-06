package cn.salesuite.saf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.utils.Preconditions;
import rx.functions.Action3;
import rx.functions.Func2;

/**
 * Created by Tony Shen on 16/2/2.
 */
public class SAFRecycleAdapter<T,VH extends Presenter<T>> extends RecyclerView.Adapter<VH> {

    protected List<T> mList;
    protected Action3<VH, Integer, T> mOnBindViewHolder;
    protected Func2<ViewGroup, Integer, VH> mPresenter;
    private boolean onBindViewHolderSupered;

    public SAFRecycleAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public int getItemCount() {

        if (mList!=null) {
            return mList.size();
        }
        return 0;
    }

    public List<T> getList() {
        return mList;
    }

    public static <T, VH extends Presenter<T>> SAFRecycleAdapter<T, VH> create() {
        return create(new ArrayList<T>());
    }

    public static <T, VH extends Presenter<T>> SAFRecycleAdapter<T, VH> create(List<T> list) {
        return new SAFRecycleAdapter<T,VH>(list);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mPresenter != null)
            return mPresenter.call(parent, viewType);

        return null;
    }

    public SAFRecycleAdapter<T, VH> createPresenter(Func2<ViewGroup, Integer, VH> presenter) {
        mPresenter = presenter;
        return this;
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {

        if (Preconditions.isNotBlank(mList) && mList.get(position)!=null) {
            onBindViewHolderSupered = false;
            onBindViewHolder(viewHolder, position, mList.get(position));
            if (!onBindViewHolderSupered)
                throw new IllegalArgumentException("super.onBindViewHolder() not be called");
        }
    }

    public void onBindViewHolder(VH viewHolder, int position, T item) {
        onBindViewHolderSupered = true;
        if (mOnBindViewHolder == null) {

            mOnBindViewHolder = new Action3<VH, Integer, T>() {
                @Override
                public void call(VH viewHolder, Integer position, T item) {
                    viewHolder.onBind(position,item);
                }
            };
        }
        mOnBindViewHolder.call(viewHolder, position, item);
    }

    public SAFRecycleAdapter<T, VH> bindViewHolder(Action3<VH, Integer, T> onBindViewHolder) {
        mOnBindViewHolder = onBindViewHolder;
        return this;
    }
}
