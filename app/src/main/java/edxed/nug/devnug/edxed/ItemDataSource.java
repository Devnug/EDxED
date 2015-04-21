package edxed.nug.devnug.edxed;

/**
 * Created by Nug on 3/21/2015.
 */
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

public class ItemDataSource {

    // Database fields
    private SQLiteDatabase database;
    private ItemDbHelper dbHelper;
    private String[] allColumns = { ItemDbHelper.COLUMN_ID,
            ItemDbHelper.KEY_NAME, ItemDbHelper.KEY_TITLE, ItemDbHelper.KEY_DESC, ItemDbHelper.KEY_ROOM, ItemDbHelper.KEY_SESSION,
            ItemDbHelper.KEY_ATTENDING, ItemDbHelper.KEY_EVENT_ID, ItemDbHelper.KEY_PIC, ItemDbHelper.KEY_PIC2, ItemDbHelper.KEY_LAST_UPDATE, ItemDbHelper.KEY_STRAND};
    private String[] allOrgColumns = { ItemDbHelper.COLUMN_ID,
            ItemDbHelper.KEY_NAME, ItemDbHelper.KEY_JOB_TITLE, ItemDbHelper.KEY_TWITTER, ItemDbHelper.KEY_EMAIL, ItemDbHelper.KEY_PIC};
    private static final String TAG = "ItemDataSource ::";

