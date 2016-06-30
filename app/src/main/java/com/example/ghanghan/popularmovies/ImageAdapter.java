package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by GhanGhan on 6/14/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //will get from server
    private String[] mThumbIds = null;
    private String[] movieID = null;
    private String[] mPosterKey = null;
    private File[]   mPosterPaths = null;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setThumbIds(String[] thumbIds){
        mThumbIds = thumbIds;
    }

    public void setMovieID(String[] movieIDs){
        movieID = movieIDs;
    }

    public void setPosterKeys(String [] posterKeys){
        mPosterKey = posterKeys;
    }

    public void setPosterPaths(File[] posterPaths){
        mPosterPaths = posterPaths;
    }

    public void setPosterPath(int position, File path){
        mPosterPaths[position] = path;
    }

    public int getCount() {
        if(mThumbIds== null)
            return 0;
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public String getMovieID(int position){
        return movieID[position];
    }

    public String[] getMovieIdArray(){
        return movieID;
    }

    public int getNumberOfMovies(){
        return movieID.length;
    }

    public long getItemId(int position) {
        return 0;
    }

    public File getPosterPath(int position){
        return mPosterPaths[position];
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        //GETTING FROM FILE SYSTEM
        ContextWrapper wrapper = new ContextWrapper(mContext.getApplicationContext());
        /*File directory = wrapper.getDir(getMovieID(position), Context.MODE_PRIVATE);//path to the folder
        File posterPath = new File(directory.getAbsolutePath(), mPosterKey[position]);//path to poster
        Log.v("Directory", directory.getAbsolutePath());
        Log.v("posterPath", posterPath.getAbsolutePath());*/
        ///////////////////////////////////////////
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else
            imageView = (ImageView) convertView;

        if(mPosterPaths != null) {
            String url = (String)getItem(position);

            Log.v("Adap Positon",""+ position );
            while(getPosterPath(position).length() == 0){//loop until image is in file location
                Log.v("ImageAdapter", "file is empty");
            }


            //Picasso.with(mContext).load(url).into(imageView); //from server
            Picasso.with(mContext).load(getPosterPath(position)).into(imageView);
        }

        return imageView;
    }

}// end ImageAdapter Class