package week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;

public class Solver {

    private Collection<Board> sollution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        SearchNode node = solve(initial);
        if (node == null) // Unsolvable. Nothing to do here.
            return;

        Deque<Board> sollution = new ArrayDeque<Board>();

        assert (node.previous != null); // At least one itter
        do {
            sollution.push(node.board);
            node = node.previous;
        } while (node != null);

        this.sollution = sollution;
    }

    private SearchNode solve(Board initial) {
        SearchNode initialNode = new SearchNode(initial, null);
        if (initial.isGoal())
            return initialNode;

        MinPQ<SearchNode> queue = new MinPQ<>(comparator());
        queue.insert(initialNode);

        Board twinNode = initial.twin();
        MinPQ<SearchNode> twinQueue = new MinPQ<>(comparator());
        twinQueue.insert(new SearchNode(twinNode, null));

        while (true) {

            SearchNode twin = nextStep(twinQueue);
            if (twin.board.isGoal())
                return null;

            SearchNode node = nextStep(queue);
            if (node.board.isGoal()) {
                return node;
            }
        }
    }

    private SearchNode nextStep(MinPQ<SearchNode> queue) {
        SearchNode node = queue.delMin();
        // Add neighbors
        for (Board n : node.board.neighbors()) {
            if (!n.equals(node.previous)) {
                queue.insert(new SearchNode(n, node));
            }
        }

        return node;
    }


    // is the initial board solvable?
    public boolean isSolvable() {
        return sollution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable())
            return -1;
        return sollution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return sollution;
    }

    private static class SearchNode {
        private final Board board;
        private final SearchNode previous;
        private final int moves;
        private final int priority;

        private SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.moves = previous == null ? 0 : previous.moves + 1;
            this.priority = moves + board.manhattan();
            if (!(previous == null) && !(this.priority >= previous.priority))
                throw new IllegalArgumentException("eeeeee");

        }
    }

    private Comparator<SearchNode> comparator() {
        return new Comparator<SearchNode>() {
            @Override
            public int compare(SearchNode o1, SearchNode o2) {
                return o1.priority - o2.priority;
            }
        };
    }


    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        System.out.println(args[0]);
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
        System.out.println(solver.moves());
        if (solver.sollution != null)
            System.out.println(solver.sollution.size());
    }


}
