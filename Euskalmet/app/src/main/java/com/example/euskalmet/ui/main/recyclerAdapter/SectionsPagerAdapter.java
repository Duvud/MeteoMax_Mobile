package com.example.euskalmet.ui.main.recyclerAdapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.euskalmet.ui.main.fragment.EnabledStationsFragment;
import com.example.euskalmet.ui.main.fragment.MapsFragment;
import com.example.euskalmet.ui.main.fragment.StationListFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[] TAB_TITLES = new String[]{"Map","Stations","Data"};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new MapsFragment();
        }
        if(position == 1){
            return new StationListFragment(mContext);
        }
        if(position == 2){
            return new EnabledStationsFragment(mContext);
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}