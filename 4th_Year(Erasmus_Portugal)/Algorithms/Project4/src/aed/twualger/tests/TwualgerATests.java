/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.twualger.tests;

import aed.twualger.*;
import aed.utils.TemporalAnalysisUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.BiFunction;

public class TwualgerATests {

    private static String INFO = "TEST INFO: ";

    private static final int GARGANTUAN = 1600000;
    private static final int HUGE = 1000000;
    private static final int LARGE = 100000;
    private static final int BIG = 10000;
    private static final int MEDIUM = 1000;
    private static final int SMALL = 100;
    private static final int TINY = 10;
    //creates a random generator with a specific seed
    private static final Random pseudoRandom = new Random(8371);
    private static final OffsetDateTime CURRENT_DATE = OffsetDateTime.of(2022,07,11,23,59,0,0,ZoneOffset.UTC);
    private static final OffsetDateTime CURRENT_MINUS_72H = CURRENT_DATE.minusHours(72);
    private static final String PATH = "data";

    public static List<Runnable> getAllTests()
    {
        ArrayList<Runnable> tests = new ArrayList<Runnable>();
        tests.add(TwualgerATests::test1);
        tests.add(TwualgerATests::test2);
        tests.add(TwualgerATests::test3);
        tests.add(TwualgerATests::test4);
        tests.add(TwualgerATests::test5);
        tests.add(TwualgerATests::test6);
        tests.add(TwualgerATests::test7);

        return tests;
    }

    public static void test1()
    {
        System.out.println(INFO + "testing readTweetsFromFile");
        UserCacheA cache = TwualgerA.readUserTweetsFromFile(PATH,"elonmusk");
        Twualger.printTweets(cache.tweets,3);

        cache = TwualgerA.readUserTweetsFromFile(PATH,"robertdowneyjr");
        Twualger.printTweets(cache.tweets,5);

        cache = TwualgerA.readUserTweetsFromFile(PATH,"cristiano");
        Twualger.printTweets(cache.tweets,6);
    }

    public static void test2()
    {
        System.out.println(INFO + "testing getUserCache");
        TwualgerA twualger = new TwualgerA(PATH);

        UserCacheA miley = twualger.getUserCache("mileycyrus");
        printUserCache(miley,2);

        UserCacheA benzema = twualger.getUserCache("benzema");
        printUserCache(benzema,2);

        UserCacheA miley2 = twualger.getUserCache("mileycyrus");
        System.out.println("Two searches retrieve the same cache object: " + (miley2 == miley));
        miley2 = twualger.getUserCache("mileycyrus");
        printUserCache(miley2);

        UserCacheA jlo = twualger.getUserCache("jlo");
        printUserCache(jlo);
    }

    public static void test3()
    {
        System.out.println(INFO + "testing constructor & getUserCache");
        TwualgerA twualger = new TwualgerA(PATH);
        System.out.println("Total caches: " + twualger.getCaches().size());

        UserCacheA miley = twualger.getUserCache("mileycyrus");
        miley = twualger.getUserCache("mileycyrus");
        printUserCache(miley);

        UserCacheA benzema = twualger.getUserCache("benzema");
        printUserCache(benzema);
        System.out.println("Total caches: " + twualger.getCaches().size());


        System.out.println(INFO + "Getting a top celebrity from cache, should be immediate");
        Long time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerA(PATH),
                TINY,
                cache->
                {
                    cache.getUserCache("elonmusk");
                },
                30);
        System.out.println("AET <= 0.2ms: " + ((time/1E6)<=0.2f));

