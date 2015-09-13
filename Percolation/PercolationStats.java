import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int N;
    private int T;

    // Perform T independent experiments on an N-by-N grid
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

        //StdOut.printf("openSites %d, N2 %d, res %f\n",
                //openSites, N*N, (double)openSites/(N*N));
        return (double)openSites / (N * N);
    }

    // sample mean of percolation threshold
    public double mean() 
    {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() 
    {
        return StdStats.stddev(results);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    // test client
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
