/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

import aed.sorting.tests.MergeInsertionSortTests;
//import aed.sorting.tests.SmartMergeSortTests; THERE'S AN ERROR HERE

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class MooshakMain {

    public static void main(String[] args)
    {
        HashMap<String, List<Runnable>> unitTests = new HashMap<>();

        //problem A - Unit tests for MergeInsertionSort
        unitTests.put("A", MergeInsertionSortTests.getAllTests());
        //problem B - Unit tests for SmartMergeSort
        //unitTests.put("B", SmartMergeSortTests.getAllTests());

        InputStreamReader inputReader = new InputStreamReader(System.in);
        BufferedReader bReader = new BufferedReader(inputReader);

        try
        {
            String line = bReader.readLine();
            String[] lineArgs = line.split(" ");
            String problem = lineArgs[0];
            int testNumber = Integer.parseInt(lineArgs[1])-1;

            unitTests.get(problem).get(testNumber).run();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
