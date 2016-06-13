package com.example.ghanghan.popularmovies.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by GhanGhan on 5/24/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.ghanghan.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POPULAR = "popular";
    public static final String PATH_HIGH_RATING = "high_rate";
    public static final String PATH_FAVOURITE = "favorite";

    public static final class PopularEntry implements BaseColumns{
        public static final String TABLE_NAME = "popular";

        //TODO: path and trailer keys will have to be where pictures are stored in the phone

        public static final String COLUMN_FAVOURITE_KEY = "favourite_id";
        public static final String COLUMN_MOVIE_ID = "id"; //used in MoviesFragment
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TRAILER_KEYS = "key";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        public static final String COLUMN_AUTHORS = "authors";
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_POPULAR;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_POPULAR;

        public static Uri buildPopularUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class HighestRatedEntry implements BaseColumns{
        public static final String TABLE_NAME = "high_rate";

        //TODO: path and trailer keys will have to be where pictures are stored in the phone
        public static final String COLUMN_FAVOURITE_KEY = "favourite_id";
        public static final String COLUMN_MOVIE_ID = "id"; //used in MoviesFragment
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TRAILER_KEYS = "key";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        public static final String COLUMN_AUTHORS = "authors";
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_HIGH_RATING;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_HIGH_RATING;

        public static Uri buildHighestRatedUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        
    }

    public static final class FavoritedEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_TRAILER_KEYS = "key";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        public static final String COLUMN_AUTHORS = "authors";
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_FAVOURITE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_FAVOURITE;

        public static Uri buildFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
