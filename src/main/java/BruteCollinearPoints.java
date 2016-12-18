import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BruteCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
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

/*        Object[][] combs = Combinator.findCombinations(points, 4);
        System.out.println("combinator " + combs.length);*/

        int c = 0;
        List<LineSegment> segmentsList = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        Point p = points[i];
                        Point q = points[j];
                        Point r = points[k];
                        Point s = points[l];

                        Point[] myPoints = new Point[]{p, q, r, s};
                        c++;

                        double slopePq = p.slopeTo(q);
                        double slopePr = p.slopeTo(r);
                        double slopePs = p.slopeTo(s);

                        if (slopePq == slopePr && slopePr == slopePs) {
                            System.out.println(Arrays.toString(myPoints));
                            Arrays.sort(myPoints);
                            segmentsList.add(new LineSegment(myPoints[0], myPoints[3]));
                        }
                    }
                }
            }
        }


        System.out.println("combination count is " + c);
        return segmentsList.toArray(new LineSegment[0]);
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        System.out.println(collinear.segments().length);
    }
}
