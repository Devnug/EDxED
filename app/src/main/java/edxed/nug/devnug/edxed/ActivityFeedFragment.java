package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A placeholder fragment containing a card view of recent tweets for #edxednyc.
 */
public class ActivityFeedFragment extends Fragment implements LoadingTwitterTask.LoadingTwitterTaskFinishedListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private Context context;
    static FragmentActivity mActivity;
    ArrayList<Tweet> list = new ArrayList<Tweet>();
    private RecyclerView mRecyclerView;
    ActivityFeedAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isSwipeRefreshing = false;
    static ActivityFeedFragment mFragment;
    private LinearLayoutManager mLinearLayoutManager;
    TextView noConnection;
    ProgressBar progress;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String TAG = "ActivityFeedFragment";

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
        mSwipeRefreshLayout =(SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!progress.isShown() && !isSwipeRefreshing) {
                    //progress.setVisibility(View.VISIBLE);
                    isSwipeRefreshing = true;
                    list.clear();
                    new LoadingTwitterTask(mActivity, ActivityFeedFragment.this).execute("www.google.co.uk");
                }
            }
        });
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ActivityFeedAdapter(list,R.layout.tweet_item, mActivity);
        noConnection = (TextView) rootView.findViewById(R.id.no_connection);
        progress = (ProgressBar) rootView.findViewById((R.id.loading));
        setHasOptionsMenu(true);
        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        this.mActivity = (FragmentActivity)activity;
        new LoadingTwitterTask(mActivity, this).execute("www.google.co.uk");
    }

    @Override
    public void onTaskFinished(QueryResult result) {
        System.out.println("Finished!");
        if(result != null) {
            createList(result);
            progress.setVisibility(View.GONE);
        }
        else {
            progress.setVisibility(View.GONE);
            noConnection.setVisibility((View.VISIBLE));
        }
        if(isSwipeRefreshing) {
            isSwipeRefreshing = false;
            mSwipeRefreshLayout.setRefreshing(false);
        }

        //mActivity.getSupportFragmentManager().beginTransaction().detach(mFragment).attach(mFragment).commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mRecyclerviewAdapter = new RecyclerView.Adapter(list);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.feed_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.feed_menu, menu);
        /*
        menu.add(0, R.menu.main, 0,
                getResources().getString(R.string.action_refresh))
                .setIcon(android.R.drawable.ic_menu_recent_history)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Check for action bar button clicks

        //Make sure to refresh only when the application is not already performing a refresh opertation
        if (id == R.id.action_refresh && !progress.isShown() && !isSwipeRefreshing) {
            progress.setVisibility(View.VISIBLE);
            list.clear();
            adapter.notifyDataSetChanged();
            new LoadingTwitterTask(mActivity, this).execute("www.google.co.uk");
            return true;
        }
        else if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     *   @param result - the result of a query from the Twitter4.j library.  Each result is stored in an ArrayList with its pertinent details
     */
    public void createList(QueryResult result) {
        //Log.d(TAG, "Number of entries: " + result.getTweets().size());
        for (twitter4j.Status status : result.getTweets()) {
            // ignore the retweets to unclutter the screen
            //Log.d(TAG, "Username: " + status.getUser().getScreenName());
            if(!status.getText().substring(0,2).equals("RT") && status.getMediaEntities().length!=0) {
                list.add(new Tweet(mActivity, status.getUser().getScreenName(), "@" + status.getUser().getScreenName(), status.getCreatedAt(), fixLinks(status.getText()), status.getUser().getProfileImageURL(), status.getMediaEntities()[0].getMediaURL()));
            }
            else if(!status.getText().substring(0,2).equals("RT")) {
                list.add(new Tweet(mActivity, status.getUser().getScreenName(), "@" + status.getUser().getScreenName(), status.getCreatedAt(), fixLinks(status.getText()), status.getUser().getProfileImageURL()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
        @param text - text that possibly contains urls from a Twitter.  These will be parsed and
        replaced with a working link version in the text.
     */
    public String fixLinks(String text) {
        //clear newline characters that lead to link errors
        if(text.contains("\n")) {
            text = text.replaceAll("\n", " ");
        }
        String afterLink = text;
        while(afterLink.indexOf("http") != -1) {
            if (afterLink.indexOf("http") != -1 && afterLink.indexOf(" ", afterLink.indexOf("http")) != -1) {
                String link = afterLink.substring(afterLink.indexOf("http"), afterLink.indexOf(" ", afterLink.indexOf("http")));
                //afterLink = text.substring(text.indexOf(" ", text.indexOf("http")));
                text = text.replace(link, "<a href=\"" + link + "\">" + link + "</a>");
                afterLink = text.substring(text.lastIndexOf("</a>"));
                //Log.d(TAG, "With fixed link: " + text);
                if(text.length() > 750)
                    return "Error parsing tweet";
            } else {
                String link = afterLink.substring(afterLink.indexOf("http"));
                text = text.replace(link, "<a href=\"" + link + "\">" + link + "</a>");
                //Log.d(TAG, "With fixed link: " + text);
                afterLink = "";
            }
        }
        return fixUser(text);
    }

    /**
        @param text - text that possibly contains usernames for Tweet.  These will be parsed and
        replaced with a working link version in the text.
     */
    public String fixUser(String text) {
        //if(text.indexOf("http") != -1 && text.indexOf(" ", text.indexOf("http")) != -1) {
        String afterLink = text;
        while(afterLink.indexOf("@") != -1) {
            if (afterLink.indexOf("@") != -1 && afterLink.indexOf(" ", afterLink.indexOf("@")) != -1 && afterLink.substring(afterLink.indexOf("@"),afterLink.indexOf(" ", afterLink.indexOf("@"))).length() != 1 ) {
                String link = afterLink.substring(afterLink.indexOf("@") + 1, afterLink.indexOf(" ", afterLink.indexOf("@")));
                //afterLink = text.substring(text.indexOf(" ", text.indexOf("@")));
                text = text.replace("@" + link, "<a href=\"http://www.twitter.com/" + link + "\">" + "@" + link + "</a>");
                //Log.d(TAG, "afterLink: " + afterLink);
                afterLink = afterLink.substring(afterLink.indexOf(link));
                //afterLink = text.substring(text.indexOf("a>", text.indexOf("@")));
                //afterLink = text.substring(text.indexOf(" ", text.indexOf("@")));
                //Log.d(TAG, "With fixed link: " + text);
                if (text.length() > 750) {
                    return "error with text";
                }
            } /*
            if (afterLink.indexOf("@") != -1 && afterLink.indexOf(" ", afterLink.indexOf("@")) != -1 && afterLink.indexOf(" ", afterLink.indexOf("@")) - afterLink.indexOf("@") == 1) {
                    afterLink = afterLink.substring(afterLink.indexOf("@") + 1);
            }*/
            else {
                String link = afterLink.substring(afterLink.indexOf("@") + 1);
                text = text.replace("@" + link, "<a href=\"http://www.twitter.com/" + link + "\">" + "@" + link + "</a>");
                //Log.d(TAG, "With fixed link: " + text);
                afterLink = "";
            }
        }
        return text;
    }

    private static final String FRAGMENT_TITLE = "Activity Feed";

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_TITLE);
    }

}

