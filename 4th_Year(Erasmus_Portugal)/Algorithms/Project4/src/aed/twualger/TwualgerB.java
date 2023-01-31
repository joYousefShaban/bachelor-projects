package aed.twualger;

import aed.tables.Treap;
import aed.twualger.tests.TwualgerATests;
import aed.twualger.tests.TwualgerBTests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TwualgerB extends Twualger {

    HashMap<String, UserCacheB> cache;
    int totalsearches, cachesearches;

    public TwualgerB(String path) {
        super(path);
        //TODO: implement

        totalsearches = 0;
        cachesearches = 0;
        cache = new HashMap<>();
        List<String> topCelebrities = readTopCelebs(path);
        for (String topCelebrity : topCelebrities) {
            UserCacheB currentUser = readUserTweetsFromFile(path, topCelebrity);
            currentUser.isTop = true;
            cache.put(topCelebrity, currentUser);
        }
    }

    public List<UserCacheB> getCaches() {
        return new ArrayList<>(this.cache.values());
    }

    public static UserCacheB readUserTweetsFromFile(String path, String userHandle) {
        //TODO: implement
        UserCacheB user = new UserCacheB(userHandle);
        try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + userHandle + ".csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 3);
                Tweet newTweet = new Tweet(Long.parseLong(values[0]), OffsetDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZZZZZ")), userHandle, values[2]);
                user.tweets.put(newTweet.date, newTweet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public UserCacheB getUserCache(String userHandle) {
        //TODO: implement

        totalsearches++;
        for (UserCacheB cache : cache.values())
            if (cache.userName.equals(userHandle)) {
                cachesearches++;
                cache.useCount++;
                return cache;
            }
        UserCacheB currentUser = readUserTweetsFromFile(path, userHandle);
        currentUser.useCount++;
        cache.put(userHandle, currentUser);
        return currentUser;
    }

    public List<Tweet> buildTimeLine(List<String> following, OffsetDateTime from, OffsetDateTime to) {
        //TODO: implement

        Treap<OffsetDateTime, Tweet> tweetList = new Treap<>();
        for (String currentUserName : following) {
            UserCacheB currentCache = getUserCache(currentUserName);
            for (OffsetDateTime tweetTime : currentCache.tweets.keys()) {
                if (from.isBefore(tweetTime) && to.isAfter(tweetTime))
                    tweetList.put(tweetTime, currentCache.tweets.get(tweetTime));
            }
        }

        List<Tweet> target = new ArrayList<>();
        tweetList.values().iterator().forEachRemaining(target::add);
        Collections.reverse(target);
        return target;
    }

    public int totalSearches() {
        //TODO: implement

        return totalsearches;
    }

    public float cacheHitRatio() {
        //TODO: implement

        float answer = (float) cachesearches / totalsearches;
        if (Float.isNaN(answer)) return 0;
        return answer;
    }

    public void downsizeCache() {
        //TODO: implement

        List<Map.Entry<String, UserCacheB>> sortedEntries = new ArrayList<>(cache.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.comparing((UserCacheB a) -> a.isTop).thenComparing(a -> a.useCount).reversed()));
//        temp.sort(Comparator.comparing((UserCacheB a) -> a.isTop).thenComparing(a -> a.useCount).reversed());
//        for (int i = 0; i < cache.size() - 1; i++)
//            System.out.println(cache.get(i).isTop + "||" + cache.get(i).useCount);

        int originalSize = sortedEntries.size();
        for (int i = originalSize - 1; i >= 0; i--) {
            if (i >= ((originalSize) / 2) && !sortedEntries.get(i).getValue().isTop)
                cache.remove(sortedEntries.get(i).getKey());
            else cache.get(sortedEntries.get(i).getKey()).useCount = 0;
        }
        totalsearches = 0;
        cachesearches = 0;
    }

    public static void main(String[] args) {
//        aed.twualger.TwualgerB twualger = new aed.twualger.TwualgerB("data");
//
//        ArrayList<String> following = new ArrayList<String>(Arrays.asList("elonmusk","robertdowneyjr","cristiano"));
//
//        List<Tweet> tweets = twualger.buildTimeLine(
//                following,
//                OffsetDateTime.of(2022,4,1,0,0,0,0, ZoneOffset.UTC),
//                OffsetDateTime.of(2022,4,29,23,59,0,0,ZoneOffset.UTC));
//
//        printTweets(tweets);

        TwualgerBTests.test5();
    }
}