/*
 * Name: Sandro Demartin
 * Course and Section: CS 2430, Section 501
 * Project: Programming Project 2 - Summer 2026
 * File Purpose: Implements ordinary set operations using Boolean arrays.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Provides standard operations on ordinary sets represented by Boolean arrays.
 * Each array position corresponds to an element at the same index in a universal set.
 */
public final class OrdinarySetOperations {

    private OrdinarySetOperations() {
        // Utility class: prevent object creation.
    }

    /** Returns the complement of set A with respect to the universal set. */
    public static boolean[] complement(boolean[] a) {
        boolean[] result = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = !a[i];
        }
        return result;
    }

    /** Returns the union of A and B. */
    public static boolean[] union(boolean[] a, boolean[] b) {
        validateSameLength(a, b);
        boolean[] result = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] || b[i];
        }
        return result;
    }

    /** Returns the intersection of A and B. */
    public static boolean[] intersection(boolean[] a, boolean[] b) {
        validateSameLength(a, b);
        boolean[] result = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] && b[i];
        }
        return result;
    }

    /** Returns the set difference A - B. */
    public static boolean[] difference(boolean[] a, boolean[] b) {
        validateSameLength(a, b);
        boolean[] result = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] && !b[i];
        }
        return result;
    }

    /** Returns the symmetric difference A XOR B. */
    public static boolean[] symmetricDifference(boolean[] a, boolean[] b) {
        validateSameLength(a, b);
        boolean[] result = new boolean[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    /** Converts a Boolean set representation into a compact bit string. */
    public static String toBitString(boolean[] set) {
        StringBuilder bits = new StringBuilder(set.length);
        for (boolean present : set) {
            bits.append(present ? '1' : '0');
        }
        return bits.toString();
    }

    /** Converts a Boolean set representation into a readable element listing. */
    public static String toElementListing(boolean[] set, String[] universe) {
        validateUniverse(set, universe);
        List<String> elements = new ArrayList<>();
        for (int i = 0; i < set.length; i++) {
            if (set[i]) {
                elements.add(universe[i]);
            }
        }
        return "{" + String.join(", ", elements) + "}";
    }

    private static void validateSameLength(boolean[] a, boolean[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Set arrays cannot be null.");
        }
        if (a.length != b.length) {
            throw new IllegalArgumentException("Set arrays must have the same length.");
        }
    }

    private static void validateUniverse(boolean[] set, String[] universe) {
        if (set == null || universe == null || set.length != universe.length) {
            throw new IllegalArgumentException(
                    "The set and universal-set arrays must be non-null and have equal lengths.");
        }
    }
}
