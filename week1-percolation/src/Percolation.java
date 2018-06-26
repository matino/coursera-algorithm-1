import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int gridSize;
    private final int topSite;
    private final int bottomSite;
    private int sitesOpened;
    private boolean[] sites;
    private final WeightedQuickUnionUF uf;

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException("N must be greater than 0");
        int nSquared = n*n;
        this.topSite = 0;
        this.bottomSite = nSquared + 1;
        this.gridSize = n;
        this.sitesOpened = 0;
        this.sites = new boolean[nSquared + 1];
        this.uf = new WeightedQuickUnionUF(nSquared + 2);
    }

    public void open(int row, int col) {
        if (isOpen(row, col))
            return;
        Site site = new Site(row, col, this.gridSize);
        this.sites[site.index] = true;
        this.sitesOpened += 1;
        connectToOpenNeighbours(site);
    }

    public boolean isOpen(int row, int col) {
        Site site = new Site(row, col, this.gridSize);
        return isOpen(site);
    }

    private boolean isOpen(Site site) {
        return this.sites[site.index];
    }

    public boolean isFull(int row, int col) {
        Site site = new Site(row, col, this.gridSize);
        return this.uf.connected(this.topSite, site.index);
    }

    public int numberOfOpenSites() {
        return this.sitesOpened;
    }

    public boolean percolates() {
        return this.uf.connected(this.topSite, this.bottomSite);
    }

    private void connectToOpenNeighbours(Site site) {
        connectToOpenNeighbour(site, site.row - 1, site.col);  // Upper neighbour
        connectToOpenNeighbour(site, site.row + 1, site.col);  // Lower neighbour
        connectToOpenNeighbour(site, site.row, site.col - 1);  // Left neighbour
        connectToOpenNeighbour(site, site.row, site.col + 1);  // Right neighbour
    }

    private void connectToOpenNeighbour(Site site, int row, int col) {
        try {
            Site neighbour = new Site(row, col, this.gridSize);
            if (isOpen(neighbour))
                this.uf.union(site.index, neighbour.index);
        } catch (IllegalArgumentException e) {
            String errorMsg = e.getMessage();
            if (errorMsg.equals("row index is too small")) {
                this.uf.union(site.index, this.topSite);
            }
            if (errorMsg.equals("row index is too big")) {
                this.uf.union(site.index, this.bottomSite);
            }
        }
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(2, 2);
        System.out.println("Does the system percolates: " + p.percolates());
        p.open(1, 1);
        System.out.println("Does the system percolates: " + p.percolates());
        p.open(1, 2);
        System.out.println("Does the system percolates: " + p.percolates());
        p.open(3, 3);
        System.out.println("Does the system percolates: " + p.percolates());
        p.open(3, 2);
        System.out.println("Does the system percolates: " + p.percolates());

    }
}

/* 0
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * 10
 *
 * (1,1) (1,2) (1,3)
 * (2,1) (2,2) (2,3)
 * (3,1) (3,2) (3,3)
 *
*/

class Site {

    public final int row;
    public final int col;
    public final int index;

    public Site(int row, int col, int gridSize) {
        if (row <= 0) {
            throw new java.lang.IllegalArgumentException("row index is too small");
        }
        if (row > gridSize) {
            throw new java.lang.IllegalArgumentException("row index is too big");
        }
        if (col <= 0 || col > gridSize) {
            throw new java.lang.IllegalArgumentException("col index out of bounds");
        }
        this.row = row;
        this.col = col;
        this.index = (row - 1) * gridSize + col;
    }
}
