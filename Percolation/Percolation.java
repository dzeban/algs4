import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// !!!!!!!!!!!!!!!!!!!!!!!
// i is a row
// j is a column
// !!!!!!!!!!!!!!!!!!!!!!!
public class Percolation {

	private int N;
	private Site[] grid;
    private WeightedQuickUnionUF unionFind;

    public Percolation(int N) 
	{
		int i, j;
		this.N = N;
		
		grid = new Site[N * (N + 2)];
		unionFind = new WeightedQuickUnionUF(N * (N + 2));

		// Create grid
		i = 0;
		j = 1;
		for (int n = 0; n < N * (N + 2); n++)
		{
			grid[n] = new Site(i, j, n);

            j = (j + 1) % (N + 1);

            // Next row
            if (j == 0) {
                j = 1;
                i++;
            }
		}

		// Initialize utility rows.
		// We MUST do this after all sites are _created_
		i = 0;
		j = 1;
		for (int n = 0; n < N * (N + 2); n++)
		{
			Site site = grid[n];

			if (site.i == 0 || site.i == (N + 1))
				open(site);
		}
		System.out.println("------------------------");
	}

    // Get site number in array as n = i*N + (j -1) because columns are 1 based
	// and we must take into account utility rows.
    private int coordinates2number(int i, int j)
    {
        return i * N + (j - 1);
    }

    private Site getSite(int i, int j)
    {
        if (i < 0 || i > N + 1 || j < 1 || j > N)
            return null;

        int n = coordinates2number(i, j);
        return grid[n];
    }

    public void open(int i , int j)
	{
        if (i < 0 || i > N + 1|| j < 1 || j > N )
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        site.open();

        // Connect to neighbors
		System.out.printf("%d %d\n", i, j);
        site.connectTo(getSite(i-1, j), unionFind);
        site.connectTo(getSite(i+1, j), unionFind);
        site.connectTo(getSite(i, j-1), unionFind);
        site.connectTo(getSite(i, j+1), unionFind);
	}

	public void open(Site site)
	{
		open(site.i, site.j);
	}

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
			// Get sites from first utility row
            topSite = getSite(0, column);
            if (unionFind.connected(site.getNumber(), topSite.getNumber()))
                return true;
        }

        return false;
    }

    public boolean isFull(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        return checkFull(site);
    }

    public boolean percolates()
    {
        Site bottomSite;
        for (int column = 1; column <= N; column++)
        {
			// Get sites from the bottom utility row
            bottomSite = getSite(N + 1, column);
            if (checkFull(bottomSite))
                return true;
        }
        return false;
    }

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
        public int i, j; 

        // Site number in a grid
        private int number; 

        private boolean isOpen;

        Site(int i, int j, int n)
        {
            this.i = i;
            this.j = j;
            this.number = n;
            this.isOpen = false;

            System.out.printf("New site(%d, %d, %d)\n", i, j , n);
        }

        public int     getNumber() { return number; }
        public boolean getOpen()   { return isOpen; }
        public void    open()      { isOpen = true; }

        public void connectTo(Site neighbor, WeightedQuickUnionUF uf)
        {
            if (neighbor == null)
                return;

            if (neighbor.getOpen())
                uf.union(number, neighbor.getNumber());
        }
    }
}
