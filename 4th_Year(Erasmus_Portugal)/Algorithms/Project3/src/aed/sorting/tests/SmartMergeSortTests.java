/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.sorting.tests;

import aed.sorting.*;
import aed.utils.TemporalAnalysisUtils;
import aed.sorting.Run;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.BiFunction;

public class SmartMergeSortTests {

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
        tests.add(SmartMergeSortTests::test1);
        tests.add(SmartMergeSortTests::test2);
        tests.add(SmartMergeSortTests::test3);
        tests.add(SmartMergeSortTests::test4);
        tests.add(SmartMergeSortTests::test5);
        tests.add(SmartMergeSortTests::test6);
        tests.add(SmartMergeSortTests::test7);
        tests.add(SmartMergeSortTests::test8);
        tests.add(SmartMergeSortTests::test9);
        tests.add(SmartMergeSortTests::test10);

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
        //This test is not disclosed
    }

    public static void test2()
    {
        System.out.println(INFO + "testing getNaturalRun");
        Integer[] a = new Integer[]{5,6,7,8,9,4,3,2,1,0};
        //test1
        Run r = SmartMergeSort.getNaturalRun(a,2,3);
        System.out.println("Run: " + r.toString());
        //test2
        r = SmartMergeSort.getNaturalRun(a,0,9);
        System.out.println("Run: " + r.toString());
        //test3
        r = SmartMergeSort.getNaturalRun(a,9,9);
        System.out.println("Run: " + r.toString());
        //test4
        a = new Integer[]{9,8,7,6,5,4};
        r = SmartMergeSort.getNaturalRun(a,0,5);
        System.out.println("Run: " + r.toString());
        //test5
        a = new Integer[]{3,4,4,5,5,6,1,2,3,4};
        r = SmartMergeSort.getNaturalRun(a,0,9);
        System.out.println("Run: " + r.toString());

        //test6
        a = new Integer[MEDIUM];
        for(int i = 0; i < MEDIUM; i++)
        {
            a[i] = i;
        }
        a[MEDIUM/4] = 0;
        a[MEDIUM/2] = 0;
        r = SmartMergeSort.getNaturalRun(a,0,MEDIUM-1);
        System.out.println("Run: " + r.toString());
        //test7
        r = SmartMergeSort.getNaturalRun(a,(MEDIUM/2),MEDIUM-1);
        System.out.println("Run: " + r.toString());

    }

    public static void test3()
    {
        //This test is not disclosed

    }

    public static void test4()
    {
        System.out.println(INFO + "testing getNextRunWithMinimumSize, starting with natural runs");
        //test1
        Integer[] a = new Integer[SMALL];
        for(int i = 0; i < SMALL/2; i++)
        {
            a[i] = i;
        }
        for(int i = SMALL/2; i < SMALL; i++)
        {
            a[i] = SMALL-i;
        }
        Run r = SmartMergeSort.getNextRunWithMinimumSize(a,0,SMALL-1,32);
        System.out.println("Run: " + r.toString());

        //test2
        a = new Integer[SMALL];
        for(int i = 0; i < SMALL; i++)
        {
            a[i] = i;
        }
        r = SmartMergeSort.getNextRunWithMinimumSize(a,SMALL-15,SMALL-1,32);
        System.out.println("Run: " + r.toString());

        //test3
        a = new Integer[32];
        for(int i = 0; i < 32; i++)
        {
            a[i] = i;
        }
        r = SmartMergeSort.getNextRunWithMinimumSize(a,0,31,32);
        System.out.println("Run: " + r.toString());

        //test4
        System.out.println(INFO + "testing natural runs that need to be extended");

        a = new Integer[SMALL];
        for(int i = 0; i < 16; i++)
        {
            a[i] = i;
        }
        for(int i = 16; i < SMALL; i++)
        {
            a[i] = pseudoRandom.nextInt(SMALL);
        }
        r = SmartMergeSort.getNextRunWithMinimumSize(a,0,SMALL-1,43);
        System.out.println("Run: " + r.toString());
        printArray(a,16,43-1);

        //test5
        r = SmartMergeSort.getNextRunWithMinimumSize(a,43,SMALL-1,33);
        System.out.println("Run: " + r.toString());
        printArray(a,43,43+33-1);

        //test6
        r = SmartMergeSort.getNextRunWithMinimumSize(a,43+33,SMALL-1,35);
        System.out.println("Run: " + r.toString());
        printArray(a,43+33,SMALL-1);

        //test7
        System.out.println(INFO + "Efficency of method when natural run is almost min size");
        a = new Integer[50];
        for(int i = 0; i < 30; i++)
        {
            a[i] = i;
        }
        for(int i = 30; i < 50; i++)
        {
            a[i] = 58-i;
        }
        final Integer[] initialArray = a.clone();
        r = SmartMergeSort.getNextRunWithMinimumSize(a,0,49,32);
        printArray(a,16,31);

        //This test is not ideal because the time of the clone method is included in the execution time.
        //However, for testing purposes in Mooshak it is not relevant.
        long time = TemporalAnalysisUtils.getAverageCPUTime(
                i->initialArray,
                TINY,
                example->{
                    Integer[] copy;
                    for(int i = 0; i < 10000; i++)
                    {
                        copy = example.clone();
                        SmartMergeSort.getNextRunWithMinimumSize(copy,0,49,32);
                    }},
                30);
        System.out.println("AET <= 4ms: " + ((time/1E6)<=4.0f));

    }

    public static void test5()
    {
        //This test is not disclosed

    }

    public static void test6()
    {
        System.out.println(INFO + "testing mergeCollapse");


        MergeStack s = new MergeStack();
        Integer[] aux = new Integer[100];
        Integer[] a = new Integer[100];

        for(int i = 0; i < 100; i++)
        {
            a[i] = (int) Math.pow(2,i%16)+i/16;
        }

        for(int i = 60; i < 80; i++)
        {
            a[i] = 60-i;
        }


        //simpler tests where mergeCollapse does not do anything
        //test1
        s.push(new Run(0,16));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);

        //test2
        s.push(new Run(16,8));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);

        //test3
        s.push(new Run(24,4));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);

        //collapses will begin now
        //test4
        s.push(new Run(28,4));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);
        printArray(a,0,31);

        //test5
        s.push(new Run(32,8));
        s.push(new Run(40,4));
        s.push(new Run(44,4));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);
        printArray(a,32,47);

        //test6
        s.push(new Run(48,8));
        s.push(new Run(56,4));
        //now we push a big natural run
        s.push(new Run(60,20));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);
        printArray(a,48,79);

        //test7
        s.push(new Run(80,16));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);

        //test8
        s.push(new Run(96,4));
        SmartMergeSort.mergeCollapse(s,a,aux);
        System.out.println(s);

    }

    public static void test7()
    {
        System.out.println(INFO + "testing sort");

        Integer[] a = generateRandomArray(TINY);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
        printArray(a);

        System.out.println(INFO + "testing sort with size power of 2");
        a = generateRandomArray(1024);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));

        System.out.println(INFO + "testing sort with size != power of 2");
        a = generateRandomArray(1038);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));

        System.out.println(INFO + "testing sort with large size != power of 2");
        a = generateRandomArray(BIG);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));

        System.out.println(INFO + "testing sort with large sorted array");
        a = MergeInsertionSort.generateAscendingExample(pseudoRandom,LARGE);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));

        System.out.println(INFO + "testing sort with large descending array");
        a = MergeInsertionSort.generateDescendingExample(pseudoRandom,LARGE);
        SmartMergeSort.sort(a);
        System.out.println("isSorted: " + Sort.isSorted(a));
    }

    private static void test8()
    {
        //This test is not disclosed

    }

    public static void test9()
    {
        System.out.println(INFO + "This test is worth 0 points, exercise 9 will be evaluated manually");

        final float ERROR_MARGIN_1 = 0.0002f;
        final float DESIRED_VALUE_1 = 0.999f;

        //since I want to give some freedom in the way students implement these methods, I'm testing them
        //by average number of sorted positions.
        //test1
        System.out.println(INFO + "testing LargeNaturalRuns with size " + LARGE);
        float avgPercentage = getAverageSortedPercentage(SmartMergeSort::generateLargeNaturalRunsExample,LARGE,100);
        boolean testResult = withinErrorMargin(avgPercentage, DESIRED_VALUE_1, ERROR_MARGIN_1);
        float minorRange = DESIRED_VALUE_1 - ERROR_MARGIN_1;
        float majorRange = DESIRED_VALUE_1 + ERROR_MARGIN_1;
        Locale.setDefault(new Locale("en", "US"));
        DecimalFormat df = new DecimalFormat(".#####");
        System.out.println(df.format(minorRange) + " <= (Avg. Sorted %) <=" + df.format(majorRange) + ": " + testResult);
    }

    public static void test10()
    {
        //This test is not disclosed
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
        printArray("Array",a);
    }

    private static void printArray(String description, Object[] a)
    {
        if(a == null)
        {
            System.out.println(description + ": []");
        }

        printArray(description,a,0,a.length-1);
    }

    private static void printArray(String description, Object[] a, int low, int high)
    {
        if(a == null || a.length == 0)
        {
            System.out.println(description + ": []");
            return;
        }

        if(low > 0)
        {
            System.out.print(description + ": [...");
        }
        else
        {
            System.out.print(description + ": [");
        }

        for(int i = low; i < high; i++)
        {
            System.out.print(a[i]+",");
        }
        if(high < a.length-1)
        {
            System.out.print(a[high]+"...]");
        }
        else
        {
            System.out.print(a[a.length-1]+"]");
        }

        System.out.println();
    }

    private static void printArray(Object[] a, int low, int high)
    {
        printArray("Array",a,low,high);
    }
}