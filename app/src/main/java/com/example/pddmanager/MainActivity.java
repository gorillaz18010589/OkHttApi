package com.example.pddmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pddmanager.request.ApiRequest;
import com.example.pddmanager.request.LoginRequest;
import com.example.pddmanager.result.LoginResult;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String url = "https://shop.ljz789.com/index.php?store_id=8";
    private String TAG = "hank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    //1.application/x-www-form-urlencoded格式 post 原始方式
    public void formBody(View view) {
        LoginRequest loginRequest = new LoginRequest("13232236359", "aaaa1111", "");

        //FormBody.Builder() //提交表單的物件實體
        RequestBody requestBody = new FormBody.Builder()//建立提交表單物件實體
                .add("module", "app") //新增沒有編碼的字船參數(key:value)
                .add("action", "login")
                .add("app", "login")
                .add("phone", loginRequest.phone)
                .add("password", loginRequest.password)
                .add("cart_id", loginRequest.cart_id)
//                .addEncoded(); //新增有編碼的字串參數(key:value)
                .build();//表單建立完成
        Log.v("hank", requestBody.toString());

        Request request = new Request.Builder() //建立Request物件
                .url(url)//設定Request連線的網址(要連線的字串網址)
                .post(requestBody)//走Post方法傳送參數(要傳送的參數key:value)
                .build();//request建立完成

        OkHttpClient okHttpClient = new OkHttpClient(); //OkHttpClient建構式物件實體化
        okHttpClient
                .newCall(request)//準備在某時執行傳送request(要被傳送的Request)(回傳Call)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.v("hank", "onFailure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {

                            Headers headers = response.headers();//取得Haeders(回傳到Headers)
                            for (int i = 0; i < headers.size(); i++) {
                                String name = headers.name(i);//name(int index)取得標頭擋的名稱(index)(回傳String)
                                String value = headers.get(name); //取得Headers相應欄位的值(String欄位名稱) (回傳String)
                                Log.v(TAG, "name:" + name + "/value:" + value);
                            }

                            String obj = response
                                    .body() // body():取得respnse裡的body(回傳ResponseBody)
                                    .string(); //string():將body內容轉成String(回傳String)
                            Log.v(TAG, "body:" + obj);
                            try {
                                JSONObject jsonObject = new JSONObject(obj);
//                                String access_id = jsonObject.getString("access_id");//傳統方式取欄位資料
                                Gson gson = new Gson();
                                LoginResult loginResult;
                                loginResult = gson.fromJson(jsonObject.toString(), LoginResult.class);

                                Log.v(TAG, "access_id:" + loginResult.accessId);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v(TAG, "解析失敗:" + e.toString());
                            }
                        }
                    }
                });
    }

    //2.post 傳遞contentType 為Json資料原始方法
    public void postJson(View view) {
        OkHttpClient okHttpClient1 = new OkHttpClient();

        MediaType contentType = MediaType.parse("application/json;charset=UTF-8");
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("module", "app");
            jsonObject.put("action", "login");
            jsonObject.put("app", "login");
            jsonObject.put("phone", "13232236359");
            jsonObject.put("password", "aaaa1111");
            jsonObject.put("cart_id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create(contentType, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("hank", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String msg = response.body().string();
                    int code = response.code();
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        String name = headers.name(i);
                        String value = headers.get(name);
                        Log.v(TAG, "headers:" + "name:" + name + "/value:" + value);
                    }

                    Log.v(TAG, "成功:" + msg + "/code:" + code);
                }

                try {
                    JSONObject row = new JSONObject("a");
                } catch (JSONException e) {
                    Log.v("hank", "解析失敗:" + e.toString());
                }

            }
        });

    }

    //解析Json方法
    private void parseJSON(String json) {
        try {
            JSONArray root = new JSONArray(json);
            for (int i = 0; i < root.length(); i++) {
                JSONObject row = root.getJSONObject(i);
                Log.v("brad", row.getString("Name") + ":" + row.getString("Tel"));
            }
        } catch (Exception e) {

        }


    }

    //3.doGet自己寫的Api
    public void doGetApi(View view) {
        MyOkHttpApi.instance().doGet("https://jsonplaceholder.typicode.com/posts", new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        Log.v("hank", "msg:" + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //4.FormBody:application/x-www-form-urlencoded格式 post(API)
    public void formBodyApi(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "login");
        map.put("app", "login");
        map.put("phone", "13232236359");
        map.put("password", "aaaa1111");
        map.put("cart_id", "");
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String obj = response.body().string();
                        Gson gson = new Gson();
                        LoginResult login = gson.fromJson(obj, LoginResult.class);
                        Log.v(TAG, "accessId:" + login.accessId);

                    } catch (IOException e) {
                        Log.v(TAG, "IOException:" + e.toString());
                    }
                }
            }
        });
    }

    //5.直接帶入JavaBean轉成Map用FormBody傳送參數的API
    public void javaBeanToMapApi(View view) {


        LoginRequest loginRequest = new LoginRequest("13232236359","aaaa1111","");
        Map<String,String> map =  MyOkHttpApi.javaBeanToMap(loginRequest);

        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String obj = response.body().string();
                        Gson gson = new Gson();
                        LoginResult login = gson.fromJson(obj, LoginResult.class);
                        Log.v(TAG, "accessId:" + login.accessId);

                    } catch (IOException e) {
                        Log.v(TAG, "IOException:" + e.toString());
                    }
                }
            }
        });
    }

    public void getParamFromJson(View view) {
        LoginRequest loginRequest = new LoginRequest("04111111","123456","aa");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);

        MyOkHttpApi.getParaFromJson(json,"phone");
    }
}