        System.out.println(INFO + "Getting a non-top celebrity 10x.");
        time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerA(PATH),
                TINY,
                cache->
                {
                    for(int i = 0; i < 10; i++)
                        cache.getUserCache("benzema");
                },
                20);
        System.out.println("AET <= 10.0ms: " + ((time/1E6)<=10.0));
    }

    public static void test4()
    {
        System.out.println(INFO + "testing totalSearches and cacheHitRatio");
        List<String> topCelebs = TwualgerA.readTopCelebs(PATH);
        List<String> allCelebs = TwualgerA.readAllCelebs(PATH);
        Set<String> following = generateRandomFollowing(topCelebs,allCelebs,50);
        TwualgerA twualger = new TwualgerA(PATH);

        System.out.println("Total Searches: " + twualger.totalSearches());
        System.out.println("Hit Ratio: " + twualger.cacheHitRatio());

        System.out.println(INFO + "Searching only for top celebs");
        for(String userHandle : topCelebs)
        {
            twualger.getUserCache(userHandle);
        }
        System.out.println("Total Searches: " + twualger.totalSearches());
        System.out.println("Hit Ratio: " + twualger.cacheHitRatio());
        System.out.println("Total caches: " + twualger.getCaches().size());

        System.out.println(INFO + "doing 50 searches");
        for(String userHandle : following)
        {
            twualger.getUserCache(userHandle);
        }

        System.out.println("Total Searches: " + twualger.totalSearches());
        System.out.println("Hit Ratio: " + twualger.cacheHitRatio());
        System.out.println("Total caches: " + twualger.getCaches().size());

        System.out.println(INFO + "doing same 50 searches");
        for(String userHandle : following)
        {
            twualger.getUserCache(userHandle);
        }
        System.out.println("Total Searches: " + twualger.totalSearches());
        System.out.println("Hit Ratio: " + twualger.cacheHitRatio());
        System.out.println("Total caches: " + twualger.getCaches().size());

    }

    public static void test5()
    {
        System.out.println(INFO + "testing buildTimeLine");
        List<String> topCelebs = TwualgerA.readTopCelebs(PATH);
        List<String> allCelebs = TwualgerA.readAllCelebs(PATH);
        List<String> following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,15));

        TwualgerA twualger = new TwualgerA(PATH);

        //we don't have tweets so old, so this should not return any tweets
        List<Tweet> tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusYears(16),CURRENT_DATE.minusYears(15));
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets);
        System.out.println(INFO + "Invalid dates for timeline");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusHours(48),CURRENT_DATE.minusHours(72));
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets,3);
        System.out.println(INFO + "Timeline with old tweets");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusYears(11),CURRENT_DATE.minusYears(10));
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets,3);

        System.out.println(INFO + "Timeline with 1 day 3 years ago");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusYears(3).minusHours(24),CURRENT_DATE.minusYears(3));
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets,3);
    }

    public static void test6()
    {
        System.out.println(INFO + "testing Twualger general functionality and efficiency");
        List<String> topCelebs = TwualgerA.readTopCelebs(PATH);
        List<String> allCelebs = TwualgerA.readAllCelebs(PATH);
        List<String> following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10));
        List<Tweet> tweets;
        OffsetDateTime toDate;
        OffsetDateTime fromDate;

        TwualgerA twualger = new TwualgerA(PATH);

        System.out.println(INFO + "Timeline 1Week");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusDays(7),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets,2);

        for(int i = 0; i < 5; i++)
        {
            following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,5+i));
            toDate = generateRandomToDate();
            fromDate = generateRandomFromDate(toDate);
            tweets = twualger.buildTimeLine(following,fromDate,toDate);
        }


        System.out.println("Total tweets retrieved: " + tweets.size());
        System.out.println(INFO + "printing older tweets");
        for(int i = 1;i <= 2; i++)
        {
            System.out.println(tweets.get(tweets.size()-i));
        }

        System.out.println("Total Searches:" + twualger.totalSearches());
        System.out.println("Hit ratio: " + twualger.cacheHitRatio());

        UserCacheA elon = twualger.getUserCache("elonmusk");
        printUserCache(elon);
        //this is just for releasing memory, this test is hard to execute in Mooshak due to memory limits
        twualger = null;

        System.out.println(INFO + "Building timeline should have an ok time because of cache");
        Long time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerA(PATH),
                TINY,
                cache->
                {
                    for(int i = 0; i < 5; i++)
                    {
                        List<String> followedAccounts = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10+pseudoRandom.nextInt(5)));
                        OffsetDateTime to = generateRandomToDate();
                        OffsetDateTime from = generateRandomFromDate(to);
                        cache.buildTimeLine(followedAccounts,from,to);
                    }
                },
                20);
        System.out.println("AET <= 100ms: " + ((time/1E6)<=100.0));

    }

    public static void test7()
    {
        System.out.println(INFO + "testing downsizeCache");
        List<String> topCelebs = TwualgerA.readTopCelebs(PATH);
        List<String> allCelebs = TwualgerA.readAllCelebs(PATH);
        List<String> following;

        List<Tweet> tweets;
        OffsetDateTime toDate;
        OffsetDateTime fromDate;
        List<UserCacheA> caches;

        TwualgerA twualger = new TwualgerA(PATH);

        for(int i = 0; i < 5; i++)
        {
            following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10+i));

            toDate = generateRandomToDate();
            fromDate = generateRandomFromDate(toDate);
            tweets = twualger.buildTimeLine(following,fromDate,toDate);
        }

        System.out.println("Total searches: " + twualger.totalSearches());
        System.out.println("Hit ratio:" + twualger.cacheHitRatio());
        caches = twualger.getCaches();
        caches.sort(TwualgerATests::compareCaches);
        System.out.println("Total caches: " + caches.size());
        UserCacheA leastSearched = getLeastSearchedNonTop(caches);
        //UserCacheA mostSearched = getMostSearchedNonTop(caches);
        printUserCache(leastSearched);
        //printUserCache(mostSearched);

        System.out.println("Downsizing cache");
        twualger.downsizeCache();
        System.out.println("Total searches: " + twualger.totalSearches());
        System.out.println("Hit ratio:" + twualger.cacheHitRatio());
        caches = twualger.getCaches();
        System.out.println("Total caches: " + caches.size());
        caches.sort(TwualgerATests::compareCaches);
        printUserCache(caches.get(caches.size()-1));
        //In this particular test I cannot test anything else, because all non-top caches have a count of 1, and all of them are deleted

    }

    private static UserCacheA getLeastSearchedNonTop(List<UserCacheA> lst)
    {
        int min = Integer.MAX_VALUE;
        UserCacheA leastSearched = null;
        for(UserCacheA a : lst)
        {
            if(!a.isTop && a.useCount < min)
            {
                min = a.useCount;
                leastSearched = a;
            }
        }
        return leastSearched;
    }

    private static UserCacheA getMostSearchedNonTop(List<UserCacheA> lst)
    {
        int max = Integer.MIN_VALUE;
        UserCacheA mostSearched = null;
        for(UserCacheA a : lst)
        {
            if(!a.isTop && a.useCount > max)
            {
                max = a.useCount;
                mostSearched = a;
            }
        }
        return mostSearched;
    }

    private static int compareCaches(UserCacheA a, UserCacheA b)
    {
        int compare = a.useCount-b.useCount;
        //in case of tie, order alphabetically by userName
        if(compare == 0)
        {
            compare = a.userName.compareTo(b.userName);
        }
        return compare;
    }

    private static void printUserCache(UserCacheA user, int tweetNumber)
    {
        String top = user.isTop? "(TOP)": "";
        System.out.println(user.userName+top+": count " + user.useCount + ", tweets:");
        Twualger.printTweets(user.tweets,tweetNumber);
    }

    private static void printUserCache(UserCacheA user)
    {
        String top = user.isTop? "(TOP)": "";
        System.out.println(user.userName+top+": count " + user.useCount + ", tweets:");
        System.out.println("Newest tweet:" + user.tweets.get(0).toString());
        System.out.println("Oldest tweet:" + user.tweets.get(user.tweets.size()-1).toString());
    }


    private static OffsetDateTime generateRandomToDate()
    {
        int roll;

        roll = pseudoRandom.nextInt(100);
        if(roll < 80)
        {
            //80% of the time, return the current date
           return CURRENT_DATE;
        }
        else
        {
            //20% of the time, select a day that can go back up to 300 days (we only have data of 10 months before the current date)
            return CURRENT_DATE.minusDays(pseudoRandom.nextInt(300));
        }
    }

    private static OffsetDateTime generateRandomFromDate(OffsetDateTime toDate)
    {
        int roll = pseudoRandom.nextInt(100);
        if(roll < 80)
        {
            //80% of the time, it is a 72 hours timeline
            return toDate.minusHours(72);
        }
        else
        {
            //20% of the time, select a date up to 90 days before the toDate (3 months)
            return toDate.minusDays(pseudoRandom.nextInt(90));
        }
    }


    private static Set<String> generateRandomFollowing(List<String> topCelebs, List<String> allCelebs, int n)
    {
        HashSet<String> selectedFollowing = new HashSet<>();
        int roll;

        while(selectedFollowing.size() < n)
        {
            roll = pseudoRandom.nextInt(100);
            if(roll < 70)
            {
                //70% of the time, get a random element from the top celebs
                selectedFollowing.add(topCelebs.get(pseudoRandom.nextInt(topCelebs.size())));
            }
            else
            {
                //30% of the time, get a random element from all celebs
                selectedFollowing.add(allCelebs.get(pseudoRandom.nextInt(allCelebs.size())));
            }
        }

        return selectedFollowing;
    }

    private static long getUsedHeapMemory()
    {
        //Runtime.getRuntime().
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
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
