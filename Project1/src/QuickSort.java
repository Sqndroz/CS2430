/*
 * Sandro Demartin
 * CS 2430, Section 501
 * Programming Project 1 - Summer 2026
 *
 * This file implements quick sort and counts data comparisons.
 */

import java.util.Arrays;

/**
 * Sorts an array using quick sort.
 */
public class QuickSort {
    private int comparisons;

    /**
     * Sorts a copy of the input array using quick sort.
     *
     * @param input original array
     * @return sorted array and comparison count
     */
    public SortResult sort(int[] input) {
        int[] copy = Arrays.copyOf(input, input.length);
        comparisons = 0;
        quickSort(copy, 0, copy.length - 1);
        return new SortResult(copy, comparisons);
    }

    /**
     * Recursively sorts the section between low and high.
     */
    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    /**
     * Partitions the array around the last value as the pivot.
     */
    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int smallerIndex = low - 1;

        for (int current = low; current < high; current++) {
            comparisons++;
            if (array[current] <= pivot) {
                smallerIndex++;
                swap(array, smallerIndex, current);
            }
        }

        swap(array, smallerIndex + 1, high);
        return smallerIndex + 1;
    }

    /**
     * Swaps two values in the array.
     */
    private void swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }
}
