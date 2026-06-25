/*
 * Sandro Demartin
 * CS 2430, Section 501
 * Programming Project 1 - Summer 2026
 *
 * This file implements heap sort and counts data comparisons.
 */

import java.util.Arrays;

/**
 * Sorts an array using heap sort.
 */
public class HeapSort {
    private int comparisons;

    /**
     * Sorts a copy of the input array using heap sort.
     *
     * @param input original array
     * @return sorted array and comparison count
     */
    public SortResult sort(int[] input) {
        int[] copy = Arrays.copyOf(input, input.length);
        comparisons = 0;

        for (int i = copy.length / 2 - 1; i >= 0; i--) {
            heapify(copy, copy.length, i);
        }

        for (int i = copy.length - 1; i > 0; i--) {
            swap(copy, 0, i);
            heapify(copy, i, 0);
        }

        return new SortResult(copy, comparisons);
    }

    /**
     * Restores the max-heap property for a subtree.
     */
    private void heapify(int[] array, int heapSize, int rootIndex) {
        int largest = rootIndex;
        int leftChild = 2 * rootIndex + 1;
        int rightChild = 2 * rootIndex + 2;

        if (leftChild < heapSize) {
            comparisons++;
            if (array[leftChild] > array[largest]) {
                largest = leftChild;
            }
        }

        if (rightChild < heapSize) {
            comparisons++;
            if (array[rightChild] > array[largest]) {
                largest = rightChild;
            }
        }

        if (largest != rootIndex) {
            swap(array, rootIndex, largest);
            heapify(array, heapSize, largest);
        }
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
