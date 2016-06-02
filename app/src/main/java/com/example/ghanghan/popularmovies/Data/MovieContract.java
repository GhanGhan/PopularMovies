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

        public static Uri buildPopularMovieIdPoster(String movieId, String posterPath){
            return CONTENT_URI.buildUpon().appendPath(movieId).appendPath(posterPath).build();
        }

        public static Uri buildPopularDetailInformation(String favId, String movieId, String poster,
                                                       String title, String status,
                                                       String voteAverage, String trailers,
                                                       String overview, int numberOfReviews,
                                                       String authors, String reviews){
            return CONTENT_URI.buildUpon().appendPath(favId).appendPath(movieId).appendPath(poster).
                    appendPath(title).appendPath(status).appendPath(voteAverage).
                    appendPath(trailers).appendPath(overview).
                    appendPath(Integer.toString(numberOfReviews)).appendPath(authors).
                    appendPath(reviews).build();
        }

        public static int getFavIdFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getMoviePosterFromUri(Uri uri){
            return uri.getPathSegments().get(3);
        }

        public static String getOriginalTitleFromUri(Uri uri){
            return uri.getPathSegments().get(4);
        }

        public static String getStatusFromUri(Uri uri){
            return uri.getPathSegments().get(5);
        }

        public static String getVoteAverageFromUri(Uri uri){
            return uri.getPathSegments().get(6);
        }

        public static String getTrailersFromUri(Uri uri){
            return uri.getPathSegments().get(7);
        }

        public static String getOverviewFromUri(Uri uri){
            return uri.getPathSegments().get(8);
        }
        public static String getNumberOfReviews(Uri uri){
            return uri.getPathSegments().get(9);
        }

        public static String getAuthors(Uri uri){
            return uri.getPathSegments().get(10);
        }

        public static String getReviews(Uri uri){
            return uri.getPathSegments().get(11);
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

        public static Uri buildHightestRatedMovieIdPoster(String movieId, String posterPath){
            return CONTENT_URI.buildUpon().appendPath(movieId).appendPath(posterPath).build();
        }

        public static Uri buildHighestRatedDetailInformation(String favId, String movieId, String poster,
                                                       String title, String status,
                                                       String voteAverage, String trailers,
                                                       String overview, int numberOfReviews,
                                                       String authors, String reviews){
            return CONTENT_URI.buildUpon().appendPath(favId).appendPath(movieId).appendPath(poster).
                    appendPath(title).appendPath(status).appendPath(voteAverage).
                    appendPath(trailers).appendPath(overview).
                    appendPath(Integer.toString(numberOfReviews)).appendPath(authors).
                    appendPath(reviews).build();
        }

        public static int getFavIdFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }

        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getMoviePosterFromUri(Uri uri){
            return uri.getPathSegments().get(3);
        }

        public static String getOriginalTitleFromUri(Uri uri){
            return uri.getPathSegments().get(4);
        }

        public static String getStatusFromUri(Uri uri){
            return uri.getPathSegments().get(5);
        }

        public static String getVoteAverageFromUri(Uri uri){
            return uri.getPathSegments().get(6);
        }

        public static String getTrailersFromUri(Uri uri){
            return uri.getPathSegments().get(7);
        }

        public static String getOverviewFromUri(Uri uri){
            return uri.getPathSegments().get(8);
        }
        public static String getNumberOfReviews(Uri uri){
            return uri.getPathSegments().get(9);
        }

        public static String getAuthors(Uri uri){
            return uri.getPathSegments().get(10);
        }

        public static String getReviews(Uri uri){
            return uri.getPathSegments().get(11);
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

        public static Uri buildFavoriteMovieIdPoster(String movieId, String posterPath){
            return CONTENT_URI.buildUpon().appendPath(movieId).appendPath(posterPath).build();
        }

        public static Uri buildHighestRatedDetailInformation(String movieId, String poster,
                                                             String title, String status,
                                                             String voteAverage, String trailers,
                                                             String overview, int numberOfReviews,
                                                             String authors, String reviews){
            return CONTENT_URI.buildUpon().appendPath(movieId).appendPath(poster).appendPath(title).
                    appendPath(status).appendPath(voteAverage).appendPath(trailers).
                    appendPath(overview).appendPath(Integer.toString(numberOfReviews)).
                    appendPath(authors).appendPath(reviews).build();
        }


        public static String getMovieIdFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static String getMoviePosterFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }

        public static String getOriginalTitleFromUri(Uri uri){
            return uri.getPathSegments().get(3);
        }

        public static String getStatusFromUri(Uri uri){
            return uri.getPathSegments().get(4);
        }

        public static String getVoteAverageFromUri(Uri uri){
            return uri.getPathSegments().get(5);
        }

        public static String getTrailersFromUri(Uri uri){
            return uri.getPathSegments().get(6);
        }

        public static String getOverviewFromUri(Uri uri){
            return uri.getPathSegments().get(7);
        }
        public static String getNumberOfReviews(Uri uri){
            return uri.getPathSegments().get(8);
        }

        public static String getAuthors(Uri uri){
            return uri.getPathSegments().get(9);
        }

        public static String getReviews(Uri uri){
            return uri.getPathSegments().get(10);
        }

    }
}
