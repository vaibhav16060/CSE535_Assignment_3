package vaibhav.iiitd.com.grievanceredressal;

/**
 * Created by Vaibhav on 30-09-2016.
 */

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.DatabaseUtils;
import android.content.ContentValues;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import android.database.Cursor;

public class Grievance extends SQLiteOpenHelper  {

    public static final String DATABASE = "Grievance.db";
    public static final String TABLE_NAME = "grievance";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_FILE_NAME = "filename";
    public static final String COL_INSERTED_ON = "inserted_on";

    public Grievance(Context con){
        super(con, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + " (id text, name text, filename text, inserted_on DATETIME, serviced_on DATETIME)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS grievance");
        onCreate(db);
    }

    public void insertGrievance(String id, String name, String filename){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String d = dateFormat.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "insert into grievance(id, name, filename, inserted_on) values('" + id + "', '" + name+ "', '" + filename + "', '" + d +"')";
        db.execSQL(query);
    }

    public void markResolved(String id, String name, String filename, String inserted_on){

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String d = dateFormat.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "update grievance set serviced_on='" + d +"' where id='"+ id +"' and name='"
                       + name +"' and filename='" + filename + "' and inserted_on='" + inserted_on + "'";
        db.execSQL(query);
    }

    public void delete(String id, String name, String filename, String inserted_on){

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "delete from grievance where id='"+ id +"' and name='"
                + name +"' and filename='" + filename + "' and inserted_on='" + inserted_on + "'";
        db.execSQL(query);
    }

    public String getMaxFileValue(){
        String ret = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select filename from " + TABLE_NAME + " order by inserted_on desc limit 1", null );
        result.moveToFirst();
        if(result.isAfterLast() == false)
            ret = result.getString(result.getColumnIndex(COL_FILE_NAME));
        return ret;
    }

    public String[][] getAllGrievances(){
        String [][]all_grievances = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select id, name, filename, inserted_on from " + TABLE_NAME + " where serviced_on is null", null );
        result.moveToFirst();
        int count = 0;
        while(result.isAfterLast() == false){
            count++;
            result.moveToNext();
        }
        all_grievances = new String[count][4];
        result.moveToFirst();
        int i = 0;
        while(result.isAfterLast() == false){
            all_grievances[i][0] = result.getString(result.getColumnIndex(COL_ID));
            all_grievances[i][1] = result.getString(result.getColumnIndex(COL_NAME));
            all_grievances[i][2] = result.getString(result.getColumnIndex(COL_FILE_NAME));
            all_grievances[i][3] = result.getString(result.getColumnIndex(COL_INSERTED_ON));
            i++;
            result.moveToNext();
        }
        return all_grievances;
    }
}
