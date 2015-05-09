package edxed.nug.devnug.edxed;

/**
 * Created by Nug on 3/18/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScheduleFragment extends Fragment {
    private static final String TAG = "ScheduleFragment";
    private static RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    static ScheduleAdapter adapter;
    FragmentActivity mActivity;
    static ItemDataSource db;
    static ArrayList<ViewModel> list;
    TextView empty;

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
    public static ScheduleFragment newInstance(int sectionNumber) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        // Create database on first run
        return fragment;
    }

    public ScheduleFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        if(list.size() == 0) {
            Log.d(TAG, "There is nothing here!");
            empty = (TextView) rootView.findViewById(R.id.empty);
            empty.setVisibility(View.VISIBLE);
        }
        adapter = new ScheduleAdapter(list, R.layout.item2, mActivity);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (FragmentActivity) activity;
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        db = new ItemDataSource(this.mActivity.getApplicationContext());
        //populateList();
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

    public void populateList(){
        list = new ArrayList<ViewModel>();
        Cursor c2 = db.queryScheduleAll();
        //if(c2.moveToFirst()) {
            int idColumn = c2.getColumnIndex(ItemDbHelper.COLUMN_ID);
            int nameColumn = c2.getColumnIndex(ItemDbHelper.KEY_NAME);
            int roomColumn = c2.getColumnIndex(ItemDbHelper.KEY_ROOM);
            int timeColumn = c2.getColumnIndex(ItemDbHelper.KEY_TIME);
            //int Column = c.getColumnIndex(ItemDbHelper.KEY_LAST_UPDATE);
            do {
                Log.d(TAG, "Adding to Schedule title: " + c2.getString(nameColumn));
                list.add(new ViewModel(c2.getString(nameColumn), "", "", c2.getString(roomColumn), "", c2.getString(timeColumn), "", ""));

            } while(c2.moveToNext());
        //}
        c2.close();

    }

    //  Creates a base list and populates the ArrayList list to ensure that the cardview has entries
    public void createList() {

        list = new ArrayList<ViewModel>();
        boolean session1 = false;
        boolean session2 = false;
        boolean session3 = false;
        list.add(new ViewModel("Check-In, Breakfast, Networking","", "", "Lobby", "true", "8:15-9:00", "", ""));
        list.add(new ViewModel("Keynote Address: Marisol Bradbury","", " ", "Auditorium", "true", "9:00-9:30", "", ""));
        list.add(new ViewModel("Conversation Session 1","", "", " ", "true", "9:30-11:00", "", ""));
        list.add(new ViewModel("Lunch & Ignite Session","", "", "Auditorium", "true", "11:00-12:00", "", ""));
        list.add(new ViewModel("Conversation Session 2","", "", " ", "true", "12:00-1:30", "", ""));
        list.add(new ViewModel("Conversation Session 3","", "", " ", "true", "1:30-3:05", "", ""));
        list.add(new ViewModel("Closing Remarks","", "", "Auditorium", "true", "3:10-3:30", "", ""));
        /*
        Cursor c2 = db.queryScheduleAll();
        if(c2.moveToFirst()) {
            int idColumn = c2.getColumnIndex(ItemDbHelper.COLUMN_ID);
            int nameColumn = c2.getColumnIndex(ItemDbHelper.KEY_NAME);
            int roomColumn = c2.getColumnIndex(ItemDbHelper.KEY_ROOM);
            int timeColumn = c2.getColumnIndex(ItemDbHelper.KEY_TIME);
            //int Column = c.getColumnIndex(ItemDbHelper.KEY_LAST_UPDATE);
            do {
                list.add(new ViewModel(c2.getString(nameColumn), "", "", c2.getString(roomColumn), "", c2.getString(timeColumn)));

            } while(c2.moveToNext());
        }
        c2.close();
        */
        Cursor c = db.queryAttending();
        if(c.moveToFirst()) {
            int nameColumn = c.getColumnIndex(ItemDbHelper.KEY_NAME);
            int titleColumn = c.getColumnIndex(ItemDbHelper.KEY_TITLE);
            int descColumn = c.getColumnIndex(ItemDbHelper.KEY_DESC);
            int roomColumn = c.getColumnIndex(ItemDbHelper.KEY_ROOM);
            int picColumn = c.getColumnIndex(ItemDbHelper.KEY_PIC);
            int pic2Column = c.getColumnIndex(ItemDbHelper.KEY_PIC2);
            int sessionColumn = c.getColumnIndex(ItemDbHelper.KEY_SESSION);
            int attendingColumn = c.getColumnIndex(ItemDbHelper.KEY_ATTENDING);
            int eventIdColumn = c.getColumnIndex(ItemDbHelper.KEY_EVENT_ID);
            do {
                Log.d(TAG, "Name: " + c.getString(nameColumn) + " session: " + c.getString(sessionColumn) + " attending " + c.getString(attendingColumn));
                if(c.getString(sessionColumn).equals("1")) {
                    list.add(3, new ViewModel(c.getString(nameColumn), c.getString(titleColumn), c.getString(descColumn), c.getString(roomColumn), c.getString(attendingColumn), c.getString(sessionColumn), c.getString(picColumn), c.getString(pic2Column)));
                    session1 = true;
                    //Log.d(TAG, "Session 1 Picked");
                }
                if(c.getString(sessionColumn).equals("2")) {
                    for(int i = 0; i < list.size(); i++) {
                        //Log.d(TAG, "Session: " + list.get(i).getName());
                        if(list.get(i).getName().equals("Conversation Session 2")) {
                            list.add(i+1, new ViewModel(c.getString(nameColumn), c.getString(titleColumn), c.getString(descColumn), c.getString(roomColumn), c.getString(attendingColumn), c.getString(sessionColumn), c.getString(picColumn), c.getString(pic2Column)));
                            session2 = true;
                            //Log.d(TAG, "Session 2 Picked");
                        }
                    }
                }
                if(c.getString(sessionColumn).equals("3")) {
                    //Log.d(TAG, "Session 3 Here");
                    for(int i = 0; i < list.size(); i++) {
                        if(list.get(i).getName().equals("Conversation Session 3")) {
                            list.add(i+1, new ViewModel(c.getString(nameColumn), c.getString(titleColumn), c.getString(descColumn), c.getString(roomColumn), c.getString(attendingColumn), c.getString(sessionColumn), c.getString(picColumn), c.getString(pic2Column)));
                            session3 = true;
                            //Log.d(TAG, "Session 3 Picked");
                        }
                    }
                }
            } while(c.moveToNext());
            if(!session1) {
                //Log.d(TAG, "No session 1");
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 1")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 1!", "", "", " ", "", "", "", ""));
                    }
                }
            }
            if(!session2) {
                //Log.d(TAG, "No session 2");
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 2")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 2!", "", "", " ", "", "", "", ""));
                    }
                }
            }
            if(!session3) {
                //Log.d(TAG, "No session 3");
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 3")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 3!", "", "", " ", "", "", "", ""));
                    }
                }
            }
        }
        else {
            if(!session1) {
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 1")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 1!", "", "", " ", "", "", "", ""));
                    }
                }
            }
            if(!session2) {
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 2")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 2!", "", "", " ", "", "", "", ""));
                    }
                }
            }
            if(!session3) {
                for(int i = 0; i < list.size(); i++) {
                    if(list.get(i).getName().equals("Conversation Session 3")) {
                        list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 3!", "", "", " ", "", "", "", ""));
                    }
                }
            }
        }
        c.close();
    }
    /*
    private ArrayList<ViewModel> createMockList() {
        list = new ArrayList<ViewModel>();
        list.add(new ViewModel("HALLEY ANNE CURTIS","INTERNATIONAL TRAVEL", "Traveling internationally expands students' world views and brings classroom learning to life, so how can you bring this opportunity to your school? At this session, you'll learn about the logistics of planning an international trip through the school system. Additionally, you'll hear about how Hudson provided financial support to make travel possible for students from a variety of backgrounds. You'll leave with the basic supports and inspiration you need to bring international travel to your students!","no img"));
        list.add(new ViewModel("JENNIFER GUNN","THE BIG IDEA PROJECT", "The Big Idea Project is a student-designed passion project that allows students to discover their passions and conduct deep research and experimentation on topics they love or have yet to have the space to discover. This session will share The Big Idea Project model, as well as the lessons and successes we acquired along the way. Presentation participants will discuss practical solutions for launching a long-term independent project model with built-in supports and learning competencies. Furthermore, participants will gain access to our wealth of open-source materials.","no img"));
        list.add(new ViewModel("CHRISTOPHER PURCELL","MULTIPLE ACCESS POINTS", "Incorporating multiple access points into our instruction allows all of our students the opportunity to engage in their learning of both knowledge and skills. Participants will examine strategies and resources in developing multiple access points for content, process, and product.","no img"));
        list.add(new ViewModel("SARAH KATZ","THE FUTURE PROJECT", "The Future Project is dedicated to seeing students living lives of passion and purpose decades after they leave high school. So what does sustainable inspiration look like amongst young people? What is the Future Project methodology and how can the presence of a Dream Director and a Dream Team re-imagine school culture? Learn from students and staff who are part of the Future Project family.","no img"));
        list.add(new ViewModel("WALTER BROWN","INSTRUCTIONAL ROUNDS", "Participants will share and discuss the strengths and benefits of establishing a culture of peer professional conversations. We will use David Allen's \"Tuning Protocol\" as we look at several pieces of evidence and documents from the process..","no img"));
        list.add(new ViewModel("LEIA PETTY & MELISSA TORTORA","RESTORATIVE JUSTICE", "Restorative Justice stems from the belief that conflict in a community can be repaired and relationships can be restored. It is an attempt to resolve conflict in a way that brings a community closer together rather than punitive models that alienate those in conflict from the larger community to whom they caused harm. Many teachers, support staff and administrators in their schools have used the restorative justice model to varying degrees. In this session, restorative justice faculty and student leaders at Hudson will walk you through the benefits of employing restorative justice, model a justice circle, and discuss how you can start a program at your school.","no img"));
        list.add(new ViewModel("TIM COMER","BLENDED LEARNING", "What does blended learning look like? How does leveraging technology in this way fundamentally change the role of both the student and the teacher? In this session, teachers will begin to conceptualize and plan their digital classrooms to construct student paced blended learning lessons or units.","no img"));
        list.add(new ViewModel("MICHAEL PONELLA","ELECTIVE PROGRAMS", "What are the best practices for administering an effective elective program in schools? In this session discussions will center around the Elective Exhibition, Elective Registration, student choice vs. teacher choice, the value of electives, and the goals for our elective classes. Come to this session and get a fresh new perspective on the administration of electives and enrichment.","no img"));
        list.add(new ViewModel("ANNE TALIAFERRO","GRADE-TEAM MEETINGS", "In this session, educators will be sharing the use of grade teams within the school to examine student work and analyze student achievement data, including growth and gaps, to inform evidence-based adjustments to units, lessons and teaching practices, and ensure we are serving individual student needs. The discussion will center on the different uses of grade team meetings, and attempt to answer the questions \"How do grade teams help us develop as educators?\" and \"How does the use of grade team influence school culture?\"","no img"));
        list.add(new ViewModel("PHIL LINDER","TEACHING THEMATICALLY IN SOCIAL STUDIES", "The benefits of teaching Social Studies thematically (as opposed to chronologically) are well documented, and after years of teaching United States History in the traditional, chronological way, I have finally decided to make the switch. Join educators from around the city in an open conversation as we share ideas and examine the strengths and weaknesses of shifting pedagogical strategies to capture all types of diverse learners in today's city.","no img"));
        list.add(new ViewModel("THOMAS RODNEY","TECHNOLOGY USE IN THE MATH CLASSROOM", "Technology, such as calculators, standard software programs, and the Internet, can be effectively used to enhance instruction in a number of ways. Participants will share best practices and discuss the benefits and capabilities of technology in the classroom.","no img"));
        list.add(new ViewModel("GINA ANGELILLO","GOOGLE CLASSROOM", "With Google Classroom, teachers can seamlessly integrate Google Docs, Google Drive, and Gmail to create assignments, provide feedback for in progress and completed work, and communicate with their students directly and with whole class announcements--all without using a single piece of paper. Participants will set up Google Classroom teacher accounts and learn how to create, assign, and collect student assignments digitally.","no img"));
        list.add(new ViewModel("KATE SALUTE","CONTENT, PROCESS & PRODUCT", "The conversation will be around students demonstrating their understanding in a variety of ways without compromising Common Core standards. This method engages students in the process, the content and drives them to produce quality work. Bring your best project assignments! Small content groups with hands on project starters and 10 slides in 100 seconds to present their product.","no img"));
        return list;
    }
    */

    public static void removeEvent(ViewModel item) {
        db.updateAttending(item.getName());
        if(item.getSession().equals("1")) {
            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).getName().equals("Conversation Session 1")) {
                    list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 1!", "", "", " ", "", "", "", ""));
                }
            }
        }
        else if(item.getSession().equals("2")) {
            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).getName().equals("Conversation Session 2")) {
                    list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 2!", "", "", " ", "", "", "", ""));
                }
            }
        }
        else if(item.getSession().equals("3")) {
            for(int i = 0; i < list.size(); i++) {
                if(list.get(i).getName().equals("Conversation Session 3")) {
                    list.add(i+1, new ViewModel("You have not chosen a conversation yet for Session 3!", "", "", " ", "", "", "", ""));
                }
            }
        }

    }

    public static void removeCard(View v) {
        int selectedItemPosition = mRecyclerView.getChildPosition(v);
        //RecyclerView.ViewHolder viewHolder = mRecyclerView.findViewHolderForPosition(selectedItemPosition);
        list.remove(selectedItemPosition);
        adapter.notifyItemRemoved(selectedItemPosition);
        adapter.notifyDataSetChanged();
    }

    public static void addEvent(Context context, ViewModel item) {
        // Intent calIntent = new Intent(Intent.ACTION_INSERT);
        // calIntent.setData(CalendarContract.Events.CONTENT_URI);
        // context.startActivity(calIntent);
        db.updateAttending(item.getName());
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

    private static final String FRAGMENT_TITLE = "Schedule";

    @Override
    public void onResume() {
        super.onResume();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(FRAGMENT_TITLE);
    }

}
