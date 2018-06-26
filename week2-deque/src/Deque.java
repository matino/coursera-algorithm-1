import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    private Node head;
    private Node tail;
    private int size;
    
    private class Node {
        Item item;
        Node next;
        Node prev;
        
        public Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
            
        private Node current = head;
        
        @Override
        public boolean hasNext() {
            return this.current != null;
        }
        
        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new java.util.NoSuchElementException("Iterator exhausted");
            }
            
            Item item = this.current.item;
            this.current = this.current.next;
            return item;
        }
        
        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation not supported");
        }
    };
    
    public Deque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    public boolean isEmpty() { 
        return this.size == 0;
    }
    
    public int size() { 
        return this.size;
    }
    
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Invalid value for item");
        }
        
        Node first = new Node(item);
        
        if (isEmpty()) {
            this.head = first;
            this.tail = first;
        }
        else {
            Node current = this.head;
            current.prev = first;
            first.next = current;
            this.head = first;
        }
        this.size++;
    }
    
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Invalid value for item");
        }
        
        Node last = new Node(item);
        
        if (isEmpty()) {
            this.head = last;
            this.tail = last;
        }
        else {
            Node current = this.tail;
            current.next = last;
            last.prev = current;
            this.tail = last;
        }
        this.size++;
    }
    
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Can't remove from empty deque");
        }
        Item item = this.head.item;
        this.size--;
        
        if (isEmpty()) {
            this.head = null;
            this.tail = null;
        }
        else {
            this.head = this.head.next;
            this.head.prev = null;
        }
        
        return item;
    }
    
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Can't remove from empty deque");
        }
        Item item = this.tail.item;
        this.size--;
        
        if (isEmpty()) {
            this.head = null;
            this.tail = null;
        }
        else {
            this.tail = this.tail.prev;
            this.tail.next = null;
        }
        
        return item;
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.size());
        deque.addFirst(2);
        StdOut.println(deque.removeLast());
        deque.addFirst(4);
        deque.addLast(5);
        for (Integer s : deque)
            StdOut.println(s);
    }
}
