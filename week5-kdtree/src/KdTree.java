import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

class Node {

    public Point2D point;
    public Node left, right;
    public int size;  // number of nodes in subtree

    public Node(Point2D point, int size) {
        this.point = point;
        this.size = size;
    }
}

class NearestCandidate {

    public Point2D point;
    public double distance;

    public NearestCandidate(Point2D point, double distance) {
        this.point = point;
        this.distance = distance;
    }

    public NearestCandidate() {
        this.point = null;
        this.distance = Double.POSITIVE_INFINITY;
    }
}

public class KdTree {

    private static final RectHV BOARD_RECT = new RectHV(0, 0, 1, 1);
    private static final boolean HORIZONTAL = true;

    private Node root = null;

    public KdTree() {
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size(this.root);
    }

    private int size(Node node) {
        return (node == null) ? 0 : node.size;
    }

    public void insert(Point2D point) {
        if (point == null)
            throw new java.lang.IllegalArgumentException("Point can't be null");

        this.root = this.insert(this.root, point, HORIZONTAL);
    }

    private Node insert(Node node, Point2D point, boolean orientation) {
        if (node == null)
            return new Node(point, 1);

        if (node.point.equals(point))
            return node;

        int cmp = this.compare(node, point, orientation);
        if (cmp < 0)
            node.left = this.insert(node.left, point, !orientation);
        if (cmp >= 0)
            node.right = this.insert(node.right, point, !orientation);

        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public boolean contains(Point2D point) {
        if (point == null)
            throw new java.lang.IllegalArgumentException("Point can't be null");

        return this.get(this.root, point, HORIZONTAL) != null;
    }

    private Node get(Node node, Point2D point, boolean orientation) {
        if (node == null)
            return null;

        if (node.point.equals(point))
            return node;

        int cmp = this.compare(node, point, orientation);
        if (cmp < 0)
            return this.get(node.left, point, !orientation);
        else
            return this.get(node.right, point, !orientation);
    }

    private int compare(Node node, Point2D point, boolean orientation) {
        return orientation ?
                Point2D.X_ORDER.compare(node.point, point) :
                Point2D.Y_ORDER.compare(node.point, point);
    }

    public void draw() {
        this.draw(this.root, BOARD_RECT, HORIZONTAL);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.IllegalArgumentException("Rectangular can't be null");

        ArrayList<Point2D> points = new ArrayList<>();
        this.range(this.root, BOARD_RECT, rect, points, HORIZONTAL);
        return points;
    }

    private void range(Node node, RectHV nodeRect, RectHV rect, ArrayList<Point2D> points, boolean orientation) {
        if (node == null)
            return;

        if (nodeRect.intersects(rect)) {
            if (rect.contains(node.point))
                points.add(node.point);
            this.range(node.left, this.leftRectangle(node, nodeRect, orientation), rect, points, !orientation);
            this.range(node.right, this.rightRectangle(node, nodeRect, orientation), rect, points, !orientation);
        }
    }

    public Point2D nearest(Point2D target) {
        if (target == null)
            throw new java.lang.IllegalArgumentException("Point can't be null");

        NearestCandidate candidate = this.nearest(this.root, BOARD_RECT, target, new NearestCandidate(), HORIZONTAL);
        return (candidate != null) ? candidate.point : null;
    }

    private NearestCandidate nearest(Node node, RectHV nodeRect, Point2D target, NearestCandidate min, boolean orientation) {
        if (node == null)
            return null;

        if (nodeRect.distanceSquaredTo(target) >= min.distance)
            return null;

        double nodeDistance = node.point.distanceSquaredTo(target);
        if (nodeDistance < min.distance)
            min = new NearestCandidate(node.point, nodeDistance);

        RectHV leftRectangle = this.leftRectangle(node, nodeRect, orientation);
        RectHV rightRectangle = this.rightRectangle(node, nodeRect, orientation);

        double leftRectDist = leftRectangle.distanceSquaredTo(target);
        double rightRectDist = rightRectangle.distanceSquaredTo(target);

        Node first = node.right;
        Node second = node.left;
        RectHV firstRect = rightRectangle;
        RectHV secondRect = leftRectangle;
        if (leftRectDist <= rightRectDist) {
            first = node.left;
            firstRect = leftRectangle;
            second = node.right;
            secondRect = rightRectangle;
        }

        // Process closer rectangle first
        NearestCandidate firstCandidate = this.nearest(first, firstRect, target, min, !orientation);
        if (firstCandidate != null && firstCandidate.distance < min.distance)
            min = firstCandidate;

        // Don't process right rectangle if it's further than found minimum
        if (rightRectDist >= min.distance)
            return min;

        NearestCandidate secondCandidate = this.nearest(second, secondRect, target, min, !orientation);
        if (secondCandidate != null && secondCandidate.distance < min.distance)
            min = secondCandidate;

        return min;
    }

    private void draw(Node node, RectHV parent, boolean orientation) {
        // Draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();

        // Draw line
        StdDraw.setPenRadius();
        if (orientation) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), parent.ymin(), node.point.x(), parent.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(parent.xmin(), node.point.y(), parent.xmax(), node.point.y());
        }

        if (node.left != null)
            this.draw(
                node.left,
                this.leftRectangle(node, parent, orientation),
                !orientation
            );
        if (node.right != null)
            this.draw(
                node.right,
                this.rightRectangle(node, parent, orientation),
                !orientation
            );
    }

    private RectHV rightRectangle(Node node, RectHV parent, boolean orientation) {
        if (orientation)
            return new RectHV(parent.xmin(), parent.ymin(), node.point.x(), parent.ymax());
        else
            return new RectHV(parent.xmin(), parent.ymin(), parent.xmax(), node.point.y());
    }

    private RectHV leftRectangle(Node node, RectHV parent, boolean orientation) {
        if (orientation)
            return new RectHV(node.point.x(), parent.ymin(), parent.xmax(), parent.ymax());
        else
            return new RectHV(parent.xmin(), node.point.y(), parent.xmax(), parent.ymax());
    }

    public static void main(String[] args) {
        KdTree set = new KdTree();
        set.insert(new Point2D(0.372, 0.497));
        set.insert(new Point2D(0.564, 0.413));
        set.insert(new Point2D(0.226, 0.577));
        set.insert(new Point2D(0.144, 0.179));
        set.insert(new Point2D(0.083, 0.51));
        set.insert(new Point2D(0.32, 0.708));
        set.insert(new Point2D(0.417, 0.362));
        set.insert(new Point2D(0.862, 0.825));
        set.insert(new Point2D(0.785, 0.725));
        set.insert(new Point2D(0.499, 0.208));
        set.draw();

        Iterable<Point2D> points = set.range(new RectHV(0.2, 0.2, 0.6, 0.9));
        System.out.println(points);

        // Traversal: (0.372, 0.497), (0.564, 0.413), (0.862, 0.825), (0.785, 0.725)
        System.out.println(set.nearest(new Point2D(0.97, 0.85)));  // (0.862, 0.825)

        System.out.println();

        KdTree set2 = new KdTree();
        set2.insert(new Point2D(0.7, 0.2));
        set2.insert(new Point2D(0.5, 0.4));
        set2.insert(new Point2D(0.2, 0.3));
        set2.insert(new Point2D(0.4, 0.7));
        set2.insert(new Point2D(0.9, 0.6));
        // Traversal: (0.7, 0.2), (0.5, 0.4), (0.4, 0.7), (0.2, 0.3)
        System.out.println(set2.nearest(new Point2D(0.37, 0.4)));  // (0.5, 0.4)
    }

}
