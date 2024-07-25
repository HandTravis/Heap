package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    static final int START_INDEX = 0;
    List<PriorityNode<T>> items;
    Map<T, Integer> sortedItems;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        sortedItems = new HashMap<>();
    }

    private void swap(int a, int b) {
        PriorityNode<T> tempNode = items.get(a);
        sortedItems.put(items.get(a).getItem(), b);
        sortedItems.put(items.get(b).getItem(), a);
        items.set(a, items.get(b));
        items.set(b, tempNode);
    }

    @Override
    public void add(T item, double priority) {
        if (sortedItems.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        PriorityNode<T> newNode = new PriorityNode<>(item, priority);
        items.add(newNode);
        sortedItems.put(item, items.size() - 1);
        percolateUp(items.size() - 1);

    }

    @Override
    public boolean contains(T item) {
        return sortedItems.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items.get(0).getItem();
    }

    @Override
    public T removeMin() {
        if (items.isEmpty()) {
            throw new NoSuchElementException();
        }
        T item = items.set(0, items.get(size() - 1)).getItem();
        items.remove(size() - 1);
        sortedItems.remove(item);
        percolateDown(0);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!sortedItems.containsKey(item)) {
            throw new NoSuchElementException();
        }
        if (sortedItems.containsKey(item)) {
            double oldPri = items.get(sortedItems.get(item)).getPriority();
            items.get(sortedItems.get(item)).setPriority(priority);
            if (oldPri > priority) {
                percolateUp(sortedItems.get(item));
            } else if (priority > oldPri) {
                percolateDown(sortedItems.get(item));
            }
        }
    }

    @Override
    public int size() { return items.size(); }

    public boolean isEmpty() { return size() == 0; }

    private int getParent(int pos) { return (pos - 1) / 2; }

    private int getLeftChild(int pos) { return 2 * pos + 1; }

    private int getRightChild(int pos) { return 2 * pos + 2; }



    private void percolateUp(int cur) {
        if (items.get(cur).getPriority() < items.get(getParent(cur)).getPriority()) {
            sortedItems.put(items.get(cur).getItem(), getParent(cur));
            swap(cur, getParent(cur));
            percolateUp(getParent(cur));
        }
    }

    private void percolateDown(int cur) {
        if (!isLeafNode(cur)) {
            if (getRightChild(cur) < size() && items.get(cur).getPriority() >
                 items.get(getRightChild(cur)).getPriority() && items.get(getRightChild(cur)).getPriority() <
                                                                    items.get(getLeftChild(cur)).getPriority()) {
                sortedItems.put(items.get(cur).getItem(), getRightChild(cur));
                swap(cur, getRightChild(cur));
                percolateDown(getRightChild(cur));
            } else if (getLeftChild(cur) < size()
                    && items.get(cur).getPriority() > items.get(getLeftChild(cur)).getPriority()) {
                sortedItems.put(items.get(cur).getItem(), getLeftChild(cur));
                swap(cur, getLeftChild(cur));
                percolateDown(getLeftChild(cur));
            }
        }
    }

    private boolean isLeafNode(int index) {
        return (getLeftChild(index) >= size() && getRightChild(index) >= size());
    }
}
