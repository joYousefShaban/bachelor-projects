package aed.twualger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Twualger {

    protected String path;

    public Twualger(String path)
    {
        this.path = path;
    }

    public static void printTweets(List<Tweet> tweets, int n)
    {
        for(Tweet t : tweets)
        {
            if(n-- == 0) break;
            System.out.println(t);
        }
    }

    public static void printTweets(List<Tweet> tweets)
    {
        for(Tweet t : tweets)
        {
            System.out.println(t);
        }
    }

    public static List<String> readAllCelebs(String path)
    {
        return readCelebs(path,"__all_celebs.csv");
    }

    public static List<String> readTopCelebs(String path)
    {
        return readCelebs(path,"__top_celebs.csv");
    }

    private static List<String> readCelebs(String path, String fileName)
    {
        ArrayList<String> celebs = new ArrayList<>();

        try
        {
            File file = new File(path + "/" + fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String name;

            while((name = br.readLine()) != null)
            {
                celebs.add(name);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return celebs;
    }
}
