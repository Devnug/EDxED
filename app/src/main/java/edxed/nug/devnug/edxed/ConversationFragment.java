package edxed.nug.devnug.edxed;

/**
 * Created by Nug on 3/18/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationFragment extends Fragment {
    private static final String TAG = "Conversion Fragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    ConversationAdapter adapter;
    static FragmentActivity mActivity;
    static ItemDataSource db;
    ArrayList<ViewModel> list;

    //private ArrayList<String> list = new ArrayList<String>();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConversationFragment newInstance(int sectionNumber) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        // Create database on first run
        return fragment;
    }

    public ConversationFragment() {

        // Create database on first run
        /*
        if(!db.hasEntries()) {
            db.createBaseTable();
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_conversations, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        adapter = new ConversationAdapter(list,R.layout.item2, mActivity);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (FragmentActivity) activity;
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        db = new ItemDataSource(this.mActivity.getApplicationContext());
        createList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mRecyclerviewAdapter = new RecyclerView.Adapter(list);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    //  Creates a base list and populates the ArrayList list to ensure that the cardview has entries
    public void createList() {
        list = new ArrayList<ViewModel>();
        Cursor c = db.queryAll();
        if(c.moveToFirst()) {
            int idColumn = c.getColumnIndex(ItemDbHelper.COLUMN_ID);
            int nameColumn = c.getColumnIndex(ItemDbHelper.KEY_NAME);
            int titleColumn = c.getColumnIndex(ItemDbHelper.KEY_TITLE);
            int descColumn = c.getColumnIndex(ItemDbHelper.KEY_DESC);
            int roomColumn = c.getColumnIndex(ItemDbHelper.KEY_ROOM);
            int sessionColumn = c.getColumnIndex(ItemDbHelper.KEY_SESSION);
            int attendingColumn = c.getColumnIndex(ItemDbHelper.KEY_ATTENDING);
            int eventIdColumn = c.getColumnIndex(ItemDbHelper.KEY_EVENT_ID);
            int picColumn = c.getColumnIndex(ItemDbHelper.KEY_PIC);
            int pic2Column = c.getColumnIndex(ItemDbHelper.KEY_PIC2);
            int cancelledColumn = c.getColumnIndex(ItemDbHelper.KEY_CANCELLED);
            //Log.d(TAG, "Name: " + c.getString(nameColumn));
            do {
                //Log.d(TAG, "cancelled: " + c.getString(cancelledColumn));
                if(c.getString(cancelledColumn) == null || !c.getString(cancelledColumn).equals("yes"))
                    list.add(new ViewModel(c.getString(nameColumn),c.getString(titleColumn), c.getString(descColumn), c.getString(roomColumn), c.getString(attendingColumn), c.getString(sessionColumn), 0, c.getInt(idColumn), c.getString(picColumn), c.getString(pic2Column)));

            } while(c.moveToNext());
        }
        c.close();
        boolean twoFound = false;
        boolean threeFound = false;
        list.add(0, new ViewModel("Session 1", "", "", " ", "true", "9:30-11:00", "", ""));
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getSession().equals("2") && !twoFound) {
                list.add(i, new ViewModel("Session 2", "", "", " ", "true", "12:00-1:30", "", ""));
                twoFound = true;
            }
            if(list.get(i).getSession().equals("3") && !threeFound) {
                list.add(i, new ViewModel("Session 3", "", "", " ", "true", "1:30-3:00", "", ""));
                threeFound = true;
            }
        }
    }

    public static void removeEvent(ViewModel item) {
        db.updateAttending(item.getName());
    }

    public static void removeEvent(String session) { db.removeSession(session); }

    // Unusued as of Version 1.0
    public static boolean addEvent(final Context context, ViewModel item) {
        // Intent calIntent = new Intent(Intent.ACTION_INSERT);
        // calIntent.setData(CalendarContract.Events.CONTENT_URI);
        // context.startActivity(calIntent);
        final ViewModel viewItem = item;

        if(!db.isOpenSpot(item.getSession())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(R.string.conflict_on_cal)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            removeEvent(viewItem.getSession());
                            addEvent(context, viewItem);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
        }
        else {
            db.updateAttending(item.getName());
            //final ViewModel viewItem = item;

            // Adding to Google Calendar disabled until I can figure out an easy way to remove an event
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(R.string.add_to_cal)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setData(CalendarContract.Events.CONTENT_URI);
                            //intent.setType("vnd.android.cursor.item/event");
                            intent.putExtra(CalendarContract.Events.TITLE, viewItem.getTitle());
                            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, viewItem.getRoom());
                            intent.putExtra(CalendarContract.Events.DESCRIPTION, "EDxED NYC");

                            // Setting dates
                            //Log.d(TAG, "ID = " + CalendarContract.Calendars.);
                            GregorianCalendar calDate = new GregorianCalendar(2015, 5, 4);
                            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    calDate.getTimeInMillis());
                            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                    calDate.getTimeInMillis() + 60 * 60 * 1000);

                            // Make it a full day event
                            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

                            // Making it private and shown as busy
                            //intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
                            //intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                            //intent.res
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();
            */
            return true;
        }
        return false;

        /*
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        //intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, item.getTitle());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "HHSLT");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "EDxED NYC");

        // Setting dates
        //Log.d(TAG, "ID = " + CalendarContract.Calendars.);
        GregorianCalendar calDate = new GregorianCalendar(2015, 5, 4);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis() + 60*60*1000);

        // Make it a full day event
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        // Making it private and shown as busy
        //intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PUBLIC);
        //intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        //intent.res
        context.startActivity(intent);
        */
        /*
        Calendar cal = Calendar.getInstance();
        cal.set(2015,5,4);
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        //intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", item.getTitle() + " WITH " + item.getName());
        intent.putExtra("description", item.getDesc());
        intent.putExtra("eventLocation", item.getRoom());
        context.startActivity(intent);
        */

    }

    private static final String FRAGMENT_TITLE = "Conversations";

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_TITLE);
    }



}
