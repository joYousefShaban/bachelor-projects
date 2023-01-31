/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */
package aed.sorting;

//This class can be used as an efficient temporary buffer. It always returns a buffer array with appropriate size
//but tries to reuse the previously existing buffer. Why? Initializing an array also takes time.
//The class is declared as default (no access modifier), so that it can only be used by other classes in the sorting package
class RecyclableBuffer<T extends Comparable<T>> {
    private T[] buffer;

    @SuppressWarnings("unchecked")
    RecyclableBuffer(int initialSize)
    {
        assert(initialSize > 0);
        this.buffer = (T[]) new Comparable[initialSize];
    }

    @SuppressWarnings("unchecked")
    T[] getBuffer(int size)
    {
        if(size > this.buffer.length)
        {
            //if current buffer size is not enough, get a bigger one, doubling the previous size until it is
            //enough for the desired size
            int nextSize = this.buffer.length*2;
            while(nextSize < size)
            {
                nextSize *= 2;
            }

            this.buffer = (T[]) new Comparable[nextSize];
        }

        return this.buffer;
    }
}
