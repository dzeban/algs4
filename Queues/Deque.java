/*----------------------------------------------------------------
 *  Author:        Alex Dzyoba
 *  Written:       2015-09-22
 *  Last updated:  2015-09-22
 *
 *  Compilation:   javac-algs4 Deque.java
 *  Execution:     java-algs4 Deque
 *
 *  Implementation of generic double ended queue data structure (deque)
 *  Allows you to:
 *
 *  - create an empty deque
 *  - add an element to the head or tail
 *  - remove an element from the head or tail
 *  - check emptyness and size
 *  - get an iterator over deque
 *
 *----------------------------------------------------------------*/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * Double ended queue (a.k.a. deque) implementation.
 *
 * Underlying data structure is a doubly-linked list, e.g. list with pointers to
 * next and previous element.
 *
 * Every operation is a constant worst-case time.
 */
public class Deque<Item> implements Iterable<Item> 
{
    /** Inner class representing doubly-linked list node */
    private class Node
    {
        Item item;
        Node next, prev;

        public Node(Item item)
        { 
            this.item = item; 
            this.next = this.prev = null;
        }
    }

    /** Pointers to head and tail of deque */
    private Node head, tail;

    /** Size of the deque */
    private int size;

    /** Construct an empty deque */
    public Deque()
    {
        head = tail = null;
        size = 0;
    }

    /** Is the deque emptry? */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /** Return the number of items on the deque */
    public int size()
    {
        return size;
    }

    /** Add the item to the front */
    public void addFirst(Item item)
    {
        if (item == null)
            throw new NullPointerException();

        Node newHead = new Node(item);
        newHead.next = head;
        if (head != null)
            head.prev = newHead;
        head = newHead;
        
        // Adding first element to the beginning
        // updates tail to point to the head
        if (tail == null)
            tail = head;

        size++;
    }

    /** Add the item to the end */
    public void addLast(Item item)
    {
        if (item == null)
            throw new NullPointerException();

        Node newTail = new Node(item);
        if (tail != null)
            tail.next = newTail;
        newTail.prev = tail;
        tail = newTail;

        // Adding first element to the end 
        // updates head to point to the tail
        if (head == null)
            head = tail;

        size++;
    }

    /** Remove and return the item from the front */
    public Item removeFirst()
    {
        if (isEmpty())
            throw new NoSuchElementException();

        Item item = head.item;

        Node newHead = head.next;
        head.next = null;
        if (newHead != null)
            newHead.prev = null;
        head = newHead;

        // Removing last element updates tail
        if (head == null)
            tail = head;

        size--;
        return item;
    }

    /** Remove and return the item from the end */
    public Item removeLast()
    {
        if (isEmpty())
            throw new NoSuchElementException();

        Item item = tail.item;

        Node newTail = tail.prev;
        tail.prev = null;
        if (newTail != null)
            newTail.next = null;
        tail = newTail;

        // Removing last element updates head
        if (tail == null)
            head = tail;

        size--;
        return item;
    }

    /** Return an iterator over items in order from front to end */
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    /** Inner class representing deque iterator */
    private class DequeIterator implements Iterator<Item>
    {
        private Node current = head;

        public boolean hasNext()
        {
            return current != null;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        public Item next()
        {
            if (current == null)
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)
    {
        Deque<Integer> deque = new Deque<Integer>();

        deque.addLast(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.removeLast();
        deque.addLast(4);
        deque.addLast(5);
        deque.removeFirst();

        Iterator<Integer> it = deque.iterator();
        while (it.hasNext())
        {
            int n = it.next();
            StdOut.println(n);
        }

        if (deque.isEmpty() == false)
            StdOut.printf("Size is %d\n", deque.size());
        else
            StdOut.println("Empty");
    }
}
