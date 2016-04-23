package inc.synapse.notify;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "db_notify";
    // Contacts table name
    private static final String TB_SUMMARY = "summary";
    // Contacts Table Columns names
    private static final String PACKAGE_NAME = "package_name";
    private static final String TIME_STAMP = "timestamp";
    private static final String NO_NOTF = "no_notf";
    private String TAG=getClass().getSimpleName();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_SUMMARY_TABLE = "CREATE TABLE " + TB_SUMMARY + "("
                + PACKAGE_NAME + " TEXT," + TIME_STAMP + " TEXT" + NO_NOTF + " TEXT"+")";
        Log.d(TAG, "sql: "+CREATE_SUMMARY_TABLE);
        db.execSQL(CREATE_SUMMARY_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_SUMMARY);
        onCreate(db);
    }
    // Adding new contact
    void addRecord(Summary sm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, sm.get_pckg_name()); //package name
        values.put(TIME_STAMP, sm.get_timestamp()); //package name
        values.put(NO_NOTF, sm.get_no_notf()); //package name

        // Inserting Row
        db.insert(TB_SUMMARY, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Summary getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TB_SUMMARY, new String[] {PACKAGE_NAME, TIME_STAMP, NO_NOTF}, null, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Summary contact = new Summary(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

}
