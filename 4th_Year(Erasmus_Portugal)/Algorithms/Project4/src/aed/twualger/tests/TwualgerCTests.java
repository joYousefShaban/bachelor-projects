/**************************************************************
 * Copyright 2022 João Dias
 * Source code developed for the AED Course
 * Feel free to use for pedagogical purposes
 * FCT, Universidade do Algarve
 */

package aed.twualger.tests;

import aed.tables.Treap;
import aed.twualger.*;
import aed.utils.TemporalAnalysisUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class TwualgerCTests {

    private static String INFO = "TEST INFO: ";

    private static final int GARGANTUAN = 1600000;
    private static final int HUGE = 1000000;
    private static final int LARGE = 100000;
    private static final int BIG = 10000;
    private static final int MEDIUM = 1000;
    private static final int SMALL = 100;
    private static final int TINY = 10;
    //creates a random generator with a specific seed
    private static final Random pseudoRandom = new Random(12311);
    private static final OffsetDateTime CURRENT_DATE = OffsetDateTime.of(2022,07,11,23,59,0,0,ZoneOffset.UTC);
    private static final OffsetDateTime CURRENT_MINUS_72H = CURRENT_DATE.minusHours(72);
    private static final String PATH = "data";

    public static List<Runnable> getAllTests()
    {
        ArrayList<Runnable> tests = new ArrayList<Runnable>();
        tests.add(TwualgerCTests::test1);
        tests.add(TwualgerCTests::test2);
        tests.add(TwualgerCTests::test3);
        tests.add(TwualgerCTests::test4);
        tests.add(TwualgerCTests::test5);
        tests.add(TwualgerCTests::test6);

        return tests;
    }

    public static void test1()
    {
        System.out.println(INFO + "testing readTweetsFromFile");
        UserCacheB cache = TwualgerC.readUserTweetsFromFile(PATH,"elonmusk",CURRENT_DATE.minusHours(24));
        printUserCache(cache,3);

        cache = TwualgerC.readUserTweetsFromFile(PATH,"elonmusk",CURRENT_DATE.minusYears(2));
        printUserCache(cache,2);

        cache = TwualgerC.readUserTweetsFromFile(PATH,"robertdowneyjr",CURRENT_MINUS_72H);
        printUserCache(cache,4);

        cache = TwualgerC.readUserTweetsFromFile(PATH,"cristiano",CURRENT_DATE.minusYears(1));
        printUserCache(cache,4);
    }

    public static void test2()
    {
        System.out.println(INFO + "testing updateUserCacheFromFile");
        TwualgerC twualger = new TwualgerC(PATH);

        UserCacheB cache = TwualgerC.readUserTweetsFromFile(PATH,"elonmusk",CURRENT_DATE.minusHours(24));
        printUserCache(cache,2);

        //this should not update anything since the currentDate is actually more recent than the previous oldest
        TwualgerC.updateUserCacheFromFile(cache,PATH,"elonmusk",CURRENT_DATE);
        printUserCache(cache,2);

        TwualgerC.updateUserCacheFromFile(cache,PATH,"elonmusk",CURRENT_DATE.minusMonths(1));
        printUserCache(cache,2);

        TwualgerC.updateUserCacheFromFile(cache,PATH,"elonmusk",CURRENT_DATE.minusMonths(6));
        printUserCache(cache,2);

        TwualgerC.updateUserCacheFromFile(cache,PATH,"elonmusk",CURRENT_DATE.minusMonths(12));
        printUserCache(cache,2);


    }

    public static void test3()
    {
        System.out.println(INFO + "testing constructor & getUserCache");
        TwualgerC twualger = new TwualgerC(PATH);
        System.out.println("Total caches: " + twualger.getCaches().size());

        UserCacheB miley = twualger.getUserCache("mileycyrus");
        miley = twualger.getUserCache("mileycyrus");
        printUserCache(miley);

        UserCacheB benzema = twualger.getUserCache("benzema");
        printUserCache(benzema);
        System.out.println("Total caches: " + twualger.getCaches().size());

        UserCacheB miley2 = twualger.getUserCache("mileycyrus");
        System.out.println("Two searches retrieve the same cache object: " + (miley2 == miley));
        miley2 = twualger.getUserCache("mileycyrus");
        printUserCache(miley2);

        UserCacheB jlo = twualger.getUserCache("jlo");
        printUserCache(jlo);

        System.out.println(INFO + "Getting a top celebrity from cache, should be immediate");
        Long time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerC(PATH),
                TINY,
                cache->
                {
                    cache.getUserCache("elonmusk");
                },
                30);
        System.out.println("AET <= 0.1ms: " + ((time/1E6)<=0.1f));

        System.out.println(INFO + "Getting a non-top celebrity 10x.");
        time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerC(PATH),
                TINY,
                cache->
                {
                    for(int i = 0; i < 10; i++)
                        cache.getUserCache("benzema");
                },
                30);
        System.out.println("AET <= 0.25ms: " + ((time/1E6)<=0.25f));
    }

    public static void test4()
    {
        System.out.println(INFO + "testing buildTimeLine");
        List<String> topCelebs = Twualger.readTopCelebs(PATH);
        List<String> allCelebs = Twualger.readAllCelebs(PATH);
        List<String> following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,15));

        TwualgerC twualger = new TwualgerC(PATH);


        List<Tweet> tweets;
        System.out.println("Invalid Timeline");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusHours(48),CURRENT_DATE.minusHours(72));
        System.out.println("Total tweets retrieved: " + tweets.size());
        printOlderTweets(tweets,3);

        System.out.println("Timeline with 72 hours");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusHours(72),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        printOlderTweets(tweets,3);

        System.out.println(INFO + "Timeline with 1 month");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusMonths(1),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        printOlderTweets(tweets,3);

        System.out.println(INFO + "Timeline with 1 year");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusYears(1),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        printOlderTweets(tweets,3);
    }

    public static void test5()
    {
        System.out.println(INFO + "testing Twualger general functionality and efficiency");
        List<String> topCelebs = Twualger.readTopCelebs(PATH);
        List<String> allCelebs = Twualger.readAllCelebs(PATH);
        List<String> following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10));
        List<Tweet> tweets;
        OffsetDateTime toDate;
        OffsetDateTime fromDate;

        TwualgerC twualger = new TwualgerC(PATH);

        System.out.println(INFO + "Timeline 1Week");
        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusDays(7),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        Twualger.printTweets(tweets,2);

        for(int i = 0; i < 10; i++)
        {
            following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10+i));
            toDate = generateRandomToDate();
            fromDate = generateRandomFromDate(toDate);
            tweets = twualger.buildTimeLine(following,fromDate,toDate);
        }

        tweets = twualger.buildTimeLine(following,CURRENT_DATE.minusMonths(6),CURRENT_DATE);
        System.out.println("Total tweets retrieved: " + tweets.size());
        System.out.println(INFO + "printing older tweets");
        printOlderTweets(tweets,3);

        System.out.println("Total Searches:" + twualger.totalSearches());
        System.out.println("Hit ratio: " + twualger.cacheHitRatio());

        UserCacheB elon = twualger.getUserCache("elonmusk");
        printUserCache(elon);

        UserCacheB firstGuy = twualger.getUserCache(following.get(0));
        printUserCache(firstGuy);

        System.out.println(INFO + "Building timeline should have a good performance");
        Long time = TemporalAnalysisUtils.getAverageCPUTime(
                i->new TwualgerC(PATH),
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
                30);
        System.out.println("AET <= 20ms: " + ((time/1E6)<=20.0f));

    }

    public static void test6()
    {
        System.out.println(INFO + "testing downsizeCache");
        List<String> topCelebs = TwualgerC.readTopCelebs(PATH);
        List<String> allCelebs = TwualgerC.readAllCelebs(PATH);
        List<String> following;

        List<Tweet> tweets;
        OffsetDateTime toDate;
        OffsetDateTime fromDate;
        List<UserCacheB> caches;

        TwualgerC twualger = new TwualgerC(PATH);

        for(int i = 0; i < 10; i++)
        {
            following = new ArrayList<String>(generateRandomFollowing(topCelebs,allCelebs,10+i));

            toDate = generateRandomToDate();
            fromDate = generateRandomFromDate(toDate);
            tweets = twualger.buildTimeLine(following,fromDate,toDate);
        }

        following = new ArrayList<String>();
        following.add("elonmusk");
        following.add("niallofficial");
        following.add("kyliejenner");
        following.add("mileycyrus");
        following.add("sleepinthegardn");
        following.add("sandradewi88");
        following.add("ristomejide");
        following.add("rioferdy5");
        following.add("residente");
        following.add("radioleary");
        following.add("1loganhenderson");
        twualger.buildTimeLine(following,CURRENT_DATE.minusYears(1),CURRENT_DATE);

        System.out.println("Total searches: " + twualger.totalSearches());
        System.out.println("Hit ratio:" + twualger.cacheHitRatio());


        caches = twualger.getCaches();
        System.out.println("Total caches: " + caches.size());
        caches.sort(TwualgerCTests::compareCaches);
        UserCacheB leastSearched = getLeastSearchedNonTop(caches);
        UserCacheB mostSearched = getMostSearchedNonTop(caches);
        printUserCache(leastSearched);
        printUserCache(mostSearched);

        UserCacheB elon = twualger.getUserCache("elonmusk");
        UserCacheB niall = twualger.getUserCache("niallofficial");
        printUserCache(elon);
        printUserCache(niall);


        System.out.println(INFO + "Downsizing cache");
        twualger.downsizeCache();
        System.out.println("Total searches: " + twualger.totalSearches());
        System.out.println("Hit ratio:" + twualger.cacheHitRatio());
        caches = twualger.getCaches();
        caches.sort(TwualgerCTests::compareCaches);
        //all of these have 2 searches, and should not be deleted
        printNonTopCaches(caches);

        System.out.println("Total caches: " + caches.size());
        elon = twualger.getUserCache("elonmusk");
        niall = twualger.getUserCache("niallofficial");

        printUserCache(elon);
        printUserCache(niall);

        System.out.println(INFO + "Downsizing cache again");
        twualger.downsizeCache();
        System.out.println("Total searches: " + twualger.totalSearches());
        System.out.println("Hit ratio:" + twualger.cacheHitRatio());
        caches = twualger.getCaches();
        System.out.println("Total caches: " + caches.size());
        caches.sort(TwualgerCTests::compareCaches);
        //This should print an empty list
        printNonTopCaches(caches);

    }

    private static boolean containsCache(List<UserCacheB> lst, String userHandle)
    {
        for(UserCacheB cache : lst)
        {
            if(cache.userName.equals(userHandle)) return true;
        }
        return false;
    }

    private static List<UserCacheB> getNonTopCaches(List<UserCacheB> lst)
    {
        ArrayList<UserCacheB> nonTop = new ArrayList<>();
        for(UserCacheB cache : lst)
        {
            if(!cache.isTop) nonTop.add(cache);
        }

        return nonTop;
    }

    private static void printNonTopCaches(List<UserCacheB> lst)
    {
        System.out.print("Existing Non-Top Caches: {");
        for(UserCacheB cache : getNonTopCaches(lst))
        {
            System.out.print(cache.userName+",");
        }
        System.out.println('}');
    }

    private static UserCacheB getLeastSearchedNonTop(List<UserCacheB> lst)
    {
        int min = Integer.MAX_VALUE;
        UserCacheB leastSearched = null;
        for(UserCacheB a : lst)
        {
            if(!a.isTop && a.useCount < min)
            {
                min = a.useCount;
                leastSearched = a;
            }
        }
        return leastSearched;
    }

    private static UserCacheB getMostSearchedNonTop(List<UserCacheB> lst)
    {
        int max = Integer.MIN_VALUE;
        UserCacheB mostSearched = null;
        for(UserCacheB a : lst)
        {
            if(!a.isTop && a.useCount > max)
            {
                max = a.useCount;
                mostSearched = a;
            }
        }
        return mostSearched;
    }

    private static int compareCaches(UserCacheB a, UserCacheB b)
    {
        int compare = a.useCount-b.useCount;
        //in case of tie, order alphabetically by userName
        if(compare == 0)
        {
            compare = a.userName.compareTo(b.userName);
        }
        return compare;
    }

    private static void printUserCache(UserCacheB user, int tweetNumber)
    {
        if(tweetNumber == 2)
        {
            printUserCache(user);
            return;
        }
        String top = user.isTop? "(TOP)": "";
        int inCache = user.tweets.size();
        System.out.println(user.userName+top+": count " + user.useCount + ", tweets in cache " + inCache + ", moreInFile? " + user.moreTweetsInFile);
        printTweets(user.tweets,Math.min(tweetNumber,inCache));
    }

    private static void printUserCache(UserCacheB user)
    {
        String top = user.isTop? "(TOP)": "";
        int inCache = user.tweets.size();
        System.out.println(user.userName+top+": count " + user.useCount + ", tweets in cache " + inCache+ ", moreInFile? " + user.moreTweetsInFile);
        if(inCache>=1)
        {
            System.out.println("Newest:" + user.tweets.maxValue());
        }
        if(inCache>=2)
        {
            System.out.println("Oldest:" + user.tweets.minValue());
        }
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


    private static void printTweets(Treap<OffsetDateTime,Tweet> tweets, int n)
    {
        int index = tweets.size()-n;
        for(int i = tweets.size()-1; i>index; i--)
        {
            System.out.println(tweets.get(tweets.select(i)));
        }

        if(n>1)
        {
            System.out.println("Oldest: " + tweets.minValue());
        }
    }

    private static void printOlderTweets(List<Tweet> tweets, int n)
    {
        int index;
        for(int i = 1;i <= n; i++)
        {
            index = tweets.size()-i;
            if(index<0) break;
            System.out.println(tweets.get(index));
        }
    }

    private static void printTweets(Treap<OffsetDateTime,Tweet> tweets)
    {
        System.out.println("Oldest: " + tweets.minValue());
        System.out.println("Newest: " + tweets.maxValue());
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
