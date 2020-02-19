package com.ewo.skripsi.api;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    // variable name should be same as in the json response from php
    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }

}
