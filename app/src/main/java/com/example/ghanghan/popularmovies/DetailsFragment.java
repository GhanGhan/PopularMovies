package com.example.ghanghan.popularmovies;

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
public class DetailsFragment extends Fragment {
    private static boolean expand = false;
    private ThumbnailAdapter thumbnails;
    static final String MOVIE_ID = "movie_ID";
    private String mMovieId;

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
        String[] projection = {MovieContract.PopularEntry.COLUMN_FAVORITE_KEY};
        String[] selectionArgs = {mMovieId};
        //Set color of the button
        Button favouritesButton = (Button)rootView.findViewById(R.id.favourite_button);
        changeButtonColor(favouritesButton);
        return rootView;
    }

    public void changeButtonColor(Button favButton){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order = prefs.getString(getActivity().getString(R.string.pref_sort_key),
                getActivity().getString(R.string.pref_sort_default));
        String[] selectionArgs = {mMovieId};
        String[] projection = {MovieContract.PopularEntry.COLUMN_FAVORITE_KEY};
        String[] projectionHigh = {MovieContract.HighestRatedEntry.COLUMN_FAVORITE_KEY};
        if(order.equals("popularity.desc")){
            Cursor cursor = getActivity().getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                    projection, MovieContract.PopularEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs,
                    null);
            if(cursor.moveToFirst()){
                Log.v("Details onCreateView", "is favourtite?");
                int fav = cursor.getInt(0);
                if(fav == -1)
                    favButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                else
                    favButton.setBackgroundColor(getResources().getColor(R.color.selected));
            }
        }
        else if (order.equals("vote_average.desc")){

            Cursor cursor = getActivity().getContentResolver().query(MovieContract.HighestRatedEntry.CONTENT_URI,
                    projectionHigh, MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs,
                    null);
            if(cursor.moveToFirst()){
                Log.v("Details onCreateView", "is favourtite?");
                int fav = cursor.getInt(0);
                if(fav == -1)
                    favButton.setBackgroundColor(getResources().getColor(R.color.button_unselected));
                else
                    favButton.setBackgroundColor(getResources().getColor(R.color.selected));
            }
        }

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
