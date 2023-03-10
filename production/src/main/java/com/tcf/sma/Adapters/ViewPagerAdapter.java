package com.tcf.sma.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList;
    private final List<String> mFragmentTitleList;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentTitleList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int index = mFragmentTitleList.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return mFragmentList.size() > 0 ? mFragmentList.size() : 0;
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void clearFrag() {
        try {
            if (mFragmentList.size() > 0)
                mFragmentList.clear();
            if (mFragmentTitleList.size() > 0)
                mFragmentTitleList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}
