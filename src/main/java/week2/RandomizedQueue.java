package week2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    // Index pointing the the end of the item array
    private int index = -1;
    private Item[] items = (Item[]) new Object[10];

    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // is the queue empty?
    public boolean isEmpty() {
        return index < 0;
    }

    // return the number of items on the queue
    public int size() {
        return index + 1;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException();
        // If we are full
        if (index + 1 == items.length)
            doubleSize();
        items[++index] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (index < 0)
            throw new NoSuchElementException();
        int i = randomIndex();
        // Extract
        Item item = items[i];

        // If not picked from the end move the last one to  position.\
        // of the dequed item
        if (i != index)
            items[i] = items[index];
        items[index] = null; // Remove ref
        index--;

        downsizeIfNeeded();
        return item;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (index < 0)
            throw new NoSuchElementException();
        return items[randomIndex()];
    }

    private int randomIndex() {
        return StdRandom.uniform(index + 1);
    }

    private void doubleSize() {
        Item[] doubled = (Item[]) new Object[items.length * 2];

        for (int i = 0; i < items.length; i++) {
            doubled[i] = items[i];
        }

        items = doubled;
    }

    private void downsizeIfNeeded() {
        if (items.length < 10)
            return;

        double d = ((double) index + 1) / items.length;
        if (d > 0.25)
            return;

        int newSize = items.length / 2;
        Item[] newArray = (Item[]) new Object[newSize];

        for (int i = 0; i <= index; i++) {
            newArray[i] = items[i];
        }

        items = newArray;
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new MyIterator<Item>(items, index + 1);
    }

    private static class MyIterator<E> implements Iterator<E> {

        private int position;
        private final E[] items;

        public MyIterator(E[] items, int size) {
            this.items = (E[]) new Object[size];
            position = size - 1;

            items = Arrays.copyOf(items, items.length);

            // Build array of items at random
            for (int i = 0; i < size; i++) {
                int last = size - i - 1;
                int ranIndex = StdRandom.uniform(last + 1);
                this.items[i] = items[ranIndex];

                // Move the last to the extracted one position
                if (last != ranIndex)
                    items[ranIndex] = items[last];
            }
        }

        @Override
        public boolean hasNext() {
            return position > -1;
        }

        @Override
        public E next() {
            if (position < 0)
                throw new NoSuchElementException();
            E res = items[position];
            items[position] = null; // Remove ref
            position--;
            return res;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        int[] ints = {5, 88, 33, 346, 75, 99};
        RandomizedQueue<Integer> d = new RandomizedQueue<>();

        for (int i = 0; i < 10_000; i++) {
            System.out.println(i);
            d.enqueue(i);
            d.sample();
            d.isEmpty();

            d.dequeue();
            d.isEmpty();
        }
/*        for (int i = 0; i < 10_000; i++) {
            d.sample();
            d.dequeue();
            d.isEmpty();
        }*/

/*        for (int i = 0; i < ints.length; i++) {
            System.out.println(d.dequeue());
        }
        System.out.println(d.isEmpty());*/

/*        for (int i : d)
            System.out.println(i);*/
    }

}
