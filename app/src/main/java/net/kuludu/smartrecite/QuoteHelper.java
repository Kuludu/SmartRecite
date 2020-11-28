package net.kuludu.smartrecite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.Random;

public class QuoteHelper {
    private File localQuoteFile;

    public QuoteHelper(Context context) {
        String localQuoteFilePath = context.getApplicationContext().getFilesDir() + "/quote.db";
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
        assert db != null;
        Random random = new Random();

        Cursor cursor = db.query("quote", new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursor.moveToFirst();
        int index = random.nextInt(cursor.getInt(0));

        cursor = db.query("quote", null, "`index`=?", new String[]{Integer.toString(index)}, null, null, null);
        cursor.moveToFirst();
        Quote quote = new Quote(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        cursor.close();
        db.close();

        return quote;
    }
}
