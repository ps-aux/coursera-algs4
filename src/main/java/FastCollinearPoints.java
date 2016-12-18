import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by arkonix on 12/18/16.
 */
public class FastCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();
        Set<Point> set = new HashSet<>();

        System.out.println("point  count is " + points.length);
        for (Point p : points)
            if (!set.add(p))
                throw new IllegalArgumentException("Duplicate point " + p);


        this.points = points;
        this.segments = calculateSegments();
    }

    private LineSegment[] calculateSegments() {
        List<LineSegment> segmentsList = new ArrayList<>();
        for (Point p : points) {
            Arrays.sort(points, p.slopeOrder());
            segmentsList.addAll(findSegments(p));
        }

        return segmentsList.toArray(new LineSegment[0]);
    }

    private List<LineSegment> findSegments(Point main) {
        double lastSlope = main.slopeTo(points[1]); // 0th will be  Point p

        List<Double> d = Arrays.stream(points).map(p -> main.slopeTo(p)).collect(Collectors.toList());
        System.out.println(d);

        // Pointers to the range of points with same slope to the main point
        int first = 1;
        int last = 1;

        List<LineSegment> segmentsList = new ArrayList<>();
        for (int i = 2; i < points.length; i++) {
            Point p = points[i];
            double slope = main.slopeTo(p);

            // If the same slope as the previous one and not the last point
            if (Double.compare(slope, lastSlope) == 0 && i < points.length - 1) {
                last = i; // Increment end of the range pointer
            } else { // Slope different then previous one
                int count = last - first + 1;
                if (count >= 3) { // Enough colinear points
                    // Copy of array including last + 1 extra fo the current point
                    Point[] range = Arrays.copyOfRange(points, first, last + 2);
                    range[count] = main;
                    segmentsList.add(buildSegment(range));
                }
                // Ready for the next round
                first = i;
                last = i;
                lastSlope = slope;
            }

        }

        return segmentsList;
    }

    private LineSegment buildSegment(Point[] segmentPoints) {
        Arrays.sort(segmentPoints);
        return new LineSegment(segmentPoints[0], segmentPoints[3]);
    }

    // the number of line segments

    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
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
