/**
 * Name: Sandro Demartin
 * Course: CS 2430, Section: 501
 * Project: Programming Project 3 - Summer 2026
 * Purpose: Represents one indivisible science experiment with a name, weight,
 *          and rating for the shuttle payload optimization problem.
 */
public class Experiment {
    private final String name;
    private final int weight;
    private final int rating;

    public Experiment(String name, int weight, int rating) {
        this.name = name;
        this.weight = weight;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public int getRating() {
        return rating;
    }

    public double getRatingToWeightRatio() {
        return (double) rating / weight;
    }

    @Override
    public String toString() {
        return String.format("%-27s weight=%3d kg, rating=%2d, ratio=%.4f",
                name, weight, rating, getRatingToWeightRatio());
    }
}
