import edu.princeton.cs.algorithms.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF unionFind;
    private final int N;
    private final int virtualStart;
    private final int virtualEnd;
    private final boolean[] openSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 0)
            throw new IllegalArgumentException(n + " < 1");


        this.N = n;
        int size = N * N + 2; // Include two virtual sites
        this.virtualStart = 0;
        this.virtualEnd = size - 1;

        unionFind = new WeightedQuickUnionUF(size);

        for (int i = 1; i <= N; i++) {
            // Connect start
            unionFind.union(virtualStart, index(1, i));
            // Connect end
            unionFind.union(virtualEnd, index(N, i));
        }

        this.openSites = new boolean[N * N];
    }


    private int index(int row, int col) {
        // +1 for virtual
        // -1 off by one for UF data structure
        return N * (row - 1) + col + 1 - 1;

    }

    /**
     * Open site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        if (isOpen(row, col))
            return;

        ensureValid(row, col);
        int index = index(row, col);

        // Connect to the neighbouring sites
        if (row > 1) {
            int top = index(row - 1, col);
            if (isOpenInteral(top))
                unionFind.union(index, top);
        }
        if (row < N) {
            int bottom = index(row + 1, col);
            if (isOpenInteral(bottom))
                unionFind.union(index, bottom);
        }
        if (col > 1) {
            int left = index(row, col - 1);
            if (isOpenInteral(left))
                unionFind.union(index, left);
        }
        if (col < N) {
            int right = index(row, col + 1);
            if (isOpenInteral(right))
                unionFind.union(index, right);
        }

        openSites[index - 1] = true;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        ensureValid(row, col);
        return isOpenInteral(index(row, col));
    }

    private boolean isOpenInteral(int index) {
        return openSites[index - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        ensureValid(row, col);
        return unionFind.connected(index(row, col), virtualStart);
    }

    // does the system percolate?
    public boolean percolates() {
        return unionFind.connected(virtualStart, virtualEnd);
    }

    private void ensureValid(int row, int col) {
        if (row < 1)
            throw new IndexOutOfBoundsException("row " + row + " is < 1");
        if (col < 1)
            throw new IndexOutOfBoundsException("col " + col + " is < 1");
        if (row > N)
            throw new IndexOutOfBoundsException("row " + row + " is > " + N);
        if (col > N)
            throw new IndexOutOfBoundsException("col " + col + " is > " + N);
    }


    // test client (optional)
    public static void main(String[] args) {


    }
}

