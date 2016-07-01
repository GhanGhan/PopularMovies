package com.example.ghanghan.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ghanghan.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class DetailsFragment extends Fragment implements View.OnClickListener {
    private static boolean expand = false;
    private ThumbnailAdapter thumbnails;
    static final String MOVIE_ID = "movie_ID";
    private String mMovieId;
    private String mTable;
    private Button mFavButton;

    private static final String[] POPULAR_COLUMNS = {
        MovieContract.PopularEntry.COLUMN_FAVORITE_KEY,
                MovieContract.PopularEntry.COLUMN_MOVIE_ID,
                MovieContract.PopularEntry.COLUMN_POSTER_PATH,
                MovieContract.PopularEntry.COLUMN_ORIGINAL_TITLE,
                MovieContract.PopularEntry.COLUMN_STATUS,
                MovieContract.PopularEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.PopularEntry.COLUMN_TRAILER_KEYS,
                MovieContract.PopularEntry.COLUMN_OVERVIEW,
                MovieContract.PopularEntry.COLUMN_NUMBER_OF_REVIEWS,
                MovieContract.PopularEntry.COLUMN_AUTHORS,
                MovieContract.PopularEntry.COLUMN_REVIEW_CONTENT
    };

    private static final String[] HIGH_RATE_COLUMNS = {
            MovieContract.HighestRatedEntry.COLUMN_FAVORITE_KEY,
            MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID,
            MovieContract.HighestRatedEntry.COLUMN_POSTER_PATH,
            MovieContract.HighestRatedEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.HighestRatedEntry.COLUMN_STATUS,
            MovieContract.HighestRatedEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.HighestRatedEntry.COLUMN_TRAILER_KEYS,
            MovieContract.HighestRatedEntry.COLUMN_OVERVIEW,
            MovieContract.HighestRatedEntry.COLUMN_NUMBER_OF_REVIEWS,
            MovieContract.HighestRatedEntry.COLUMN_AUTHORS,
            MovieContract.HighestRatedEntry.COLUMN_REVIEW_CONTENT
    };

    private static final int COL_FAVORITE_KEY = 0;
    private static final int COL_MOVIE_ID = 1;
    private static final int COL_POSTER_PATH = 2;
    private static final int COL_ORIGINAL_TITLE = 3;
    private static final int COL_STATUS = 4;
    private static final int COL_VOTE_AVERAGE = 5;
    private static final int COL_TRAILER_KEYS = 6;
    private static final int COL_OVERVIEW = 7;
    private static final int COL_NUMBER_OF_REVIEWS = 8;
    private static final int COL_AUTHORS = 9;
    private static final int COL_REVIEW_CONTENT = 10;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        thumbnails = new ThumbnailAdapter(getActivity());

        Bundle arguments = getArguments();

        if(arguments != null)
            mMovieId = arguments.getString(MOVIE_ID);
        //get table
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mTable = prefs.getString(getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_default));
        //Set color of the button
        mFavButton = (Button)rootView.findViewById(R.id.favourite_button);
        changeButtonColor();
        mFavButton.setOnClickListener(this);
        return rootView;
    }

    public void changeButtonColor(){
        String[] selectionArgs = {mMovieId};
        String[] projection = {MovieContract.PopularEntry.COLUMN_FAVORITE_KEY};
        String[] projectionHigh = {MovieContract.HighestRatedEntry.COLUMN_FAVORITE_KEY};
        if(mTable.equals("popularity.desc")){
            Cursor cursor = getActivity().getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                    projection, MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs,
                    null);
            if(cursor.moveToFirst()){
                Log.v("Details onCreateView", "is favourtite?");
                int fav = cursor.getInt(0);
                if(fav == -1) {
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                    mFavButton.setText(getString(R.string.not_in_fav));
                }
                else {
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.selected));
                    mFavButton.setText(getString(R.string.in_fav));
                }
            }
        }//end if
        else if (mTable.equals("vote_average.desc")){

            Cursor cursor = getActivity().getContentResolver().query(MovieContract.HighestRatedEntry.CONTENT_URI,
                    projectionHigh, MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs,
                    null);
            if(cursor.moveToFirst()){
                Log.v("Details onCreateView", "is favourtite?");
                int fav = cursor.getInt(0);
                if(fav == -1) {
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                    mFavButton.setText(getString(R.string.not_in_fav));
                }

                else {
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.selected));
                    mFavButton.setText(getString(R.string.in_fav));
                }
            }
        }//end if
        else if(mTable.equals("favorites")){
            mFavButton.setBackgroundColor(getResources().getColor(R.color.not_removed));
            mFavButton.setText(getString(R.string.remove_fav));
        }

    }

    @Override
    public void onClick(View view) {
        Log.v("View id", "" + view.getId());
        Log.v("Button id", "" + R.id.favourite_button);
        if(view.getId() == R.id.favourite_button) changeFavoriteState();
    }

    public void changeFavoriteState(){
        Log.v("DetailFrag", "buttonClicked");
        String[] selectionArgs = {mMovieId};
        Cursor cursor;
        if(mTable.equals("popularity.desc")){
            cursor = getActivity().getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                    POPULAR_COLUMNS, MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ?",
                    selectionArgs, null );
            if(cursor.moveToFirst()){
                int favId = cursor.getInt(COL_FAVORITE_KEY);
                if(favId == -1){
                    favId = addToFavoritesTable(cursor);
                    updatePopularTable(favId, selectionArgs);
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.selected));
                    mFavButton.setText(getString(R.string.in_fav));
                }
                else{
                    int rowsD = getActivity().getContentResolver().delete(MovieContract.FavoritedEntry.CONTENT_URI,
                            MovieContract.FavoritedEntry._ID + " = ?", new String[] {Integer.toString(favId)});
                    favId = -1;
                    updatePopularTable(favId, selectionArgs);
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                    mFavButton.setText(getString(R.string.not_in_fav));
                    Log.v("Fav rows deleted", Integer.toString(rowsD));
                }
            }
            cursor.close();
        }// end if
        else if(mTable.equals("vote_average.desc")){
            cursor = getActivity().getContentResolver().query(MovieContract.HighestRatedEntry.CONTENT_URI,
                    HIGH_RATE_COLUMNS, MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID + " = ?",
                    selectionArgs, null);
            if(cursor.moveToFirst()){
                int favId = cursor.getInt(COL_FAVORITE_KEY);
                if(favId == -1){
                    favId = addToFavoritesTable(cursor);
                    updateHighTable(favId, selectionArgs);
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.selected));
                    mFavButton.setText(getString(R.string.in_fav));
                }
                else{
                    int rowsD = getActivity().getContentResolver().delete(MovieContract.FavoritedEntry.CONTENT_URI,
                            MovieContract.FavoritedEntry._ID + " = ?", new String[] {Integer.toString(favId)});
                    favId = -1;
                    updateHighTable(favId, selectionArgs);
                    mFavButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                    mFavButton.setText(R.string.not_in_fav);
                    Log.v("Fav rows deleted", Integer.toString(rowsD));
                }
            }
        }//end else if
        else if(mTable.equals("favorites")){
            //check if it exists in popular table or high table
            updatePopularTable(-1, selectionArgs);
            updateHighTable(-1, selectionArgs);
            getActivity().getContentResolver().delete(MovieContract.FavoritedEntry.CONTENT_URI,
                    MovieContract.FavoritedEntry.COLUMN_MOVIE_ID + " = ? ", selectionArgs);
            mFavButton.setClickable(false);
            mFavButton.setBackgroundColor(getResources().getColor(R.color.removed));
            mFavButton.setText(getString(R.string.fav_removed));
        }

    }

    private int addToFavoritesTable(Cursor cursor){
        ContentValues values = new ContentValues(10);

        values.put(MovieContract.FavoritedEntry.COLUMN_MOVIE_ID,
                cursor.getString(COL_MOVIE_ID));
        values.put(MovieContract.FavoritedEntry.COLUMN_ORIGINAL_TITLE,
                cursor.getString(COL_ORIGINAL_TITLE));
        values.put(MovieContract.FavoritedEntry.COLUMN_OVERVIEW,
                cursor.getString(COL_OVERVIEW));
        values.put(MovieContract.FavoritedEntry.COLUMN_VOTE_AVERAGE,
                cursor.getFloat(COL_VOTE_AVERAGE));
        values.put(MovieContract.FavoritedEntry.COLUMN_STATUS,
                cursor.getString(COL_STATUS));
        values.put(MovieContract.FavoritedEntry.COLUMN_POSTER_PATH,
                cursor.getString(COL_POSTER_PATH));
        values.put(MovieContract.FavoritedEntry.COLUMN_NUMBER_OF_REVIEWS,
                cursor.getString(COL_NUMBER_OF_REVIEWS));
        values.put(MovieContract.FavoritedEntry.COLUMN_AUTHORS,
                cursor.getString(COL_AUTHORS));
        values.put(MovieContract.FavoritedEntry.COLUMN_REVIEW_CONTENT,
                cursor.getString(COL_REVIEW_CONTENT));
        values.put(MovieContract.FavoritedEntry.COLUMN_TRAILER_KEYS,
                cursor.getString(COL_TRAILER_KEYS));

        Uri newFav = getActivity().getContentResolver().insert(MovieContract.FavoritedEntry.CONTENT_URI,
                values);
        Log.v("Fav Table, uri row", newFav.toString());
        int rowNumber = Integer.parseInt(MovieContract.FavoritedEntry.getRowId(newFav));
        Log.v("The row #","" + rowNumber);

        return rowNumber;
    }

    private void updatePopularTable(int favId, String[] selectionArgs){
        ContentValues value = new ContentValues(1);
        value.put(MovieContract.PopularEntry.COLUMN_FAVORITE_KEY, favId);
        int numRowsUpdated;
        numRowsUpdated = getActivity().getContentResolver().update(MovieContract.PopularEntry.CONTENT_URI,
                value, MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs);
        Log.v("Pop Table, rows updated", ""+ numRowsUpdated);
    }

    private void updateHighTable(int favId, String[] selectionArgs){
        ContentValues value = new ContentValues(1);
        value.put(MovieContract.HighestRatedEntry.COLUMN_FAVORITE_KEY, favId);
        int numRowsUpdated;
        numRowsUpdated = getActivity().getContentResolver().update(MovieContract.HighestRatedEntry.CONTENT_URI,
                value, MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs);
        Log.v("High Table,rows updated", ""+ numRowsUpdated);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        super.onStart();
        //TODO: use id from bundle instead of intent
        //String url = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        FetchInfo MovieData = new FetchInfo(getActivity(), thumbnails, mMovieId);
        MovieData.loadView();
    }

    public static void expandContent(View view){
        TextView content = (TextView)view.getRootView().findViewById(R.id.movie_review_content);
        if(expand){
            content.setVisibility(View.GONE);
            expand = false;
        }else{
            content.setVisibility(View.VISIBLE);
            expand = true;
            content.getParent().requestChildFocus(content, content);
        }
    }

}
