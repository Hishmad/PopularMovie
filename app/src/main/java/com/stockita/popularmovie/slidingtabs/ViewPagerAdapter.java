package com.stockita.popularmovie.slidingtabs;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stockita.popularmovie.adapters.RatingRecyclerViewAdapter;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieNowPlaying;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMoviePopular;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieSearch;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieUpcoming;
import com.stockita.popularmovie.fragment.RecyclerViewFragmentMovieRating;


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


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titles, int numbOfTabsumb) {
        super(fm);
        mTitles = titles;
        mNumbOfTabs = numbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        FragmentHolder fragmentHolder = new FragmentHolder();

        switch (position) {
            case FRAGMENT_TAB_ONE:
                return fragmentHolder.nowPlaying;
            case FRAGMENT_TAB_TWO:
                return fragmentHolder.upcoming;
            case FRAGMENT_TAB_THREE:
                return fragmentHolder.search;
            case FRAGMENT_TAB_FOUR:
                return fragmentHolder.popularities;
            default:
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
