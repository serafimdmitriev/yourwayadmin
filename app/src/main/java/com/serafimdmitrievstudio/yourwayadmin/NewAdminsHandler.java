package com.serafimdmitrievstudio.yourwayadmin;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;

import java.util.ArrayList;



class NewAdminsHandler {
    private ArrayList<NewAdminSplash> splashesArray;
    private RelativeLayout noNewAdmins;

    NewAdminsHandler(final Activity activity, LinearLayout newAdministratorsLinearLayout, JsonArray newAdministratorsJsonArray, final Animation shakeAnimation, final Animation removeLeftward, final ServerApi serverApi) {
        noNewAdmins = (RelativeLayout) activity.findViewById(R.id.noNewAdminsRelativeLayout);
        splashesArray = new ArrayList<>();

        if (newAdministratorsJsonArray.size() != 0) {

            noNewAdmins.setVisibility(View.GONE);

            for (int i = 0; i < newAdministratorsJsonArray.size(); i++) {
                final NewAdminSplash splash = new NewAdminSplash(activity
                        , newAdministratorsJsonArray.get(i).getAsJsonObject()
                        , serverApi
                        , removeLeftward
                        , shakeAnimation);

                splash.setNewAdminsLeftChecker(new NewAdminsLeftChecker() {
                    @Override
                    void check() {
                        checkLeftNewAdmins();
                    }
                });

                newAdministratorsLinearLayout.addView(splash.getView());

                splashesArray.add(splash);
            }
        }
    }

    private void checkLeftNewAdmins() {
        boolean left = true;
        for (int i = 0; i < splashesArray.size(); i++) {
            if (splashesArray.get(i).getView().getVisibility() != View.GONE) {
                left = false;
            }
        }
        if (left) noNewAdmins.setVisibility(View.VISIBLE);
    }
}
