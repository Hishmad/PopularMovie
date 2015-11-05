package com.stockita.popularmovie.slidingtabs;

import com.stockita.popularmovie.detailfragment.NewDetailFragment;

/*
The MIT License (MIT)

Copyright (c) 2015 Hishmad Abubakar Al-Amudi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.stockita.popularmovie.detailfragment.DetailFragmentTabThree;
import com.stockita.popularmovie.detailfragment.DetailFragmentTabTwo;

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
