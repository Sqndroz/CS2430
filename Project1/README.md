# CS 2430 Programming Project 1 - Sorting Comparison

Name: Sandro Demartin  
Course: CS 2430
Semester: Summer 2026

## Project Description

This project compares four sorting algorithms: merge sort, quick sort, heap sort, and shaker sort. The program generates fixed integer arrays of sizes 4, 6, and 8, sorts each array with all four algorithms, and counts the number of direct comparisons between array values.

## Files

- `SortingDriver.java` - main program and test driver
- `DataGenerator.java` - creates the test arrays
- `SortResult.java` - stores the sorted array and comparison count
- `MergeSort.java` - merge sort implementation
- `QuickSort.java` - quick sort implementation
- `HeapSort.java` - heap sort implementation
- `ShakerSort.java` - shaker/cocktail sort implementation

## Build Instructions

Open a terminal in the `project1/src` folder and compile all Java files:

Java 21

## Run Instructions

After compiling, run the driver:

```bash
java SortingDriver
```

## Notes

The arrays are fixed instead of random so that the comparison counts are reproducible. The comparison rule is to count each direct comparison between two array values, such as `array[i] <= pivot` or `leftArray[i] <= rightArray[j]`.
