package net.kuludu.smartrecite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordHelper {
    private String localWordFilePath;
    private File localDatabaseFile;
    private SharedPreferences sharedPreferences;

    public WordHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        localWordFilePath = context.getApplicationContext().getFilesDir() + "/word.db";
        localDatabaseFile = new File(localWordFilePath);
    }

    public boolean isDatabaseExists() {
        return localDatabaseFile.exists();
    }

    private SQLiteDatabase openDatabase() {
        if (isDatabaseExists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(localDatabaseFile, null);

            return db;
        } else {
            Log.e("WordHelper", "Database not found!");
        }

        return null;
    }

    public Integer getWordsCount() {
        SQLiteDatabase db = openDatabase();
        assert db != null;
        String level = sharedPreferences.getString("level", "cet_4");

        Cursor cursor = db.query(level, new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor.moveToFirst();
        Integer wordCount = cursor.getInt(0);

        cursor.close();
        db.close();

        return wordCount;
    }

    public List<Word> getWords() {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();
        assert db != null;
        String level = sharedPreferences.getString("level", "cet_4");

        Cursor cursor = db.query(level, null, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            result.add(word);
        }
        while (cursor.moveToNext()) {
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            result.add(word);
        }

        cursor.close();
        db.close();

        return result;
    }

    public List<Word> getRandXWords(int requireNum) {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();
        assert db != null;
        Cursor cursor;
        String level = sharedPreferences.getString("level", "cet_4");

        int totalWordCount = getWordsCount();
        boolean isWordCountExceed = false;

        if (totalWordCount < requireNum) {
            isWordCountExceed = true;

            Log.w("WordHelper", "Word count exceed!");
        }

        // TODO: Performance is not optimal
        Set<Integer> wordIndex = new HashSet<>();
        while (!isWordCountExceed && wordIndex.size() < requireNum) {
            Random random = new Random();
            wordIndex.add(random.nextInt(totalWordCount));
        }

        for (Integer index : wordIndex) {
            cursor = db.query(level, null, "`index`=?", new String[]{index.toString()}, null, null, null, null);
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            cursor.close();
            result.add(word);
        }

        db.close();

        return result;
    }

    public Word getXWord(Integer index) {
        SQLiteDatabase db = openDatabase();
        assert db != null;
        String level = sharedPreferences.getString("level", "cet_4");

        Cursor cursor = db.query(level, null, "`index`=?", new String[]{index.toString()}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

            cursor.close();
            db.close();

            return word;
        }

        cursor.close();
        db.close();

        return null;
    }

    public int setLastReview(Integer lastReview, Integer id) {
        SQLiteDatabase db = openDatabase();
        assert db != null;
        String level = sharedPreferences.getString("level", "cet_4");

        ContentValues cv = new ContentValues();
        cv.put("last_review", lastReview);

        int state = db.update(level, cv, "`index`=?", new String[]{id.toString()});

        db.close();

        return state;
    }

    public List<Word> getLearnedWord() {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();
        assert db != null;
        String level = sharedPreferences.getString("level", "cet_4");

        Cursor cursor = db.query(level, null, "last_review IS NOT NULL", null, "last_review", null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            result.add(word);
        }
        while (cursor.moveToNext()) {
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            result.add(word);
        }

        cursor.close();
        db.close();

        return result;
    }
}
