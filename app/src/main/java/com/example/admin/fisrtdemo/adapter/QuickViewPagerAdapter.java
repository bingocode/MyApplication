package com.example.admin.fisrtdemo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 2017/7/9.
 */

public class QuickViewPagerAdapter <T extends View> extends PagerAdapter{

    private List<T>  mList;
    private List<String> mtitles;
    public QuickViewPagerAdapter(List<T> mList,List<String> titles) {
        this.mList = mList;
        this.mtitles=titles;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object==view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(position));
        return mList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mtitles==null?super.getPageTitle(position):mtitles.get(position);
    }
}
