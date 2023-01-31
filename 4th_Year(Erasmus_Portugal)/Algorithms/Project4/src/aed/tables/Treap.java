package aed.tables;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

//this is defined as an outer class because I want to save 8 bytes for each node.
class Node<Key extends Comparable<Key>, Value> {
    Key key;
    int priority;
    Value value;
    Node<Key, Value> left;
    Node<Key, Value> right;
    int size;

    Node(Key k, Value v, int size, int priority) {
        this.key = k;
        this.value = v;
        this.size = size;
        this.priority = priority;
    }

    public String toString() {
        return "[k:" + this.key + " v:" + this.value + " p:" + this.priority + " s:" + this.size + "]";
    }
}

public class Treap<Key extends Comparable<Key>, Value> implements IOrderedSymbolTable<Key, Value> {

    private Node<Key, Value> root;
    private final Random randomGenerator;

    private Treap(Node<Key, Value> root, Random r) {
        this.root = root;
        this.randomGenerator = r;
    }

    public Treap() {
        this.randomGenerator = new Random();
        this.root = null;
    }

    public Treap(Random r) {
        this.randomGenerator = r;
        this.root = null;
    }

    private int size(Node<Key, Value> n) {
        if (n == null) return 0;
        else return n.size;
    }

    public int size() {
        return size(this.root);
    }

    public Value get(Key k) {
        return get(this.root, k);
    }

    private Value get(Node<Key, Value> n, Key k) {
        if (n == null) return null;
        int cmp = k.compareTo(n.key);
        if (cmp < 0) return get(n.left, k);
        else if (cmp > 0) return get(n.right, k);
        else return n.value;
    }

    public boolean contains(Key k) {
        return get(this.root, k) != null;
    }

    @Override
    public boolean isEmpty(Key k) {
        return this.root == null;
    }

    public void put(Key k, Value v) {
        this.root = put(this.root, k, v, false);
    }

    private Node<Key, Value> put(Node<Key, Value> n, Key k, Value v, boolean isSplit) {
        if (n == null)
            return new Node<Key, Value>(k, v, 1, isSplit ? Integer.MAX_VALUE : this.randomGenerator.nextInt());

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = put(n.left, k, v, isSplit);
            if (n.left.priority > n.priority) {
                n = rotateRight(n);
            }
        } else if (cmp > 0) {
            n.right = put(n.right, k, v, isSplit);
            if (n.right.priority > n.priority) {
                n = rotateLeft(n);
            }
        } else {
            n.value = v;
            if (isSplit) n.priority = Integer.MAX_VALUE;
        }

        //update the node size
        n.size = size(n.left) + size(n.right) + 1;

