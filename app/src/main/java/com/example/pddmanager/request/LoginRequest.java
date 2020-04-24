package com.example.pddmanager.request;

import java.util.List;

public class LoginRequest extends ApiRequest{
    public String phone;
    public String password;
    public String cart_id;

    public LoginRequest(String phone, String password, String cart_id) {
        module = "app";
        action = "login";
        app = "login";
        this.phone = phone;
        this.password = password;
        this.cart_id = cart_id;
    }


}
