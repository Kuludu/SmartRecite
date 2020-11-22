package net.kuludu.smartrecite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WordHelper {
    SQLiteDatabase db;
    DBHelper helper;

    public List<Word> getWords(Context context) {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase();
        List<Word> result = new ArrayList<>();

        Cursor cursor = db.query("word", null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
            result.add(word);
        }
        while (cursor.moveToNext()) {
            Word word = new Word(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));
            result.add(word);
        }

        cursor.close();
        db.close();

        return result;
    }

    private void fetchDB() {

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "word.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            fetchDB();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
