package net.kuludu.smartrecite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginHelper {
    private String username;
    private String password;
    private String token;
    private SharedPreferences sharedPreferences;
    private String server_url;
    private Handler login_handler;
    private Handler push_handler;
    private Handler fetch_handler;
    private String localWordFilePath;

    public LoginHelper(Context context, String username, String password) {
        this.username = username;
        this.password = password;

        localWordFilePath = context.getFilesDir() + "/word.db";
        sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        server_url = sharedPreferences.getString("server_url", "");
        login_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 0) {
                    Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show();
                } else {
                    token = (String) msg.obj;
                    Toast.makeText(context, "Login success!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        push_handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                if (msg.what == 0) {
                    Toast.makeText(context, "Push failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Push success!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        fetch_handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                if (msg.what == 0) {
                    Toast.makeText(context, "Fetch failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Fetch success!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void login() {
        try {
            final String login_entry = server_url + "/login";
            URL url = new URL(login_entry);
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 0;
                    login_handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    Message message = new Message();
                    if (!resp.equals("Bad authentication.")) {
                        message.obj = resp;
                        message.what = 1;
                    } else {
                        message.what = 0;
                    }
                    login_handler.sendMessage(message);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void upload() {
        if (token == null) {
            return;
        }

        try {
            final String push_entry = server_url + "/api/upload";
            File localWordFile = new File(localWordFilePath);
            URL url = new URL(push_entry);
            OkHttpClient client = new OkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            RequestBody fileBody = RequestBody.create(localWordFile, MediaType.parse("application/octet-stream"));
            requestBody.addFormDataPart("token", token)
                    .addFormDataPart("db", username + ".sqlite", fileBody);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody.build())
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 0;
                    push_handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String resp = response.body().string();
                    Message message = new Message();
                    if (resp.equals("Successfully uploaded.")) {
                        message.what = 1;
                    } else {
                        message.what = 0;
                    }
                    push_handler.sendMessage(message);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void fetch() {
        if (token == null) {
            return;
        }

        try {
            final String fetch_entry = server_url + "/api/fetch";
            URL url = new URL(fetch_entry);
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("token", token)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = new Message();
                    message.what = 0;
                    fetch_handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody respBody = response.body();
                    String resp = respBody.string();
                    Message message = new Message();
                    if (!resp.equals("Bad authentication.")) {
                        FileOutputStream fos = new FileOutputStream(localWordFilePath);
                        fos.write(resp.getBytes());
                        fos.close();

                        message.what = 1;
                    } else {
                        message.what = 0;
                    }
                    fetch_handler.sendMessage(message);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
