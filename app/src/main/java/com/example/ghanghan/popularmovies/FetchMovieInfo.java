package com.example.ghanghan.popularmovies;

import com.example.ghanghan.popularmovies.data.MovieContract.PopularEntry;
import com.example.ghanghan.popularmovies.data.MovieContract.HighestRatedEntry;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GhanGhan on 6/14/2016.
 */
public class FetchMovieInfo extends AsyncTask<String, Void, String[][]> {
    private static final String LOG_TAG = FetchMovieInfo.class.getName();
    Context mContext;
    public FetchMovieInfo(Context context){
        mContext = context;
    }
    @Override
    protected String[][] doInBackground(String... strings) {
        //strings is the array of movie iD's
        Log.i("The first movie Id", strings[0]);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        BufferedReader readerReview = null;
        BufferedReader readerTrailer = null;

        //Raw Json String data
        String discoverString = null;
        String reviewString = null;
        String trailerString = null;
        //parsed movie and movie review data
        String dataMovie[], dataReview[], dataTrailer[], movieDataArray[];
        //Movie data will be held in 2-d array before uploaded in database
        int numberOfMovies = strings.length;
        String[][] allMovieData = new String[numberOfMovies][];

        for (int i = 0; i < numberOfMovies; i++) {
            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String REVIEW = "reviews";
                final String TRAILER = "videos";
                final String KEY = "api_key";
                //Build uri to acquire movie data
                Uri builtUriGeneral = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[i])//movie ID
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlGeneral = new URL(builtUriGeneral.toString());

                //Build uri to acquire movie review data
                Uri builtUriReview = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[i])
                        .appendPath(REVIEW)
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlReview = new URL(builtUriReview.toString());

