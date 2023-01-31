package aed.collections;

import java.util.Arrays;
import java.util.Iterator;

public class QueueArray<Item> implements Iterable<Item> {

    Item[] itemArray;
    int length;
    int occupied;
    int head;
    int tail;

    @SuppressWarnings("unchecked")
    public QueueArray() {
        length = 50;
        itemArray = (Item[]) new Object[length];
        head = 0;
        tail = 0;
        occupied = 0;
    }

    @SuppressWarnings("unchecked")
    public void resize() {
        if (occupied == 0) { //reset all
            length = 50;
            itemArray = (Item[]) new Object[length];
            head = 0;
            tail = 0;
        } else {
            length +=occupied*1.5;
            itemArray = Arrays.copyOf(itemArray, length);
        }
    }

    public void enqueue(Item item) {
        itemArray[tail] = item;
        tail++;
        if (tail == length) {
            if (itemArray[0] == null)
                tail = 0;
            else
                resize();
        }
        occupied++;
    }

    public Item dequeue() {
        Item target = itemArray[head];
        itemArray[head] = null;
        head++;
        if (head == tail) {
            itemArray[0] = itemArray[head];
            itemArray[head] = null;
            head = tail = 0;
        }
        if (head >= length)
            head = 0;
        if (occupied > 0)
            occupied--;
        else if (occupied == 0)
            resize();
        return target;
    }

    public Item peek() {
        return itemArray[head];
    }

    public boolean isEmpty() {
        return occupied == 0;
    }

    public int size() {
        return occupied;
    }

    public QueueArray<Item> shallowCopy() {
        QueueArray<Item> newList = new QueueArray<>();
        newList.itemArray=this.itemArray.clone();
        newList.head=this.head;
        newList.length=this.length;
        newList.occupied=this.occupied;
        newList.tail=this.tail;
        return newList;
    }

    @Override
    public Iterator<Item> iterator() {
        Iterator<Item> currentI=new Iterator<Item>() {
            int iterator = head;

            @Override
            public boolean hasNext() {
                return itemArray[++iterator] != null;
            }


            @Override
            public Item next() {
                if (hasNext())
                    return itemArray[iterator];
                return null;
            }
        };
        return currentI;
    }


//
//    public void relengthArray() {
//        intArray = Arrays.copyOf(intArray, intArray.length + 1);
//    }
//
//    public void enqueue(Item item) {
//        if (length == 0) {
//            head = new Node();
//            head.item = item;
//            head.next = null;
//            head.prev = null;
//            tail = head;
//        } else {
//            Node nn = new Node();
//            nn.item = item;
//            nn.prev = tail;
//            tail.next = nn;
//            tail = nn;
//        }
//        length++;
//    }
//
//    public Item dequeue() {
//        if (length == 0)
//            return null;
//        length--;
//        if (length == 0) {
//            Item value = head.item;
//            head = tail = null;
//            return value;
//        }
//        Item value = head.item;
//        head = head.next;
//        head.prev = null;
//        return value;
//    }
//
//    public Item peek() {
//        if (length == 0)
//            return null;
//        return head.item;
//    }
//
//    public boolean isEmpty() {
//        return length == 0;
//    }
//
//    public QueueArray<Item> shallowCopy() {
//        return this;
//    }
//
//    public Integer length() {
//        return length;
//    }
//
//    @Override
//    public Iterator<Item> iterator() {
//        return new Iterator<Item>() {
//
//            Node current = head;
//
//            @Override
//            public boolean hasNext() {
//                return current != null;
//            }
//
//            @Override
//            public Item next() {
//                if (hasNext()) {
//                    Item data = current.item;
//                    current = current.next;
//                    return data;
//                }
//                return null;
//            }
//        };
//    }
//
//
//    public void printAll() {
//        Node nn = head;
//        while (nn != null) {
//            System.out.println(nn.item);
//            nn = nn.next;
//        }
//    }
}