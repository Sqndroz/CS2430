import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Name: Sandro Demartin
 * Course: CS 2430, Section: 501
 * Project: Programming Project 3 - Summer 2026
 * Purpose: Stores a selected subset of experiments and its calculated totals.
 */
public class PayloadSolution implements Comparable<PayloadSolution> {
    private final List<Experiment> experiments;
    private final int totalWeight;
    private final int totalRating;

    public PayloadSolution(List<Experiment> experiments) {
        this.experiments = Collections.unmodifiableList(new ArrayList<>(experiments));
        this.totalWeight = experiments.stream().mapToInt(Experiment::getWeight).sum();
        this.totalRating = experiments.stream().mapToInt(Experiment::getRating).sum();
    }

    public List<Experiment> getExperiments() {
        return experiments;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public String experimentNames() {
        return experiments.stream()
                .map(Experiment::getName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");
    }

    /**
     * Sort higher ratings first. For equal ratings, prefer the lighter payload.
     * If both totals match, use the experiment-name list for deterministic output.
     */
    @Override
    public int compareTo(PayloadSolution other) {
        int ratingComparison = Integer.compare(other.totalRating, this.totalRating);
        if (ratingComparison != 0) {
            return ratingComparison;
        }

        int weightComparison = Integer.compare(this.totalWeight, other.totalWeight);
        if (weightComparison != 0) {
            return weightComparison;
        }

        return this.experimentNames().compareTo(other.experimentNames());
    }
}
