/*----------------------------------------------------------------
 *  Author:        Alex Dzyoba
 *  Written:       2015-07-08
 *  Last updated:  2015-09-13
 *
 *  Compilation:   javac-algs4 PercolationStats.java
 *  Execution:     java-algs4 PercolationStats <N: grid size> <T: iterations>
 *
 *  Estimate percolation threshold using Monte Carlo simulation.
 *
 *  Percolation threshold is a value p* such when p < p* system almost never
 *  percolates, and when p > p* it almost always percolates, where p is a
 *  probability of site being open in a random grid NxN.
 *
 *  Simulation makes T iterations and computes mean, standart deviation and a
 *  confidence interval.
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int N;
    private int T;

    /** Perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T) 
    {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N and T must be at least 1");

        this.N = N;
        this.T = T;

        results = new double[T];
        for (int i = 0; i < T; i++)
            results[i] = doExperiment(N);
    }

	/** Single experiment iteration */
    private double doExperiment(int N)
    {
        int i, j;
        int openSites = 0;
        Percolation pc = new Percolation(N);

        do {
            i = StdRandom.uniform(1, N + 1);
            j = StdRandom.uniform(1, N + 1);
            if (pc.isOpen(i, j))
                continue;

            pc.open(i, j);
            openSites++;            
        } while(!pc.percolates());

        return (double)openSites / (N * N);
    }

    /** Sample mean of percolation threshold */
    public double mean() { return StdStats.mean(results); }

    /** Sample standard deviation of percolation threshold */
    public double stddev() { return StdStats.stddev(results); }

    /** Low  endpoint of 95% confidence interval */
    public double confidenceLo() { return mean() - 1.96 * stddev() / Math.sqrt(T); }

    /** High endpoint of 95% confidence interval */
    public double confidenceHi() { return mean() + 1.96 * stddev() / Math.sqrt(T); }

    /** Test client */
    public static void main(String[] args)
    {
        if (args.length != 2) {
            StdOut.println("Requires 2 arguments <N> (grid size) and <T> (iterations)");
            System.exit(1);
        }

        // Read 2 arguments - N and T.
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        // Perform T independent computation experiments 
        // on N-by-N grid
        PercolationStats pcs = new PercolationStats(N, T);
        
        // Print out the mean, standart deviation and 95% confidence interval
        // for the percolation threshold.
        StdOut.printf("mean\t= %f\n", pcs.mean());
        StdOut.printf("stddev\t= %f\n", pcs.stddev());
        StdOut.printf("95%% confidence interval\t= %f %f\n", 
                pcs.confidenceLo(), pcs.confidenceHi());

    }
}
