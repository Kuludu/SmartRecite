package net.kuludu.smartrecite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class WrongActivity extends AppCompatActivity {
    private Button nextWrong;
    private ImageButton backBtn;
    private TextView chinaText, wordText, englishText;
    private SharedPreferences sharedPreferences;
    private WordHelper wordHelper;
    Iterator it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_wrong);
        initControl();
        nextWrong();
    }


    private void initControl(){
        chinaText = findViewById(R.id.china_text);
        wordText = findViewById(R.id.word_text);
        englishText = findViewById(R.id.english_text);
        nextWrong = findViewById(R.id.i_know_btn);
        backBtn = findViewById(R.id.back_btn);

        wordHelper = new WordHelper(this);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        Set<String> wrong= sharedPreferences.getStringSet("wrong",new HashSet<>());
        it = wrong.iterator();

        nextWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextWrong();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setText(Word word){
        chinaText.setText(word.getChinese());
        wordText.setText(word.getWord());
        englishText.setText(word.getSoundmark());
    }

    private void nextWrong(){
        if(it.hasNext()){
            String index = (String) it.next();
            Word word = wordHelper.getXWord(Integer.parseInt(index));
            setText(word);
        }else{
            Toast.makeText(this,"天哪，你好厉害",Toast.LENGTH_SHORT).show();
        }
    }



}