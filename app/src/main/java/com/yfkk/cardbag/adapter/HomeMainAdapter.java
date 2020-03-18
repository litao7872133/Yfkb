package com.yfkk.cardbag.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yfkk.cardbag.R;
import com.yfkk.cardbag.bean.home.StoreInfoBean;
import com.yfkk.cardbag.log.LogUtils;
import com.yfkk.cardbag.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

/**
 * Created by litao on 2020/3/13.
 */
public class HomeMainAdapter extends BaseAdapter {

    public final static int TYPE_HEAD = 1; // 头部
    public final static int TYPE_CONTENT = 2; // 内容

    Context context;
    List datas = new ArrayList<StoreInfoBean>(); // 数据源

    int[] typeResources = new int[]{R.mipmap.ic_type01, R.mipmap.ic_type02, R.mipmap.ic_type03,
            R.mipmap.ic_type04, R.mipmap.ic_type05, R.mipmap.ic_type06,
            R.mipmap.ic_type07, R.mipmap.ic_type08, R.mipmap.ic_type09,
            R.mipmap.ic_type10,};
    String[] typeNames = new String[]{"丽人美发", "美食", "生活服务"
            , "家具家装", "健身运动", "KTV"
            , "学习培训", "附近商圈", "休闲娱乐"
            , "全部分类"};

    public HomeMainAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size() + 1 + 110;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        LogUtils.e("onCreateHolder :  " + viewType);
        if (viewType == TYPE_HEAD) {
            return new HeadHolder(LayoutInflater.from(context).inflate(R.layout.item_home_main_head, parent,
                    false));
        } else {
            return new ContentHolder(LayoutInflater.from(context).inflate(R.layout.item_home_main_content, parent,
                    false));
        }
    }

    @Override
    public void onBindHolder(RecyclerView.ViewHolder viewHolder, int position) {
        LogUtils.e("onBindHolder :  " + position);
        if (getItemType(position) == TYPE_HEAD) {
            HeadHolder headHolder = ((HeadHolder) viewHolder);
            headHolder.viewPager.getLayoutParams().height = (int) (StringUtils.getWidthPixels(context) * 0.3426f);
            headHolder.viewPager.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return Integer.MAX_VALUE;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View view = View.inflate(context, R.layout.item_home_main_ad, null);
                    ImageView imgContent = (ImageView) view.findViewById(R.id.img_content);
                    imgContent.setImageResource(R.mipmap.img_banner);
                    container.addView(view);
                    return view;
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    container.removeView((View) object);
                }
            });
            headHolder.recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
            headHolder.recyclerView.setAdapter(new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                    return new TypeHolder(LayoutInflater.from(context).inflate(R.layout.item_home_main_type, parent,
                            false));
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    TypeHolder typeHolder = ((TypeHolder) viewHolder);
                    typeHolder.imgContent.setImageResource(typeResources[i]);
                    typeHolder.textTitle.setText(typeNames[i]);
                }

                @Override
                public int getItemCount() {
                    return typeResources.length;
                }
            });

        } else {
            ContentHolder contentHolder = ((ContentHolder) viewHolder);
            int width = (StringUtils.getWidthPixels(context) - StringUtils.dpToPx(context, 36)) / 2;
            contentHolder.imgContent.getLayoutParams().height = position % 2 == 0 ? width : width * 4 / 3;
            contentHolder.textTitle.setText(position + "上海普陀区，真北路1817弄（长征家苑）34号");
        }
    }

    @Override
    public int getItemType(int position) {
        return position == 0 ? TYPE_HEAD : TYPE_CONTENT;
    }

    public class TypeHolder extends RecyclerView.ViewHolder {

        public ImageView imgContent;
        public TextView textTitle;

        public TypeHolder(View itemView) {
            super(itemView);
            imgContent = itemView.findViewById(R.id.img_content);
            textTitle = itemView.findViewById(R.id.text_title);
        }
    }


    public class HeadHolder extends RecyclerView.ViewHolder {

        ViewPager viewPager;
        RecyclerView recyclerView;

        public HeadHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewpager);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        public View card_view;
        public ImageView imgContent;
        public TextView textTitle;
        public TextView textPlace;
        public TextView textRecharge01;

        public ContentHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            imgContent = itemView.findViewById(R.id.img_content);
            textTitle = itemView.findViewById(R.id.text_title);
            textPlace = itemView.findViewById(R.id.text_place);
            textRecharge01 = itemView.findViewById(R.id.text_recharge01);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            // 第一行占用2格
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }

    }

}
