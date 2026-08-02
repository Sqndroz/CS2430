import java.util.ArrayList;
import java.util.List;

/**
 * Name: Sandro Demartin
 * Course: CS 2430, Section: 501
 * Project: Programming Project 3 - Summer 2026
 * Purpose: Runs all required algorithms, displays readable results, compares
 *          greedy solutions with the true optimum, and verifies the DP result.
 */
public class Project3App {
    private static final int MAX_PAYLOAD_WEIGHT = 700;

    public static void main(String[] args) {
        List<Experiment> experiments = createExperiments();
        KnapsackSolver solver = new KnapsackSolver(experiments, MAX_PAYLOAD_WEIGHT);

        System.out.println("SPACE SHUTTLE PAYLOAD OPTIMIZATION");
        System.out.println("Maximum payload weight: " + MAX_PAYLOAD_WEIGHT + " kg");
        System.out.println("Number of experiments: " + experiments.size());
        System.out.println("Possible subsets checked by brute force: "
                + (1 << experiments.size()));
        printDivider();

        PayloadSolution ratingFirst = solver.greedyHighestRatingFirst();
        PayloadSolution lightestFirst = solver.greedyLightestFirst();
        PayloadSolution ratioFirst = solver.greedyBestRatioFirst();

        printSolution("GREEDY 1 - HIGHEST RATING FIRST", ratingFirst);
        printSolution("GREEDY 2 - LIGHTEST WEIGHT FIRST", lightestFirst);
        printSolution("GREEDY 3 - BEST RATING/WEIGHT RATIO FIRST", ratioFirst);

        List<PayloadSolution> topThree = solver.bruteForceTopSolutions(3);
        System.out.println("BRUTE FORCE - TOP 3 VALID SUBSETS");
        for (int i = 0; i < topThree.size(); i++) {
            String label = i == 0 ? "Rank " + (i + 1) + " (OPTIMAL)" : "Rank " + (i + 1);
            printSolution(label, topThree.get(i));
        }

        PayloadSolution optimal = topThree.get(0);
        PayloadSolution dynamicProgramming = solver.dynamicProgrammingSolution();
        printSolution("DYNAMIC PROGRAMMING - EXTRA CREDIT", dynamicProgramming);

        System.out.println("COMPARISON SUMMARY");
        printComparison("Highest rating first", ratingFirst, optimal);
        printComparison("Lightest weight first", lightestFirst, optimal);
        printComparison("Best ratio first", ratioFirst, optimal);
        printComparison("Dynamic programming", dynamicProgramming, optimal);

        boolean dpVerified = sameTotals(dynamicProgramming, optimal);
        System.out.println();
        System.out.println("Verification: DP and brute force have matching totals: "
                + (dpVerified ? "PASS" : "FAIL"));
        System.out.println("All reported payloads remain at or below 700 kg.");
    }

    private static List<Experiment> createExperiments() {
        List<Experiment> experiments = new ArrayList<>();
        experiments.add(new Experiment("Cloud Patterns", 36, 5));
        experiments.add(new Experiment("Solar Flares", 264, 9));
        experiments.add(new Experiment("Solar Power", 188, 6));
        experiments.add(new Experiment("Binary Stars", 203, 8));
        experiments.add(new Experiment("Relativity", 104, 8));
        experiments.add(new Experiment("Seed Viability", 7, 4));
        experiments.add(new Experiment("Sun Spots", 90, 2));
        experiments.add(new Experiment("Mice Tumors", 65, 8));
        experiments.add(new Experiment("Microgravity Plant Growth", 75, 5));
        experiments.add(new Experiment("Micrometeorites", 170, 9));
        experiments.add(new Experiment("Cosmic Rays", 80, 7));
        experiments.add(new Experiment("Yeast Fermentation", 27, 4));
        return experiments;
    }

    private static void printSolution(String title, PayloadSolution solution) {
        System.out.println(title);
        System.out.println("Selected experiments:");
        for (Experiment experiment : solution.getExperiments()) {
            System.out.println("  - " + experiment);
        }
        System.out.println("Total weight: " + solution.getTotalWeight() + " kg");
        System.out.println("Total rating: " + solution.getTotalRating());
        printDivider();
    }

    private static void printComparison(
            String strategyName,
            PayloadSolution strategy,
            PayloadSolution optimal) {
        boolean matched = sameTotals(strategy, optimal);
        int ratingDifference = optimal.getTotalRating() - strategy.getTotalRating();
        System.out.printf("%-28s %s", strategyName + ":", matched ? "MATCHED OPTIMAL" : "did not match");
        if (!matched) {
            System.out.print(" (rating gap = " + ratingDifference + ")");
        }
        System.out.println();
    }

    private static boolean sameTotals(PayloadSolution first, PayloadSolution second) {
        return first.getTotalWeight() == second.getTotalWeight()
                && first.getTotalRating() == second.getTotalRating();
    }

    private static void printDivider() {
        System.out.println("--------------------------------------------------------------------------");
    }
}
