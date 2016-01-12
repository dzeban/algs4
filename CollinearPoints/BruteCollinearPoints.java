import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class BruteCollinearPoints 
{
    LineSegment[] lines;
    Map<Double, Set<Point>> slopePoints = new HashMap<Double, Set<Point>>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points)
    {
        Point p, q, r, s;
        int length = points.length;
        for (int i = 0; i < length; i++)
        {
            p = points[i];
            for (int j = i + 1; j < length; j++)
            {
                q = points[j];
                for (int k = j + 1; k < length; k++)
                {
                    r = points[k];
                    for (int l = k + 1; l < length; l++)
                    {
                        s = points[l];
                        if ((p.slopeTo(q) == p.slopeTo(r)) &&
                            (p.slopeTo(q) == p.slopeTo(s)))
                        {
                           addSlopePoints(p.slopeTo(q), p, q, r, s);
                        }
                    }
                }
            }
        }

        makeLineSegments();
    }

    private void addSlopePoints(double slope, Point p, Point q, Point r, Point s)
    {
        Set<Point> set = slopePoints.get(slope);
        if (set == null)
            set = new HashSet<Point>();

        set.add(p); set.add(q); set.add(r); set.add(s);

        StdOut.printf("Slope %f\n", slope);
        slopePoints.put(slope, set);
    }

    private void makeLineSegments()
    {
        lines = new LineSegment[slopePoints.size()];
        Collection<Set<Point>> pointSets = slopePoints.values();        

        StdOut.println(slopePoints);

        int i = 0;
        for (Set<Point> set : pointSets)
        {
            lines[i++] = makeLineSegmentFromSet(set);            
        }
    }

    private LineSegment makeLineSegmentFromSet(Set<Point> set)
    {
        Point min = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Point max = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (Point a : set)
        {
            if (min.compareTo(a) < 0)
                min = a;

            if (max.compareTo(a) > 0)
                max = a;
        }

        return new LineSegment(min, max);
    }

    // the number of line segments
    public int numberOfSegments()
    {
        return lines.length;
    }

    // the line segments
    public LineSegment[] segments()
    {
        return lines;
    }

    public static void main(String[] args) 
    {

        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) 
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) 
        {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) 
        {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
