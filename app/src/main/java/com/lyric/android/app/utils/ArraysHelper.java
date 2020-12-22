package com.lyric.android.app.utils;

/**
 * 数组工具类
 *
 * @author Lyric Gan
 * @since 2020/12/22
 */
public class ArraysHelper {

    public static int binarySearch(int[] array, int fromIndex, int toIndex, int value) {
        if (array == null || array.length == 0) {
            return -1;
        }
        if (fromIndex > toIndex || fromIndex < 0 || toIndex > array.length) {
            return -1;
        }
        int low = fromIndex;
        int high = toIndex - 1;
        while ((low <= high)) {
            final int middle = (low + high) >>> 1;
            final int middleValue = array[middle];
            if (middleValue < value) {
                low = middle + 1;
            } else if (middleValue > value) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -(low + 1);
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
        quickSortInner(array, low, high);
        return array;
    }

    private static void quickSortInner(int[] array, int low, int high) {
        if (low < high) {
            int middle = partition(array, low, high);
            quickSortInner(array, low, middle - 1);
            quickSortInner(array, middle + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        if (array == null || array.length == 0) {
            return 0;
        }
        int i = low - 1;
        int j;
        int temp;
        for (j = low; j < high; ++j) {
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
