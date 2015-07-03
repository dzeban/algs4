public class Percolation {
    // Grid size
    private int N;


    // Create N-by-N grid with all sites blocked
    public Percolation(int N) 
    {
        if (N <= 0)
            throw new IllegalArgumentException("N must be positive");

        this.N = N;
    }

    // Open site (row i, column j) if it is not open already
    public void open(int i , int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");
    }

    // Is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        return false;
    }

    // Is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        return false;
    }

    // Does the whole system percolates?
    public boolean percolates()
    {
        return false;
    }

    // Test client
    public static void main(String [] args)
    {
    }
}
