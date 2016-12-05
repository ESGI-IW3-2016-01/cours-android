package com.ninebaguettes.antoine.myapplication;

import org.json.JSONObject;

public interface IHTTPRequestListener {

    void onSuccess(JSONObject jsonObject);

    void onFailure(JSONObject jsonObject);

}
