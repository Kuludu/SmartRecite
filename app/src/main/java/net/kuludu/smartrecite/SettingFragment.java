package net.kuludu.smartrecite;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assetsbasedata.AssetsDatabaseManager;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private Switch onLockScreen;
    private Spinner difficulty, allNumber;
    private EditText et_serverUrl, et_username, et_password;
    private Button btn_saveServerUrl, btn_saveUserPwd, btn_login, btn_fetch, btn_upload;
    private LoginHelper loginHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_setting, null);
        initView(view);
        AssetsDatabaseManager.initManager(getActivity());
        new SpinnerActivity().initAdapter();

        return view;
    }

    private void initView(View view) {
        onLockScreen = view.findViewById(R.id.on_lock_screen);
        difficulty = view.findViewById(R.id.spinner_difficulty);
        allNumber = view.findViewById(R.id.spinner_all_number);
        et_serverUrl = view.findViewById(R.id.et_server_url);
        et_username = view.findViewById(R.id.et_username);
        et_password = view.findViewById(R.id.et_password);
        btn_saveServerUrl = view.findViewById(R.id.save_server_url);
        btn_saveUserPwd = view.findViewById(R.id.save_user_pwd);
        btn_login = view.findViewById(R.id.login);
        btn_fetch = view.findViewById(R.id.fetch);
        btn_upload = view.findViewById(R.id.upload);

        onLockScreen.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("btnTf", isChecked);
            editor.apply();
        });
        btn_saveServerUrl.setOnClickListener(this);
        btn_saveUserPwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_fetch.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        et_serverUrl.setText(sharedPreferences.getString("server_url", ""));
        et_username.setText(sharedPreferences.getString("username", ""));
        et_password.setText(sharedPreferences.getString("password", ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_server_url:
                editor.putString("server_url", et_serverUrl.getText().toString());
                editor.apply();
                break;
            case R.id.save_user_pwd:
                editor.putString("username", et_username.getText().toString());
                editor.putString("password", et_password.getText().toString());
                editor.apply();
                break;
            case R.id.login:
                loginHelper = new LoginHelper(getContext(),
                        et_username.getText().toString(),
                        et_password.getText().toString());
                loginHelper.login();
            case R.id.fetch:
                loginHelper.fetch();
                break;
            case R.id.upload:
                loginHelper.upload();
                break;
        }
    }

    public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        public void initAdapter() {
            String[] arr_difficulty = {"CET-4", "CET-6"};
            String[] arr_allNumber = {"3", "5", "9", "12"};

            ArrayAdapter<String> adapter_difficulty = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_difficulty);
            ArrayAdapter<String> adapter_allNumber = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arr_allNumber);

            difficulty.setAdapter(adapter_difficulty);
            allNumber.setAdapter(adapter_allNumber);
            difficulty.setOnItemSelectedListener(this);
            allNumber.setOnItemSelectedListener(this);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}