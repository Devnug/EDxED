package edxed.nug.devnug.edxed;

import java.util.Date;

/**
 * Created by Nug on 4/22/2015.
 */
public class Tweet {
    private String name;
    private String screenName;
    private Date date;
    private String tweetText;

    public Tweet(String name, String screenName, Date date, String tweetText) {
        this.name = name;
        this.screenName = screenName;
        this.date = date;
        this.tweetText = tweetText;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getTweetText() {
        return tweetText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
