package edxed.nug.devnug.edxed;

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
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ViewModel> items;
    private int itemLayout;
    private int titleLayout;
    private final FragmentActivity mActivity;
    public ItemDataSource db;

    private static final int VIEW_TYPE_FIRST  = 0;
    private static final int VIEW_TYPE_SECOND  = 1;

    private static final String TAG = "ScheduleAdapter";

    public ScheduleAdapter(List<ViewModel> items, int itemLayout, FragmentActivity mActivity) {
        this.items = items;
        this.itemLayout = itemLayout;
        titleLayout = R.layout.titles;
        this.mActivity = mActivity;
        db = new ItemDataSource(mActivity.getApplicationContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder viewType: " + viewType);
        switch (viewType) {
            case VIEW_TYPE_FIRST:
                View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
                return new ViewHolder(v, false);
            case VIEW_TYPE_SECOND:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(titleLayout, parent, false);
                return new ViewHolder(v2, true);
        }
        //View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        //return new ViewHolder(v);

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "Position: " + position);
        switch(getItemViewType(position)) {
            case VIEW_TYPE_FIRST:
                ViewModel item = items.get(position);
                holder.name.setText(item.getTitle() + " WITH " + item.getName());
                //holder.title.setText(item.getTitle());
                holder.desc.setText(item.getDesc());
                holder.imageView.setImageResource(item.getImg());
                holder.room.setText("Room: " + item.getRoom());
                holder.session.setText("Session " + item.getSession());
                /*
                The following is used as a workaround to some trouble I'm having with cards other then those
                being clicked are opened.  However all cards will now hide the description tag when they are
                no longer in view.
                */
                holder.showMore.setVisibility(View.VISIBLE);
                holder.desc.setVisibility(View.GONE);
                holder.googleCal.setVisibility(View.GONE);
                holder.remGoogleCal.setVisibility(View.GONE);

                holder.currentItem = item;
                holder.itemView.setTag(item);
                holder.divider = false;
                break;
            case VIEW_TYPE_SECOND:
                Log.d(TAG, "name: " + items.get(position).getName());
                ViewModel title = items.get(position);
                holder.name.setText(title.getName());
                //holder.title.setText(item.getTitle());
                //holder.desc.setText(item.getDesc());
                //holder.imageView.setImageResource(item.getImg());
                if(!title.getRoom().equals("TBD"))
                    holder.room.setText("Room: " + title.getRoom());
                if(!title.getSession().equals(""))
                    holder.session.setText("Time " + title.getSession());
                /*
                The following is used as a workaround to some trouble I'm having with cards other then those
                being clicked are opened.  However all cards will now hide the description tag when they are
                no longer in view.
                */
                //holder.showMore.setVisibility(View.VISIBLE);
                //holder.desc.setVisibility(View.GONE);
                //holder.googleCal.setVisibility(View.GONE);
                //holder.remGoogleCal.setVisibility(View.GONE);

                holder.currentItem = title;
                holder.itemView.setTag(title);
                holder.divider = true;
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType position: " + position);
        if(items.get(position).getTitle().equals(""))
            return 1;
        else
            return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView title;
        public TextView desc;
        public TextView room;
        public TextView session;
        public TextView showMore;
        public TextView googleCal;
        public TextView remGoogleCal;
        public ImageView imageView;
        public View view;
        public ViewModel currentItem;
        public boolean divider;

        public ViewHolder(View itemView, boolean isTitle) {
            super(itemView);
            if (!isTitle) {
                view = itemView;
                name = (TextView) itemView.findViewById(R.id.name);
                title = (TextView) itemView.findViewById(R.id.title);
                desc = (TextView) itemView.findViewById(R.id.desc);
                room = (TextView) itemView.findViewById(R.id.room);
                session = (TextView) itemView.findViewById(R.id.session);
                room.setText("Room: ");
                googleCal = (TextView) itemView.findViewById(R.id.google_calendar);
                googleCal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Added to Schedule");
                        final View contextView = v;
                        Toast.makeText(v.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                        googleCal.setVisibility(View.GONE);
                        remGoogleCal.setVisibility(View.VISIBLE);
                        currentItem.setAttending("true");
                        ConversationFragment.addEvent(v.getContext(), currentItem);

                    }
                });
                remGoogleCal = (TextView) itemView.findViewById(R.id.rem_google_calendar);
                final View itView = itemView;
                remGoogleCal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Removed from Schedule");
                        final View contextView = v;
                        Toast.makeText(v.getContext(), "Removed from Schedule", Toast.LENGTH_LONG).show();
                        currentItem.setAttending("false");
                        remGoogleCal.setVisibility(View.GONE);
                        googleCal.setVisibility(View.VISIBLE);
                        ScheduleFragment.removeCard(itView);
                        ScheduleFragment.removeEvent(currentItem);
                    }
                });
                imageView = (ImageView) itemView.findViewById(R.id.pic);
                //imageView.setImageResource(currentItem.getImg());
                showMore = (TextView) view.findViewById(R.id.show_more);
                showMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //ViewHolder view = new ViewHolder(v);
                        Log.d(TAG, "currentItem attending: " + currentItem.getAttending());
                        //Log.d(TAG, "view attending: " + view.currentItem.getAttending());
                        if (currentItem.getAttending().equals("false")) {
                            desc.setVisibility(View.VISIBLE);
                            googleCal.setVisibility(View.VISIBLE);
                        } else {
                            desc.setVisibility(View.VISIBLE);
                            remGoogleCal.setVisibility(View.VISIBLE);

                        }
                        showMore.setVisibility(View.GONE);
                        Log.d(TAG, currentItem.getName().toString());
                        //Log.d(TAG, view.currentItem.getName().toString());
                    }
                });
            }
            else {
                    view = itemView;
                    name = (TextView) itemView.findViewById(R.id.name);
                    //title = (TextView) itemView.findViewById(R.id.title);
                    //desc = (TextView) itemView.findViewById(R.id.desc);
                    room = (TextView) itemView.findViewById(R.id.room);
                    session = (TextView) itemView.findViewById(R.id.session);
            }

        }


    }

    public static class ViewTitleHolder extends RecyclerView.ViewHolder {
        //public TextView name;
        public TextView title;
        //public TextView desc;
        public TextView room;
        public TextView session;
        //public TextView showMore;
        //public TextView googleCal;
        //public TextView remGoogleCal;
        //public ImageView imageView;
        public View view;
        public ViewModel currentItem;

        public ViewTitleHolder(View itemView) {
            super(itemView);
            view = itemView;
            //name = (TextView) itemView.findViewById(R.id.name);
            title = (TextView) itemView.findViewById(R.id.title);
            //desc = (TextView) itemView.findViewById(R.id.desc);
            room = (TextView) itemView.findViewById(R.id.room);
            session = (TextView) itemView.findViewById(R.id.session);
            //session.setText("Session 1");
            //room.setText("Room: ");
            //googleCal = (TextView) itemView.findViewById(R.id.google_calendar);
            /*googleCal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Added to Schedule");
                    final View contextView = v;
                    Toast.makeText(v.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                    googleCal.setVisibility(View.GONE);
                    remGoogleCal.setVisibility(View.VISIBLE);
                    currentItem.setAttending("true");
                    ConversationFragment.addEvent(v.getContext(),currentItem);

                }
            });
            remGoogleCal = (TextView) itemView.findViewById(R.id.rem_google_calendar);
            final View itView = itemView;
            remGoogleCal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Removed from Schedule");
                    final View contextView = v;
                    Toast.makeText(v.getContext(), "Removed from Schedule", Toast.LENGTH_LONG).show();
                    currentItem.setAttending("false");
                    remGoogleCal.setVisibility(View.GONE);
                    googleCal.setVisibility(View.VISIBLE);
                    ScheduleFragment.removeCard(itView);
                    ScheduleFragment.removeEvent(currentItem);
                }
            });
            imageView = (ImageView)itemView.findViewById(R.id.pic);
            //imageView.setImageResource(currentItem.getImg());
            showMore = (TextView) view.findViewById(R.id.show_more);
            showMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //ViewHolder view = new ViewHolder(v);
                    Log.d(TAG, "currentItem attending: " + currentItem.getAttending());
                    //Log.d(TAG, "view attending: " + view.currentItem.getAttending());
                    if(currentItem.getAttending().equals("false")) {
                        desc.setVisibility(View.VISIBLE);
                        googleCal.setVisibility(View.VISIBLE);
                    }
                    else {
                        desc.setVisibility(View.VISIBLE);
                        remGoogleCal.setVisibility(View.VISIBLE);

                    }
                    showMore.setVisibility(View.GONE);
                    Log.d(TAG, currentItem.getName().toString());
                    //Log.d(TAG, view.currentItem.getName().toString());
                }
            });
            /*
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, currentItem.getName().toString());
                    Log.d(TAG, v.getTag().toString());

                }
            });
            */
        }


    }

}
