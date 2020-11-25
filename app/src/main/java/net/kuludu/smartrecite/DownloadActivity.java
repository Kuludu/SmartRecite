package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.EventListener;

public class DownloadActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private WordHelper wordHelper;
    private QuoteHelper quoteHelper;
    private boolean isFileValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getString("server_url", null) == null) {
            editor.putString("server_url", "http://134.175.26.145");
        }
        if (sharedPreferences.getString("level", null) == null) {
            editor.putString("level", "cet_4");
        }
        editor.apply();

        wordHelper = new WordHelper(this);
        isFileValid = true;
        if (!wordHelper.isDatabaseExists()) {
            Toast.makeText(this, getString(R.string.wait_for_db_download), Toast.LENGTH_SHORT).show();
            isFileValid = false;
        }

        quoteHelper = new QuoteHelper(this);
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

    private class HttpEventListener extends EventListener {

    }
}