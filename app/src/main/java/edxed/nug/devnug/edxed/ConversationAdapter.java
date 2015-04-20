package edxed.nug.devnug.edxed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private List<ViewModel> items;
    private int itemLayout;
    private final FragmentActivity mActivity;
    public ItemDataSource db;

    private static final String TAG = "ConversationAdapter";

    public ConversationAdapter(List<ViewModel> items, int itemLayout, FragmentActivity mActivity) {
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
        Typeface type = Typeface.createFromAsset(mActivity.getAssets(), "fonts/PacificaCondensedRegular.ttf");
        holder.name.setTypeface(type);
        holder.name.setText(item.getName());
        holder.title.setTypeface(type);
        holder.title.setText(item.getTitle());
        holder.desc.setText(item.getDesc());
        //holder.imageView.setImageResource(item.getImg());
        holder.room.setText("Room: " + item.getRoom());
        holder.session.setText("Session " + item.getSession());
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
        Log.d(TAG, "imgString: " + item.getImgString());
        byte[] decodedString = Base64.decode(item.getImgString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageView.setImageBitmap(decodedByte);

        holder.showMore.setVisibility(View.VISIBLE);
        holder.desc.setVisibility(View.GONE);
        holder.googleCal.setVisibility(View.GONE);
        holder.remGoogleCal.setVisibility(View.GONE);

        holder.currentItem = item;
        holder.itemView.setTag(item);
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

        public ViewHolder(View itemView) {
            super(itemView);
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
                    if(ConversationFragment.addEvent(v.getContext(),currentItem)) {
                        Toast.makeText(v.getContext(), "Added to Schedule", Toast.LENGTH_LONG).show();
                        googleCal.setVisibility(View.GONE);
                        remGoogleCal.setVisibility(View.VISIBLE);
                        currentItem.setAttending("true");
                        //ConversationFragment.addEvent(v.getContext(), currentItem);
                    }

                }
            });
            remGoogleCal = (TextView) itemView.findViewById(R.id.rem_google_calendar);
            remGoogleCal.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Removed from Schedule");
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
            imageView = (ImageView)itemView.findViewById(R.id.pic);
            //imageView.setImageBitmap(decodedByte);
            //imageView.setImageResource(currentItem.getImg());
            showMore = (TextView) view.findViewById(R.id.show_more);
            showMore.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //ViewHolder view = new ViewHolder(v);
                    Log.d(TAG, "currentItem attending: " + currentItem.getAttending());
                    //Log.d(TAG, "view attending: " + view.currentItem.getAttending());
                    if(currentItem.getAttending().equals("false") || currentItem.getAttending().equals(null)) {
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
