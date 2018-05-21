package edu.brandeis.cs.walkingfoodies.walkingfood.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.brandeis.cs.walkingfoodies.walkingfood.backend.CaloriesBackend;
import edu.brandeis.cs.walkingfoodies.walkingfood.backend.MainBackend;
import edu.brandeis.cs.walkingfoodies.walkingfood.fragments.FoodFragment;
import edu.brandeis.cs.walkingfoodies.walkingfood.fragments.HistoryFragment;
import edu.brandeis.cs.walkingfoodies.walkingfood.fragments.ProfileFragment;
import edu.brandeis.cs.walkingfoodies.walkingfood.fragments.WalkFragment;

/**
 * Created by 聂聂聂 on 11/29/2016.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FoodFragment();
            case 1:
                return new WalkFragment();
            case 2:
                return new HistoryFragment();
            case 3:
                return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
