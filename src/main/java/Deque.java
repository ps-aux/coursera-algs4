import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException();

        Node<Item> n = new Node<>(item);
        if (size == 0) {
            first = n;
            last = n;

        } else {
            first.previous = n;
            n.next = first;
        }
        first = n;
        size++;
    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException();


        Node<Item> n = new Node<>(item);
        if (size == 0) {
            first = n;
            last = n;

        } else {
            last.next = n;
            n.previous = last;
        }
        last = n;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0)
            throw new NoSuchElementException();
        Node<Item> res = first;

        if (size > 1) {
            first = res.next;
            first.previous = null; // Remove ref
        } else {
            first = null;
            last = null;
        }

        size--;
        return res.val;
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (size == 0)
            throw new NoSuchElementException();

        Node<Item> res = last;

        if (size > 1) {
            last = res.previous;
            last.next = null; // Remove ref
        } else {
            last = null;
            first = null;
        }

        size--;
        return res.val;
    }

    @Override
    public Iterator<Item> iterator() {
        return new MyIterator<Item>(first);
    }

    // iterator over items in order from front to end
    private static class MyIterator<E> implements Iterator<E> {

        private Node<E> next;

        MyIterator(Node<E> next) {
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            if (next == null)
                throw new NoSuchElementException();

            Node<E> res = next;
            next = res.next;

            return res.val;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class Node<E> {

        private Node<E> next;
        private Node<E> previous;
        private final E val;

        Node(E e) {
            this.val = e;
        }

    }

    public static void main(String[] args) {
        int[] ints = {5, 88, 33, 346, 75, 99};
        Deque<Integer> deque = new Deque<>();

/*
        for (int i : ints) {
            d.addFirst(i);
        }
*/

        deque.addFirst(1);
        printSize(deque);
        deque.addFirst(2);
        printSize(deque);
        deque.removeFirst();
        printSize(deque);
        deque.removeLast();
        printSize(deque);


/*        for (int i = 0; i < ints.length; i++) {
            System.out.println(d.removeLast());
        }*/

/*        for (int i : deque)
            System.out.println(i);*/
    }

    private static void printSize(Deque<?> deque) {
        Iterator<?> it = deque.iterator();
        int i = 0;
        while (it.hasNext()) {
            it.next();
            i++;
        }
        System.out.print(i + " : ");
        System.out.println(deque.size());
    }

}
