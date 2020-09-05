package com.serafimdmitrievstudio.yourwayadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ServerSimpleResponse {
    @SerializedName("response")
    @Expose

    private String response;

    String getResponse() {
        return response;
    }
}