    public ItemDataSource(Context context) {
        dbHelper = ItemDbHelper.getInstance(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    
    public void createItem(ViewModel item) {
        ContentValues values = new ContentValues();
        
         if(database.isOpen())
         Log.d(TAG, "open");
         else
         Log.d(TAG, "closed");

        if(item.getName().indexOf("'") != -1) {

            item.setName(item.getName().substring(0,item.getName().indexOf("'") + 1) + "'" + item.getName().substring(item.getName().indexOf("'") + 1));
            Log.d(TAG, "New name: " + item.getName());
        }


        //Cursor cursor2 = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '%" + name + "%'", null, null, null, null);
        //cursor2.moveToFirst();
        if(canUpdateRow(item)) {

            //values.put(ItemDbHelper.COLUMN_ID, item.getId());
            values.put(ItemDbHelper.KEY_NAME, item.getName());
            values.put(ItemDbHelper.KEY_DESC, item.getDesc());
            values.put(ItemDbHelper.KEY_TITLE, item.getTitle());
            values.put(ItemDbHelper.KEY_ROOM, item.getRoom());
            values.put(ItemDbHelper.KEY_SESSION, item.getSession());
            values.put(ItemDbHelper.KEY_LAST_UPDATE, item.getLastUpdate());
            values.put(ItemDbHelper.KEY_PIC, item.getImgString());
            Log.d(TAG, item.getImgString());
            Log.d(TAG, "Updating " + item.getName() + " with room " + item.getRoom() + "with last update " + item.getLastUpdate());

            int num = database.updateWithOnConflict(ItemDbHelper.CONVERSATION_TABLE_NAME, values, ItemDbHelper.KEY_NAME + " LIKE '" + item.getName() + "'", null, SQLiteDatabase.CONFLICT_REPLACE);
            values.put(ItemDbHelper.COLUMN_ID, item.getId());
            values.put(ItemDbHelper.KEY_ATTENDING, "false");
            if(num == 0) {
                database.insertWithOnConflict(ItemDbHelper.CONVERSATION_TABLE_NAME, "", values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            //MainActivity.itemsUpdated++;
            //int num = database.updateWithOnConflict(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, values, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, SQLiteDatabase.CONFLICT_REPLACE);

            Log.d(TAG, "num of rows affected: " + num);
	    }
        //return newItem;
    }
    
    public void createBaseTable() {
        ContentValues values = new ContentValues();
        //Cursor cursor2 = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '%" + name + "%'", null, null, null, null);
        //cursor2.moveToFirst();
        //Initial Database
        String sql = "INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 1 + "','" + "HALLEY ANNE CURTIS" + "','" + "INTERNATIONAL TRAVEL" + "','" + "Traveling internationally expands students'' world views and brings classroom learning to life, so how can you bring this opportunity to your school? At this session, you''ll learn about the logistics of planning an international trip through the school system. Additionally, you''ll hear about how Hudson provided financial support to make travel possible for students from a variety of backgrounds. You''ll leave with the basic supports and inspiration you need to bring international travel to your students!" + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')";
            this.database.execSQL(sql);
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 2 + "','" + "JENNIFER GUNN" + "','" + "THE BIG IDEA PROJECT" + "','" + "The Big Idea Project is a student-designed passion project that allows students to discover their passions and conduct deep research and experimentation on topics they love or have yet to have the space to discover. This session will share The Big Idea Project model, as well as the lessons and successes we acquired along the way. Presentation participants will discuss practical solutions for launching a long-term independent project model with built-in supports and learning competencies. Furthermore, participants will gain access to our wealth of open-source materials." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 3 + "','" + "CHRISTOPHER PURCELL" + "','" + "MULTIPLE ACCESS POINTS" + "','" + "Incorporating multiple access points into our instruction allows all of our students the opportunity to engage in their learning of both knowledge and skills. Participants will examine strategies and resources in developing multiple access points for content, process, and product." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 4 + "','" + "SARAH KATZ" + "','" + "THE FUTURE PROJECT" + "','" + "The Future Project is dedicated to seeing students living lives of passion and purpose decades after they leave high school. So what does sustainable inspiration look like amongst young people? What is the Future Project methodology and how can the presence of a Dream Director and a Dream Team re-imagine school culture? Learn from students and staff who are part of the Future Project family." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 5 + "','" + "WALTER BROWN" + "','" + "INSTRUCTIONAL ROUNDS" + "','" + "Participants will share and discuss the strengths and benefits of establishing a culture of peer professional conversations. We will use David Allen''s \"Tuning Protocol\" as we look at several pieces of evidence and documents from the process." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 6 + "','" + "LEIA PETTY & MELISSA TORTORA" + "','" + "RESTORATIVE JUSTICE" + "','" + "Restorative Justice stems from the belief that conflict in a community can be repaired and relationships can be restored. It is an attempt to resolve conflict in a way that brings a community closer together rather than punitive models that alienate those in conflict from the larger community to whom they caused harm. Many teachers, support staff and administrators in their schools have used the restorative justice model to varying degrees. In this session, restorative justice faculty and student leaders at Hudson will walk you through the benefits of employing restorative justice, model a justice circle, and discuss how you can start a program at your school." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 7 + "','" + "TIM COMER" + "','" + "BLENDED LEARNING" + "','" + "What does blended learning look like? How does leveraging technology in this way fundamentally change the role of both the student and the teacher? In this session, teachers will begin to conceptualize and plan their digital classrooms to construct student paced blended learning lessons or units." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 8 + "','" + "MICHAEL PONELLA" + "','" + "ELECTIVE PROGRAMS" + "','" + "What are the best practices for administering an effective elective program in schools? In this session discussions will center around the Elective Exhibition, Elective Registration, student choice vs. teacher choice, the value of electives, and the goals for our elective classes. Come to this session and get a fresh new perspective on the administration of electives and enrichment." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 9 + "','" + "ANNE TALIAFERRO" + "','" + "GRADE-TEAM MEETINGS" + "','" + "In this session, educators will be sharing the use of grade teams within the school to examine student work and analyze student achievement data, including growth and gaps, to inform evidence-based adjustments to units, lessons and teaching practices, and ensure we are serving individual student needs. The discussion will center on the different uses of grade team meetings, and attempt to answer the questions \"How do grade teams help us develop as educators?\" and \"How does the use of grade team influence school culture?\"" + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 10 + "','" + "PHIL LINDER" + "','" + "TEACHING THEMATICALLY IN SOCIAL STUDIES" + "','" + "The benefits of teaching Social Studies thematically (as opposed to chronologically) are well documented, and after years of teaching United States History in the traditional, chronological way, I have finally decided to make the switch. Join educators from around the city in an open conversation as we share ideas and examine the strengths and weaknesses of shifting pedagogical strategies to capture all types of diverse learners in today''s city." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 11 + "','" + "THOMAS RODNEY" + "','" + "TECHNOLOGY USE IN THE MATH CLASSROOM" + "','" + "Technology, such as calculators, standard software programs, and the Internet, can be effectively used to enhance instruction in a number of ways. Participants will share best practices and discuss the benefits and capabilities of technology in the classroom." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 12 + "','" + "GINA ANGELILLO" + "','" + "GOOGLE CLASSROOM" + "','" + "With Google Classroom, teachers can seamlessly integrate Google Docs, Google Drive, and Gmail to create assignments, provide feedback for in progress and completed work, and communicate with their students directly and with whole class announcements--all without using a single piece of paper. Participants will set up Google Classroom teacher accounts and learn how to create, assign, and collect student assignments digitally. " + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            this.database.execSQL("INSERT INTO conversationlist (_id, name, title, description, room, session, attending, eventID, pic)  VALUES ('" + 13 + "','" + "KATE SALUTE" + "','" + "CONTENT, PROCESS & PRODUCT" + "','" + "The conversation will be around students demonstrating their understanding in a variety of ways without compromising Common Core standards. This method engages students in the process, the content and drives them to produce quality work. Bring your best project assignments! Small content groups with hands on project starters and 10 slides in 100 seconds to present their product." + "','" + "TBD" + "','" + "1" + "','" + "false" + "','" + "" + "','" + "" + "')");
            //Log.d(TAG, item.getItemName());
            //int num = database.updateWithOnConflict(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, values, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, SQLiteDatabase.CONFLICT_REPLACE);

            //Log.d(TAG, "num of rows affected: " + num);
	    /*
	    Cursor cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
	        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Item newItem = cursorToItem(cursor);
	    cursor.close();
	    */

        //return newItem;
    }

    public void createOrgBaseTable() {
        ContentValues values = new ContentValues();
        //Cursor cursor2 = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '%" + name + "%'", null, null, null, null);
        //cursor2.moveToFirst();
        //Initial Database
        String sql = "INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 1 + "','" + "NANCY AMLING" + "','" + "PRINCIPAL" + "','" + "@amlingnancy" + "','" + "" + "','" + "" + "')";
        this.database.execSQL(sql);
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 2 + "','" + "DR. GARY HABER" + "','" + "ASSISTANT PRINCIPAL" + "','" + "@garyhaberdc" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 3 + "','" + "WALTER BROWN" + "','" + "ASSISTANT PRINCIPAL, IA" + "','" + "@wbrownhhs" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 4 + "','" + "PHIL LINDER" + "','" + "EDUCATOR" + "','" + "@thehistorydude" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 5 + "','" + "THOMAS RODNEY" + "','" + "EDUCATOR" + "','" + "" + "','" + "trodney@hudsonhs.com" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 6 + "','" + "TIM COMER" + "','" + "EDUCATOR" + "','" + "@cambrianed" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 7 + "','" + "CHRIS PURCELL" + "','" + "EDUCATOR" + "','" + "@mr_purcell" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 8 + "','" + "VANESSA LETOURNEAU" + "','" + "EDUCATOR" + "','" + "@vletourneau437" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 9 + "','" + "JENNIFER GUNN" + "','" + "EDUCATOR" + "','" + "@jenniferlmgunn" + "','" + "" + "','" + "" + "')");
        this.database.execSQL("INSERT INTO organizerslist (_id, name, job_title, twitter_handle, email, pic)  VALUES ('" + 10 + "','" + "GINA ANGELILLO" + "','" + "EDUCATOR" + "','" + "@cultivatinggina" + "','" + "" + "','" + "" + "')");
        //Log.d(TAG, item.getItemName());
        //int num = database.updateWithOnConflict(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME, values, edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, SQLiteDatabase.CONFLICT_REPLACE);

        //Log.d(TAG, "num of rows affected: " + num);
	    /*
	    Cursor cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
	        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.COLUMN_ID + " = " + insertId, null,
	        null, null, null);
	    cursor.moveToFirst();
	    Item newItem = cursorToItem(cursor);
	    cursor.close();
	    */

        //return newItem;
    }

    public boolean isOpenSpot(String session) {
        Cursor c = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME, allColumns, ItemDbHelper.KEY_SESSION + " LIKE '" + session + "'", null, null, null, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex("attending")).equals("true")) {
                c.close();
                return false;
            }
            c.moveToNext();
        }
        c.close();
        return true;
    }

