public class Percolation {
    // Grid size
    private int N;

    // Grid itself
    private Site[] grid;

    // Union-Find object to 
    // calculate percolation
    private WeightedQuickUnionUF unionFind;

    // Create N-by-N grid with all sites blocked
    public Percolation(int N) 
    {
        if (N <= 0)
            throw new IllegalArgumentException("N must be positive");

        this.N = N;

        // Initialize grid of sites
        grid = new Site[N * N];
        unionFind = new WeightedQuickUnionUF(N * N);
        int i = 1;
        int j = 1;
        for (int n = 0; n < N * N; n++) 
        {
            grid[n] = new Site(i, j, n);

            i = (i + 1) % (N + 1);

            // Next row
            if (i == 0) {
                i = 1;
                j++;
            }
        }
    }

    // Get site number in array as n = j*N + i, but
    // coordinates are 1 based, while numbers are 0-based
    private int coordinates2number(int i, int j)
    {
        return (j - 1) * N + (i - 1);
    }

    private Site getSite(int i, int j)
    {
        if (i < 1 || i > N || j < 1 || j > N)
            return null;

        int n = coordinates2number(i, j);
        return grid[n];
    }

    private Site getSite(int n)
    {
        if (n < 0 || n > N * N)
            return null;

        return grid[n];
    }

    // Open site (row i, column j) if it is not open already
    // i and j is "1" based
    public void open(int i , int j)
    {
        if (i < 1 || i > N || j < 1 || j > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        site.open();

        // Connect to neighbors
        site.connectTo(getSite(i-1, j), unionFind);
        site.connectTo(getSite(i+1, j), unionFind);
        site.connectTo(getSite(i, j-1), unionFind);
        site.connectTo(getSite(i, j+1), unionFind);
    }

    // Is site (row i, column j) open?
    public boolean isOpen(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        return site.getOpen();
    }

    private boolean checkFull(Site site)
    {
        Site topSite;

        for (int column = 1; column <= N; column++)
        {
            topSite = getSite(1, column);
            if (unionFind.connected(site.getNumber(), topSite.getNumber()))
                return true;
        }

        return false;
    }

    // Is site (row i, column j) full?
    public boolean isFull(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        return checkFull(site);
    }

    public boolean isFull(int number)
    {
        if (number < 0 || number > N*N)
            throw new IllegalArgumentException("Site number must be (0..N*N)");

        Site site = getSite(number);
        return checkFull(site);
    }

    // Does the whole system percolates?
    public boolean percolates()
    {
        Site bottomSite;
        for (int column = 1; column <= N; column++)
        {
            bottomSite = getSite(N, column);
            if (checkFull(bottomSite))
                return true;
        }
        return false;
    }

    // Test client
    public static void main(String [] args)
    {
       Percolation pc = new Percolation(5);
       //pc.open(0, 0);
       pc.open(1, 1);
       pc.open(2, 1);
       pc.open(3, 3);
       pc.open(5, 2);
       pc.open(4, 2);
       pc.open(3, 1);
       pc.open(4, 1);
       System.out.println(pc.isFull(3, 3));
       System.out.println(pc.isFull(4, 2));
       System.out.println(pc.percolates());
    }

    private class Site
    {
        // Site coordinates on a grid
        private int i, j; 

        // Site number in a grid
        private int number; 

        private boolean isOpen;

        Site(int i, int j, int n)
        {
            this.i = i;
            this.j = j;
            this.number = n;
            this.isOpen = false;

            //System.out.printf("New site(%d, %d, %d)\n", i, j , n);
        }

        public int getNumber()
        {
            return number;
        }

        public void open()
        {
            isOpen = true;
        }

        public boolean getOpen()
        {
            return isOpen;
        }

        public void connectTo(Site neighbor, WeightedQuickUnionUF uf)
        {
            if (neighbor == null)
                return;

            if (neighbor.getOpen())
                uf.union(number, neighbor.getNumber());
        }
    }
}
