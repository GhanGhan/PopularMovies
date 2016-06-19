package com.example.ghanghan.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by GhanGhan on 5/24/2016.
 */
public class MovieContract {
    //'Content authority' is the name of the entire content provider
    public static final String CONTENT_AUTHORITY = "com.example.ghanghan.popularmovies";
    //'Base content uri' is the base of all URI's which the apps wil use to access content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //POSSIBLE PATHS
    public static final String PATH_POPULAR = "popular";//Append to 'Base content uri' to access popular movie table
    public static final String PATH_HIGH_RATING = "high_rate";//Append to 'Base content uri' to access high rating movie table
    public static final String PATH_FAVORITE = "favorite";//Append to 'Base content uri' to access favorite movie table

    public static final class PopularEntry implements BaseColumns{
        public static final String TABLE_NAME = "popular"; //name of the table

        //TODO: path and trailer keys will have to be where pictures are stored in the phone, large text (overview and reviews) may be stored in txt files
        //Column with the foreign key into the favorite table
        public static final String COLUMN_FAVORITE_KEY = "favorite_id";
        //Unique to each movie, used to access movie specific data from data
        //Also used as name of folder containing movie data (trailer keys, poster etc.)
        public static final String COLUMN_MOVIE_ID = "id"; //used in MoviesFragment
        //Key for obtaining movie poster from the server
        //Also used as name of poster in app file system
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        //Title of the movie
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        //State of movie release
        public static final String COLUMN_STATUS = "status";
        //User generated average score of the movie out of 10
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        //Key for link to movie trailer(s)
        //Also used as name of trailer thumbnail in app file system
        public static final String COLUMN_TRAILER_KEYS = "key";
        //Plot summary of the movie
        public static final String COLUMN_OVERVIEW = "overview";
        //Number of reviews about the movie on the server
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        //Author(s) of the review(s)
        public static final String COLUMN_AUTHORS = "authors";
        //The actual movie review(s)
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_POPULAR).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_POPULAR;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_POPULAR;

        public static Uri buildPopularUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getRowId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

    public static final class HighestRatedEntry implements BaseColumns{
        public static final String TABLE_NAME = "high_rate"; // name of the table

        //TODO: path and trailer keys will have to be where pictures are stored in the phone, large text (overview and reviews) may be stored in txt files
        //Column with the foreign key into the favorite table
        public static final String COLUMN_FAVORITE_KEY = "favourite_id";
        //Unique to each movie, used to access movie specific data from data
        //Also used as name of folder containing movie data (trailer keys, poster etc.)
        public static final String COLUMN_MOVIE_ID = "id"; //used in MoviesFragment
        //Key for obtaining movie poster from the server
        //Also used as name of poster in app file system
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        //Title of the movie
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        //State of movie release
        public static final String COLUMN_STATUS = "status";
        //User generated average score of the movie out of 10
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        //Key for link to movie trailer(s)
        //Also used as name of trailer thumbnail in app file system
        public static final String COLUMN_TRAILER_KEYS = "key";
        //Plot summary of the movie
        public static final String COLUMN_OVERVIEW = "overview";
        //Number of reviews about the movie on the server
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        //Author(s) of the review(s)
        public static final String COLUMN_AUTHORS = "authors";
        //The actual movie review(s)
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_HIGH_RATING).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_HIGH_RATING;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_HIGH_RATING;

        public static Uri buildHighestRatedUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getRowId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }

    public static final class FavoritedEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite";

        //Unique to each movie, used to access movie specific data from data
        //Also used as name of folder containing movie data (trailer keys, poster etc.)
        public static final String COLUMN_MOVIE_ID = "id"; //used in MoviesFragment
        //Key for obtaining movie poster from the server
        //Also used as name of poster in app file system
        public static final String COLUMN_POSTER_PATH = "poster_path";  //used in MoviesFragment
        //Title of the movie
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";//used in Details Fragment
        //State of movie release
        public static final String COLUMN_STATUS = "status";
        //User generated average score of the movie out of 10
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        //Key for link to movie trailer(s)
        //Also used as name of trailer thumbnail in app file system
        public static final String COLUMN_TRAILER_KEYS = "key";
        //Plot summary of the movie
        public static final String COLUMN_OVERVIEW = "overview";
        //Number of reviews about the movie on the server
        public static final String COLUMN_NUMBER_OF_REVIEWS = "total_results";
        //Author(s) of the review(s)
        public static final String COLUMN_AUTHORS = "authors";
        //The actual movie review(s)
        public static final String COLUMN_REVIEW_CONTENT = "content";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE = ContentResolver.
                CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_FAVORITE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.
                CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                PATH_FAVORITE;

        public static Uri buildFavoriteUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getRowId(Uri uri){
            return uri.getPathSegments().get(1);
        }

    }
}
