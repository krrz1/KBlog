package com.krrz;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DETEST {
    public static void quickSort(char[] arr, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(arr, left, right);
            quickSort(arr, left, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    private static int partition(char[] arr, int left, int right) {
        int pivotIndex = left;
        char pivotValue = arr[left];
        for (int i = left + 1; i <= right; i++) {
            if (arr[i] < pivotValue) {
                pivotIndex++;
                swap(arr, pivotIndex, i);
            }
        }
        swap(arr, left, pivotIndex);
        return pivotIndex;
    }

    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    @Test
    public static void test(){
        char[] n={'L','F','Q','I','M','D'};
        quickSort(n,0,6);
    }
}
