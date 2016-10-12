package sodevan.adtouch;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Devanshu on 12-10-2016.
 */
public class AdDatabase {

    //Database Constants

    public static final String DB_NAME = "adtouch.db";
    public static final int DB_VER = 2;

    // Create ads index table

    public static final String DB_TABLE = "ads_index";
    public static final String C_NAME = "ID";
    public static final String C_TITLE = "Title";
    public static final String C_IMAGE = "Image";
    public static final String Q_CREATE = "CREATE TABLE " + DB_TABLE + "(" + C_NAME +
            " integer primary key autoincrement, " + C_TITLE + " text, " + C_IMAGE + " blob)";


    // Create ads data table
    public static final String DB1_TABLE = "ads_data";
    public static final String C1_ID = "ID";
    public static final String C1_ADID = "AD_ID";
    public static final String C1_ADTEXT = "AdText";
    public static final String C1_ADIMAGE = "AdImage";
    public static final String Q1_CREATE = "CREATE TABLE " + DB1_TABLE + "("+C1_ID +
            " integer primary key autoincrement, " + C1_ADID + " int, " + C1_ADTEXT + " text, " +
            C1_ADIMAGE + " blob)";

    Context c;
    private SQLiteDatabase database;

    public AdDatabase(Context context) {
        c = context;
    }

    public AdDatabase open() {

        DBHelper dbh = new DBHelper(c);
        database = dbh.getWritableDatabase();
        return this;

    }

    public long save(String title, byte[] imgblob) {
        ContentValues cv = new ContentValues();
        cv.put(C_TITLE, title);
        cv.put(C_IMAGE, imgblob);
        long id = database.insert(DB_TABLE, null, cv);
        return id;
    }

    public void close() {
        database.close();
    }

    public void cleardatabase(){
        try {
            database.execSQL("DELETE FROM " + DB_TABLE);
            database.execSQL("DELETE FROM " + DB1_TABLE);
        }catch (Exception ex){
        }
    }

    public void saveData(long insertid, String adtext, byte[] adimgblob) {
        ContentValues cv = new ContentValues();
        cv.put(C1_ADID, insertid);
        cv.put(C1_ADTEXT, adtext);
        cv.put(C1_ADIMAGE, adimgblob);
        database.insert(DB1_TABLE, null, cv);
    }

    private class DBHelper extends SQLiteOpenHelper{

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Q_CREATE);
            db.execSQL(Q1_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