    public void removeSession(String session) {
        Cursor c = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME, allColumns, ItemDbHelper.KEY_SESSION + " LIKE '" + session + "'", null, null, null, null);
        c.moveToFirst();
        ContentValues values = new ContentValues();
        while(!c.isAfterLast()) {
            values.put(ItemDbHelper.KEY_ATTENDING, "false");
            int num = database.updateWithOnConflict(ItemDbHelper.CONVERSATION_TABLE_NAME, values, ItemDbHelper.KEY_SESSION + " LIKE '" + session + "'", null, SQLiteDatabase.CONFLICT_REPLACE);

            c.moveToNext();
        }
        c.close();
    }

    public void updateAttending(String name) {
        ContentValues values = new ContentValues();
        Cursor c = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME, allColumns, ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, null, null, null);
        c.moveToFirst();
        //Log.d(TAG,"attending originally set to: " + c.getString(c.getColumnIndex("attending")));
        if(c.getString(c.getColumnIndex("attending")).equals("false")) {
            values.put(ItemDbHelper.KEY_ATTENDING, "true");
            //Log.d(TAG, "Attending changed to: true");
        }
        else {
            values.put(ItemDbHelper.KEY_ATTENDING, "false");
            //Log.d(TAG, "Attending changed to: false");
        }
        c.close();
        int num = database.updateWithOnConflict(ItemDbHelper.CONVERSATION_TABLE_NAME, values, ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, SQLiteDatabase.CONFLICT_REPLACE);
       //Log.d(TAG, "Number of rows affected: " + num);

    }


    public boolean canUpdateRow(ViewModel item) {
        //int lastUpdateColumn = cursor.getColumnIndex(edxed.nug.devnug.edxed.ItemDbHelper.KEY_LAST_UPDATE);
        //Log.d(TAG, "Column index: " + lastUpdateColumn);
        Cursor cursor = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME, allColumns, ItemDbHelper.KEY_NAME + " LIKE '" + item.getName() + "'", null, null, null, null);
        cursor.moveToFirst();
        //Log.d(TAG, "Cursor: " + cursor.getCount());
        if(cursor.getCount() == 0) {
            Log.d(TAG, "UPDATING NAME: " + item.getName());
            return true;
        }
        int lastUpdateColumn = cursor.getColumnIndex(ItemDbHelper.KEY_LAST_UPDATE);
        int lastUpdateCol = cursor.getInt(lastUpdateColumn);
        int lastUp = item.getLastUpdate();
        //Log.d(TAG, "CURSOR COUNT = " + cursor.getCount());
        if(lastUpdateCol == 1 || lastUp == 1 && cursor.getCount() == 1) {
            //Log.d(TAG, "UPDATING NAME: " + item.getItemName());
            return false;
        }
        cursor.close();
        if(lastUp == 1) {
            Log.d(TAG, "UPDATING NAME: " + item.getName());
            return true;
        }
        //Log.d(TAG, "Last update: " + Long.valueOf(lastUpdateCol).longValue() + "Item last Update: " + Long.valueOf(lastUp).longValue());
        // MainActivity.lastUpdateLong < Long.valueOf(lastUp).longValue()
        if(lastUpdateCol < lastUp) {
            Log.d(TAG, "UPDATING NAME: " + item.getName());
            return true;
        }
        return false;
    }

    /*
    public List<Item> getAllItems() {
        List<Item> comments = new ArrayList<Item>();

        Cursor cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item comment = cursorToItem(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return comments;
    }
    */


    public String getDb()
    {
        Cursor cursor = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME,
                allColumns, null, null,
                null, null, null);
        cursor.moveToLast();
        String name = cursor.getString(1);
        return name;
    }

    public boolean hasEntries()
    {
        Log.d(TAG, database.toString());
        try {
            Cursor cursor = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME,
                    allColumns, null, null,
                    null, null, null);
            //cursor.moveToFirst();
            Log.d(TAG, cursor.getCount() + "");
            if(cursor.getCount() == 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }
        catch (Exception e) {
            this.resetAllTables();
        }
        return true;

    }

    public Cursor queryAll()
    {
        Cursor cursor = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME,
                allColumns, null, null,
                null, null, ItemDbHelper.KEY_SESSION + " ASC");
        return cursor;
    }

    public Cursor queryAllOrg()
    {
        Cursor cursor = database.query(ItemDbHelper.ORGANIZERS_TABLE_NAME,
                allOrgColumns, null, null,
                null, null, null);
        return cursor;
    }

    public Cursor queryAttending()
    {
        Cursor cursor = database.query(ItemDbHelper.CONVERSATION_TABLE_NAME,
                allColumns, ItemDbHelper.KEY_ATTENDING + " LIKE 'true'", null,
                null, null, null);
        return cursor;
    }

    public void resetTable() {
        dbHelper.resetTable(database);
    }
