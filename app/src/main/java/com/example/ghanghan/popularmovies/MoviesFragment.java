package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.ghanghan.popularmovies.ImageAdapter;
import com.example.ghanghan.popularmovies.data.MovieContract;


public class MoviesFragment extends Fragment{
    private static final String LOG_TAG = Fragment.class.getName();
    private ImageAdapter thumbnails;

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //ImageAdapter will take images from a server and use it to populate
        //the GridView
        GridView gridView = (GridView)rootView.findViewById(R.id.thumbnail_grid);
        thumbnails = new ImageAdapter(getActivity());

        gridView.setAdapter(thumbnails);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";
                String iD = thumbnails.getMovieID(position);

                if (iD != null) {
                    ((Callback) getActivity()).onItemSelected(iD);
                }

            }
        });


        return rootView;
    }

    public interface Callback{
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String movieId);
    }

    @Override
    public void onStart() {
        super.onStart();
        //TESTING THE DATABASE
        String[] testArray = {MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE};
        Cursor testCursor = getActivity().getContentResolver().query(MovieContract.PopularEntry.buildPopularUri(10),
                testArray, null,null, null);
        if(testCursor.moveToFirst()){//if database (popular) has been updated
            LoadMoviePoster loadMoviePoster = new LoadMoviePoster(thumbnails,getActivity().getApplicationContext());
            loadMoviePoster.loadImages();
            Log.v(LOG_TAG, "load from db");
        }
        else {
            updateMovieData();//getmovie data from server
            Log.v(LOG_TAG, "load from server");
        }
        testCursor.close();
    }

    public void updateMovieData(){
        FetchThumbnail thumb = new FetchThumbnail(thumbnails, getActivity().getApplicationContext());
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));
        thumb.execute(order);
    }

}
