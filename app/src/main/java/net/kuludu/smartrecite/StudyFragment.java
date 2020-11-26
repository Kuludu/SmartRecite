package net.kuludu.smartrecite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assetsbasedata.AssetsDatabaseManager;

import java.util.HashSet;
import java.util.Set;

public class StudyFragment extends Fragment {
    private TextView difficultyText;
    private TextView quoteEnglishText;
    private TextView quoteChinaText;
    private TextView alreadyStudyText;
    private TextView alreadyMasterText;
    private TextView wrongText;
    private QuoteHelper quoteHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onStart() {
        super.onStart();
        setText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_study, null);
        initControl(view);
        AssetsDatabaseManager.initManager(getActivity());
        return view;
    }

    private void setText() {
        Quote quote = quoteHelper.getRandQuote();
        String quoteChinese = quote.getChinese();
        String quoteEnglish = quote.getEnglish();
        quoteEnglishText.setText(quoteEnglish);
        quoteChinaText.setText(quoteChinese);

        Set<String> wrong = sharedPreferences.getStringSet("wrong", new HashSet<>());
        String wrongCount = String.valueOf(wrong.size());
        wrongText.setText(wrongCount);

        Set<String> right = sharedPreferences.getStringSet("right", new HashSet<>());
        String rightCount = String.valueOf(right.size());
        alreadyMasterText.setText(rightCount);

        String totalCount = String.valueOf(wrong.size() + right.size());

        alreadyStudyText.setText(totalCount);
        String level = sharedPreferences.getString("level", "cet_4");
        if (level.equals("cet_4")) {
            difficultyText.setText("四级难度");
        } else if (level.equals("cet_6")) {
            difficultyText.setText("六级难度");
        }
    }

    private void initControl(View view) {
        difficultyText = view.findViewById(R.id.difficulty_text);
        quoteEnglishText = view.findViewById(R.id.wisdom_english);
        quoteChinaText = view.findViewById(R.id.wisdom_china);
        alreadyStudyText = view.findViewById(R.id.already_study);
        alreadyMasterText = view.findViewById(R.id.already_mastered);
        wrongText = view.findViewById(R.id.wrong_text);
        quoteHelper = new QuoteHelper(getActivity());
    }

}