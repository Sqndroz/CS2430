/*
 * Name: Sandro Demartin
 * Course and Section: CS 2430, Section 501
 * Project: Programming Project 2 - Summer 2026
 * File Purpose: Implements multiset operations using maps of element counts.
 */

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides multiset operations. A LinkedHashMap stores each element and its
 * multiplicity while preserving the universal-set display order.
 */
public final class MultisetOperations {

    private MultisetOperations() {
        // Utility class: prevent object creation.
    }

    /** Multiset union: stores the maximum count from A and B for each element. */
    public static Map<String, Integer> union(
            Map<String, Integer> a, Map<String, Integer> b, String[] universe) {
        return combine(a, b, universe, Operation.UNION);
    }

    /** Multiset intersection: stores the minimum count from A and B. */
    public static Map<String, Integer> intersection(
            Map<String, Integer> a, Map<String, Integer> b, String[] universe) {
        return combine(a, b, universe, Operation.INTERSECTION);
    }

    /** Multiset difference: subtracts B from A without allowing negative counts. */
    public static Map<String, Integer> difference(
            Map<String, Integer> a, Map<String, Integer> b, String[] universe) {
        return combine(a, b, universe, Operation.DIFFERENCE);
    }

    /** Multiset sum: adds the counts from A and B. */
    public static Map<String, Integer> sum(
            Map<String, Integer> a, Map<String, Integer> b, String[] universe) {
        return combine(a, b, universe, Operation.SUM);
    }

    /** Creates a multiset from alternating element/count pairs. */
    public static Map<String, Integer> create(Object... elementCountPairs) {
        if (elementCountPairs.length % 2 != 0) {
            throw new IllegalArgumentException("Elements and counts must be supplied in pairs.");
        }

        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 0; i < elementCountPairs.length; i += 2) {
            String element = (String) elementCountPairs[i];
            int count = (Integer) elementCountPairs[i + 1];
            if (count < 0) {
                throw new IllegalArgumentException("Multiplicity cannot be negative.");
            }
            if (count > 0) {
                result.put(element, count);
            }
        }
        return result;
    }

    /** Returns a readable listing such as {Apple x 3, Banana x 1}. */
    public static String toListing(Map<String, Integer> multiset) {
        if (multiset.isEmpty()) {
            return "{}";
        }

        StringBuilder output = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Integer> entry : multiset.entrySet()) {
            if (!first) {
                output.append(", ");
            }
            output.append(entry.getKey()).append(" x ").append(entry.getValue());
            first = false;
        }
        return output.append('}').toString();
    }

    private static Map<String, Integer> combine(
            Map<String, Integer> a,
            Map<String, Integer> b,
            String[] universe,
            Operation operation) {

        if (a == null || b == null || universe == null) {
            throw new IllegalArgumentException("Multisets and universe cannot be null.");
        }

        Map<String, Integer> result = new LinkedHashMap<>();
        for (String element : universe) {
            int countA = a.getOrDefault(element, 0);
            int countB = b.getOrDefault(element, 0);
            int resultCount;

            switch (operation) {
                case UNION:
                    resultCount = Math.max(countA, countB);
                    break;
                case INTERSECTION:
                    resultCount = Math.min(countA, countB);
                    break;
                case DIFFERENCE:
                    resultCount = Math.max(countA - countB, 0);
                    break;
                case SUM:
                    resultCount = countA + countB;
                    break;
                default:
                    throw new IllegalStateException("Unknown operation.");
            }

            // Zero-count elements are omitted because they are not members of the result.
            if (resultCount > 0) {
                result.put(element, resultCount);
            }
        }
        return result;
    }

    private enum Operation {
        UNION, INTERSECTION, DIFFERENCE, SUM
    }
}
