/*
 * Sandro Demartin
 * CS 2430, Section ___
 * Programming Project 1 - Summer 2026
 *
 * This file creates the integer arrays used for testing the sorting algorithms.
 */

/**
 * Generates fixed test arrays for the sorting experiment.
 */
public class DataGenerator {

    /**
     * Returns a fixed integer array for the requested size.
     * Fixed arrays make the results easy to reproduce and compare.
     *
     * @param size requested array size
     * @return integer array with the requested size
     */
    public static int[] generateArray(int size) {
        if (size == 4) {
            return new int[] {7, 2, 9, 1};
        } else if (size == 6) {
            return new int[] {14, 3, 9, 1, 20, 8};
        } else if (size == 8) {
            return new int[] {12, 4, 18, 1, 7, 15, 3, 10};
        } else {
            throw new IllegalArgumentException("This project only supports sizes 4, 6, and 8.");
        }
    }

    /**
     * Returns a small edge-case array with duplicate values.
     * This is used to verify that sorting still works when values repeat.
     *
     * @return array containing duplicate values
     */
    public static int[] generateDuplicateArray() {
        return new int[] {5, 1, 5, 3};
    }
}
