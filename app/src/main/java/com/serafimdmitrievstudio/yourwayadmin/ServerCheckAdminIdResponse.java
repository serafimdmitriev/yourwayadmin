package com.serafimdmitrievstudio.yourwayadmin;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Serafim on 19.02.2018.
 */

class ServerCheckAdminIdResponse {
    @Expose
    @SerializedName ("response")
    private JsonObject response;

    JsonObject getResponse() {
        return response;
    }

    /*
    private short exist;
    @Expose
    @SerializedName("newAdministrators")
    private JSONArray newAdministrators;
    @Expose
    @SerializedName("mapActions")
    @
    private JSONArray mapActions;




    short exist() {
        return exist;
    }

    JSONArray getNewAdministrators() {
        return newAdministrators;
    }

    JSONArray getMapActions() {
        return mapActions;
    }
    */
}
