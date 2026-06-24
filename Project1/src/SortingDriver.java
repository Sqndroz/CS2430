/*
 * Sandro Demartin
 * CS 2430, Section ___
 * Programming Project 1 - Summer 2026
 *
 * This file contains the main driver for the sorting comparison project. It
 * generates the test arrays, runs the four sorting algorithms, and prints the
 * comparison-count results.
 */

import java.util.Arrays;

/**
 * Runs the sorting comparison experiment.
 */
public class SortingDriver {

    /**
     * Program starting point.
     *
     * @param args command-line arguments, not used in this project
     */
    public static void main(String[] args) {
        int[] sizes = {4, 6, 8};
        String[] algorithmNames = {"Merge Sort", "Quick Sort", "Heap Sort", "Shaker Sort"};
        int[][] comparisonTable = new int[sizes.length][algorithmNames.length];

        System.out.println("CS 2430 Programming Project 1 - Sorting Comparison");
        System.out.println("Comparison rule: count each direct comparison between array values.\n");

        for (int row = 0; row < sizes.length; row++) {
            int size = sizes[row];
            int[] input = DataGenerator.generateArray(size);

            System.out.println("==================================================");
            System.out.println("n = " + size);
            System.out.println("Input: " + Arrays.toString(input));
            System.out.println("==================================================");

            SortResult mergeResult = new MergeSort().sort(input);
            SortResult quickResult = new QuickSort().sort(input);
            SortResult heapResult = new HeapSort().sort(input);
            SortResult shakerResult = new ShakerSort().sort(input);

            SortResult[] results = {mergeResult, quickResult, heapResult, shakerResult};

            for (int col = 0; col < results.length; col++) {
                comparisonTable[row][col] = results[col].getComparisons();
                printRun(algorithmNames[col], size, input, results[col]);
            }
        }

        printResultsTable(sizes, algorithmNames, comparisonTable);
        runEdgeCaseCheck();
    }

    /**
     * Prints the result from one algorithm run.
     */
    private static void printRun(String algorithmName, int size, int[] input, SortResult result) {
        System.out.println("Algorithm: " + algorithmName);
        System.out.println("n value: " + size);
        System.out.println("Input: " + Arrays.toString(input));
        System.out.println("Sorted output: " + Arrays.toString(result.getSortedArray()));
        System.out.println("Comparisons: " + result.getComparisons());
        System.out.println();
    }

    /**
     * Prints a compact table of comparison counts.
     */
    private static void printResultsTable(int[] sizes, String[] algorithmNames, int[][] comparisonTable) {
        System.out.println("Results Table - Comparison Counts");
        System.out.printf("%-8s", "n");
        for (String name : algorithmNames) {
            System.out.printf("%-15s", name);
        }
        System.out.println();

        for (int row = 0; row < sizes.length; row++) {
            System.out.printf("%-8d", sizes[row]);
            for (int col = 0; col < algorithmNames.length; col++) {
                System.out.printf("%-15d", comparisonTable[row][col]);
            }
            System.out.println();
        }
    }

    /**
     * Runs one duplicate-value edge case to show that repeated values sort correctly.
     */
    private static void runEdgeCaseCheck() {
        int[] duplicateInput = DataGenerator.generateDuplicateArray();
        SortResult result = new MergeSort().sort(duplicateInput);

        System.out.println("\nEdge Case Check - Duplicate Values");
        System.out.println("Input: " + Arrays.toString(duplicateInput));
        System.out.println("Sorted output: " + Arrays.toString(result.getSortedArray()));
        System.out.println("Comparisons: " + result.getComparisons());
    }
}
