package edxed.nug.devnug.edxed;

/**
 * Created by Nug on 3/18/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrganizersFragment extends Fragment {
    private static final String TAG = "OrganizersFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerviewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    OrganizersAdapter adapter;
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
    public static OrganizersFragment newInstance(int sectionNumber) {
        OrganizersFragment fragment = new OrganizersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        // Create database on first run
        return fragment;
    }

    public OrganizersFragment() {

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
        adapter = new OrganizersAdapter(list,R.layout.organizer_card, mActivity);
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
        Cursor c = db.queryAllOrg();
        if(c.moveToFirst()) {
            int nameColumn = c.getColumnIndex(ItemDbHelper.KEY_NAME);
            int jobTitleColumn = c.getColumnIndex(ItemDbHelper.KEY_JOB_TITLE);
            int pictureColumn  = c.getColumnIndex(ItemDbHelper.KEY_PIC);
            int twitterColumn = c.getColumnIndex(ItemDbHelper.KEY_TWITTER);
            int emailColumn = c.getColumnIndex(ItemDbHelper.KEY_EMAIL);
            Log.d(TAG, "Name: " + c.getString(nameColumn));
            do {
                list.add(new ViewModel(c.getString(nameColumn),c.getString(jobTitleColumn), c.getString(twitterColumn), c.getString(emailColumn)));

            } while(c.moveToNext());
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
    }

    public static void addEvent(final Context context, ViewModel item) {
        // Intent calIntent = new Intent(Intent.ACTION_INSERT);
        // calIntent.setData(CalendarContract.Events.CONTENT_URI);
        // context.startActivity(calIntent);
        db.updateAttending(item.getName());
        final ViewModel viewItem = item;
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
                                calDate.getTimeInMillis() + 60*60*1000);

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

}
