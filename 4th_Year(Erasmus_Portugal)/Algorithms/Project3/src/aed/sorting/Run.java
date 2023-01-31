/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.sorting;

//This class represents a Run in an array, i.e. a sequence or a part of the array where all elements are ordered
//A Run is represented by a starting position (representing the index where the Run begins), and a length.
//The length represents how many elements of the array, starting from the index start, are ordered

//This class should be declared as default, since this is an auxiliary class to be used in the SmartMergetSort algorithm
//However, in order to make Mooshak tests easier, this is made public.
public class Run {

    //no need to protect these instance variables since they should only be used as internal class for the MergeSort
    //feel free to use them in your SmartMergeSort Class
    int start;
    int length;

    public Run(int start, int length)
    {
        this.start = start;
        this.length = length;
    }

    //this method collapses two consecutive runs into a single run
    //r2 must come after this run
    Run collapse(Run r2)
    {
        //checking if the received run naturally follows this run
        assert(r2.start == this.start + this.length);

        return new Run(this.start,this.length+r2.length);
    }

    @Override
    public String toString() {
        return "{" + this.start + "+" + this.length + "}";
    }
}
