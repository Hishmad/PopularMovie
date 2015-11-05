package com.stockita.popularmovie.slidingtabs;

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


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieNowPlaying;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMoviePopular;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieSearch;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieUpcoming;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieRating;
import com.stockita.popularmovie.utility.Utilities;


/**
 * Created by hishmadabubakaralamudi on 10/16/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    // Constant
    private static final int FRAGMENT_TAB_ONE = 0;
    private static final int FRAGMENT_TAB_TWO = 1;
    private static final int FRAGMENT_TAB_THREE = 2;
    private static final int FRAGMENT_TAB_FOUR = 3;

    // Member variable
    private CharSequence[] mTitles;
    private int mNumbOfTabs;
    private Context mContext;


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titles, int numbOfTabsumb, Context context) {
        super(fm);
        mTitles = titles;
        mNumbOfTabs = numbOfTabsumb;
        mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        FragmentHolder fragmentHolder = new FragmentHolder();

        switch (position) {
            case FRAGMENT_TAB_ONE:
                Utilities.hideKeyboard((Activity) mContext);
                return fragmentHolder.nowPlaying;
            case FRAGMENT_TAB_TWO:
                Utilities.hideKeyboard((Activity) mContext);
                return fragmentHolder.upcoming;
            case FRAGMENT_TAB_THREE:
                Utilities.hideKeyboard((Activity) mContext);
                return fragmentHolder.search;
            case FRAGMENT_TAB_FOUR:
                Utilities.hideKeyboard((Activity) mContext);
                return fragmentHolder.popularities;
            default:
                Utilities.hideKeyboard((Activity) mContext);
                return fragmentHolder.rating;
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
    public class FragmentHolder {
        private RecyclerViewFragmentMoviePopular popularities;
        private RecyclerViewFragmentMovieUpcoming upcoming;
        private RecyclerViewFragmentMovieNowPlaying nowPlaying;
        private RecyclerViewFragmentMovieRating rating;
        private RecyclerViewFragmentMovieSearch search;

        public FragmentHolder() {
            popularities = new RecyclerViewFragmentMoviePopular();
            upcoming = new RecyclerViewFragmentMovieUpcoming();
            nowPlaying = new RecyclerViewFragmentMovieNowPlaying();
            rating = new RecyclerViewFragmentMovieRating();
            search = new RecyclerViewFragmentMovieSearch();

        }

    }
}
