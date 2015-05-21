package edxed.nug.devnug.edxed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Nug on 3/18/2015.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<ViewModel> items;
    private int itemLayout;
    private int titleLayout;
    private final FragmentActivity mActivity;
    public static ItemDataSource db;
    public static final String URL = "http://192.241.187.197/edxed/";

    private static final int VIEW_TYPE_FIRST  = 0;
    private static final int VIEW_TYPE_SECOND  = 1;

    private static final String TAG = "ConversationAdapter";

    public ConversationAdapter(List<ViewModel> items, int itemLayout, FragmentActivity mActivity) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mActivity = mActivity;
        titleLayout = R.layout.titles;
        db = new ItemDataSource(mActivity.getApplicationContext());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return new ViewHolder(v);
        */
        switch (viewType) {
            case VIEW_TYPE_FIRST:
                View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
                return new ViewHolder(v, false);
            case VIEW_TYPE_SECOND:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(titleLayout, parent, false);
                return new ViewHolder(v2, true);
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v, false);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch(getItemViewType(position)) {
            case VIEW_TYPE_FIRST:
                final ViewModel item = items.get(position);
                Typeface type = Typeface.createFromAsset(mActivity.getAssets(), "fonts/PacificaCondensedRegular.ttf");
                holder.name.setTypeface(type);
                holder.name.setText(item.getName());
                holder.title.setTypeface(type);
                holder.title.setText(item.getTitle());
                holder.desc.setText(item.getDesc());
                //Log.d(TAG, "Img value: " + item.getImgString2());
                ImageView view1 = holder.imageView;
                view1.setImageDrawable(null);
                ImageView view2 = holder.imageView2;
                view2.setImageDrawable(null);
                //Log.d(TAG, "img string: a" + item.getImgString2() + "b");
                if (item.getImgString2() != null) {
                    if (item.getImgString2().contains(".png")) {
                        Ion.with(view2)
                                // use a placeholder google_image if it needs to load from the network
                                .placeholder(R.drawable.nopic)
                                        // load the url
                                .load(URL + item.getImgString());
                        Ion.with(view1)
                                // use a placeholder google_image if it needs to load from the network
                                .placeholder(R.drawable.nopic)
                                        // load the url
                                .load(URL + item.getImgString2());
                    } else {
                        Ion.with(view1)
                                // use a placeholder google_image if it needs to load from the network
                                .placeholder(R.drawable.nopic)
                                        // load the url
                                .load(URL + item.getImgString());
                        //view2.setVisibility(View.INVISIBLE);
                    }
                }

                holder.room.setText("Room " + item.getRoom());
                holder.session.setText("Session " + item.getSession());

                final TextView gCal = holder.googleCal;
                final TextView rGCal = holder.remGoogleCal;

                holder.showMore.setVisibility(View.VISIBLE);
                holder.desc.setVisibility(View.GONE);
                holder.googleCal.setVisibility(View.GONE);
                holder.remGoogleCal.setVisibility(View.GONE);

                holder.currentItem = item;
                holder.itemView.setTag(item);
                break;
            case VIEW_TYPE_SECOND:
                //Log.d(TAG, "name: " + items.get(position).getName());
                ViewModel title = items.get(position);
                Typeface type2 = Typeface.createFromAsset(mActivity.getAssets(), "fonts/PacificaCondensedRegular.ttf");
                holder.name.setTypeface(type2);
                holder.name.setText(title.getName());
                //holder.title.setText(item.getTitle());
                //holder.desc.setText(item.getDesc());
                //holder.imageView.setImageResource(item.getImg());
                holder.room.setText(title.getRoom());
                if(!title.getSession().equals(""))
                    holder.session.setText("Time: " + title.getSession());
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
                //holder.divider = true;
                break;
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d(TAG, "getItemViewType position: " + position);
        if(items.get(position).getTitle().equals(""))
            return 1;
        else
            return 0;
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
        public ImageView imageView2;
        public View view;
        public ViewModel currentItem;

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
                        //Log.d(TAG, "Added to Schedule");
                        final View contextView = v;
                        if(!db.isOpenSpot(currentItem.getSession())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(contextView.getContext());
                            builder.setMessage(R.string.conflict_on_cal)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Too much work on the main thread.  Need to fix
                                            ConversationFragment.removeEvent(currentItem.getSession());
                                            ConversationFragment.addEvent(contextView.getContext(), currentItem);
                                            Toast.makeText(contextView.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                                            googleCal.setVisibility(View.GONE);
                                            remGoogleCal.setVisibility(View.VISIBLE);
                                            currentItem.setAttending("true");
                                        }
                                    })
                                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create().show();
                            /*
                        if (ConversationFragment.addEvent(v.getContext(), currentItem)) {
                            Toast.makeText(v.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                            googleCal.setVisibility(View.GONE);
                            remGoogleCal.setVisibility(View.VISIBLE);
                            currentItem.setAttending("true");
                            //ConversationFragment.addEvent(v.getContext(), currentItem);
                        }
                        */
                        }
                        else {
                            Toast.makeText(v.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                            googleCal.setVisibility(View.GONE);
                            remGoogleCal.setVisibility(View.VISIBLE);
                            currentItem.setAttending("true");
                            ConversationFragment.addEvent(v.getContext(), currentItem);
                        }


                    }
                });
                remGoogleCal = (TextView) itemView.findViewById(R.id.rem_google_calendar);
                remGoogleCal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Log.d(TAG, "Removed from Schedule");
                        final View contextView = v;
                        Toast.makeText(v.getContext(), "Removed from Schedule", Toast.LENGTH_LONG).show();
                        currentItem.setAttending("false");
                        remGoogleCal.setVisibility(View.GONE);
                        googleCal.setVisibility(View.VISIBLE);
                        ConversationFragment.removeEvent(currentItem);
                    }
                });
                //Log.d(TAG, "imgString: " + currentItem.getImgString());
                //byte[] decodedString = Base64.decode(currentItem.getImgString(), Base64.URL_SAFE);
                //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView = (ImageView) itemView.findViewById(R.id.pic);
                imageView2 = (ImageView) itemView.findViewById(R.id.pic2);
                //if(currentItem.getName().indexOf("&") == -1)
                //        imageView2.setVisibility(View.GONE);
                //imageView.setImageBitmap(decodedByte);
                //imageView.setImageResource(currentItem.getImg());
                showMore = (TextView) view.findViewById(R.id.show_more);
                showMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //ViewHolder view = new ViewHolder(v);
                        //Log.d(TAG, "currentItem attending: " + currentItem.getAttending());
                        //Log.d(TAG, "view attending: " + view.currentItem.getAttending());
                        if (currentItem.getAttending().equals("false") || currentItem.getAttending().equals(null)) {
                            desc.setVisibility(View.VISIBLE);
                            googleCal.setVisibility(View.VISIBLE);
                        } else {
                            desc.setVisibility(View.VISIBLE);
                            remGoogleCal.setVisibility(View.VISIBLE);

                        }
                        showMore.setVisibility(View.GONE);
                        //Log.d(TAG, currentItem.getName().toString() + " " +currentItem.getSession().toString());
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

}
