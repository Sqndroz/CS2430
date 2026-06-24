/*
 * Sandro Demartin
 * CS 2430, Section ___
 * Programming Project 1 - Summer 2026
 *
 * This file stores the result of a sorting algorithm. It keeps the sorted
 * array and the number of comparisons made during the sort.
 */

/**
 * Stores the sorted output and comparison count from one sorting algorithm.
 */
public class SortResult {
    private int[] sortedArray;
    private int comparisons;

    /**
     * Creates a new sorting result.
     *
     * @param sortedArray the sorted array produced by the algorithm
     * @param comparisons the number of value comparisons counted
     */
    public SortResult(int[] sortedArray, int comparisons) {
        this.sortedArray = sortedArray;
        this.comparisons = comparisons;
    }

    /**
     * Returns the sorted array.
     *
     * @return sorted integer array
     */
    public int[] getSortedArray() {
        return sortedArray;
    }

    /**
     * Returns the number of comparisons used by the algorithm.
     *
     * @return comparison count
     */
    public int getComparisons() {
        return comparisons;
    }
}