                //Build uri to acquire movie review data
                Uri builtUriTrailer = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[i])
                        .appendPath(TRAILER)
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlTrailer = new URL(builtUriTrailer.toString());

                if(i == (numberOfMovies/2)) {//due to 40 request limit
                    try {
                        Log.v(LOG_TAG, "sleep");
                        Thread.sleep(10000, 1);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }

                //create request for TheMovieDataBase and open connection for general data
                urlConnection = (HttpURLConnection) urlGeneral.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                //Read input stream into String
                InputStream inputStream = null;
                try {
                    inputStream = urlConnection.getInputStream();
                }catch(FileNotFoundException e){
                    inputStream = urlConnection.getErrorStream();
                    e.printStackTrace();
                }
                StringBuffer buffer = new StringBuffer();

                //create request for TheMovieDataBase and open connection for Review data
                urlConnection = (HttpURLConnection) urlReview.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String for Review
                InputStream inputStreamReview;
                try {
                    inputStreamReview = urlConnection.getInputStream();
                }catch(FileNotFoundException e){
                    inputStreamReview = urlConnection.getErrorStream();
                    e.printStackTrace();
                }
                StringBuffer bufferReview = new StringBuffer();

                //create request for TheMovieDataBase and open connection for Trailer data
                urlConnection = (HttpURLConnection) urlTrailer.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String for Trailer
                InputStream inputStreamTrailer;
                try {
                    inputStreamTrailer = urlConnection.getInputStream();
                }catch(FileNotFoundException e){
                    inputStreamTrailer = urlConnection.getErrorStream();
                    e.printStackTrace();
                }
                StringBuffer bufferTrailer = new StringBuffer();

                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                readerReview = new BufferedReader(new InputStreamReader(inputStreamReview));
                readerTrailer = new BufferedReader(new InputStreamReader(inputStreamTrailer));

                String line;
                //place data in associated buffer
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                while ((line = readerReview.readLine()) != null) {
                    bufferReview.append(line + "\n");
                }
                while ((line = readerTrailer.readLine()) != null) {
                    bufferTrailer.append(line + "\n");
                }

                if (buffer.length() == 0 || bufferReview.length() == 0 || bufferTrailer.length() == 0) {
                    return null;
                }

                discoverString = buffer.toString();
                reviewString = bufferReview.toString();
                trailerString = bufferTrailer.toString();
                Log.v("data: ", discoverString);
                Log.v("Data2:", reviewString);
                Log.v("Data3:", trailerString);


            } catch (MalformedURLException e) {
                Log.e("Creating URL ", "Error", e);
                return null;
            } catch (IOException e) {
                Log.e("Connecting to Server ", "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Done", "Error closing stream", e);
                    }
                }
            }
            try {

                dataMovie = getMovieData(discoverString);
                dataReview = getReviewData(reviewString);
                dataTrailer = getTrailerData(trailerString);
                //Log.v("DataMovie Array", dataMovie[0] + " \n" + dataReview[0] + "\n" + dataTrailer[0]);
                movieDataArray = concatArrays(dataMovie, dataReview);
                movieDataArray = concatArrays(movieDataArray, dataTrailer);
                allMovieData[i] = addElementToArray(strings[i], movieDataArray);



            } catch (JSONException e) {
                Log.e("Json", e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        } // end for loop
        return allMovieData;
    }

    public String[] getMovieData(String movieSt) throws JSONException {

        final String MTB_TITLE = "original_title";
        final String MTB_PLOT = "overview";
        final String MTB_RATING = "vote_average";
        final String MTB_RELEASE = "status";
        final String MTB_THUMBNAIL = "poster_path";

        JSONObject movieJson = new JSONObject(movieSt);

        String[] movieResults = new String[5];

        movieResults[0] = movieJson.getString(MTB_TITLE);
        movieResults[1] = movieJson.getString(MTB_PLOT);
        movieResults[2] = movieJson.getString(MTB_RATING);
        movieResults[3] = movieJson.getString(MTB_RELEASE);
        movieResults[4] = movieJson.getString(MTB_THUMBNAIL);
        //Log.v("Got Title", movieResults[0]);
        //Log.v("Got Plot", movieResults[1]);
        //Log.v("Got Rating", movieResults[2]);

        return movieResults;

    }//end getMovieData

    public String[] getReviewData(String revString) {
        final String MTB_TOTAL_RESULTS = "total_results";
        final String MTB_WRITER = "author";
        final String MTB_REVIEW = "content";
        final String MTB_RESULTS = "results";

        try {
            String[] data = new String[3];
            JSONObject reviewJson = new JSONObject(revString);
            data[0] = reviewJson.getString(MTB_TOTAL_RESULTS);
            data[1] = "";
            data[2] = "";
            JSONArray reviewArray = reviewJson.getJSONArray(MTB_RESULTS);

            for (int i = 0; i < reviewArray.length(); i++) {
                JSONObject reviewData = reviewArray.getJSONObject(i);

                data[1] += reviewData.getString(MTB_WRITER);
                if (i > 0) {
                    data[1] += " " + reviewData.getString(MTB_WRITER);
                    data[2] += "\n" + "\n" + reviewData.getString(MTB_REVIEW);
                } else {
                    data[2] += reviewData.getString(MTB_REVIEW);
                    data[1] += reviewData.getString(MTB_WRITER);
                }
            }
            return data;
        } catch (JSONException e) {
            Log.e("Json", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public String[] getTrailerData(String trailerString) {
        final String MTB_RESULTS = "results";
        final String MTB_KEY = "key";
        final String MTB_NAME = "name";

        try {
            String[] data = new String[2];
            JSONObject trailerJson = new JSONObject(trailerString);
            JSONArray trailerArray = trailerJson.getJSONArray(MTB_RESULTS);

            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject reviewData = trailerArray.getJSONObject(i);

                if (i > 0) {
                    data[0] += "," + reviewData.getString(MTB_NAME);
                    data[1] += "," + reviewData.getString(MTB_KEY);
                } else {
                    data[0] += reviewData.getString(MTB_NAME);
                    data[1] += reviewData.getString(MTB_KEY);
                }
            }
            return data;
        } catch (JSONException e) {
            Log.e("Json", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    public String[] concatArrays(String[] array1, String[] array2) {
        String[] results = new String[array1.length + array2.length];

        for (int i = 0; i < array1.length; i++) {
            results[i] = array1[i];
        }
        for (int i = array1.length; i < array1.length + array2.length; i++) {
            results[i] = array2[i - array1.length];
        }
        return results;
    }

    public String[] addElementToArray(String element, String[] array){
        String[] results = new String[array.length + 1];
        for (int i = 0; i < results.length ; i++) {
            if(i ==0) results[i] = element;
            else results[i] = array[i-1];
        }
        return results;
    }

    @Override
    protected void onPostExecute(String[][] strings) {

        /* needed elements in allMovieData
        [0] - id (movie id)
        [1] - original_title
        [2] - overview
        [3] - vote_average
        [4] - status
        [5] - poster_path
        [6] - total_results
        [7] - author
        [8] - content
        [10]- key
         */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String order = prefs.getString(mContext.getString(R.string.pref_sort_key),
                mContext.getString(R.string.pref_sort_default));
        ContentValues[] values = new ContentValues[strings.length];
        if(order.equals("popularity.desc")) {
            for (int i = 0; i < strings.length; i++) {
                values[i] = new ContentValues(11);
                values[i].put(PopularEntry.COLUMN_FAVORITE_KEY, -1);
                values[i].put(PopularEntry.COLUMN_MOVIE_ID, strings[i][0]);
                values[i].put(PopularEntry.COLUMN_ORIGINAL_TITLE, strings[i][1]);
                values[i].put(PopularEntry.COLUMN_OVERVIEW, strings[i][2]);
                values[i].put(PopularEntry.COLUMN_VOTE_AVERAGE, strings[i][3]);
                values[i].put(PopularEntry.COLUMN_STATUS, strings[i][4]);
                values[i].put(PopularEntry.COLUMN_POSTER_PATH, strings[i][5]);
                values[i].put(PopularEntry.COLUMN_NUMBER_OF_REVIEWS, strings[i][6]);
                values[i].put(PopularEntry.COLUMN_AUTHORS, strings[i][7]);
                values[i].put(PopularEntry.COLUMN_REVIEW_CONTENT, strings[i][8]);
                values[i].put(PopularEntry.COLUMN_TRAILER_KEYS, strings[i][10]);
            }
            mContext.getContentResolver().bulkInsert(PopularEntry.CONTENT_URI, values);
        }else if (order.equals("vote_average.desc")){
            for (int i = 0; i < strings.length; i++) {
                values[i] = new ContentValues(11);
                values[i].put(HighestRatedEntry.COLUMN_FAVORITE_KEY, -1);
                values[i].put(HighestRatedEntry.COLUMN_MOVIE_ID, strings[i][0]);
                values[i].put(HighestRatedEntry.COLUMN_ORIGINAL_TITLE, strings[i][1]);
                values[i].put(HighestRatedEntry.COLUMN_OVERVIEW, strings[i][2]);
                values[i].put(HighestRatedEntry.COLUMN_VOTE_AVERAGE, strings[i][3]);
                values[i].put(HighestRatedEntry.COLUMN_STATUS, strings[i][4]);
                values[i].put(HighestRatedEntry.COLUMN_POSTER_PATH, strings[i][5]);
                values[i].put(HighestRatedEntry.COLUMN_NUMBER_OF_REVIEWS, strings[i][6]);
                values[i].put(HighestRatedEntry.COLUMN_AUTHORS, strings[i][7]);
                values[i].put(HighestRatedEntry.COLUMN_REVIEW_CONTENT, strings[i][8]);
                values[i].put(HighestRatedEntry.COLUMN_TRAILER_KEYS, strings[i][10]);
            }
            mContext.getContentResolver().bulkInsert(HighestRatedEntry.CONTENT_URI, values);
        }

        //Log.v("Movie Title", mContext.getContentResolver().q)
    }


}//end FetchMovieInfo class
