/*----------------------------------------------------------------
 *  Author:        Alex Dzyoba
 *  Written:       2015-07-08
 *  Last updated:  2015-09-13
 *
 *  Compilation:   javac-algs4 Percolation.java
 *  Execution:     java-algs4 Percolation
 *
 *  Find percolation of a system modeled by square grid of size NxN.
 *  Allows you to:
 *
 *  - create an empty grid with all grid sites blocked
 *  - open a site by coordinates (i, j) where i is a row
 *    and j is a column
 *  - check whether given site is full, i.e. it's connected with a top row
 *  - check if the whole system percolates
 *
 *----------------------------------------------------------------*/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


/**
 * Percolation class provides you method to model grid systems and checks
 * percolation.
 *
 * It creates grid of size NxN with additional utility rows ("shadow" rows) that
 * simplifies accounting of connected components. All sites in these shadow rows
 * are open. To check whether system percolates we simply check if any of the
 * sites in bottom shadow row is connected with any of the top shadow row. But
 * because both of these rows are open, such check is done only once.
 *
 *            Columns
 *
 *            1   2   3
 *          +-----------+
 *       0  | x | x | x | -- Top shadow row
 *          +===========+
 *       1  |   |   |   |  -+
 *          +-----------+   |
 * rows  2  |   |   |   |   +-- Visible to client grid
 *          +-----------+   |
 *       3  |   |   |   |  -+
 *          +===========+
 *       4  | x | x | x | -- Bottom shadow row
 *          +-----------+
 *
 * So, the actual size of the grid is N * (N + 2)
 *
 * Why use a shadow rows? Because of the 1x1 case. WeightedQuickUnionUF class
 * thinks that site is connected to itself which is wrong for our purposes.
 *
 * You should beware though that this implementation suffers from the "backwash"
 * problem.
 *
 */
public class Percolation {

    /** Grid size */
    private int N;

    /** Grid itself */
    private Site[] grid;

    /** Union-find structure to account connected components */
    private WeightedQuickUnionUF unionFind; // structure

    /**
     * Create grid and initialize all sites.
     */
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
        // We MUST do this after all sites are _created_ to avoid null pointers.
        i = 0;
        j = 1;
        for (int n = 0; n < N * (N + 2); n++)
        {
            Site site = grid[n];

            if (site.i == 0 || site.i == (N + 1))
                open(site);
        }
    }

    /**
     * Get site number in array as n = i*N + (j -1) because columns are 1 based
     * and we must take into account utility rows.
     */
    private int xyToN(int i, int j) { return i * N + (j - 1); }

    /**
     * Return site by given row and column
     */
    private Site getSite(int i, int j)
    {
        if (i < 0 || i > N + 1 || j < 1 || j > N)
            return null;

        int n = xyToN(i, j);
        return grid[n];
    }

    /**
     * Open a site by given row and column
     */
    public void open(int i , int j)
    {
        if (i < 0 || i > N + 1 || j < 1 || j > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        site.open();

        // Connect to neighbors
        site.connectTo(getSite(i-1, j), unionFind);
        site.connectTo(getSite(i+1, j), unionFind);
        site.connectTo(getSite(i, j-1), unionFind);
        site.connectTo(getSite(i, j+1), unionFind);
    }

    /**
     * Open a site by given reference
     */
    private void open(Site site) { open(site.i, site.j); }

    /**
     * Check whether is site pointed by given row and column is open
     */
    public boolean isOpen(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        return site.getOpen();
    }

    /**
     * Check whether site is full, i.e. connected to the top row
     */
    private boolean checkFull(Site site)
    {
        Site topSite;
        return unionFind.connected(site.getNumber(), xyToN(0, 1));
    }

    /**
     * Check whether site by given row and column is full, i.e. connected to the
     * top row
     */
    public boolean isFull(int i, int j)
    {
        if (i < 1 || i > N)
            throw new IllegalArgumentException("Site must be (1..N, 1..N)");

        Site site = getSite(i, j);
        return checkFull(site);
    }

    /**
     * Check whether the whole system percolates
     */
    public boolean percolates()
    {
        Site bottomSite;
        return unionFind.connected(xyToN(0, 1), xyToN(N + 1, 1));
    }

    /**
     * Test client
     */
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

    /**
     * Inner class representing a Site
     */
    private class Site
    {
        /** Site coordinates on a grid */
        public int i, j; 

        /** Site number in a grid array */
        private int number; 

        /** Site state */
        private boolean isOpen;

        /** Public constructor */
        Site(int i, int j, int n)
        {
            this.i = i;
            this.j = j;
            this.number = n;
            this.isOpen = false;
        }

        /** Get site number in a grid array */
        public int     getNumber() { return number; }

        /** Get site state */
        public boolean getOpen() { return isOpen; }

        /** Set site state to "open". Does NOT connect it to neighbors! */
        public void    open()  { isOpen = true; }

        /** Connect this site to the neighbor site using Union-Find structure */
        public void connectTo(Site neighbor, WeightedQuickUnionUF uf)
        {
            if (neighbor == null)
                return;

            if (neighbor.getOpen())
                uf.union(number, neighbor.getNumber());
        }
    }
}
