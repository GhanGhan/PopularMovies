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
import org.w3c.dom.Text;

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
    String url;

    private OnFragmentInteractionListener mListener;

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

        return inflater.inflate(R.layout.fragment_details, container, false);
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

            //Raw Json String data
            String discoverString = null;

            try{

                URL url = new URL(strings[0]);
                //create request for TheMovieDataBase and open connection
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

                while((line = reader.readLine())!= null){
                    buffer.append(line + "\n");
                }

                if(buffer.length()==0){
                    return null;
                }

                discoverString = buffer.toString();
                Log.v("Data: ", discoverString);
                String data[] = getData(discoverString);

            }catch(MalformedURLException e){
                Log.e("Creating URL ", "Error", e);
                return null;
            }catch(IOException e){
                Log.e("Connecting to Server ", "Error", e);
                return null;
            }catch(JSONException e){
                Log.e("Json ", "Error", e);
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
                String data[] = getData(discoverString);

                return data;

            }catch(JSONException e){
                Log.e("Json", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        public String[] getData(String movieSt) throws JSONException {

            final String MTB_TITLE = "original_title";
            final String MTB_PLOT = "overview";
            final String MTB_RATING = "vote_average";
            final String MTB_RELEASE = "status";
            final String MTB_THUMBNAIL = "poster_path";
            String poster;
            int idInt;

            JSONObject movieJson = new JSONObject(movieSt);
            //JSONArray movieArray = movieJson.getJSONArray(MTB_RESULTS);
            String[] results = new String[5];
            results[0] = movieJson.getString(MTB_TITLE);
            results[1] = movieJson.getString(MTB_PLOT);
            results[2] = movieJson.getString(MTB_RATING);
            results[3] = movieJson.getString(MTB_RELEASE);
            results[4] = movieJson.getString(MTB_THUMBNAIL);
            Log.v("Got Title", results[0]);
            Log.v("Got Plot", results[1]);
            Log.v("Got Rating", results[2]);
            return results;

        }//end getData

        @Override
        protected void onPostExecute(String[] strings) {
            TextView title =  (TextView)getActivity().findViewById(R.id.movie_title);
            TextView plot =  (TextView)getActivity().findViewById(R.id.movie_plot);
            TextView rating =  (TextView)getActivity().findViewById(R.id.movie_rating);
            TextView release_date =  (TextView)getActivity().findViewById(R.id.movie_release);

            ImageView poster = (ImageView)getActivity().findViewById(R.id.thumbnail);

            title.setText(strings[0]);
            plot.setText(strings[1]);
            rating.setText(strings[2]+"/10");
            release_date.setText(strings[3]);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w500/" + strings[4])
                    .into(poster);

        }
    }
}
