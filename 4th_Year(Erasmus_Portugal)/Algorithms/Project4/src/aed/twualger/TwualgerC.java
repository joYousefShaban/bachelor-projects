package aed.twualger;

import aed.tables.Treap;
import aed.twualger.tests.TwualgerCTests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TwualgerC extends Twualger {

    //ideally we would use current datetime, but we only have tweet data until July 11th
    private static final OffsetDateTime CURRENT_DATE = OffsetDateTime.of(2022, 07, 11, 23, 59, 59, 0, ZoneOffset.UTC);
    private static final OffsetDateTime CURRENT_MINUS_72H = CURRENT_DATE.minusHours(72);

    HashMap<String, UserCacheB> cache;
    int totalSearches, cacheSearches;

    public TwualgerC(String path) {
        super(path);
        //TODO: implement

        totalSearches = 0;
        cacheSearches = 0;
        cache = new HashMap<>();
        List<String> topCelebrities = readTopCelebs(path);
        for (String topCelebrity : topCelebrities) {
            UserCacheB currentUser = readUserTweetsFromFile(path, topCelebrity, CURRENT_MINUS_72H);
            currentUser.isTop = true;
            cache.put(topCelebrity, currentUser);
        }
    }

    public List<UserCacheB> getCaches() {
        return new ArrayList<UserCacheB>(this.cache.values());
    }


    public static UserCacheB readUserTweetsFromFile(String path, String userHandle, OffsetDateTime oldest) {
        //TODO: implement

        UserCacheB user = new UserCacheB(userHandle);
        try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + userHandle + ".csv"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 3);
                OffsetDateTime tweetTime = OffsetDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZZZZZ"));
                if (oldest.isAfter(tweetTime)) {
                    user.moreTweetsInFile = true;
                    return user;
                }
                Tweet newTweet = new Tweet(Long.parseLong(values[0]), tweetTime, userHandle, values[2]);
                user.tweets.put(tweetTime, newTweet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static void updateUserCacheFromFile(UserCacheB userCache, String path, String userHandle, OffsetDateTime oldest) {
        //TODO: implement

        try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + userHandle + ".csv"))) {
            String line;
            for (int i = 0; i <= userCache.tweets.size(); i++)
                br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", 3);
                OffsetDateTime tweetTime = OffsetDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZZZZZ"));
                if (oldest.isAfter(tweetTime)) {
                    userCache.moreTweetsInFile = true;
                    return;
                }
                Tweet newTweet = new Tweet(Long.parseLong(values[0]), tweetTime, userHandle, values[2]);
                userCache.tweets.put(tweetTime, newTweet);
            }
            userCache.moreTweetsInFile = false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UserCacheB getUserCache(String userHandle) {
        //TODO: implement

        totalSearches++;
        for (UserCacheB cache : cache.values())
            if (cache.userName.equals(userHandle)) {
                cacheSearches++;
                cache.useCount++;
                return cache;
            }
        UserCacheB currentUser = readUserTweetsFromFile(path, userHandle, CURRENT_MINUS_72H);
        currentUser.useCount++;
        cache.put(userHandle, currentUser);
        return currentUser;
    }

    public List<Tweet> buildTimeLine(List<String> following, OffsetDateTime from, OffsetDateTime to) {
        //TODO: implement

        //tweetList will contain all tweets from the range of (OffsetDateTime from, OffsetDateTime to)
        Treap<OffsetDateTime, Tweet> tweetList = new Treap<>();
        for (String currentUserName : following) {
            UserCacheB currentCache = getUserCache(currentUserName);
            //oldestDate will contain the oldest date of (OffsetDateTime from, or the minimum OffsetDateTime in currentCache)
            OffsetDateTime oldestDate;
            if (currentCache.tweets.size() > 0 && currentCache.tweets.min().isBefore(from))
                oldestDate = currentCache.tweets.min();
            else oldestDate = from;

            if (currentCache.moreTweetsInFile) {
                updateUserCacheFromFile(currentCache, path, currentUserName, oldestDate);
                //fill in tweetList with the tweets in the range of (OffsetDateTime from, OffsetDateTime to)
                for (OffsetDateTime tweetTime : currentCache.tweets.keys()) {
                    if (to.isAfter(tweetTime)) tweetList.put(tweetTime, currentCache.tweets.get(tweetTime));
                }
            }
        }

        List<Tweet> target = new ArrayList<>();
        tweetList.values().iterator().forEachRemaining(target::add);
        Collections.reverse(target);
        return target;
    }

    public int totalSearches() {
        //TODO: implement

        return totalSearches;
    }

    public float cacheHitRatio() {
        //TODO: implement

        float answer = (float) cacheSearches / totalSearches;
        if (Float.isNaN(answer)) return 0;
        return answer;
    }

    public void downsizeCache() {
        //TODO: implement

        List<Map.Entry<String, UserCacheB>> sortedEntries = new ArrayList<>(cache.entrySet());
        sortedEntries.sort(Map.Entry.comparingByValue(Comparator.comparing((UserCacheB a) -> a.isTop).thenComparing(a -> a.useCount).reversed()));

        int originalSize = sortedEntries.size();
        for (int i = originalSize - 1; i >= 0; i--) {
            if (i >= ((originalSize) / 2.0) && !sortedEntries.get(i).getValue().isTop)
                cache.remove(sortedEntries.get(i).getKey());
            else {
//                updateUserCacheFromFile(cache.get(sortedEntries.get(i).getKey()), path, sortedEntries.get(i).getKey(), CURRENT_MINUS_72H);
                UserCacheB newCache = readUserTweetsFromFile(path, sortedEntries.get(i).getKey(), CURRENT_MINUS_72H);
                cache.get(sortedEntries.get(i).getKey()).tweets = newCache.tweets;
                cache.get(sortedEntries.get(i).getKey()).moreTweetsInFile = newCache.moreTweetsInFile;
                cache.get(sortedEntries.get(i).getKey()).useCount = 0;
            }
        }
        totalSearches = 0;
        cacheSearches = 0;
    }

    public static void main(String[] args) {

//        TwualgerC twualger = new TwualgerC("data");
//
//        ArrayList<String> following = new ArrayList<String>(Arrays.asList("elonmusk","RobertDowneyJr","Cristiano"));
//
//        List<Tweet> tweets = twualger.buildTimeLine(
//                following,
//                OffsetDateTime.of(2022,4,1,0,0,0,0, ZoneOffset.UTC),
//                OffsetDateTime.of(2022,4,29,23,59,0,0,ZoneOffset.UTC));
//
//        printTweets(tweets);

        TwualgerCTests.test4();
    }
}