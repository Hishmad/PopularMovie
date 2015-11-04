package com.stockita.popularmovie.slidingtabs;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.stockita.popularmovie.detailfragment.DetailFragmentTabThree;
import com.stockita.popularmovie.detailfragment.DetailFragmentTabTwo;
import com.stockita.popularmovie.detailfragment.NewDetailFragment;

/**
 * Created by hishmadabubakaralamudi on 10/22/15.
 */
public class ViewPagerAdapterDetail extends FragmentStatePagerAdapter {

    // Constant
    private static final int FRAGMENT_TAB_ONE = 0;
    private static final int FRAGMENT_TAB_TWO = 1;

    // Member variables
    private CharSequence[] mTitles;
    private int mNumbOfTabs;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterDetail(FragmentManager fm, CharSequence[] titles, int mNumbOfTabnumb) {
        super(fm);
        mTitles = titles;
        mNumbOfTabs = mNumbOfTabnumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        FragmentHolder fragmentHolder = new FragmentHolder();

        switch (position) {
            case FRAGMENT_TAB_ONE:
                return fragmentHolder.newDetailFragment;
            case FRAGMENT_TAB_TWO:
                return fragmentHolder.detailFragmentTabTwo;
            default:
                return fragmentHolder.detailFragmentTabThree;
        }
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return mNumbOfTabs;
    }

    // Helper class to initialize the fragments
    public class FragmentHolder{
        private NewDetailFragment newDetailFragment;
        private DetailFragmentTabTwo detailFragmentTabTwo;
        private DetailFragmentTabThree detailFragmentTabThree;

        public FragmentHolder() {
            newDetailFragment = new NewDetailFragment();
            detailFragmentTabTwo =  new DetailFragmentTabTwo();
            detailFragmentTabThree = new DetailFragmentTabThree();
        }
    }
}
