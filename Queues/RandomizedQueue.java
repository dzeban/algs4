/*----------------------------------------------------------------
 *  Author:        Alex Dzyoba
 *  Written:       2015-09-22
 *  Last updated:  2015-09-23
 *
 *  Compilation:   javac-algs4 RandomizedQueue.java
 *  Execution:     java-algs4 RandomizedQueue
 *
 *  Implementation of generic randomized queue. After enqueing some elements you
 *  can deque them in random order.
 *----------------------------------------------------------------*/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> 
{
    /** Initial items array size */
    private static final int INIT_SIZE = 4;

    /** Array of items */
    private Item[] array;

    /** Number of items in array */
    private int items;

    /** Size of array, usually larger than elements */
    private int size;

    /** Construct an empty randomized queue */
    public RandomizedQueue()
    {
        // Ugly cast
        array = (Item[]) new Object[INIT_SIZE];        
        size = INIT_SIZE;

        items = 0;
    }

    /** Is the queue empty? */
    public boolean isEmpty()
    {
        return items == 0;
    }

    /** Return the number of items on the queue */
    public int size()
    {
        return items;
    }

    /** Resize array to the given size */
    private void resize(int newSize)
    {
        Item[] newArray = (Item[]) new Object[newSize];
        
        for (int i = 0; i < items; i++)
            newArray[i] = array[i];
        
        array = newArray;
        size = newSize;
    }

    /** Grow array doubling size */
    private void growArray()
    {
        int newSize = size * 2;
        resize(newSize);
    }

    /** Shrink array half size */
    private void shrinkArray()
    {
        int newSize = size / 2;
        resize(newSize);
    }

    /** Add the item */
    public void enqueue(Item item)
    {
        if (item == null)
            throw new NullPointerException("Trying to add null element");

        if (items >= size)
            growArray();

        array[items] = item;        
        items++;
    }

    /** Swap last element with given */
    private void swapLast(int i)
    {
        int last = items - 1;

        if (i != last)
        {
            array[i] = array[last];
            array[last] = null;
        }
    }

    /** Remove and return a random item */
    public Item dequeue()
    {
        if (isEmpty())
            throw new NoSuchElementException("Queue is empty");

        int n = StdRandom.uniform(items);
        Item item = array[n];

        array[n] = null;

        // If we remove element from array we must
        // rearrange array to not contain any holes.
        // We could shift all the elements, but we 
        // don't care about the order of items, so we
        // just swap last element into the hole
        swapLast(n);

        // Decrement only after swapping
        items--;

        // Don't waste memory and shrink 
        // array when it's one-quarter full. 
        if (items > 0 && items <= size/4)
            shrinkArray();

        return item;
    }

    /** Print whole array */
    private void printArray()
    {
        StdOut.print("Array: ");
        for (int i = 0; i < size; i++)
            StdOut.print(array[i] + " ");

        StdOut.println();
    }

    /** Return (but do not remove) a random item */
    public Item sample()
    {
        int n = StdRandom.uniform(items);
        return array[n];
    }

    /** Return an independent iterator over items in random order */
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    /** Inner iterator class */
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private Item[] iterArray;
        private int current;

        public RandomizedQueueIterator()
        {
            iterArray = (Item[]) new Object[items];
            for (int i = 0; i < items; i++)
                iterArray[i] = array[i];

            StdRandom.shuffle(iterArray);
            current = 0;
        }

        public boolean hasNext()
        { return current != iterArray.length; }

        public void remove()
        { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (current == iterArray.length)
                throw new NoSuchElementException();

            Item item = iterArray[current];
            current++;
            return item;
        }
    }

    /** Unit testing */
    public static void main(String[] args)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        rq.enqueue(6);
        rq.enqueue(7);

        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
    }
}
