package com.example.ghanghan.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ghanghan.popularmovies.data.MovieContract;
import com.example.ghanghan.popularmovies.data.MovieContract.PopularEntry;
import com.example.ghanghan.popularmovies.data.MovieContract.HighestRatedEntry;
import com.example.ghanghan.popularmovies.data.MovieContract.FavoritedEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * Created by GhanGhan on 6/14/2016.
 */
public class FetchInfo {
    private FragmentActivity mActivity;
    private ThumbnailAdapter mThumbnailAdapter;
    private String movieID;

    public FetchInfo (FragmentActivity activity, ThumbnailAdapter thumbnailAdapter, String id){
        //mContext = context;
        mActivity = activity;
        mThumbnailAdapter = thumbnailAdapter;
        movieID = id;
    }

    public final String[] movietext = {
            PopularEntry.COLUMN_MOVIE_ID,
            PopularEntry.COLUMN_ORIGINAL_TITLE,
            PopularEntry.COLUMN_OVERVIEW,
            PopularEntry.COLUMN_VOTE_AVERAGE,
            PopularEntry.COLUMN_STATUS,
            PopularEntry.COLUMN_POSTER_PATH,
            PopularEntry.COLUMN_NUMBER_OF_REVIEWS,
            PopularEntry.COLUMN_AUTHORS,
            PopularEntry.COLUMN_REVIEW_CONTENT,
            PopularEntry.COLUMN_TRAILER_KEYS
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_ORIGINAL_TITLE = 1;
    private static final int COL_OVERVIEW = 2;
    private static final int COL_VOTE_AVERAGE = 3;
    private static final int COL_STATUS = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_NUMBER_OF_REVIEWS = 6;
    public static final int COL_AUTHORS = 7;
    public static final int COL_REVIEW_CONTENT = 8;
    public static final int COL_TRAILER_KEYS = 9;

    protected void loadView() {
        //Use database to populate textViews
        String[] idArray = new String[1];
        idArray[0] = movieID;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        String order = prefs.getString(mActivity.getString(R.string.pref_sort_key),
                mActivity.getString(R.string.pref_sort_default));
        Cursor testCursor;
        if(order.equals("popularity.desc")) {
            testCursor = mActivity.getContentResolver().query(PopularEntry.CONTENT_URI,
                    movietext, PopularEntry.COLUMN_MOVIE_ID + " = ?", idArray, null);
        }
        else if (order.equals("vote_average.desc")) {
            testCursor = mActivity.getContentResolver().query(HighestRatedEntry.CONTENT_URI,
                    movietext, HighestRatedEntry.COLUMN_MOVIE_ID + " = ?", idArray, null);
        }
        else if (order.equals("favorites")) {
            testCursor = mActivity.getContentResolver().query(FavoritedEntry.CONTENT_URI,
                    movietext, FavoritedEntry.COLUMN_MOVIE_ID + " = ?", idArray, null);
        }
        else
            testCursor = null;

        TextView title = (TextView) mActivity.findViewById(R.id.movie_title);
        TextView plot = (TextView) mActivity.findViewById(R.id.movie_plot);
        TextView rating = (TextView) mActivity.findViewById(R.id.movie_rating);
        TextView release_date = (TextView) mActivity.findViewById(R.id.movie_release);
        TextView review_tile = (TextView) mActivity.findViewById(R.id.movie_review_tile);
        TextView review_content = (TextView) mActivity.findViewById(R.id.movie_review_content);
        ImageView poster = (ImageView) mActivity.findViewById(R.id.thumbnail);

        ContextWrapper  wrapper = new ContextWrapper(mActivity.getApplicationContext());
        File directory;
        File posterPath;

        if(testCursor.moveToFirst()){
            title.setText(testCursor.getString(COL_ORIGINAL_TITLE));
            plot.setText(testCursor.getString(COL_OVERVIEW));
            rating.setText(testCursor.getString(COL_VOTE_AVERAGE)+ "/10");
            release_date.setText(testCursor.getString(COL_STATUS));
            review_tile.setText("Reviews (" + testCursor.getString(COL_NUMBER_OF_REVIEWS) + ")");
            review_content.setText(testCursor.getString(COL_REVIEW_CONTENT));
            directory = wrapper.getDir(testCursor.getString(COL_MOVIE_ID), Context.MODE_PRIVATE);//path to the folder
            posterPath = new File(directory, testCursor.getString(COL_POSTER_PATH));//path to poster
            Picasso.with(mActivity).load(posterPath).into(poster);
            Log.v("Loaded from database", testCursor.getString(COL_ORIGINAL_TITLE));
            ///For trailer thumbnail
            String thumbKeys = testCursor.getString(COL_TRAILER_KEYS);
            //String trailerDes = strings[strings.length-2];
            if(thumbKeys != null) {
                parseTrailerKeys(thumbKeys, testCursor.getString(COL_MOVIE_ID));
                LinearLayout trailerLayout = (LinearLayout)mActivity.findViewById(R.id.trailer_horizontal_list);
                initiateTrailerViews(trailerLayout);

            }
            testCursor.close();
            Log.v("Cursor status", "closed");
        }

    }
    private void parseTrailerKeys(String thumbKeys, String filename) {
        //Log.v("Trailer Description", trailerDes);
        //Log.v("Null description", trailerDes.substring(0, 4));
        if (thumbKeys != null) {
            if(thumbKeys.substring(0,4).equals("null"))
                thumbKeys = thumbKeys.substring(4);
        }
        //Log.v("Trailer Description", trailerDes);
        ContextWrapper wrapper = new ContextWrapper(mActivity.getApplicationContext());
        File directory = wrapper.getDir(filename, Context.MODE_PRIVATE);
        File trailerPath[];

        StringTokenizer parseKeys = new StringTokenizer(thumbKeys, ",");
        int number = parseKeys.countTokens();
        String keyUrl[] = new String[number];
        trailerPath = new File[number];
        String urlKey;
        //constructing movie poster url

        for (int i = 0; i < number; i++) {
            urlKey = parseKeys.nextToken();
            trailerPath[i] = new File(directory, urlKey);//path to poster
            Log.v("Loading trailer image", trailerPath[i].toString());
            keyUrl[i] = urlKey;
            //Log.v("Post Ex", "The trailer URL " + thumbUrl[i]);
        }
        //store poster url array inside Image adapter
        Log.v("Post Ex", "Now in adapter");

        mThumbnailAdapter.setTrailerImageKey(trailerPath);
        mThumbnailAdapter.setTrailerId(keyUrl);
        mThumbnailAdapter.notifyDataSetChanged();
    }

    private void initiateTrailerViews(LinearLayout trailerLayout){
        for (int i = 0; i < mThumbnailAdapter.getNumberOfTrailers(); i++) {
            final int index = i;
            View placeHolder = mThumbnailAdapter.getView(i, null, trailerLayout);

            trailerLayout.addView(mThumbnailAdapter.getView(i, placeHolder, trailerLayout));

            placeHolder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String key = mThumbnailAdapter.getTrailerId(index);
                    String url = "https://www.youtube.com/watch?v=" + key;
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mActivity.startActivity(trailerIntent);
                }
            });

        }
    }

}
