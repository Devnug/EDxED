package edxed.nug.devnug.edxed;

import android.content.Intent;
import android.net.Uri;
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
 * Created by Nug on 3/24/2015.
 */
public class OrganizersAdapter extends RecyclerView.Adapter<OrganizersAdapter.ViewHolder> {

    private List<ViewModel> items;
    private int itemLayout;
    private final FragmentActivity mActivity;
    public ItemDataSource db;

    private static final String TAG = "ConversationAdapter";

    public OrganizersAdapter(List<ViewModel> items, int itemLayout, FragmentActivity mActivity) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mActivity = mActivity;
        db = new ItemDataSource(mActivity.getApplicationContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewModel item = items.get(position);
        holder.name.setText( item.getName());
        //holder.title.setText(item.getTitle());
        holder.jobTitle.setText(item.getJobTitle());
        holder.imageView.setImageResource(item.getImg());
        holder.twitter.setText(item.getTwitterHandle());
        holder.email.setText(item.getEmail());
        //Use below for when we have pictures for each person
        /*
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        Picasso.with(holder.image.getContext()).load(item.getImage()).into(holder.image);
        */

        /*
            The following is used as a workaround to some trouble I'm having with cards other then those
            being clicked are opened.  However all cards will now hide the description tag when they are
            no longer in view.
         */
        if (item.getTwitterHandle().equals("")) {
            holder.email.setVisibility(View.VISIBLE);
            holder.twitter.setVisibility(View.GONE);
        }
            holder.currentItem = item;
            holder.itemView.setTag(item);

    }


        @Override
        public int getItemCount () {
            return items.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView jobTitle;
            public TextView twitter;
            public TextView email;
            public ImageView imageView;
            public final View view;
            public ViewModel currentItem;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                name = (TextView) itemView.findViewById(R.id.name);
                jobTitle = (TextView) itemView.findViewById(R.id.job_title);
                twitter = (TextView) itemView.findViewById(R.id.twitter);
                email = (TextView) itemView.findViewById(R.id.email);
                imageView = (ImageView) itemView.findViewById(R.id.pic);
                twitter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "To Twitter");
                        final View contextView = v;
                        Uri address= Uri.parse("http://www.twitter.com/" + currentItem.getTwitterHandle().substring(1));
                        Intent browser= new Intent(Intent.ACTION_VIEW, address);
                        v.getContext().startActivity(browser);

                    }
                });
            }


        }

}
