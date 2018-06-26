import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] items;
    private int size;
    
    public RandomizedQueue() {
        this.items = (Item[]) new Object[2];
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public int size() {
        return this.size;
    }
    
    public void enqueue(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException("Invalid value for item");
        }
        if (this.size == this.items.length) {
            this.grow();
        } 
        this.items[this.size++] = item;
    }
    
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
        
        int i = StdRandom.uniform(this.size);
        Item item = this.items[i];
        this.size--;
        
        this.items[i] = this.items[this.size];
        this.items[this.size] = null;
        
        if (this.size > 0 && this.items.length/4 == this.size) {
            this.shrink();
        }
        return item;
    }
    
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Queue is empty");
        }
        
        Item item = null;
        while (item == null) {
            int i = StdRandom.uniform(this.size);
            item = this.items[i];
        }
        return item;
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }
    
//    public void printItems() {
//        for (int i = 0; i < items.length; i++) {
//            StdOut.println(items[i]);
//        }
//        StdOut.println("-----");
//    }
    
    private void grow() {
        this.items = java.util.Arrays.copyOf(this.items, 2*this.items.length);
    }
    
    private void shrink() {
        Item[] temp = (Item[]) new Object[this.items.length/2];
        int i = 0;
        for (Item item : this.items) {
            if (item != null) {
                temp[i++] = item;
            }
        }
        this.items = temp;
    }
    
    private class QueueIterator implements Iterator<Item> {
        
        private final int[] indices;
        private int n;
        
        public QueueIterator() {
            this.n = 0;
            this.indices = new int[size];
            for (int i = 0; i < size; i++) {
                this.indices[i] = i;
            }
            StdRandom.shuffle(this.indices);
        }
                    
        @Override
        public boolean hasNext() {
            return this.n < size;
        }
        
        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new java.util.NoSuchElementException("Iterator exhausted");
            }
            
            return items[this.indices[this.n++]];
        }
        
        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException("Operation not supported");
        }
    };
    
    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("Foo");
        queue.enqueue("Bar");
        queue.enqueue("Baz");
        queue.enqueue("Matt");
        queue.enqueue("Tom");
//        queue.printItems();
        
        StdOut.println(queue.dequeue());
        StdOut.println();
        
        StdOut.println(queue.dequeue());
        StdOut.println();
        
        StdOut.println(queue.dequeue());
        StdOut.println();
        
        StdOut.println(queue.dequeue());
        StdOut.println();
        
        StdOut.println(queue.sample());
        StdOut.println();
        
//        queue.printItems();
    }
    
}
