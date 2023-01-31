/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.utils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TemporalAnalysisUtils {

    private static final int DEFAULT_TRIALS = 30;
    private static final int DEFAULT_COMPLEXITY = 128;

    public static<T> void runDoublingRatioTest(String testName, Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int initialComplexity, int iterations)
    {
        runDoublingRatioTest(testName, exampleGenerator,methodToTest, initialComplexity, iterations, DEFAULT_TRIALS);
    }

    public static<T> void runDoublingRatioTest(Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int iterations)
    {
        runDoublingRatioTest("", exampleGenerator,methodToTest, DEFAULT_COMPLEXITY, iterations, DEFAULT_TRIALS);
    }

    public static<T> void runDoublingRatioTest(String testName, Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int initialComplexity, int iterations, int trials)
    {
        runDoublingRatioTest(System.out,testName,exampleGenerator,methodToTest,initialComplexity,iterations,trials);
    }

    public static<T> void runDoublingRatioTest(String fileName, String testName, Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int initialComplexity, int iterations, int trials)
    {
        try
        {
            FileOutputStream outputStream = new FileOutputStream(fileName,true);
            PrintStream output = new PrintStream(outputStream,true);
            runDoublingRatioTest(output,testName,exampleGenerator,methodToTest,initialComplexity,iterations,trials);

        }
        catch (IOException e)
        {
            System.out.println("Error opening file " + fileName);
            e.printStackTrace();
        }
    }

    public static<T> void runDoublingRatioTest(PrintStream output, String testName, Function<Integer,T> exampleGenerator, Consumer<T> methodToTest, int initialComplexity, int iterations, int trials)
    {
        assert(iterations > 0);
        assert(initialComplexity > 0);
        assert(trials > 0);

        int n = initialComplexity;
        double previousTime = getAverageCPUTime(exampleGenerator,n,methodToTest,DEFAULT_TRIALS);
        String line1 = "i\tn    \ttime(ms)\testimated r";
        String line2 = "1\t" + String.format("%05d",n) + "\t" + String.format("%08.4f",(previousTime/1E6)) + "\t-----";
        output.println(testName);
        output.println(line1);
        output.println(line2);
        if(output != System.out)
        {
            //we're writing to a file or other type of stream,
            // but we also want to print to screen, because if not it becomes boring to watch...
            System.out.println(testName);
            System.out.println(line1);
            System.out.println(line2);
        }
        double newTime;
        double doublingRatio;

        for(int i = 1; i < iterations; i++)
        {
            n *= 2;
            newTime = getAverageCPUTime(exampleGenerator,n,methodToTest,trials);
            if(previousTime > 0)
            {
                doublingRatio = newTime/previousTime;
            }
            else doublingRatio = 0;

            previousTime = newTime;
            line1 = (i+1)+"\t" + String.format("%05d",n) + "\t" + String.format("%08.4f",(newTime/1E6)) + "\t" + String.format("%.3f",doublingRatio);
            output.println(line1);

            if(output != System.out)
            {
                //we're writing to a file, but we also want to print to screen, because if not it becomes boring
                System.out.println(line1);
            }
        }
    }



    //this method is used to empirically determine the average execution time of the received consumer method across a number of trials
    //However, it additionally receives a method to generate a new example for a given complexity size n. The example generation method will not
    //count towards the execution time.
    //This is useful when we want to perform a doubling ratio analysis
    //@param exampleGenerator - a method that receives an integer representing the complexity of a problem, and generates a new example with the given complexity
    //@param complexity - the level of complexity used to generate an example
    //@param method - the method for which we want to measure average time, receiving the generated example
    //@param trials - the number of trials used to determine the average execution time
    public static<T> long getAverageCPUTime(Function<Integer,T> exampleGenerator, int complexity, Consumer<T> method, int trials)
    {
        assert(trials > 0);
        assert(complexity > 0);
        long startTime;
        long stopTime;
        long elapsedCPU = 0;
        T example;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            example = exampleGenerator.apply(complexity);
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.accept(example);
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            //call the garbage collector when time is not running, to clear out unused memory.
            //This is to help prevent the GC to activate during the method to be tested.
            // The next line forces the example to be also removed from memory.
            example = null;
            System.gc();
            elapsedCPU += stopTime - startTime;
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    //this method is used to empirically determine the average execution time of the received consumer method across a number of trials
    //However, it additionally receives a method to initialize/copy the object. The initialization method will not
    //count towards the execution time.
    //This is useful when we want to analyze the execution time of a method working with a complex object that will be destroyed
    //or changed by the method.
    //For instance, if we want to measure the execution time of an operation that deletes/removes elements from a list,
    //in the initialization method we want to initialize the list (or create a copy of an already initialized list),
    //and in the consumer method we want to delete the elements from that list
    //@param object - the initial object used for initialization
    //@param setupMethod - a method that is applied to the initial object and returns the initialized object (or a copy of the object)
    //@param method - the method for which we want to measure average time, receiving the initialized object
    //@param trials - the number of trials
    public static<T> long getAverageCPUTime(T object, UnaryOperator<T> setupMethod, Consumer<T> method, int trials)
    {
        assert(trials > 0);
        long startTime;
        long stopTime;
        long elapsedCPU = 0;
        T startingObject;
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            startingObject = setupMethod.apply(object);
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.accept(startingObject);
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            elapsedCPU += stopTime - startTime;
            //call the garbage collector when time is not running, to clear out unused memory.
            //This is to help prevent the GC to activate during the method to be tested.
            // The next line forces any copy of the initial object to be also removed from memory.
            startingObject = null;
            System.gc();
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    public static<T> long getAverageCPUTime(T object, UnaryOperator<T> setupMethod, Consumer<T> method)
    {
        return getAverageCPUTime(object, setupMethod, method, DEFAULT_TRIALS);
    }


    //this method empirically determines the average execution time of the received method across a number of trials
    public static long getAverageCPUTime(Runnable method, int trials)
    {
        assert(trials > 0);
        long startTime;
        long stopTime;
        long elapsedCPU = 0;

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] allThreadIds = threadMXBean.getAllThreadIds();

        for(int i = 0; i < trials; i++)
        {
            startTime = getCPUTime(threadMXBean,allThreadIds);
            method.run();
            stopTime = getCPUTime(threadMXBean,allThreadIds);
            elapsedCPU += stopTime - startTime;
            //call the garbage collector when time is not running, to clear out unused memory.
            //This is to help prevent the GC to activate during the method to be tested.
            System.gc();
        }

        elapsedCPU /= trials;

        return elapsedCPU;
    }

    public static long getAverageCPUTime(Runnable method)
    {
        return getAverageCPUTime(method,DEFAULT_TRIALS);
    }

    private static long getCPUTime(ThreadMXBean threadMXBean, long[] allThreadIds)
    {
        long nano = 0;
        for (long id : allThreadIds) {
            nano += threadMXBean.getThreadCpuTime(id);
        }
        return nano;
    }

}
