package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by GhanGhan on 7/2/2016.
 */
public class PosterAdapter extends CursorAdapter {

private String mTable;

    public PosterAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        //get table
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        mTable = prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));

    }

    public static class ViewHolder{
        public final ImageView posterView;

        public ViewHolder(View view){
            posterView = (ImageView)view.findViewById(R.id.grid_element_poster);
        }
    }

    //populare view with information
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ContextWrapper wrapper = new ContextWrapper(context);
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        String folderName;
        String posterName;
        File directory;
        File posterPath;
        if(mTable.equals("popularity.desc") || mTable.equals("vote_average.desc")){
            folderName = cursor.getString(DetailsFragment.COL_MOVIE_ID);
            posterName = cursor.getString(DetailsFragment.COL_POSTER_PATH);
            directory = wrapper.getDir(folderName, Context.MODE_PRIVATE);
        }
        else{
            folderName = cursor.getString((DetailsFragment.COL_MOVIE_ID -1));
            posterName = cursor.getString(DetailsFragment.COL_POSTER_PATH - 1);
            directory = wrapper.getDir(folderName, Context.MODE_PRIVATE);
        }
        posterPath = new File(directory, posterName);

        ImageView posterView = viewHolder.posterView;
        Picasso.with(context).load(posterPath).into(posterView);

    }

    //decide on the layout to use
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //choose layout type
        //only have one layout type for grid cell
        int layout = R.layout.grid_item_poster;
        //inflate layout
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);
        return view;
    }

}
