package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solver { // find a solution to the initial board (using the A* algorithm)

    private List<Board> bestSollution;
    private boolean solvable;

    public Solver(Board initial) {

        List<Board> manhattanSollution = solve(initial, true);
        if (!solvable) // We found out that puzzle is unsolvable we abort finding best sollution
            return;
        List<Board> hammingSollution = solve(initial, false);

        bestSollution = manhattanSollution.size() > hammingSollution.size() ?
                hammingSollution : manhattanSollution;

    }

    private List<Board> solve(Board initial, boolean manhattan) {
        System.out.println("Initial is : \n" + initial);

        MinPQ<SearchNode> queue = new MinPQ<>(comparator());
        queue.insert(SearchNode.create(initial, 0, manhattan));

        Board twin = initial.twin();
        MinPQ<SearchNode> twinQueue = new MinPQ<>(comparator());
        twinQueue.insert(SearchNode.create(twin, 0, manhattan));

        List<Board> sollution = new ArrayList<>();

        boolean go = true;

        Board previous = null;
        Board twinPrevious = null;
        int origMoves = 0;
        int twinMoves = 0;
        while (go) {
            SearchNode node = queue.delMin();
            SearchNode twinNode = twinQueue.delMin();
            System.out.println(twinNode.board);

            sollution.add(node.board);

            nextStep(node.board, previous, queue, origMoves, manhattan);
            nextStep(twinNode.board, twinPrevious, twinQueue, twinMoves, manhattan);

            // We detected that the puzzle is unsolvable
            if (twinNode.board.isGoal()) {
                solvable = false;
                return null;
            }

            go = !node.board.isGoal();
            previous = node.board;
            twinPrevious = twinNode.board;
            origMoves++;
            twinMoves++;
        }

        solvable = true;
        return sollution;
    }

    private void nextStep(Board b, Board previous, MinPQ<SearchNode> queue, int moves, boolean manhattan) {
        // Add neighbors
        for (Board n : b.neighbors()) {
            if (!n.equals(previous)) {
                queue.insert(SearchNode.create(n, moves + 1, manhattan));
            }
        }
    }


    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable)
            return -1;
        return bestSollution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable)
            return null;
        return bestSollution;
    }

    private static class SearchNode {
        private final Board board;
        private final int score;

        private SearchNode(Board board, int score) {
            this.board = board;
            this.score = score;
        }

        static SearchNode create(Board board, int moves, boolean manhattan) {
            if (manhattan)
                return byManhattan(board, moves);
            else
                return byHamming(board, moves);
        }

        static SearchNode byManhattan(Board board, int moves) {
            return new SearchNode(board, 0 + board.manhattan());
        }

        static SearchNode byHamming(Board board, int moves) {
            return new SearchNode(board, 0 + board.hamming());
        }
    }

    private Comparator<SearchNode> comparator() {
        return new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                return o1.score - o2.score;
            }
        };
    }


    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}
