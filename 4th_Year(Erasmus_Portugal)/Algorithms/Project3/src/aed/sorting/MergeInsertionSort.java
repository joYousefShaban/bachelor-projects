package aed.sorting;

import aed.sorting.tests.MergeInsertionSortTests;
import aed.utils.TemporalAnalysisUtils;

import java.util.Arrays;
import java.util.Random;

public class MergeInsertionSort extends Sort {

    private static final int MAX_INTERVAL = 64;

    //creates a random generator with a specific seed
    //this is useful for testing methods that are supposed to generate random elements
    //because we can always repeat the same tests by using the same seed
    private static final Random pseudoRandom = new Random(4582);

    //sort an array of elements (using MergeSort Bottom Up)
    //this method uses extra memory of O(n) to perform the sort

    protected static <T extends Comparable<T>> void merge(T[] a, T[] aux, int low, int mid, int high) {
        //TODO: implement

        int left = low;
        int right = mid + 1;
        for (int i = low; i <= high; i++) {
            aux[i] = a[i];
        }
        for (int i = low; i <= high; i++) {
            if (left > mid)
                a[i] = aux[right++];
            else if (right > high)
                a[i] = aux[left++];
            else if (less(aux[right],
                    aux[left])) a[i] = aux[right++];
            else
                a[i] = aux[left++];
        }
    }

    public static <T extends Comparable<T>> void traditionalBottomUpSort(T[] a) {
        //TODO: implement

        int n = a.length;
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable[a.length];
        for (int groupSize = 1; groupSize < n; groupSize *= 2) {
            for (int low = 0; low < n - groupSize; low += 2 * groupSize) {
                merge(a, aux, low, low + groupSize - 1, Math.min(low + 2 * groupSize - 1, n - 1));
            }
        }
    }

    public static <T extends Comparable<T>> void traditionalBottomUpSort(T[] a,int groupSize) {
        //TODO: implement

        int n = a.length;
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Comparable[a.length];
        for (; groupSize < n; groupSize *= 2) {
            for (int low = 0; low < n - groupSize; low += 2 * groupSize) {
                merge(a, aux, low, low + groupSize - 1, Math.min(low + 2 * groupSize - 1, n - 1));
            }
        }
    }

    //sort an array of elements (using InsertionSort) from low to high (including)
    //In a conventional implementation, this method should be private.
    //However, for testing purposes in Mooshak, we need to make it public
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

    //In a conventional implementation, this method should be private.
    //However, for testing purposes in Mooshak, we need to make it public
    public static int determineRunSize(int n) {
        //TODO: implement

        //check if it's
        double v = Math.log(n) / Math.log(2);
        if (v == (int) v) {
            return 32;
        }

        int k = n;
        boolean condition = false;
        while (k >= 64) {
            if (k % 2 != 0) {
                condition = true;
            }
            k /= 2;
        }
        if (condition)
            k++;
        return k;
    }

    public static <T extends Comparable<T>> void sort(T[] a) {
        //TODO: implement

        if (a.length < 64)
            insertionSort(a, 0, a.length - 1);
        else {
            int subSize = determineRunSize(a.length);
            for (int i = 0; i < a.length; i += subSize) {
                if (i + subSize < a.length)
                    insertionSort(a, i, i + subSize);
                else
                    insertionSort(a, i, a.length - 1);
            }
            traditionalBottomUpSort(a,subSize);
        }
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

    public static Integer[] generateRandomExample(int n) {
        return generateRandomExample(pseudoRandom, n);
    }

    public static Integer[] generateRandomExample(Random randomGenerator, int n) {
        //TODO: implement

        Integer[] randomArray = new Integer[n];
        for (int i = 0; i < n; i++)
            randomArray[i] = randomGenerator.nextInt(n);
        return randomArray;
    }

    public static Integer[] generateMostlySortedExample(int n) {
        return generateMostlySortedExample(pseudoRandom, n);
    }

    public static Integer[] generateMostlySortedExample(Random r, int n) {
        //TODO: implement

        Integer[] randomArray = generateRandomExample(r, n);
        int percentageBasedOnSize = (int) (n * (90 / 100.0f));
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if ((randomArray[j].compareTo(randomArray[j - 1]) < 0 && i <= percentageBasedOnSize) || (randomArray[j].compareTo(randomArray[j - 1]) > 0 && i > percentageBasedOnSize)) {
                    exchange(randomArray, j, j - 1);
                } else break;
            }
        }
        return randomArray;
    }

    public static Integer[] generateAlmostSortedExample(Random r, int n) {
        //TODO: implement

        Integer[] randomArray = generateRandomExample(r, n);
        int percentageBasedOnSize = (int) (n * (99 / 100.0f));
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if ((randomArray[j].compareTo(randomArray[j - 1]) < 0 && i <= percentageBasedOnSize) || (randomArray[j].compareTo(randomArray[j - 1]) > 0 && i > percentageBasedOnSize)) {
                    exchange(randomArray, j, j - 1);
                } else break;
            }
        }
        return randomArray;
    }

    public static Integer[] generateAlmostSortedExample(int n) {
        return generateAlmostSortedExample(pseudoRandom, n);
    }

    public static Integer[] generateAscendingExample(Random r, int n) {
        //TODO: implement

        Integer[] randomArray = generateRandomExample(r, n);
        sort(randomArray);
        return randomArray;
    }

    public static Integer[] generateAscendingExample(int n) {
        return generateAscendingExample(pseudoRandom, n);
    }

    public static Integer[] generateDescendingExample(Random r, int n) {
        //TODO: implement

        Integer[] randomArray = new Integer[n];
        for (int i = 0; i < n; i++) {
            randomArray[i] = n - i;
        }
        return randomArray;
    }

    public static Integer[] generateDescendingExample(int n) {
        return generateDescendingExample(pseudoRandom, n);
    }

    public static void main(String[] args) {
        //TODO: implement tests
//        TemporalAnalysisUtils.runDoublingRatioTest("tests.tsv", "Merge/Random", MergeInsertionSort::generateRandomExample, MergeInsertionSort::sort, 100, 15, 30);
//        TemporalAnalysisUtils.runDoublingRatioTest("tests.tsv", "Merge/Mostly", MergeInsertionSort::generateMostlySortedExample, MergeInsertionSort::sort, 100, 15, 30);
//        TemporalAnalysisUtils.runDoublingRatioTest("tests.tsv", "Merge/Almost", MergeInsertionSort::generateAlmostSortedExample, MergeInsertionSort::sort, 100, 15, 30);

//        System.out.println(determineRunSize(10234));
        MergeInsertionSortTests.test5();
    }
}