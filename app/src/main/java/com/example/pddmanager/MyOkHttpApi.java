package com.example.pddmanager;

import android.util.Log;
import android.view.View;

import androidx.constraintlayout.solver.GoalRow;
import androidx.lifecycle.MutableLiveData;

import com.example.pddmanager.request.ApiRequest;
import com.example.pddmanager.request.LoginRequest;
import com.example.pddmanager.result.ApiResult;
import com.example.pddmanager.result.GetMoreResult;
import com.example.pddmanager.result.LoginResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyOkHttpApi {
    private static OkHttpClient client = null;
    private static MyOkHttpApi instance;
    private static final String TAG = "hank";

    //初始化設定取得new MyOkHttpApi()建構好連線的物件實體
    public static MyOkHttpApi instance() {
        if (instance == null) instance = new MyOkHttpApi();
        return instance;
    }

    //1.設定OkHttpClient.Builder連線時間設定
    public MyOkHttpApi() {
        if (client == null) {
            client = new OkHttpClient
                    .Builder()//物件實體化Client.Builder
                    .readTimeout(120, TimeUnit.SECONDS)//設定連線讀取超時,值0表示沒有超時,default為10秒(1,long讀取時間,2.時間的類型)
                    .connectTimeout(120, TimeUnit.SECONDS)//設定連線超時,值0表示沒有超時,default為10秒(1,long讀取時間,2.時間的類型)
                    .writeTimeout(120, TimeUnit.SECONDS)//設定寫入連線超時,值0表示沒有超時,default為10秒(1,long讀取時間,2.時間的類型)
                    .build();//OkHttpClient.Builder設定完成
            Log.v(TAG, "MyOkHttpApi()");
        }
    }


    //2.自己定義的CallBack接口方法
    public interface OkHttpCallBack {
        void onFailure(IOException e); //只留IOException

        void onSuccess(Response response); //只留response
    }


    //3.自己定義的MyCallBack實作OkHttp.Callback
    private class MyCallBack implements Callback {
        private OkHttpCallBack okHttpCallBack; //宣告自己的街口

        //建構式時將自己的callBcak初始化,將自己的街口丟進來
        public MyCallBack(OkHttpCallBack okHttpCallBack) {
            this.okHttpCallBack = okHttpCallBack;
        }

        //這邊是實作Okhttp的onFailure方法,將IOException帶入到我自己寫的OkHttpCallBack裡的onFail方法
        @Override
        public void onFailure(Call call, IOException e) {
            okHttpCallBack.onFailure(e);
        }

        //onResponse,將response帶入到我自己寫的OkHttpCallBack裡的onSuccess方法
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            okHttpCallBack.onSuccess(response);
        }
    }

    /*4.自己寫doGet方法
     * @param 1.url => 連線網址
     * @param 2.MyCallBack => 自己實作CallBcak方法
     * */
    public void doGet(String url, OkHttpCallBack callBack) {
        MyCallBack myCallBack = new MyCallBack(callBack);
        Request request = new Request.Builder()
                .url(url)
                .build();

        //這邊直接幫忙連線
        client.newCall(request).enqueue(myCallBack);
    }


    /*
     * 5.doPost方法,用Form格式傳遞參數
     *@param:1.String url =>連線網址
     *@param:2.Map<String, String> map => key:value
     *@param:3.OkHttpCallBack okHttpCallBack => 自訂的CallBack街口
     * */
    public void doPostFormBody(String url, Map<String, String> map, OkHttpCallBack okHttpCallBack) {
        MyCallBack myCallBack = new MyCallBack(okHttpCallBack);
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.v(TAG, "key:" + key + "/value:" + value);
                formBuilder.add(key, value);
            }
        }

        FormBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(myCallBack);
    }


    /*6.javabean轉成Map
     *@param =>Object javabean:預轉成Map的javabean
     *@return =>Map<String, String>:回傳JsonMapo格式
     * */
    public static Map<String, String> javaBeanToMap(Object javabean) {
        Gson gson = new Gson();

        //將傳進來的bean轉成Json
        String json = gson.toJson(javabean);//toJson(Object src):將物件轉成字串Json(要被轉成Json的物件)(回傳String)
        Log.v(TAG, "gson.toJson:" + json); //{"cart_id":"aaa","}

        //將Json格式Sting資料,轉成Map<String, String>
        //fromJson(String json, Type typeOfT):從Json字串解析回傳到資料結構上(1.要轉型的Json字串,2.要轉型的資料結構類型)(回傳<T> T)
        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType()); //getType():取得底層資料結構的種類(回傳Type)

        Log.v(TAG, "map:" + map.toString());
        return map;
    }

    /*7.將javabean轉成List
     *@param:Object javabean => 預轉成List的javabean
     *@return:List<Object>
     * */
    public static<T>List<GetMoreResult> javaBeanToList(Object javabean) {
        Gson gson = new Gson();
        String json = gson.toJson(javabean);
        List<GetMoreResult> list = gson.fromJson(json, new TypeToken<List<Object>>() {
        }.getType());
        return list;
    }

    /* 8.將指定的字串Json,跟要查詢的節點名稱傳入回傳節點的值
     *@param: 1.String json => 要解析的Json字串
     *@param: 2.String param => 要查詢的節點
     *  */
    public static String getParaFromJson(String json, String param) {
        //parse(String json):將Json字串解析成為JsonTree(要解新的Json字串)(回傳JsonElement)
        //getAsJsonObject():取得JsonObject(回傳JsonObject);

        JsonElement jsonElement = new JsonParser().parse(json);//將Json字串解析成為JsonTree(要解新的Json字串)(回傳JsonElement)
        JsonObject jsonObject = jsonElement.getAsJsonObject();//取得JsonObject(回傳JsonObject);
        String value = jsonObject.get(param).toString();
        Log.v(TAG, "jsonElement:" + jsonElement.toString() + "/value:" + value);
        return null;
    }

    /* 9.解資料結構{data:[{}]}方法 return => String json
    *@param: 1.String body => response.body的資料結構
    *@param: 2.要取得的資料結構節點
    * */
    public static String getStringJson(String body, String param) {
        String json = null;
        try {
            JSONObject jsonObject = new JSONObject(body);
            String data = jsonObject.getString(param);
            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsobject = jsonArray.getJSONObject(i);
                json = jsobject.toString();
                Log.v(TAG,json);
            }
        } catch (JSONException e) {
            Log.v(TAG, "getStringJson => 錯誤:" + e.toString());
        }
        return json;
    }

}