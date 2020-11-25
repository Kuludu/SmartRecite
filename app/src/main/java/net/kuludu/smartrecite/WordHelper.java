package net.kuludu.smartrecite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordHelper {
    private String localDatabaseFilePath;
    private String remoteDatabaseFilePath;
    private File localDatabaseFile;
    private String level;
    private Context context;
    private SharedPreferences sharedPreferences;

    public WordHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        level = sharedPreferences.getString("level", "");
        localDatabaseFilePath = context.getApplicationContext().getFilesDir() + "/word.db";
        remoteDatabaseFilePath = sharedPreferences.getString("server_url", "") + "/word";
        this.context = context;

        localDatabaseFile = new File(localDatabaseFilePath);
        if (!isDatabaseExists()) {
            fetchDB();
        }
    }

    public boolean isDatabaseExists() {
        return localDatabaseFile.exists();
    }

    private void fetchDB() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(remoteDatabaseFilePath).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] buf = response.body().bytes();
                FileOutputStream fos = new FileOutputStream(localDatabaseFile);
                fos.write(buf, 0, buf.length);

                fos.close();
            }
        });
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

    public List<Word> getWords() {
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

    public List<Word> getRandXWords(int requireNum) {
        List<Word> result = new ArrayList<>();
        SQLiteDatabase db = openDatabase();
        Cursor cursor;

        if (db == null) {
            return null;
        }

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

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query(level, null, "`index`=?", new String[]{index.toString()}, null, null, null);

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

    public int setLastReview(Integer lastReview, Integer id) {
        SQLiteDatabase db = openDatabase();

        if (db == null) {
            return 0;
        }

        ContentValues cv = new ContentValues();
        cv.put("last_review", lastReview);

        return db.update(level, cv, "`index`=?", new String[]{id.toString()});
    }
}
