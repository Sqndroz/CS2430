/*
 * Sandro Demartin
 * CS 2430, Section ___
 * Programming Project 1 - Summer 2026
 *
 * This file implements merge sort and counts data comparisons.
 */

import java.util.Arrays;

/**
 * Sorts an array using merge sort.
 */
public class MergeSort {
    private int comparisons;

    /**
     * Sorts a copy of the input array using merge sort.
     *
     * @param input original array
     * @return sorted array and comparison count
     */
    public SortResult sort(int[] input) {
        int[] copy = Arrays.copyOf(input, input.length);
        comparisons = 0;
        mergeSort(copy, 0, copy.length - 1);
        return new SortResult(copy, comparisons);
    }

    /**
     * Recursively divides the array and then merges the sorted halves.
     */
    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    /**
     * Merges two sorted sections of the array.
     */
    private void merge(int[] array, int left, int middle, int right) {
        int[] leftArray = Arrays.copyOfRange(array, left, middle + 1);
        int[] rightArray = Arrays.copyOfRange(array, middle + 1, right + 1);

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftArray.length && j < rightArray.length) {
            comparisons++;
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < leftArray.length) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < rightArray.length) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }
}
