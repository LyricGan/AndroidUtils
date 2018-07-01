package com.lyric.android.app.utils;

/**
 * @author lyricgan
 */
public class SortUtils {

    private SortUtils() {
    }

    public static int[] bubbleSort(int[] array) {
        if (array == null || array.length == 0) {
            return array;
        }
        int length = array.length;
        int temp;
        for (int i = length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    public static int[] bubbleSort(int[] array, int min, int max) {
        if (array == null || array.length == 0) {
            return array;
        }
        int i;
        int temp;
        while (min < max) {
            for (i = min; i < max; i++) {
                if (array[i] > array[i + 1]) {
                    temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                }
            }
            max--;
            for (i = max; i > min; i--) {
                if (array[i] < array[i - 1]) {
                    temp = array[i];
                    array[i] = array[i - 1];
                    array[i - 1] = temp;
                }
            }
            min++;
        }
        return array;
    }

    public static int[] quickSort(int[] array, int low, int high) {
        if (array == null || array.length == 0) {
            return array;
        }
        if (low < high) {
            int middle = partition(array, low, high);
            quickSort(array, low, middle - 1);
            quickSort(array, middle + 1, high);
        }
        return array;
    }

    private static int partition(int[] array, int low, int high) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int i = low - 1;
        int j;
        int temp;
        for (j = low; j < high; ++ j) {
            if (array[j] < array[high]) {
                temp = array[++i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return (i + 1);
    }
}
