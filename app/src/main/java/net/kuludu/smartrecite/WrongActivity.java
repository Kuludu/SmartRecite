package net.kuludu.smartrecite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class WrongActivity extends AppCompatActivity {
    private Button nextWrong;
    private ImageButton backBtn;
    private TextView chinaText, wordText, englishText;
    private SharedPreferences sharedPreferences;
    private WordHelper wordHelper;
    private ImageView playVoice;
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
            if(x2 - x1 > 200){
                nextWrong();
            }
        }
        return super.onTouchEvent(event);
    }

    private void initControl(){
        chinaText = findViewById(R.id.china_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        nextWrong = findViewById(R.id.i_know_btn);
        backBtn = findViewById(R.id.back_btn);
        playVoice = findViewById(R.id.play_voice);
        wordHelper = new WordHelper(this);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        Set<String> wrong= sharedPreferences.getStringSet("wrong",new LinkedHashSet<>());
        it = wrong.iterator();

        nextWrong.setOnClickListener(view -> {
            try{
                it.remove();
                nextWrong();
            }catch (IllegalStateException e){
                setFinalText();
            }
        });
        backBtn.setOnClickListener(view -> finish());
    }

    private void setText(Word word){
        chinaText.setText(word.getChinese());
        wordText.setText(word.getWord());
        englishText.setText(word.getSoundmark());
        playVoice.setVisibility(View.VISIBLE);
    }

    private void setFinalText(){
        chinaText.setText("你真棒！");
        wordText.setText("");
        englishText.setText("");
        playVoice.setVisibility(View.INVISIBLE);
    }

    private void nextWrong(){
        if(it.hasNext()){
            String wordIndex = (String) it.next();
            Word word = wordHelper.getXWord(Integer.parseInt(wordIndex));
            setText(word);
        }else{
            setFinalText();
        }
    }
}