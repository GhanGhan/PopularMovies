package com.example.ghanghan.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by GhanGhan on 6/24/2016.
 */
public class ToFile implements Runnable{
    public File filePath;
    public String link;
    public Context mContext;

    public ToFile(File file, String url, Context context){
        filePath = file;
        link = url;
        mContext = context;
    }
    @Override
    public void run() {
        FileOutputStream outputStream = null;
        Bitmap image = null;
        try {
            image = Picasso.with(mContext).load(link).error(R.drawable.invalid_poster).get();
            Log.v("Image string", "" + image.toString());
            //place bitmaps in myPath_ location
            Log.v("To File Thread", "place image in folder");

            outputStream = new FileOutputStream(filePath);
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
