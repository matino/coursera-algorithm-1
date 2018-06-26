import java.util.Arrays;
import java.util.ArrayList;

public class BruteCollinearPoints {

    private final Point[] points;
    private ArrayList<LineSegment> lines;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("You must pass and array of points");
        }

        this.points = new Point[points.length];
        this.lines = null;
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Point can't be null");
            }
            this.checkIfAlreadyExists(points[i]);
            this.points[i] = points[i];
        }
    }

    private void checkIfAlreadyExists(Point p) {
        for (int j = 0; j < this.points.length; j++) {
            if (this.points[j] == null)
                break;
            if (this.points[j].compareTo(p) == 0)
                throw new IllegalArgumentException("Points contain duplicates");
        }
    }

    public int numberOfSegments() {
        return this.segments().length;
    }

    public LineSegment[] segments() {
        if (this.lines != null)
            return this.linesAsArray();

        this.lines = new ArrayList<>();
        Point[] points = this.points.clone();
        int n = points.length;

        Arrays.sort(points);

        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    for (int s = r + 1; s < n; s++) {
                        double slopeToQ = points[p].slopeTo(points[q]);
                        double slopeToR = points[p].slopeTo(points[r]);
                        if (slopeToQ != slopeToR)
                            continue;
                        double slopeToS = points[p].slopeTo(points[s]);
                        if ((slopeToQ == slopeToS) && (slopeToR == slopeToS)) {
                            this.lines.add(new LineSegment(points[p], points[s]));
                        }
                    }
                }
            }
        }
        return this.linesAsArray();
    }

    private LineSegment[] linesAsArray() {
        return this.lines.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {
        Point[] points = new Point[15];
        points[14] = new Point(3, 1);
        points[1] = new Point(4, 1);
        points[2] = new Point(6, 1);
        points[3] = new Point(2, 2);
        points[4] = new Point(3, 2);
        points[5] = new Point(4, 2);
        points[6] = new Point(5, 2);
        points[7] = new Point(3, 3);
        points[8] = new Point(7, 4);
        points[9] = new Point(4, 6);
        points[10] = new Point(6, 6);
        points[11] = new Point(2, 7);
        points[12] = new Point(7, 7);
        points[13] = new Point(5, 8);
        points[0] = new Point(8, 8);
        System.out.println(Arrays.toString(points));

        BruteCollinearPoints cp = new BruteCollinearPoints(points);
        System.out.println(Arrays.toString(cp.segments()));
    }
}
