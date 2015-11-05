package com.stockita.popularmovie.activity;

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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stockita.popularmovie.R;
import com.stockita.popularmovie.data.ContractMovies;
import com.stockita.popularmovie.interfaces.CallThis;
import com.stockita.popularmovie.slidingtabs.SlidingTabLayout;
import com.stockita.popularmovie.slidingtabs.ViewPagerAdapter;
import com.stockita.popularmovie.sync.SyncUtility;
import com.stockita.popularmovie.utility.FetchAndParse;


public class MainActivity extends AppCompatActivity implements CallThis {

    // Constant
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int NUMBER_OF_TAB = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getWindow().setBackgroundDrawable(null);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // We can add or remove tabs from here, then goto ViewPagerAdapter.java in getItem() and
        // add the fragment there.
        CharSequence[] tabTitles = {getString(R.string.activity_main_tab_one),
                getString(R.string.activity_main_tab_two), getString(R.string.activity_main_tab_three),
                getString(R.string.activity_main_tab_four), getString(R.string.activity_main_tab_five)};
        int numberOfTabs = NUMBER_OF_TAB;

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), tabTitles, numberOfTabs, this);

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

        // Sync data in interval mode, this framework will run once every 24 hours.
        try {
            SyncUtility.execSyncInterval(this);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /**
     * This method is called when one of the menu items to selected. These items
     * can be on the Action Bar, the overflow menu, or the standard options menu. You
     * should return true if you handle the selection.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                try {
                    startService(new Intent(this, FetchAndParse.class));
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.toString());
                }
                return true;

            case R.id.about:
                // TODO create activity/fragment About
                Toast.makeText(this, R.string.menu_title_about, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method to pass data from RecyclerViewFragment.. into NewDetailFragment.
     */
    @Override
    public void onItemSelectedMovieId(String movieId, String sortGroup) {

        // Go to detail activity
        Intent intent = new Intent(this, NewDetailActivity.class);

        // Pass these data to detail activity
        intent.putExtra(ContractMovies.MovieEntry.COLUMN_MOVIE_ID, movieId);
        intent.putExtra(ContractMovies.MovieEntry.COLUMN_SORT_GROUP, sortGroup);

        startActivity(intent);

    }
}
