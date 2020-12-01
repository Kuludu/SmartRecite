package net.kuludu.smartrecite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public class WrongActivity extends AppCompatActivity {
    private TextView chinaText, wordText, englishText;
    private WordHelper wordHelper;
    private ImageView playVoice;
    private TextToSpeech textToSpeech;
    Iterator it;
    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);
        initControl();
        nextWrong();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            if (x2 - x1 > 200) {
                nextWrong();
            }
        }
        return super.onTouchEvent(event);
    }

    private void initControl() {
        chinaText = findViewById(R.id.china_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        Button nextWrong = findViewById(R.id.i_know_btn);
        ImageButton backBtn = findViewById(R.id.back_btn);
        playVoice = findViewById(R.id.play_voice);
        nextWrong.setVisibility(View.VISIBLE);

        wordHelper = new WordHelper(this);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        Set<String> wrong = sharedPreferences.getStringSet("wrong", new LinkedHashSet<>());
        it = wrong.iterator();
        nextWrong.setOnClickListener(view -> {
            try {
                it.remove();
                nextWrong();
            } catch (IllegalStateException e) {
                setFinalText();
            }
        });
        backBtn.setOnClickListener(view -> finish());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1.5f);
                } else {
                    Toast.makeText(WrongActivity.this, "语言功能初始化失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        playVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = wordText.getText().toString();
                textToSpeech.speak(content, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    private void setText(Word word) {
        chinaText.setText(word.getChinese());
        wordText.setText(word.getWord());
        englishText.setText(word.getSoundmark());
        playVoice.setVisibility(View.VISIBLE);
    }

    private void setFinalText() {
        chinaText.setText("复习错题完毕！");
        wordText.setText("");
        englishText.setText("");
        playVoice.setVisibility(View.INVISIBLE);
    }

    private void nextWrong() {
        if (it.hasNext()) {
            String wordIndex = (String) it.next();
            Word word = wordHelper.getXWord(Integer.parseInt(wordIndex));
            setText(word);
        } else {
            setFinalText();
        }
    }
}