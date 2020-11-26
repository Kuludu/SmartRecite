package net.kuludu.smartrecite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.Random;

public class QuoteHelper {
    private String localQuoteFilePath;
    private File localQuoteFile;
    private Context context;
    private SharedPreferences sharedPreferences;

    public QuoteHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        this.context = context;
        localQuoteFilePath = context.getApplicationContext().getFilesDir() + "/quote.db";
        localQuoteFile = new File(localQuoteFilePath);
    }

    public boolean isQuoteExists() {
        return localQuoteFile.exists();
    }

    private SQLiteDatabase openDatabase() {
        if (isQuoteExists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(localQuoteFile, null);

            return db;
        } else {
            Log.e("QuoteHelper", "Database not found!");
        }

        return null;
    }

    public Quote getRandQuote() {
        SQLiteDatabase db = openDatabase();
        Random random = new Random();

        if (db == null) {
            return null;
        }

        Cursor cursor = db.query("quote", new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor.moveToFirst();
        Integer index = random.nextInt(cursor.getInt(0));

        cursor = db.query("quote", null, "`index`=?", new String[]{index.toString()}, null, null, null);
        cursor.moveToFirst();
        Quote quote = new Quote(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        cursor.close();
        db.close();

        return quote;
    }
}
