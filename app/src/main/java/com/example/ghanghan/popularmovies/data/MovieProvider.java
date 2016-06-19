package com.example.ghanghan.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;

import com.example.ghanghan.popularmovies.data.MovieContract;
import com.example.ghanghan.popularmovies.data.MovieContract.PopularEntry;
import com.example.ghanghan.popularmovies.data.MovieContract.HighestRatedEntry;
import com.example.ghanghan.popularmovies.data.MovieContract.FavoritedEntry;
import com.example.ghanghan.popularmovies.data.MovieDbHelper;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by GhanGhan on 6/4/2016.
 */
public class MovieProvider extends ContentProvider {

    private MovieDbHelper movieDbHelper;
    //The URI Matcher used by the content Provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int POPULAR = 100;
    public static final int POPULAR_WITH_ID = 101;
    public static final int HIGHEST_RATED = 200;
    public static final int HIGHEST_RATED_WITH_ID = 201;
    public static final int FAVORITE = 300;
    public static final int FAVORITE_WITH_ID = 301;

    @Override
    public boolean onCreate() {
         movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }




    static UriMatcher buildUriMatcher(){
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        //2 Use addUri functions to match each of the types
        matcher.addURI(authority, MovieContract.PATH_POPULAR, POPULAR);
        matcher.addURI(authority, MovieContract.PATH_POPULAR + "/#", POPULAR_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_HIGH_RATING, HIGHEST_RATED);
        matcher.addURI(authority, MovieContract.PATH_HIGH_RATING + "/#", HIGHEST_RATED_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_FAVORITE + "/#", FAVORITE_WITH_ID);

        return matcher;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        //Use the URI matcher to determine what kind of URI this is
        final int match = sUriMatcher.match(uri);

        switch(match){
            case POPULAR:
                return PopularEntry.CONTENT_TYPE;
            case POPULAR_WITH_ID:
                return PopularEntry.CONTENT_ITEM_TYPE;
            case HIGHEST_RATED:
                return HighestRatedEntry.CONTENT_TYPE;
            case HIGHEST_RATED_WITH_ID:
                return HighestRatedEntry.CONTENT_ITEM_TYPE;
            case FAVORITE:
                return FavoritedEntry.CONTENT_TYPE;
            case FAVORITE_WITH_ID:
                return FavoritedEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    public Cursor getPopularRow(Uri uri, String[] projection){
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        String id = PopularEntry.getRowId(uri);
        String selection = "_id = ?";
        String[] selectionArgs = {id};
        return db.query(PopularEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
        null);
    }

    public Cursor getHighRatedRow(Uri uri, String[] projection){
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        String id = HighestRatedEntry.getRowId(uri);
        String selection = "_id = ?";
        String[] selectionArgs = {id};
        return db.query(HighestRatedEntry.TABLE_NAME, projection, selection, selectionArgs, null,
                null, null);
    }

    public Cursor getFavoriteRow(Uri uri, String[] projection){
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        String id = FavoritedEntry.getRowId(uri);
        String selection = "_id = ?";
        String[] selectionArgs = {id};
        return db.query(FavoritedEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                null);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        Cursor returnCursor;
        switch(sUriMatcher.match(uri)){
            case POPULAR: //"popular"
                returnCursor = db.query(PopularEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case POPULAR_WITH_ID: //"popular/#"
                returnCursor = getPopularRow(uri, projection);
                break;

            case HIGHEST_RATED: //"highest_rated"
                returnCursor = db.query(HighestRatedEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case HIGHEST_RATED_WITH_ID: //"highest_rated/#"
                returnCursor = getHighRatedRow(uri, projection);
                break;
            case FAVORITE: //"favorite"
                returnCursor = db.query(FavoritedEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE_WITH_ID: //"favorite/#"
                returnCursor = getFavoriteRow(uri, projection);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long _id;
        Uri retUri;
        SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        switch(match){
            case POPULAR:
                _id = db.insert(PopularEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    retUri = PopularEntry.buildPopularUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case HIGHEST_RATED:
                _id = db.insert(HighestRatedEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    retUri = HighestRatedEntry.buildHighestRatedUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case FAVORITE:
                _id = db.insert(FavoritedEntry.TABLE_NAME, null, contentValues);
                if(_id > 0)
                    retUri = FavoritedEntry.buildFavoriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int retRowsDelete = 0;
        final int match = sUriMatcher.match(uri);
        // This makes delete all rows return the number of rows deleted
        if(null == selection) selection ="1";

        switch(match){
            case POPULAR:
                retRowsDelete = db.delete(PopularEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case HIGHEST_RATED:
                retRowsDelete = db.delete(HighestRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FAVORITE:
                retRowsDelete = db.delete(HighestRatedEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if(retRowsDelete != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return retRowsDelete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        int retUpRows = 0;
        int match = sUriMatcher.match(uri);

        switch(match){
            case POPULAR:
                retUpRows = db.update(PopularEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case HIGHEST_RATED:
                retUpRows = db.update(PopularEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case FAVORITE:
                retUpRows = db.update(PopularEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if(retUpRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return retUpRows;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch(match){
            case POPULAR:
                db.beginTransaction();
                try{
                    for(ContentValues value: values){
                        long _id = db.insert(PopularEntry.TABLE_NAME, null, value);
                        if(_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount; // end case Popular
            case HIGHEST_RATED:
                db.beginTransaction();
                try{
                    for(ContentValues value: values){
                        long _id = db.insert(HighestRatedEntry.TABLE_NAME, null, value);
                        if(_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;// end case Highest Rated
            case FAVORITE:
                db.beginTransaction();
                try{
                    for(ContentValues value: values){
                        long _id = db.insert(FavoritedEntry.TABLE_NAME, null, value);
                        if (_id != -1)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
        }

        return super.bulkInsert(uri, values);
    }
}
