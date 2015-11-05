package com.stockita.popularmovie.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.slidingtabs.SlidingTabLayout;
import com.stockita.popularmovie.slidingtabs.ViewPagerAdapterDetail;
import com.stockita.popularmovie.utility.Utilities;

/**
 * Created by hishmadabubakaralamudi on 10/24/15.
 */
public class NewDetailActivity extends AppCompatActivity {

    // Constant
    private static final String LOG_TAG = NewDetailActivity.class.getSimpleName();
    // This is for the number of sliding tab on the detail activity screen,
    // they are "Details, "Posters" and "Reviews".
    private static final int NUMBER_OF_TAB = 3;

    // This constant is a Key used to put mMovieId value, to move it around.
    public static final String KEY_MOVIE_ID = "keyMovieId_com.stockita.popularmovie.activity";

    // This constant is a key used to put mSortGroup value, to move it around.
    // we have 4 kind, and they are "popular", "rating", "upcoming", "nowplaying" all in lower case.
    // only one value of this kind can be passed into the DetailActivity/DetailFragment.
    public static final String KEY_SORT_GROUP = "keySortGroup_com.stockita.popularmovie.activity";


    // Member variable
    private String mMovieId, mSortGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //getWindow().setBackgroundDrawable(null);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            mMovieId = savedInstanceState.getString(KEY_MOVIE_ID);
            mSortGroup = savedInstanceState.getString(KEY_SORT_GROUP);
        }

        if (savedInstanceState == null) {
            // Data from MovieEntry via RecyclerViewFragment into NewDetailFragment.
            mMovieId = getIntent().getStringExtra(ContractMovies.MovieEntry.COLUMN_MOVIE_ID);
            mSortGroup = getIntent().getStringExtra(ContractMovies.MovieEntry.COLUMN_SORT_GROUP);

            // We keep these two data in SharedPreferences, rather than savedInstanceState
            // When we use three sliding fragment, happening to me, when configuration changes
            // that saveInstanceSate will not survive.
            Utilities.setMovieId(this, KEY_MOVIE_ID, mMovieId);
            Utilities.setMovieId(this, KEY_SORT_GROUP, mSortGroup);

        }

        /* Sliding Tab below here */
        // We can add or remove tabs from here, then goto ViewPagerAdapter.java in getItem() and
        // add the fragment there.
        CharSequence[] tabTitles = {getString(R.string.activity_detail_tab_one),
                getString(R.string.activity_detail_tab_two), getString(R.string.activity_detail_tab_three)};
        int numberOfTabs = NUMBER_OF_TAB;

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        ViewPagerAdapterDetail adapter = new ViewPagerAdapterDetail(getFragmentManager(), tabTitles, numberOfTabs);

        // Assigning ViewPager View and setting the adapter
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the SlidingTabLayout View
        SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        // To make the Tabs Fixed set this true, this makes the tabs Space Evenly in Available width.
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getBaseContext(), R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(KEY_MOVIE_ID, mMovieId);
        outState.putString(KEY_SORT_GROUP, mSortGroup);
        super.onSaveInstanceState(outState, outPersistentState);
    }

}