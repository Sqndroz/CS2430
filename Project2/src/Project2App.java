/*
 * Name: Sandro Demartin
 * Course and Section: CS 2430, Section 501
 * Project: Programming Project 2 - Summer 2026
 * File Purpose: Runs and displays ordinary-set and multiset verification cases.
 */

import java.util.Map;

/** Main application for CS 2430 Programming Project 2. */
public class Project2App {

    private static final String[] UNIVERSE = {
        "Apple", "Banana", "Cherry", "Date", "Fig",
        "Grape", "Honeydew", "Kiwi", "Lemon", "Mango"
    };

    public static void main(String[] args) {
        System.out.println("CS 2430 PROGRAMMING PROJECT 2");
        System.out.println("SETS AND MULTISETS");
        System.out.println("Universal set: {" + String.join(", ", UNIVERSE) + "}");

        runOrdinarySetCases();
        runMultisetCases();
    }

    private static void runOrdinarySetCases() {
        System.out.println("\n============================================================");
        System.out.println("PART 1 - ORDINARY SET OPERATIONS");
        System.out.println("============================================================");

        // Representative case 1: overlapping sets.
        displayOrdinaryCase(
                "Case 1: Overlapping sets",
                new boolean[] {true, false, true, false, true, false, false, true, false, false},
                new boolean[] {false, true, true, false, false, true, false, true, false, true});

        // Representative case 2: disjoint sets.
        displayOrdinaryCase(
                "Case 2: Disjoint sets",
                new boolean[] {true, true, false, false, false, false, false, false, false, false},
                new boolean[] {false, false, true, true, false, false, false, false, false, false});

        // Edge case: A is empty and B is the universal set.
        displayOrdinaryCase(
                "Case 3 (edge): Empty A and universal B",
                new boolean[] {false, false, false, false, false, false, false, false, false, false},
                new boolean[] {true, true, true, true, true, true, true, true, true, true});
    }

    private static void displayOrdinaryCase(String label, boolean[] a, boolean[] b) {
        System.out.println("\n" + label);
        printOrdinaryResult("A", a);
        printOrdinaryResult("B", b);
        printOrdinaryResult("NOT(A)", OrdinarySetOperations.complement(a));
        printOrdinaryResult("A UNION B", OrdinarySetOperations.union(a, b));
        printOrdinaryResult("A INTERSECTION B", OrdinarySetOperations.intersection(a, b));
        printOrdinaryResult("A - B", OrdinarySetOperations.difference(a, b));
        printOrdinaryResult("A XOR B", OrdinarySetOperations.symmetricDifference(a, b));
    }

    private static void printOrdinaryResult(String label, boolean[] set) {
        System.out.printf("%-18s Bits: %s  Elements: %s%n",
                label,
                OrdinarySetOperations.toBitString(set),
                OrdinarySetOperations.toElementListing(set, UNIVERSE));
    }

    private static void runMultisetCases() {
        System.out.println("\n============================================================");
        System.out.println("PART 2 - MULTISET OPERATIONS");
        System.out.println("============================================================");

        // Both A and B contain at least two elements with multiplicity greater than one.
        Map<String, Integer> a1 = MultisetOperations.create(
                "Apple", 3, "Banana", 1, "Cherry", 2, "Fig", 4, "Kiwi", 1);
        Map<String, Integer> b1 = MultisetOperations.create(
                "Apple", 1, "Cherry", 5, "Date", 2, "Fig", 2, "Mango", 3);
        displayMultisetCase("Case 1: Overlapping multisets", a1, b1);

        Map<String, Integer> a2 = MultisetOperations.create(
                "Banana", 2, "Grape", 3, "Lemon", 2);
        Map<String, Integer> b2 = MultisetOperations.create(
                "Apple", 2, "Cherry", 4, "Mango", 2);
        displayMultisetCase("Case 2: Disjoint multisets", a2, b2);

        // Edge case: subtracting a larger multiplicity must stop at zero.
        Map<String, Integer> a3 = MultisetOperations.create(
                "Apple", 2, "Cherry", 1, "Date", 3);
        Map<String, Integer> b3 = MultisetOperations.create(
                "Apple", 5, "Cherry", 1, "Date", 1, "Kiwi", 2);
        displayMultisetCase("Case 3 (edge): Difference cannot become negative", a3, b3);
    }

    private static void displayMultisetCase(
            String label, Map<String, Integer> a, Map<String, Integer> b) {
        System.out.println("\n" + label);
        System.out.println("A:                    " + MultisetOperations.toListing(a));
        System.out.println("B:                    " + MultisetOperations.toListing(b));
        System.out.println("MULTISET UNION:       "
                + MultisetOperations.toListing(MultisetOperations.union(a, b, UNIVERSE)));
        System.out.println("MULTISET INTERSECTION:"
                + " " + MultisetOperations.toListing(
                        MultisetOperations.intersection(a, b, UNIVERSE)));
        System.out.println("MULTISET DIFFERENCE:  "
                + MultisetOperations.toListing(MultisetOperations.difference(a, b, UNIVERSE)));
        System.out.println("MULTISET SUM:         "
                + MultisetOperations.toListing(MultisetOperations.sum(a, b, UNIVERSE)));
    }
}
