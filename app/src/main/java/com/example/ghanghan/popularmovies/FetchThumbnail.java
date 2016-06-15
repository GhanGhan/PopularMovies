package com.example.ghanghan.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
 * Created by GhanGhan on 6/14/2016.
 */
public class FetchThumbnail extends AsyncTask<String, Void, String[][]> {
    private final String LOG_TAG = FetchThumbnail.class.getSimpleName();
    private ImageAdapter thumbnails;

    public FetchThumbnail(ImageAdapter imageAdapter){
        thumbnails = imageAdapter;
    }

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
                    .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

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
        Log.v(LOG_TAG, movieSt);
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
        thumbnails.setThumbIds(posterUrl);
        thumbnails.setMovieID(movieID);
        thumbnails.notifyDataSetChanged();

    }
    //store data specific shit in database
    private void updateDatabase(){
        int numberOfMovies = thumbnails.getNumberOfMovies();
    }


}
