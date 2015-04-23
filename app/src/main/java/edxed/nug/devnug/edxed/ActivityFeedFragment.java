package edxed.nug.devnug.edxed;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A placeholder fragment containing a simple view.
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
    static ActivityFeedFragment mFragment;
    private LinearLayoutManager mLinearLayoutManager;
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ActivityFeedAdapter(list,R.layout.tweet_item, mActivity);
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
        createList(result);
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

    public void createList(QueryResult result) {
        for (twitter4j.Status status : result.getTweets()) {
            list.add(new Tweet(status.getUser().getScreenName(),"@" + status.getUser().getScreenName(), status.getCreatedAt(), status.getText()));
            //System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
        }
        adapter.notifyDataSetChanged();
    }

}