/*
    public void resetTable(boolean unusualFlag) {
        dbHelper.resetTable(database, unusualFlag);
    }

    public void resetTable(String string) {
        dbHelper.resetTable(database, string);

    }
*/
    public void resetAllTables() {
        dbHelper.resetAllTables(database);
    }
/*
    private Item cursorToItem(Cursor cursor) {
        Item comment = new Item();
        comment.setId(cursor.getLong(0));
        //set each data field
        //comment.setItem(cursor.getString(1));
        return comment;
    }

    public Cursor getItem(String name) {
        if(name.indexOf("'") != -1){
            name = name.substring(0, name.indexOf("'")) + "'" + name.substring(name.indexOf("'"));
        }
        Log.d(TAG, name);
        Cursor cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME, allColumns, ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null, null, null, null);
        return cursor;

    }

    public Cursor getUrl(String url) {
        Log.d(TAG, url);
        Cursor cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME, allColumns, ItemDbHelper.KEY_NAME + " LIKE '%" + url + "%'", null, null, null, null);
        return cursor;
    }

    // Old method
    public Cursor querySpecial(String flag) {
        Cursor cursor = null;
        if(flag.equals("unique"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_UNIQUE + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("uncraft"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_UNCRAFT + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("vintage"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_VINTAGE + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("strange"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_STRANGE + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("genuine"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_GENUINE + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("haunted"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_HAUNTED + " NOT LIKE 'n%'", null,
                    null, null, null);
        }
        else if(flag.equals("scout"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Scout%'", null,
                    null, null, null);
        }
        else if(flag.equals("spy"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Spy%'", null,
                    null, null, null);
        }
        else if(flag.equals("soldier"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Soldier%'", null,
                    null, null, null);
        }
        else if(flag.equals("medic"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Medic%'", null,
                    null, null, null);
        }
        else if(flag.equals("heavy"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Heavy%'", null,
                    null, null, null);
        }
        else if(flag.equals("engineer"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Engineer%'", null,
                    null, null, null);
        }
        else if(flag.equals("demoman"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Demoman%'", null,
                    null, null, null);
        }
        else if(flag.equals("pyro"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Pyro%'", null,
                    null, null, null);
        }
        else if(flag.equals("sniper"))
        {
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Sniper%'", null,
                    null, null, null);
        }
        return cursor;
    }

    // New method
    public Cursor querySpecial(String class_flag, String type_flag, boolean class_checked, boolean type_checked) {
        if(class_flag.equals("misc"))
            class_flag = "[]";
        if(class_flag.equals("all"))
            class_flag = "";
        Cursor cursor = null;
        Log.d(TAG, "type_flag = " + type_flag);
        Log.d(TAG, "class_flag = " + class_flag);
		/*
		if(class_flag.equals("[]"))
		{
			Log.d(TAG, "Show just Misc");
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
			        null, null, null);
		}

        if(class_flag.equals("tool") || class_flag.equals("crate")) {
            Log.d(TAG, "This should be tool -> " + class_flag);

            if(type_flag.equals("all")) {
                Log.d(TAG, "Do we get here?");
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("unique")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_UNIQUE + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("uncraft")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_UNCRAFT + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("vintage")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_VINTAGE + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("genuine")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_GENUINE + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("strange")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_STRANGE + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("haunted")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_HAUNTED + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
            else if(type_flag.equals("collector")) {
                cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                        allColumns, ItemDbHelper.KEY_ITEMTYPE + " LIKE '%" + class_flag + "%' and " + ItemDbHelper.KEY_COLLECTOR + " NOT LIKE 'n%'", null,
                        null, null, ItemDbHelper.KEY_NAME);
            }
        }
        else if(type_flag.equals("all") && class_flag.equals(""))
        {
            Log.d(TAG, "1");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, null , null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("all") && !class_flag.equals(""))
        {
            Log.d(TAG, "2");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("unique"))
        {
            Log.d(TAG, "3");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_UNIQUE + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("uncraft"))
        {
            Log.d(TAG, "4");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_UNCRAFT + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("vintage"))
        {
            Log.d(TAG, "5");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_VINTAGE + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("strange"))
        {
            Log.d(TAG, "6");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_STRANGE + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("genuine"))
        {
            Log.d(TAG, "7");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_GENUINE + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("haunted"))
        {
            Log.d(TAG, "8");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_HAUNTED + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
        else if(type_flag.equals("collector"))
        {
            Log.d(TAG, "9");
            cursor = database.query(ItemDbHelper.DICTIONARY_TABLE_NAME,
                    allColumns, ItemDbHelper.KEY_COLLECTOR + " NOT LIKE 'n%' AND " + ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + class_flag + "%'", null,
                    null, null, ItemDbHelper.KEY_NAME);
        }
		/*
		else if(class_flag.equals("scout"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Scout%' ", null,
			        null, null, null);
		}
		else if(class_flag.equals("spy"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Spy%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("soldier"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Soldier%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("medic"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Medic%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("heavy"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Heavy%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("engineer"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Engineer%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("demoman"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Demoman%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("pyro"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Pyro%'", null,
			        null, null, null);
		}
		else if(class_flag.equals("sniper"))
		{
			cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME,
			        allColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%Sniper%'", null,
			        null, null, null);
		}

        return cursor;
    }

	/*
	public Cursor queryUnusual() {
		Cursor cursor = database.query(edxed.nug.devnug.edxed.ItemDbHelper.UNUSUAL_TABLE,
		        unusualColumns, null, null,
		        null, null, edxed.nug.devnug.edxed.ItemDbHelper.KEY_UNUSUAL_NAME + " ASC");
	  return cursor;
	}*/

	/*
	public Cursor queryUnusual(String flag) {
		Cursor cursor = database.rawQuery("SELECT " + edxed.nug.devnug.edxed.ItemDbHelper.KEY_UNUSUAL_NAME + " FROM " + edxed.nug.devnug.edxed.ItemDbHelper.UNUSUAL_TABLE + ", " + edxed.nug.devnug.edxed.ItemDbHelper.DICTIONARY_TABLE_NAME
				+ " WHERE " + edxed.nug.devnug.edxed.ItemDbHelper.KEY_UNUSUAL_NAME + " = " + edxed.nug.devnug.edxed.ItemDbHelper.KEY_NAME + " AND " + edxed.nug.devnug.edxed.ItemDbHelper.KEY_CLASSEQUIP + " LIKE '%" + flag + "%'", null);
		        //unusualColumns, edxed.nug.devnug.edxed.ItemDbHelper.KEY_UNUSUAL_NAME + " LIKE '%" + flag + "%'", null,
		        //null, null, null);
	  return cursor;
	}*/



    public void updatePic(TextView cName, Bitmap result) {
        ContentValues values = new ContentValues();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Log.d(TAG, "Update Pic " + result + "\n for " + cName.getText());
        if(result != null) {
            result.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] photo = baos.toByteArray();
            String name = ((String) cName.getText()).replace("'", "''");
            values.put(ItemDbHelper.KEY_PIC, photo);
            Log.d(TAG, "photo = " + photo.toString());
            System.out.println(database.update(ItemDbHelper.CONVERSATION_TABLE_NAME, values, ItemDbHelper.KEY_NAME + " LIKE '" + name + "'", null));
        }

    }


}
