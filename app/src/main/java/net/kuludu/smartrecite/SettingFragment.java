package net.kuludu.smartrecite;

import android.app.Activity;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetsbasedata.AssetsDatabaseManager;

public class SettingFragment extends Fragment {
    private Spinner openApp = null;
    private Spinner difficulty = null;
    private Spinner allNumber = null;
    private Spinner newNumber = null;
    private Spinner reviseNumber = null;

    public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        public void init() {
            String[] arr_openApp = {"开启", "关闭"};
            String[] arr_difficulty = {"四级难度", "六级难度"};
            String[] arr_allNumber = {"3", "5", "9", "12"};
            String[] arr_newNumber = {"3", "5", "9", "12"};
            String[] arr_reviseNumber = {"3", "5", "9", "12"};
            ArrayAdapter<String> adapter_openApp = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_openApp);
            ArrayAdapter<String> adapter_difficulty = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_difficulty);
            ArrayAdapter<String> adapter_allNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_allNumber);
            ArrayAdapter<String> adapter_newNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_newNumber);
            ArrayAdapter<String> adapter_reviseNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_reviseNumber);
            openApp.setAdapter(adapter_openApp);
            difficulty.setAdapter(adapter_difficulty);
            allNumber.setAdapter(adapter_allNumber);
            newNumber.setAdapter(adapter_newNumber);
            reviseNumber.setAdapter(adapter_reviseNumber);
            openApp.setOnItemSelectedListener(this);
            difficulty.setOnItemSelectedListener(this);
            allNumber.setOnItemSelectedListener(this);
            newNumber.setOnItemSelectedListener(this);
            reviseNumber.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String content = adapterView.getItemAtPosition(i).toString();
            switch (adapterView.getId()) {
                case R.id.spinner_open:
                    Toast.makeText(getActivity(), "您" + content + "单词锁屏功能", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_difficulty:
                    Toast.makeText(getActivity(), "您选择了" + content, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_all_number:
                    Toast.makeText(getActivity(), "您设置了需要解锁题的个数为" + content + "个", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_new_number:
                    Toast.makeText(getActivity(), "您设置了每日新题的个数为" + content + "个", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_revise_number:
                    Toast.makeText(getActivity(), "您设置了每日复习题的个数为" + content + "个", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_setting, null);
        initControl(view);
        AssetsDatabaseManager.initManager(getActivity());
        new SpinnerActivity().init();
        return view;
    }

    private void initControl(View view) {
        difficulty = view.findViewById(R.id.spinner_difficulty);
        openApp = view.findViewById(R.id.spinner_open);
        allNumber = view.findViewById(R.id.spinner_all_number);
        newNumber = view.findViewById(R.id.spinner_new_number);
        reviseNumber = view.findViewById(R.id.spinner_revise_number);
    }
}