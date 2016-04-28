package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {
    private ImageAdapter thumbnails;

    private final String THE_MOVIE_DB_KEY = "29cc35d5be0b20cc6c4b29beb1d90f78";
    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //ImageAdapter will take images from a server and use it to populate
        //the GridView
        GridView gridView = (GridView)rootView.findViewById(R.id.thumbnail_grid);
        thumbnails = new ImageAdapter(getActivity());

        gridView.setAdapter(thumbnails);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";
                String iD = thumbnails.movieID[position];
                String url = MOVIE_BASE_URL +iD +"?api_key="+ BuildConfig.THE_MOVIE_DB_KEY;
                Intent openDetail = new Intent(getActivity(), DetailsActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, url);
                startActivity(openDetail);

            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Start background thread - getting movie data
        updateMovieData();
    }

    public void updateMovieData(){
        FetchThumbnail thumb = new FetchThumbnail();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order = prefs.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_default));
        thumb.execute(order);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public class FetchThumbnail extends AsyncTask<String, Void, String[][]>{
        private final String LOG_TAG = FetchThumbnail.class.getSimpleName();

        @Override
        protected String[][] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //will contain raw Json as string
            String discoverString = null;
            //will contain organized movie data
            String[][] data;
            try{
                //Construct URL
                final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";

                //"http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&";
                final String QUERY_ORDER = "sort_by";
                String order = params[0];
                //sorting order based on settings
                final String KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_ORDER, order)
                        .appendQueryParameter(KEY, THE_MOVIE_DB_KEY).build();

                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI" + builtUri.toString());

                //Create request for TheMovieDataBase and open connection
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null){
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }

                discoverString = buffer.toString();
                //data = getData(discoverString);
                //String[] poster = data[1];

               // Log.v(LOG_TAG, "JSON " + buffer);
            }catch(IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try{
                data = getData(discoverString);
                /*for(int i = 0; i <6; i++) {
                    Log.v(LOG_TAG, "The Movie ID's " + data[0][i]);
                    Log.v(LOG_TAG, "The Movie Poster path " + data[1][i]);
                }*/
                return data;

            }catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        public String[][] getData(String movieSt) throws JSONException{

            final String MTB_RESULTS = "results";
            final String MTB_ID = "id";
            final String MTP_POSTER = "poster_path";
            String poster;
            int idInt;
            //Log.v(LOG_TAG, movieSt);
            JSONObject movieJson = new JSONObject(movieSt);
            JSONArray movieArray = movieJson.getJSONArray(MTB_RESULTS);
            String[][] results = new String[2][movieArray.length()];

            for(int i = 0; i < movieArray.length(); i++){
                //getting the movie ID
                JSONObject movieData = movieArray.getJSONObject(i);
                idInt = movieData.getInt(MTB_ID);
                poster = movieData.getString(MTP_POSTER);

                results[0][i]= idInt+"";
                results[1][i] = poster;

            }
            return results;


        }

        @Override
        protected void onPostExecute(String[][] strings) {
            //movie poster file path
            String[] posterUrl = new String[strings[1].length];
            String[] movieID = new String[strings[1].length];
            //constructing movie poster url
            for(int i = 0; i < strings[1].length; i++){
                posterUrl[i] = "http://image.tmdb.org/t/p/w500/" + strings[1][i];
                movieID[i] = strings[0][i];
                Log.v(LOG_TAG, "The Poster URL " + posterUrl[i]);
                Log.v(LOG_TAG, "The Movie ID " + movieID[i]);
            }
            //store poster url array inside Image adapter
            Log.v(LOG_TAG, "Now in adapter");
            thumbnails.mThumbIds = posterUrl;
            thumbnails.movieID = movieID;
            thumbnails.notifyDataSetChanged();

        }
    }



    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        //will get from server
        private String[] mThumbIds = null;
        private String[] movieID = null;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            if(mThumbIds== null)
                return 0;
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return mThumbIds[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else
                imageView = (ImageView) convertView;


            if(mThumbIds != null) {
            String url = (String)getItem(position);
            Log.v("In adapter", "Before in View");

                Log.v("In adapter", "The Poster URL " + url);
                Picasso.with(mContext).load(url).into(imageView);
            }

            return imageView;
        }

    }// end ImageAdapter Class

}
