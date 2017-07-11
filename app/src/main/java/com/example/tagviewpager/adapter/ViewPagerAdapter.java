package com.example.tagviewpager.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tagviewpager.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * Description：ViewPager适配器
 * Author：lxl
 * Date： 2017/7/3 17:43
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<Integer> mPhotoList;
    private Context mContext;
    private OnViewPagerClickListener mListener;

    public void setOnViewPagerClickListener(OnViewPagerClickListener listener) {
        mListener = listener;
    }

    public ViewPagerAdapter(List<Integer> urls, Context context) {
        mPhotoList = urls;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        Integer urlid = mPhotoList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_item, container, false);
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        Glide.with(view.getContext())
                .load(urlid)
                .fitCenter()
                .crossFade()
                .into(photoView);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickListener(v, position);
                }
            }
        });
        container.addView(view);
        return view;
    }


    public interface OnViewPagerClickListener {
        void onClickListener(View view, int position);

    }


}

