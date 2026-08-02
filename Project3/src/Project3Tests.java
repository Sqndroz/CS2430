import java.util.ArrayList;
import java.util.List;

/**
 * Name: Sandro Demartin
 * Course: CS 2430, Section: 501
 * Project: Programming Project 3 - Summer 2026
 * Purpose: Provides lightweight verification tests without external libraries.
 */
public class Project3Tests {
    public static void main(String[] args) {
        List<Experiment> experiments = createExperiments();
        KnapsackSolver solver = new KnapsackSolver(experiments, 700);

        PayloadSolution ratingFirst = solver.greedyHighestRatingFirst();
        PayloadSolution lightestFirst = solver.greedyLightestFirst();
        PayloadSolution ratioFirst = solver.greedyBestRatioFirst();
        PayloadSolution bruteForce = solver.bruteForceTopSolutions(3).get(0);
        PayloadSolution dynamicProgramming = solver.dynamicProgrammingSolution();

        check(ratingFirst.getTotalWeight() == 690, "Rating-first weight should be 690");
        check(ratingFirst.getTotalRating() == 45, "Rating-first rating should be 45");
        check(lightestFirst.getTotalWeight() == 654, "Lightest-first weight should be 654");
        check(lightestFirst.getTotalRating() == 52, "Lightest-first rating should be 52");
        check(ratioFirst.getTotalWeight() == 654, "Ratio-first weight should be 654");
        check(ratioFirst.getTotalRating() == 52, "Ratio-first rating should be 52");
        check(bruteForce.getTotalWeight() == 692, "Optimal weight should be 692");
        check(bruteForce.getTotalRating() == 53, "Optimal rating should be 53");
        check(dynamicProgramming.getTotalRating() == bruteForce.getTotalRating(),
                "DP rating should match brute force");
        check(dynamicProgramming.getTotalWeight() == bruteForce.getTotalWeight(),
                "DP weight should match brute force");

        for (PayloadSolution solution : List.of(
                ratingFirst, lightestFirst, ratioFirst, bruteForce, dynamicProgramming)) {
            check(solution.getTotalWeight() <= 700, "Every solution must satisfy capacity");
        }

        System.out.println("All Project 3 tests passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
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
}
