package net.kuludu.smartrecite;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assetsbasedata.AssetsDatabaseManager;

public class StudyFragment extends Fragment {
    private TextView difficultyText;
    private TextView quoteEnglishText;
    private TextView quoteChinaText;
    private TextView alreadyStudyText;
    private TextView alreadyMasterText;
    private TextView wrongText;
    private QuoteHelper quoteHelper;

    public StudyFragment() {
        // Required empty public constructor
    }
    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setText();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.study_frame, null);
        initControl(view);
        AssetsDatabaseManager.initManager(getActivity());
        return view;
    }

    private void setText(){
        Quote quote = quoteHelper.getRandQuote();
        String quoteChinese = quote.getChinese();
        String quoteEnglish = quote.getEnglish();
        quoteEnglishText.setText(quoteEnglish);
        quoteChinaText.setText(quoteChinese);
    }

    private void initControl(View view){
        difficultyText = view.findViewById(R.id.difficulty_text);
        quoteEnglishText = view.findViewById(R.id.wisdom_english);
        quoteChinaText = view.findViewById(R.id.wisdom_china);
        alreadyStudyText = view.findViewById(R.id.already_study);
        alreadyMasterText = view.findViewById(R.id.already_mastered);
        wrongText = view.findViewById(R.id.wrong_text);
        quoteHelper = new QuoteHelper(getActivity());
    }

}