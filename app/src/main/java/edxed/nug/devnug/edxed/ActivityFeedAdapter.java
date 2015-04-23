package edxed.nug.devnug.edxed;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Nug on 3/18/2015.
 */
public class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityFeedAdapter.ViewHolder> {

    private List<Tweet> items;
    private int itemLayout;
    private final FragmentActivity mActivity;
    ActivityFeedAdapter adapter;
    private RecyclerView mRecyclerView;

    private static final String TAG = "ActivityFeedAdapter";

    public ActivityFeedAdapter(List<Tweet> items, int itemLayout, FragmentActivity mActivity) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet item = items.get(position);
        //Typeface type = Typeface.createFromAsset(mActivity.getAssets(), "fonts/PacificaCondensedRegular.ttf");
        holder.name.setText(item.getName());
        Log.d(TAG, "Name: " + item.getScreenName());
        holder.screenName.setText(item.getScreenName());
        Log.d(TAG, "Username: " + item.getScreenName());
        //holder.date.setText(item.getDate().toString());
        Log.d(TAG, "Date: " + item.getDate().toString());
        holder.tweetText.setText(item.getTweetText());
        Log.d(TAG, "Text: " + item.getTweetText());
        holder.currentItem = item;
        holder.itemView.setTag(item);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView screenName;
        public TextView date;
        public TextView tweetText;
        public View view;
        public Tweet currentItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            screenName = (TextView) itemView.findViewById(R.id.screen_name);
            date = (TextView) itemView.findViewById(R.id.date);
            tweetText = (TextView) itemView.findViewById(R.id.tweet_text);

        }


    }

}
