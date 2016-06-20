package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.database.Cursor;

import com.example.ghanghan.popularmovies.data.MovieContract;

/**
 * Created by GhanGhan on 6/18/2016.
 */
public class LoadMoviePoster {
    private ImageAdapter mImageAdapter;
    private Context mContext;
    private String[] imageProjection = {MovieContract.PopularEntry.COLUMN_POSTER_PATH,
            MovieContract.PopularEntry.COLUMN_MOVIE_ID};

    public LoadMoviePoster(ImageAdapter imageAdapter, Context context){
        mImageAdapter = imageAdapter;
        mContext = context;

    }

    public void loadImages(){
        Cursor imageCursor = mContext.getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                imageProjection,null, null, null);
        String[] posterArray = new String[18];
        String[] idArray = new String[18];


        for(int i = 0; i< 18; i++){
            imageCursor.moveToPosition(i);
            posterArray[i] = "http://image.tmdb.org/t/p/w500/" + imageCursor.getString(0);
            idArray[i] = imageCursor.getString(1);
        }

        mImageAdapter.setThumbIds(posterArray);
        mImageAdapter.setMovieID(idArray);
        mImageAdapter.notifyDataSetChanged();
        imageCursor.close();

    }
}
