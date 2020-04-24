package com.example.pddmanager.result;

import com.google.gson.annotations.SerializedName;

public class LoginResult extends ApiResult {
    @SerializedName("code")
    public String code;
    @SerializedName("access_id")
    public String accessId;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("relationId")
    public String relationId;
    @SerializedName("mch_status")
    public String mchStatus;
    @SerializedName("headimgurl")
    public String headImgurl;
    @SerializedName("y_password")
    public String yPassword;
    @SerializedName("wx_status")
    public String wxStatus;
    @SerializedName("message")
    public String message;

    public LoginResult() {
    }

    public LoginResult(String code, String accessId, String userName, String relationId, String mchStatus, String headImgurl, String yPassword, String wxStatus, String message) {
        this.code = code;
        this.accessId = accessId;
        this.userName = userName;
        this.relationId = relationId;
        this.mchStatus = mchStatus;
        this.headImgurl = headImgurl;
        this.yPassword = yPassword;
        this.wxStatus = wxStatus;
        this.message = message;
    }


}
