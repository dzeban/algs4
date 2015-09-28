/*----------------------------------------------------------------
 *  Author:        Alex Dzyoba
 *  Written:       2015-09-23
 *  Last updated:  2015-09-23
 *
 *  Compilation:   javac-algs4 Subset.java
 *  Execution:     echo [<Strings>] | java-algs4 Subset <Subset size>
 *
 *  Read strings from input and return random subset of size given 
 *  as an argument
 *----------------------------------------------------------------*/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

public class Subset
{
    public static void main(String[] args)
    {
        int N = Integer.parseInt(args[0]);

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty())
        {
            String s = StdIn.readString();
            rq.enqueue(s);                        
        }

        while (N-- != 0)
        {
            StdOut.println(rq.dequeue());
        }
    }
}
