package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by GhanGhan on 6/14/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    //will get from server
    private String[] mThumbIds = null;
    private String[] movieID = null;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setThumbIds(String[] thumbIds){
        mThumbIds = thumbIds;
    }

    public void setMovieID(String[] movieIDs){
        movieID = movieIDs;
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

            Picasso.with(mContext).load(url).into(imageView);
        }

        return imageView;
    }

}// end ImageAdapter Class