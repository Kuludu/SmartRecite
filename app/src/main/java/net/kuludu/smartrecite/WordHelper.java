package net.kuludu.smartrecite;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordHelper {
    private String localDatabaseFilePath;
    private String remoteDatabaseFilePath;
    private File localDatabaseFile;
    private Context context;

    public WordHelper(Context context) {
        localDatabaseFilePath = context.getApplicationContext().getFilesDir() + "/word.db";
        // TODO : Fetch from Preference
        remoteDatabaseFilePath = context.getString(R.string.server_url) + "/word";
        this.context = context;

        localDatabaseFile = new File(localDatabaseFilePath);
        if (!isDatabaseExists()) {
            fetchDB();
        }
    }

    public boolean isDatabaseExists() {
        if (localDatabaseFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void fetchDB() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(remoteDatabaseFilePath));
        request.setTitle(context.getResources().getString(R.string.download_db));
        request.setDestinationInExternalFilesDir(context, null, "word.db");
        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
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

    public Integer getWordsCount(String level) {
        SQLiteDatabase db = openDatabase();

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query(level, new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor.moveToFirst();
        Integer wordCount = cursor.getInt(0);

        cursor.close();
        db.close();

        return wordCount;
    }

    public List<Word> getWords(String level) {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();

        if (db == null) {
            return null;
        }

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

    public List<Word> getRandXWords(String level, int requireNum) {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();
        Cursor cursor;

        if (db == null) {
            return null;
        }

        int totalWordCount = getWordsCount(level);
        boolean isWordCountExceed = false;

        if (totalWordCount < requireNum) {
            isWordCountExceed  = true;

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

    public Word getXWord(String level, Integer x) {
        SQLiteDatabase db = openDatabase();

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query(level, null, "`index`=?", new String[]{x.toString()}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));

            cursor.close();
            db.close();

            return word;
        }

        db.close();
        cursor.close();

        return null;
    }

    public int setLastReview(String level, Integer lastReview, Integer id) {
        SQLiteDatabase db = openDatabase();

        if (db == null) {
            return 0;
        }

        ContentValues cv = new ContentValues();
        cv.put("last_review", lastReview);

        return db.update(level, cv, "`index`", new String[]{id.toString()});
    }
}
