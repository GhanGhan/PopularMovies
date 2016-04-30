package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 */
public class DetailsFragment extends Fragment {
    private static boolean expand = false;

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
        return rootView;
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
        String url = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        FetchInfo MovieData = new FetchInfo();
        MovieData.execute(url);
    }

    public class FetchInfo extends AsyncTask<String, Void, String[]>{
        @Override
        protected String[] doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            BufferedReader readerReview = null;

            //Raw Json String data
            String discoverString = null;
            String reviewString = null;
            //parsed movie and movie review data
            String dataMovie[], dataReview[], movieDataArray[];

            try{

                final String MOVIE_BASE_URL ="http://api.themoviedb.org/3/movie/";
                final String REVIEW = "reviews";
                final String TRAILER = "videos";
                final String KEY = "api_key";
                //Build uri to acquire movie data
                Uri builtUriGeneral = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[0])//movie ID
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlGeneral = new URL(builtUriGeneral.toString());

                //Build uri to acquire movie review data
                Uri builtUriReview = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[0])
                        .appendPath(REVIEW)
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlReview = new URL(builtUriReview.toString());

                //Build uri to acquire movie review data
                Uri builtUriTrailer = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendPath(strings[0])
                        .appendPath(TRAILER)
                        .appendQueryParameter(KEY, BuildConfig.THE_MOVIE_DB_KEY).build();

                URL urlTrailer = new URL(builtUriTrailer.toString());

                //create request for TheMovieDataBase and open connection for general data
                urlConnection = (HttpURLConnection)urlGeneral.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                //create request for TheMovieDataBase and open connection for Review data
                urlConnection = (HttpURLConnection)urlReview.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String for Review
                InputStream inputStreamReview = urlConnection.getInputStream();
                StringBuffer bufferReview = new StringBuffer();

                //create request for TheMovieDataBase and open connection for Review data
                urlConnection = (HttpURLConnection)urlTrailer.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read input stream into String for Review
                //InputStream inputStreamReview = urlConnection.getInputStream();
                //StringBuffer bufferReview = new StringBuffer();

                if(inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));
                readerReview = new BufferedReader(new InputStreamReader(inputStreamReview));

                String line;
                //place data in associated buffer
                while((line = reader.readLine())!= null){
                    buffer.append(line + "\n");
                }
                while((line = readerReview.readLine())!= null){
                    bufferReview.append(line + "\n");
                }

                if(buffer.length()==0 || bufferReview.length() == 0){
                    return null;
                }

                discoverString = buffer.toString();
                reviewString = bufferReview.toString();
                Log.v("Data: ", discoverString);
                Log.v("Data2:", reviewString);



            }catch(MalformedURLException e){
                Log.e("Creating URL ", "Error", e);
                return null;
            }catch(IOException e){
                Log.e("Connecting to Server ", "Error", e);
                return null;
            } finally{
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
            try{

                dataMovie = getMovieData(discoverString);
                dataReview = getReviewData(reviewString);
                Log.v("DataMovie Array", dataMovie[0]+" " +dataReview[0]);
                movieDataArray = concatArrays(dataMovie, dataReview);

                return movieDataArray;

            }catch(JSONException e){
                Log.e("Json", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
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
            Log.v("Got Title", movieResults[0]);
            Log.v("Got Plot", movieResults[1]);
            Log.v("Got Rating", movieResults[2]);

            return movieResults;

        }//end getMovieData

        public String[] getReviewData(String revString){
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
                    if(i>0) {
                        data[1] += " " + reviewData.getString(MTB_WRITER);
                        data[2] += "\n"+ "\n" + reviewData.getString(MTB_REVIEW);
                    }else {
                        data[2] += reviewData.getString(MTB_REVIEW);
                        data[1] += reviewData.getString(MTB_WRITER);
                    }
                }
                return data;
            }catch (JSONException e){
                Log.e("Json", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        public String[] concatArrays(String[] array1, String[] array2){
            String[] results = new String [array1.length +array2.length];

            for(int i = 0; i <array1.length; i++){
                results[i] = array1[i];
            }

            for(int i = array1.length; i< array1.length +array2.length; i++){
                results[i] = array2[i-array1.length];
            }
            return results;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            TextView title =  (TextView)getActivity().findViewById(R.id.movie_title);
            TextView plot =  (TextView)getActivity().findViewById(R.id.movie_plot);
            TextView rating =  (TextView)getActivity().findViewById(R.id.movie_rating);
            TextView release_date =  (TextView)getActivity().findViewById(R.id.movie_release);
            TextView review_tile = (TextView)getActivity().findViewById(R.id.movie_review_tile);
            TextView review_content = (TextView)getActivity().findViewById(R.id.movie_review_content);

            ImageView poster = (ImageView)getActivity().findViewById(R.id.thumbnail);

            title.setText(strings[0]);
            plot.setText(strings[1]);
            rating.setText(strings[2]+"/10");
            release_date.setText(strings[3]);
            review_tile.setText("Reviews (" +strings[5]+")" );
            review_content.setText(strings[7]);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/" + strings[4])
                    .into(poster);

        }
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
