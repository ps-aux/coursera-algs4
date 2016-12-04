import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by arkonix on 12/4/16.
 */
public class PercolationStats {

    private final int n;
    private final double[] results;
    private final double mean;
    private double stdDev;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1)
            throw new IllegalArgumentException();

        this.n = n;
        this.results = new double[trials];

        for (int i = 0; i < trials; i++) {
            results[i] = doTrial(n);
        }

        mean = StdStats.mean(results);
        stdDev = StdStats.stddev(results);
    }

    private static double doTrial(int n) {
        Percolation p = new Percolation(n);

        int size = n * n;
        while (!p.percolates()) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            p.open(row, col);
        }

        double res = calculateOpenSites(p, n);

        return res / size;
    }

    private static int calculateOpenSites(Percolation p, int n) {
        int count = 0;
        for (int row = 1; row <= n; row++) {
            for (int col = 1; col <= n; col++) {
                if (p.isOpen(row, col))
                    count++;
            }
        }

        return count;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stdDev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - (1.96 * Math.sqrt(stdDev) / Math.sqrt(n));

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + (1.96 * Math.sqrt(stdDev) / Math.sqrt(n));
    }

    // test client (described below){
    public static void main(String[] args) {
        int n = 200;
        int trials = 100;
        PercolationStats s = new PercolationStats(n, trials);
        System.out.println("mean = " + s.mean());
        System.out.println("stddev = " + s.stddev());
        System.out.println("95% confidence interval = "
                + s.confidenceLo() + ", " + s.confidenceHi());
    }
}

