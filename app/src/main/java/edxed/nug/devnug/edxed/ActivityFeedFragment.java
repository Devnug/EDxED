package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityFeedFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private Context context;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ActivityFeedFragment newInstance(int sectionNumber) {
        ActivityFeedFragment fragment = new ActivityFeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivityFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity_feed, container, false);
        ArrayList<Tweet> tweets = null;
        try {
            tweets = getTweets("android", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context = this.getActivity().getApplicationContext();
        ListView listView = (ListView) container.findViewById(R.id.ListViewId);
        listView.setAdapter(new UserItemAdapter(this.getActivity().getApplicationContext(), R.layout.listitem, tweets));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public class UserItemAdapter extends ArrayAdapter<Tweet> {
        private ArrayList<Tweet> tweets;

        public UserItemAdapter(Context context, int textViewResourceId, ArrayList<Tweet> tweets) {
            super(context, textViewResourceId, tweets);
            this.tweets = tweets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.listitem, null);
            }

            Tweet tweet = tweets.get(position);
            if (tweet != null) {
                TextView username = (TextView) v.findViewById(R.id.username);
                TextView message = (TextView) v.findViewById(R.id.message);
                ImageView image = (ImageView) v.findViewById(R.id.avatar);

                if (username != null) {
                    username.setText(tweet.username);
                }

                if(message != null) {
                    message.setText(tweet.message);
                }

                if(image != null) {
                    image.setImageBitmap(getBitmap(tweet.image_url));
                }
            }
            return v;
        }
    }

    public Bitmap getBitmap(String bitmapUrl) {
        try {
            URL url = new URL(bitmapUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch(Exception ex) {return null;}
    }

    public ArrayList<Tweet> getTweets(String searchTerm, int page) throws JSONException {
        String searchUrl = "https://api.twitter.com/1.1/search/tweets.json?q=%23edxednyc";

        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(searchUrl);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        String responseBody = null;
        JSONArray obj = null;
        try{
            responseBody = client.execute(get, responseHandler);
            obj = new JSONArray(responseBody);
        }catch(Exception ex) {
            ex.printStackTrace();
        }

        for(int i = 0; i < obj.length(); i++) {
            Tweet tweet = new Tweet(
                    ((JSONObject)obj.get(i)).get("from_user").toString(),
                    ((JSONObject)obj.get(i)).get("text").toString(),
                    ((JSONObject)obj.get(i)).get("profile_image_url").toString()
            );
            tweets.add(tweet);
        }

        return tweets;
    }

    public class Tweet {
        public String username;
        public String message;
        public String image_url;

        public Tweet(String username, String message, String url) {
            this.username = username;
            this.message = message;
            this.image_url = url;
        }
    }
}

