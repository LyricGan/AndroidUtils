package com.lyric.android.app.test.arithmetic;

/**
 * 查找工具类
 * @author lyricgan
 * @date 2017/12/1 11:10
 */
public class SearchUtils {

    private SearchUtils() {
    }

    /**
     * 二分查找
     * @param array 查找的数组
     * @param value 查找的值
     * @return int
     */
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
