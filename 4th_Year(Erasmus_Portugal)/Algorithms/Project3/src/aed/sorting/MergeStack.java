/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */
package aed.sorting;

//Class that implements a stack of runs, specially designed for the SmartMergeSort algorithm.
//This implementation of a stack is special, because we want to push, but we need to access
//elements other than the top of the stack, and we also need to do collapses.
//A collapse is when you collapse two consecutive runs on the stack, merging them into a single run in the stack.
//This class should be declared as default, since this is an auxiliary class to be used in the SmartMergetSort algorithm.
//However, in order to make Mooshak tests easier, this is made public.
public class MergeStack {
    private int height;
    private Run[] runs;

    private static final int MIN_STACK_SIZE = 5;

    @SuppressWarnings("unchecked")
    public MergeStack()
    {
        this.height = 0;
        this.runs = new Run[MIN_STACK_SIZE];
    }
    @SuppressWarnings("unchecked")

    public int height(){
        return this.height;
    }

    public boolean isEmpty()
    {
        return this.height == 0;
    }


    @SuppressWarnings("unchecked")
    private void resize(int newArrayLength)
    {
        Run[] resizedArray =  new Run[newArrayLength];
        System.arraycopy(this.runs,0,resizedArray,0,this.height);

        this.runs = resizedArray;
    }

    public void push(Run r)
    {
        if(this.height == this.runs.length) {
            resize(2*this.height);
        }
        this.runs[this.height++] = r;
    }

    //gets the ith run on the stack, where i=1 refers to the top of the stack
    //i=2 refers to the second element on the top, and so on
    public Run get(int i)
    {
        int index = this.height-i;
        assert(index >= 0);
        return this.runs[index];
    }

    //collapses the runs in position i and i+1 in the stack, where i=1 refers to the top of the stack
    //i=2 refers to the second element on the top, and so on
    //a collapse is when you collapse two consecutive runs on the stack, merging them into a single run in the stack
    //this could be performed with a combination of pops followed by pushes, but it is more efficient to
    //implement a custom method for this
    public void collapse(int i)
    {
        int index = this.height-(i+1);
        assert(index >= 0);

        this.runs[index] = this.runs[index].collapse(this.runs[index+1]);

        //shifts down all remaining elements on top of the collapsed one
        this.height--;
        for(int j = index +1; j < this.height; j++)
        {
            this.runs[j] = this.runs[j+1];
        }

        //I'm not worrying about resizing down the array when collapsing because this stack is used only for sorting
        //with the SmartMergeSort. Once the sort is finished, this stack will disappear anyway, and we don't gain
        //anything by releasing the memory earlier. Therefore, in order to make collapse as fast as possible I'm
        //never resizing down.
    }

    @Override
    public String toString()
    {
        String result = "[";
        for(int i = this.height-1; i >= 0; i--)
        {
            result += this.runs[i].toString() + " ";
        }
        result += "]";

        return result;
    }

    //for testing purposes
    public static void main(String[] args)
    {
        MergeStack s = new MergeStack();

        s.push(new Run(0,5));
        s.push(new Run(5,8));
        s.push(new Run(13,7));
        s.push(new Run(20,10));
        s.push(new Run(30,5));
        System.out.println(s);
        System.out.println(s.get(1));
        System.out.println(s.get(2));
        System.out.println(s.get(3));

        //in the SmartMergeSort we will collapse R1 with R2, or R2 with R3, so I'm mainly testing those cases
        s.collapse(2);
        System.out.println(s);
        s.collapse(2);
        System.out.println(s);
        s.collapse(1);
        System.out.println(s);
        s.collapse(1);
        System.out.println(s);
    }
}
