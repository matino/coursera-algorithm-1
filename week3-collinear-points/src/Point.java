import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        if (this.x == that.x) return Double.POSITIVE_INFINITY;
        if (this.y == that.y) return +0.0;
        return (double) (that.y - this.y) / (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.y == that.y) {
            if (this.x < that.x) return -1;
            if (this.x > that.x) return +1;
        }
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder(this);
    }

    private static class SlopeOrder implements Comparator<Point> {

        private final Point point;

        public SlopeOrder(Point point) {
            this.point = point;
        }

        public int compare(Point p1, Point p2) {
            Double slope1 = this.point.slopeTo(p1);
            Double slope2 = this.point.slopeTo(p2);
            return slope1.compareTo(slope2);
        }
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static void main(String[] args) {
        Point[] points = new Point[10];
        Random rn = new Random();
        for (int i = 0; i < points.length; i++) {
            int x = rn.nextInt(5) + 1;
            int y = rn.nextInt(5) + 1;
            points[i] = new Point(x, y);
        }
        Arrays.sort(points);
        System.out.println(Arrays.toString(points));

        Point p1 = new Point(0, 0);
        Point p2 = new Point(2, 1);
        Point p3 = new Point(1, 2);
        Comparator<Point> c = p1.slopeOrder();
        System.out.println(c.compare(p2, p3));
    }
}