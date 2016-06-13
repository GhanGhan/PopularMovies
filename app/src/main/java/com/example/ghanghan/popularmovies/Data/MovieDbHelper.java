package com.example.ghanghan.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ghanghan.popularmovies.Data.MovieContract.PopularEntry;
import com.example.ghanghan.popularmovies.Data.MovieContract.HighestRatedEntry;
import com.example.ghanghan.popularmovies.Data.MovieContract.FavoritedEntry;

/**
 * Created by GhanGhan on 6/1/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "PopularMovies.db";

    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create popular, rated, and favorites table
        final String SQL_CREATE_POPULAR_TABLE = "CREATE TABLE " + PopularEntry.TABLE_NAME + "(" +
                PopularEntry._ID + "INTEGER PRIMARY KEY, " +
                PopularEntry.COLUMN_FAVOURITE_KEY + " INTEGER NOT NULL, " +
                PopularEntry.COLUMN_MOVIE_ID + "STRING NOT NULL," +
                PopularEntry.COLUMN_POSTER_PATH + " STRING NOT NULL," +
                PopularEntry.COLUMN_ORIGINAL_TITLE +" STRING NOT NULL, "+
                PopularEntry.COLUMN_STATUS + "STRING NOT NULL, " +
                PopularEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                PopularEntry.COLUMN_TRAILER_KEYS + " STRING NOT NULL, " +
                PopularEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                PopularEntry.COLUMN_NUMBER_OF_REVIEWS + " INTEGER NOT NULL, " +
                PopularEntry.COLUMN_AUTHORS + " STRING NOT NULL, " +
                PopularEntry.COLUMN_REVIEW_CONTENT + "STRING NOT NULL" +
                "FOREIGN KEY " + "(" + PopularEntry.COLUMN_FAVOURITE_KEY + ")" +
                "REFERENCES " + FavoritedEntry.TABLE_NAME + "(" + FavoritedEntry._ID + ");";

        final String SQL_CREATE_HIGH_RATED_TABLE = "CREATE TABLE "+HighestRatedEntry.TABLE_NAME+"("+
                HighestRatedEntry._ID + "INTEGER PRIMARY KEY, " +
                HighestRatedEntry.COLUMN_FAVOURITE_KEY + " INTEGER NOT NULL, " +
                HighestRatedEntry.COLUMN_MOVIE_ID + "STRING NOT NULL," +
                HighestRatedEntry.COLUMN_POSTER_PATH + " STRING NOT NULL," +
                HighestRatedEntry.COLUMN_ORIGINAL_TITLE +" STRING NOT NULL, "+
                HighestRatedEntry.COLUMN_STATUS + "STRING NOT NULL, " +
                HighestRatedEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                HighestRatedEntry.COLUMN_TRAILER_KEYS + " STRING NOT NULL, " +
                HighestRatedEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                HighestRatedEntry.COLUMN_NUMBER_OF_REVIEWS + " INT NOT NULL, " +
                HighestRatedEntry.COLUMN_AUTHORS + " STRING NOT NULL, " +
                HighestRatedEntry.COLUMN_REVIEW_CONTENT + "STRING NOT NULL " +
                "FOREIGN KEY " + "(" + HighestRatedEntry.COLUMN_FAVOURITE_KEY + ")" +
                "REFERENCES " + FavoritedEntry.TABLE_NAME + "(" + FavoritedEntry._ID + ");";

        final String SQL_CREATE_FAVOURITED_TABLE = "CREATE TABLE "+FavoritedEntry.TABLE_NAME+"("+
                FavoritedEntry._ID + "INTEGER PRIMARY KEY, " +
                FavoritedEntry.COLUMN_MOVIE_ID + "STRING NOT NULL," +
                FavoritedEntry.COLUMN_POSTER_PATH + " STRING NOT NULL," +
                FavoritedEntry.COLUMN_ORIGINAL_TITLE +" STRING NOT NULL, "+
                FavoritedEntry.COLUMN_STATUS + "STRING NOT NULL, " +
                FavoritedEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoritedEntry.COLUMN_TRAILER_KEYS + " STRING NOT NULL, " +
                FavoritedEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +
                FavoritedEntry.COLUMN_NUMBER_OF_REVIEWS + " INT NOT NULL, " +
                FavoritedEntry.COLUMN_AUTHORS + " STRING NOT NULL, " +
                FavoritedEntry.COLUMN_REVIEW_CONTENT + "STRING NOT NULL);";

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HighestRatedEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
