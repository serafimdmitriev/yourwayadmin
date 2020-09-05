package com.serafimdmitrievstudio.yourwayadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.JsonObject;


import java.net.URLDecoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class NewAdminSplash {
    private TextView authorizationSystemTextView = null;
    private TextView identificationNumberTextView = null;
    private TextView emailTextView = null;
    private TextView nameTextView = null;
    private TextView contactsTextView = null;
    private TextView messageTextView = null;
    private Button deleteButton = null;
    private Button saveButton = null;

    private View newAdminSplash = null;

    private NewAdminsLeftChecker newAdminsLeftChecker = null;

    private Integer requestId = null;

    NewAdminSplash (Context context, JsonObject object, final ServerApi serverApi, final Animation removeLeftward, final Animation shakeAnimation){
        LayoutInflater inflater;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        newAdminSplash =  inflater.inflate(R.layout.new_administrator, null);

        authorizationSystemTextView = (TextView) newAdminSplash.findViewById(R.id.authSystemTextView);
        identificationNumberTextView = (TextView) newAdminSplash.findViewById(R.id.idNumberTextView);
        emailTextView = (TextView) newAdminSplash.findViewById(R.id.emailTextView);
        nameTextView = (TextView) newAdminSplash.findViewById(R.id.nameTextView);
        contactsTextView = (TextView) newAdminSplash.findViewById(R.id.contactTextView);
        messageTextView = (TextView) newAdminSplash.findViewById(R.id.messageTextView);

        deleteButton = (Button) newAdminSplash.findViewById(R.id.deleteNewAdminButton);
        saveButton = (Button) newAdminSplash.findViewById(R.id.saveNewAdminButton);


        try {
            authorizationSystemTextView.setText(object.get("authorizationSystem").getAsString());
            identificationNumberTextView.setText(object.get("identificationNumber").getAsString());
            emailTextView.setText(object.get("email").getAsString());
            nameTextView.setText(URLDecoder.decode(object.get("name").getAsString(), "UTF-8"));
            contactsTextView.setText(URLDecoder.decode(object.get("contacts").getAsString(), "UTF-8"));
            messageTextView.setText(URLDecoder.decode(object.get("message").getAsString(), "UTF-8"));

            requestId = object.get("requestId").getAsInt();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptNewAdmin(false, serverApi, removeLeftward, shakeAnimation);
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptNewAdmin(true, serverApi, removeLeftward, shakeAnimation);
                }
            });

        } catch (Exception e) {
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }
    }

    void setNewAdminsLeftChecker(NewAdminsLeftChecker checker) {
        newAdminsLeftChecker = checker;
    }

    View getView() {
        return newAdminSplash;
    }

    private void acceptNewAdmin(boolean accept, ServerApi serverApi, final Animation removeLeftward, final Animation shakeAnimation) {
        try {
            short accessLevel = -1;
            if (((RadioButton) newAdminSplash.findViewById(R.id.radioButtonAccessLevel0)).isChecked()) accessLevel = 0;
            if (((RadioButton) newAdminSplash.findViewById(R.id.radioButtonAccessLevel1)).isChecked()) accessLevel = 1;
            if (accept && accessLevel == -1) {
                newAdminSplash.findViewById(R.id.radioGroupAccessLevel).startAnimation(shakeAnimation);
                return;
            }

            Call<ServerSimpleResponse> call = serverApi.acceptNewAdministrator("acceptNewAdministrator"
                    , requestId
                    , accept
                    , accessLevel
            );

            call.enqueue(new Callback<ServerSimpleResponse>() {
                @Override
                public void onResponse(Call<ServerSimpleResponse> call, Response<ServerSimpleResponse> response) {
                    if (response.body().getResponse().equals("success")) {
                        removeLeftward.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                newAdminSplash.setVisibility(View.GONE);
                                newAdminsLeftChecker.check();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        newAdminSplash.startAnimation(removeLeftward);
                    } else {
                        Log.write(response.body().getResponse());
                    }

                }

                @Override
                public void onFailure(Call<ServerSimpleResponse> call, Throwable t) {
                    Log.write(call.toString());
                    Log.write(t.toString());
                }
            });
        } catch (Exception e) {
            Log.write(e.getMessage());
            for (StackTraceElement el : e.getStackTrace()) {
                Log.write(el.toString());
            }
        }
    }
}
