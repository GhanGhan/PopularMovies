package com.example.ghanghan.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by GhanGhan on 6/20/2016.
 */
public class FileTarget implements Target {
    private static final String LOG_TAG = FileTarget.class.getName();

    private File filePath;
    public FileTarget(File path){
        filePath = path;
    }
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        FileOutputStream outputStream;
        try{
            //place bitmaps in myPath_ location
            Log.v(LOG_TAG, filePath.toString() + "dl bitmap");
            outputStream = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}