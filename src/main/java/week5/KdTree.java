package week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KdTree {

    private Node root;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        if (isEmpty())
            return 0;
        return root.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (isEmpty()) {
            root = new Node(p, 0, new RectHV(0, 0, 1, 1));
            return;
        }
        root.insert(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (isEmpty())
            return false;
        return root.find(p) != null;
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty())
            return;
        draw(root);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (isEmpty() == true)
            return (Iterable<Point2D>) Collections.EMPTY_LIST; // Won't accept assignment otherwise
        return root.range(rect);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty())
            return null;
        return root.nearest(p);
    }


    private void draw(Node n) {
        if (n == null)
            return;
        double x = n.point.x();
        double y = n.point.y();

        StdDraw.setPenColor();
        StdDraw.filledCircle(x, y, 0.005);
        if (n.depth % 2 == 0) {
            //Draw vertical line
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(x, n.r.ymin(), x, n.r.ymax());

            // Children will draw vertical line - bound x coordinates
            draw(n.left); // Left
            draw(n.right); // Right
        } else {
            //Draw horizontal line
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(n.r.xmin(), y, n.r.xmax(), y);

            draw(n.left); // Top
            draw(n.right); // Botton
        }
    }

    private static class Node {
        private final Point2D point;
        private final int depth;
        private Node left;
        private Node right;
        private int size;
        private final RectHV r;

        Node(Point2D point, int depth, RectHV r) {
            this.point = point;
            this.depth = depth;
            this.r = r;
            size = 1;
        }

        Point2D find(Point2D p) {
            if (p.equals(point))
                return point;

            return isLarger(p) ? findInChild(p, right) : findInChild(p, left);
        }

        boolean insert(Point2D p) {
            if (p.equals(point))
                return false;

            boolean inserted;

            if (isLarger(p)) {
                if (right == null) {
                    right = new Node(p, depth + 1, calculateRectRight());
                    inserted = true;
                } else {
                    inserted = right.insert(p);
                }
            } else {
                if (left == null) {
                    left = new Node(p, depth + 1, calculateRectLeft());
                    inserted = true;
                } else {
                    inserted = left.insert(p);
                }
            }

            if (inserted)
                size++;

            return inserted;
        }

        private RectHV calculateRectLeft() {
            if (depth % 2 == 0)
                return new RectHV(r.xmin(), r.ymin(), point.x(), r.ymax()); //Left
            else
                return new RectHV(r.xmin(), r.ymin(), r.xmax(), point.y()); //Bottom
        }

        private RectHV calculateRectRight() {
            if (depth % 2 == 0)
                return new RectHV(point.x(), r.ymin(), r.xmax(), r.ymax());
            else
                return new RectHV(r.xmin(), point.y(), r.xmax(), r.ymax());

        }


        private Point2D findInChild(Point2D p, Node child) {
            return child == null ? null : child.find(p);
        }

        private boolean isLarger(Point2D p) {
            if (depth % 2 == 0)
                return p.x() > point.x();
            else
                return p.y() > point.y();
        }


        List<Point2D> range(RectHV rec) {
            List<Point2D> acc = new ArrayList<>();
            this.range(rec, acc);

            return acc;
        }

        Point2D nearest(Point2D p) {
            NearestResult acc = new NearestResult();
            acc.point = p;
            acc.champion = point;
            /**
             * Use distanceSquareTo as it is less computationally intensive
             * as distanceTwo and the relation between values holds anyway
             */
            acc.minDistance = point.distanceSquaredTo(p);

            if (left != null)
                left.nearest(acc);
            if (right != null)
                right.nearest(acc);


            return acc.champion;
        }

        private void nearest(NearestResult acc) {
            // This rectangle is further than the nearest point
            if (acc.minDistance < r.distanceSquaredTo(acc.point))
                return;
            acc.challenge(point);
            if (left != null)
                left.nearest(acc);
            if (right != null)
                right.nearest(acc);
        }

        private static class NearestResult {

            private Point2D point;
            private Point2D champion;
            private double minDistance;

            void challenge(Point2D candidate) {
                double d = candidate.distanceSquaredTo(point);
                if (d >= minDistance) // Champion still holds title
                    return;

                minDistance = d;
                champion = candidate;
            }
        }

        private void range(RectHV rect, List<Point2D> acc) {
            // No intersection here.
            if (!r.intersects(rect))
                return;

            if (left != null) {
                left.range(rect, acc);
            }
            if (rect.contains(point))
                acc.add(point);
            if (right != null)
                right.range(rect, acc);
        }


    }
}

