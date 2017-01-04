package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points = new TreeSet<>();

    // construct an empty set of points
    public PointSET() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        points.forEach(p -> StdDraw.point(p.x(), p.y()));
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> res = new ArrayList<>();

        for (Point2D p : points) {
            if (rect.contains(p))
                res.add(p);
        }

        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {

        Iterator<Point2D> iter = points.iterator();

        if (!iter.hasNext())
            return null;


        Point2D nearest = iter.next();
        double minDist = nearest.distanceTo(p);

        while (iter.hasNext()) {
            Point2D other = iter.next();

            double thisDis = p.distanceTo(other);
            if (thisDis < minDist) {
                minDist = thisDis;
                nearest = other;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
    }
}
