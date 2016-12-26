package week4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final int[][] blocks;
    private final int size;
    private int empty = -1;
    private final int dimension;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.blocks = makeArrayCopy(blocks);


        this.dimension = this.blocks.length;
        size = dimension * dimension;

        // Detect empty
        for (int i = 1; i <= size; i++) {
            if (get(i) == 0) {
                empty = i;
                break;
            }
        }

        if (empty == -1)
            throw new IllegalArgumentException("Could not find empty field");
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int count = 0;
        for (int i = 1; i <= size; i++) {
            if (i == empty)
                continue;
            if (get(i) != i)
                count++;
        }

        return count;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int count = 0;
        for (int i = 1; i <= size; i++) {
            if (i == empty)
                continue;
            count += getManhattanDistance(i);
        }

        return count;
    }

    private int getManhattanDistance(int i) {
        int val = get(i);
        if (val == i)
            return 0;

        // Target coordinates
        int tx = getX(val);
        int ty = getY(val);

        // Current coordinates
        int cx = getX(i);
        int cy = getY(i);

        return Math.abs(tx - cx) + Math.abs(ty - cy);
    }

    private int getY(int index) {
        int row = (int) Math.ceil((double) index / dimension);
        return row;
    }

    private int getX(int index) {
        int col = index % dimension;
        if (col == 0) col = dimension; // Cycle

        return col;
    }

    private int get(int index) {
        return blocks[getY(index) - 1][getX(index) - 1];
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int first = 1;
        int second = first + 1;
        if (get(first) == 0) { // We hit empty field so we go the row bellow
            first = first + dimension;
            second = first + 1; // Recalculate
        } else {
            // Try second one
            if (get(second) == 0) { // Nope it is empty. Again we go to the row below
                first = first + dimension;
                second = first + 1; // Recalculate
            }
        }
        assert get(first) != 0;
        assert get(second) != 0;


        return swapped(first, second);
    }

    private Board swapped(int i1, int i2) {
        int[][] cp = makeArrayCopy(blocks);
        int tmp = get(i1);
        cp[getY(i1) - 1][getX(i1) - 1] = get(i2);
        cp[getY(i2) - 1][getX(i2) - 1] = tmp;

        return new Board(cp);
    }

    /**
     * Deep makeArrayCopy of the 2 dimensional array
     */
    private int[][] makeArrayCopy(int[][] original) {
        int[][] cp = new int[original.length][];
        for (int i = 0; i < original.length; i++)
            cp[i] = Arrays.copyOf(original[i], original.length);

        return cp;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;

        if (!(y.getClass() == this.getClass()))
            return false;

        Board other = (Board) y;
        if (dimension() != other.dimension())
            return false;

        for (int i = 1; i <= size; i++) {
            if (get(i) != other.get(i))
                return false;

        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Integer> neighborFields = new ArrayList<>();

        if (getY(empty) > 1) // Is not first row
            neighborFields.add(empty - dimension);
        if (getY(empty) < dimension) // If not the last row
            neighborFields.add(empty + dimension);
        if (getX(empty) > 1)
            neighborFields.add(empty - 1);
        if (getX(empty) < dimension)
            neighborFields.add(empty + 1);

        List<Board> neighbors = new ArrayList<>();
        for (int field : neighborFields)
            neighbors.add(swapped(empty, field));

        return neighbors;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension).append("\n");
        for (int i = 1; i <= size; i++) {
            sb.append(String.format("%2d", get(i)));
            if (i % dimension() == 0)
                sb.append("\n");
            else
                sb.append(" ");

        }

        return sb.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] ar = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

        Board b = new Board(ar);
        System.out.println(b);
        System.out.println();
        System.out.println();

        System.out.println(b.neighbors());

    }
}
