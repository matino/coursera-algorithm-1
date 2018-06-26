import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final int trials;
    private final int gridSize;
    private final double mean;
    private double stddev;

    public PercolationStats(int gridSize, int trials) {
        if (gridSize <= 0)
            throw new java.lang.IllegalArgumentException("Grid size must be greater than 0");
        if (trials <= 0)
            throw new java.lang.IllegalArgumentException("Trials must be greater than 0");
        this.gridSize = gridSize;
        this.trials = trials;
        double[] results = new double[trials];

        for (int i = 0; i < trials; i++) {
            results[i] = compute();
        }
        this.mean = StdStats.mean(results);
        if (this.trials == 1)
            this.stddev = Double.NaN;
        else
            this.stddev = StdStats.stddev(results);
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.mean - CONFIDENCE_95 * stddev() / Math.sqrt(this.trials);
    }

    public double confidenceHi() {
        return this.mean + CONFIDENCE_95 * stddev() / Math.sqrt(this.trials);
    }

    private double compute() {
        Percolation perc = new Percolation(this.gridSize);
        int i, j;
        while (!perc.percolates()) {
            do {
                i = StdRandom.uniform(this.gridSize) + 1;
                j = StdRandom.uniform(this.gridSize) + 1;
            } while (perc.isOpen(i, j));
            perc.open(i, j);
        }
        return perc.numberOfOpenSites() / (Math.pow(this.gridSize, 2));
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean = %f\n", stats.mean());
        StdOut.printf("stddev = %f\n", stats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", stats.confidenceLo(), stats.confidenceHi());
    }

}
