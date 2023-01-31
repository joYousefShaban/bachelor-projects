package aed.twualger;

import java.util.ArrayList;

//this class will be used as a struct in c
//we're just using this to store and access the fields
public class UserCacheA {
    public String userName;
    public ArrayList<Tweet> tweets;
    //counts the number of times this UserCache has been used
    public int useCount;
    public boolean isTop;

    UserCacheA(String userName)
    {
        this.userName = userName;
        this.tweets = new ArrayList<>();
        this.useCount = 0;
        this.isTop = false;
    }
}
