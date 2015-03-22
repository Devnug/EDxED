package edxed.nug.devnug.edxed; /**
 * Created by Nug on 3/21/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ItemDbHelper extends SQLiteOpenHelper {

    private static ItemDbHelper mInstance = null;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "EDxEDDB.db";
    public static final String COLUMN_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESC = "description";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SESSION = "session";
    public static final String KEY_PIC = "pic";
    public static final String KEY_ATTENDING = "attending";
    public static final String KEY_ROOM = "room";
    public static final String KEY_EVENT_ID = "eventId";
    public static final String CONVERSATION_TABLE_NAME = "conversationlist";
    public static final String CONVERSATION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + CONVERSATION_TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT, "
            + KEY_TITLE + " TEXT, "
            + KEY_DESC + " TEXT, "
            + KEY_ROOM + " TEXT, "
            + KEY_SESSION + " TEXT, "
            + KEY_ATTENDING + " TEXT, "
            + KEY_EVENT_ID + " TEXT, "
            + KEY_PIC + " BLOB);";

    public static final String DEBUG_TAG = "edxed.nug.devnug.edxed.ItemDbHelper :: ";

    public static ItemDbHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new ItemDbHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DEBUG_TAG, "onCreate");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(DEBUG_TAG, "onOpen");
        //db.execSQL(UNUSUAL_TABLE_CREATE);
        db.execSQL(CONVERSATION_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ItemDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE_NAME);
        onCreate(db);
    }

    public void resetTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE_NAME);
        onCreate(db);
    }

    public void resetTable(SQLiteDatabase db, boolean unusualFlag) {
            db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE_NAME);
            db.execSQL(CONVERSATION_TABLE_CREATE);
    }

    public void resetAllTables(SQLiteDatabase db) {
        //delete tables
        //db.execSQL("DROP TABLE IF EXISTS " + UNUSUAL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + UNUSUAL_TABLE_DATA);

        //recreate tables
        db.execSQL(CONVERSATION_TABLE_CREATE);
        //db.execSQL(UNUSUAL_TABLE_CREATE);
        //db.execSQL(UNUSUAL_TABLE_DATA_CREATE);
    }

}

/**
 public class edxed.nug.devnug.edxed.ItemDbHelper {
 class Row extends Object {
 public long _Id;
 public String code;
 public String name;
 public String gender;
 }

 private static final String DATABASE_CREATE =
 "create table BIODATA(_id integer primary key autoincrement, "
 + "code text not null,"
 + "name text not null"
 +");";

 private static final String DATABASE_NAME = "ITEMDB";

 private static final String DATABASE_TABLE = "PRICELIST";

 private static final int DATABASE_VERSION = 1;

 private SQLiteDatabase db;

 public edxed.nug.devnug.edxed.ItemDbHelper(Context ctx) {
 try {
 db = ctx.openDatabase(DATABASE_NAME, null);
 } catch (FileNotFoundException e) {
 try {
 db =
 ctx.openDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
 null);
 db.execSQL(DATABASE_CREATE);
 } catch (FileNotFoundException e1) {
 db = null;
 }
 }
 }

 public void close() {
 db.close();
 }

 public void createRow(String code, String name) {
 ContentValues initialValues = new ContentValues();
 initialValues.put("code", code);
 initialValues.put("name", name);
 db.insert(DATABASE_TABLE, null, initialValues);
 }

 public void deleteRow(long rowId) {
 db.delete(DATABASE_TABLE, "_id=" + rowId, null);
 }

 public List<Row> fetchAllRows() {
 ArrayList<Row> ret = new ArrayList<Row>();
 try {
 Cursor c =
 db.query(DATABASE_TABLE, new String[] {
 "_id", "code", "name"}, null, null, null, null, null);
 int numRows = c.count();
 c.first();
 for (int i = 0; i < numRows; ++i) {
 Row row = new Row();
 row._Id = c.getLong(0);
 row.code = c.getString(1);
 row.name = c.getString(2);
 ret.add(row);
 c.next();
 }
 } catch (SQLException e) {
 Log.e("Exception on query", e.toString());
 }
 return ret;
 }

 public Row fetchRow(long rowId) {
 Row row = new Row();
 Cursor c =
 db.query(true, DATABASE_TABLE, new String[] {
 "_id", "code", "name"}, "_id=" + rowId, null, null,
 null, null);
 if (c.count() > 0) {
 c.first();
 row._Id = c.getLong(0);
 row.code = c.getString(1);
 row.name = c.getString(2);
 return row;
 } else {
 row.rowId = -1;
 row.code = row.name= null;
 }
 return row;
 }

 public void updateRow(long rowId, String code, String name) {
 ContentValues args = new ContentValues();
 args.put("code", code);
 args.put("name", name);
 db.update(DATABASE_TABLE, args, "_id=" + rowId, null);
 }
 public Cursor GetAllRows() {
 try {
 return db.query(DATABASE_TABLE, new String[] {
 "_id", "code", "name"}, null, null, null, null, null);
 } catch (SQLException e) {
 Log.e("Exception on query", e.toString());
 return null;
 }
 }
 }

 */
