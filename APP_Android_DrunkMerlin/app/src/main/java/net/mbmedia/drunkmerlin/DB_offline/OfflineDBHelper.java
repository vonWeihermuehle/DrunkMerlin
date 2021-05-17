package net.mbmedia.drunkmerlin.DB_offline;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import net.mbmedia.drunkmerlin.TO.Drink;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OfflineDBHelper extends SQLiteOpenHelper {

    public static final String LOG = "OfflineDBHelper";

    public static final String DATABASE_NAME = "Offline_Drinks";
    public static final Integer DATABASE_VERSION = 1;

    public final String TABLE_NAME = "Drink";
    public final String DRINK_ID = "ID";
    public final String DRINK_NAME = "Name";
    public final String DRINK_PROZENT = "Prozent";
    public final String DRINK_MENGE = "Menge";
    public final String DRINK_ZEIT = "Zeit";
    public final String DRINK_PROMILLE = "Promille";



    public OfflineDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_Table = "CREATE TABLE " + TABLE_NAME + "(" +
                DRINK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DRINK_NAME + " TEXT," +
                DRINK_PROZENT + " TEXT," +
                DRINK_MENGE + " TEXT," +
                DRINK_ZEIT + " TEXT" + ")";

        db.execSQL(create_Table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int getDrinksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public void addDrink(Drink drink){

        if(drink.getZeit() == null){
            Log.i(LOG, "zeit ist null");
            drink.setZeit(getTime());
        }
        Log.i(LOG, "zeit: " + drink.getZeit());





        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DRINK_NAME, drink.getName());
        values.put(DRINK_MENGE, drink.getMenge());
        values.put(DRINK_PROZENT, drink.getProzent());
        values.put(DRINK_ZEIT, drink.getZeit());

        db.insert(TABLE_NAME, null, values);

        db.close();

    }

    public void delDrink(Drink drink){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, DRINK_ID + " = ?", new String[] { String.valueOf(drink.getId()) });
        db.close();
    }

    public ArrayList<Drink> getAllDrinks(){
        ArrayList<Drink> drinks = new ArrayList<Drink>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + DRINK_ZEIT + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Drink tmp_drink = new Drink();
                tmp_drink.setId(cursor.getString(0));
                tmp_drink.setName(cursor.getString(1));
                tmp_drink.setMenge(cursor.getString(2));
                tmp_drink.setProzent(cursor.getString(3));
                tmp_drink.setZeit(cursor.getString(4) + ":00.0");
                // Adding note to list
                drinks.add(tmp_drink);

                Log.i(LOG, tmp_drink.getId() + " " + tmp_drink.getName() + " " + tmp_drink.getMenge() + " " +
                        tmp_drink.getProzent() + " " + tmp_drink.getZeit());
            } while (cursor.moveToNext());
        }

        return  drinks;
    }

    public void addBeispielDrink(){
        Drink drink = new Drink();
        drink.setName("Beispiel");
        drink.setProzent("5.0");
        drink.setMenge("0.5");
        drink.setZeit(getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DRINK_NAME, drink.getName());
        values.put(DRINK_MENGE, drink.getMenge());
        values.put(DRINK_PROZENT, drink.getProzent());
        values.put(DRINK_ZEIT, drink.getZeit());

        db.insert(TABLE_NAME, null, values);

        db.close();

    }

    public void delAlleDrinks(){
        String del = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(del);
    }

    public String getTime(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
