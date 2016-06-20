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
public class ThumbnailAdapter extends BaseAdapter {
    private Context mContext;
    //will get from server
    private String[] mTrailerImageKey = null;
    private String[] mTrailerId = null;


    public ThumbnailAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        if(mTrailerImageKey== null)
            return 0;
        return mTrailerImageKey.length;
    }

    public Object getItem(int position) {
        return mTrailerImageKey[position];
    }

    public String getTrailerId(int position){
        return mTrailerId[position];
    }

    public int getNumberOfTrailers(){
        return mTrailerImageKey.length;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setTrailerImageKey( String[] keys){
        mTrailerImageKey = keys;
    }

    public void setTrailerId(String[] iD){
        mTrailerId = iD;
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


        if(mTrailerImageKey != null) {
            String url = (String)getItem(position);
            //Log.v("In adapter", "The  URL " + getCount());
            Picasso.with(mContext).load(url).into(imageView);
        }
        return imageView;
    }

}// end ThumbNailAdapter Class
