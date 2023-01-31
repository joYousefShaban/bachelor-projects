package aed.twualger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public class Tweet {

    public long id;
    public OffsetDateTime date;
    public String handle;
    public String text;

    public Tweet(long id, OffsetDateTime date, String handle, String text)
    {
        this.id = id;
        this.date = date;
        this.handle = handle;
        this.text = text;
    }

    public String toString()
    {
        String shortText = this.text.length() >= 50 ? this.text.substring(0,47)+"..." : this.text;
        return this.date + ", " + this.handle + ":" + shortText;
    }
}
