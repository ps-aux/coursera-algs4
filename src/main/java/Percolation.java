import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF unionFind;
    private final int n;
    private final int count;
    private final int virtualStart;
    private final int virtualEnd;

    private final boolean[] openSites;
    private final boolean[] fullSites;
    // Sites being checked
    private final boolean[] checkedSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 0)
            throw new IllegalArgumentException(n + " < 1");


        this.n = n;
        this.count = n * n;
        int size = count + 2; // Include two virtual sites
        this.virtualStart = 0;
        this.virtualEnd = size - 1;

        unionFind = new WeightedQuickUnionUF(size);

        this.openSites = new boolean[this.n * this.n];
        this.fullSites = new boolean[this.n * this.n];
        this.checkedSites = new boolean[this.n * this.n];
    }


    private int index(int row, int col) {
        // +1 for virtual
        // -1 off by one for UF data structure
        return n * (row - 1) + col + 1 - 1;

    }


    // Open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col))
            return;
        ensureValid(row, col);
        int index = index(row, col);

        unionNeighbour(getLeft(index), index);
        unionNeighbour(getRight(index), index);
        unionNeighbour(getTop(index), index);
        unionNeighbour(getBot(index), index);

        // Connect to either part if it is the first or last row site
        if (row == 1)
            unionFind.union(index, virtualStart);
        if (row == n)
            unionFind.union(index, virtualEnd);

        openSites[index - 1] = true;
    }

    private void unionNeighbour(int neighbour, int index) {
        if (neighbour > -1 && isOpenInteral(neighbour))
            unionFind.union(neighbour, index);
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        ensureValid(row, col);
        return isOpenInteral(index(row, col));
    }

    private boolean isOpenInteral(int index) {
        return openSites[index - 1];
    }

    private boolean isTop(int index) {
        return index <= n;
    }

    private boolean isBottom(int index) {
        return index > (count - n);
    }

    private boolean isLeft(int index) {
        return index % n == 1;
    }

    private boolean isRight(int index) {
        return index % n == 0;
    }

    private int getLeft(int index) {
        if (isLeft(index))
            return -1;
        return index - 1;
    }

    private int getRight(int index) {
        if (isRight(index))
            return -1;
        return index + 1;
    }

    private int getTop(int index) {
        if (isTop(index))
            return -1;
        return index - n;
    }

    private int getBot(int index) {
        if (isBottom(index))
            return -1;
        return index + n;
    }

    private boolean isFullInternal(int index) {
        int arrayIndex = index - 1;

        if (!openSites[arrayIndex])
            return false;
        if (fullSites[arrayIndex])
            return true;

        // Open tops are always full
        if (isTop(index))
            return true;

        // Set as being checked
        checkedSites[arrayIndex] = true;

        boolean isFull;

        isFull = isNeighbourFull(getLeft(index));
        if (!isFull)
            isFull = isNeighbourFull(getRight(index));
        if (!isFull)
            isFull = isNeighbourFull(getTop(index));
        if (!isFull)
            isFull = isNeighbourFull(getBot(index));

        if (isFull)
            fullSites[arrayIndex] = true;

        checkedSites[arrayIndex] = false;
        return isFull;
    }

    private boolean isNeighbourFull(int neighbour) {
        return neighbour != -1 &&
                !checkedSites[neighbour - 1] &&
                isFullInternal(neighbour);

    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        ensureValid(row, col);
        int index = index(row, col);

        // Not opened
        if (!openSites[index - 1])
            return false;
        return unionFind.connected(index, virtualStart);
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
        if (row > n)
            throw new IndexOutOfBoundsException("row " + row + " is > " + n);
        if (col > n)
            throw new IndexOutOfBoundsException("col " + col + " is > " + n);
    }


    // test client (optional)
    public static void main(String[] args) {

    }
}

