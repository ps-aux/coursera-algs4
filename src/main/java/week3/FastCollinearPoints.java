package week3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by arkonix on 12/18/16.
 */
public class FastCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;
    private final List<PointPair> pointPairs = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

  /*      for (int i = 0; i < points.length - 1; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length; j++) {
                Point p2 = points[j];
                if (p.slopeTo(p2) == Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("Duplicate point " + p);
            }
        }*/

        this.points = Arrays.copyOf(points, points.length);
        this.segments = calculateSegments();
    }

    private LineSegment[] calculateSegments() {
        List<LineSegment> segmentsList = new ArrayList<>();

        if (points.length > 1) { // Corner case of only one point
            // Make copy as we will be soring the array
            Point[] orig = Arrays.copyOf(points, points.length);
            for (Point p : orig) {
                Arrays.sort(points, p.slopeOrder());
                segmentsList.addAll(findSegments(p));
            }
        }

        return segmentsList.toArray(new LineSegment[0]);
    }

    private List<LineSegment> findSegments(Point main) {
        double lastSlope = main.slopeTo(points[1]); // 0th will be  Point p

        // Pointers to the range of points with same slope to the main point
        int first = 1;
        int last = 1;

        List<LineSegment> segmentsList = new ArrayList<>();
        for (int i = 2; i < points.length; i++) {
            Point p = points[i];
            double slope = main.slopeTo(p);

            boolean isSame = Double.compare(slope, lastSlope) == 0;
            boolean isLast = i == points.length - 1;

            if (isSame)
                last = i; // Increment end of the range pointer
            if (isSame && !isLast)
                continue; // It is same as previous one but not last so continue

            int count = last - first + 1;
            if (count >= 3) { // Enough colinear points
                // Copy of array including last + 1 extra fo the current point
                Point[] pointRange = Arrays.copyOfRange(points, first, last + 2);
                pointRange[count] = main;
                Arrays.sort(pointRange);
                Point a = pointRange[0];
                Point b = pointRange[pointRange.length - 1];

                if (!segmentExists(a, b)) {
                    segmentsList.add(buildSegment(a, b));
                }
            }
            // Ready for the next round
            first = i;
            last = i;
            lastSlope = slope;

        }

        return segmentsList;
    }

    private boolean segmentExists(Point a, Point b) {
        // Linear search
        for (PointPair pp : pointPairs)
            if ((pp.a == a && pp.b == b) || (pp.a == b && pp.b == a))
                return true;
        pointPairs.add(new PointPair(a, b));
        return false;
    }

    private LineSegment buildSegment(Point a, Point b) {
        return new LineSegment(a, b);
    }

    // the number of line segments

    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private static class PointPair {
        final Point a;
        final Point b;

        public PointPair(Point a, Point b) {
            this.a = a;
            this.b = b;
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.segments().length);
    }
}
