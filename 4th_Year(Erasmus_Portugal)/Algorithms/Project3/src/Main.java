import java.util.Arrays;
import java.util.Random;

import aed.sorting.MergeInsertionSort;
import aed.sorting.SmartMergeSort;
import aed.sorting.tests.SmartMergeSortTests;

public class Main {
    public static void main(String[] args) {
//        Random pseudoRandom = new Random(4582);
//        Integer[] randomArray =MergeInsertionSort.generateRandomExample(pseudoRandom,60);
//        Integer[] randomArray ={1,3,4,5,9,2,10,12,15,19,2,1,3,4,5,9,11,22};
//        System.out.println(Arrays.toString(randomArray));
//        System.out.println(SmartMergeSort.getNaturalRun(randomArray,0,randomArray.length));

        SmartMergeSortTests.test5();
    }
}