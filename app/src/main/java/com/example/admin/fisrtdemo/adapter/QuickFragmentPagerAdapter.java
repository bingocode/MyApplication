package com.example.admin.fisrtdemo.adapter;





import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by admin on 2017/7/9.
 */

public class QuickFragmentPagerAdapter <T extends Fragment>extends FragmentPagerAdapter {
    private List<T> mList;
    private List<String> mStrings;
    public QuickFragmentPagerAdapter(FragmentManager fm, List<T> list, List<String> titles ) {
        super(fm);
        mList=list;
        mStrings=titles;


    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mStrings == null ? super.getPageTitle(position) : mStrings.get(position);
    }
}
