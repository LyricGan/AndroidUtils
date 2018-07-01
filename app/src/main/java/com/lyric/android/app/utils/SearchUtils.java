package com.lyric.android.app.utils;

/**
 * @author lyricgan
 */
public class SearchUtils {

    private SearchUtils() {
    }

    public static int binarySearch(int[] array, int value) {
        if (array == null || array.length == 0) {
            return -1;
        }
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int middle = left + ((right - left) >> 2);
            if (array[middle] > value) {
                right = middle - 1;
            } else if (array[middle] < value) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }
}
