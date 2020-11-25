package net.kuludu.smartrecite;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.Random;

public class QuoteHelper {
    private String localQuoteFilePath;
    private String remoteQuoteFilePath;
    private File localQuoteFile;
    private Context context;

    public QuoteHelper(Context context) {
        localQuoteFilePath = context.getApplicationContext().getFilesDir() + "/quote.db";
        // TODO : Fetch from Preference
        remoteQuoteFilePath = context.getString(R.string.server_url) + "/quote";
        this.context = context;

        localQuoteFile = new File(localQuoteFilePath);
        if (!isQuoteExists()) {
            fetchQuote();
        }
    }

    public boolean isQuoteExists() {
        if (localQuoteFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void fetchQuote() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(remoteQuoteFilePath));
        request.setTitle(context.getResources().getString(R.string.download_quote));
        request.setDestinationInExternalFilesDir(context, null, "quote.db");
        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
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
