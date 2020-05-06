package com.example.pddmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pddmanager.request.LoginRequest;
import com.example.pddmanager.result.Data;
import com.example.pddmanager.result.GetMoreResult;
import com.example.pddmanager.result.IndexUserResult;
import com.example.pddmanager.result.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<GetMoreResult> getMoreResultList;
    private LoginResult login;

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


        LoginRequest loginRequest = new LoginRequest("13232236359", "aaaa1111", "");
        Map<String, String> map = MyOkHttpApi.javaBeanToMap(loginRequest);

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
                        login = gson.fromJson(obj, LoginResult.class);
                        Log.v(TAG, "accessId:" + login.accessId);

                    } catch (IOException e) {
                        Log.v(TAG, "IOException:" + e.toString());
                    }
                }
            }
        });
    }

    //6.查詢Json字串中的節點欄位資料
    public void getParamFromJson(View view) {
        LoginRequest loginRequest = new LoginRequest("04111111", "123456", "aa");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequest);
        MyOkHttpApi.getParaFromJson(json, "phone");
    }


    public void getMore(View view) {
        final Map<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "index");
        map.put("app", "get_more");
        map.put("page", "0");
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String json = MyOkHttpApi.getStringJson(response.body().string(), "data");
                        Gson gson = new Gson();
                        GetMoreResult getMoreResult = gson.fromJson(json, GetMoreResult.class);
                        getMoreResultList = new ArrayList<>();
                        getMoreResultList.add(getMoreResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void API(View view) {


    }

    public void myAuth(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "user");
        map.put("app", "my_auth");

        map.put("access_id", login.accessId);
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        Log.v(TAG, "msg:" + response.body().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //取得UserId
    public void user(View view) {
        HashMap<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "user");
        map.put("app", "mch_banklist");
        map.put("access_id", login.accessId);
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        Log.v(TAG, "body:" + body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void getcon(View view) {
        Map map = new HashMap();
        map.put("timeid", "8");
        map.put("pageIndex", "10");
        map.put("pageSize", "30");
        map.put("sql", "sql");
        MyOkHttpApi.instance().doPostFormBody("https://shop.ljz789.com/index.php?module=app&action=seckill&m=list", map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        Log.v(TAG, "body:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void aplyauth_realname(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "user");
        map.put("app", "aplyauth_realname");
        map.put("access_id", login.accessId);
        map.put("real_name", "王小名");
        map.put("id_card", "中國銀行");
        map.put("my_phone", "13232236359");
        map.put("cardno", "123456");
        map.put("bankcode", "1");
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                try {
                    String body = response.body().string();
                    Log.v(TAG, "body:" + body);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void index1(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("module", "app");
        map.put("action", "index");
        map.put("app", "index1");
        map.put("access_id", login.accessId);
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        Log.v(TAG, "body:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void guided_graph(View view) {
        Map<String, String> map = new HashMap<>();
        map.put("app", "guided_graph");
        map.put("action", "index");
        map.put("module", "app");
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    try {
                        Log.v(TAG, "body:" + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void userlogin(View view) {
        final Map<String, String> map = new HashMap<>();

//        map.put("access_id",login.accessId);
//        map.put("module","app");
//        map.put("action","mch_user");
//        map.put("app","info");
        map.put("access_id", login.accessId);
        map.put("module", "app");
        map.put("action", "user");
        map.put("app", "index");

//        {"code":200,"data":{"user":{"headimgurl":null,"user_name":"\u6211","money":"43.00","score":"0.00","mobile":"13232236359","zhenzhu":"0.00","zhanghao":"13232236359","is_vip":"0","real_name":"\u674e\u9806\u9032","user_id":"z3595","relationId":null,"is_pay":false,"is_clock":0,"ysbflag":"1","coupon_num":0,"childs_num":"275","grade":"\u7ea7\u522b\uff1a\u4e1a\u52a1\u5458","adr_flag":0},"mch_id":0,"sys_role":[{"path":"\/pagesI\/member\/index","sys_name":"\u4f1a\u5458\u7cfb\u7edf","imgurl":"http:\/\/api1.xhdncppf.com\/upload\/Images\/app\/static\/img\/yao_back.png"},{"path":"\/pagesH\/my\/phone_chong","sys_name":"\u63a8\u8350\u529e\u5361","imgurl":"http:\/\/api1.xhdncppf.com\/upload\/Images\/app\/static\/img\/phone-chong.png"}],"mch_type":-1,"th":0,"dfk_num":0,"dfh_num":0,"dsh_num":0,"dpj_num":0,"logo":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/image_8\/1542895065481.png","company":"\u81fb\u65b9\u4fbf\u5546\u57ce","collection_num":"0","footprint_num":"798"},"message":"\u6210\u529f"}
        MyOkHttpApi.instance().doPostFormBody(url, map, new MyOkHttpApi.OkHttpCallBack() {
            @Override
            public void onFailure(IOException e) {

            }

            //            {"a":"100", "b":[{"b1":"b_value1","b2":"b_value2"},{"b1":"b_value1","b2":"b_value2"}]，"c":{"c1":"c_value1","c2":"c_value2"}}
            @Override
            public void onSuccess(Response response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {
                        String body = response.body().string();
                        Log.v(TAG,"body:" + body);
                        IndexUserResult indexUserResult = gson.fromJson(body, IndexUserResult.class);
                        Log.v(TAG, "code:" + indexUserResult.code + "/msg:" + indexUserResult.message);
                        String realName = indexUserResult.data.user.realName;
                        Log.v(TAG,"realName:" + realName);
                        List<Data.SysRole> list = indexUserResult.data.listHashMap;
                        for (Data.SysRole sysRole : list) {
                            Log.v(TAG, "sysRoleName:" + sysRole.sysName);
                        }
                    } catch (Exception e) {
                        Log.v(TAG, "Ex:" + e.toString());
                    }

                }

            }

        });
    }

    //ok
    private void parseData(String body) throws JSONException {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(body);
            for (int i = 0; i < jsonObject.length(); i++) {
                String data = jsonObject.getString("data");
                Log.v(TAG, "data:" + data);
                Data datas = gson.fromJson(data, Data.class);
                Log.v(TAG, "users:" + datas.user.realName);
                List<Data.SysRole> list = datas.listHashMap;
                for (Data.SysRole sysRole : list) {
                    Log.v(TAG, "sysRole:" + sysRole.sysName);
                }
            }
        } catch (Exception e) {
            Log.v(TAG, "Ex:" + e.toString());
        }
    }
}

//      try {
//        //{"code":200,"data":[{"id":"669","name":"\u53cc\u6e29\u63a7\u6dae\u70e4\u4e00\u4f53\u9505\u7535\u70e7\u70e4\u76d8\u7535\u70e4\u7089\u714e\u70e4\u6cb9\u70b8\u70e4\u8089\u9505\u7eb8\u4e0a\u70e4\u8089","yprice":"299.00","price":"199.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1569035739753.jpeg","volume":"29"},{"id":"817","name":"\u6e58\u6e58\u6c34\u9f99\u5934\u52a0\u70ed\u5668","yprice":"280.00","price":"199.99","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1573625661756.jpeg","volume":"9"},{"id":"573","name":"\u84dd\u6708\u4eae\u6d17\u8863\u6db28\u74f6*3kg","yprice":"390.00","price":"299.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1563347889594.png","volume":"9"},{"id":"208","name":"\u7537\u58eb\u81ea\u8c6a\u725b\u76ae\u8170\u5e26","yprice":"120.00","price":"96.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/15556403304.jpeg","volume":"7"},{"id":"265","name":"\u81ea\u6e05\u6d17\u91cf\u5b50\u78c1\u5316\u76f4\u996e\u6c34\u673a\u5c0f\u5206\u5b50\u56e2\u6c34\u80fd\u91cf\u6d3b\u6027\u6d3b\u5316\u9632\u57a2\u9664\u57a2\u51c0\u6c34\u5668","yprice":"1999.00","price":"1699.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1558075743798.jpeg","volume":"6"},{"id":"1372","name":"\u7f8e\u5f0f\u8f7b\u5962\u771f\u76ae\u6c99\u53d1\u5ba2\u5385\u540e\u73b0\u4ee3\u6e2f\u5f0f\u5927\u5c0f\u6237\u578b","yprice":"5000.00","price":"5000.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/app\/1587003856727.png","volume":"4"},{"id":"673","name":"\u5fae\u6c14\u6ce1\u6d01\u80a4\u5b9d\u9664\u6c2f\u6c90\u6d74\u51c0\u6c34\u5668\u8d1f\u6c27\u79bb\u5b50\u7f8e\u5bb9\u7f8e\u80a4\u6ce1\u6fa1SPA\u5fae\u7eb3\u7c73\u6c14\u6ce1\u673a","yprice":"2799.00","price":"1888.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1569045033110.jpeg","volume":"4"},{"id":"815","name":"\u5fb7\u56fd316\u4e0d\u9508\u94a2\u7092\u83dc\u9505\u4e0d\u7c98\u9505\u65e0\u6d82\u5c42\u5c11\u6cb9\u70df\u5bb6\u7528\u7535\u78c1\u7089\u71c3\u7164\u6c14\u7076\u4e13\u7528","yprice":"480.00","price":"399.99","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1573282303365.jpeg","volume":"3"},{"id":"1669","name":"\u8bbe\u8ba1\u591a\u8fb9\u5f62\u4e94\u5206\u8896\u7eaf\u8272\u77ed\u8896T\u6064\u5973\u590f","yprice":"480.00","price":"464.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1587554941944.jpeg","volume":"2"},{"id":"1665","name":"\u8d85\u706bcec\u4e5d\u5206\u4f11\u95f2\u8fd0\u52a8\u88e4\u7537\u97e9\u7248\u6f6e\u6d41\u590f\u5b63\u8584","yprice":"60.00","price":"60.00","imgurl":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/8\/0\/1587550707978.jpeg","volume":"2"}],"message":"\u64cd\u4f5c\u6210\u529f"}
//        //資料結構{data:[{}]}
//        // 先解JSONOBJECT拿到  =>data:[{}]
//        //在解JSONARRAY=> 拿到{id:669,name:双温控涮烤一体锅电烧烤盘电烤炉煎烤油炸烤肉锅纸上烤肉}這個Json{}物件
//        //就可以用Gson.formJson轉型成類別
//        String msg = response.body().string();
//        Log.v("hank",msg);
//        try {
//            JSONObject jsonObject = new JSONObject(msg);
//            String data = jsonObject.getString("data");
//            Log.v(TAG,"data:" + data);
//            JSONArray jsonArray = new JSONArray(data);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                Log.v(TAG, "jsonObject1:" + jsonObject1);
//                String json = jsonObject1.toString();
//                Gson gson = new Gson();
//                GetMoreResult getMoreResult = gson.fromJson(json,GetMoreResult.class);
//                String name = getMoreResult.getName();
//                Log.v(TAG,"name:" + name +"\n");
//            }
//            Log.v(TAG, "data:" + data);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
