import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

    public PointSET() {
        this.points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size() {
        return this.points.size();
    }

    public void insert(Point2D point) {
        if (point == null) throw new java.lang.IllegalArgumentException("Point can't be null");
        this.points.add(point);
    }

    public boolean contains(Point2D point) {
        if (point == null) throw new java.lang.IllegalArgumentException("Point can't be null");
        return this.points.contains(point);
    }

    public void draw() {
        for (Point2D point : this.points) {
            StdDraw.setPenColor(StdDraw.BLACK);
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rectangular) {
        if (rectangular == null) throw new java.lang.IllegalArgumentException("Rectangular can't be null");

        ArrayList<Point2D> result = new ArrayList<>();
        for (Point2D point : this.points) {
            if (rectangular.contains(point)) {
                result.add(point);
            }
        }
        return result;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) throw new java.lang.IllegalArgumentException("Point can't be null");

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D p : this.points) {
            double currentDistance = p.distanceSquaredTo(point);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0.1, 0.1));
        set.insert(new Point2D(0.1, 0.2));
        set.insert(new Point2D(0.5, 0.5));
        set.insert(new Point2D(0.5, 0.1));
        set.insert(new Point2D(0.6, 0.3));
        set.insert(new Point2D(0.6, 0.9));
        System.out.println(set.contains(new Point2D(0,1)));
        System.out.println(set.contains(new Point2D(0,0)));

        Iterable<Point2D> points = set.range(new RectHV(4, 1, 8, 6));
        System.out.println(points.toString());
        System.out.println(set.nearest(new Point2D(4, 3)));
        set.draw();
    }
}
