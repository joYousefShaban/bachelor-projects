package aed.tables;

import aed.tables.ISymbolTable;

public interface IOrderedSymbolTable<Key extends Comparable<Key>, Value> extends ISymbolTable<Key,Value>
{
    //returns the minimum key in the Symbol Table
    Key min();
    //returns the maximum key in the Symbol Table
    Key max();
    //returns the largest key in the Symbol Table that is <= k
    Key floor(Key k);

    //returns the smallest key in the Symbol Table that is >= k
    Key ceiling(Key k);

    //returns the number of keys in the Symbol Table that are < k
    int rank(Key k);

    //returns the nth smallest key in the Symbol Table. If n = 0, returns the smallest key. If n=1,
    // returns the second smallest key, etc.
    Key select(int n);

    void deleteMin();
    void deleteMax();
    int size(Key low, Key high);
    Iterable<Key> keys(Key low, Key high);

    @Override
    //returns an Iterable object (usually a collection) that can be used to iterate through all keys currently
    //stored in the Symbol Table. The keys are sorted.
    Iterable<Key> keys();
}
