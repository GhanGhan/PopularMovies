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
    private String mTable;
    private String[] imageProjectionPopular = {MovieContract.PopularEntry.COLUMN_POSTER_PATH,
            MovieContract.PopularEntry.COLUMN_MOVIE_ID};
    private String[] imageProjetionHighRate = {MovieContract.HighestRatedEntry.COLUMN_POSTER_PATH,
            MovieContract.HighestRatedEntry.COLUMN_MOVIE_ID};

    public LoadMoviePoster(ImageAdapter imageAdapter, Context context, String table){
        mImageAdapter = imageAdapter;
        mContext = context;
        mTable = table;

    }

    public void loadImages(){
        String[] posterArray = new String[18];
        String[] idArray = new String[18];
        if(mTable.equals("popularity.desc")) {
            Cursor imageCursor = mContext.getContentResolver().query(MovieContract.PopularEntry.CONTENT_URI,
                    imageProjectionPopular, null, null, null);
            for (int i = 0; i < 18; i++) {
                imageCursor.moveToPosition(i);
                posterArray[i] = "http://image.tmdb.org/t/p/w500/" + imageCursor.getString(0);
                idArray[i] = imageCursor.getString(1);
            }
            mImageAdapter.setThumbIds(posterArray);
            mImageAdapter.setMovieID(idArray);
            mImageAdapter.notifyDataSetChanged();
            imageCursor.close();
        }
        else if(mTable.equals("vote_average.desc")){
            Cursor imageCursor = mContext.getContentResolver().query(MovieContract.HighestRatedEntry.CONTENT_URI,
                    imageProjetionHighRate, null, null, null);
            for (int i = 0; i < 18; i++) {
                imageCursor.moveToPosition(i);
                posterArray[i] = "http://image.tmdb.org/t/p/w500/" + imageCursor.getString(0);
                idArray[i] = imageCursor.getString(1);
            }
            mImageAdapter.setThumbIds(posterArray);
            mImageAdapter.setMovieID(idArray);
            mImageAdapter.notifyDataSetChanged();
            imageCursor.close();
        }
    }//end load images
}
