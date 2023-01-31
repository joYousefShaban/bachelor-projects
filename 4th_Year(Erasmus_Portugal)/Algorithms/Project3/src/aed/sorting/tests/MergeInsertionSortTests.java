/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.sorting.tests;

import aed.sorting.MergeInsertionSort;
import aed.sorting.Sort;
import aed.utils.TemporalAnalysisUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MergeInsertionSortTests {

    private static String INFO = "TEST INFO: ";

    private static final int HUGE = 1000000;
    private static final int LARGE = 100000;
    private static final int BIG = 10000;
    private static final int MEDIUM = 1000;
    private static final int SMALL = 100;
    private static final int TINY = 10;
    //creates a random generator with a specific seed
    private static final Random pseudoRandom = new Random(2048);

    public static List<Runnable> getAllTests()
    {
        ArrayList<Runnable> tests = new ArrayList<Runnable>();
        tests.add(MergeInsertionSortTests::test1);
        tests.add(MergeInsertionSortTests::test2);
        tests.add(MergeInsertionSortTests::test3);
        tests.add(MergeInsertionSortTests::test4);
        tests.add(MergeInsertionSortTests::test5);
        tests.add(MergeInsertionSortTests::test6);

        return tests;
    }

    private static Integer[] generateRandomArray(int size)
    {
        Integer[] a = new Integer[size];

        for(int i = 0; i < size; i++)
        {
            a[i] = pseudoRandom.nextInt(size);
        }

        return a;
    }

    public static void test1()
    {
        System.out.println(INFO + "Testing traditionalBottomUpSort");
        Integer[] a = generateRandomArray(TINY);

        MergeInsertionSort.traditionalBottomUpSort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        printArray(a);

        a = generateRandomArray(SMALL);
        MergeInsertionSort.traditionalBottomUpSort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        printArray(a);
    }

    public static void test2()
    {
        //This test is not disclosed
    }

    public static void test3()
    {
        //This test is not disclosed
    }

    public static void test4()
    {
        //This test is not disclosed
    }

    public static void test5()
    {
        System.out.println(INFO + "testing sort efficiency");

        Integer[] a;
        long time;
        final int powerOf2 = (int) Math.pow(2,16);

        //test 1
        System.out.println(INFO + "testing 10 000 sorts with array of size " + TINY);
        a = generateRandomArray(TINY);
        MergeInsertionSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        printArray(a);
        a = generateRandomArray(TINY);
        //This test is not ideal because the time of the clone method is included in the execution time.
        //However, for testing purposes in Mooshak it is not relevant.
        time = TemporalAnalysisUtils.getAverageCPUTime(
                i->generateRandomArray(i),
                TINY,
                example->{
                    Integer[] copy;
                    for(int i = 0; i < 10000; i++)
                    {
                        copy = example.clone();
                        MergeInsertionSort.sort(copy);
                    }},
                30);
        System.out.println("AET <= 3.6ms: " + (time/1E6 <=3.6f));


        //test 2
        System.out.println(INFO + "testing sort with array of size " + powerOf2 + " (a power of 2)");
        a = generateRandomArray(powerOf2);
        MergeInsertionSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        time = TemporalAnalysisUtils.getAverageCPUTime(i->generateRandomArray(i),powerOf2,MergeInsertionSort::sort,30);
        System.out.println("AET <= 23ms: " + (time/1E6 <= 23.0f));

        //test 3
        System.out.println(INFO + "testing sort with array of size " + LARGE);
        a = generateRandomArray(LARGE);
        MergeInsertionSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        time = TemporalAnalysisUtils.getAverageCPUTime(i->generateRandomArray(i),LARGE,MergeInsertionSort::sort,30);
        System.out.println("AET <= 33ms: " + (time/1E6 <= 33.0f));
    }

    public static void test6()
    {
        final float ERROR_MARGIN_1 = 0.02f;
        final float DESIRED_VALUE_1 = 0.9f;
        final float ERROR_MARGIN_2 = 0.005f;
        final float DESIRED_VALUE_2 = 0.99f;
        System.out.println(INFO + "testing example generation methods");

        //test1
        //this one I can test by printing the array, because the specification is very clear on what to do
        Integer[] example = MergeInsertionSort.generateRandomExample(pseudoRandom,20);
        printArray(example);


        //since I want to give some freedom in the way students implement these methods, I'm testing them
        //by average number of sorted positions.
        //test2
        System.out.println(INFO + "testing MostlySorted with size " + MEDIUM);
        float avgPercentage = getAverageSortedPercentage(MergeInsertionSort::generateMostlySortedExample,MEDIUM,100);
        boolean testResult = withinErrorMargin(avgPercentage, DESIRED_VALUE_1, ERROR_MARGIN_1);
        System.out.println("Avg. Sorted Elements " + (DESIRED_VALUE_1 - ERROR_MARGIN_1) + " <= " + (DESIRED_VALUE_1 + ERROR_MARGIN_1) + ": " + testResult);

        //test3
        System.out.println(INFO + "testing AlmostSorted with size " + BIG);
        avgPercentage = getAverageSortedPercentage(MergeInsertionSort::generateAlmostSortedExample,BIG,100);
        testResult = withinErrorMargin(avgPercentage, DESIRED_VALUE_2, ERROR_MARGIN_2);
        System.out.println("Avg. Sorted Elements " + (DESIRED_VALUE_2 - ERROR_MARGIN_2) + " <= " + (DESIRED_VALUE_2 + ERROR_MARGIN_2) + ": " + testResult);

        //test4
        System.out.println(INFO + "testing Ascending with size " + MEDIUM);
        example = MergeInsertionSort.generateAscendingExample(pseudoRandom,MEDIUM);
        System.out.println("isSorted: " + Sort.isSorted(example));

        //test5
        System.out.println(INFO + "testing Descending with size " + MEDIUM);
        example = MergeInsertionSort.generateDescendingExample(pseudoRandom,MEDIUM);
        System.out.println("isStrictlyDescending: " + isStrictlyDescending(example));
    }

    private static boolean withinErrorMargin(float value, float desiredValue, float errorMargin)
    {
        return (value <= desiredValue + errorMargin) && (value >= desiredValue - errorMargin);
    }

    private static float getAverageSortedPercentage(BiFunction<Random,Integer,Integer[]> exampleGenerator, int size, int trials)
    {
        float totalSortedPositions = 0;
        Integer[] example;
        for(int i = 0; i < trials; i++)
        {
            example = exampleGenerator.apply(pseudoRandom,size);
            totalSortedPositions += countSortedPositions(example);
        }

        return (totalSortedPositions/trials)/size;
    }

    private static boolean isStrictlyDescending(Integer[] a)
    {
        for(int i = 1; i< a.length; i++)
        {
            if(a[i] >= a[i-1]) return false;
        }

        return true;
    }

    private static int countSortedPositions(Integer[] a)
    {
        int sorted = 0;
        for(int i = 1; i < a.length; i++)
        {
            if(a[i] >= a[i-1]) sorted++;
        }

        return sorted;
    }

    private static void printArray(Object[] a)
    {
        if(a == null || a.length == 0)
        {
            System.out.println("Array: []");
            return;
        }

        System.out.print("Array: [");
        for(int i = 0; i < a.length-1; i++)
        {
            System.out.print(a[i]+",");
        }
        System.out.print(a[a.length-1]+"]");
        System.out.println();
    }
}
