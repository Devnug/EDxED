package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by Nug on 4/22/2015.
 */
public class Tweet implements LoadingPicTask.LoadingPicTaskFinishedListener{
    private String name;
    private String screenName;
    private Date date;
    private String tweetText;
    private String userPicUrl;
    private String tweetPicUrl;
    private Bitmap pic;

    // If no media entities are present
    public Tweet(Activity mActivity, String name, String screenName, Date date, String tweetText, String userPicUrl) {
        this.name = name;
        this.screenName = screenName;
        this.date = date;
        this.tweetText = tweetText;
        this.userPicUrl = userPicUrl;
        //new LoadingPicTask(mActivity, this).execute(userPicUrl);
        //imageView.setImageBitmap(bmp);
    }

    // Media entities are present
    public Tweet(Activity mActivity, String name, String screenName, Date date, String tweetText, String userPicUrl, String tweetPicUrl) {
        this.name = name;
        this.screenName = screenName;
        this.date = date;
        this.tweetText = tweetText;
        this.userPicUrl = userPicUrl;
        this.tweetPicUrl = tweetPicUrl;
        //new LoadingPicTask(mActivity, this).execute(userPicUrl);
        //imageView.setImageBitmap(bmp);
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

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    @Override
    public void onTaskFinished(Bitmap result) {
        pic = result;
        //mActivity.getSupportFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();
    }

    public String getTweetPicUrl() {
        return tweetPicUrl;
    }

    public void setTweetPicUrl(String tweetPicUrl) {
        this.tweetPicUrl = tweetPicUrl;
    }
}
