import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Name: Sandro Demartin
 * Course: CS 2430, Section: 501
 * Project: Programming Project 3 - Summer 2026
 * Purpose: Implements three greedy strategies, exhaustive search, and an
 *          extra-credit dynamic programming solution for the 0/1 knapsack.
 */
public class KnapsackSolver {
    private final List<Experiment> experiments;
    private final int capacity;

    public KnapsackSolver(List<Experiment> experiments, int capacity) {
        this.experiments = new ArrayList<>(experiments);
        this.capacity = capacity;
    }

    public PayloadSolution greedyHighestRatingFirst() {
        Comparator<Experiment> comparator = Comparator
                .comparingInt(Experiment::getRating).reversed()
                .thenComparingInt(Experiment::getWeight)
                .thenComparing(Experiment::getName);
        return buildGreedySolution(comparator);
    }

    public PayloadSolution greedyLightestFirst() {
        Comparator<Experiment> comparator = Comparator
                .comparingInt(Experiment::getWeight)
                .thenComparing(Comparator.comparingInt(Experiment::getRating).reversed())
                .thenComparing(Experiment::getName);
        return buildGreedySolution(comparator);
    }

    public PayloadSolution greedyBestRatioFirst() {
        Comparator<Experiment> comparator = Comparator
                .comparingDouble(Experiment::getRatingToWeightRatio).reversed()
                .thenComparing(Comparator.comparingInt(Experiment::getRating).reversed())
                .thenComparingInt(Experiment::getWeight)
                .thenComparing(Experiment::getName);
        return buildGreedySolution(comparator);
    }

    private PayloadSolution buildGreedySolution(Comparator<Experiment> comparator) {
        List<Experiment> sorted = new ArrayList<>(experiments);
        sorted.sort(comparator);

        List<Experiment> selected = new ArrayList<>();
        int currentWeight = 0;

        for (Experiment experiment : sorted) {
            if (currentWeight + experiment.getWeight() <= capacity) {
                selected.add(experiment);
                currentWeight += experiment.getWeight();
            }
        }

        return new PayloadSolution(selected);
    }

    /**
     * Evaluates every subset. With 12 experiments, 2^12 = 4096 subsets.
     */
    public List<PayloadSolution> bruteForceTopSolutions(int count) {
        List<PayloadSolution> validSolutions = new ArrayList<>();
        int subsetCount = 1 << experiments.size();

        for (int mask = 0; mask < subsetCount; mask++) {
            List<Experiment> selected = new ArrayList<>();
            int totalWeight = 0;

            for (int index = 0; index < experiments.size(); index++) {
                if ((mask & (1 << index)) != 0) {
                    Experiment experiment = experiments.get(index);
                    totalWeight += experiment.getWeight();
                    if (totalWeight > capacity) {
                        break;
                    }
                    selected.add(experiment);
                }
            }

            if (totalWeight <= capacity) {
                validSolutions.add(new PayloadSolution(selected));
            }
        }

        validSolutions.sort(null);
        return new ArrayList<>(validSolutions.subList(0, Math.min(count, validSolutions.size())));
    }

    /**
     * Extra-credit dynamic programming solution.
     * dp[i][w] stores the highest rating obtainable using the first i items
     * with a weight limit of w. The boolean table records inclusion decisions
     * so the selected experiments can be reconstructed after the table is built.
     */
    public PayloadSolution dynamicProgrammingSolution() {
        int itemCount = experiments.size();
        int[][] dp = new int[itemCount + 1][capacity + 1];
        boolean[][] take = new boolean[itemCount + 1][capacity + 1];

        for (int i = 1; i <= itemCount; i++) {
            Experiment experiment = experiments.get(i - 1);
            for (int w = 0; w <= capacity; w++) {
                dp[i][w] = dp[i - 1][w];

                if (experiment.getWeight() <= w) {
                    int includeRating = experiment.getRating()
                            + dp[i - 1][w - experiment.getWeight()];
                    if (includeRating > dp[i][w]) {
                        dp[i][w] = includeRating;
                        take[i][w] = true;
                    }
                }
            }
        }

        List<Experiment> selected = new ArrayList<>();
        int remainingWeight = capacity;
        for (int i = itemCount; i >= 1; i--) {
            if (take[i][remainingWeight]) {
                Experiment experiment = experiments.get(i - 1);
                selected.add(0, experiment);
                remainingWeight -= experiment.getWeight();
            }
        }

        return new PayloadSolution(selected);
    }
}
