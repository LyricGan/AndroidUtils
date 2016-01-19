package com.lyric.android.app.test;

/**
 * @author ganyu
 * @description 排序测试类
 * @time 2016/1/19 13:15
 */
public class SortTest {

    /**
     * 冒泡排序
     * @param array 排序数组
     * @return
     */
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

    /**
     * 冒泡排序，双向排序
     * @param array 排序数组
     * @param min 数组最小索引
     * @param max 数组最大索引
     * @return
     */
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

    /**
     * 快速排序
     * @param array 排序数组
     * @param low 数组最小索引
     * @param high 数组最大索引
     * @return
     */
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

    /**
     * 划分数组，获取数组索引
     * @param array 排序数组
     * @param low 数组最小索引
     * @param high 数组最大索引
     * @return
     */
    public static int partition(int[] array, int low, int high) {
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

    /**
     * 二分查找
     * @param array 查找的数组
     * @param value 查找的值
     * @return
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
