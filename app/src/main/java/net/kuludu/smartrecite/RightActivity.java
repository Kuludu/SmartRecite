package net.kuludu.smartrecite;

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
import java.util.List;
import java.util.Locale;

public class RightActivity extends AppCompatActivity {
    private TextView chinaText, wordText, englishText;
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
        WordHelper wordHelper = new WordHelper(this);

        nextWrong.setVisibility(View.INVISIBLE);
        List<Word> right = wordHelper.getLearnedWord();
        it = right.iterator();
        backBtn.setOnClickListener(view -> finish());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1.5f);
                } else {
                    Toast.makeText(RightActivity.this, "语言功能初始化失败", Toast.LENGTH_SHORT).show();
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
        chinaText.setText("复习完毕！");
        wordText.setText("");
        englishText.setText("");
        playVoice.setVisibility(View.INVISIBLE);
    }

    private void nextWrong() {
        if (it.hasNext()) {
            Word word = (Word) it.next();
            setText(word);
        } else {
            setFinalText();
        }
    }
}