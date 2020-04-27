package com.example.pddmanager.result;

public class GetMoreResult extends ApiResult {
    private String code;
    private String data;
    private String id;
    private String name;
    private String yprice;
    private String price;
    private String imgurl;
    private String volume;

    public GetMoreResult(String code, String data, String id, String name, String yprice, String price, String imgurl, String volume) {
        this.code = code;
        this.data = data;
        this.id = id;
        this.name = name;
        this.yprice = yprice;
        this.price = price;
        this.imgurl = imgurl;
        this.volume = volume;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYprice() {
        return yprice;
    }

    public void setYprice(String yprice) {
        this.yprice = yprice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
