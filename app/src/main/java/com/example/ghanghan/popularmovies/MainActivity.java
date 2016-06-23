package com.example.ghanghan.popularmovies;

//import android.support.v7.app.AppCompatActivity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class MainActivity extends ActionBarActivity implements  MoviesFragment.Callback{

    private static final String DETAILFRAGMENT_TAG = "DFtag";
    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) != null){
            //app running on a tablet
            mTwoPane = true;

            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.movie_detail_container, new DetailsFragment(), DETAILFRAGMENT_TAG).
                        commit();
            }

        }
        else{
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String movieId) {
        if(mTwoPane){
            Bundle argument = new Bundle();
            argument.putString(DetailsFragment.MOVIE_ID, movieId);

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setArguments(argument);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.movie_detail_container, detailsFragment, DETAILFRAGMENT_TAG)
                    .commit();
        }
        else{
            Intent startDetail = new Intent(this, DetailsActivity.class);
            startDetail.putExtra(DetailsFragment.MOVIE_ID, movieId);
            startActivity(startDetail);

        }

    }

    public void expandContent(View v) {
        DetailsFragment.expandContent(v);// will expand the view containing the reviews
    }


}
