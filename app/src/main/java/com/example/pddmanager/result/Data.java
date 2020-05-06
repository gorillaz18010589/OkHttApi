package com.example.pddmanager.result;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class Data {
//    data":{"user":{"headimgurl":null,"user_name":"\u6211","money":"43.00","score":"0.00","mobile":"13232236359","zhenzhu":"0.00","zhanghao":"13232236359","is_vip":"0","real_name":"\u674e\u9806\u9032","user_id":"z3595","relationId":null,"is_pay":false,"is_clock":0,"ysbflag":"1","coupon_num":0,"childs_num":"386","grade":"\u7ea7\u522b\uff1a\u4e1a\u52a1\u5458","adr_flag":0},"mch_id":0,"sys_role":[{"path":"\/pagesI\/member\/index","sys_name":"\u4f1a\u5458\u7cfb\u7edf","imgurl":"http:\/\/api1.xhdncppf.com\/upload\/Images\/app\/static\/img\/yao_back.png"},{"path":"\/pagesH\/my\/phone_chong","sys_name":"\u63a8\u8350\u529e\u5361","imgurl":"http:\/\/api1.xhdncppf.com\/upload\/Images\/app\/static\/img\/phone-chong.png"}],"mch_type":-1,"th":0,"dfk_num":0,"dfh_num":0,"dsh_num":0,"dpj_num":0,"logo":"https:\/\/zfbpic.oss-cn-beijing.aliyuncs.com\/image_8\/1542895065481.png","company":"\u81fb\u65b9\u4fbf\u5546\u57ce","collection_num":"0","footprint_num":"883"},"message":"\u6210\u529f"}
    @SerializedName("user")
    public User user;

    @SerializedName("mch_id")
    public int mchId;

    @SerializedName("sys_role")
    public List<SysRole> listHashMap;

    @SerializedName("mch_type")
    public int mchType;

    public int th;

    @SerializedName("dfk_num")
    public int dfkNum;

    @SerializedName("dfh_num")
    public int dfhNum;

    @SerializedName("dsh_num")
    public int dshNum;

    @SerializedName("dpj_num")
    public int dpjNum;

    public String logo;

    public String company;

    @SerializedName("collection_num")
    public String collectionNum;

    @SerializedName("footprint_num")
    public String footprintNum;

    public static class User {
        @SerializedName("headimgurl")
        public String headImgurl;

        @SerializedName("user_name")
        public String userName;

        public String money;

        public String score;

        public String mobile;

        public String zhenzhu;

        public String zhanghao;

        @SerializedName("is_vip")
        public String isVip;

        @SerializedName("real_name")
        public String realName;

        @SerializedName("user_id")
        public String userId;

        public String relationId;

        @SerializedName("is_pay")
        public boolean isPay;

        @SerializedName("is_clock")
        public String isClock;

        public String ysbflag;

        @SerializedName("coupon_num")
        public int couponNum;

        @SerializedName("childs_num")
        public String childsNum;

        public String grade;

        public int adr_flag;

    }

    public static class SysRole {

        public String path;

        @SerializedName("sys_name")
        public String sysName;

        @SerializedName("imgurl")
        public String imgUrl;
    }


}
