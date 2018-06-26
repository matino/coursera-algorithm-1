import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private ArrayList<LineSegment> lines;

    public FastCollinearPoints(Point[] points) {
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
        int n = this.points.length;
        Point[] pointsCopy = this.points.clone();

        // It's critical to process points in order, so we can detect duplicate lines
        Arrays.sort(pointsCopy);

        for (int i = 0; i < n - 3; i++) {
            Point startPoint = pointsCopy[i];

            // Last element is null => we don't need to duplicate the "if section in the for loop below" for last entry
            Point[] sortedPoints = Arrays.copyOf(pointsCopy, n + 1);
            // Note that this sort is stable and doesn't change the order of equal points from the top sort
            Arrays.sort(sortedPoints, 0, n, startPoint.slopeOrder());

            int start = 0;
            for (int j = 1; j < sortedPoints.length; j++) {
                // currPoint is the 2nd point of the possible line segment (the first point is `startPoint`)
                Point currPoint = sortedPoints[start];
                Point nextPoint = sortedPoints[j];
                if (nextPoint == null || currPoint.slopeTo(startPoint) != nextPoint.slopeTo(startPoint)) {
                    int distance = j - start;
                    /*
                    We don't want to add the line, if it was already processed. We know that it was processed, if
                    the 2nd point is below the `startPoint`. In other words, the line is valid if and only if all points
                    in the sequence are increasing e.g. [(2,2), (3,3), (4,4), (5,5)]. This sequence is invalid:
                    [(3,3), (2,2), (4,4), (5,5)]. Since we first sort the entire array by points natural order, we can
                    be sure that the 2nd point in the sequence will be the smallest one, even though we sort by slope
                    order afterwards (which is a stable sort i.e. doesn't change the order of the previous sort.
                     */
                    if (distance > 2 && currPoint.compareTo(startPoint) > 0) {
                        Point endPoint = sortedPoints[j - 1];
                        lines.add(new LineSegment(startPoint, endPoint));
                    }
                    start = j;
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

        FastCollinearPoints cp = new FastCollinearPoints(points);
        System.out.println(Arrays.toString(cp.segments()));
        System.out.println(Arrays.toString(cp.segments()));

        Point[] points2 = new Point[4];
        points2[0] = new Point(3, 1);
        points2[1] = new Point(4, 1);
        points2[2] = new Point(6, 1);
        points2[3] = new Point(2, 1);

        Arrays.sort(points2);
        System.out.println(Arrays.toString(points2));

        FastCollinearPoints cp2 = new FastCollinearPoints(points2);
        System.out.println(Arrays.toString(cp2.segments()));
    }
}
