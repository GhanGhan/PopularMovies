package com.example.ghanghan.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle argument = new Bundle();

        argument.putString(DetailsFragment.MOVIE_ID,
                getIntent().getStringExtra(DetailsFragment.MOVIE_ID));
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(argument);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, detailsFragment)
                    .commit();
        }
    }

    public void expandContent(View v) {
        DetailsFragment.expandContent(v);// will expand the view containing the reviews
    }

}
