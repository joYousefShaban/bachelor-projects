package aed.twualger;

import aed.tables.Treap;

import java.time.OffsetDateTime;
import java.util.ArrayList;

//this class will be used as a struct in c
//we're just using this to store and access the fields
public class UserCacheB {
    public String userName;
    public Treap<OffsetDateTime, Tweet> tweets;
    //counts the number of times this UserCache has been used
    public int useCount;
    public boolean isTop;
    //used for Problem C
    public boolean moreTweetsInFile;

    UserCacheB(String userName) {
        this.userName = userName;
        this.tweets = new Treap<>();
        this.useCount = 0;
        this.isTop = false;
        this.moreTweetsInFile = false;
    }
}
