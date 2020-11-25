package net.kuludu.smartrecite;

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
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuoteHelper {
    private String localQuoteFilePath;
    private String remoteQuoteFilePath;
    private File localQuoteFile;
    private Context context;
    private SharedPreferences sharedPreferences;

    public QuoteHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        localQuoteFilePath = context.getApplicationContext().getFilesDir() + "/quote.db";
        remoteQuoteFilePath = sharedPreferences.getString("server_url", "") + "/quote";
        this.context = context;

        localQuoteFile = new File(localQuoteFilePath);
        if (!isQuoteExists()) {
            fetchQuote();
        }
    }

    public boolean isQuoteExists() {
        return localQuoteFile.exists();
    }

    private void fetchQuote() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(remoteQuoteFilePath).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] buf = response.body().bytes();
                FileOutputStream fos = new FileOutputStream(localQuoteFile);
                fos.write(buf, 0, buf.length);

                fos.close();
            }
        });
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
