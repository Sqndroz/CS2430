/*
 * Sandro Demartin
 * CS 2430, Section ___
 * Programming Project 1 - Summer 2026
 *
 * This file implements shaker sort, also called cocktail sort, and counts
 * data comparisons.
 */

import java.util.Arrays;

/**
 * Sorts an array using shaker sort.
 */
public class ShakerSort {

    /**
     * Sorts a copy of the input array using shaker sort.
     *
     * @param input original array
     * @return sorted array and comparison count
     */
    public SortResult sort(int[] input) {
        int[] copy = Arrays.copyOf(input, input.length);
        int comparisons = 0;
        boolean swapped = true;
        int start = 0;
        int end = copy.length - 1;

        while (swapped) {
            swapped = false;

            for (int i = start; i < end; i++) {
                comparisons++;
                if (copy[i] > copy[i + 1]) {
                    swap(copy, i, i + 1);
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }

            swapped = false;
            end--;

            for (int i = end - 1; i >= start; i--) {
                comparisons++;
                if (copy[i] > copy[i + 1]) {
                    swap(copy, i, i + 1);
                    swapped = true;
                }
            }

            start++;
        }

        return new SortResult(copy, comparisons);
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
