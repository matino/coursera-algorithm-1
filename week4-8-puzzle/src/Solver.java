import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

class Move implements Comparable<Move> {

    public Move previous;
    public final Board board;
    public final int manhattan;
    public final int numMoves;

    public Move(Board board) {
        this.board = board;
        this.manhattan = board.manhattan();
        this.numMoves = 0;
    }

    public Move(Board board, Move previous) {
        this.board = board;
        this.manhattan = board.manhattan();
        this.previous = previous;
        this.numMoves = previous.numMoves + 1;
    }

    public int compareTo(Move other) {
        int thisPriority = this.manhattan + this.numMoves;
        int otherPriority = other.manhattan + other.numMoves;

        if (thisPriority > otherPriority) return 1;
        if (thisPriority < otherPriority) return -1;

        thisPriority = thisPriority + board.hamming();
        otherPriority = otherPriority + other.board.hamming();

        if (thisPriority > otherPriority) return 1;
        if (thisPriority < otherPriority) return -1;

        return 0;
    }
}

public class Solver {

    private Move lastMove;

    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.IllegalArgumentException("Board must not be null");

        MinPQ<Move> queue = new MinPQ<>();
        queue.insert(new Move(initial));

        MinPQ<Move> twinQueue = new MinPQ<>();
        twinQueue.insert(new Move(initial.twin()));

        while (true) {
            this.lastMove = this.makeMove(queue);
            if (this.lastMove != null || this.makeMove(twinQueue) != null)
                break;
        }
    }

    private Move makeMove(MinPQ<Move> queue) {
        if (queue.isEmpty()) return null;

        Move move = queue.delMin();
        Board board = move.board;
        if (board.isGoal()) return move;

        for (Board neighbour : board.neighbors())
            if (move.previous == null || !neighbour.equals(move.previous.board))
                queue.insert(new Move(neighbour, move));
        return null;
    }

    public boolean isSolvable() {
        return this.lastMove != null;
    }

    public int moves() {
        return this.isSolvable() ? this.lastMove.numMoves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        Stack<Board> boards = new Stack<>();
        Move move = this.lastMove;
        while (move != null) {
            boards.push(move.board);
            move = move.previous;
        }
        return boards;
    }

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
            StdOut.println("Minimum number of numMoves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
