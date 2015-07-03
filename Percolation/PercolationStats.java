public class PercolationStats {

    // Perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) 
    {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N and T must be at least 1");
    }

    // sample mean of percolation threshold
    public double mean() 
    {
    }

    // sample standard deviation of percolation threshold
    public double stddev() 
    {
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo()
    {
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
    }

    // test client (described below)
    public static void main(String[] args)
    {
        // Read 2 arguments - N and T.

        // Perform T independent computation experiments 
        // on N-by-N grid
        
        // Print out the mean, standart deviation and 95% confidence interval
        // for the percolation threshold.

    }
}
