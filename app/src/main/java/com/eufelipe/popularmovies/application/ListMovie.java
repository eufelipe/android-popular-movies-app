package com.eufelipe.popularmovies.application;


public class ListMovie {

    public enum Category {
        POPULAR, TOP_RATED, FAVORITE
    }

    public static int getListMovieCategoryId(Category category) {
        switch (category) {
            case FAVORITE:
                return 3;

            case TOP_RATED:
                return 2;

            default:
                return 1;
        }
    }

    public static Category getListMovieCategory(int id) {
        switch (id) {
            case 3:
                return Category.FAVORITE;

            case 2:
                return Category.TOP_RATED;

            default:
                return Category.POPULAR;
        }
    }

}
