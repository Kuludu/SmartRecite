package net.kuludu.smartrecite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WordHelper wordHelper;
    private QuoteHelper quoteHelper;
    private String remoteWordFilePath;
    private String remoteQuoteFilePath;
    private String localWordFilePath;
    private String localQuoteFilePath;
    private TextView count;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Integer cur_count = Integer.parseInt(count.getText().toString());
                cur_count++;
                count.setText(cur_count.toString());
            }
        };
        count = findViewById(R.id.count);

        initDatabase();
        checkDatabase();
    }

    private void initDatabase() {
        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("server_url", null) == null) {
            editor.putString("server_url", "http://134.175.26.145");
        }
        if (sharedPreferences.getString("level", null) == null) {
            editor.putString("level", "cet_4");
        }
        editor.apply();

        remoteWordFilePath = sharedPreferences.getString("server_url", "") + "/word";
        remoteQuoteFilePath = sharedPreferences.getString("server_url", "") + "/quote";
        localWordFilePath = getApplicationContext().getFilesDir() + "/word.db";
        localQuoteFilePath = getApplicationContext().getFilesDir() + "/quote.db";
        wordHelper = new WordHelper(this);
        quoteHelper = new QuoteHelper(this);
        if (!wordHelper.isDatabaseExists()) {
            fetch(remoteWordFilePath, localWordFilePath);
        }
        if (!quoteHelper.isQuoteExists()) {
            fetch(remoteQuoteFilePath, localQuoteFilePath);
        }
    }

    private void checkDatabase() {
        boolean isFileValid = true;

        if (!wordHelper.isDatabaseExists()) {
            Toast.makeText(this, getString(R.string.wait_for_db_download), Toast.LENGTH_SHORT).show();

            isFileValid = false;
        }

        if (!quoteHelper.isQuoteExists()) {
            Toast.makeText(this, getString(R.string.wait_for_quote_download), Toast.LENGTH_SHORT).show();

            isFileValid = false;
        }

        if (!isFileValid) {
            Log.w("DB", "Database file invalid.");
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void fetch(String remote_path, String local_path) {
        try {
            URL url = new URL(remote_path);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(DownloadActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] buf = response.body().bytes();
                    FileOutputStream fos = new FileOutputStream(local_path);
                    fos.write(buf, 0, buf.length);

                    fos.close();

                    handler.sendEmptyMessage(0);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}