        return n;
    }

    //Method that performs a Left Rotation. A left rotation is performed by a parent with its right child.
    //The right child becomes the new parent, and the parent becomes the left child of the new parent
    private Node<Key, Value> rotateLeft(Node<Key, Value> parentN) {
        Node<Key, Value> rightN = parentN.right;
        parentN.right = rightN.left;
        rightN.left = parentN;

        parentN.size = size(parentN.left) + size(parentN.right) + 1;

        return rightN;
    }

    //Method that performs a right rotation. A right rotation is performed by a parent with its left child.
    //The left child becomes the new parent, and the parent becomes the right child of the new parent
    private Node<Key, Value> rotateRight(Node<Key, Value> parentN) {
        Node<Key, Value> leftN = parentN.left;
        parentN.left = leftN.right;
        leftN.right = parentN;

        parentN.size = size(parentN.left) + size(parentN.right) + 1;

        return leftN;
    }

    public void delete(Key k) {
        if (this.root != null) this.root = delete(this.root, k);
    }

    private Node<Key, Value> delete(Node<Key, Value> n, Key k) {
        //reached a null node, meaning the key is not there, nothing to delete
        if (n == null) return null;

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = delete(n.left, k);
        } else if (cmp > 0) {
            n.right = delete(n.right, k);
        } else {
            //found the node to be deleted
            //simplest case, the node is already a leaf, just return null so that the node is deleted
            if (n.left == null && n.right == null) return null;
            //if not leaf, we need to move it to a leaf
            n.priority = Integer.MIN_VALUE;

            n = moveDown(n);
        }

        //update the node size
        assert n != null;
        n.size = size(n.left) + size(n.right) + 1;

        return n;
    }

    private Node<Key, Value> moveDown(Node<Key, Value> n) {
        //we finally reached a leaf, return null so that the node is deleted
        if (n.left == null && n.right == null) return null;

        //get max from children
        if (n.left != null) {
            if (n.right != null && n.right.priority > n.left.priority) {
                n = rotateLeft(n);
                n.left = moveDown(n.left);
            } else {
                n = rotateRight(n);
                n.right = moveDown(n.right);
            }
        } else {
            n = rotateLeft(n);
            n.left = moveDown(n.left);
        }

        n.size = size(n.left) + size(n.right) + 1;
        return n;
    }

    //delete from the treap all keys(and values) which are <= than the received Key k
    //this method has a time complexity of ~log n, where n is the number of keys stored
    //it does not depend on the number of keys that will be removed
    public void deleteLesserEqual(Key k) {
        if (this.root == null) return;

        //split the treap by the received key, and keep only the right subtree
        //yes, it is as simple as this, the put with a split method does most of the work
        this.root = put(this.root, k, null, true);
        this.root = this.root.right;
    }

    //delete from the treap all keys(and values) which are < than the received Key k
    //this method has a time complexity of ~2 log n, where n is the number of keys stored
    //it does not depend on the number of keys that will be removed
    public void deleteLesser(Key k) {
        if (this.root == null) return;

        Node<Key, Value> previous = previous(this.root, k);
        if (previous == null) return; //nothing to delete

        deleteLesserEqual(previous.key);
    }

    //delete from the treap all keys(and values) which are >= than the received Key k
    //this method has a time complexity of O(log n), where n is the number of keys stored
    //it does not depend on the number of keys that will be removed
    public void deleteGreaterEqual(Key k) {
        if (this.root == null) return;

        //split the treap by the received key, and keep only the left subtree
        //yes, it is as simple as this, the put with a split method does most of the work
        this.root = put(this.root, k, null, true);
        this.root = this.root.left;
    }

    public Treap<Key, Value>[] split(Key k) {
        @SuppressWarnings("unchecked")
        Treap<Key, Value>[] result = (Treap<Key, Value>[]) new Treap[2];

        if (this.root == null) {
            result[0] = new Treap<Key, Value>(null, this.randomGenerator);
            result[1] = new Treap<Key, Value>(null, this.randomGenerator);
            return result;
        }

        Treap<Key, Value> clone = this;//this.shallowCopy();

        clone.put(clone.root, k, null, true);

        result[0] = new Treap<Key, Value>(clone.root.left, clone.randomGenerator);
        result[1] = new Treap<Key, Value>(clone.root.right, clone.randomGenerator);


        return result;
    }

    public Key min() {
        if (this.root == null) return null;
        return min(this.root).key;
    }

    public Value minValue() {
        if (this.root == null) return null;
        return min(this.root).value;
    }

    private Node<Key, Value> min(Node<Key, Value> n) {
        if (n.left == null) return n;
        return min(n.left);
    }

    public Key max() {
        if (this.root == null) return null;
        return max(this.root).key;
    }

    public Value maxValue() {
        if (this.root == null) return null;
        return max(this.root).value;
    }

    private Node<Key, Value> max(Node<Key, Value> n) {
        if (n.right == null) return n;
        return max(n.right);
    }

    @Override
    public Key floor(Key k) {
        Node<Key, Value> n = floor(this.root, k);
        if (n != null) return n.key;
        else return null;
    }

    public Value floorValue(Key k) {
        Node<Key, Value> n = floor(this.root, k);
        if (n != null) return n.value;
        else return null;
    }

    private Node<Key, Value> floor(Node<Key, Value> n, Key k) {
        if (n == null) return null;

        int cmp = n.key.compareTo(k);

        if (cmp == 0) return n;
        if (cmp > 0) return floor(n.left, k);

        Node<Key, Value> otherOption = floor(n.right, k);
        if (otherOption != null) return otherOption;

        return n;
    }

    private Node<Key, Value> previous(Node<Key, Value> n, Key k) {
        if (n == null) return null;

        int cmp = n.key.compareTo(k);

        if (cmp >= 0) return previous(n.left, k);

        Node<Key, Value> otherOption = previous(n.right, k);
        if (otherOption != null) return otherOption;

        return n;
    }


    public Key ceiling(Key k) {
        Node<Key, Value> n = ceiling(this.root, k);
        if (n != null) return n.key;
        else return null;
    }

    public Value ceilingValue(Key k) {
        Node<Key, Value> n = ceiling(this.root, k);
        if (n != null) return n.value;
        else return null;
    }

    private Node<Key, Value> ceiling(Node<Key, Value> n, Key k) {
        if (n == null) return null;

        int cmp = n.key.compareTo(k);

        if (cmp == 0) return n;
        if (cmp < 0) return ceiling(n.right, k);

        Node<Key, Value> otherOption = ceiling(n.left, k);
        if (otherOption != null) return otherOption;

        return n;
    }

    private Node<Key, Value> next(Node<Key, Value> n, Key k) {
        if (n == null) return null;

        int cmp = n.key.compareTo(k);

        if (cmp <= 0) return next(n.right, k);

        Node<Key, Value> otherOption = next(n.left, k);
        if (otherOption != null) return otherOption;

        return n;
    }


    public void deleteMin() {
        if (this.root != null) {
            this.root = deleteMin(this.root);
        }
    }

    private Node<Key, Value> deleteMin(Node<Key, Value> n) {
        Node<Key, Value> newParent;
        if (n.left == null) {
            if (n.right == null) return null;

            newParent = rotateLeft(n);
            newParent.left = n.right;
            n = newParent;
        } else {
            n.left = deleteMin(n.left);
        }

        n.size = size(n.left) + size(n.right) + 1;
        return n;
    }


    private Node<Key, Value> deleteMax(Node<Key, Value> n) {
        Node<Key, Value> newParent;
        if (n.right == null) {
            if (n.left == null) return null;

            newParent = rotateRight(n);
            newParent.right = n.left;
            n = newParent;
        } else {
            n.right = deleteMax(n.right);
        }

        n.size = size(n.left) + size(n.right) + 1;
        return n;
    }

    public void deleteMax() {
        if (this.root != null) {
            this.root = deleteMax(this.root);
        }
    }

    public int rank(Key k) {
        return rank(this.root, k);
    }

    private int rank(Node<Key, Value> n, Key k) {
        if (n == null) return 0;
        int cmp = k.compareTo(n.key);
        if (cmp == 0) return size(n.left);
        if (cmp < 0) return rank(n.left, k);
        return 1 + size(n.left) + rank(n.right, k);
    }

    private int size(Node<Key, Value> n, Key min, Key max) {
        int total = 0;

        if (n == null) return total;

        int cmpMin = n.key.compareTo(min);
        int cmpMax = n.key.compareTo(max);

        if (cmpMin > 0) total += size(n.left, min, max);
        if (cmpMin >= 0 && cmpMax <= 0) total++;
        if (cmpMax < 0) total += size(n.right, min, max);

        return total;
    }

    public int size(Key min, Key max) {
        return size(this.root, min, max);
    }

    private Key select(Node<Key, Value> n, int position) {
        if (n == null) return null;

        int leftSize = size(n.left);

        if (leftSize == position) return n.key;

        if (leftSize > position) return select(n.left, position);
        else return select(n.right, position - leftSize - 1);
    }

    public Key select(int n) {
        return select(this.root, n);
    }

    private void gatherKeys(Node<Key, Value> n, Queue<Key> queue) {
        if (n == null) return;

        gatherKeys(n.left, queue);
        queue.add(n.key);
        gatherKeys(n.right, queue);
    }

    private void gatherValues(Node<Key, Value> n, Queue<Value> queue) {
        if (n == null) return;

        gatherValues(n.left, queue);
        queue.add(n.value);
        gatherValues(n.right, queue);
    }

    private void gatherPriorities(Node<Key, Value> n, Queue<Integer> queue) {
        if (n == null) return;

        gatherPriorities(n.left, queue);
        queue.add(n.priority);
        gatherPriorities(n.right, queue);
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new ArrayDeque<Key>();
        gatherKeys(this.root, queue);
        return queue;
    }

    public Iterable<Value> values() {
        Queue<Value> queue = new ArrayDeque<Value>();
        gatherValues(this.root, queue);
        return queue;
    }

    public Iterable<Integer> priorities() {
        Queue<Integer> queue = new ArrayDeque<Integer>();
        gatherPriorities(this.root, queue);
        return queue;
    }

    private void gatherKeys(Node<Key, Value> n, Key min, Key max, Queue<Key> queue) {
        if (n == null) return;

        int cmpMin = n.key.compareTo(min);
        int cmpMax = n.key.compareTo(max);

        if (cmpMin > 0) gatherKeys(n.left, min, max, queue);
        if (cmpMin >= 0 && cmpMax <= 0) queue.add(n.key);
        if (cmpMax < 0) gatherKeys(n.right, min, max, queue);
    }

    private void gatherValues(Node<Key, Value> n, Key min, Key max, Queue<Value> queue) {
        if (n == null) return;

        int cmpMin = n.key.compareTo(min);
        int cmpMax = n.key.compareTo(max);

        if (cmpMin > 0) gatherValues(n.left, min, max, queue);
        if (cmpMin >= 0 && cmpMax <= 0) queue.add(n.value);
        if (cmpMax < 0) gatherValues(n.right, min, max, queue);
    }

    public Iterable<Key> keys(Key min, Key max) {
        Queue<Key> queue = new ArrayDeque<Key>();
        gatherKeys(this.root, min, max, queue);
        return queue;
    }

    public Iterable<Value> values(Key min, Key max) {
        Queue<Value> queue = new ArrayDeque<Value>();
        gatherValues(this.root, min, max, queue);
        return queue;
    }

    private Node<Key, Value> clone(Node<Key, Value> n) {
        if (n == null) return null;
        Node<Key, Value> newNode = new Node<Key, Value>(n.key, n.value, n.size, n.priority);
        newNode.left = clone(n.left);
        newNode.right = clone(n.right);
        return newNode;
    }

    public Treap<Key, Value> shallowCopy() {
        Treap<Key, Value> result = new Treap<Key, Value>();
        result.root = clone(this.root);
        return result;
    }

    private void fillHeapArray(Node<Key, Value> n, Object[] a, int position) {
        if (n == null) return;

        if (position < a.length) {
            a[position] = n;
            fillHeapArray(n.left, a, 2 * position);
            fillHeapArray(n.right, a, 2 * position + 1);
        }
    }

    private Object[] getHeapArray() {
        //we only store the first 31 elements (position 0 is not used, so we need a size of 32), to print the first 5 rows of the treap
        Object[] a = new Object[32];
        fillHeapArray(this.root, a, 1);
        return a;
    }

    private void printCentered(String line) {
        //assuming 120 characters width for a line
        int padding = (120 - line.length()) / 2;
        if (padding < 0) padding = 0;
        String paddingString = "";
        for (int i = 0; i < padding; i++) {
            paddingString += " ";
        }

        System.out.println(paddingString + line);
    }

    public void printTreapBeginning() {
        Object[] heap = getHeapArray();
        int size = size(this.root);
        int printedNodes = 0;
        String nodeToPrint;
        int i = 1;
        int nodes;
        String line;

        //only prints the first five levels
        for (int depth = 0; depth < 5; depth++) {
            //number of nodes to print at a particular depth
            nodes = (int) Math.pow(2, depth);
            line = "";
            for (int j = 0; j < nodes && i < heap.length; j++) {
                if (heap[i] != null) {
                    nodeToPrint = heap[i].toString();
                    printedNodes++;
                } else {
                    nodeToPrint = "[null]";
                }
                line += " " + nodeToPrint;
                i++;
            }

            printCentered(line);
            if (i >= heap.length || printedNodes >= size) break;
        }
    }

    public static void main(String[] args) {
        Treap<Integer, Integer> treap = new Treap<>();

        for (int i = 0; i < 200; i += 2) {
            treap.put(i, i);
        }

        System.out.println(treap.ceiling(35));
        System.out.println(treap.floor(35));
        System.out.println(treap.ceiling(20));
        System.out.println(treap.floor(20));
        System.out.println(treap.floor(300));
        System.out.println(treap.ceiling(300));
    }
}