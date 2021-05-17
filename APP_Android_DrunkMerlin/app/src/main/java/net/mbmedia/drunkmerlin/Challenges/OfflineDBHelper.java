package net.mbmedia.drunkmerlin.Challenges;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import net.mbmedia.drunkmerlin.TO.Drink;
import net.mbmedia.drunkmerlin.TO.Frage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OfflineDBHelper extends SQLiteOpenHelper {

    public static final String LOG = "ChallengeOfflineDBHelper";

    public static final String DATABASE_NAME = "Offline_Challenges";
    public static final Integer DATABASE_VERSION = 1;

    public final String TABLE_NAME = "Frage";
    public final String ID = "ID";
    public final String FRAGE = "Frage";


    public OfflineDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_Table = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FRAGE + " TEXT" + ")";

        db.execSQL(create_Table);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int getFragenCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public void addFrage(Frage frage){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if(frage.getId() >= 0){
            values.put(ID, frage.getId());
            values.put(FRAGE, frage.get());
        }else{
            values.put(FRAGE, frage.get());
        }

        db.insert(TABLE_NAME, null, values);

        db.close();

    }

    public void delFrage(Frage frage){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(frage.getId()) });
        db.close();
    }

    public ArrayList<Frage> getAlleFragen(){
        ArrayList<Frage> fragen = new ArrayList<Frage>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Frage tmp = new Frage();
                tmp.setId(cursor.getInt(0));
                tmp.set(cursor.getString(1));
                fragen.add(tmp);

            } while (cursor.moveToNext());
        }

        return fragen;
    }

    public void delAlleFragen(){
        String del = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(del);
    }

    public void setStandardFragen(){

        String[] fragen = StandardFragenSet.getStandardFragen();

        for(int i=0; i<fragen.length; i++){
            addFrage(new Frage(fragen[i]));
        }


    }

    public ArrayList<Frage> getZufallsFragen(int maxZahl){
        //SELECT * FROM table ORDER BY RANDOM() LIMIT 1
        ArrayList<Frage> fragen = new ArrayList<Frage>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT " + maxZahl;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Frage tmp = new Frage();
                tmp.setId(cursor.getInt(0));
                tmp.set(cursor.getString(1));
                fragen.add(tmp);

            } while (cursor.moveToNext());
        }

        return fragen;
    }

}
