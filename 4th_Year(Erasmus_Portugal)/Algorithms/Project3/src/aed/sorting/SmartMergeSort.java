package aed.sorting;

import aed.sorting.tests.SmartMergeSortTests;

import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Random;

public class SmartMergeSort extends Sort {

    private static final int MAX_INTERVAL = 64;

    //creates a random generator with a specific seed
    //this is useful for testing methods that are supposed to generate random elements
    //because we can always repeat the same tests by using the same seed
    private static final Random pseudoRandom = new Random(3729);


    //sort an array of elements (using InsertionSort) from low to high (including)
    //assuming that the first n elements are already sorted between themselves
    //this variation implies that we can start with a bigger hand immediately
    //this method is very useful for the SmartMergeSort in order to extend a natural
    //ascending run with additional elements to make a run with minimum size
    public static <T extends Comparable<T>> void insertionSortWithInitialSortedHand(T[] a, int low, int n, int high) {
        assert (low <= high);
        assert (n > 0);
        assert (low + n <= high);
        //TODO: implement

        for (int i = low; i <= high - n; i++) {
            for (int j = i + n; j > low; j--) {
                if (less(a[j], a[j - 1])) {
                    exchange(a, j, j - 1);
                } else break;
            }
        }
    }

    public static <T extends Comparable<T>> Run getNaturalRun(T[] a, int low, int high) {
        assert (low <= high);
        //TODO: implement


//        //maximum 1st index will contain the low, 2nd will contain high, 3rd will contain the length
//        if (low >= high)
//            return new Run(high, 1);
//        int[] maximum = new int[3];
//        for (int i = low; i < high; i++) {
//            int[] current = new int[3];
//            current[0] = low;
//            current[2]++;
//            while (i < high && a[i].compareTo(a[i + 1]) <= 0) {
//                current[2]++;
//                i++;
//            }
//            if (current[2] > maximum[2]) {
//                maximum = current;
//            }
//        }
//        return new Run(maximum[0], maximum[2]);

        int loopStart, loopLength = 1;
        for (int i = low + 1; i <= high; i++)
            if (a[i].compareTo(a[i - 1]) >= 0) {
                loopStart = i - 1;
                for (int j = i; j <= high && a[j].compareTo(a[j - 1]) >= 0; j++)
                    loopLength++;
                return new Run(loopStart, loopLength);
            }
        return new Run(low, 1);
    }

    public static <T extends Comparable<T>> Run getNaturalOrMakeAscendingRun(T[] a, int low, int high) {
        assert (low <= high);
        //TODO: implement

        if (a.length == 1) {
            return new Run(0, 1);
        } else if (low == high) {
            return new Run(high, 1);
        }
        if (less(a[0], a[1])) {
            return getNaturalRun(a, low, high);
        } else {
            //maximum 1st index will contain the low, 2nd will contain high, 3rd will contain the length
            int loopStart, loopLength = 1;
            for (int i = low + 1; i <= high; i++)
                if (a[i].compareTo(a[i - 1]) <= 0) {
                    loopStart = i - 1;
                    for (int j = i; j <= high && a[j].compareTo(a[j - 1]) < 0; j++)
                        loopLength++;
                    insertionSort(a, loopStart, loopLength + loopStart - 1);
                    return new Run(loopStart, loopLength);
                }
            return new Run(low, 1);
        }
    }

    public static <T extends Comparable<T>> void insertionSort(T[] a, int low, int high) {
        //TODO: implement

        for (int i = low + 1; i <= high; i++) {
            for (int j = i; j > low; j--) {
                if (less(a[j], a[j - 1])) {
                    exchange(a, j, j - 1);
                } else break;
            }
        }
    }

    public static <T extends Comparable<T>> Run getNextRunWithMinimumSize(T[] a, int low, int high, int minRunSize) {
        assert (low < high);
        assert (minRunSize > 0);
        //TODO: implement

        Run naturalRun = getNaturalRun(a, low, high);
        if (naturalRun.length >= minRunSize)
            return naturalRun;
        else if (minRunSize + low <= high) {
            insertionSort(a, low, minRunSize+low);
            return new Run(low, minRunSize);
        } else {
            insertionSort(a, low, high);
            return new Run(low, high-low+1);
        }
    }

    public static <T extends Comparable<T>> void merge(T[] a, T[] aux, Run leftRun, Run rightRun) {
        assert (rightRun.start == leftRun.start + leftRun.length);
        //TODO: implement

        for(int i=leftRun.start,j=0;i<leftRun.start+ leftRun.length+rightRun.length;i++,j++){
            aux[j]=a[j];
        }
    }

    public static <T extends Comparable<T>> void mergeCollapse(MergeStack stack, T[] a, T[] aux) {
        //TODO: implement


    }


    public static <T extends Comparable<T>> void sort(T[] a) {
        //TODO: implement
        @SuppressWarnings("unchecked") T[] aux = (T[]) new Comparable[a.length];
    }

    private static void printArray(Object[] a, int low, int high) {
        if (a == null || a.length == 0) {
            System.out.println("Array: []");
            return;
        }

        if (low > 0) {
            System.out.print("Array: [...");
        } else {
            System.out.print("Array: [");
        }

        for (int i = low; i <= high; i++) {
            System.out.print(a[i] + ",");
        }
        if (high < a.length - 1) {
            System.out.print(a[a.length - 1] + "...]");
        } else {
            System.out.print(a[a.length - 1] + "]");
        }

        System.out.println();
    }

    public static Integer[] generateLargeNaturalRunsExample(Random r, int n) {
        //todo: implement

        Integer[] randomArray = new Integer[n];
        for (int i = 0; i < n; i++)
            randomArray[i] = r.nextInt(n);
        int percentageBasedOnSize = (int) (n * (999 / 1000.0f));
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if ((randomArray[j].compareTo(randomArray[j - 1]) < 0 && i <= percentageBasedOnSize) || (randomArray[j].compareTo(randomArray[j - 1]) > 0 && i > percentageBasedOnSize)) {
                    exchange(randomArray, j, j - 1);
                } else break;
            }
        }
        return randomArray;
    }


    public static void main(String[] args) {
        //TODO: implement tests
//        Integer[] randomArray = {9, 8, 7, 6, 5, 4, 3};
//        SmartMergeSort.insertionSortWithInitialSortedHand(randomArray, 1, 4, 9);
//        System.out.println(SmartMergeSort.getNaturalOrMakeAscendingRun(randomArray, 2,5));
        SmartMergeSortTests.test5();
    }
}