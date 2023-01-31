package aed.collections;

import java.util.Comparator;
import java.util.Iterator;

public class SmartList<Item> implements Iterable<Item> {
    Node head;
    int size;
    float numberOfMTFCompares;
    float numberOfTransCompares;
    float numberOfMTFCalls;
    float numberOfTransCalls;

    public SmartList() {
        this.size = 0;
        head = null;
        numberOfMTFCompares = 0;
        numberOfTransCompares = 0;
        numberOfMTFCalls = 0;
        numberOfTransCalls = 0;
    }

    private class Node {
        Item item;
        int numberOfFetches;
        Node next;
    }

    public void add(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (head == null) {
            head = new Node();
            head.item = item;
            head.next = null;
            head.numberOfFetches = 0;
        } else {
            Node nn = new Node();
            nn.next = head;
            nn.item = item;
            head = nn;
            nn.numberOfFetches = 0;
        }
        size++;
    }

    public Item searchMTF(Item item) {
        Node nn = head;
        numberOfMTFCalls++;
        for (int i = 0; i < size; i++) {
            nn.numberOfFetches++;
            if (i == 0 && nn.item.equals(item)) { //it's the head! (We do nothing)
                numberOfMTFCompares += i + 1;
                return searchMTF(nn, item);
            } else if (i + 1 < size && nn.next.item.equals(item)) { // it's in between or the tail!
                numberOfMTFCompares += i + 2;
                return searchMTF(nn, item);
            }
            nn = nn.next;
        }
        return null;
    }

    public Item searchMTF(Node nn, Item item) {
        if (!nn.item.equals(item) || !nn.equals(head)) {
            Node target = nn.next;
            nn.next = nn.next.next;
            Node OldHead = head;
            head = target;
            head.next = OldHead;
        }
        return head.item;
    }

    public Item searchTrans(Item item) {
        Node nn = head;
        numberOfTransCalls++;
        for (int i = 0; i < size; i++) {
            nn.numberOfFetches++;
            if (i == 0) //it's the head! (We do nothing)
            {
                if (nn.item.equals(item)) {
                    numberOfTransCompares += 1;
                    return searchTrans(nn, item);

                } else if (i + 1 < size && nn.next.item.equals(item)) { //it's the item after the head
                    numberOfTransCompares += 2;
                    return searchTrans(nn, item);
                }
            }
            if (i + 2 < size && nn.next.next.item.equals(item)) { // it's in between or the tail!
                numberOfTransCompares += i + 3;
                return searchTrans(nn, item);
            }
            nn = nn.next;
        }
        return null;
    }

    public Item searchTrans(Node nn, Item item) {
        if (nn.item.equals(item) && nn.equals(head)) { //it's the head
            return head.item;
        } else if (nn.next.item.equals(item)) { //it's the item after the head
            nn.next.numberOfFetches++;
            head = nn.next;
            nn.next = head.next;
            head.next = nn;
            head.numberOfFetches++;
            return head.item;
        } else if (nn.next.next.item.equals(item)) { // it's in between or the tail!
            nn.next.numberOfFetches++;
            nn.next.next.numberOfFetches++;
            Node target = nn.next.next;
            Node preTarget = nn.next;
            preTarget.next = target.next;
            nn.next = target;
            target.next = preTarget;
            return target.item;
        }
        return null;
    }

    public Item search(Item item) {
        Node nn = head;
        for (int i = 0; i < size; i++) {
            nn.numberOfFetches++;
            if (i == 0 && nn.item.equals(item)) //It's the head
                return nn.item;
            else if (nn.item.equals(item)) {
                if (head.numberOfFetches < nn.numberOfFetches)
                    return searchMTF(nn, item);
                return nn.item;
            } else if (i + 2 < size && nn.next.next.item.equals(item) && nn.next.numberOfFetches < nn.next.next.numberOfFetches) { //Use searchTrans
                numberOfTransCompares += i + 3;
                return searchTrans(nn, item);
            }
            nn = nn.next;
        }
        return null;
    }

    public Item search(Item item, Comparator<Item> c) {
        Node nn = head;
        for (int i = 0; i < size; i++) {
            nn.numberOfFetches++;
            if (i == 0 && c.compare(nn.item, item) == 0) //It's the head
                return nn.item;
            else if (c.compare(nn.item, item) == 0) {
                if (head.numberOfFetches < nn.numberOfFetches)
                    return searchMTF(nn, nn.item);
                return nn.item;
            } else if (i + 2 < size && c.compare(nn.next.next.item, item) == 0) { //Use searchTrans
                numberOfTransCompares += i + 3;
                return searchTrans(nn, nn.next.next.item);
            }
            nn = nn.next;
        }
        return null;
    }

    public Item remove(Item item) {
        if (head == null || item == null) return null; //there is no items, and there's no null in the list sideways!

        Node nn = head;
        for (int i = 0; i < size; i++) {
            if (i + 1 < size && nn.next.item.equals(item)) { // it's in between or the tail!
                Item x = nn.next.item;
                nn.next = nn.next.next;
                size--;
                return x;
            } else if (i == 0 && nn.item.equals(item)) { //it's the head!
                Item x = head.item;
                if (head.next == null) head = null;
                else head = head.next;
                size--;
                return x;
            }
            nn = nn.next;
        }
        return null; //not found!
    }

    public float getAvgMTFCompares() {
        if (Float.isNaN(numberOfMTFCompares / numberOfMTFCalls))
            return 0;
        return numberOfMTFCompares / numberOfMTFCalls;
    }

    public float getAvgTransCompares() {
        if (Float.isNaN(numberOfTransCompares / numberOfTransCalls))
            return 0;
        return numberOfTransCompares / numberOfTransCalls;
    }

    public void clear() {
        head = null;
        size = 0;
        numberOfMTFCompares = 0;
        numberOfTransCompares = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Object[] toArray() {
        Object[] ar = new Object[size];
        Node nn = head;
        for (int i = 0; i < size; i++) {
            ar[i] = nn.item;
            nn = nn.next;
        }
        return ar;
    }

    public SmartList<Item> shallowCopy() {
        SmartList<Item> newList = new SmartList<>();
        Node nn=head;
        for(int i=0;i<size;i++){
            newList.add(nn.item);
            nn=nn.next;
        }
        newList.numberOfMTFCompares=this.numberOfMTFCompares;
        newList.numberOfTransCompares=this.numberOfTransCompares;
        newList.numberOfMTFCalls=this.numberOfMTFCalls;
        newList.numberOfTransCalls=this.numberOfTransCalls;
        return newList;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (hasNext()) {
                    Item data = current.item;
                    current = current.next;
                    return data;
                }
                return null;
            }
        };
    }

    public void printAll() {
        Node nn = head;
        while (nn != null) {
            System.out.println(nn.item);
            nn = nn.next;
        }
    }

    void main(String[] args) {
    }
}