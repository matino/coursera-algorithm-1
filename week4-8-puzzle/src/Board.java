import java.util.ArrayList;
import java.util.Arrays;

class Position {
    final int x;
    final int y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Board {

    private static final int BLANK = 0;
    private final int[][] blocks;
    private final int n;

    private Integer hamming;
    private Integer manhattan;

    private int blankX;
    private int blankY;

    public Board(int[][] blocks) {
        this.hamming = null;
        this.n = blocks.length;
        this.blocks = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == BLANK) {
                    this.blankX = i;
                    this.blankY = j;
                }
            }
        }
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbours = new ArrayList<>();

        if (blankX - 1 >= 0) {
            this.addNeighbour(neighbours, blankX - 1, blankY);
        }
        if (blankX + 1 < this.n) {
            this.addNeighbour(neighbours, blankX + 1, blankY);
        }
        if (blankY - 1 >= 0) {
            this.addNeighbour(neighbours, blankX, blankY - 1);
        }
        if (blankY + 1 < this.n) {
            this.addNeighbour(neighbours, blankX, blankY + 1);
        }

        return neighbours;
    }

    private void addNeighbour(ArrayList<Board> neighbours, int x, int y) {
        // Create a copy of this.blocks
        int[][] blocksCopy = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++)
            blocksCopy[i] = this.blocks[i].clone();

        blocksCopy[blankX][blankY] = this.blocks[x][y];
        blocksCopy[x][y] = BLANK;
        Board leftNeighbour = new Board(blocksCopy);
        neighbours.add(leftNeighbour);
    }

    public int dimension() {
        return this.n;
    }

    public int hamming() {
        if (this.hamming == null) {
            int wrongPositions = 0;
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    int value = this.blocks[i][j];
                    if (value != BLANK && value != i * this.n + j + 1)
                        wrongPositions++;
                }
            }
            this.hamming = wrongPositions;
        }

        return this.hamming;
    }

    public int manhattan() {
        if (this.manhattan == null) {
            int distance = 0;
            for (int i = 0; i < this.n; i++) {
                for (int j = 0; j < this.n; j++) {
                    int value = this.blocks[i][j];
                    if (value == BLANK)
                        continue;
                    int valueX = (value - 1) / this.n;
                    int valueY = (value - 1) % this.n;
                    distance = distance + Math.abs(valueX - i) + Math.abs(valueY - j);
                }
            }
            this.manhattan = distance;
        }
        return this.manhattan;
    }

    public boolean isGoal() {
        return this.manhattan() == 0;
    }

    public Board twin() {
        // Create a copy of this.blocks
        int[][] blocksCopy = new int[this.n][this.n];
        for (int i = 0; i < this.n; i++)
            blocksCopy[i] = this.blocks[i].clone();

        // Get positions of elements to swap
        Position p1 = this.findNotBlankBlockPosition(0);
        Position p2 = this.findNotBlankBlockPosition(1);

        // Swap elements
        int temp = blocksCopy[p1.x][p1.y];
        blocksCopy[p1.x][p1.y] = blocksCopy[p2.x][p2.y];
        blocksCopy[p2.x][p2.y] = temp;

        Board twin = new Board(blocksCopy);
        return twin;
    }

    private Position findNotBlankBlockPosition(int row) {
        for (int i = 0; i < this.n; i++) {
            if (this.blocks[row][i] != BLANK) {
                return new Position(row, i);
            }
        }
        return null;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board that = (Board) other;
        return Arrays.deepEquals(this.blocks, that.blocks);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.n + "\n");
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = new int[3][3];
        blocks[0][0] = 8;
        blocks[0][1] = 1;
        blocks[0][2] = 3;

        blocks[1][0] = 4;
        blocks[1][1] = 0;
        blocks[1][2] = 2;

        blocks[2][0] = 7;
        blocks[2][1] = 6;
        blocks[2][2] = 5;

        Board board = new Board(blocks);
        System.out.println(board);
        System.out.println(board.hamming());
        System.out.println(board.manhattan());

        System.out.println("--- Neighbours:\n");
        Iterable<Board> neighbours = board.neighbors();
        for (Board b : neighbours) {
            System.out.println(b);
        }
        System.out.println(board.twin());
    }
}
