package net.kuludu.smartrecite;

import android.app.DownloadManager;
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
    private SQLiteDatabase db;
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
        if (!localDatabaseFile.exists()) {
            fetchDB();
        }

        db = openDatabase();
    }

    private void fetchDB() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(remoteDatabaseFilePath));
        request.setTitle(context.getResources().getString(R.string.download_db));
        request.setDestinationInExternalFilesDir(context, null, "word.db");
        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    private SQLiteDatabase openDatabase() {
        if (localDatabaseFile.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(localDatabaseFile, null);

            return db;
        } else {
            Log.e("WordHelper", "Database not found!");
        }

        return null;
    }

    public List<Word> getWords() {
        List<Word> result = new ArrayList<>();

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query("word", null, null, null, null, null, null);
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

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query("word", new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor.moveToFirst();
        int totalWordCount = cursor.getInt(0);
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
            cursor = db.query("word", null, "`index` = " + index.toString(), null, null, null, null);
            cursor.moveToFirst();
            Word word = new Word(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
            result.add(word);
        }

        cursor.close();
        db.close();

        return result;
    }
}
