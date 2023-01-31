package aed.twualger;

import aed.twualger.tests.TwualgerATests;

import java.io.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class TwualgerA extends Twualger {

    private ArrayList<UserCacheA> cache;
    int totalsearches, cachesearches;

    public TwualgerA(String path) {
        super(path);
        //TODO: implement

        totalsearches = 0;
        cachesearches = 0;
        cache = new ArrayList<>();
        List<String> topCelebrities = readTopCelebs(path);
        for (String topCelebrity : topCelebrities) {
            UserCacheA currentUser = readUserTweetsFromFile(path, topCelebrity);
            currentUser.isTop = true;
            cache.add(currentUser);
        }
    }

    public List<UserCacheA> getCaches() {
        return this.cache;
    }

    public static UserCacheA readUserTweetsFromFile(String path, String userHandle) {
        //TODO: implement

        UserCacheA user = new UserCacheA(userHandle);
        try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + userHandle + ".csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 3);
                Tweet newTweet = new Tweet(Long.parseLong(values[0]), OffsetDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZZZZZ")), userHandle, values[2]);
                user.tweets.add(newTweet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public UserCacheA getUserCache(String userHandle) {
        //TODO: implement

        totalsearches++;
        for (UserCacheA cache : cache)
            if (cache.userName.equals(userHandle)) {
                cachesearches++;
                cache.useCount++;
                return cache;
            }
        UserCacheA currentUser = readUserTweetsFromFile(path, userHandle);
        currentUser.useCount++;
        cache.add(currentUser);
        return currentUser;
    }

    public List<Tweet> buildTimeLine(List<String> following, OffsetDateTime from, OffsetDateTime to) {
        //TODO: implement

        ArrayList<Tweet> tweetList = new ArrayList<>();
        for (String currentUserName : following) {
            UserCacheA currentCache = getUserCache(currentUserName);
            for (int i = 0; i < currentCache.tweets.size(); i++) {
                if (from.isBefore(currentCache.tweets.get(i).date) && to.isAfter(currentCache.tweets.get(i).date))
                    tweetList.add(currentCache.tweets.get(i));
            }
        }

        tweetList.sort(Comparator.comparing((Tweet a) -> a.date).reversed());
        return tweetList;
    }

    public int totalSearches() {
        //TODO: implement

        return totalsearches;
    }

    public float cacheHitRatio() {
        //TODO: implement

        float answer = (float) cachesearches / totalsearches;
        if (Float.isNaN(answer))
            return 0;
        return answer;
    }

    public void downsizeCache() {
        //TODO: implement

        cache.sort(Comparator.comparing((UserCacheA a) -> a.isTop).thenComparing(a -> a.useCount).reversed());
//        for (int i = 0; i < cache.size() - 1; i++)
//            System.out.println(cache.get(i).isTop + "||" + cache.get(i).useCount);
        int originalSize = cache.size();
        for (int i = originalSize - 1; i >= 0; i--) {
            if (i >= ((originalSize) / 2) && !cache.get(i).isTop)
                cache.remove(cache.get(i));
            else
                cache.get(i).useCount = 0;
        }
        totalsearches = 0;
        cachesearches = 0;
    }

    public static void main(String[] args) {
//        TwualgerA twualger = new TwualgerA("data");
//
//        ArrayList<String> following = new ArrayList<String>(Arrays.asList("elonmusk","robertdowneyjr","cristiano"));
//
//        List<Tweet> tweets = twualger.buildTimeLine(
//                following,
//                OffsetDateTime.of(2022,4,1,0,0,0,0, ZoneOffset.UTC),
//                OffsetDateTime.of(2022,4,29,23,59,0,0,ZoneOffset.UTC));
//
//        printTweets(tweets);
//
//        System.out.println("Cache size: " + twualger.cache.size());
//        twualger.downsizeCache();
//        System.out.println("Cache size: " + twualger.cache.size());

        TwualgerATests.test3();
    }
}
